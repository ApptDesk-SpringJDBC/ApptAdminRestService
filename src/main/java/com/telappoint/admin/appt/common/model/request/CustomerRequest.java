package com.telappoint.admin.appt.common.model.request;

import com.telappoint.admin.appt.common.model.Customer;

public class CustomerRequest extends BaseRequest {
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
