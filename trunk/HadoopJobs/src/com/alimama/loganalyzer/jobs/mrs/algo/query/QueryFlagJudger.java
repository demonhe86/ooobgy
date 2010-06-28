package com.alimama.loganalyzer.jobs.mrs.algo.query;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.alimama.loganalyzer.jobs.mrs.algo.BrandStylePredict;
import com.alimama.loganalyzer.jobs.mrs.algo.Normalizer;
import com.alimama.loganalyzer.jobs.mrs.algo.QueryCatPredict;
import com.alimama.loganalyzer.jobs.mrs.algo.TwsTokenization;
import com.alimama.loganalyzer.jobs.mrs.algo.query.helper.QueryCatPredictor;
import com.alimama.loganalyzer.jobs.mrs.algo.query.helper.WordSeperator;
import com.alimama.loganalyzer.jobs.mrs.algo.query.helper.TwsWordSeperator;
import com.alimama.loganalyzer.jobs.mrs.algo.query.judger.BrandJudger;
import com.alimama.loganalyzer.jobs.mrs.algo.query.judger.DataJudger;
import com.alimama.loganalyzer.jobs.mrs.algo.query.judger.CatJudger;
import com.alimama.loganalyzer.jobs.mrs.algo.query.judger.DescJudger;
import com.alimama.loganalyzer.jobs.mrs.algo.query.judger.ModelJudger;
import com.alimama.loganalyzer.jobs.mrs.algo.query.loader.BrandLoader;
import com.alimama.loganalyzer.jobs.mrs.algo.query.loader.CatLoader;
import com.alimama.loganalyzer.jobs.mrs.algo.query.loader.ModelLoader;
import com.alimama.loganalyzer.jobs.mrs.util.GlobalInfo;

/**
 * Query��ʶ�ж���
 * 
 	����һ��Query��Ԥ���Query������Ŀ��Ȼ���Query���зִʣ���ÿ���ʽ����жϣ��ó�Query���������ԡ�
 
  	QueryFlag�ķ����ǣ�
		��Ŀ��|Ʒ�ƴ�|�ͺŴ�|���δ�|��ͨ��
		
	���磺
		10011
		00101
		
	���һ��Query�Ĵʣ���������Ŀ�ʺ�Ʒ�ƴʹ��ɣ�����ͻ��ǣ�
	
		11000
		����
		11100
		
	û����ͨ�ʣ���ô���Query��ǰ�˾Ϳ���ͨ����������׵��޳���
 * 
 * 
 * @author ����
 * @date 2010-04-26
 */
public class QueryFlagJudger {

	//��Ŀ��Ʒ�ƣ��ͺų�ʼ������������ļ�ϵͳ��ȡ����
	private static CatLoader catLoader = new CatLoader();
	private static BrandLoader brandLoader = new BrandLoader();
	private static ModelLoader modelLoader = new ModelLoader();

	//��Ŀ��Ʒ�ƣ��ͺ��ж��������ݳ�ʼ�����ṩ�����ݣ��ж�һ���ʣ��Ƿ�Ϊ��Ŀ�ʣ�Ʒ�ƴʺ��ͺŴ�
	private static CatJudger catJudger;
	private static BrandJudger brandJudger;
	private static ModelJudger modelJudger;
	//���δ��ж���
	private static DescJudger descJudger = new DescJudger();
	
	//Query��ĿԤ����
	private static QueryCatPredictor queryCatPredictor;
	
	//Query�ִ���
	private static WordSeperator wordSeperator;
	
	//Query��ʶλ��
	QueryFlag queryFlag = new QueryFlag();
	
	/**
	 * �������У�ͨ��Loader��ȡ���ݣ��������ݷַ�����ͬ��Judger��
	 * 
	 * 
	 * @param _catPath 		��Ŀ��λ��
	 * @param _brandsPath	Ʒ�ƿ�λ��
	 * @param _stylesPath	�ͺſ�λ��
	 */
	public QueryFlagJudger(String _catPath, String _brandPath, String _modelPath) {
		System.out.println("QueryFlagJudger start initing����");		
		
		try {
			catLoader.loadFromPath(_catPath);
			brandLoader.loadFromPath(_brandPath);
			modelLoader.loadFromPath(_modelPath);
			
			catJudger = new CatJudger(catLoader.getData());
			brandJudger = new BrandJudger(brandLoader.getData());
			modelJudger = new ModelJudger(modelLoader.getData());
			
			catJudger.setCatIdTree(catLoader.getCatIdTree());
			brandJudger.setCatIdTree(catLoader.getCatIdTree());
			modelJudger.setCatIdTree(catLoader.getCatIdTree());
			
			System.out.println("QueryFlagJudger init successfully!");
			
		} catch (Throwable t) {
			System.out.println("QueryFlagJudger init failed!");
			System.err.println(t);
			t.printStackTrace();
		}		
	}

	public void setQueryCatPredict(QueryCatPredictor _queryCatPredict){
		this.queryCatPredictor = _queryCatPredict;
	}
	
	public void setWordSperator(WordSeperator _wordSeperator){
		this.wordSeperator = _wordSeperator;
	}
	
