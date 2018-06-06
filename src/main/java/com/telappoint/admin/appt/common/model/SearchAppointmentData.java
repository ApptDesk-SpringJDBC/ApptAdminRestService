package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SearchAppointmentData extends Customer {
	// appointmentDetails - result
	private Long schedulerId;
	private String apptTimeStamp;
	private String apptDateTime;
	private String locationName;
	private String resourceName;
	private String serviceName;
	private String apptStatus;
	private String apptMethod;
	private Long confirmNumber;
	private boolean isFutureAppt;
	
	public Long getSchedulerId() {
		return schedulerId;
	}
	public void setSchedulerId(Long schedulerId) {
		this.schedulerId = schedulerId;
	}
	public String getApptTimeStamp() {
		return apptTimeStamp;
	}
	public void setApptTimeStamp(String apptTimeStamp) {
		this.apptTimeStamp = apptTimeStamp;
	}
	public String getApptDateTime() {
		return apptDateTime;
	}
	public void setApptDateTime(String apptDateTime) {
		this.apptDateTime = apptDateTime;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public boolean isFutureAppt() {
		return isFutureAppt;
	}
	public void setFutureAppt(boolean isFutureAppt) {
		this.isFutureAppt = isFutureAppt;
	}
	
	
}
