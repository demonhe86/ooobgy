package com.ooobgy.ifnote.entity;

import java.sql.Timestamp;

/**
 * �û�����¼
 * @author frogcherry ������
 * @created 2011-8-8
 * @Email frogcherry@gmail.com
 */
public class Inote_Deposit {
	/** PK */
	private Integer id;
	/** ��¼ʱ�� */
	private Timestamp note_time;
	/** �����û� */
	private Integer user_id;
	/** ������� */
	private String type;
	/** �����,��ֵ��ʾ���룬��ֵ��ʾȡ�� */
	private Double sum;
	/** ������� */
	private Double rate;
	/** ���ʱ�� */
	private String dep_time;
	/** ������� */
	private String bank_name;
	/** ˵�� */
	private String comment;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Timestamp getNote_time() {
		return note_time;
	}
	public void setNote_time(Timestamp note_time) {
		this.note_time = note_time;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getDep_time() {
		return dep_time;
	}
	public void setDep_time(String dep_time) {
		this.dep_time = dep_time;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
