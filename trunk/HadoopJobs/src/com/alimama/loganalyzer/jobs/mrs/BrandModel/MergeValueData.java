package com.alimama.loganalyzer.jobs.mrs.BrandModel;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.lib.HashPartitioner;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import com.alimama.loganalyzer.common.AbstractProcessor;

/*
 * 	����base_value�� (MergeCatPV������ļ�)
 */
public class MergeValueData extends AbstractProcessor {
	public static final String SPLIT = "\u0001";
	public static final String OUTTITLE = "Merge3";
	
	public static class Map extends MapReduceBase implements
	Mapper<LongWritable, Text, Text, Text> {
		
		public void map(LongWritable key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
		throws IOException {
			String line = value.toString();
			if( line.startsWith("Merge2") ) {// (MergeCatPV������ļ�)
				String line1 = value.toString();
				String[] sub = line1.split(SPLIT);
				if( sub.length >= 8 ) {
					String value2 = sub[1] + SPLIT + sub[2] + SPLIT + sub[3] + SPLIT + 
						sub[4]+ SPLIT + sub[5] + SPLIT + sub[6] + SPLIT + sub[7];
					output.collect(new Text(sub[4]+SPLIT+"B"), new Text(value2));
				}
			}
			else {// base_value
				String line1 = new String(value.getBytes(),0,value.getLength(),"GBK");
				String[] sub2 = line1.split(SPLIT);
				if( sub2.length >= 2)
					output.collect(new Text(sub2[0]+SPLIT+"A"), new Text(sub2[1]));
			}
		}
	}
	
	public static class Reduce extends MapReduceBase implements
	Reducer<Text, Text, Text, Text> {
		
		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, Text> output, Reporter reporter)
		throws IOException {
			String value_data = "";
			boolean bUse = false;
			
			while (values.hasNext()) {
				String line = values.next().toString();
				if( line.contains(SPLIT) == false )
				{
					bUse = true;
					value_data = line;
				}
				else if (bUse)
				{
					String[] sub = line.split(SPLIT);
					if(sub.length==7) {
					/*
					 * category_id, property_id, property_name, 
					 * value_id, value_data,
					 * parent_value_id, parent_property_id, status 
					 */
					String value = OUTTITLE + SPLIT + sub[0] + SPLIT + sub[1] + SPLIT + sub[2] + SPLIT + 
						sub[3] + SPLIT + value_data + SPLIT + sub[4] + SPLIT + sub[5] + SPLIT + sub[6];
					output.collect(new Text(value), null);
					}
				}
			}
		}
	}
	
	public Class getMapper() {
		return Map.class;
	}
	
	public Class getReducer() {
		return Reduce.class;
	}
	
	public static class PKPartitioner<K2, V2> //���Ʒ���Ľڵ�
		extends HashPartitioner<K2, V2> {
		public PKPartitioner() {
		}
		
		public int getPartition(K2 key, V2 value, int numReduceTasks) {
			return (key.toString().split(SPLIT)[0].hashCode() & Integer.MAX_VALUE) % numReduceTasks;
		}	
	}
	
	public static class PVComparator extends WritableComparator { //key����
		public PVComparator() {
			super(Text.class, true);
		}

		private int compareStr(String a, String b) {
			String aTokens = a.split(SPLIT)[0];
			String bTokens = b.split(SPLIT)[0];
			//throw new RuntimeException("group is : " + a + "----" + b);
			return aTokens.compareTo(bTokens);
		}

		public int compare(Object a, Object b) {

			String aStr = a.toString();
			String bStr = b.toString();
			int i = compareStr(aStr, bStr);
			return i;
		}

		public int compare(WritableComparable a, WritableComparable b) {
			String aStr = a.toString();
			String bStr = b.toString();
			return compareStr(aStr, bStr);
		}

	}
	
	protected void configJob(JobConf conf) {
		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Text.class);
		
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
		
		conf.setPartitionerClass(PKPartitioner.class);
		conf.setOutputValueGroupingComparator(PVComparator.class);
	}
}
