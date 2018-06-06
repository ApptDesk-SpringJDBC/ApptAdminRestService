package com.telappoint.admin.appt.common.model.response;

import java.util.ArrayList;
import java.util.List;
import com.telappoint.admin.appt.common.model.AppointmentDetails;

public class AppointmentsDataResponse extends BaseResponse {
	private List<AppointmentDetails> apptDetails = new ArrayList<AppointmentDetails>();
	private String message;

	public List<AppointmentDetails> getBookedAppts() {
		return apptDetails;
	}

	public void setBookedAppts(List<AppointmentDetails> bookedAppts) {
		this.apptDetails = bookedAppts;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
