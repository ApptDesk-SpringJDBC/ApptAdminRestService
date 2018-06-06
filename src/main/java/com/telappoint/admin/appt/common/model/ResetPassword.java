package com.telappoint.admin.appt.common.model;

public class ResetPassword {
	private int userId;
	private String clientCode;
	private String userName;
	private String token;
	private String passwordComplexity;
	private String newpassword;
	private String passwordResetAlgorithm;
	private String hostName;
	private String hostport;
	private String applicationName;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPasswordComplexity() {
		return passwordComplexity;
	}
	public void setPasswordComplexity(String passwordComplexity) {
		this.passwordComplexity = passwordComplexity;
	}
	public String getNewpassword() {
		return newpassword;
	}
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	
	public String getPasswordResetAlgorithm() {
		return passwordResetAlgorithm;
	}
	public void setPasswordResetAlgorithm(String passwordResetAlgorithm) {
		this.passwordResetAlgorithm = passwordResetAlgorithm;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHostport() {
		return hostport;
	}
	public void setHostport(String hostport) {
		this.hostport = hostport;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}
