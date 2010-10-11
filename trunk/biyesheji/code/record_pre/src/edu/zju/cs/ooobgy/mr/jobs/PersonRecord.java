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
 * ��һ������PersonNode����Ϣ 
 * ����format��
 * PersonId^A���д���^A����ʱ��^A��������^A���д���^A����ʱ��^A��������^Aͨ������^A˫����ϵ����^A�ۺ�Ȩֵ 
 * Ȩֵ��ʽ weight = (����Ȩֵ/10+��������*50)*1 + (����Ȩֵ/10+��������*50)*0.8 
 * ����ȡ��
 * 
 * @author ������
 * @created 2010-10-11
 */
public class PersonRecord implements HadoopJob {
	/**
	 * Ȩֵ��ʽ weight = (����Ȩֵ/10+��������*50)*1.0 + (����Ȩֵ/10+��������*50)*0.8 
	 * ����ȡ��
	 * 
	 * @return weight
	 */
	public static int weightEvaluate(int callPcnt, int callWeight, int recPcnt,
			int recWeight) {
		double weight;

		weight = (callWeight / 10.0 + callPcnt * 50) * 1.0
				+ (recWeight / 10.0 + recPcnt * 50) * 0.8;

		return (int) weight;
	}

	public static class TopMapper extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, Text> {

		@Override
		public void configure(JobConf job) {
			super.configure(job);
		}

		/**
		 * flag = 1�����м�¼
		 * flag = 0�����м�¼
		 */
		@Override
		public void map(LongWritable key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String line = value.toString();
			String[] items = line.split(KQConst.COMMON_SPLIT);
			String caller = items[0];
			String receiver = items[1];
			String cnt = items[2];
			String time = items[3];
			String weight = items[4];
			
			StringBuilder calRecBuilder = new StringBuilder();
			calRecBuilder.append("1").append(KQConst.COMMON_SPLIT);
			calRecBuilder.append(receiver).append(KQConst.COMMON_SPLIT);
			calRecBuilder.append(cnt).append(KQConst.COMMON_SPLIT);
			calRecBuilder.append(time).append(KQConst.COMMON_SPLIT);
			calRecBuilder.append(weight);
			
			StringBuilder recRecBuilder = new StringBuilder();
			recRecBuilder.append("0").append(KQConst.COMMON_SPLIT);
			recRecBuilder.append(caller).append(KQConst.COMMON_SPLIT);
			recRecBuilder.append(cnt).append(KQConst.COMMON_SPLIT);
			recRecBuilder.append(time).append(KQConst.COMMON_SPLIT);
			recRecBuilder.append(weight);
			
			output.collect(new Text(caller), new Text(calRecBuilder.toString()));
			output.collect(new Text(receiver), new Text(recRecBuilder.toString()));	
		}
	}

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
		}
}
	
	
	@Override
	public void configJob(JobConf conf) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Mapper> getMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Reducer> getReducer() {
		// TODO Auto-generated method stub
		return null;
	}

}
