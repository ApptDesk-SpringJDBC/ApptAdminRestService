package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.AppointmentStatusData;

public class AppointmentStatusDropDownResponse extends BaseResponse {
	private List<AppointmentStatusData> appointmentStatusList;

	public List<AppointmentStatusData> getAppointmentStatusList() {
		return appointmentStatusList;
	}

	public void setAppointmentStatusList(List<AppointmentStatusData> appointmentStatusList) {
		this.appointmentStatusList = appointmentStatusList;
	}
}
