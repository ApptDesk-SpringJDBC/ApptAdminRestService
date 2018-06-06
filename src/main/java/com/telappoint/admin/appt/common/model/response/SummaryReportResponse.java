package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.admin.appt.common.model.StatisticsReportResult;
import com.telappoint.admin.appt.common.model.SummaryStatisticsResult;
/**
 * 
 * @author Balaji N
 *
 */
@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SummaryReportResponse extends BaseResponse {
	private StatisticsReportResult procedureStatReports;
	private StatisticsReportResult locationStatReports;
	private StatisticsReportResult resourceStatReports;
	private StatisticsReportResult serviceStatReports;	
	private StatisticsReportResult locationServiceStatReports;
	
	// summary report by D/W/M
	private List<SummaryStatisticsResult> summaryStatisticsResults;
	private int summaryTotalNoOfAppts;
	
	public StatisticsReportResult getProcedureStatReports() {
		return procedureStatReports;
	}
	public void setProcedureStatReports(StatisticsReportResult procedureStatReports) {
		this.procedureStatReports = procedureStatReports;
	}
	public StatisticsReportResult getLocationStatReports() {
		return locationStatReports;
	}
	public void setLocationStatReports(StatisticsReportResult locationStatReports) {
		this.locationStatReports = locationStatReports;
	}
	public StatisticsReportResult getServiceStatReports() {
		return serviceStatReports;
	}
	public void setServiceStatReports(StatisticsReportResult serviceStatReports) {
		this.serviceStatReports = serviceStatReports;
	}
	public StatisticsReportResult getLocationServiceStatReports() {
		return locationServiceStatReports;
	}
	public void setLocationServiceStatReports(StatisticsReportResult locationServiceStatReports) {
		this.locationServiceStatReports = locationServiceStatReports;
	}
	public StatisticsReportResult getResourceStatReports() {
		return resourceStatReports;
	}
	public void setResourceStatReports(StatisticsReportResult resourceStatReports) {
		this.resourceStatReports = resourceStatReports;
	}
	
	
	public List<SummaryStatisticsResult> getSummaryStatisticsResults() {
		return summaryStatisticsResults;
	}
	public void setSummaryStatisticsResults(List<SummaryStatisticsResult> summaryStatisticsResults) {
		this.summaryStatisticsResults = summaryStatisticsResults;
	}
	public int getSummaryTotalNoOfAppts() {
		return summaryTotalNoOfAppts;
	}
	public void setSummaryTotalNoOfAppts(int summaryTotalNoOfAppts) {
		this.summaryTotalNoOfAppts = summaryTotalNoOfAppts;
	}	
}
