package com.telappoint.admin.appt.common.constants;

public enum CommonApptDeskConstants {
	COMMA(","),
    VERSION("version"),
	EMPTY_STRING(""),
	ONLINE("online"),
	ADMIN("admin"),
	COMPANY("Company"),
	PROCEDURE("Procedure"),
	DEPARTMENT("Department"),
	LOCATION("Location"),
	RESOURCE("Resource"),
	SERVICE("Service"),
	LOCATION_SERVICE("LocationAndService "),
	USER_ACCESS_LEVEL_SUPER_USER("Super User"),
	USER_ACCESS_LEVEL_ADMINISTRATOR("Administrator"),
	USER_ACCESS_LEVEL_MANAGER("Manager"),
	USER_ACCESS_LEVEL_LOCATION("Location"),
	USER_ACCESS_LEVEL_PROVIDER("Provider"),
	USER_ACCESS_LEVEL_READ_ONLY("Read Only"),
	USER_ACCESS_LEVEL_SCHEDULER("Scheduler");

	private String value;

	CommonApptDeskConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}