package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.telappoint.admin.appt.common.model.Resource;
/**
 * 
 * @author Balaji N
 *
 */
@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResourceResponse extends BaseResponse {
	
	// populated if more then one resources.
	private List<Resource> resourceList;
	private List<Resource> deletedResourceList;
	private Integer resourceId;

	//populated if one resource
	private Resource resource;

	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public List<Resource> getDeletedResourceList() {
		return deletedResourceList;
	}

	public void setDeletedResourceList(List<Resource> deletedResourceList) {
		this.deletedResourceList = deletedResourceList;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
}
