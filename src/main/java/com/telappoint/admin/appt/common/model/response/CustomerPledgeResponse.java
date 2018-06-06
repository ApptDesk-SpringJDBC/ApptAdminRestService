package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.admin.appt.common.model.CustomerPledge;

/**
 * 
 * @author Balaji N
 *
 */

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CustomerPledgeResponse extends BaseResponse {
	private List<CustomerPledge> customerPledgeList;
	
	public List<CustomerPledge> getCustomerPledgeList() {
		return customerPledgeList;
	}
	public void setCustomerPledgeList(List<CustomerPledge> customerPledgeList) {
		this.customerPledgeList = customerPledgeList;
	}
}
