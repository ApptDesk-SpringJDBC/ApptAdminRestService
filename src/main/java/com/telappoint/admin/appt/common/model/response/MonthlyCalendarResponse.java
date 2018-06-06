package com.telappoint.admin.appt.common.model.response;

import java.util.Map;

import com.telappoint.admin.appt.common.model.PerDateAppts;

/**
 * 
 * @author Balaji N
 *
 */
public class MonthlyCalendarResponse extends BaseResponse {
	private Integer locationId;
	private Integer blockTimeInMins;
	private String calendarLastDate;
	private Map<String,PerDateAppts> perDateAppts;

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

	public Integer getBlockTimeInMins() {
		return blockTimeInMins;
	}

	public void setBlockTimeInMins(Integer blockTimeInMins) {
		this.blockTimeInMins = blockTimeInMins;
	}

	public String getCalendarLastDate() {
		return calendarLastDate;
	}

	public void setCalendarLastDate(String calendarLastDate) {
		this.calendarLastDate = calendarLastDate;
	}
}
