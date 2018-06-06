package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.admin.appt.common.model.Location;
import com.telappoint.admin.appt.common.model.Resource;
import com.telappoint.admin.appt.common.model.ServiceVO;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HomePageResponse extends BaseResponse {
	private List<Location> locationList;
    private List<ServiceVO> serviceList;
    private List<Resource> resourceList;
    
	public List<Location> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}
	public List<ServiceVO> getServiceList() {
		return serviceList;
	}
	public void setServiceList(List<ServiceVO> serviceList) {
		this.serviceList = serviceList;
	}
	public List<Resource> getResourceList() {
		return resourceList;
	}
	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}
}