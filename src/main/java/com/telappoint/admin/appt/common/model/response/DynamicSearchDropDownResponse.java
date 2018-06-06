package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.DynamicSearchByFields;

public class DynamicSearchDropDownResponse extends BaseResponse {
	private List<DynamicSearchByFields> dynamicSearchByFields;

	public List<DynamicSearchByFields> getDynamicSearchByFields() {
		return dynamicSearchByFields;
	}

	public void setDynamicSearchByFields(List<DynamicSearchByFields> dynamicSearchByFields) {
		this.dynamicSearchByFields = dynamicSearchByFields;
	}
}
