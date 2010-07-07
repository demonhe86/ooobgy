package com.alimama.loganalyzer.jobs.mrs.algo.query.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.alimama.loganalyzer.jobs.mrs.algo.query.judger.ModelJudger;
import com.alimama.loganalyzer.jobs.mrs.algo.query.po.Model;
import com.alimama.loganalyzer.jobs.mrs.util.KQConst;


public class ModelLoaderTest {
	ModelLoader modelLoader = new ModelLoader();
	String dataPath = "types";
	
	@Test
	public void testLoad(){
		modelLoader.loadFromPath(Thread.currentThread().getContextClassLoader().getResource(dataPath).getFile());
		Map models = modelLoader.getData();
		
		System.out.println("Model BrandId size: " + models.size());
		System.out.println("Duplicate num: " + modelLoader.duplicate);
		System.out.println("File num: " + modelLoader.dataLineNum);

		/**
		 * ��������߼����ԣ�Դ����ʵ�ֵĹ�������ж������ݣ�Ȼ����"\u0001"�ָ����ݣ�
		 * <strong>����ÿһ���ָ�������ִ����ٳ����ÿո�ָ�������ܹ��ָ
		 * �Խ���ִ�(���������ִ����ִ�)���˺����model��
		 * �����ָܷ�������ִ�(�����ݵ��ִ�)����model��</strong>
		 * �����ӣ��޴ӵ�֪����������֮��ĵ�ʽ��ϵ���������Ӧ���ǲ�ȷ���ģ�����true����false��
		 * �˴�ȥ����---------by zhouxiaolong.pt
		 */
		//assertTrue(models.size() == (modelLoader.dataLineNum - modelLoader.duplicate));
		
		ModelJudger modelJudger = new ModelJudger(modelLoader.getData());
		int modelId = modelJudger.judge("���������", KQConst.NOT_EXIST_INT);
		System.out.println(modelId);
		
		/**
		 * add by zhouxiaolong.pt���������֤
		 */
		Model model = (Model)models.get("���������");
		String testStr = model.modelName;
		assertEquals("���������", testStr);
		assertTrue(model.modelIds.contains(new Integer(modelId)));
		
		//System.out.println(testStr);
		
	}
}
