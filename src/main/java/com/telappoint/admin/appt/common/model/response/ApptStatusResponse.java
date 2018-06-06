package com.telappoint.admin.appt.common.model.response;

public class ApptStatusResponse extends BaseResponse {
	private Integer apptStatus;
	private String screened;
	private String accessed;
	public Integer getApptStatus() {
		return apptStatus;
	}
	public void setApptStatus(Integer apptStatus) {
		this.apptStatus = apptStatus;
	}
	public String getScreened() {
		return screened;
	}
	public void setScreened(String screened) {
		this.screened = screened;
	}
	public String getAccessed() {
		return accessed;
	}
	public void setAccessed(String accessed) {
		this.accessed = accessed;
	}
}
