package com.taobao.dw.comm.tws;

/**
 * Normalizer singleton�汾
 * 
 * @author zhouxiaolong.pt
 * 
 */
public class Normalizer {
	static {
		System.loadLibrary("Normalizer");
	}

	native String normalize(String words, int options);

	public native void initialize(String confPath);

	public native void unitialize();

	private volatile static Normalizer INSTANCE;

	/**
	 * ���ù��챣֤singleton
	 */
	private Normalizer() {
	}

	/**
	 * ���Normalizer</p> ��򵥵�ʵ��,�̰߳�ȫ����Hadoop����</p>
	 * 
	 * @return
	 */
	public static Normalizer getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Normalizer();
		}
		return INSTANCE;
	}

	public String normalize(String words) {
		return normalize(words, 380);
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("usage: Normalizer <Word>");
			return;
		}
		Normalizer norm = new Normalizer();
		norm.initialize("/home/a/share/phoenix/normalize/conf/norm.conf");

		String normalized1 = norm.normalize(args[0]);
		String normalized24 = norm.normalize(args[0], 24);
		String normalized380 = norm.normalize(args[0], 380);

		System.out.println("1:" + normalized1);
		System.out.println("2:" + normalized24);
		System.out.println("3:" + normalized380);
	}
}
