package com.alimama.loganalyzer.jobs.mrs.algo.query.loader;

import java.util.Map;

/**
 * ���ݼ�����
 * 
 * @author ����
 * @date 2010-04-26
 */
public interface DataLoader {
	public void loadFromPath(String dataPath);
	public Map getData();
	public void releaseData();	
}
