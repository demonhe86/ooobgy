package com.taobao.dw.pizza.path_analysis.mr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.json.JSONException;
import org.json.JSONObject;

import com.taobao.dw.pizza.path_analysis.core.PizzaConst;
import com.taobao.dw.pizza.path_analysis.core.algo.InvertedListReader;
import com.taobao.dw.pizza.path_analysis.core.algo.PizzaJSON;
import com.taobao.dw.pizza.path_analysis.core.algo.PathTraceUtils;
import com.taobao.dw.pizza.path_analysis.core.pojo.AtomTrace;
import com.taobao.dw.pizza.path_analysis.core.pojo.CompositePath;
import com.taobao.dw.pizza.path_analysis.core.pojo.CompositeTrace;
import com.taobao.dw.pizza.path_analysis.core.pojo.InvertedList;

/**
 * ·��ƥ��
 * 
 * @author ����
 * @modified: ������ 2011��9��11��11:04:49
 * @refactored: ������ 2011��9��12��18:29:09
 */
public class PathMatcher extends Configured implements Tool {
	/**
	 * ����Ϊ���û�·��λ�ڵ�9λ��json�ļ�������֮��Ϊ�� userRoute: a,b,c,d,e,f,g
	 * 
	 * ԭ��·��ƥ������Ϊ pathNodePairs: a+b: <p1,c>, <p3,e>, <p5,h>... b+c: <p1,c>,
	 * <p3,e>, <p5,h>... c+d: <p1,c>, <p3,e>, <p5,h>... a-c: <p2,c>, <p3,e>,
	 * <p5,h>... a-e: <p1,c>, <p3,e>, <p5,h>... a-f: <p1,c>, <p3,e>, <p5,h>...
	 * 
	 * ������Ϊ�켣��ϣ�����ֻ���Ĵ��ܹ�׷�ݵ������ƥ��·���� a+b+c+d: <p1,e> a-c-h: <p2,o> a-f+g:
	 * <p3,i>
	 * 
	 * 3��·��
	 */
	public static class PmMapper extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, Text> {
		private final InvertedList invertedList = new InvertedList();
		private Text key = new Text();
		private Text value = new Text();

		private Map<String, JSONObject> nodeFeatureAttrs;
		private String[] rawTraces;
		private Map<String, JSONObject> nodeIdsAttrs;

