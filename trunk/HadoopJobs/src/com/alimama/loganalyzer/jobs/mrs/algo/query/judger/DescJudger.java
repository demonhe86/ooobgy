package com.alimama.loganalyzer.jobs.mrs.algo.query.judger;

import java.util.List;

/**
 * ���δ��ж���
 * 
 * @author ����
 *
 */
public class DescJudger {
	List<String> descWords;
	
	public boolean judge(String word) {
		if (descWords == null){
			return false;
		}
		return this.descWords.contains(word);		
	}
	
	public void init(List<String> _descWords) {
		this.descWords = _descWords;
	}
}
