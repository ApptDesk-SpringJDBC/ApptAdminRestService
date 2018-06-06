package com.telappoint.admin.appt.common.model;

public class UserActivityLog {
	private int userId;
	private String userName;
	private String sessionId;
	private int pageId;
	private String pageName;
	private int clickId;
	private String clickName;
	private String summaryLog;
	private String timestampStr;
	private String userFirstName;
	private String userLastName;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public int getClickId() {
		return clickId;
	}
	public void setClickId(int clickId) {
		this.clickId = clickId;
	}
	public String getClickName() {
		return clickName;
	}
	public void setClickName(String clickName) {
		this.clickName = clickName;
	}
	public String getSummaryLog() {
		return summaryLog;
	}
	public void setSummaryLog(String summaryLog) {
		this.summaryLog = summaryLog;
	}
	public String getTimestampStr() {
		return timestampStr;
	}
	public void setTimestampStr(String timestampStr) {
		this.timestampStr = timestampStr;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
}
