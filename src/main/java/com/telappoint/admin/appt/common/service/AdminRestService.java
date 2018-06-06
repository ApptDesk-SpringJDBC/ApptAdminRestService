package com.telappoint.admin.appt.common.service;

import com.telappoint.admin.appt.common.model.*;
import com.telappoint.admin.appt.common.model.request.*;
import org.springframework.http.ResponseEntity;

/**
 * @author Koti, Balaji
 */

public interface AdminRestService {
    public ResponseEntity<ResponseModel> handleException(String clientCode, Exception tae);
    public ResponseEntity<ResponseModel> handleException(Exception tae);
    public ResponseEntity<ResponseModel> getHomePageResponse(String clientCode, Long loginUserId) throws Exception;
	public ResponseEntity<ResponseModel> getGaugeChart(String clientCode, Integer locationId, String startDate, String endDate) throws Exception;
	public ResponseEntity<ResponseModel> getStackedChart(String clientCode, Integer locationId, Integer resourceId, String stackChartType) throws Exception;
	public ResponseEntity<ResponseModel> getPieChart(String clientCode, Integer locationId, String selectedDate) throws Exception;
	public ResponseEntity<ResponseModel> loginAuthenticate(UserLogin userLogin) throws Exception;
	public ResponseEntity<ResponseModel> getApptSysConfig(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getDisplayNames(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getPrivilegedPageNames(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getServiceLocationApptDatesWindow(String clientCode, Integer locationId) throws Exception;
	public ResponseEntity<ResponseModel> updateApptRestrictDates(String clientCode, String apptStartDate, String apptEndDate) throws Exception;
	public ResponseEntity<ResponseModel> updateApptPerSeasonDetails(String clientCode, String termStartDate, String termEndDate, Integer noApptPerTerm) throws Exception;
    public ResponseEntity<ResponseModel> updateServiceLocationApptDatesWindow(ServiceLocationApptDatesRequest serviceLocApptDatesReq) throws Exception;
    public ResponseEntity<ResponseModel> getClientDetails(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> updateLocationsApptDates(LocationsApptDatesRequest locationApptReq) throws Exception;
	public ResponseEntity<ResponseModel> updateScheduleClosedStatus(String clientCode, String closedStatus) throws Exception;
	public ResponseEntity<ResponseModel> getHomePageLocationList(String clientCode, Long loginUserId) throws Exception;
	public ResponseEntity<ResponseModel> getActiveLocationDropDownData(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getAllLocationsBasicData(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getResourceList(String clientCode, Long loginUserId) throws Exception;
	public ResponseEntity<ResponseModel> getAllResourcesBasicData(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getLocationById(String clientCode, Integer locationId) throws Exception;
	public ResponseEntity<ResponseModel> getResourceById(String clientCode, Integer resourceId) throws Exception;
	public ResponseEntity<ResponseModel> getCompleteResourceDataById(String clientCode, Integer resourceId) throws Exception;
	public ResponseEntity<ResponseModel> getResourceListByLocationId(String clientCode, Integer locationId) throws Exception;
	public ResponseEntity<ResponseModel> getServiceById(String clientCode, Integer serviceId) throws Exception;
	public ResponseEntity<ResponseModel> getAllServicesBasicData(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getServiceListByResourceId(String clientCode, Integer resourceId, Integer serviceId) throws Exception;
	public ResponseEntity<ResponseModel> getCompleteServiceDataById(String clientCode, Integer serviceId) throws Exception;
	public ResponseEntity<ResponseModel> getCompleteLocationDataById(String clientCode, Integer locationId) throws Exception;
	public ResponseEntity<ResponseModel> updateLocation(Location location) throws Exception;
	public ResponseEntity<ResponseModel> getResourcePrefixList(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getResourceTitleList(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> deleteLocation(String clientCode, Integer locationId) throws Exception;
	public ResponseEntity<ResponseModel> unDeleteLocation(String clientCode, Integer locationId) throws Exception;
	public ResponseEntity<ResponseModel> updateResource(Resource resource) throws Exception;
	public ResponseEntity<ResponseModel> addLocation(Location location) throws Exception;
	public ResponseEntity<ResponseModel> deleteResource(String clientCode, Integer resourceId) throws Exception;
	public ResponseEntity<ResponseModel> unDeleteResource(String clientCode, Integer resourceId) throws Exception;
	public ResponseEntity<ResponseModel> deleteService(String clientCode, Integer serviceId) throws Exception;
	public ResponseEntity<ResponseModel> unDeleteService(String clientCode, Integer serviceId) throws Exception;
	public ResponseEntity<ResponseModel> getDynamicFieldDisplayData(String clientCode, String pageName) throws Exception;
	public ResponseEntity<ResponseModel> addResource(Resource resource) throws Exception;
	public ResponseEntity<ResponseModel> addService(ServiceVO service) throws Exception;
	public ResponseEntity<ResponseModel> updateService(ServiceVO service) throws Exception;
	public ResponseEntity<ResponseModel> getResourceTypeList(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> addUser(AdminLogin adminLogin) throws Exception;
	public ResponseEntity<ResponseModel> updateUser(AdminLogin adminLogin) throws Exception;
	public ResponseEntity<ResponseModel> getUsers(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getInBoundCallLogs(String clientCode, String fromDate, String toDate, String callerId) throws Exception;
	public ResponseEntity<ResponseModel> getOutBoundCallLogs(String clientCode, String fromDate, String toDate, String callerId) throws Exception;
	public ResponseEntity<ResponseModel> getActiveResourceDropDownData(String clientCode) throws Exception;
	ResponseEntity<ResponseModel> getActiveServiceDropDownData(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getLocationsByServiceIdToCloseServiceStatus(String clientCode, Integer serviceId) throws Exception;
	public ResponseEntity<ResponseModel> addAppointmentReportConfig(AppointmentReportConfig apptReportConfig) throws Exception;
	public ResponseEntity<ResponseModel> getAppointmentReportConfig(String userName) throws Exception;
	public ResponseEntity<ResponseModel> deleteApptReportConfigById(Integer configId) throws Exception;
	public ResponseEntity<ResponseModel> getDynamicIncludeReportsData(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getAppointmentReport(String clientCode, String fromDate, String toDate, String locationIds, String resourceIds, String serviceIds,
			String apptStatus) throws Exception;
	public ResponseEntity<ResponseModel> searchByFirstLastName(String clientCode, String firstName, String lastName) throws Exception;
	public ResponseEntity<ResponseModel> searchByConfirmationNumber(String clientCode, Long confirmationNumber) throws Exception;
	public ResponseEntity<ResponseModel> getUserById(String clientCode, Integer userId) throws Exception;
	public ResponseEntity<ResponseModel> validateUser(String clientCode, Integer userId, String userName) throws Exception;
	public ResponseEntity<ResponseModel> getPasswordComplexity(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> searchByAccountNumber(String clientCode, String accountNumber) throws Exception;
	public ResponseEntity<ResponseModel> searchByContactPhone(String clientCode, String contactPhone) throws Exception;
	ResponseEntity<ResponseModel> searchByCallerId(String clientCode, String callerId) throws Exception;
	public ResponseEntity<ResponseModel> searchByAttrib1(String clientCode, String attrib1) throws Exception;
	ResponseEntity<ResponseModel> searchByDob(String clientCode, String dob) throws Exception;
	ResponseEntity<ResponseModel> searchByHouseHoldId(String clientCode, Long houseHoldId) throws Exception;
	ResponseEntity<ResponseModel> getSearchDropDownList(String clientCode) throws Exception;
	ResponseEntity<ResponseModel> getAppointmentsByCustomerId(String clientCode, Long customerId) throws Exception;
	ResponseEntity<ResponseModel> getCustomersById(String clientCode, Long customerId) throws Exception;
	ResponseEntity<ResponseModel> getCustomerActivities(String clientCode, Long customerId) throws Exception;
	public ResponseEntity<ResponseModel> getHouseHoldInfo(String clientCode, Long houseHoldId) throws Exception;
	public ResponseEntity<ResponseModel> getAppointmentStatusDropDownList(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> mergeHouseHoldId(String clientCode, String fromHouseHoldIds, String mergeToHouseHoldId) throws Exception;
	public ResponseEntity<ResponseModel> splitHouseHoldIdWithAssignNewHouseHoldId(String clientCode, String customerIds, String newHouseHoldId, String assignNewHouseholdID) throws Exception;
	ResponseEntity<ResponseModel> splitHouseHoldId(String clientCode, String customerIds, String newHouseHoldId) throws Exception;
	ResponseEntity<ResponseModel> getBlockedCustomers(String clientCode) throws Exception;

	public ResponseEntity<ResponseModel> updateCustomerBlockedReason(String clientCode, Long customerId, String reasonMessage) throws Exception;
	public ResponseEntity<ResponseModel> unBlockCustomer(String clientCode, Long customerId) throws Exception;
	@Deprecated
	public ResponseEntity<ResponseModel> searchAppointmentsByFirstLastName(String clientCode, String firstName, String lastName) throws Exception;
	
	@Deprecated
	public ResponseEntity<ResponseModel> searchAppointmentsByAccountNumber(String clientCode, String accountNumber) throws Exception;
	public ResponseEntity<ResponseModel> getTransStates(String clientCode, Long transId) throws Exception;
	ResponseEntity<ResponseModel> getPrivilegeSettings(String clientCode, String accessPrivilegeName) throws Exception;
	public ResponseEntity<ResponseModel> getTablePrintView(String clientCode, Integer locationId, String resourceIds, String date) throws Exception;
	public ResponseEntity<ResponseModel> getSummaryReport(String clientCode, String fromDate, String toDate, String reportCategory) throws Exception;
	ResponseEntity<ResponseModel> cancelAppointment(String clientCode, String langCode, Long scheduleId) throws Exception;
	ResponseEntity<ResponseModel> updateAppointmentStatus(String clientCode, Long scheduleId, int status, String userName) throws Exception;
	public ResponseEntity<ResponseModel> getItemizedReport(String clientCode, Integer locationId, Integer serviceId, String fromDate, String toDate, String reportCategory) throws Exception;

	public ResponseEntity<ResponseModel> getPledgeReport(String clientCode, String fromDate, String toDate, Integer locationId, String groupByIntake, String groupByFundSource,
			String groupByVendor, Integer resourceId, Integer fundSourceId) throws Exception;
	public ResponseEntity<ResponseModel> getServiceDropDownList(String clientCode, Integer locationId, Integer resourceId) throws Exception;
	public ResponseEntity<ResponseModel> getResourceServiceList(String clientCode, Integer locationId, Integer loginUserId) throws Exception;
	public ResponseEntity<ResponseModel> getServiceListByLocationId(String clientCode, Integer locationId, Integer loginUserId) throws Exception;
	public ResponseEntity<ResponseModel> getDynamicPledgeResults(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getMonthlyCalendar(String clientCode, String calendarDate, Integer locationId, String resourceIdStr) throws Exception;
	ResponseEntity<ResponseModel> getDailyCalendar(String clientCode, String calendarDate, Integer locationId, String resourceIds) throws Exception;
	ResponseEntity<ResponseModel> getWeeklyCalendar(String clientCode, String calendarDate, Integer locationId, String resourceIds) throws Exception;
	public ResponseEntity<ResponseModel> confirmAppointment(ConfirmAppointmentRequest confirmAppointmentRequest) throws Exception;
	public ResponseEntity<ResponseModel> getAutoSuggestCustomerNames(String clientCode, String customerName) throws Exception;
	public ResponseEntity<ResponseModel> getCustomerRegistrationDetails(String clientCode, String langCode) throws Exception;
	public ResponseEntity<ResponseModel> holdAppointment(String clientCode, Long locationId, Long resourceId, Long procedureId, Long departmentId, Long serviceId, Long customerId,
			String apptDateTime, String device, String langCode, Long transId) throws Exception;
	ResponseEntity<ResponseModel> createCustomer(CustomerRequest customerRequest) throws Exception;
	public ResponseEntity<ResponseModel> updateCustomer(CustomerRequest customerRequest) throws Exception;
	public ResponseEntity<ResponseModel> getFutureAppointments(String clientCode, long customerId) throws Exception;
	public ResponseEntity<ResponseModel> rescheduleAppointment(String clientCode, Long locationId, Long resourceId, Long procedureId, Long departmentId, Long serviceId,
			Long customerId, String apptDateTime, String device, String langCode, Long transId, Long oldscheduleId) throws Exception;
	public ResponseEntity<ResponseModel> updateAppointStatus(String clientCode, String screenedFlag, String accessedFlag, Integer status, Long scheduleId) throws Exception;
	ResponseEntity<ResponseModel> updateRecordTime(String clientCode, String recordName, String recordType, long scheduleId) throws Exception;
	ResponseEntity<ResponseModel> getPledgeHistory(String clientCode, String device, String langCode, Long customerId, Long transId) throws Exception;
	public ResponseEntity<ResponseModel> getPastAppointments(String clientCode, long customerId) throws Exception;
	public ResponseEntity<ResponseModel> getAppointmentStatus(String clientCode, Long scheduleId) throws Exception;
	public ResponseEntity<ResponseModel> getSameServiceBlockList(String clientCode, Integer locationId, Integer resourceId, Integer serviceId) throws Exception;
	ResponseEntity<ResponseModel> addCustomerPledgeDetails(CustomerPledgeRequest customerPledgeReq) throws Exception;
	public ResponseEntity<ResponseModel> updateCustomerPledgeDetails(CustomerPledgeRequest customerPledgeReq) throws Exception;
	public ResponseEntity<ResponseModel> getApptsForOutlook(String clientCode, String userName, String password) throws Exception;
	public ResponseEntity<ResponseModel> updateOutlookSyncStatus(OutlookSyncReq outlookSyncReq) throws Exception;
	public ResponseEntity<ResponseModel> getItemizedReportTemplate(String clientCode, Integer locationId, Integer serviceId, String fromDate, String toDate, String reportCategory) throws Exception;

    ResponseEntity<ResponseModel> getSuggestedResourceWorkingHours(String clientCode, String locationId, String resourceIds, String fromDate, String toDate) throws Exception;

    ResponseEntity<ResponseModel> updateResourceWorkingHours(ResourceHoursRequest resourceHoursRequest) throws Exception;
	public ResponseEntity<ResponseModel> getAvailableDates(String clientCode, String device, Long locationId, Long departmentId, String resourceIds, String serviceIds) throws Exception;
	ResponseEntity<ResponseModel> getJSCalendarAvailablity(String clientCode, String calendarDate, Integer locationId, String resourceIdStr, String serviceIdStr) throws Exception;
	ResponseEntity<ResponseModel> releaseHoldAppointment(String clientCode, String device, Long scheduleId) throws Exception;
	ResponseEntity<ResponseModel> createOrUpdateCustomer(CustomerRequest customerRequest) throws Exception;
	public ResponseEntity<ResponseModel> getVerifyPageData(String clientCode, String device, String langCode, Long customerId, Long scheduleId) throws Exception;
	ResponseEntity<ResponseModel> getOneDateResourceWorkingHrs(String clientCode, Integer locationId, Integer resourceId, String date) throws Exception;
	public ResponseEntity<ResponseModel> updateConfirmAppointment(String clientCode, String comments, Long serviceId, Long scheduleId) throws Exception;
	public ResponseEntity<ResponseModel> updateOneDateResourceWorkingHrs(ResourceWorkingHoursRequest resourceWorkingHoursReq) throws Exception;
	ResponseEntity<ResponseModel> getOneDateResourceWorkingHoursDetails(String clientCode) throws Exception;
	ResponseEntity<ResponseModel> getUserAcitivityLogs(String clientCode, Integer userId, String startDate, String endDate) throws Exception;
	public ResponseEntity<ResponseModel> updatePrivilegeSettings(PrivilegeSettings privilegeSettings) throws Exception;
	public ResponseEntity<ResponseModel> getAccessPrivilege(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getPrivilegePageMapping(String clientCode, int accessPrivilegeId) throws Exception;
	public ResponseEntity<ResponseModel> getPasswordComplexityLogic(String clientCode) throws Exception;
	ResponseEntity<ResponseModel> getPasswordComplexityLogicByUserName(String userName) throws Exception;
	public ResponseEntity<ResponseModel> getCustomerPledgeStatusList(String clientCode) throws Exception;
	public ResponseEntity<ResponseModel> getCustomerPledgeFundSourceList(String clientCode) throws Exception;
	ResponseEntity<ResponseModel> getCustomerPledgeVendorList(String clientCode, String fundId) throws Exception;
	ResponseEntity<ResponseModel> updatePassword(ResetPassword resetPassword) throws Exception;
	public ResponseEntity<ResponseModel> validateOldPassword(ChangePassword changepassword) throws Exception;
	public ResponseEntity<ResponseModel> checkValidUserName(String userName, Integer userId) throws Exception;

	public ResponseEntity<ResponseModel> getCustomerPastApptsList(String clientCode, Long customerId) throws Exception;
	public ResponseEntity<ResponseModel> getAppointmentStatusReportList(String clientCode) throws Exception;
	ResponseEntity<ResponseModel> deleteUser(Integer userId) throws Exception;

	ResponseEntity<ResponseModel> updateResourceCalendarWithScheduleId(String clientCode, Integer resourceId, String date,
																	   String timeSlots, String action, int scheduleId) throws Exception;
	ResponseEntity<ResponseModel> closeAppt(String clientCode, Integer resourceId, String date, String timeSlots) throws Exception;
	public ResponseEntity<ResponseModel> getPrivilegeByUserPrivilege(String clientCode, String accessPrivilegeName) throws Exception;
	ResponseEntity<ResponseModel> deletePledge(String clientCode, String customerPledgeId, String fundName, String eligible, String houseHoldId) throws Exception;
}
