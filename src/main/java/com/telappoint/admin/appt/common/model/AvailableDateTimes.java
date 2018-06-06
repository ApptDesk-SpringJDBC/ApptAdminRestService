package com.telappoint.admin.appt.common.model;

import java.util.List;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.admin.appt.common.model.response.BaseResponse;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AvailableDateTimes extends BaseResponse {
    private String availableDates;
    private List<String> availableDatesArray;
    private String availableTimes;
    private List<String> availableTimesArray;
    private String displayTimeList;
    private String warningFlag;
    private String warningMessage;
    private String errorFlag;
	private String errorMessage;
	private String timeZone;
	

	public String getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(String availableDates) {
		this.availableDates = availableDates;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getAvailableTimes() {
		return availableTimes;
	}

	public void setAvailableTimes(String availableTimes) {
		this.availableTimes = availableTimes;
	}

	public String getDisplayTimeList() {
		return displayTimeList;
	}

	public void setDisplayTimeList(String displayTimeList) {
		this.displayTimeList = displayTimeList;
	}
	
	public AvailableDateTimes() {
		
	}
	
	public AvailableDateTimes(String availableDates, String errorMessage) {
		this.availableDates = availableDates;
		this.errorMessage = errorMessage;
	}
	
	public AvailableDateTimes(String availableDates, boolean status, String errorMessage) {
		this.status=status;
		this.availableDates = availableDates;
		this.errorMessage = errorMessage;
	}

	public AvailableDateTimes(String availableDates, String availableTimes, String errorMessage) {
		this.availableDates = availableDates;
		this.availableTimes = availableTimes;
		this.errorMessage = errorMessage;
	}

	public AvailableDateTimes(String availableDates, String availableTimes, String displayTimeList, String errorMessage) {
		this.availableDates = availableDates;
		this.availableTimes = availableTimes;
		this.displayTimeList = displayTimeList;
		this.errorMessage = errorMessage;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getWarningFlag() {
		return warningFlag;
	}

	public void setWarningFlag(String warningFlag) {
		this.warningFlag = warningFlag;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	
	public String getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}

	public List<String> getAvailableDatesArray() {
		return availableDatesArray;
	}

	public void setAvailableDatesArray(List<String> availableDatesArray) {
		this.availableDatesArray = availableDatesArray;
	}

	public List<String> getAvailableTimesArray() {
		return availableTimesArray;
	}

	public void setAvailableTimesArray(List<String> availableTimesArray) {
		this.availableTimesArray = availableTimesArray;
	}
}
