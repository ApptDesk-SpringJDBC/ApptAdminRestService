package com.telappoint.admin.appt.common.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;
/**
 * 
 * @author Balaji N
 *
 */
@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResourceCalendarData {
	private String resourceFirstName;
	private String resourceLastName;
	private Integer resourceId;
	private String date;
	private List<CalendarData> calendarDataList;

	
	public String getResourceFirstName() {
		return resourceFirstName;
	}

	public void setResourceFirstName(String resourceFirstName) {
		this.resourceFirstName = resourceFirstName;
	}

	public String getResourceLastName() {
		return resourceLastName;
	}

	public void setResourceLastName(String resourceLastName) {
		this.resourceLastName = resourceLastName;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public List<CalendarData> getCalendarDataList() {
		return calendarDataList;
	}

	public void setCalendarDataList(List<CalendarData> calendarDataList) {
		this.calendarDataList = calendarDataList;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
