package com.telappoint.admin.appt.common.model.request;

/**
 * 
 * @author Balaji N
 *
 */

public class UserLogin {
	private String username;
	private String password;
	private String ipAddress;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
