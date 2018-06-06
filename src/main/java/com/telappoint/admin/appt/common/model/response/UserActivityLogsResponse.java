package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.UserActivityLog;

public class UserActivityLogsResponse extends BaseResponse {
	private List<UserActivityLog> userActivityLogs;

	public List<UserActivityLog> getUserActivityLogs() {
		return userActivityLogs;
	}

	public void setUserActivityLogs(List<UserActivityLog> userActivityLogs) {
		this.userActivityLogs = userActivityLogs;
	}
}
