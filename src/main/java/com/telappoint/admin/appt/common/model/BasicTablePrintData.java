package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author Balaji N
 */
public class BasicTablePrintData {
	private Long resourceId;
	private String resourceName;
	private Long totalBookedAppts;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicTablePrintData other = (BasicTablePrintData) obj;
		if (resourceId == null) {
			if (other.resourceId != null)
				return false;
		} else if (!resourceId.equals(other.resourceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		ObjectMapper ow = new ObjectMapper();
		String json = null;
		try {
			json = ow.writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Long getTotalBookedAppts() {
		return totalBookedAppts;
	}

	public void setTotalBookedAppts(Long totalBookedAppts) {
		this.totalBookedAppts = totalBookedAppts;
	}
}
