package com.telappoint.admin.appt.common.model;

import com.telappoint.admin.appt.common.model.response.BaseResponse;

public class PasswordComplexity extends BaseResponse {
	private String complexityValue;

	public String getComplexityValue() {
		return complexityValue;
	}

	public void setComplexityValue(String complexityValue) {
		this.complexityValue = complexityValue;
	}
}	
