package com.ooobgy.ifnote.entity;

/**
 * ������������
 * @author frogcherry ������
 * @created 2011-8-8
 * @Email frogcherry@gmail.com
 */
public class St_Rate {
	/** PK */
	private Integer id;
	/** ����key */
	private String name;
	/** ���� */
	private Double rate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
}
