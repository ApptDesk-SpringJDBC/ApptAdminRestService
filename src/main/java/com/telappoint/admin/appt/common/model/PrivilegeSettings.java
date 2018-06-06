package com.telappoint.admin.appt.common.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrivilegeSettings {
	private List<AccessPrivilege> accessPrivilegeList;
	private List<PrivilegePageMapping> privilegePageMappingList;
	private List<JSPPageNames> jspPageNamesList;
	private long selectedAccessPrivilegeId;
	private String selectedAccessPrivilege;
	private List<String> privilegedPageNames;
	private String clientCode;
	private Map<String,List<JSPPageNames>> jspPagesPrivilegesMap;
	
	public List<AccessPrivilege> getAccessPrivilegeList() {
		return accessPrivilegeList;
	}
	public void setAccessPrivilegeList(List<AccessPrivilege> accessPrivilegeList) {
		this.accessPrivilegeList = accessPrivilegeList;
	}
	public List<PrivilegePageMapping> getPrivilegePageMappingList() {
		return privilegePageMappingList;
	}
	public void setPrivilegePageMappingList(List<PrivilegePageMapping> privilegePageMappingList) {
		this.privilegePageMappingList = privilegePageMappingList;
	}
	public List<JSPPageNames> getJspPageNamesList() {
		return jspPageNamesList;
	}
	public void setJspPageNamesList(List<JSPPageNames> jspPageNamesList) {
		this.jspPageNamesList = jspPageNamesList;
	}
	public long getSelectedAccessPrivilegeId() {
		return selectedAccessPrivilegeId;
	}
	public void setSelectedAccessPrivilegeId(long selectedAccessPrivilegeId) {
		this.selectedAccessPrivilegeId = selectedAccessPrivilegeId;
	}
	public String getSelectedAccessPrivilege() {
		return selectedAccessPrivilege;
	}
	public void setSelectedAccessPrivilege(String selectedAccessPrivilege) {
		this.selectedAccessPrivilege = selectedAccessPrivilege;
	}
	public List<String> getPrivilegedPageNames() {
		return privilegedPageNames;
	}
	public void setPrivilegedPageNames(List<String> privilegedPageNames) {
		this.privilegedPageNames = privilegedPageNames;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public Map<String, List<JSPPageNames>> getJspPagesPrivilegesMap() {
		return jspPagesPrivilegesMap;
	}
	public void setJspPagesPrivilegesMap(LinkedHashMap<String, List<JSPPageNames>> jspPagesPrivilegesMap) {
		this.jspPagesPrivilegesMap = jspPagesPrivilegesMap;
	}
}
