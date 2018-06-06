package com.telappoint.admin.appt.common.model.request;

import java.util.List;
import com.telappoint.admin.appt.common.model.ServiceLocation;

/**
 * 
 * @author Balaji N
 *
 */
public class ServiceLocationApptDatesRequest extends BaseRequest {
	private List<ServiceLocation> serviceLocationList;

	public List<ServiceLocation> getServiceLocationList() {
		return serviceLocationList;
	}

	public void setServiceLocationList(List<ServiceLocation> serviceLocationList) {
		this.serviceLocationList = serviceLocationList;
	}
}
