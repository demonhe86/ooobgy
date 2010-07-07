package com.taobao.dw.comm.tws;

import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * @Author: zhouxiaolong.pt
 * @created: 2010-7-7
 **/

public class TwsTokenization_Test extends TestCase {
	private TwsTokenization tws;

	@Override
	protected void setUp() throws Exception {
		this.tws = TwsTokenization.getInstance();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		this.tws.unitialize();
		this.tws = null;
		super.tearDown();
	}
	
	/**
	 * �򵥵Ĺ��ܲ���
	 */
	@Test
	public void testSimpleCase() {
		String testWord = "��ɫ8g SONY PSP";
		
		tws.initialize("/usr/local/libdata/tws/tws.conf");
		tws.segment(testWord);
		
		System.out.println("Query��" + testWord);
        List<String> words = tws.getKeyWords();
        System.out.println("Tws�ִʽ����" + words);
        
        List<String> descWords = tws.getDescWords();
        System.out.println("���δʣ�" + descWords);
	}
}
