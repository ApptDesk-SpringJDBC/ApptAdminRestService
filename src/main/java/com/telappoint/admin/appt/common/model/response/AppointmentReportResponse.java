package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.AppointmentReportData;
/**
 * 
 * @author Balaji N
 *
 */
public class AppointmentReportResponse extends BaseResponse {
	private List<AppointmentReportData> appointmentReportDataList;

	public List<AppointmentReportData> getAppointmentReportDataList() {
		return appointmentReportDataList;
	}

	public void setAppointmentReportDataList(List<AppointmentReportData> appointmentReportDataList) {
		this.appointmentReportDataList = appointmentReportDataList;
	}
	
}
