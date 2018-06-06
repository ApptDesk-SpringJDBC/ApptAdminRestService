package com.telappoint.admin.appt.common.model.response;

import java.util.Map;

import com.telappoint.admin.appt.common.model.PerDateAppts;

/**
 * 
 * @author Balaji N
 *
 */
public class JSCalendarResponse extends BaseResponse {
	private Integer locationId;
	private String resourceIds;
	private Map<String,PerDateAppts> perDateAppts;
	private String isFullyBookedMessage="Booked Full";
	private String isSlotAvailableMessage="Available";
	private String isNotAvailableMessage="Not Available";
	private String isHolidayMessage="Holiday";
	private String isClosedMessage="Closed";
	private String firstAvailableDate;
	
	public Map<String,PerDateAppts> getPerDateAppts() {
		return perDateAppts;
	}

	public void setPerDateAppts(Map<String,PerDateAppts> perDateAppts) {
		this.perDateAppts = perDateAppts;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}

	public String getIsFullyBookedMessage() {
		return isFullyBookedMessage;
	}

	public void setIsFullyBookedMessage(String isFullyBookedMessage) {
		this.isFullyBookedMessage = isFullyBookedMessage;
	}

	public String getIsSlotAvailableMessage() {
		return isSlotAvailableMessage;
	}

	public void setIsSlotAvailableMessage(String isSlotAvailableMessage) {
		this.isSlotAvailableMessage = isSlotAvailableMessage;
	}

	public String getIsNotAvailableMessage() {
		return isNotAvailableMessage;
	}

	public void setIsNotAvailableMessage(String isNotAvailableMessage) {
		this.isNotAvailableMessage = isNotAvailableMessage;
	}

	public String getIsHolidayMessage() {
		return isHolidayMessage;
	}

	public void setIsHolidayMessage(String isHolidayMessage) {
		this.isHolidayMessage = isHolidayMessage;
	}

	public String getIsClosedMessage() {
		return isClosedMessage;
	}

	public void setIsClosedMessage(String isClosedMessage) {
		this.isClosedMessage = isClosedMessage;
	}

	public String getFirstAvailableDate() {
		return firstAvailableDate;
	}

	public void setFirstAvailableDate(String firstAvailableDate) {
		this.firstAvailableDate = firstAvailableDate;
	}
}
