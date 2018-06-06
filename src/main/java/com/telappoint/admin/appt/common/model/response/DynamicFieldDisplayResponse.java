package com.telappoint.admin.appt.common.model.response;

import java.util.Map;

import com.telappoint.admin.appt.common.model.DynamicFieldDisplay;

public class DynamicFieldDisplayResponse extends BaseResponse {
	private Map<String, DynamicFieldDisplay> dynamicFieldDisplay;

	public Map<String, DynamicFieldDisplay> getDynamicFieldDisplay() {
		return dynamicFieldDisplay;
	}

	public void setDynamicFieldDisplay(Map<String, DynamicFieldDisplay> dynamicFieldDisplay) {
		this.dynamicFieldDisplay = dynamicFieldDisplay;
	}
	
}
