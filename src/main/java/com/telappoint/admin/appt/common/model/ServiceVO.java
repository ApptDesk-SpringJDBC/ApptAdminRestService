package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.admin.appt.common.model.request.BaseRequest;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServiceVO extends BaseRequest {
	private Integer serviceId;
	private String serviceNameOnline;
	private Integer duration;
	
	// additional fields 
	private String serviceNameMobile;
	private String serviceNameSMS;
	private String serviceNameIvrTts;
	private String serviceNameIvrAudio;
	private String customMsgTts;
	private String customMsgAudio;
	
	private Integer blocks;
	private Integer buffer;
	private Float minCharge;
	private Float price;
	private String deleteFlag;
	private String enable;
	private String allowDuplicateAppt;
	private String skipDateTIme;
	private String closed;
	private String closedMessage;
	private String closedAudio;
	private String closedTts;
	private String isSunOpen;
	private String isMonOpen;
	private String isTueOpen;
	private String isWedOpen;
	private String isThuOpen;
	private String isFriOpen;
	private String isSatOpen;
	private String apptStartDate;
	private String apptEndDate;
	private String serCustomMsg;
	private Integer placement;
	private String closedLocationIds;
	
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceNameOnline() {
		return serviceNameOnline;
	}
	public void setServiceNameOnline(String serviceNameOnline) {
		this.serviceNameOnline = serviceNameOnline;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getServiceNameIvrTts() {
		return serviceNameIvrTts;
	}
	public void setServiceNameIvrTts(String serviceNameIvrTts) {
		this.serviceNameIvrTts = serviceNameIvrTts;
	}
	public String getServiceNameIvrAudio() {
		return serviceNameIvrAudio;
	}
	public void setServiceNameIvrAudio(String serviceNameIvrAudio) {
		this.serviceNameIvrAudio = serviceNameIvrAudio;
	}
	public String getCustomMsgTts() {
		return customMsgTts;
	}
	public void setCustomMsgTts(String customMsgTts) {
		this.customMsgTts = customMsgTts;
	}
	public String getCustomMsgAudio() {
		return customMsgAudio;
	}
	public void setCustomMsgAudio(String customMsgAudio) {
		this.customMsgAudio = customMsgAudio;
	}
	public Integer getBlocks() {
		return blocks;
	}
	public void setBlocks(Integer blocks) {
		this.blocks = blocks;
	}
	public Integer getBuffer() {
		return buffer;
	}
	public void setBuffer(Integer buffer) {
		this.buffer = buffer;
	}
	public Float getMinCharge() {
		return minCharge;
	}
	public void setMinCharge(Float minCharge) {
		this.minCharge = minCharge;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getAllowDuplicateAppt() {
		return allowDuplicateAppt;
	}
	public void setAllowDuplicateAppt(String allowDuplicateAppt) {
		this.allowDuplicateAppt = allowDuplicateAppt;
	}
	public String getSkipDateTIme() {
		return skipDateTIme;
	}
	public void setSkipDateTIme(String skipDateTIme) {
		this.skipDateTIme = skipDateTIme;
	}
	public String getClosed() {
		return closed;
	}
	public void setClosed(String closed) {
		this.closed = closed;
	}
	public String getClosedMessage() {
		return closedMessage;
	}
	public void setClosedMessage(String closedMessage) {
		this.closedMessage = closedMessage;
	}
	public String getClosedAudio() {
		return closedAudio;
	}
	public void setClosedAudio(String closedAudio) {
		this.closedAudio = closedAudio;
	}
	public String getClosedTts() {
		return closedTts;
	}
	public void setClosedTts(String closedTts) {
		this.closedTts = closedTts;
	}
	public String getIsSunOpen() {
		return isSunOpen;
	}
	public void setIsSunOpen(String isSunOpen) {
		this.isSunOpen = isSunOpen;
	}
	public String getIsMonOpen() {
		return isMonOpen;
	}
	public void setIsMonOpen(String isMonOpen) {
		this.isMonOpen = isMonOpen;
	}
	public String getIsTueOpen() {
		return isTueOpen;
	}
	public void setIsTueOpen(String isTueOpen) {
		this.isTueOpen = isTueOpen;
	}
	public String getIsWedOpen() {
		return isWedOpen;
	}
	public void setIsWedOpen(String isWedOpen) {
		this.isWedOpen = isWedOpen;
	}
	public String getIsThuOpen() {
		return isThuOpen;
	}
	public void setIsThuOpen(String isThuOpen) {
		this.isThuOpen = isThuOpen;
	}
	public String getIsFriOpen() {
		return isFriOpen;
	}
	public void setIsFriOpen(String isFriOpen) {
		this.isFriOpen = isFriOpen;
	}
	public String getApptStartDate() {
		return apptStartDate;
	}
	public void setApptStartDate(String apptStartDate) {
		this.apptStartDate = apptStartDate;
	}
	public String getApptEndDate() {
		return apptEndDate;
	}
	public void setApptEndDate(String apptEndDate) {
		this.apptEndDate = apptEndDate;
	}
	public String getSerCustomMsg() {
		return serCustomMsg;
	}
	public void setSerCustomMsg(String serCustomMsg) {
		this.serCustomMsg = serCustomMsg;
	}
	public Integer getPlacement() {
		return placement;
	}
	public void setPlacement(Integer placement) {
		this.placement = placement;
	}
	public String getIsSatOpen() {
		return isSatOpen;
	}
	public void setIsSatOpen(String isSatOpen) {
		this.isSatOpen = isSatOpen;
	}
	public String getServiceNameSMS() {
		return serviceNameSMS;
	}
	public void setServiceNameSMS(String serviceNameSMS) {
		this.serviceNameSMS = serviceNameSMS;
	}
	public String getServiceNameMobile() {
		return serviceNameMobile;
	}
	public void setServiceNameMobile(String serviceNameMobile) {
		this.serviceNameMobile = serviceNameMobile;
	}
	public String getClosedLocationIds() {
		return closedLocationIds;
	}
	public void setClosedLocationIds(String closedLocationIds) {
		this.closedLocationIds = closedLocationIds;
	}
}