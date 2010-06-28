package com.alimama.loganalyzer.jobs.mrs.algo.query.judger;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alimama.loganalyzer.jobs.mrs.util.KQConst;

/**
 * �����ж���
 * 
 * @author ����
 * @date 2010-04-29
 */
public abstract class DataJudger {
	private Map<Integer, Set<Integer>> catIdTree;

	public void setCatIdTree(Map _catIdTree){
		this.catIdTree = _catIdTree;
	}

	/**
	 * �ж���Ŀ�Ƿ���Ԥ����Ŀ�µ�����Ŀ����ͬ����Ŀ
	 * 
	 * @param catId
	 * @param predictedCatId
	 * @return
	 */
	public boolean isChild(int catId, int predictedCatId) {
		if (catId == KQConst.NOT_EXIST_INT) {
			return false;
		}
		if (predictedCatId == KQConst.NOT_EXIST_INT) {
			return true;
		}
		
		if (catId == predictedCatId){
			return true;
		}
		
		if (catIdTree == null) {
			return false;
		}

		Set<Integer> parentCatIds = catIdTree.get(catId);
		if (parentCatIds == null) {
			return false;
		}

		return parentCatIds.contains(predictedCatId);
	}
	
	/**
	 * �ж�һ�����Ƿ���Ԥ����Ŀ�£�����ĳ�ִ�
	 * 
	 * @param word
	 * @param predictedCatId
	 * 
	 * @return ��ӦID��-99Ϊ�޶�Ӧ�ƥ�䲻��
	 */
	public abstract int judge(String word, int predictedCatId);
	
}