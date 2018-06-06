package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.CustomerActivity;

public class CustomerActivityResponse {
	private List<CustomerActivity> customerActivityList;

	public List<CustomerActivity> getCustomerActivityList() {
		return customerActivityList;
	}

	public void setCustomerActivityList(List<CustomerActivity> customerActivityList) {
		this.customerActivityList = customerActivityList;
	}
}
