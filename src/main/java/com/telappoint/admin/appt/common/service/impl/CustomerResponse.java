package com.telappoint.admin.appt.common.service.impl;
import com.telappoint.admin.appt.common.model.response.BaseResponse;

public class CustomerResponse extends BaseResponse {
	private Long customerId;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
}
