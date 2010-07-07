package com.alimama.loganalyzer.jobs.mrs.algo.query.loader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alimama.loganalyzer.jobs.mrs.algo.Normalizer;
import com.alimama.loganalyzer.jobs.mrs.algo.query.po.Brand;
import com.alimama.loganalyzer.jobs.mrs.util.KQConst;

/**
 * Ʒ�ƿ������

        	Ʒ�ƿ��У�ÿ�����ݵ������� 162116^A20000^AƷ��^A11596^A������ʯ���U2
 			
 			��1λ����Ŀid
 			��4λ��Ʒ��id
 			��5λ��Ʒ����
 			
 			����ĿId(CatIdΪһά)��Ʒ��Id(BrandId)Ϊ��ά��Ʒ�ƴ�����Ϊֵ
				{catId1, {brandId1, [brandName1,brandName2]}, {brandId2, [brandName3,brandName4]}...}
				
				
  
 * @author ����
 * @date 2010-04-27
  */

public class BrandLoader extends DataLoader{
//	private static final int CAT_SIZE = 4000;
//	private static final int BRAND_PER_CAT_SIZE = 40;

	private static final int BRAND_SIZE = 100000;
	
	
	/**
	 * Map��ʼ����С�Ŀ���
	 * 
	 * ����20100428�����ݣ�brand�ļ������������72,246
	 * ���ص�Map�к� ��һά�Ĵ�СΪ2537���ڶ�ά��������71978/2537=28������Java�У�HashMapĬ�ϵ��������ӣ�0.75
	 * һά�Ĵ�СΪ 2537/ 0.75 = 3382 -> 4000,
	 * ��ά�Ĵ�СΪ28 /0.75 = 37 -> 40
	 * 
	 * 	Brand CatId size: 2537
 	 *	Brand size: 71978
 	 *
 	 *	����������һ�£��Ľ����ݽṹ��ֱ����BrandNameΪKey��������ң���ô��С��Ϊ1ά��ֱ����72,246/0.75~100,000
	 */
	private Map<String, Brand> brands = new HashMap<String, Brand>(BRAND_SIZE);	
	public int duplicated;
	
	@Override
	public void loadFromPath(String dataPath) {
		BufferedReader reader = null;
		String line = null;
		int dataLineNum = 0;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataPath), KQConst.UTF_8_ENCODING));
			while ((line = reader.readLine())!= null) {
				try {
					String[] items = line.split(KQConst.SPLIT);
					if (items.length != 5) {
						continue;
					}

					String catId = items[0];
					String brandId = items[3];
					String brandName = items[4];

					if (brandName.indexOf(KQConst.SLASH_SPLIT) > 0) {					
						String[] subNames = brandName.split(KQConst.SLASH_SPLIT);
						for (String subName : subNames) {
							addBrand(subName, Integer.valueOf(catId), Integer.valueOf(brandId));
						}
					} else {
						addBrand(brandName, Integer.valueOf(catId), Integer.valueOf(brandId));
					}
					
					dataLineNum++;
				} catch (Throwable e) {
					System.err.println("Ʒ�ƿ�������̳���" + line);
					e.printStackTrace();
				}
			}
			
		} catch (Throwable t) {
			t.printStackTrace();
		} finally{
			try {
				if (reader != null) reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.printf("From Brand file [%s], load %d line data. \n", dataPath, dataLineNum);
	}

	/**
	 * ���Ʒ��
	 * 
	 * @param brandName Ʒ������
	 * @param brandCatId Ʒ��������ĿID
	 * @param brandId Ʒ��ID
	 */
	private void addBrand(String brandName, Integer catId, Integer brandId) {		
		if (super.normalizer != null){
			brandName = super.normalizer.normalize(brandName);
		}
		Brand brand = brands.get(brandName);
		if (brand == null) {
			brand = new Brand(brandName, catId, brandId);
			brands.put(brandName, brand);
		}else{
			brand.add(catId, brandId);
			duplicated++;
		}
	}

	@Override
	public void releaseData() {
		this.brands.clear();
	}


	@Override
	public Map getData() {
		return this.brands;
	}

}
