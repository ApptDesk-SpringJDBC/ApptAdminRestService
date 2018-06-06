package com.telappoint.admin.appt.common.model.request;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.admin.appt.common.model.Customer;
import com.telappoint.admin.appt.common.model.CustomerPledge;

/**
 * @author Balaji N
 * 
 */
@JsonAutoDetect
@JsonSerialize(include = Inclusion.NON_NULL)
public class CustomerPledgeRequest extends BaseRequest {
	private Customer customer;
	private List<CustomerPledge> customerPledgeList = new ArrayList<>();
	private String timeZone;

	public List<CustomerPledge> getCustomerPledgeList() {
		return customerPledgeList;
	}

	public void setCustomerPledgeList(List<CustomerPledge> customerPledgeList) {
		this.customerPledgeList = customerPledgeList;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
}
