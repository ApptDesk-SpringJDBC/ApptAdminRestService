package com.telappoint.admin.appt.common.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;

	public Appointment() {
	}


	private long conf_number;


	private Long transId;


	private Long scheduleId;

	private java.sql.Timestamp timestamp;

	private int appt_method;

	private int appt_type;

	private String outlook_google_sync = "N";


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getConf_number() {
		return conf_number;
	}

	public void setConf_number(long conf_number) {
		this.conf_number = conf_number;
	}

	public Long getTransId() {
		return transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getAppt_method() {
		return appt_method;
	}

	public void setAppt_method(int appt_method) {
		this.appt_method = appt_method;
	}

	public int getAppt_type() {
		return appt_type;
	}

	public void setAppt_type(int appt_type) {
		this.appt_type = appt_type;
	}

	public String getOutlook_google_sync() {
		return outlook_google_sync;
	}

	public void setOutlook_google_sync(String outlook_google_sync) {
		this.outlook_google_sync = outlook_google_sync;
	}
}