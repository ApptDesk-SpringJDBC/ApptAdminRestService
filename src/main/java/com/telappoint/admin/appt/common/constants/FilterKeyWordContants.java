package com.telappoint.admin.appt.common.constants;

public enum FilterKeyWordContants {
	LOCATIONS_HOME_PAGE_DATA("LOCATION_HOME_PAGE_DATA", "By match with this key word, it will return location data for home page."),
	LOCATIONS_DROP_DOWN_DATA("LOCATION_DROP_DOWN_DATA", "By match with this key word, it will return location data for location dropdown."),
	LOCATIONS_BASIC_DATA("LOCATIONS_BASIC_DATA", "By match with this key word, it will return basic location data from location table."),
	LOCATION_COMPLETE_DATA("LOCATION_COMPLETE_DATA", "By match with this key word, it will return all location data from location table."),
	
	SERVICES_HOME_PAGE_DATA("SERVICE_HOME_PAGE_DATA", "By match with this key word, it will return service data for home page."),
	SERVICES_DROP_DOWN_DATA("SERVICE_DROP_DOWN_DATA", "By match with this key word, it will return service data."),
	SERVICES_BASIC_DATA("SERVICES_BASIC_DATA", "By match with this key word, it will return basic service data from service table."),
	SERVICE_COMPLETE_DATA("SERVICE_COMPLETE_DATA", "By match with this key word, it will return all service data from service table."),
	
	RESOURCE_HOME_PAGE_DATA("RESOURCE_HOME_PAGE_DATA", "By match with this key word, it will return resource data for home page."),
	RESOURCE_DROP_DOWN_DATA("RESOURCE_DROP_DOWN_DATA", "By match with this key word, it will return resource data."),
	RESOURCES_BASIC_DATA("RESOURCES_BASIC_DATA", "By match with this key word, it will return basic resource data from resource table."),
	RESOURCE_COMPLETE_DATA("RESOURCE_COMPLETE_DATA", "By match with this key word, it will return all resource data from resource table.");

	private String filterKey;
	private String filterMessage;

	private FilterKeyWordContants(String filterKey, String filterMessage) {
		this.filterKey = filterKey;
		this.filterMessage = filterMessage;
	}

	public String getFilterKey() {
		return filterKey;
	}

	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}

	public String getFilterMessage() {
		return filterMessage;
	}

	public void setFilterMessage(String filterMessage) {
		this.filterMessage = filterMessage;
	}
}
