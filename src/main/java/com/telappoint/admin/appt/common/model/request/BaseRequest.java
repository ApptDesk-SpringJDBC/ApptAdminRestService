package com.telappoint.admin.appt.common.model.request;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by Koti on 23-10-2016.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BaseRequest {
	public String clientCode;
	private Long transId;
	private String device;
	
	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public Long getTransId() {
		return transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
}
