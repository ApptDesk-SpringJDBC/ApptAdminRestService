package com.telappoint.admin.appt.common.model.response;

public class PasswordResponse extends BaseResponse {
	private String passwordComplexity;

	public String getPasswordComplexity() {
		return passwordComplexity;
	}

	public void setPasswordComplexity(String passwordComplexity) {
		this.passwordComplexity = passwordComplexity;
	}
}
