package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import com.telappoint.admin.appt.common.model.ResourceTitle;

public class ResourceTitleResponse extends BaseResponse {
	private List<ResourceTitle> resourceTitleList;

	public List<ResourceTitle> getResourceTitleList() {
		return resourceTitleList;
	}

	public void setResourceTitleList(List<ResourceTitle> resourceTitleList) {
		this.resourceTitleList = resourceTitleList;
	}
}
