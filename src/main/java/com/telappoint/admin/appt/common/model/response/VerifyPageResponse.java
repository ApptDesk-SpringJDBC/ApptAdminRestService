package com.telappoint.admin.appt.common.model.response;

import java.util.Map;

import com.telappoint.admin.appt.common.model.VerifyPageData;

public class VerifyPageResponse extends BaseResponse {
	private VerifyPageData verifyPageData;
	private Map<String,String> pageData;

	public VerifyPageData getVerifyPageData() {
		return verifyPageData;
	}

	public void setVerifyPageData(VerifyPageData verifyPageData) {
		this.verifyPageData = verifyPageData;
	}

	public Map<String,String> getPageData() {
		return pageData;
	}

	public void setPageData(Map<String,String> pageData) {
		this.pageData = pageData;
	}
}