	//����
	private QueryFlagJudger(){
		super();
	}
	
	
	/**
	 * �ж�Query�ʵ����ͣ����øýӿڣ���ʾQuery��Ϊ��һ����δ�ִʣ���Ҫ��������û��Ԥ�����Ŀ
	 * 
	 * @param query ԭʼQuery��
	 * 
	 * @return	��ʶ�ַ���
	 */
	public String judge(String query) {
		if (this.queryCatPredictor == null){
			throw new RuntimeException("��ĿԤ����Ϊ�գ�Query�ж���������������");
		}			
		int catPredicted = queryCatPredictor.predict(query);
		return judge(query, catPredicted);
	}

	
	/**
	 * Query��δ��һ����δ�ִʣ��Ѿ�Ԥ�����Ŀ
	 * 
	 * @param query			ԭʼQuery��
	 * @param catPredicted	Query�����ڵ���Ŀ
	 * @return
	 */
	public  String judge(String query, int catPredicted) {
		if (this.wordSeperator == null){
			throw new RuntimeException("�ִ���Ϊ�գ�Query�ж���������������");
		}		
		
		List<String> words = wordSeperator.segment(query);
		List<String> descs = wordSeperator.getDescWords();
		
		GlobalInfo.debugMessage("�ִʽ��:"+words);
		GlobalInfo.debugMessage("������:"+descs);
		
		return judge(words, descs, catPredicted);
	}
	
	/**
	 * Query���Ѿ��ִʣ��Ѿ�Ԥ�����Ŀ
	 * 
	 * @param words			�ִʺ�Ľ��
	 * @param descWords		���δ�
	 * @param catPredicted
	 * @return
	 */
	public String judge(List<String> words , List<String> descWords, int catPredicted){
		descJudger.init(descWords);		
		queryFlag.reset();
	
		for (String word: words){
			GlobalInfo.debugMessage("��ʼ�ж�:[" + word + "]");
			int catId = catJudger.judge(word, catPredicted);
			if (catId > 0){
				queryFlag.setCat(true);
				GlobalInfo.debugMessage("1.��Ŀ��");
				continue;
			}			
			
			int brandId = brandJudger.judge(word, catPredicted);			
			if (brandId > 0){
				queryFlag.setBrand(true);
				GlobalInfo.debugMessage("2.Ʒ�ƴ�");
				continue;
			}
			
			int modelId = modelJudger.judge(word, catPredicted);			
			if (modelId > 0){
				queryFlag.setModel(true);
				GlobalInfo.debugMessage("3.�ͺŴ�");
				continue;
			}
				
			//���δ��ж�
			if (descJudger.judge(word)){
				queryFlag.setDesc(true);
				GlobalInfo.debugMessage("4.���δ�");
				continue;
			}

			//��������κ�һ�֣���Ϊ��ͨ��
			queryFlag.setNormal(true);
			GlobalInfo.debugMessage("5.��ͨ��");
		}
		
		return queryFlag.getFlag();		
	}
	
	public static List<String> normalizeList(List<String> words, Normalizer normalizer) {
		return normalizeList(words, 380, normalizer);
	}

	public static List<String> normalizeList(List<String> words, int option, Normalizer normalizer) {
		if (normalizer != null) {
			List<String> wordsNormalized = new LinkedList<String>();
			for (String word : words) {
				String wordNormalized = normalizer.normalize(word, option);
				if (!wordNormalized.isEmpty() && !wordsNormalized.contains(wordNormalized)) {
					wordsNormalized.add(wordNormalized);	
				}
			}
			return wordsNormalized;
		} else {
			return words;
		}
	}	
	
	public static String generateUniformQuery(String query, String cat, List<String> words, List<String> brands, Normalizer normalizer, BrandStylePredict brandStylePredict) {
		// Step 1) generate all brands, types
		List<String> wordsNormalized = normalizeList(words, 380, normalizer); // �����дʻ���й�һ����ͬ����滻��ͣ�ôʹ��ˣ���д��Сд��
		if (wordsNormalized.isEmpty()) {
			return null;
		}
		
		Collections.sort(wordsNormalized);
		StringBuilder wordsSorted = new StringBuilder();
		Iterator<String> itWordNormalized = wordsNormalized.iterator();
		while (itWordNormalized.hasNext()) {
			wordsSorted.append(itWordNormalized.next());
			
			if (itWordNormalized.hasNext()) {
				wordsSorted.append(" ");
			}
		}
		
		String querySorted = wordsSorted.toString();  // ���򣬶Բ�ͬ˳��Ĵʻ����Ϊͬһ�������� "���� ����"������Ϊ "���� ���ǡ�
		return querySorted;
	}	
	
	public static void main(String[] args){
		if(args.length != 2) {
			System.out.println("usage: QueryFlagJudger <Query> <CatId>");
			return;
		}		

		GlobalInfo.setDebugOn(true);
		
		TwsTokenization tokenization = new TwsTokenization();
		tokenization.initialize("/usr/local/libdata/tws/tws.conf");
		
		Normalizer normalizer = new Normalizer();
		normalizer.initialize("/home/a/share/phoenix/normalize/conf/norm.conf");
		
		WordSeperator tws = TwsWordSeperator.getInstance();
		((TwsWordSeperator)tws).setTws(tokenization);
		
		QueryFlagJudger queryFlagJudger = new QueryFlagJudger("./data/cats", "./data/brands", "./data/types");		
		queryFlagJudger.setWordSperator(tws);

		String query = args[0];

		String queryNormalized = normalizer.normalize(query, 24);
		tokenization.segment(queryNormalized);
		List<String> words = tokenization.getKeyWords();
		List<String> descs = tokenization.getDescWords();
	

		String queryUniformed = generateUniformQuery(null, null, words, null, normalizer, null);
		
		String catId = args[1];
		
		String queryFlag = queryFlagJudger.judge(words, descs, Integer.parseInt(catId));
		
		System.out.println("�ִʺ�Ľ����:" + words);
		System.out.printf("��һ��Ľ����[%s]\n", queryUniformed);
		System.out.printf("�жϽ����[%s]\n", queryFlag);
		System.out.println();
		
		GlobalInfo.setDebugOn(false);
	}

}


