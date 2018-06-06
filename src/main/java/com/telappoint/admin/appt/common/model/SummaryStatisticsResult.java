package com.telappoint.admin.appt.common.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Balaji 
 * 
 */
@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SummaryStatisticsResult  {
	private String day;
	private String week;
	private String month;
	private String monthName;
	private String quarter;
	private String year;
	private int totalAppointments;
	private Map<Integer,Integer> apptStatusWithApptCount;

	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getTotalAppointments() {
		return totalAppointments;
	}
	public void setTotalAppointments(int totalAppointments) {
		this.totalAppointments = totalAppointments;
	}
	
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	public Map<Integer,Integer> getApptStatusWithApptCount() {
		return apptStatusWithApptCount;
	}
	public void setApptStatusWithApptCount(Map<Integer,Integer> apptStatusWithApptCount) {
		this.apptStatusWithApptCount = apptStatusWithApptCount;
	}	
}