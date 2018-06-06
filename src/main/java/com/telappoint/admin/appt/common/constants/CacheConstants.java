package com.telappoint.admin.appt.common.constants;

public enum CacheConstants {

	// Master db table keys
    CLIENT("CLIENT"),
    CLIENT_DEPLOYMENT_CONFIG("CLIENT_DEPLOYMENT_CONFIG"),
    DISPLAY_FIELD_LABEL("DISPLAY_FIELD_LABEL"),
    DISPLAY_PAGE_CONTENT("DISPLAY_PAGE_CONTENT"),
    EMAIL_TEMPLATE("EMAIL_TEMPLATE");

	private String value;

	private CacheConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
