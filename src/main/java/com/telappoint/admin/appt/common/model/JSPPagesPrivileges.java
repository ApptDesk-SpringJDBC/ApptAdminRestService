package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class JSPPagesPrivileges {
	private Long privilegeId;
	private String groupTitle;
	private String pagesTitle;
	private String jspPages;
	private String jspPageDesc;
	private String enableFlag;
	private String superUser;
	private String administrator;
	private String manager;
	private String location;
	private String provider;
	private String scheduler;
	private String readOnly;
	
	
	public Long getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(Long privilegeId) {
		this.privilegeId = privilegeId;
	}
	public String getGroupTitle() {
		return groupTitle;
	}
	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	public String getPagesTitle() {
		return pagesTitle;
	}
	public void setPagesTitle(String pagesTitle) {
		this.pagesTitle = pagesTitle;
	}
	public String getJspPages() {
		return jspPages;
	}
	public void setJspPages(String jspPages) {
		this.jspPages = jspPages;
	}
	public String getJspPageDesc() {
		return jspPageDesc;
	}
	public void setJspPageDesc(String jspPageDesc) {
		this.jspPageDesc = jspPageDesc;
	}
	public String getEnableFlag() {
		return enableFlag;
	}
	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}
	public String getSuperUser() {
		return superUser;
	}
	public void setSuperUser(String superUser) {
		this.superUser = superUser;
	}
	public String getAdministrator() {
		return administrator;
	}
	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getScheduler() {
		return scheduler;
	}
	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}
	public String getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
}
