package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import java.util.Map;

import com.telappoint.admin.appt.common.model.PledgeDetails;

/**
 * 
 * @author Balaji N
 *
 */
public class PledgeReportResponse extends BaseResponse {
	private  Map<String, List<PledgeDetails>> pledgeReportData;

	public Map<String, List<PledgeDetails>> getPledgeReportData() {
		return pledgeReportData;
	}

	public void setPledgeReportData(Map<String, List<PledgeDetails>> pledgeReportData) {
		this.pledgeReportData = pledgeReportData;
	}
}
