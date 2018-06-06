package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AppointmentReportData extends Customer {
	private Long scheduleId;
	private String apptDateTime;
	private String locationName;
	private String departmentName;
	private String resourceName; 	
	private String serviceName;
	private String apptStatus;
	private String apptMethod;
	private String comments;
	private String walkIn;
	private String accessed;
	private Long confirmNumber;
	private String resourceApptDuration;
	private String frontDeskApptDuration;
	private String paymentAmount;
	
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
	public String getWalkIn() {
		return walkIn;
	}
	public void setWalkIn(String walkIn) {
		this.walkIn = walkIn;
	}
	public String getAccessed() {
		return accessed;
	}
	public void setAccessed(String accessed) {
		this.accessed = accessed;
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
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getResourceApptDuration() {
		return resourceApptDuration;
	}
	public void setResourceApptDuration(String resourceApptDuration) {
		this.resourceApptDuration = resourceApptDuration;
	}
	public String getFrontDeskApptDuration() {
		return frontDeskApptDuration;
	}
	public void setFrontDeskApptDuration(String frontDeskApptDuration) {
		this.frontDeskApptDuration = frontDeskApptDuration;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
}
