package com.telappoint.admin.appt.common.model;

/**
 * 
 * @author Balaji N
 *
 */
public class OutLookAppointment {
	private String firstName = "";
	private String lastName = "";
	private String dateTime = "";
	private String locationName = "";
	private String resFirstName = "";
	private String resLastName = "";
	private String homePhone = "";
	private String cellPhone = "";
	private String workphone = "";
	private Long confNumber;
	private String email = "";
	private String serviceName = "";
	private String accountNumber;
	private int duration = 0;
	private int apptType = 0;
	private String comments = "";
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getApptType() {
		return apptType;
	}
	public void setApptType(int apptType) {
		this.apptType = apptType;
	}
	public String getResFirstName() {
		return resFirstName;
	}
	public void setResFirstName(String resFirstName) {
		this.resFirstName = resFirstName;
	}
	public String getResLastName() {
		return resLastName;
	}
	public void setResLastName(String resLastName) {
		this.resLastName = resLastName;
	}
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getWorkphone() {
		return workphone;
	}
	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}
	public Long getConfNumber() {
		return confNumber;
	}
	public void setConfNumber(Long confNumber) {
		this.confNumber = confNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
}
