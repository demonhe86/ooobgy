package com.alimama.loganalyzer.jobs.mrs.algo.query;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.alimama.loganalyzer.jobs.mrs.util.StringUtils;

/**
 * Query��ʶ�࣬��ʶ��Query�У��ִʺ��Ƿ���ڸ������͵Ĵ�
 * 
 * ��Ŀ��|Ʒ�ƴ�|�ͺŴ�|���δ�|��ͨ��
 * 
 * 
 * @author ����
 * @date 2010-04-27
 * 
 *
 */
public class QueryFlag {
	private static final int catFlag = 0x10;
	private static final int brandFlag = 0x08;
	private static final int modelFlag = 0x04;
	private static final int descFlag = 0x02;
	private static final int normalFlag = 0x01;
	
	private int flag = 0x00;  

	private boolean isCat;
	private boolean isBrand;
	private boolean isModel;
	private boolean isDesc;
	private boolean isNormal;
	
	public boolean isCat() {
		return isCat;
	}
	public void setCat(boolean isCat) {
		this.isCat = isCat;
		if (isCat){
			this.flag |= catFlag;
		}else{
			this.flag &= ~catFlag;
		}
	}
	public boolean isBrand() {
		return isBrand;
	}
	public void setBrand(boolean isBrand) {
		this.isBrand = isBrand;
		if (isBrand){
			this.flag |= brandFlag;
		}else{
			this.flag &= ~brandFlag;
		}
	}
	public boolean isModle() {
		return isModel;
	}
	public void setModel(boolean isStyle) {
		this.isModel = isStyle;
		if (isStyle){
			this.flag |= modelFlag;
		}else{
			this.flag &= ~modelFlag;
		}
	}
	public boolean isDesc() {
		return isDesc;
	}
	public void setDesc(boolean isDesc) {
		this.isDesc = isDesc;
		if (isDesc){
			this.flag |= descFlag;
		}else{
			this.flag &= ~descFlag;
		}
	}
	public boolean isNormal() {
		return isNormal;
	}
	public void setNormal(boolean isNormal) {
		this.isNormal = isNormal;
		if (isNormal){
			this.flag |= normalFlag;
		}else{
			this.flag &= ~normalFlag;
		}
	}	

	/**
	 * ���ر�ʶ�ַ���
	 * 
	 * @return
	 */
	public String getFlag(){
		return StringUtils.leftPad(Integer.toBinaryString(flag), 5, '0');
	}
	
	/**
	 * ���ã���������
	 */
	public void reset() {
		this.flag = 0x00;		
	}

}
