package com.telappoint.admin.appt.common.constants;

/**
 * 
 * @author Balaji
 * 
 */

public enum LoginConstants {
	LOGIN_SUCESSES_RESPONSE("sucesses"),
	LOGIN_FAILURE_RESPONSE("failure"),
	LOGIN_USER_LOCKED_RESPONSE("User account is locked"),
	LOGIN_RESTRICT_IPS_RESPONSE("IP is restricted"),
	LOGIN_PASSWORD_EXPIRED_RESPONSE("Password is expired"),
	LOGIN_USER_SUSPENDED_RESPONSE("User account is suspended"),
	LOGIN_WRONG_LOGIN_ATTEMPTS_EXCEEDED_RESPONSE("Max wrong login attempts exceeded"),
	LOGIN_INVALID_PASSWORD_RESPONSE("Invalid password"),
	LOGIN_CHANGE_PASSWORD_RESPONSE("Change Password Response"),
	LOGIN_SLA_FORWARD_URL_PATH("sla.html"),
	LOGIN_ATEMPTS_LOGIN_STATUS_SUCESS("s"),
	LOGIN_ATEMPTS_LOGIN_STATUS_FAILURE("f");

	private String value;

	private LoginConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
