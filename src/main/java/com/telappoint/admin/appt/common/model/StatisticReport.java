package com.telappoint.admin.appt.common.model;

public class StatisticReport {
	private String name;
	private Integer noOfBookedAppts;
	private Long totalNoOfAppts;
	private Integer noOfOtherAppts=0;
	
	// for location-service statistic report
	private String locationName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNoOfBookedAppts() {
		return noOfBookedAppts;
	}
	public void setNoOfBookedAppts(Integer noOfBookedAppts) {
		this.noOfBookedAppts = noOfBookedAppts;
	}
	public Long getTotalNoOfAppts() {
		return totalNoOfAppts;
	}
	public void setTotalNoOfAppts(Long totalNoOfAppts) {
		this.totalNoOfAppts = totalNoOfAppts;
	}
	public Integer getNoOfOtherAppts() {
		return noOfOtherAppts;
	}
	public void setNoOfOtherAppts(Integer noOfOtherAppts) {
		this.noOfOtherAppts = noOfOtherAppts;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
}
