package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import com.telappoint.admin.appt.common.model.CustomerRegistration;
public class CustomerRegistrationRepsonse extends BaseResponse {
	private List<CustomerRegistration> customerRegistrationList;
    
	public List<CustomerRegistration> getCustomerRegistrationList() {
		return customerRegistrationList;
	}

	public void setCustomerRegistrationList(List<CustomerRegistration> customerRegistrationList) {
		this.customerRegistrationList = customerRegistrationList;
	}
}
