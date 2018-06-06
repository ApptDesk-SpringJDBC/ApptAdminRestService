package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.admin.appt.common.model.OutBoundCalls;

/**
 * 
 * @author Balaji 
 * 
 */

@JsonSerialize(include = Inclusion.NON_NULL)
public class OutBoundCallsResponse extends BaseResponse {
	private String totalMinutes;	
	private List<OutBoundCalls> outBoundCallLogs;
	
	public String getTotalMinutes() {
		return totalMinutes;
	}
	public void setTotalMinutes(String totalMinutes) {
		this.totalMinutes = totalMinutes;
	}
	public List<OutBoundCalls> getOutBoundCallLogs() {
		return outBoundCallLogs;
	}
	public void setOutBoundCallLogs(List<OutBoundCalls> outBoundCallLogs) {
		this.outBoundCallLogs = outBoundCallLogs;
	}	
}
