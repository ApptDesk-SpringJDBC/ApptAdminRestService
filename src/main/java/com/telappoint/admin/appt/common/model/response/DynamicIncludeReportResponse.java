package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.DynamicIncludeReport;

public class DynamicIncludeReportResponse extends BaseResponse {
	private List<DynamicIncludeReport> dynamicIncludeReportList;

	public List<DynamicIncludeReport> getDynamicIncludeReportList() {
		return dynamicIncludeReportList;
	}

	public void setDynamicIncludeReportList(List<DynamicIncludeReport> dynamicIncludeReportList) {
		this.dynamicIncludeReportList = dynamicIncludeReportList;
	}
}
