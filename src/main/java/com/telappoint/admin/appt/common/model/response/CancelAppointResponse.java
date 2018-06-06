package com.telappoint.admin.appt.common.model.response;

public class CancelAppointResponse extends BaseResponse {
	private boolean isCancelled;
	private String displayKeys;
	private String displayValues;
	
	private String message;
	public boolean isCancelled() {
		return isCancelled;
	}
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDisplayKeys() {
		return displayKeys;
	}
	public void setDisplayKeys(String displayKeys) {
		this.displayKeys = displayKeys;
	}
	public String getDisplayValues() {
		return displayValues;
	}
	public void setDisplayValues(String displayValues) {
		this.displayValues = displayValues;
	}
}