		@Override
		public void configure(JobConf job) {
			super.configure(job);
			try {
				readFromHdfs(job);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			// System.out.println("InvertedList:" + this.invertedList);
		}

		private void read(FileSystem fs, Path path) throws IOException {
			FileStatus[] fileStatus = fs.listStatus(path);
			for (FileStatus status : fileStatus) {
				Path catPath = status.getPath();
				BufferedReader fis;
				FSDataInputStream in = fs.open(catPath);
				fis = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				InvertedListReader ilr = new InvertedListReader();
				this.invertedList.addAll(ilr.readInvertedList(fis));
				fis.close();
			}
			this.invertedList.rebuildHeadNodeIndex();
		}

		private void readFromHdfs(JobConf jobConf) throws IOException {
			FileSystem fs = FileSystem.get(jobConf);
			try {
				String invertedListPath = jobConf.get("inverted.list");
				if (invertedListPath == null || invertedListPath.length() <= 0) {
					throw new IOException("Input path is empty");
				}

				Path inputPath = new Path(invertedListPath);
				read(fs, inputPath);
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}

		@Override
		public void map(LongWritable key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			try {
				nodeFeatureAttrs = PizzaJSON.parseUserRoute(value.toString());
				rawTraces = PathTraceUtils.extractTraces(nodeFeatureAttrs
						.keySet());
				nodeIdsAttrs = PathTraceUtils
						.rebuildNodeAttrs(nodeFeatureAttrs);
			} catch (Throwable e) {
				System.err.println("Map 1:" + e);
				System.err.println("Error format line:" + value.toString());
				return;
			}

			if (nodeFeatureAttrs.size() == 0 || rawTraces.length == 0) {
				return;
			}

			// System.out.println("NodeFeatureAttrs:"
			// +nodeFeatureAttrs.keySet());
			// System.out.println("RawTraces:" + Arrays.asList(rawTraces));

			for (String rawTrace : rawTraces) {
				try {
					// ����û��켣Ϊԭ�ӹ켣
					List<AtomTrace> ats = PathTraceUtils.splitTrace(rawTrace);
					if (ats.size() <= 0)
						return;

					// �ӹ���õĵ��ű��У���ԭ�ӹ켣��Ӧ��·��id����һ�ڵ�id�������
					for (AtomTrace at : ats) {
						invertedList.match(at);
					}

					// �ϲ�ԭ�ӹ켣Ϊ��Ϲ켣,�����
					List<CompositeTrace> cts = PathTraceUtils.mergeTrace(ats);
					outputCompositeTraces(cts, nodeIdsAttrs, output);
					// �ھ���µ��׽ڵ�LHP�������LHP
					Set<String[]> lonelyPaths = PathTraceUtils.matchLonelyHeadNode(invertedList, rawTrace, cts);
					outputLHPs(lonelyPaths, nodeIdsAttrs, output);
				} catch (Throwable t) {
					System.err.println("Map 2" + t);
					t.printStackTrace();
				}
			}
		}

		private void outputLHPs(Set<String[]> lonelyPaths,
				Map<String, JSONObject> nodeAttrs,
				OutputCollector<Text, Text> output) throws JSONException, IOException {
			if (lonelyPaths == null || lonelyPaths.size() < 1 || nodeAttrs == null || nodeAttrs.size() < 1) {
				return;
			}
			
			String uid = nodeAttrs.values().iterator().next().getString("uid");
			StringBuilder sb;
			for (String[] lhp : lonelyPaths) {
				if (lhp.length != 5) {
					continue;
				}
				//LHP:[(1,p4,20,22,2),(1,p2,5,4,2)]
				sb = new StringBuilder();
				sb.append(uid).append(PizzaConst.SPLIT);//user_id
				sb.append(lhp[1]).append(PizzaConst.SPLIT);//path_key
				sb.append(lhp[0]).append(PizzaConst.SPLIT);//path_type_id
				sb.append(lhp[0]).append(PizzaConst.SPLIT);//first_node_id
				sb.append(lhp[3]).append(PizzaConst.SPLIT);//last_node_id
				sb.append(lhp[2]).append(PizzaConst.SPLIT);//next_node_id
				sb.append(lhp[0]).append(PizzaConst.SPLIT);//node_id
				sb.append(true).append(PizzaConst.SPLIT);//is_first_node
				sb.append("-1").append(PizzaConst.SPLIT);//pre_node_id  //��LHP��Ҳû���ṩ����ڵ���Ϣ
				sb.append(false).append(PizzaConst.SPLIT);//is_last_node
				sb.append(lhp[4]).append(PizzaConst.SPLIT);//next_outer_node  //��CT��ͬ��LHP�ṩ�������ڵ���Ϣ
				
				JSONObject node = nodeAttrs.get(lhp[0]);
				JSONObject cloneNode = new JSONObject();
				for (String key : concernKeys) {
					cloneNode.put(key, node.get(key));
				}

				key.set(sb.toString());
				value.set(cloneNode.toString());//node_feature

				output.collect(key, value);
			}
			
		}

		/**
		 * 
		 * @param cts
		 *            ������ 86+88-91 {-662107825=(86+88-91):-662107825,13,86,1,
		 *            -1442201463=(86+88-91):-1442201463,91,86,7,
		 *            -1898552851=(86+88-91):-1898552851,141,86,3}
		 * @param nodeMap
		 *            ������
		 *            {52={"uid":"107188702","isUser":"1","nodeFeature":"52",
		 *            "logTime"
		 *            :"20110619111834","mid":"5618641004102355756","isRefer"
		 *            :"1","url":
		 *            "http://s.taobao.com/search?q=��ɹ�� ���� ͸��&keyword=&commend=all&ssid=s5-e&search_type=item&atype=&tracelog=&sourceId=tb.index"
		 *            },
		 *            40={"uid":"107188702","isUser":"1","nodeFeature":"40","logTime"
		 *            :"20110619112150","mid":"5618641004102355756","isRefer":
		 *            "0"
		 *            ,"url":"http://item.taobao.com/item.htm?id=9519056136"}}
		 * @param output
		 * 
		 * @throws JSONException
		 * @throws IOException
		 */

		private String[] concernKeys = new String[] { "nodeFeature", "isUser",
				"mid" };

		/**
		 * ������·��
		 * @param cts
		 * @param nodeAttrs
		 * @param output
		 * @throws JSONException
		 * @throws IOException
		 */
		//TODO:��������ֱ��������·���и����⡣����һЩ�켣����Դ�ڵ�������ڵ����Ϣ���ܻᶪʧ�����Ƿ���Ҫ����
		//���磬���Ƕ���·��1+2+3��ĳ�û�����켣101,1,2,105��
		//�����ڵ��߼����ڽڵ�1�ϣ����ǲ�֪����Դ��101�ڵ㣬ֻ֪����"-1"��
		//�ڽڵ�2�ϣ����ǲ�֪�������������ڵ�105��ֻ֪����"-1"��
		private void outputCompositeTraces(List<CompositeTrace> cts,
				Map<String, JSONObject> nodeAttrs,
				OutputCollector<Text, Text> output) throws JSONException,
				IOException {
			if (cts == null || cts.size() <= 0 || nodeAttrs.size() <= 0) {
				// System.err.println("Invalid CompositeTrace or NodeMap. Nothing will be output!");
				return;
			}
			// System.out.println("Composite Traces: " + cts);

			String uid = nodeAttrs.values().iterator().next().getString("uid");
			for (CompositeTrace ct : cts) {
				for (CompositePath cp : ct.cps.values()) {
					StringBuilder sb1 = new StringBuilder();

					sb1.append(uid).append(PizzaConst.SPLIT);
					sb1.append(cp.pathId).append(PizzaConst.SPLIT);
					sb1.append(cp.headNodeId).append(PizzaConst.SPLIT);
					sb1.append(cp.headNodeId).append(PizzaConst.SPLIT);
					sb1.append(cp.tailNodeId).append(PizzaConst.SPLIT);
					sb1.append(cp.nextNodeId).append(PizzaConst.SPLIT);

					String[] nodeIds = PathTraceUtils.splitPathToNodes(cp
							.getPathKey());

					for (int i = 0, j = nodeIds.length; i < j; i++) {

						StringBuilder sb2 = new StringBuilder(sb1.toString());
						sb2.append(nodeIds[i]).append(PizzaConst.SPLIT);

						// �Ƿ�Ϊ�켣��һ�����
						if (i == 0) {
							sb2.append(true);
							sb2.append(PizzaConst.SPLIT);
							sb2.append(PizzaConst.INVALID_NODE_ID);
						} else {
							sb2.append(false);
							sb2.append(PizzaConst.SPLIT);
							sb2.append(nodeIds[i - 1]);
						}

						sb2.append(PizzaConst.SPLIT);

						// �Ƿ�Ϊ�켣�����һ���ڵ�
						if (i == j - 1) {
							sb2.append(true);
							sb2.append(PizzaConst.SPLIT);
							sb2.append(PizzaConst.INVALID_NODE_ID);
						} else {
							sb2.append(false);
							sb2.append(PizzaConst.SPLIT);
							sb2.append(nodeIds[i + 1]);
						}

						JSONObject node = nodeAttrs.get(nodeIds[i]);
						JSONObject cloneNode = new JSONObject();
						for (String key : concernKeys) {
							cloneNode.put(key, node.get(key));
						}

						key.set(sb2.toString());
						value.set(cloneNode.toString());

						output.collect(key, value);
					}

				}
			}

		}

	}

	/**
	 * 
	 */
	public static class PmReducer extends MapReduceBase implements
			Reducer<Text, Text, LongWritable, Text> {
		private LongWritable l = new LongWritable();
		private Set<String> vs = new HashSet<String>();
		private Text result = new Text();

		@Override
		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<LongWritable, Text> output, Reporter reporter)
				throws IOException {
			while (values.hasNext()) {
				vs.add(values.next().toString());
			}
			for (String v : vs) {
				result.set(key.toString() + PizzaConst.SPLIT + v);
				output.collect(l, result);
			}
			vs.clear();
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		System.out.println(Arrays.asList(args));
		if (args.length < 2) {
			System.err.println("Usage: InvertedListBuilder <in> <out>");
			System.exit(2);
		}

		String invertedList = "";
		for (int i = 0; i < args.length; ++i) {
			if ("-inverted.list".equals(args[i])) {
				invertedList = args[i + 1];
			}
		}

		try {
			JobConf pizzaJob = (JobConf) getConf();

			pizzaJob.set("inverted.list", invertedList);

			pizzaJob.setJobName("PathMatcher");

			pizzaJob.setJarByClass(PathMatcher.class);

			pizzaJob.setMapperClass(PmMapper.class);
			// pizzaJob.setNumReduceTasks(0);
			pizzaJob.setReducerClass(PmReducer.class);

			pizzaJob.setMapOutputKeyClass(Text.class);
			pizzaJob.setMapOutputValueClass(Text.class);

			pizzaJob.setOutputKeyClass(LongWritable.class);
			pizzaJob.setOutputValueClass(Text.class);

			FileInputFormat.addInputPath(pizzaJob, new Path(args[0]));

			FileSystem fstm = FileSystem.get(pizzaJob);
			Path outDir = new Path(args[1]);
			fstm.delete(outDir, true);

			pizzaJob.setOutputFormat(SequenceFileOutputFormat.class);

			SequenceFileOutputFormat.setCompressOutput(pizzaJob, true);
			SequenceFileOutputFormat.setOutputCompressionType(pizzaJob,
					CompressionType.BLOCK);
			SequenceFileOutputFormat.getOutputCompressorClass(pizzaJob,
					GzipCodec.class);

			SequenceFileOutputFormat.setOutputPath(pizzaJob, new Path(args[1]));

			JobClient.runJob(pizzaJob);

			return 0;
		} catch (Throwable t) {
			t.printStackTrace();
			return 2;
		}
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new JobConf(), new PathMatcher(), args);
		System.exit(res);
	}
}
