package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CalendarData {
	private String time;
	private Integer rowSpan;
	private String apptStatus;
	private AppointmentReportData appointmentData;
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
	}

	public AppointmentReportData getAppointmentData() {
		return appointmentData;
	}

	public void setAppointmentData(AppointmentReportData appointmentData) {
		this.appointmentData = appointmentData;
	}

	public String getApptStatus() {
		return apptStatus;
	}

	public void setApptStatus(String apptStatus) {
		this.apptStatus = apptStatus;
	}
}
