package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AppointmentStatusData {
	private Integer id;
	private String status;
	private Integer statusVal;
	private String statusValStr;
	private String denied;
	private String placeHolderName;
	private String reportDisplay;
	
	@JsonIgnore
	private String blockedFromFutureAppts;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getStatusVal() {
		return statusVal;
	}
	public void setStatusVal(Integer statusVal) {
		this.statusVal = statusVal;
	}
	public String getBlockedFromFutureAppts() {
		return blockedFromFutureAppts;
	}
	public void setBlockedFromFutureAppts(String blockedFromFutureAppts) {
		this.blockedFromFutureAppts = blockedFromFutureAppts;
	}
	public String getDenied() {
		return denied;
	}
	public void setDenied(String denied) {
		this.denied = denied;
	}
	public String getReportDisplay() {
		return reportDisplay;
	}
	public void setReportDisplay(String reportDisplay) {
		this.reportDisplay = reportDisplay;
	}
	public String getStatusValStr() {
		return statusValStr;
	}
	public void setStatusValStr(String statusValStr) {
		this.statusValStr = statusValStr;
	}
	public String getPlaceHolderName() {
		return placeHolderName;
	}
	public void setPlaceHolderName(String placeHolderName) {
		this.placeHolderName = placeHolderName;
	}
	
}
