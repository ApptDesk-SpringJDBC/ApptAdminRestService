package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.telappoint.admin.appt.common.model.ServiceVO;
/**
 * 
 * @author Balaji N
 *
 */
@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServiceResponse extends BaseResponse {
	// populated if more then one services.
	private List<ServiceVO> serviceList;
	private List<ServiceVO> deletedServiceList;
	private Integer blocksTimeInMins;
	private Integer serviceId;
	
	//populated if one service
	private ServiceVO service;

	public ServiceVO getService() {
		return service;
	}

	public void setService(ServiceVO service) {
		this.service = service;
	}

	public List<ServiceVO> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<ServiceVO> serviceList) {
		this.serviceList = serviceList;
	}

	public List<ServiceVO> getDeletedServiceList() {
		return deletedServiceList;
	}

	public void setDeletedServiceList(List<ServiceVO> deletedServiceList) {
		this.deletedServiceList = deletedServiceList;
	}

	public Integer getBlocksTimeInMins() {
		return blocksTimeInMins;
	}

	public void setBlocksTimeInMins(Integer blocksTimeInMins) {
		this.blocksTimeInMins = blocksTimeInMins;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
}
