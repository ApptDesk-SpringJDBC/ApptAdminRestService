package com.telappoint.admin.appt.common.model;

public class AppointmentData {
	private String apptDateTime;
	private String apptTimeStamp;
	private String locationName;
	private String procedureName;
	private String resourceName;
	private String serviceName;
	private String apptStatus;
	private String apptMethod;
	private Long confirmNumber;
	
	public String getApptDateTime() {
		return apptDateTime;
	}
	public void setApptDateTime(String apptDateTime) {
		this.apptDateTime = apptDateTime;
	}
	public String getApptTimeStamp() {
		return apptTimeStamp;
	}
	public void setApptTimeStamp(String apptTimeStamp) {
		this.apptTimeStamp = apptTimeStamp;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getApptStatus() {
		return apptStatus;
	}
	public void setApptStatus(String apptStatus) {
		this.apptStatus = apptStatus;
	}
	public String getApptMethod() {
		return apptMethod;
	}
	public void setApptMethod(String apptMethod) {
		this.apptMethod = apptMethod;
	}
	public Long getConfirmNumber() {
		return confirmNumber;
	}
	public void setConfirmNumber(Long confirmNumber) {
		this.confirmNumber = confirmNumber;
	}
}
