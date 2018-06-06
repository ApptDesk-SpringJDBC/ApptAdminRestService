package com.telappoint.admin.appt.common.model.response;


/**
 *  @author Balaji N
 */

public class GaugeChartResponse extends BaseResponse {
	private Integer selectedLocationId;
	private String selectedDate;
	private String gaugeStartDate;
	private String gaugeEndDate;
	private Long gaugeOpenedAppts;
	private Long gaugeBookedAppts;
	
	public Integer getSelectedLocationId() {
		return selectedLocationId;
	}
	public void setSelectedLocationId(Integer selectedLocationId) {
		this.selectedLocationId = selectedLocationId;
	}
	public String getSelectedDate() {
		return selectedDate;
	}
	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}
	public String getGaugeStartDate() {
		return gaugeStartDate;
	}
	public void setGaugeStartDate(String gaugeStartDate) {
		this.gaugeStartDate = gaugeStartDate;
	}
	public String getGaugeEndDate() {
		return gaugeEndDate;
	}
	public void setGaugeEndDate(String gaugeEndDate) {
		this.gaugeEndDate = gaugeEndDate;
	}
	public Long getGaugeOpenedAppts() {
		return gaugeOpenedAppts;
	}
	public void setGaugeOpenedAppts(Long gaugeOpenedAppts) {
		this.gaugeOpenedAppts = gaugeOpenedAppts;
	}
	public Long getGaugeBookedAppts() {
		return gaugeBookedAppts;
	}
	public void setGaugeBookedAppts(Long gaugeBookedAppts) {
		this.gaugeBookedAppts = gaugeBookedAppts;
	}
}
