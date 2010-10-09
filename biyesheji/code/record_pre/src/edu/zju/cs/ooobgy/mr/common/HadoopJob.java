package edu.zju.cs.ooobgy.mr.common;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;

/**
 * @Author: ������
 * @created: 2010-7-21
 **/

/**
 * Hadoop MapReduce��ҵ�Ĺ����ӿڣ�
 * ������Ҫʹ��Launcher��������ҵ����ʵ�ָýӿ�
 */
public interface HadoopJob {
	/**
	 * ��ȡҪʹ�õ�Mapper��
	 * @return Mapper
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends Mapper> getMapper();
	
	/**
	 * ��ȡҪʹ�õ�Reducer��
	 * @return Reducer
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends Reducer> getReducer();
	
	/**
	 * MapReduce��ҵ�����ڲ���һЩ����config�����ȼ�С�������ļ�������
	 * @param conf
	 */
	public void configJob(JobConf conf);
}
