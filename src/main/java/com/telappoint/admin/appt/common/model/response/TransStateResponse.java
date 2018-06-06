package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.TransState;

public class TransStateResponse extends BaseResponse {
	private List<TransState> transStateList;

	public List<TransState> getTransStateList() {
		return transStateList;
	}

	public void setTransStateList(List<TransState> transStateList) {
		this.transStateList = transStateList;
	}
}
