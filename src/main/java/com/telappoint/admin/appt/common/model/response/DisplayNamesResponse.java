package com.telappoint.admin.appt.common.model.response;

import com.telappoint.admin.appt.common.model.DisplayNames;

/**
 * 
 * @author Balaji N
 *
 */
public class DisplayNamesResponse extends BaseResponse {
	private DisplayNames displayNames;

	public DisplayNames getDisplayNames() {
		return displayNames;
	}

	public void setDisplayNames(DisplayNames displayNames) {
		this.displayNames = displayNames;
	}
}
