package com.telappoint.admin.appt.common.model.response;

/**
 * 
 * @author Balaji N
 *
 */
public class StackedChartResponse extends BaseResponse {
	private String stackedChartDays;
	private String noOfApptsBooked;
	private String noOfApptsOpened;
	private String noOfConfirmedNotifications;
	private String noOfUnConfirmedNotifications;
	
	public String getStackedChartDays() {
		return stackedChartDays;
	}
	public void setStackedChartDays(String stackedChartDays) {
		this.stackedChartDays = stackedChartDays;
	}
	public String getNoOfApptsBooked() {
		return noOfApptsBooked;
	}
	public void setNoOfApptsBooked(String noOfApptsBooked) {
		this.noOfApptsBooked = noOfApptsBooked;
	}
	public String getNoOfApptsOpened() {
		return noOfApptsOpened;
	}
	public void setNoOfApptsOpened(String noOfApptsOpened) {
		this.noOfApptsOpened = noOfApptsOpened;
	}
	public String getNoOfConfirmedNotifications() {
		return noOfConfirmedNotifications;
	}
	public void setNoOfConfirmedNotifications(String noOfConfirmedNotifications) {
		this.noOfConfirmedNotifications = noOfConfirmedNotifications;
	}
	public String getNoOfUnConfirmedNotifications() {
		return noOfUnConfirmedNotifications;
	}
	public void setNoOfUnConfirmedNotifications(String noOfUnConfirmedNotifications) {
		this.noOfUnConfirmedNotifications = noOfUnConfirmedNotifications;
	}
}
