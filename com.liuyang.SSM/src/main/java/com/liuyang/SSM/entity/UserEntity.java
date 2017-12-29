package com.liuyang.SSM.entity;

import java.io.Serializable;

/**
 * userEntity
 * @author 刘阳
 *
 */
@SuppressWarnings("serial")
public class UserEntity implements Serializable{
	private String userName;
	private String password;
	private String userEmail;
	private Integer userAge;
	private Integer userId;
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
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public void setUserAge(Integer userAge) {
		this.userAge = userAge;
	}
	public Integer getUserAge() {
		return userAge;
	}
	

}
