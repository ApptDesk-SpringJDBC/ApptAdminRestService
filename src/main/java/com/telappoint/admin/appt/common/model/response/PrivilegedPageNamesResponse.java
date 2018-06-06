package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import java.util.Map;
/**
 * 
 * @author Balaji N
 *
 */
public class PrivilegedPageNamesResponse extends BaseResponse {
	private Map<String, List<String>> previlegePageNames;

	public Map<String, List<String>> getPrevilegePageNames() {
		return previlegePageNames;
	}

	public void setPrevilegePageNames(Map<String, List<String>> previlegePageNames) {
		this.previlegePageNames = previlegePageNames;
	}
}
