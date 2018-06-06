package com.telappoint.admin.appt.common.model.request;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResourceWorkingHoursRequest {
	private String clientCode;
	private boolean dayOpen;
	private boolean breakTimeOpen;
	private List<String> dates;
	private String selectedStartTime;
	private String selectedEndTime;
	private String selectedBreakTime;
	private Integer selectedDuration;
	private List<Integer> selectedResourceIds;
	private boolean continueUpdate;
	private String notifyCheckBox;
	private String userName;
	private Integer locationId;

	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	
	public boolean isDayOpen() {
		return dayOpen;
	}
	public void setDayOpen(boolean dayOpen) {
		this.dayOpen = dayOpen;
	}
	public boolean isBreakTimeOpen() {
		return breakTimeOpen;
	}
	public void setBreakTimeOpen(boolean breakTimeOpen) {
		this.breakTimeOpen = breakTimeOpen;
	}
	public List<String> getDates() {
		return dates;
	}
	public void setDates(List<String> dates) {
		this.dates = dates;
	}
	public String getSelectedStartTime() {
		return selectedStartTime;
	}
	public void setSelectedStartTime(String selectedStartTime) {
		this.selectedStartTime = selectedStartTime;
	}
	public String getSelectedEndTime() {
		return selectedEndTime;
	}
	public void setSelectedEndTime(String selectedEndTime) {
		this.selectedEndTime = selectedEndTime;
	}
	public String getSelectedBreakTime() {
		return selectedBreakTime;
	}
	public void setSelectedBreakTime(String selectedBreakTime) {
		this.selectedBreakTime = selectedBreakTime;
	}
	public Integer getSelectedDuration() {
		return selectedDuration;
	}
	public void setSelectedDuration(Integer selectedDuration) {
		this.selectedDuration = selectedDuration;
	}
	public List<Integer> getSelectedResourceIds() {
		return selectedResourceIds;
	}
	public void setSelectedResourceIds(List<Integer> selectedResourceIds) {
		this.selectedResourceIds = selectedResourceIds;
	}
	
	public String getNotifyCheckBox() {
		return notifyCheckBox;
	}
	public void setNotifyCheckBox(String notifyCheckBox) {
		this.notifyCheckBox = notifyCheckBox;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public boolean isContinueUpdate() {
		return continueUpdate;
	}
	public void setContinueUpdate(boolean continueUpdate) {
		this.continueUpdate = continueUpdate;
	}
	
	
}
