/**
 * @Author ������   frogcherry
 * @Email frogcherry@gmail.com
 * @CreateDate 2010-6-10
 */

package com.ooobgy.data.entity;

import java.sql.Timestamp;


public class Admin {
	/**
	 * �й�����
	 */
	private Integer adminId;
	/**
	 * ��¼��
	 */
	private String name;
	private String password;
	/**
	 * �ǳ�
	 */
	private String nickname;
	/**
	 * ע��ʱ��
	 */
	private Timestamp checkinTime;
	public Integer getAdminId() {
		return adminId;
	}
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Timestamp getCheckinTime() {
		return checkinTime;
	}
	public void setCheckinTime(Timestamp checkinTime) {
		this.checkinTime = checkinTime;
	}
	
	
}
