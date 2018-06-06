package com.telappoint.admin.appt.common.model;

/**
 * 
 * @author Balaji N
 *
 */
public class AdminLoginConfig {
	private String userRestrictIps;
	private Integer maxWrongLoginAttempts;
	private Integer passwordExpireDays;
	
	public String getUserRestrictIps() {
		return userRestrictIps;
	}
	public void setUserRestrictIps(String userRestrictIps) {
		this.userRestrictIps = userRestrictIps;
	}
	public Integer getMaxWrongLoginAttempts() {
		return maxWrongLoginAttempts;
	}
	public void setMaxWrongLoginAttempts(Integer maxWrongLoginAttempts) {
		this.maxWrongLoginAttempts = maxWrongLoginAttempts;
	}
	public Integer getPasswordExpireDays() {
		return passwordExpireDays;
	}
	public void setPasswordExpireDays(Integer passwordExpireDays) {
		this.passwordExpireDays = passwordExpireDays;
	}
}
