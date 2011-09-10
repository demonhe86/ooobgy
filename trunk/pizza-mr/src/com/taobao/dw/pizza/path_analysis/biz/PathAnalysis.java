package com.taobao.dw.pizza.path_analysis.biz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.taobao.dw.pizza.path_analysis.common.CommonFunction;
/**
 * @author lemie
 */
@SuppressWarnings( { "unused", "deprecation" })
public class PathAnalysis extends Configured implements Tool {
	
	public static class MapClass1 extends MapReduceBase implements
			Mapper<Writable, Text, Text, Text> {

		private Text word = new Text();
		private Text str = new Text();

		Map<String, String> invalidUserMap = new HashMap<String, String>();

		int array_length = 2500;
		Pattern[] rule = new Pattern[array_length];
		String[] node_path_array = new String[array_length];
		int count = 0;
		Pattern p = null;

		public void configure(JobConf jobIn) {
			super.configure(jobIn);
			FileSystem fs = null;
			FSDataInputStream in;
			BufferedReader bufread;
			String strLine;
			String strTrim;
			try {
				fs = FileSystem.get(jobIn);
				String pa = jobIn.get("invaliduser");
				Path inputPath = new Path(pa);
				if (!fs.exists(inputPath))
					throw new IOException("Input file not found");
				if (!fs.isFile(inputPath))
					throw new IOException("Input should be a file");
				in = fs.open(inputPath);
				bufread = new BufferedReader(new InputStreamReader(in));
				while ((strLine = bufread.readLine()) != null) {
					invalidUserMap.put(strLine, strLine);
				}
				in.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				fs = FileSystem.get(jobIn);
				String pa = jobIn.get("nodepath");
				Path inputPath = new Path(pa);
				if (!fs.exists(inputPath))
					throw new IOException("Input file not found");
				if (!fs.isFile(inputPath))
					throw new IOException("Input should be a file");
				in = fs.open(inputPath);
				bufread = new BufferedReader(new InputStreamReader(in));
				while ((strLine = bufread.readLine()) != null) {
					String[] strArray = strLine.split("\t", -1);
					if (strArray.length > 3) {
						try {
							p = Pattern.compile(strArray[3].trim(),
									Pattern.MULTILINE);
							rule[count] = p;
							node_path_array[count] = strArray[0];
							count++;
						} catch (Exception e) {
							continue;
						}
					}
				}
				in.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void map(Writable key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String line = value.toString().trim();
			String[] lineArray = line.split("\001", -1);
			if (lineArray.length != 8)
				return;
			String refer = lineArray[2];
			if (!refer.startsWith("/1.gif?"))
				return;
			String url = lineArray[6];
			//���ȵõ�refer��httpheader��������������url����
			String param = CommonFunction.getReferParam(refer, url);
			//url_with_param����ֶεĺ���Ϊ��url�������httpheader�в����ϳɵ�url
			String url_with_param = url + param;
			
			//�õ���ʵ��refer
			refer = CommonFunction.getDeCodeURL(CommonFunction
					.getRealReferFromRefer(CommonFunction.getDeCodeURL(refer)));
			
			//referΪ�ջ���http://��ͷ��Ϊ�����������Ķ�����
			if(!"".equals(refer) && !refer.startsWith("http://")) return;
			String logtime = lineArray[0];
			String mid = lineArray[3];
			String uid = lineArray[4];
			String is_user = lineArray[7];
			String sid = lineArray[5];
			if (invalidUserMap.get(uid) != null
					|| invalidUserMap.get(mid) != null)
				return;
			String url_node_feature = "";
			String refer_node_feature = "";
			for (int i = 0; i < count; i++) {
				if (rule[i].matcher(url_with_param).find()) {
					url_node_feature +=  "," + node_path_array[i];
				}
				if (rule[i].matcher(refer).find()) {
					refer_node_feature +=  "," + node_path_array[i];
				}
			}
			if(url_node_feature.startsWith(",")) url_node_feature = url_node_feature.substring(1);
			if(refer_node_feature.startsWith(",")) refer_node_feature = refer_node_feature.substring(1);
			
			//�����-1������������˼����ʾurlƥ�䲻����������
			if("".equals(url_node_feature)) url_node_feature = "-1";
			
			//������refer���ڣ���ƥ�䲻�ϵ�����£���Ϊ-1
			if("".equals(refer_node_feature) && refer.length() > 1) refer_node_feature = "-1";
			String id = "-".equals(uid) ? mid : uid;
			
			word.set(id + "\"" + logtime);
			str.set(uid + "\"" + refer + "\"" + url + "\"" + logtime + "\""
					+ url_node_feature + "\"" + is_user + "\"" + mid + "\"" + refer_node_feature + "\"" + url_with_param);
			output.collect(word, str);
		}
	}

	public static class ReduceClass1 extends MapReduceBase implements
			Reducer<Text, Text, Text, NullWritable> {
		private Text word = new Text();
		private NullWritable str;

		@Override
		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, NullWritable> output, Reporter reporter)
				throws IOException {
			String id = key.toString().split("\"", -1)[0];//����mid��uid
			List<LogBean> list = new ArrayList<LogBean>();
			int lastSize = -1;
			while (values.hasNext()) {
				String[] lineArray = values.next().toString().split("\"", -1);
				if (lineArray.length != 9)
					continue;
				String refer = lineArray[1];
				String url = lineArray[2];
				String logtime = lineArray[3];
				String url_node_feature = lineArray[4];
				String isUser = lineArray[5];
				String mid = lineArray[6];
				String refer_node_feature = lineArray[7];
				String url_with_param = lineArray[8];
				LogBean logBeanRefer = new LogBean(mid, isUser, refer_node_feature,refer,"1",logtime,url_with_param);
				LogBean logBeanUrl   = new LogBean(mid, isUser, url_node_feature,  url,  "0",logtime,url_with_param);
				
				//referΪ�յ�����£�������Ϊ�û�����ˢ��ҳ���ֱ�Ӵ�ҳ��
				if(CommonFunction.isBlank(refer)){
					lastSize = list.lastIndexOf(logBeanUrl);
					//referΪ�գ�����url���ҵ���ʷ��¼:��ʱ�����Ϊҳ��ˢ�£�����Ҫ���url�ڵ㣬Ҳ����Ҫ������һ�ڵ�
					if(lastSize != -1) {
						//list.get(lastSize).getNextPaths().add(logBeanUrl);
						//logBeanUrl.setPrevious(list.get(lastSize));
						//list.add(logBeanUrl);
					}
					//referΪ�գ�url�Ҳ�����ʷ��¼:��ʱ�����Ϊ���ղؼл��������ط�ֱ�ӽ���ҳ�档��ʱ��url�ڵ�Ϊ��ʼ�ڵ�
					else{
						list.add(logBeanUrl);
					}
					
				}
				//refer��Ϊ�յ������
				else{
					lastSize = list.lastIndexOf(logBeanRefer);
					//refer��Ϊ�գ�refer���ҵ���ʷ��¼����refer����ʷ���һ��refer�Խӣ����url�ڵ�
					if(lastSize != -1) {
						list.get(lastSize).getNextPaths().add(logBeanUrl);
						logBeanUrl.setPrevious(list.get(lastSize));
						list.add(logBeanUrl);
					}
					//�Ҳ���refer�������
					else{
						//���refer�ڵ㣬��ʱrefer�ڵ�Ϊ��ʼ�ڵ�
						logBeanRefer.getNextPaths().add(logBeanUrl);
						list.add(logBeanRefer);
						//���url�ڵ㣬��Ϊ��refer���ӽڵ�
						logBeanUrl.setPrevious(logBeanRefer);
						list.add(logBeanUrl);
					}
				}
			}
			/*----------whileѭ�����----------------*/
			/**
			 * ����õ���list����Ϊ˫��ѭ���б���ΪΪ˫��ѭ���б����Ϊ�ã�
			 * ���һ��logBean.getNextPaths()Ϊ�գ���˵����ΪҶ�ӽڵ� ��ͨ����ʼ�ڵ�logBean.getPrevious()���ϵݹ�������ܵõ����в��·��
			 */
			String result = "";
			String middle_str = "{\"uid_or_mid\":\"" + id + "\",";
			for(LogBean logBean : list){
				//��Ҷ�ӽڵ㿪ʼ���ϵݹ����
				if(logBean.getNextPaths().size() > 0) continue;
				result = CommonFunction.reverseString(logBean,0);
				word.set(middle_str + result + "}");
				output.collect(word, str);
			}
			
		}
	}
	
	
	
	@Override
	public int run(String[] args) throws Exception {
		String strConfigFile = new String("");
		String strJobId = new String("");
		String strPlanTime = new String("");
		String strRunDate = "";
		List<String> other_args = new ArrayList<String>();
		int numMapTasks = 0;
		int numReduceTasks = 0;
		String strTmpOutPath = "";
		String strOutputPath = "";
		String strUrlFile = "";
		String pa = "";
		String invaliduser = "";
		String nodepath = "";
		for (int i = 0; i < args.length; ++i) {
			try {
				if ("-m".equals(args[i])) {
					numMapTasks = Integer.parseInt(args[++i]);
				} else if ("-t".equals(args[i])) {
					strTmpOutPath = args[++i];
				} else if ("-d".equals(args[i])) {
					strRunDate = args[++i];
				} else if ("-o".equals(args[i])) {
					strOutputPath = args[++i];
				} else if ("-r".equals(args[i])) {
					numReduceTasks = Integer.parseInt(args[++i]);
					// } else if ("-u".equals(args[i])) {
					// strUrlFile = args[++i];
				} else if ("-c".equals(args[i])) {
					strConfigFile = args[++i];
				} else if ("-jid".equals(args[i])) {
					strJobId = args[++i];
				} else if ("-pt".equals(args[i])) {
					strPlanTime = args[++i];
				} else if ("-pa".equals(args[i])) {
					pa = args[++i];
				} else if ("-invaliduser".equals(args[i])) {
					invaliduser = args[++i];
				} else if ("-nodepath".equals(args[i])) {
					nodepath = args[++i];
				} else {
					other_args.add(args[i]);
				}
			} catch (NumberFormatException except) {
				System.out.println("ERROR: Integer expected instead of "
						+ args[i]);
				return printUsage();
			} catch (ArrayIndexOutOfBoundsException except) {
				System.out.println("ERROR: Required parameter missing from "
						+ args[i - 1]);
				return printUsage();
			}
		}
		if (other_args.size() != 1) {
			System.out.println("ERROR: Wrong number of parameters: "
					+ other_args.size() + " instead of 1.");
			return printUsage();
		}

		String inputPath = other_args.get(0);
		Path IutputPath = new Path(inputPath);

		Path OutputPath1 = new Path(strTmpOutPath + "/out1");
		Path OutputPath2 = new Path(strTmpOutPath + "/out2");
		Path OutputPath = new Path(strOutputPath);

		FileSystem fs;

		/************ Job1 ***********/
		JobConf jobConf1 = new JobConf(getConf(), PathAnalysis.class);
		jobConf1.setJobName("PathAnalysis_1");
		if (numMapTasks != 0)
			jobConf1.setNumMapTasks(numMapTasks);
		// if (numReduceTasks != 0)
		jobConf1.setNumReduceTasks(1000);

		jobConf1.setMapperClass(MapClass1.class);
		jobConf1.setReducerClass(ReduceClass1.class);
		jobConf1.setInputFormat(SequenceFileInputFormat.class);
		
		jobConf1.set("mapred.map.tasks", "2500");

		jobConf1.setPartitionerClass(CommonFunction.FirstPartitioner.class);
		jobConf1
				.setOutputValueGroupingComparator(CommonFunction.FirstGroupingComparator.class);

		jobConf1.setMapOutputKeyClass(Text.class);
		jobConf1.setMapOutputValueClass(Text.class);

		jobConf1.set("invaliduser", invaliduser);
		jobConf1.set("nodepath", nodepath);

		fs = FileSystem.get(jobConf1);
		fs.delete(OutputPath, true);
		SequenceFileInputFormat.setInputPaths(jobConf1, IutputPath);
		SequenceFileOutputFormat.setOutputPath(jobConf1, OutputPath);
		JobClient.runJob(jobConf1);

		return 0;
	}

	static int printUsage() {
		System.out.println("PathAnalysis <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new PathAnalysis(), args);
		System.exit(res);
	}
}
