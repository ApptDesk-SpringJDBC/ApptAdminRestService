package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class PrivilegePageMapping {
	private long id;
	private long accessPrivilegeId;
	private String accessPrivilegeName;
	private String pageName;
	private boolean selected;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAccessPrivilegeId() {
		return accessPrivilegeId;
	}
	public void setAccessPrivilegeId(long accessPrivilegeId) {
		this.accessPrivilegeId = accessPrivilegeId;
	}
	public String getAccessPrivilegeName() {
		return accessPrivilegeName;
	}
	public void setAccessPrivilegeName(String accessPrivilegeName) {
		this.accessPrivilegeName = accessPrivilegeName;
	}

	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
