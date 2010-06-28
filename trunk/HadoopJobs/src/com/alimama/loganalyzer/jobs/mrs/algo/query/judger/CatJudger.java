package com.alimama.loganalyzer.jobs.mrs.algo.query.judger;

import java.util.HashMap;
import java.util.Map;

import com.alimama.loganalyzer.jobs.mrs.algo.query.po.Cat;
import com.alimama.loganalyzer.jobs.mrs.util.KQConst;

/**
 * ��Ŀ�ж���
 * 
 * @author ����
 * @date 2010-04-29
 */
public class CatJudger extends DataJudger {
	private Map<String, Cat> cats;
	
	private CatJudger(){
		super();
	}

	public CatJudger(Map cats) {
		super();
		this.cats = cats;
	}
	
	public int judge(String word, int predictedCatId) {
		Cat cat = cats.get(word);
		if (cat == null) {
			return KQConst.NOT_EXIST_INT;
		}

		/*
		 * û����ĿԤ��ID����Ȼ��ƥ���ϵ���Ŀ����ô�����ⷵ�ص�һ�� 
		 */
		if (predictedCatId <= 0) {
			if (cat.catIds.size() > 0) {
				return cat.catIds.get(0);
			} else {
				return KQConst.NOT_EXIST_INT;
			}
		}

		/**
		 * ����ĿԤ��ID����ô��ȡ����Ŀ�ʶ�Ӧ��������Ŀ������˳��ƥ�䣬����ƥ�䵽�ĵ�һ��ID
		 */
		for (int i = 0, j = cat.catIds.size(); i < j; i++) {
			int catId = cat.catIds.get(i);
			//��ʱ��������Ŀ�̳��������⣬ֻ�ж��Ƿ����
			if (catId == predictedCatId){
			//if (isChild(catId, predictedCatId)) {
				return cat.catIds.get(i);
			}
		}

		return KQConst.NOT_EXIST_INT;

	}

}
