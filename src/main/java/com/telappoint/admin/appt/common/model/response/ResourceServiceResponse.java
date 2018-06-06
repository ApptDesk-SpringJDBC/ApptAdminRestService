package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import java.util.Map;

import com.telappoint.admin.appt.common.model.Resource;
import com.telappoint.admin.appt.common.model.ServiceVO;
/**
 * 
 * @author Balaji N
 *
 */
public class ResourceServiceResponse extends BaseResponse {
	private Map<Resource, List<ServiceVO>> resourceServices;

	public Map<Resource, List<ServiceVO>> getResourceServices() {
		return resourceServices;
	}

	public void setResourceServices(Map<Resource, List<ServiceVO>> resourceServices) {
		this.resourceServices = resourceServices;
	}
}
