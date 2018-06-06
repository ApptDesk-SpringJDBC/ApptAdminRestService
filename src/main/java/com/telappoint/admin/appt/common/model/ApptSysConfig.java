package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Balaji, Koti
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ApptSysConfig {
	private String displayCompany;
	private String displayProcedure;
    private String displayLocation;
    private String displayDepartment;
    private String displayResource;
    private String displayService;
    private String allowAnyResource;
    private String schedulerClosed;
    private String restrictApptWindow;
    private String oneApptPerTerm;
    private String termStartDate;
    private String termEndDate;
    private String noApptPerTerm;
    private String apptStartDate;
    private String apptEndDate;
    private String restrictLocApptWindow;
    private String restrictLocSerApptWindow;
    private String fundingBasedScheduler;
    private String defaultDayStartTime;
    private String defaultDayEndTime;
    
    private String defaultIsSunOpen;
    private String defaultIsMonOpen;
    private String defaultIsTueOpen;
    private String defaultIsWedOpen;
    private String defaultIsThuOpen;
    private String defaultIsFriOpen;
    private String defaultIsSatOpen;
    private String ccConfirmEmails;
	private String ccCancalEmails;
	private String sendReschdEmail;
    
	public String getCcConfirmEmails() {
		return ccConfirmEmails;
	}
	public void setCcConfirmEmails(String ccConfirmEmails) {
		this.ccConfirmEmails = ccConfirmEmails;
	}
	public String getCcCancalEmails() {
		return ccCancalEmails;
	}
	public void setCcCancalEmails(String ccCancalEmails) {
		this.ccCancalEmails = ccCancalEmails;
	}
	public String getSendReschdEmail() {
		return sendReschdEmail;
	}
	public void setSendReschdEmail(String sendReschdEmail) {
		this.sendReschdEmail = sendReschdEmail;
	}
	public String getDisplayProcedure() {
		return displayProcedure;
	}
	public void setDisplayProcedure(String displayProcedure) {
		this.displayProcedure = displayProcedure;
	}
	public String getDisplayLocation() {
		return displayLocation;
	}
	public void setDisplayLocation(String displayLocation) {
		this.displayLocation = displayLocation;
	}
	public String getDisplayDepartment() {
		return displayDepartment;
	}
	public void setDisplayDepartment(String displayDepartment) {
		this.displayDepartment = displayDepartment;
	}
	public String getDisplayResource() {
		return displayResource;
	}
	public void setDisplayResource(String displayResource) {
		this.displayResource = displayResource;
	}
	public String getDisplayService() {
		return displayService;
	}
	public void setDisplayService(String displayService) {
		this.displayService = displayService;
	}
	public String getDisplayCompany() {
		return displayCompany;
	}
	public void setDisplayCompany(String displayCompany) {
		this.displayCompany = displayCompany;
	}
	public String getAllowAnyResource() {
		return allowAnyResource;
	}
	public void setAllowAnyResource(String allowAnyResource) {
		this.allowAnyResource = allowAnyResource;
	}
	public String getSchedulerClosed() {
		return schedulerClosed;
	}
	public void setSchedulerClosed(String schedulerClosed) {
		this.schedulerClosed = schedulerClosed;
	}
	public String getRestrictApptWindow() {
		return restrictApptWindow;
	}
	public void setRestrictApptWindow(String restrictApptWindow) {
		this.restrictApptWindow = restrictApptWindow;
	}
	public String getOneApptPerTerm() {
		return oneApptPerTerm;
	}
	public void setOneApptPerTerm(String oneApptPerTerm) {
		this.oneApptPerTerm = oneApptPerTerm;
	}
	public String getTermStartDate() {
		return termStartDate;
	}
	public void setTermStartDate(String termStartDate) {
		this.termStartDate = termStartDate;
	}
	public String getTermEndDate() {
		return termEndDate;
	}
	public void setTermEndDate(String termEndDate) {
		this.termEndDate = termEndDate;
	}
	public String getNoApptPerTerm() {
		return noApptPerTerm;
	}
	public void setNoApptPerTerm(String noApptPerTerm) {
		this.noApptPerTerm = noApptPerTerm;
	}
	public String getApptStartDate() {
		return apptStartDate;
	}
	public void setApptStartDate(String apptStartDate) {
		this.apptStartDate = apptStartDate;
	}
	public String getApptEndDate() {
		return apptEndDate;
	}
	public void setApptEndDate(String apptEndDate) {
		this.apptEndDate = apptEndDate;
	}
	public String getRestrictLocApptWindow() {
		return restrictLocApptWindow;
	}
	public void setRestrictLocApptWindow(String restrictLocApptWindow) {
		this.restrictLocApptWindow = restrictLocApptWindow;
	}
	public String getRestrictLocSerApptWindow() {
		return restrictLocSerApptWindow;
	}
	public void setRestrictLocSerApptWindow(String restrictLocSerApptWindow) {
		this.restrictLocSerApptWindow = restrictLocSerApptWindow;
	}
	public String getFundingBasedScheduler() {
		return fundingBasedScheduler;
	}
	public void setFundingBasedScheduler(String fundingBasedScheduler) {
		this.fundingBasedScheduler = fundingBasedScheduler;
	}
	public String getDefaultDayStartTime() {
		return defaultDayStartTime;
	}
	public void setDefaultDayStartTime(String defaultDayStartTime) {
		this.defaultDayStartTime = defaultDayStartTime;
	}
	public String getDefaultDayEndTime() {
		return defaultDayEndTime;
	}
	public void setDefaultDayEndTime(String defaultDayEndTime) {
		this.defaultDayEndTime = defaultDayEndTime;
	}
	public String getDefaultIsSunOpen() {
		return defaultIsSunOpen;
	}
	public void setDefaultIsSunOpen(String defaultIsSunOpen) {
		this.defaultIsSunOpen = defaultIsSunOpen;
	}
	public String getDefaultIsMonOpen() {
		return defaultIsMonOpen;
	}
	public void setDefaultIsMonOpen(String defaultIsMonOpen) {
		this.defaultIsMonOpen = defaultIsMonOpen;
	}
	public String getDefaultIsTueOpen() {
		return defaultIsTueOpen;
	}
	public void setDefaultIsTueOpen(String defaultIsTueOpen) {
		this.defaultIsTueOpen = defaultIsTueOpen;
	}
	public String getDefaultIsWedOpen() {
		return defaultIsWedOpen;
	}
	public void setDefaultIsWedOpen(String defaultIsWedOpen) {
		this.defaultIsWedOpen = defaultIsWedOpen;
	}
	public String getDefaultIsThuOpen() {
		return defaultIsThuOpen;
	}
	public void setDefaultIsThuOpen(String defaultIsThuOpen) {
		this.defaultIsThuOpen = defaultIsThuOpen;
	}
	public String getDefaultIsFriOpen() {
		return defaultIsFriOpen;
	}
	public void setDefaultIsFriOpen(String defaultIsFriOpen) {
		this.defaultIsFriOpen = defaultIsFriOpen;
	}
	public String getDefaultIsSatOpen() {
		return defaultIsSatOpen;
	}
	public void setDefaultIsSatOpen(String defaultIsSatOpen) {
		this.defaultIsSatOpen = defaultIsSatOpen;
	}
}
