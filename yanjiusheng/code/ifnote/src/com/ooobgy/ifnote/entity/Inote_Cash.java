package com.ooobgy.ifnote.entity;

import java.sql.Timestamp;

/**
 * �û��ֽ���ˮ��
 * @author frogcherry ������
 * @created 2011-8-8
 * @Email frogcherry@gmail.com
 */
public class Inote_Cash {
	/** PK */
	private Integer id;
	/** ��¼ʱ�� */
	private Timestamp note_time;
	/** �����û� */
	private Integer user_id;
	/** ��֧�˵� */
	private Double account;
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
	public Double getAccount() {
		return account;
	}
	public void setAccount(Double account) {
		this.account = account;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
