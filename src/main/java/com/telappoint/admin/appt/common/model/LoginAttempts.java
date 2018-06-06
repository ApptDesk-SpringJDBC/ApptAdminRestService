package com.telappoint.admin.appt.common.model;

/**
 * 
 * @author Balaji N
 *
 */
public class LoginAttempts {
	private String ipAddress;
	private String loginStatus;
	private Integer userId;
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
