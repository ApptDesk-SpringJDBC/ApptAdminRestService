package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.admin.appt.common.model.request.BaseRequest;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Location extends BaseRequest {
	private Integer locationId;
	private String locationNameOnline;
	private String locationNameMobile;
	private String locationNameSMS;
	private String locationNameRemindSMS;
	private String locationNameIvrTts;
	private String locationNameIvrAudio;
	private String locationGoogleMap;
	private String locationGoogleMapLink;
	private String timeZone;
	private String comment;
	private String deleteFlag;
	private String enable;
	private String closed;
	private String closedMessage;
	private String closedAudio;
	private String closedTts;
	
	private String apptStartDate;
	private String apptEndDate;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String workPhone;
	private Integer placement;

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getLocationNameOnline() {
		return locationNameOnline;
	}

	public void setLocationNameOnline(String locationNameOnline) {
		this.locationNameOnline = locationNameOnline;
	}

	public String getApptStartDate() {
		return apptStartDate;
	}

	public void setApptStartDate(String apptStartDate) {
		this.apptStartDate = apptStartDate;
	}

	public String getApptEndDate() {
		return apptEndDate;
	}

	public void setApptEndDate(String apptEndDate) {
		this.apptEndDate = apptEndDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public Integer getPlacement() {
		return placement;
	}

	public void setPlacement(Integer placement) {
		this.placement = placement;
	}

	public String getLocationNameIvrTts() {
		return locationNameIvrTts;
	}

	public void setLocationNameIvrTts(String locationNameIvrTts) {
		this.locationNameIvrTts = locationNameIvrTts;
	}

	public String getLocationNameIvrAudio() {
		return locationNameIvrAudio;
	}

	public void setLocationNameIvrAudio(String locationNameIvrAudio) {
		this.locationNameIvrAudio = locationNameIvrAudio;
	}

	public String getLocationGoogleMap() {
		return locationGoogleMap;
	}

	public void setLocationGoogleMap(String locationGoogleMap) {
		this.locationGoogleMap = locationGoogleMap;
	}

	public String getLocationGoogleMapLink() {
		return locationGoogleMapLink;
	}

	public void setLocationGoogleMapLink(String locationGoogleMapLink) {
		this.locationGoogleMapLink = locationGoogleMapLink;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}

	public String getClosedMessage() {
		return closedMessage;
	}

	public void setClosedMessage(String closedMessage) {
		this.closedMessage = closedMessage;
	}

	public String getClosedAudio() {
		return closedAudio;
	}

	public void setClosedAudio(String closedAudio) {
		this.closedAudio = closedAudio;
	}

	public String getClosedTts() {
		return closedTts;
	}

	public void setClosedTts(String closedTts) {
		this.closedTts = closedTts;
	}

	public String getLocationNameMobile() {
		return locationNameMobile;
	}

	public void setLocationNameMobile(String locationNameMobile) {
		this.locationNameMobile = locationNameMobile;
	}

	public String getLocationNameSMS() {
		return locationNameSMS;
	}

	public void setLocationNameSMS(String locationNameSMS) {
		this.locationNameSMS = locationNameSMS;
	}

	public String getLocationNameRemindSMS() {
		return locationNameRemindSMS;
	}

	public void setLocationNameRemindSMS(String locationNameRemindSMS) {
		this.locationNameRemindSMS = locationNameRemindSMS;
	}
}