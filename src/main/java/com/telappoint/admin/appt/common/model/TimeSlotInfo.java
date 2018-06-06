package com.telappoint.admin.appt.common.model;
/**
 * 
 * @author Balaji N
 *
 */
public class TimeSlotInfo {
	private String time;
	private String timeDisplay;
	private String apptStatus;
	private Integer apptBlocks;
	private Long scheduleId;
	private boolean pledgeFlag;
	private boolean custFlag;
	private boolean notesFlag;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTimeDisplay() {
		return timeDisplay;
	}
	public void setTimeDisplay(String timeDisplay) {
		this.timeDisplay = timeDisplay;
	}
	public String getApptStatus() {
		return apptStatus;
	}
	public void setApptStatus(String apptStatus) {
		this.apptStatus = apptStatus;
	}
	public Integer getApptBlocks() {
		return apptBlocks;
	}
	public void setApptBlocks(Integer apptBlocks) {
		this.apptBlocks = apptBlocks;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public boolean isPledgeFlag() {
		return pledgeFlag;
	}
	public void setPledgeFlag(boolean pledgeFlag) {
		this.pledgeFlag = pledgeFlag;
	}
	public boolean isCustFlag() {
		return custFlag;
	}
	public void setCustFlag(boolean custFlag) {
		this.custFlag = custFlag;
	}
	public boolean isNotesFlag() {
		return notesFlag;
	}
	public void setNotesFlag(boolean notesFlag) {
		this.notesFlag = notesFlag;
	}
}
