package edu.zju.cs.ooobgy.mr.jobs;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import edu.zju.cs.ooobgy.common.KQConst;
import edu.zju.cs.ooobgy.mr.common.HadoopJob;

/**
 * ��һ��������֮���ͨ��������ͨ��ʱ�䣬
 * ��һȨֵ output_format: 
 * ����^A����^Aͨ������^Aͨ����ʱ�䣨�룩^A��һ��Ȩֵ
 * Ȩֵ���㹫ʽ weight = 60 + (��1�����ڵ�����)/1 + (��2�����ڵ�����)/2 + ...
 * ����ȡ��
 * @author ������
 * @created 2010-10-9
 */
public class WeightRecord implements HadoopJob {

	/**
	 * Ȩֵ���㹫ʽ weight = 60 + (��1�����ڵ�����)/1 + (��2�����ڵ�����)/2 + ...
	 * ����ȡ��
	 * @return weight
	 */
	public static int weightEvaluate(int time) {
		int weight = 60;
		int min = 1;
		while (time > 0) {
			int tail = time;
			time -= 60;
			if (time > 0) {
				tail = 60;
			}
			weight = weight + tail / min;
			min++;
		}

		return weight;
	}

	public static class TopMapper extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, Text> {

		@Override
		public void configure(JobConf job) {
			super.configure(job);
		}

		@Override
		public void map(LongWritable key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String line = value.toString();
			String[] items = line.split(KQConst.COMMON_SPLIT);
			String caller = items[2];
			String receiver = items[3];
			String time = items[4];

			StringBuilder keyBuilder = new StringBuilder();
			keyBuilder.append(caller).append(KQConst.COMMON_SPLIT);
			keyBuilder.append(receiver);
			StringBuilder valueBuilder = new StringBuilder();
			valueBuilder.append(time);

			output.collect(new Text(keyBuilder.toString()), new Text(
					valueBuilder.toString()));
		}

	}

	/**
	 * ͳ��ͨ��������ͨ��ʱ���Ȩֵ
	 * 
	 * @author ������
	 * @created 2010-10-9
	 */
	public static class TopReducer extends MapReduceBase implements
			Reducer<Text, Text, Text, Text> {
		
		@Override
		public void configure(JobConf job) {
			super.configure(job);
		}
		
		@Override
		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			int callCount = 0;
			int totalTime = 0;
			int weight = 0;
			while (values.hasNext()) {
				try {
					int time = Integer.parseInt(values.next().toString());
					callCount++;
					totalTime += time;
					weight += weightEvaluate(time);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			StringBuilder keyBuilder = new StringBuilder();
			keyBuilder.append(key.toString()).append(KQConst.COMMON_SPLIT);
			keyBuilder.append(Integer.toString(callCount)).append(KQConst.COMMON_SPLIT);
			keyBuilder.append(Integer.toString(totalTime)).append(KQConst.COMMON_SPLIT);
			keyBuilder.append(Integer.toString(weight));
			output.collect(new Text(keyBuilder.toString()), null);
		}
	}

	@Override
	public void configJob(JobConf conf) {
		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Text.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Mapper> getMapper() {
		return TopMapper.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Reducer> getReducer() {
		return TopReducer.class;
	}

}
