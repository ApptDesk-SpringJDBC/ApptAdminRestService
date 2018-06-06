package com.telappoint.admin.appt.common.model.response;

import java.util.ArrayList;
import java.util.List;

import com.telappoint.admin.appt.common.model.CustomerPledgeFundSource;

public class CustomerPledgeFundSourceResponse extends BaseResponse {
	private List<CustomerPledgeFundSource> customerPledgeFundSourceList = new ArrayList<CustomerPledgeFundSource>();

	public List<CustomerPledgeFundSource> getCustomerPledgeFundSourceList() {
		return customerPledgeFundSourceList;
	}

	public void setCustomerPledgeFundSourceList(List<CustomerPledgeFundSource> customerPledgeFundSourceList) {
		this.customerPledgeFundSourceList = customerPledgeFundSourceList;
	}
}
