package com.telappoint.admin.appt.common.model.request;

import java.util.List;

public class OutlookSyncReq extends BaseRequest {
	private List<Long> confNumberList;

	public List<Long> getConfNumberList() {
		return confNumberList;
	}

	public void setConfNumberList(List<Long> confNumberList) {
		this.confNumberList = confNumberList;
	}
}
