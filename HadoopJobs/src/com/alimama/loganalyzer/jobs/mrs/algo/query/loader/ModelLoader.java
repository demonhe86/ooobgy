package com.alimama.loganalyzer.jobs.mrs.algo.query.loader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.w3c.dom.stylesheets.LinkStyle;

import com.alimama.loganalyzer.jobs.mrs.algo.Normalizer;
import com.alimama.loganalyzer.jobs.mrs.algo.query.po.Model;
import com.alimama.loganalyzer.jobs.mrs.util.KQConst;
import com.alimama.loganalyzer.jobs.mrs.util.StringUtils;

/**
 * �ͺſ������
 * 
 * �ͺſ��е����ݣ�
 * 				50010368^A4536334^A2740262^A�ͺ�^A3294690^AP801
 *				500037741047734189�������ͺ�108328SCH-S259 
 * 
 *				��һλ����ĿID 
 * 				�ڶ�λ��Ʒ��ID
 * 				����λ���ͺ�ID
 * 				����λ���ͺ���
 * 
 * 
 * @author ����
 * @date  2010-04-27
 */
public class ModelLoader extends DataLoader{
//	private static final int CAT_SIZE = 9000;
//	private static final int BRAND_SIZE = 9000;
//	private static final int MODEL_PER_BRAND_SIZE = 70;	
	
	private static final int MODEL_SIZE = 330000;
	/**
	 * Map��ʼ����С�Ŀ���
	 * 
	 * ����20100428�����ݣ�model�ļ������������294,313�����ص�Map�к󣬵�һά�Ĵ�СΪ6303���ڶ�ά��������241,030/6303=47������HashMapĬ�ϵ��������ӵĿ��ǣ�0.75
	 * һά�Ĵ�СΪ 6303 / 0.75 = 8404, �趨Ϊ9000
	 * ��ά�Ĵ�СΪ47 /0.75 = 62���趨Ϊ70
	 * 
	 * 	Brand CatId size: 6303
 	 *	Brand size: 241,030
	 */
	private Map<String, Model> models = new HashMap<String, Model>(MODEL_SIZE);

	public int duplicate;
	public int dataLineNum;

	public void loadFromPath(String dataPath) {
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataPath), KQConst.UTF_8_ENCODING));
			while ((line = reader.readLine())!= null) {
				try {
					dataLineNum++;
					String[] items = line.split(KQConst.SPLIT);
					String catId = items[0];
					String brandId = items[1];
					String modelId = items[4];
					String modelName = items[5];
					
					if (modelName.indexOf(KQConst.SPACE_SPLIT) > 0) {					
						String[] subNames = modelName.split(KQConst.SPACE_SPLIT);
						for (String subName : subNames) {
							if (StringUtils.isValid(subName)){
								addModel(subName, Integer.valueOf(modelId), Integer.valueOf(brandId), Integer.valueOf(catId));
							}
						}
					} else {
						addModel(modelName, Integer.valueOf(modelId), Integer.valueOf(brandId), Integer.valueOf(catId));
					}					
				} catch (Throwable e) {
					System.err.println("�ͺſ�������̳���" + line);
					e.printStackTrace();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}finally{
			try {
				if (reader != null) reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		System.out.printf("From Model file [%s], load %d line data. \n", dataPath, dataLineNum);
		
	}

	/**
	 * ����ͺ�
	 */
	private void addModel(String modelName, Integer modelId, Integer brandId, Integer catId) {
		if (super.normalizer != null){
			modelName = super.normalizer.normalize(modelName);
		}		
		if (models.containsKey(modelName)){
			Model model= models.get(modelName);
			duplicate++;
			model.add(modelId, brandId, catId);
		}else{
			Model model = new Model(modelName, modelId, brandId, catId);		
			models.put(modelName, model);
		}
	}

	@Override
	public void releaseData() {
		this.models.clear();		
	}

	@Override
	public Map getData() {
		return this.models;
	}	

}
