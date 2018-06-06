package com.telappoint.admin.appt.common.model;

public class ResourceData {
	private Integer resourceId;
	private Long numberOfOpenSlots=new Long(0);
	private Long numberOfBookedAppts=new Long(0);
	public Long getNumberOfOpenSlots() {
		return numberOfOpenSlots;
	}
	public void setNumberOfOpenSlots(Long numberOfOpenSlots) {
		this.numberOfOpenSlots = numberOfOpenSlots;
	}
	public Long getNumberOfBookedAppts() {
		return numberOfBookedAppts;
	}
	public void setNumberOfBookedAppts(Long numberOfBookedAppts) {
		this.numberOfBookedAppts = numberOfBookedAppts;
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
}
