package com.ooobgy.ifnote.entity;

/**
 * ÿ�չ�Ʊ��Ϣ
 * @author frogcherry ������
 * @created 2011-8-8
 * @Email frogcherry@gmail.com
 */
public class St_Stock {
	/** PK */
	private Integer id;
	/** ��¼ʱ��� */
	private String timestamp;
	/** ��Ʊ���� */
	private String code;
	/** ��Ʊ���� */
	private String name;
	/** ��Ʊ��ǰ��ֵ */
	private Double smv;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getSmv() {
		return smv;
	}
	public void setSmv(Double smv) {
		this.smv = smv;
	}
}
