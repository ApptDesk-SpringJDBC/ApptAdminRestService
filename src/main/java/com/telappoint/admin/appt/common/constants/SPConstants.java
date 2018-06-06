package com.telappoint.admin.appt.common.constants;

/**
 * @author Balaji
 */
public enum SPConstants {
	TIME_ZONE("TIME_ZONE"),
	PROCEDURE_ID("PROC_ID"),
	TRANS_ID("trans_id"),
	DEPARTMENT_ID("DEPT_ID"),
	SERVICE_ID("SER_ID"),
	RESULT_LIST("RESULT_LIST"),
	BLOCKS("BLOCKS"),
	APPT_DATE_TIME("APPT_DATE_TIME"),
	CUSTOMER_ID("CUST_ID"),
	STATUS_RESULT("status_result"),
	// out parameters
	AVAILABLE_DATE_TIMES("avail_date_times"),
	RESULT_STR("result_str"),
	AVAILABLE_DATES("avail_dates"),
	AVAILABLE_DATE("avail_date"),
	HOLD_ID("hold_id"),
	RETURN_SCHEDULE_ID("sched_id"),
	AVAILABILITY("availability"),
	DISPLAY_DATETIME("display_datetime"),
	CONFIRM_NUMBER("conf_number"),
	RESULT("result"),
	
	LOCATION_ID("LOC_ID"),
	RESOURCE_ID("RES_ID"),
	DEVICE("DEVICE"),
	APPT_METHOD("APPT_METHOD"),
	STACK_CHAR_TYPE("stack_chart_type"),
	BLOCK_TIME_IN_MINS("BLOCK_TIME_IN_MINS"),
	SCHEDULE_ID("SCHED_ID"),
	CANCEL_METHOD("CANCEL_METHOD"),
	LANG_CODE("LANG_CODE"),
	SUCCESS("success"),
	DISPLAY_KEYS("display_keys"),
	DISPLAY_VALUES("display_values"),
	// out parameters
	STACKEDCHART_INFO("stackchart_info"),
	RESPONSE("response"),
	ERROR_MESSAGE("error_msg");
	
	

	private String value;

	private SPConstants(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
