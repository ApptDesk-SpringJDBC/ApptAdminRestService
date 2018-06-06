package com.telappoint.admin.appt.common.model;
/**
 * 
 * @author Balaji N
 *
 */
public class ServiceLocation {
	private Integer serviceLocationId;
	private Integer locationId;
	private String serviceName;
	private String startDate;
	private String endDate;
	
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getServiceLocationId() {
		return serviceLocationId;
	}
	public void setServiceLocationId(Integer serviceLocationId) {
		this.serviceLocationId = serviceLocationId;
	}
}
