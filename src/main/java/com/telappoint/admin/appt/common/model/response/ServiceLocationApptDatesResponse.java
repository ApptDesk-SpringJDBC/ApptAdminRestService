package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.ServiceLocation;
/**
 * 
 * @author Balaji N
 *
 */
public class ServiceLocationApptDatesResponse extends BaseResponse {
	private List<ServiceLocation> serviceLocationList;

	public List<ServiceLocation> getServiceLocationList() {
		return serviceLocationList;
	}

	public void setServiceLocationList(List<ServiceLocation> serviceLocationList) {
		this.serviceLocationList = serviceLocationList;
	}
}
