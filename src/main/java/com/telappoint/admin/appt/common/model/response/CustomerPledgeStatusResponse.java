package com.telappoint.admin.appt.common.model.response;

import java.util.ArrayList;
import java.util.List;

import com.telappoint.admin.appt.common.model.CustomerPledgeStatus;

public class CustomerPledgeStatusResponse extends BaseResponse {
	private List<CustomerPledgeStatus> customerPledgeStatusList = new ArrayList<CustomerPledgeStatus>();

	public List<CustomerPledgeStatus> getCustomerPledgeStatusList() {
		return customerPledgeStatusList;
	}

	public void setCustomerPledgeStatusList(List<CustomerPledgeStatus> customerPledgeStatusList) {
		this.customerPledgeStatusList = customerPledgeStatusList;
	}
}
