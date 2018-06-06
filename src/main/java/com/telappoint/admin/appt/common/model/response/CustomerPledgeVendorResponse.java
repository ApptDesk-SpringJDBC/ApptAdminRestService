package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.CustomerPledgeVendor;

public class CustomerPledgeVendorResponse extends BaseResponse {
	private List<CustomerPledgeVendor> customerPledgeVendorList = new java.util.ArrayList<CustomerPledgeVendor>();

	public List<CustomerPledgeVendor> getCustomerPledgeVendorList() {
		return customerPledgeVendorList;
	}

	public void setCustomerPledgeVendorList(List<CustomerPledgeVendor> customerPledgeVendorList) {
		this.customerPledgeVendorList = customerPledgeVendorList;
	}

}
