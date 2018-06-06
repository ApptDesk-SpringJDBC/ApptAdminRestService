package com.telappoint.admin.appt.common.model.response;

public class ConfirmAppointmentResponse extends BaseResponse {
	private Long confirmation;
	private String displayKeys;
	private String displayValues;
	
	public Long getConfirmation() {
		return confirmation;
	}
	public void setConfirmation(Long confirmation) {
		this.confirmation = confirmation;
	}
	
	public String getDisplayValues() {
		return displayValues;
	}
	public void setDisplayValues(String displayValues) {
		this.displayValues = displayValues;
	}
	public String getDisplayKeys() {
		return displayKeys;
	}
	public void setDisplayKeys(String displayKeys) {
		this.displayKeys = displayKeys;
	}
	public String getErrorFlag() {
		return errorFlag;
	}
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
