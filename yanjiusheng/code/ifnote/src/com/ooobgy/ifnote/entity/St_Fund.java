package com.ooobgy.ifnote.entity;

/**
 * ÿ�ջ���̬����֧����ľ�ֵ��״̬����Ϣ
 * @author frogcherry ������
 * @created 2011-8-8
 * @Email frogcherry@gmail.com
 */
public class St_Fund {
	/** PK */
	private Integer id;
	/** ������� */
	private Integer code;
	/** �������� */
	private String name;
	/** ��ֵ�����գ���ʽyyyyMMdd */
	private String timestamp;
	/** ��λ��ֵ */
	private Double npv;
	/** �ۼƾ�ֵ */
	private Double acc_npv;
	/** ����ֵ */
	private Double inc_npv;
	/** ������ */
	private Double inc_rate;
	/** �ɹ��� */
	private boolean buyable;
	/** ����� */
	private boolean sellable;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public Double getNpv() {
		return npv;
	}
	public void setNpv(Double npv) {
		this.npv = npv;
	}
	public Double getAcc_npv() {
		return acc_npv;
	}
	public void setAcc_npv(Double acc_npv) {
		this.acc_npv = acc_npv;
	}
	public Double getInc_npv() {
		return inc_npv;
	}
	public void setInc_npv(Double inc_npv) {
		this.inc_npv = inc_npv;
	}
	public Double getInc_rate() {
		return inc_rate;
	}
	public void setInc_rate(Double inc_rate) {
		this.inc_rate = inc_rate;
	}
	public boolean isBuyable() {
		return buyable;
	}
	public void setBuyable(boolean buyable) {
		this.buyable = buyable;
	}
	public boolean isSellable() {
		return sellable;
	}
	public void setSellable(boolean sellable) {
		this.sellable = sellable;
	}
}
