package com.taobao.dw.pizza.path_analysis.common;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;

import com.taobao.dw.pizza.path_analysis.biz.LogBean;


public class CommonFunction {
	
	public static String CTRL_FIELD = "\001";

	/**
	 * �ж�һ���ַ����Ƿ�Ϊ��
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim());
	}

	public static String getDeCodeURL(String url) {
		if (url != null) {
			try {
				url = url.trim();
				if (HASURLDecoder.isUtf8Url(url)) {
					url = URLDecoder.decode(url, "UTF-8");
				} else {
					url = URLDecoder.decode(url, "GBK");
				}
			} catch (Exception e) {
				return url;
			}
		}
		return url;
	}

	/**
	 * ��refer�еõ�ʵ�ʵ�referer
	 * @param refer
	 * @return
	 */
	public static String getRealReferFromRefer(String refer) {
		if (refer.indexOf("&pre=") != -1)
			refer = refer.split("&pre=", -1)[1];
		if (refer.indexOf("&scr=") != -1)
			refer = refer.split("&scr=", -1)[0];
		return refer;
	}
	
	/**
	 * 
	 * @param refer
	 * @param url
	 * @return 
	 */
	public static String getReferParam(String refer, String url) {
		if (refer.indexOf("&scr=") == -1) return "";
		String param = "scr=" + refer.split("&scr=", -1)[1];
		if(url.indexOf("?") != -1) return "&" + param;
		return "?" + param;
	}
	
	/**
	 * �ж�mid�Ƿ�Ϊ������19λ���֣�������ǣ��������־������
	 * @param mid
	 * @return
	 */
	public static boolean isCorrectMid(String mid) {
		Pattern pattern = Pattern.compile("^[\\d]{19}$");
		Matcher matcher = pattern.matcher(mid);
		if (matcher.find()) {
			return true;
		}
		return false;
	}
	
	/**
	 * �жϵ�ǰ�Ƿ�Ϊ��¼�û�
	 * @param uid
	 * @return
	 */
	public static boolean isUser(String uid) {
		Pattern pattern = Pattern.compile("^[\\d]{2,}$");
		Matcher matcher = pattern.matcher(uid);
		if (matcher.find()) {
			return true;
		}
		return false;
	}
	
	/**
	 * �ݹ��ȡ����
	 * @param logBean
	 * @param isInit isInit=0��ʾΪҶ�ӽ�㣬-1��ʾΪ��Ҷ�ӽڵ�
	 * @return
	 */
	public static String getRecursionResult(LogBean logBean,int isInit){
		String recursionResult = isInit == 0 ? logBean.toString() + "}" : "";
		if(logBean.getPrevious() != null) {
			recursionResult += (logBean.getPrevious() + "}" + getRecursionResult(logBean.getPrevious(), -1));
		}
		return recursionResult;
	}
	
	/**
	 * ���ڵݹ������ȡ��Ϊ��������Եݹ�����Ľ�����������װ
	 * @param logBean
	 * @param isInit
	 * @return
	 */
	public static String reverseString(LogBean logBean,int isInit){
		String result = "";
		String[] array = getRecursionResult(logBean, isInit).split("]}");
		//��������е�ת�������
		for(int i = array.length - 1; i >= 0; i--){
			result += ",{" + array[i] + "}";
		}
		if(result.startsWith(",")) result = result.substring(1);
		result = "\"entitys\":[" + result + "]";
		return result;
	}


	public static class FirstPartitioner implements Partitioner<Text, Text> {

		@Override
		public void configure(JobConf conf) {
		}

		@Override
		public int getPartition(Text key, Text value, int numPartitions) {
			String[] t = key.toString().split("\"");
			int hashCode = WritableComparator.hashBytes(key.getBytes(), t[0]
					.length());
			return (hashCode & Integer.MAX_VALUE) % numPartitions;
		}

	}

	public static class FirstGroupingComparator implements RawComparator<Text> {

		@Override
		public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
			return WritableComparator.compareBytes(b1, s1, 0, b2, s2, 0);
		}

		@Override
		public int compare(Text o1, Text o2) {
			String[] t1 = o1.toString().split("\"");
			String[] t2 = o2.toString().split("\"");
			return WritableComparator.compareBytes(o1.getBytes(), 0, t1[0]
					.length(), o2.getBytes(), 0, t2[0].length());
		}
	}
}
