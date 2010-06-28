package com.alimama.loganalyzer.tester.examples;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class MyMap extends MapReduceBase implements
Mapper<LongWritable, Text, Text, Text> {

public void map(LongWritable key, Text value,
		OutputCollector<Text, Text> output, Reporter reporter)
		throws IOException {
	String line = value.toString();
	String[] tokens=line.split(" ");
	for (String token: tokens)
		output.collect(new Text(token),new Text("1"));
	
}
}