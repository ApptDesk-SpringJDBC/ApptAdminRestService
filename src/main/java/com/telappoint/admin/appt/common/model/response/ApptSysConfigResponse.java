package com.telappoint.admin.appt.common.model.response;

import com.telappoint.admin.appt.common.model.ApptSysConfig;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ApptSysConfigResponse extends BaseResponse {
	private ApptSysConfig apptSysConfig;

	public ApptSysConfig getApptSysConfig() {
		return apptSysConfig;
	}

	public void setApptSysConfig(ApptSysConfig apptSysConfig) {
		this.apptSysConfig = apptSysConfig;
	}
}
