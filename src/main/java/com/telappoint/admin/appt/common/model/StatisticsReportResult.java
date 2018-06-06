package com.telappoint.admin.appt.common.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * @author Balaji
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class StatisticsReportResult {
	
	private List<StatisticReport> statisticsReportList;
	private int totalNoOfAppts;
	
	public int getTotalNoOfAppts() {
		return totalNoOfAppts;
	}
	public void setTotalNoOfAppts(int totalNoOfAppts) {
		this.totalNoOfAppts = totalNoOfAppts;
	}
	
	public List<StatisticReport> getStatisticsReportList() {
		return statisticsReportList;
	}
	public void setStatisticsReportList(List<StatisticReport> statisticsReportList) {
		this.statisticsReportList = statisticsReportList;
	}
}
