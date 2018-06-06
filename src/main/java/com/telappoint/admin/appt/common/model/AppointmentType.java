package com.telappoint.admin.appt.common.model;

public enum AppointmentType {

	MAKE(1,"Make"),
    CANCEL(2,"Cancel");
	
	private int type;
	private String apptTypeMessage;
	
	private AppointmentType(int type,String apptTypeMessage) {
		this.type = type;
		this.apptTypeMessage = apptTypeMessage;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	public String getApptTypeMessage() {
		return apptTypeMessage;
	}

	public void setApptTypeMessage(String apptTypeMessage) {
		this.apptTypeMessage = apptTypeMessage;
	}
	
	public static class ApptTypeStatusMessage {
		public static String getApptTypeMessage(int apptType) {
			AppointmentType[] keys = AppointmentType.values();
			int _type = 0;
			for (AppointmentType key : keys) {
				_type = key.getType();
				if (_type == apptType) {
					return key.getApptTypeMessage();
				}
			}
			return "";
		}
	}
}
