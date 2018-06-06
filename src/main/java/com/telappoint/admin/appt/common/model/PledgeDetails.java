package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class PledgeDetails extends Customer {
	private String customerPledgeId;
	private String apptDateTime;
	private String fundId;
	private String fundName;
	private String pledgeTotalAmount;
	private String vendorId1;
	private String vendorName1;
	private String vendor1PledgeAmount;
	private String vendorId2;
	private String vendorName2;
	private String vendor2PledgeAmount;
	private String vendorId3;
	private String vendorName3;
	private String vendor3PledgeAmount;
	private String vendor1AccountNumber;
	private String vendor2AccountNumber;
	private String vendor3AccountNumber;
	private String pledgeDatetime;
	private String custPledgeStatus;
	private String liheapFund; // Yes or No
	private String psehelpFund; // Yes or No
	private Long scheduleId;
	private String locationNameOnline;
	private String resourceNameOnline;
	private String serviceNameOnline;
	private String urgentStatus;
	private String updatedStatus;
	private String calledinStatus;
	private String primaryStatus;
	private String secondaryStatus;
	
	public String getApptDateTime() {
		return apptDateTime;
	}
	public void setApptDateTime(String apptDateTime) {
		this.apptDateTime = apptDateTime;
	}
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getPledgeTotalAmount() {
		return pledgeTotalAmount;
	}
	public void setPledgeTotalAmount(String pledgeTotalAmount) {
		this.pledgeTotalAmount = pledgeTotalAmount;
	}
	public String getVendorId1() {
		return vendorId1;
	}
	public void setVendorId1(String vendorId1) {
		this.vendorId1 = vendorId1;
	}
	public String getVendorName1() {
		return vendorName1;
	}
	public void setVendorName1(String vendorName1) {
		this.vendorName1 = vendorName1;
	}
	public String getVendor1PledgeAmount() {
		return vendor1PledgeAmount;
	}
	public void setVendor1PledgeAmount(String vendor1PledgeAmount) {
		this.vendor1PledgeAmount = vendor1PledgeAmount;
	}
	public String getVendorId2() {
		return vendorId2;
	}
	public void setVendorId2(String vendorId2) {
		this.vendorId2 = vendorId2;
	}
	public String getVendorName2() {
		return vendorName2;
	}
	public void setVendorName2(String vendorName2) {
		this.vendorName2 = vendorName2;
	}
	
	public String getVendor2PledgeAmount() {
		return vendor2PledgeAmount;
	}
	public void setVendor2PledgeAmount(String vendor2PledgeAmount) {
		this.vendor2PledgeAmount = vendor2PledgeAmount;
	}
	public String getPledgeDatetime() {
		return pledgeDatetime;
	}
	public void setPledgeDatetime(String pledgeDatetime) {
		this.pledgeDatetime = pledgeDatetime;
	}
	public String getCustPledgeStatus() {
		return custPledgeStatus;
	}
	public void setCustPledgeStatus(String custPledgeStatus) {
		this.custPledgeStatus = custPledgeStatus;
	}
	
	public String getVendor1AccountNumber() {
		return vendor1AccountNumber;
	}
	public void setVendor1AccountNumber(String vendor1AccountNumber) {
		this.vendor1AccountNumber = vendor1AccountNumber;
	}
	public String getVendor2AccountNumber() {
		return vendor2AccountNumber;
	}
	public void setVendor2AccountNumber(String vendor2AccountNumber) {
		this.vendor2AccountNumber = vendor2AccountNumber;
	}
	public String getVendor3AccountNumber() {
		return vendor3AccountNumber;
	}
	public void setVendor3AccountNumber(String vendor3AccountNumber) {
		this.vendor3AccountNumber = vendor3AccountNumber;
	}
	public String getLiheapFund() {
		return liheapFund;
	}
	public void setLiheapFund(String liheapFund) {
		this.liheapFund = liheapFund;
	}
	public String getPsehelpFund() {
		return psehelpFund;
	}
	public void setPsehelpFund(String psehelpFund) {
		this.psehelpFund = psehelpFund;
	}
	public String getCustomerPledgeId() {
		return customerPledgeId;
	}
	
	public void setCustomerPledgeId(String customerPledgeId) {
		this.customerPledgeId = customerPledgeId;
	}
	public String getVendorId3() {
		return vendorId3;
	}
	public void setVendorId3(String vendorId3) {
		this.vendorId3 = vendorId3;
	}
	public String getVendorName3() {
		return vendorName3;
	}
	public void setVendorName3(String vendorName3) {
		this.vendorName3 = vendorName3;
	}
	public String getVendor3PledgeAmount() {
		return vendor3PledgeAmount;
	}
	public void setVendor3PledgeAmount(String vendor3PledgeAmount) {
		this.vendor3PledgeAmount = vendor3PledgeAmount;
	}
	
	public String getLocationNameOnline() {
		return locationNameOnline;
	}
	public void setLocationNameOnline(String locationNameOnline) {
		this.locationNameOnline = locationNameOnline;
	}
	public String getResourceNameOnline() {
		return resourceNameOnline;
	}
	public void setResourceNameOnline(String resourceNameOnline) {
		this.resourceNameOnline = resourceNameOnline;
	}
	public String getServiceNameOnline() {
		return serviceNameOnline;
	}
	public void setServiceNameOnline(String serviceNameOnline) {
		this.serviceNameOnline = serviceNameOnline;
	}
	
	public String getUrgentStatus() {
		return urgentStatus;
	}
	public void setUrgentStatus(String urgentStatus) {
		this.urgentStatus = urgentStatus;
	}
	public String getUpdatedStatus() {
		return updatedStatus;
	}
	public void setUpdatedStatus(String updatedStatus) {
		this.updatedStatus = updatedStatus;
	}
	public String getCalledinStatus() {
		return calledinStatus;
	}
	public void setCalledinStatus(String calledinStatus) {
		this.calledinStatus = calledinStatus;
	}
	public String getPrimaryStatus() {
		return primaryStatus;
	}
	public void setPrimaryStatus(String primaryStatus) {
		this.primaryStatus = primaryStatus;
	}
	public String getSecondaryStatus() {
		return secondaryStatus;
	}
	public void setSecondaryStatus(String secondaryStatus) {
		this.secondaryStatus = secondaryStatus;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
}
