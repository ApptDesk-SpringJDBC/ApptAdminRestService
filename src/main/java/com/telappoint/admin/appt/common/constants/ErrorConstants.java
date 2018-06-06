package com.telappoint.admin.appt.common.constants;

public enum ErrorConstants {
	// DB layer error codes

	ERROR_1000("1000", "Error in getClient fetch."),
	ERROR_1001("1001", "Error in getClientDeploymentConfig fetch."),
	ERROR_1002("1002", "Error while fetching AdminLogin data."),
	ERROR_1003("1003", "Error while fetching AdminLoginConfig data."),

	ERROR_2001("2001", "Error while prepare the connection pool"),
	ERROR_2002("2002", "Invalid input parameters passed from front end"),
	ERROR_2003("2003","Error while prepare the confirm email data."),
	ERROR_2004("2004", "Error while getApptOpenTimeSlots API."),
	ERROR_2005("2005", "Error while getApptBookedCount API."),
	ERROR_2006("2006", "Error while getApptHoldCount API."),
	ERROR_2007("2007", "Error while fetching the resources by locationId API"),
	ERROR_2008("2008", "Error while fetching get stacked chart info API"),
	ERROR_2009("2009", "Error while fetching getPriviliegedPageNames API"),
	ERROR_2010("2010", "Error while fetching ApptSysConfig API"),
	ERROR_2011("2011", "Error while fetching ServiceLocationApptWindowDates API"),
	ERROR_2012("2012", "Error while updating ServiceLocationApptDates to serviceLocation table."),
	ERROR_2013("2013", "Error while updating updateLocationsApptDates to Location table."),
	ERROR_2014("2014", "Error while fetching LocationListByLocationIds."),
	ERROR_2015("2015", "Error while fetching LocationListByResourceIds."),
	ERROR_2016("2016", "Error while fetching getServiceListByResourceIds."),
	ERROR_2017("2017", "Error while fetching getDisplayNames."),
	ERROR_2018("2018", "Error while fetching getServicesByServiceIds."),
	ERROR_2019("2019", "Error while fetching getLocationList."),
	ERROR_2020("2020", "Error while fetching getResourceList"),
	ERROR_2021("2021", "Error while fetching getResourceList"),
	ERROR_2022("2022", "Error while fetching getServiceById"),
	ERROR_2023("2023", "Error while fetching getServicesByResourceId"),
	ERROR_2024("2024", "Error while fetching getLocationById"),
	ERROR_2025("2025", "Error while adding location"),
	ERROR_2026("2026", "Error while cancelAppointment in admin."),
	ERROR_2027("2027", "Error while calling book appointment stored procedure."),
	ERROR_2028("2028", "Error while populate Map."),
	ERROR_2029("2029", "Error while holding the time."),
	ERROR_2030("2030", "Error while creating customer."),
	ERROR_2031("2031", "Error while fetching appointment details."),
	ERROR_2032("2032", "Error while rescheduling the appointment."),
	ERROR_2033("2033", "Error while fetching the pledge details."),
	ERROR_2034("2034", "Error while fetching the available dates "),
	ERROR_2035("2035", "Error while release the hold appointment"),

	ERROR_2994("2994", "Unable to fetch info from database. Please try again later."),
	ERROR_2995("2995", "IP Not Allowed"),
	ERROR_2996("2996","Unable to fetch Error/Warning Message from database"),
	ERROR_2997("2997","Error while reading properties"),
	ERROR_2998("2998", "Error while getClient information."),
	ERROR_2999("2999", "Error while getLogger instance."),
	ERROR_3000("3000", "Error while IP checking."),
	ERROR_9999("9999", "Unexpected Error Occurred. Please retry again or contact ITFrontDesk support");

	private String code;
	private String message;

	private ErrorConstants(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
