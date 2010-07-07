package com.taobao.dw.comm.tws;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.taobao.dw.comm.tws.utils.StringUtils;

/**
 * �ִ������һ����</p> singleton�汾
 * 
 * @author ����
 * @created 2010-07-05
 * @author ������
 */
public class TwsNorm {
	private static final String LOCAL_NORM_CONF = "/home/a/share/phoenix/normalize/conf/norm.conf";
	private static final String LOCAL_TWS_CONF = "/usr/local/libdata/tws/tws.conf";

	private static final String HADOOP_NORM_CONF = "./normalize/normalize/conf/norm.conf";
	private static final String HADOOP_TWS_CONF = "./tws/tws/tws.conf";

	private Normalizer normalizer;
	private TwsTokenization twsTokenization;

	private volatile static TwsNorm INSTANCE;

	/**
	 * ���ù��챣֤singleton
	 */
	private TwsNorm() {
		normalizer = Normalizer.getInstance();
		twsTokenization = TwsTokenization.getInstance();
	}

	/**
	 * ���TwsNorm</p> ��򵥵�ʵ�֣��̰߳�ȫ����Hadoop����</p>
	 * <strong>��Ҫʹ��initOnHadoop()����initOnLocal()���г�ʼ��</strong>
	 * 
	 * @return
	 */
	public static TwsNorm getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TwsNorm();
		}

		return INSTANCE;
	}

	public void initOnHadoop() {
		twsTokenization.initialize(HADOOP_TWS_CONF);
		normalizer.initialize(HADOOP_NORM_CONF);
	}

	public void initOnLocal() {
		twsTokenization.initialize(LOCAL_TWS_CONF);
		normalizer.initialize(LOCAL_NORM_CONF);
	}

	public void uninit() {
		this.normalizer.unitialize();
		this.twsTokenization.unitialize();
	}

	/**
	 * �����дʻ���й�һ����ͬ����滻��ͣ�ôʹ��ˣ���д��Сд��, ��������
	 * 
	 * @param ԭʼquery��
	 * 
	 * @return ��һ��������Query��
	 */
	public String uniformQuery(String query) {
		if (!StringUtils.isValid(query)) {
			return "";
		}
		query = normalizer.normalize(query);
		twsTokenization.segment(query);
		List<String> words = twsTokenization.getKeyWords();
		String queryUniformed = generateUniformQuery(words);
		return queryUniformed;
	}

	private String generateUniformQuery(List<String> words) {
		if (words.isEmpty()) {
			return "";
		}
		Set<String> wordsNormalized = new TreeSet<String>();
		for (String word : words) {
			String wordNormalized = normalizer.normalize(word, 380);
			if (StringUtils.isValid(wordNormalized)) {
				wordsNormalized.add(wordNormalized);
			}
		}
		StringBuilder wordsSorted = new StringBuilder();
		for (String s : wordsNormalized) {
			wordsSorted.append(s).append(" ");
		}
		return wordsSorted.toString().trim();
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("usage: TwsNorm <Word>");
			return;
		}

		String query = args[0];
		TwsNorm twsNorm = new TwsNorm();
		twsNorm.initOnLocal();
		System.out.println("��һ��ǰ��" + query);
		System.out.println("��һ����: " + twsNorm.uniformQuery(query));
		twsNorm.uninit();
	}
}
