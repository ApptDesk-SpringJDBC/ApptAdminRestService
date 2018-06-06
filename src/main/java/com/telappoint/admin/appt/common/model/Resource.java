package com.telappoint.admin.appt.common.model;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.admin.appt.common.model.request.BaseRequest;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Resource extends BaseRequest {
    private Integer resourceId;
    private Integer locationId;
    private String locationName;
    private String resourceCode;
    private String resourceType;
    private String resourceAudio;
    private String callCenterLogic;
    private String deleteFlag;
    private String enable;
    private String allowSelfService;
    private List<String> serviceNames; // to show in basic data..
    private String prefix;
    private String firstName;
    private String lastName;
    private String title;
    private String email;
    private Integer placement;
    private String resourceName;
    private String selectedServiceIds;
    private String selectedDepartmentIds;
    
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getPlacement() {
		return placement;
	}
	public void setPlacement(Integer placement) {
		this.placement = placement;
	}

	public List<String> getServiceNames() {
		return serviceNames;
	}
	public void setServiceNames(List<String> serviceNames) {
		this.serviceNames = serviceNames;
	}
	
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceCode() {
		return resourceCode;
	}
	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getResourceAudio() {
		return resourceAudio;
	}
	public void setResourceAudio(String resourceAudio) {
		this.resourceAudio = resourceAudio;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	
	public String getAllowSelfService() {
		return allowSelfService;
	}
	public void setAllowSelfService(String allowSelfService) {
		this.allowSelfService = allowSelfService;
	}
	
	public String getCallCenterLogic() {
		return callCenterLogic;
	}
	public void setCallCenterLogic(String callCenterLogic) {
		this.callCenterLogic = callCenterLogic;
	}
	public String getSelectedServiceIds() {
		return selectedServiceIds;
	}
	public void setSelectedServiceIds(String selectedServiceIds) {
		this.selectedServiceIds = selectedServiceIds;
	}
	
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
		Resource other = (Resource) obj;
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
	public String getSelectedDepartmentIds() {
		return selectedDepartmentIds;
	}
	public void setSelectedDepartmentIds(String selectedDepartmentIds) {
		this.selectedDepartmentIds = selectedDepartmentIds;
	}
}
