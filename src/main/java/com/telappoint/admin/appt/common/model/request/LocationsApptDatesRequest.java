package com.telappoint.admin.appt.common.model.request;

import java.util.List;

import com.telappoint.admin.appt.common.model.Location;

/**
 * 
 * @author Balaji N
 *
 */
public class LocationsApptDatesRequest extends BaseRequest{
	private List<Location> locations;

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
}
