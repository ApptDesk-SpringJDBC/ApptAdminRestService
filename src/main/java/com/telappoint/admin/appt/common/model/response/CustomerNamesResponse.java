package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.Customer;

public class CustomerNamesResponse extends BaseResponse {
	private List<Customer> customerNames;

	public List<Customer> getCustomerNames() {
		return customerNames;
	}

	public void setCustomerNames(List<Customer> customerNames) {
		this.customerNames = customerNames;
	}
}
