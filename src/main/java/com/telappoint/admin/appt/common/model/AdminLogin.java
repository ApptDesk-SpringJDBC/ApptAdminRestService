package com.telappoint.admin.appt.common.model;

import com.telappoint.admin.appt.common.model.request.BaseRequest;

/**
 * 
 * @author Balaji N
 *
 */
public class AdminLogin extends BaseRequest {
	private Integer userLoginId;
    private Integer clientId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String contactPhone;
    private String contactEmail;
    private String locationIds;
    private String resourceIds;
    private String startDate;
    private String expiryDate;
    private String suspend="N";
    private String accessLevel;
    private String passwordLastUpdateDate;
    private String passwordUpdate="N";
    private String wrongLoginMaxAttemptLocked = "N";
    
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getLocationIds() {
		return locationIds;
	}
	public void setLocationIds(String locationIds) {
		this.locationIds = locationIds;
	}
	public String getResourceIds() {
		return resourceIds;
	}
	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getSuspend() {
		return suspend;
	}
	public void setSuspend(String suspend) {
		this.suspend = suspend;
	}
	public String getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	public String getPasswordLastUpdateDate() {
		return passwordLastUpdateDate;
	}
	public void setPasswordLastUpdateDate(String passwordLastUpdateDate) {
		this.passwordLastUpdateDate = passwordLastUpdateDate;
	}
	public String getWrongLoginMaxAttemptLocked() {
		return wrongLoginMaxAttemptLocked;
	}
	public void setWrongLoginMaxAttemptLocked(String wrongLoginMaxAttemptLocked) {
		this.wrongLoginMaxAttemptLocked = wrongLoginMaxAttemptLocked;
	}
	public Integer getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(Integer userLoginId) {
		this.userLoginId = userLoginId;
	}
	
	public String getPasswordUpdate() {
		return passwordUpdate;
	}
	public void setPasswordUpdate(String passwordUpdate) {
		this.passwordUpdate = passwordUpdate;
	}
}