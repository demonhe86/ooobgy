package com.ooobgy.ifnote.entity;

import java.sql.Timestamp;

/**
 * �û������׼�¼
 * @author frogcherry ������
 * @created 2011-8-8
 * @Email frogcherry@gmail.com
 */
public class Inote_Fund {
	/** PK */
	private Integer id;
	/** ��¼ʱ�� */
	private Timestamp note_time;
	/** �����û� */
	private Integer user_id;
	/** ������� */
	private Integer fund_code;
	/** ���׹�������ֵ��ʾ���룬��ֵ��ʾ��� */
	private Integer count;
	/** ����ʱ��ֵ */
	private Double npv;
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
	public Integer getFund_code() {
		return fund_code;
	}
	public void setFund_code(Integer fund_code) {
		this.fund_code = fund_code;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getNpv() {
		return npv;
	}
	public void setNpv(Double npv) {
		this.npv = npv;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
