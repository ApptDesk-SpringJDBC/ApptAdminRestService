package com.telappoint.admin.appt.common.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.admin.appt.common.model.Location;
/**
 * 
 * @author Balaji N
 *
 */
@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LocationResponse extends BaseResponse {
	
	// populated if locations 
	private List<Location> locationList;
	private List<Location> deletedLocationList;
	
	// populated if only one location details.
	private Location location;
	private Integer locationId;

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<Location> getDeletedLocationList() {
		return deletedLocationList;
	}

	public void setDeletedLocationList(List<Location> deletedLocationList) {
		this.deletedLocationList = deletedLocationList;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
}
