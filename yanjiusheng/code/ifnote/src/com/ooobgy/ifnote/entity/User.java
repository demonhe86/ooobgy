package com.ooobgy.ifnote.entity;

/**
 * User Bean: ��ʾ��ͨ�û���entity�����л����û���Ϣ
 * @author frogcherry ������
 * @created 2011-8-4
 * @Email frogcherry@gmail.com
 */
public class User {
	/** PK */
	private Integer id;
	/** �û��� */
	private String userName;
	/** ���� */
	private String password;
	/** email */
	private String email;
	/** �绰���� */
	private String phoneNum;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
}
