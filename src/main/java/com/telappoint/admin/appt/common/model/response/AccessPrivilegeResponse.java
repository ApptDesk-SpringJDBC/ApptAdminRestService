package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.AccessPrivilege;

public class AccessPrivilegeResponse extends BaseResponse {
	private List<AccessPrivilege> accessPrivilegeList;
	private List<String> privilegeNames;

	public List<AccessPrivilege> getAccessPrivilegeList() {
		return accessPrivilegeList;
	}

	public void setAccessPrivilegeList(List<AccessPrivilege> accessPrivilegeList) {
		this.accessPrivilegeList = accessPrivilegeList;
	}

	public List<String> getPrivilegeNames() {
		return privilegeNames;
	}

	public void setPrivilegeNames(List<String> privilegeNames) {
		this.privilegeNames = privilegeNames;
	}	
}
