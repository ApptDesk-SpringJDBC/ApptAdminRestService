package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import com.telappoint.admin.appt.common.model.OutLookAppointment;

/**
 * 
 * @author Balaji N
 *
 */
public class OutLookResponse extends BaseResponse {
	private List<OutLookAppointment> outLookApptList;

	public List<OutLookAppointment> getOutLookApptList() {
		return outLookApptList;
	}

	public void setOutLookApptList(List<OutLookAppointment> outLookApptList) {
		this.outLookApptList = outLookApptList;
	}
}
