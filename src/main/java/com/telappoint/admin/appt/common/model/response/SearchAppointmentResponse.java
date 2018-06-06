package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import java.util.Map;

import com.telappoint.admin.appt.common.model.AppointmentData;
import com.telappoint.admin.appt.common.model.Customer;
import com.telappoint.admin.appt.common.model.SearchAppointmentData;

/**
 * 
 * @author Balaji N
 *
 */
public class SearchAppointmentResponse extends BaseResponse {
	private List<SearchAppointmentData> searchApptList;
	private Map<Customer, List<AppointmentData>> searchAppointmentList;

	public List<SearchAppointmentData> getSearchApptList() {
		return searchApptList;
	}

	public void setSearchApptList(List<SearchAppointmentData> searchApptList) {
		this.searchApptList = searchApptList;
	}

	public Map<Customer, List<AppointmentData>> getSearchAppointmentList() {
		return searchAppointmentList;
	}

	public void setSearchAppointmentList(Map<Customer, List<AppointmentData>> searchAppointmentList) {
		this.searchAppointmentList = searchAppointmentList;
	}
}
