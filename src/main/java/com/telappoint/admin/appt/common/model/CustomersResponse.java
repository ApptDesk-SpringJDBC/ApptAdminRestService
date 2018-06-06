package com.telappoint.admin.appt.common.model;

import java.util.List;

import com.telappoint.admin.appt.common.model.response.BaseResponse;

public class CustomersResponse extends BaseResponse {
	private List<Customer> customerList;

	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}
}
