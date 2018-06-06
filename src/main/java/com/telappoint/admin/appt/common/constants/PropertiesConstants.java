package com.telappoint.admin.appt.common.constants;

/**
 * 
 * @author Balaji N
 *
 */
public enum PropertiesConstants {

	APPT_ADMIN_REST_SERVICE_PROP("apptAdminRestService.properties");
	
	private String propertyFileName;
	
	private PropertiesConstants(String propertyFileName) {
		this.setPropertyFileName(propertyFileName);
	}

	public String getPropertyFileName() {
		return propertyFileName;
	}

	public void setPropertyFileName(String propertyFileName) {
		this.propertyFileName = propertyFileName;
	}
}
