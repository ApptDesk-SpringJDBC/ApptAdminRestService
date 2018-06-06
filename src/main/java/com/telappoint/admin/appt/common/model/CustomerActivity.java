package com.telappoint.admin.appt.common.model;

public class CustomerActivity extends Customer {
	private String timestamp;
	private String callerId;
	private String updatedBy;
	private String userName;
	private String ipAddress;
	private String uuid;
	private String device;
	
	private String apptDateTime;
	private String locationName;
	private String resourceName;
	private String serviceName;
	private String apptStatus;
	private String apptMethod;
	private Long confirmNumber;
	private String timeStamp;
	private String screened;
	private String comments;
	private String notes;
	
	public String getCallerId() {
		return callerId;
	}
	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getScreened() {
		return screened;
	}
	public void setScreened(String screened) {
		this.screened = screened;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
