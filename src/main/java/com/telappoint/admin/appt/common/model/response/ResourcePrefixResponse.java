package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import com.telappoint.admin.appt.common.model.ResourcePrefix;

public class ResourcePrefixResponse extends BaseResponse {
	private List<ResourcePrefix> resourcePrefixList;

	public List<ResourcePrefix> getResourcePrefixList() {
		return resourcePrefixList;
	}

	public void setResourcePrefixList(List<ResourcePrefix> resourcePrefixList) {
		this.resourcePrefixList = resourcePrefixList;
	}
}
