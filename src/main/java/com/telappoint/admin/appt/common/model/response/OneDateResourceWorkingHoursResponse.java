package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.admin.appt.common.model.OneDateResourceWorkingHoursDetails;

@JsonSerialize(include = Inclusion.NON_NULL)
public class OneDateResourceWorkingHoursResponse extends BaseResponse {
	private List<OneDateResourceWorkingHoursDetails> oneDateResourceWorkingHoursList;

	public List<OneDateResourceWorkingHoursDetails> getOneDateResourceWorkingHoursList() {
		return oneDateResourceWorkingHoursList;
	}

	public void setOneDateResourceWorkingHoursList(List<OneDateResourceWorkingHoursDetails> oneDateResourceWorkingHoursList) {
		this.oneDateResourceWorkingHoursList = oneDateResourceWorkingHoursList;
	}
}
