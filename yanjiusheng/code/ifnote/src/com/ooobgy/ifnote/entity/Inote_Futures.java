package com.ooobgy.ifnote.entity;

import java.sql.Timestamp;

/**
 * �û��ڻ����׼�¼
 * @author frogcherry ������
 * @created 2011-8-8
 * @Email frogcherry@gmail.com
 */
public class Inote_Futures {
	/** PK */
	private Integer id;
	/** ��¼ʱ�� */
	private Timestamp note_time;
	/** �����û� */
	private Integer user_id;
	/** �ڻ����� */
	private String name;
	/** ���� */
	private Double price;
	/** ������������ֵ��ʾ���룬��ֵ��ʾ���� */
	private Double sum;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
