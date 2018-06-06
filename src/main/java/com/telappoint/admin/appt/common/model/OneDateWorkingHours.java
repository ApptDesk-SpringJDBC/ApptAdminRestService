package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class OneDateWorkingHours {	
	private boolean dayOpen;
	private boolean breakTimeOpen;
	private String selectedStartTime;
	private String selectedEndTime;
	private String selectedBreakTime;
	private int selectedDuration;
	
	@JsonIgnore
	private String tempSelectedStartTime;
	@JsonIgnore
	private String tempSelectedEndTime;

	public String getTempSelectedStartTime() {
		return tempSelectedStartTime;
	}
	public void setTempSelectedStartTime(String tempSelectedStartTime) {
		this.tempSelectedStartTime = tempSelectedStartTime;
	}
	public String getTempSelectedEndTime() {
		return tempSelectedEndTime;
	}
	public void setTempSelectedEndTime(String tempSelectedEndTime) {
		this.tempSelectedEndTime = tempSelectedEndTime;
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
	
	public int getSelectedDuration() {
		return selectedDuration;
	}
	public void setSelectedDuration(int selectedDuration) {
		this.selectedDuration = selectedDuration;
	}
	
	public String getSelectedBreakTime() {
		return selectedBreakTime;
	}
	public void setSelectedBreakTime(String selectedBreakTime) {
		this.selectedBreakTime = selectedBreakTime;
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
}
