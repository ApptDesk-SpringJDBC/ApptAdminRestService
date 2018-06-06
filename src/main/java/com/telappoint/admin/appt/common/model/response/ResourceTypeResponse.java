package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.ResourceType;

public class ResourceTypeResponse extends BaseResponse {
	private List<ResourceType> resourceTypeList;

	public List<ResourceType> getResourceTypeList() {
		return resourceTypeList;
	}

	public void setResourceTypeList(List<ResourceType> resourceTypeList) {
		this.resourceTypeList = resourceTypeList;
	}	
}