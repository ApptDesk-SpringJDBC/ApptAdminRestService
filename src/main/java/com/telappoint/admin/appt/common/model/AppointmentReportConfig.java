package com.telappoint.admin.appt.common.model;

import com.telappoint.admin.appt.common.model.request.BaseRequest;

/**
 * 
 * @author Balaji N
 *
 */
public class AppointmentReportConfig extends BaseRequest {
	private String userName;
	private String timeStamp;
	private Integer clientId;
	private String clientName;
	private String reportName;
	private String locationIds;
	private String resourceIds;
	private String serviceIds;
	private String procedureIds;
	private String departmentIds;
	private String reportColumns="";
	private String apptStatusFetch;
	private String reportPath;
	private String email1;
	private String email2;
	private String email3;
	private String email4;
	private String email5;
	private String email6;
	private String sortBy1;
	private String sortBy2;
	private String sortBy3;
	private String sortBy4;
	private String sortBy5;
	private String reportStop;
	private Integer noIntervalHrs;
	private Integer reportNoDays;
	private String fileFormat;
	private String lastRunDate;
	private String enable;
	private String timeOfReport;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getLocationIds() {
		return locationIds;
	}
	public void setLocationIds(String locationIds) {
		this.locationIds = locationIds;
	}
	public String getResourceIds() {
		return resourceIds;
	}
	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}
	public String getServiceIds() {
		return serviceIds;
	}
	public void setServiceIds(String serviceIds) {
		this.serviceIds = serviceIds;
	}
	public String getProcedureIds() {
		return procedureIds;
	}
	public void setProcedureIds(String procedureIds) {
		this.procedureIds = procedureIds;
	}
	public String getDepartmentIds() {
		return departmentIds;
	}
	public void setDepartmentIds(String departmentIds) {
		this.departmentIds = departmentIds;
	}
	public String getReportColumns() {
		return reportColumns;
	}
	public void setReportColumns(String reportColumns) {
		this.reportColumns = reportColumns;
	}
	public String getApptStatusFetch() {
		return apptStatusFetch;
	}
	public void setApptStatusFetch(String apptStatusFetch) {
		this.apptStatusFetch = apptStatusFetch;
	}
	public String getReportPath() {
		return reportPath;
	}
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}
	public String getEmail1() {
		return email1;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	public String getEmail2() {
		return email2;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public String getEmail3() {
		return email3;
	}
	public void setEmail3(String email3) {
		this.email3 = email3;
	}
	public String getEmail4() {
		return email4;
	}
	public void setEmail4(String email4) {
		this.email4 = email4;
	}
	public String getEmail5() {
		return email5;
	}
	public void setEmail5(String email5) {
		this.email5 = email5;
	}
	public String getEmail6() {
		return email6;
	}
	public void setEmail6(String email6) {
		this.email6 = email6;
	}
	public String getSortBy1() {
		return sortBy1;
	}
	public void setSortBy1(String sortBy1) {
		this.sortBy1 = sortBy1;
	}
	public String getSortBy2() {
		return sortBy2;
	}
	public void setSortBy2(String sortBy2) {
		this.sortBy2 = sortBy2;
	}
	public String getSortBy3() {
		return sortBy3;
	}
	public void setSortBy3(String sortBy3) {
		this.sortBy3 = sortBy3;
	}
	public String getSortBy4() {
		return sortBy4;
	}
	public void setSortBy4(String sortBy4) {
		this.sortBy4 = sortBy4;
	}
	public String getSortBy5() {
		return sortBy5;
	}
	public void setSortBy5(String sortBy5) {
		this.sortBy5 = sortBy5;
	}
	public String getReportStop() {
		return reportStop;
	}
	public void setReportStop(String reportStop) {
		this.reportStop = reportStop;
	}
	public Integer getNoIntervalHrs() {
		return noIntervalHrs;
	}
	public void setNoIntervalHrs(Integer noIntervalHrs) {
		this.noIntervalHrs = noIntervalHrs;
	}
	public Integer getReportNoDays() {
		return reportNoDays;
	}
	public void setReportNoDays(Integer reportNoDays) {
		this.reportNoDays = reportNoDays;
	}
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public String getLastRunDate() {
		return lastRunDate;
	}
	public void setLastRunDate(String lastRunDate) {
		this.lastRunDate = lastRunDate;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getTimeOfReport() {
		return timeOfReport;
	}
	public void setTimeOfReport(String timeOfReport) {
		this.timeOfReport = timeOfReport;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}
