package com.telappoint.admin.appt.common.model.response;

import java.util.ArrayList;
import java.util.List;

import com.telappoint.admin.appt.common.model.CustomerPastAppts;

public class CustomerPastApptsResponse extends BaseResponse {
	private List<CustomerPastAppts> customerPastAppts = new ArrayList<CustomerPastAppts>();

	public List<CustomerPastAppts> getCustomerPastAppts() {
		return customerPastAppts;
	}

	public void setCustomerPastAppts(List<CustomerPastAppts> customerPastAppts) {
		this.customerPastAppts = customerPastAppts;
	}
}
