package com.taobao.dw.comm.tws.utils;

/**
 * �ַ�����������
 * 
 * @author ����
 *
 */
public class StringUtils {

	public StringUtils() {
        super();
    }
	
    /**
     * �Ƿ���Ч�ַ���
     * @param s
     * @return ��Ч
     */
	public static boolean isValid(String s){
		return !(s==null || s.trim().equals(""));
	}


}
