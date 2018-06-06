package com.telappoint.admin.appt.common.model.response;
import java.util.List;

import com.telappoint.admin.appt.common.model.AppointmentReportConfig;

/**
 * 
 * @author Balaji N
 *
 */

public class AppointmentReportConfigResponse extends BaseResponse {
	private List<AppointmentReportConfig> apptReportConfigList;

	public List<AppointmentReportConfig> getApptReportConfigList() {
		return apptReportConfigList;
	}

	public void setApptReportConfigList(List<AppointmentReportConfig> apptReportConfigList) {
		this.apptReportConfigList = apptReportConfigList;
	}
	
}
