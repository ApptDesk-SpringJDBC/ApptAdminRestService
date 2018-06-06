package com.telappoint.admin.appt.common.dao;

import com.telappoint.admin.appt.common.model.*;
import com.telappoint.admin.appt.common.model.request.ConfirmAppointmentRequest;
import com.telappoint.admin.appt.common.model.request.CustomerPledgeRequest;
import com.telappoint.admin.appt.common.model.request.CustomerRequest;
import com.telappoint.admin.appt.common.model.request.OutlookSyncReq;
import com.telappoint.admin.appt.common.model.request.ResourceHoursRequest;
import com.telappoint.admin.appt.common.model.request.ResourceWorkingHoursRequest;
import com.telappoint.admin.appt.common.model.response.*;
import com.telappoint.admin.appt.handlers.exception.TelAppointException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface AdminDAO {
    // location API's
    public List<Location> getLocationList(JdbcCustomTemplate jdbcCustomTemplate, String filterKeyWord, boolean activeList) throws TelAppointException;
    public Location getLocationById(JdbcCustomTemplate jdbcCustomTemplate, String filterKeyWord, Integer locationId) throws TelAppointException;
    public List<Location> getLocationListByLocationIds(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> locationIds, String filterKeyWord, boolean isActiveList) throws TelAppointException;
	public List<Location> getLocationListByResourceIds(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String filterKeyWord, boolean isActiveList) throws TelAppointException;
    
    // resource API's
    public List<Resource> getResourceList(JdbcCustomTemplate jdbcCustomTemplate, String filterKeyWord, boolean isActiveList) throws TelAppointException;
    public List<Resource> getResourcesByLocationId(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, String filterKeyWord, boolean isActiveList) throws TelAppointException;
    public List<Resource> getResourceListByIds(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String filterKeyWord, boolean isActiveList) throws TelAppointException;
    public Resource getResourceById(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, String filterKeyWord, boolean isActiveList) throws TelAppointException;  
    
    // service API's
    public List<ServiceVO> getServiceList(JdbcCustomTemplate jdbcCustomTemplate, int blockTimeInMins, String filterKeyWord, boolean isActiveList) throws TelAppointException;
    public List<ServiceVO> getServiceListByResourceIds(JdbcCustomTemplate jdbcCustomTemplate, int blockTimeInMins, List<Integer> resourceIds, String filterKeyWord, boolean isSelfService) throws TelAppointException;
    public List<ServiceVO> getServiceListByIds(JdbcCustomTemplate jdbcCustomTemplate, int blockTimeInMins, List<Integer> serviceIds, String filterKeyWord, boolean isActiveList) throws TelAppointException;
    public ServiceVO getServiceById(JdbcCustomTemplate jdbcCustomTemplate, Integer serviceId, int blockTimeInMins, String filterKeyWord, boolean isIncludingSuspended, boolean isActiveList) throws Exception;
    public List<ServiceVO> getServiceListByResourceId(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, int blockTimeInMins, String filterKeyWord, boolean isActiveList) throws Exception;
    
    //graphs API's
    public Long getApptOpenTimeSlotsCount(JdbcCustomTemplate jdbcCustomTemplate, int locationId, List<Integer> resourceIds, String startDate, String endDate) throws TelAppointException;
    public int getMinBlocks(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds) throws TelAppointException;
    public Long getHoldAppointmentsCount(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String startDate, String endDate) throws TelAppointException;
    public Long getBookedAppointmentsCount(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String startDate, String endDate) throws TelAppointException;
   
    public List<Integer> getResourceIds(JdbcCustomTemplate jdbcCustomTemplate, int locationId) throws TelAppointException;
    public String getFirstAvailableDate(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateStr, boolean isPastDate) throws TelAppointException;
	
	public void getStackChartInfo(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Integer locationId, Integer resourceId, String stackChartType, StackedChartResponse stackChartResponse) throws TelAppointException;
	public DisplayNames getDisplayNames(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public Map<String, List<String>> getPrivilegedPageNames(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public List<ServiceLocation> getServiceLocationDates(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId) throws Exception;
	public boolean updateApptRestrictDates(JdbcCustomTemplate jdbcCustomTemplate, String yyyyMMddStartDate, String yyyyMMddEndDate) throws TelAppointException;
	public boolean updateApptPerSeasonDetails(JdbcCustomTemplate jdbcCustomTemplate, String termStartDate, String termEndDate, Integer noApptPerTerm) throws TelAppointException;
	public boolean updateServiceLocationApptDatesWindow(JdbcCustomTemplate jdbcCustomTemplate, List<ServiceLocation> serviceLocationList) throws TelAppointException;
	public boolean updateLocationsApptDates(JdbcCustomTemplate jdbcCustomTemplate, List<Location> locations) throws TelAppointException;
	public void updateScheduleClosedStatus(JdbcCustomTemplate jdbcCustomTemplate, String closedStatus) throws TelAppointException;
    public ApptSysConfig getAppSysConfig(JdbcCustomTemplate jdbcCustomTemplate) throws TelAppointException;
    //public void setPrevNextButtonFlags(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds) throws Exception;
    public String getFirstAvailableDateOrBooked(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String timeZone) throws Exception;
    public String getCalStartAndEndDateTime(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds,String startDateTime, String endDateTime, boolean fetchAvailable) throws Exception;
    public String getResourceTimeSlots(JdbcCustomTemplate jdbcCustomTemplate,Integer resourceId, String startDateTime, String endDateTime) throws Exception;
	public boolean updateLocation(JdbcCustomTemplate jdbcCustomTemplate, Location location) throws Exception;
	public List<ServiceVO> getDeletedServiceList(JdbcCustomTemplate jdbcCustomTemplate, Integer blockTimeInMins, String filterKey) throws Exception;
	public List<Resource> getDeletedResourceList(JdbcCustomTemplate jdbcCustomTemplate, String filterKey) throws Exception;
	public List<Location> getDeletedLocationList(JdbcCustomTemplate jdbcCustomTemplate, String filterKey) throws Exception;
	public List<ResourcePrefix> getResourcePrefixList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public List<ResourceTitle> getResourceTitleList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public Map<String, DynamicFieldDisplay> getDynamicFieldDisplay(JdbcCustomTemplate jdbcCustomTemplate, String tableName) throws Exception;
	public boolean deleteLocation(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId) throws Exception;
	public boolean unDeleteLocation(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId) throws Exception;
	public boolean updateResource(JdbcCustomTemplate jdbcCustomTemplate, Resource resource) throws Exception;
	public Integer addLocation(JdbcCustomTemplate jdbcCustomTemplate, Location location) throws Exception;
	public boolean deleteResource(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId) throws Exception;
	public boolean unDeleteResource(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId) throws Exception;
	public boolean deleteService(JdbcCustomTemplate jdbcCustomTemplate, Integer serviceId) throws Exception;
	public boolean unDeleteService(JdbcCustomTemplate jdbcCustomTemplate, Integer serviceId) throws Exception;
	public Integer addResource(JdbcCustomTemplate jdbcCustomTemplate, Resource resource) throws Exception;
	public Integer addService(JdbcCustomTemplate jdbcCustomTemplate, ServiceVO service, Integer blockTimeInMins) throws Exception;
	public boolean updateService(JdbcCustomTemplate jdbcCustomTemplate, ServiceVO service, Integer blockTimeInMins) throws Exception;
	public List<ResourceType> getResourceTypeList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public List<IvrCalls> getInBoundCallLogs(JdbcCustomTemplate jdbcCustomTemplate, String fromDate, String toDate, String callerId) throws Exception;
	public List<OutBoundCalls> getOutBoundCallLogs(JdbcCustomTemplate jdbcCustomTemplate, String fromDate, String toDate, String callerId) throws Exception;
	public boolean addLocationWorkingHrs(JdbcCustomTemplate jdbcCustomTemplate, ApptSysConfig apptSysConfig, Integer locationId) throws Exception;
	public List<Procedure> getProcedureList(JdbcCustomTemplate jdbcCustomTemplate, boolean isActiveList);
	boolean addProcedureLocation(JdbcCustomTemplate jdbcCustomTemplate, List<Procedure> procedureList, Integer locationId) throws Exception;
	public List<Location> getLocationsByServiceIdToCloseServiceStatus(JdbcCustomTemplate jdbcCustomTemplate, Integer serviceId) throws Exception;
	public List<DynamicIncludeReport> getDynamicIncludeReportsData(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public List<AppointmentReportData> getAppointmentReport(JdbcCustomTemplate jdbcCustomTemplate, String fromDate, String toDate, String locationIds, String resourceIds,
			String serviceIds, String apptStatus) throws Exception;
	public List<SearchAppointmentData> searchByFirstLastName(JdbcCustomTemplate jdbcCustomTemplate, String firstName, String lastName) throws Exception;
	public List<SearchAppointmentData> searchByConfirmationNumber(JdbcCustomTemplate jdbcCustomTemplate, Long confirmationNumber) throws Exception;
	public List<SearchAppointmentData> searchByAccountNumber(JdbcCustomTemplate jdbcCustomTemplate, String accountNumber) throws Exception;
	List<SearchAppointmentData> searchByContactPhone(JdbcCustomTemplate jdbcCustomTemplate, String contactPhone) throws Exception;
	List<SearchAppointmentData> searchByCallerId(JdbcCustomTemplate jdbcCustomTemplate, String callerId) throws Exception;
	public List<SearchAppointmentData> searchByAttrib1(JdbcCustomTemplate jdbcCustomTemplate, String attrib1) throws Exception;
	List<SearchAppointmentData> searchByDOB(JdbcCustomTemplate jdbcCustomTemplate, String dob) throws Exception;
	List<SearchAppointmentData> searchByHouseHoldId(JdbcCustomTemplate jdbcCustomTemplate, Long houseHoldId) throws Exception;
	List<SearchAppointmentData> getAppointmentsByCustomerId(JdbcCustomTemplate jdbcCustomTemplate, String timeZone, Long customerId) throws Exception;
	List<DynamicSearchByFields> getSearchDropDownList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	List<Customer> getCustomersById(JdbcCustomTemplate jdbcCustomTemplate, Long customerId) throws Exception;
	public List<CustomerActivity> getCustomerActivities(JdbcCustomTemplate jdbcCustomTemplate, Long customerId) throws Exception;
	List<Customer> getHouseHoldInfo(JdbcCustomTemplate jdbcCustomTemplate, Long houseHoldId) throws Exception;
	public List<AppointmentStatusData> getAppointmentStatusDropDownList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public boolean mergeHouseHoldId(JdbcCustomTemplate jdbcCustomTemplate, String fromHouseHoldIds, String mergeToHouseHoldId) throws Exception;
	public Long getNextHouseHoldId(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public boolean splitHouseHoldId(JdbcCustomTemplate jdbcCustomTemplate, String customerIds, String newHouseHoldId) throws Exception;
	public List<Customer> getBlockedCustomers(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public boolean updateCustomerBlockedReason(JdbcCustomTemplate jdbcCustomTemplate, Long customerId, String reasonMessage) throws Exception;
	public boolean unBlockCustomer(JdbcCustomTemplate jdbcCustomTemplate, Long customerId) throws Exception;
	@Deprecated
	public Map<Customer, List<AppointmentData>> searchAppointmentsByFirstLastName(JdbcCustomTemplate jdbcCustomTemplate, String firstName, String lastName) throws Exception;
	@Deprecated
	public Map<Customer, List<AppointmentData>> searchAppointmentsByAccountNumber(JdbcCustomTemplate jdbcCustomTemplate, String accountNumber) throws Exception;
	List<TransState> getTransStateList(JdbcCustomTemplate jdbcCustomTemplate, Long transId) throws Exception;
	Map<String, List<JSPPagesPrivileges>> getPrivilegeSettings(JdbcCustomTemplate jdbcCustomTemplate, String accessPrivilegeName) throws Exception;
	TablePrintViewResponse getTablePrintViewData(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, String resourceIds, String date) throws Exception;
	boolean isHoliday(JdbcCustomTemplate jdbcCustomTemplate, String date) throws Exception;
	boolean isClosedDays(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, String date) throws Exception;
	StatisticsReportResult getStatisticsData(JdbcCustomTemplate jdbcCustomTemplate, String fromDate, String toDate, String summaryReportFor) throws Exception;
	StatisticsReportResult getLocationServiceStatisticsData(JdbcCustomTemplate jdbcCustomTemplate, String fromDate, String toDate) throws Exception;
	SummaryReportResponse getSummaryStatisticReportData(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, Integer serviceId, String fromDate, String toDate,
			String reportCategory, List<Integer> apptStatus, SummaryReportResponse summaryReportResponse) throws Exception;
	void getI18nEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Map<String, Map<String, String>> map) throws TelAppointException, Exception;
	public void cancelAppointment(JdbcCustomTemplate jdbcCustomTemplate, Long scheduleId, Integer cancelMethod, String langCode, ClientDeploymentConfig cdConfig,
			CancelAppointResponse cancelAppointResponse) throws Exception;
	boolean updateAppointmentStatus(JdbcCustomTemplate jdbcCustomTemplate, long scheduleId, int status, String userName) throws Exception;
	boolean isBlockedFromFutureAppts(JdbcCustomTemplate jdbcCustomTemplate, int status) throws Exception;
	boolean updateCustomerBlockStatus(JdbcCustomTemplate jdbcCustomTemplate, long scheduleId) throws Exception;
	List<PledgeDetails> getPledgeReport(JdbcCustomTemplate jdbcCustomTemplate, String fromDate, String toDate, Integer locationId, String groupByIntake, String groupByFundSource,
			String groupByVendor, Integer resourceId, Integer fundSourceId) throws Exception;
	
	List<ServiceVO> getServiceDropDownList(JdbcCustomTemplate jdbcCustomTemplate, String filterKeyWord, int blockTimeInMins, Integer locationId, Integer serviceId,
			boolean isActiveList) throws Exception;
	public Map<Resource, List<ServiceVO>> getResourceServiceList(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, Integer blocksTimeInMins) throws Exception;
	public List<ServiceVO> getServiceListByLocationId(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, Integer blockTimeInMins, String filterKeyWord, boolean onlyActive) throws Exception;
	public Resource getResourceById(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, boolean isActiveList) throws Exception;
	public List<DynamicPledgeResult> getDynamicPledgeResultList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	void getWeeklyCalendarData(JdbcCustomTemplate jdbcCustomTemplate, String date, Integer locationId, List<Integer> resourceIds, WeeklyCalendarResponse weeklyCalendarResponse)
			throws Exception;
	Map<String, Long> getNoOfOpenAppts(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymm) throws Exception;
	Map<String, Long> getNoOfBookedAppts(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymm) throws Exception;
	Map<String, Long> getNoOfClosedTimeSlots(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymm) throws Exception;
	Map<String, Long> getNoOfTotalTimeSlots(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymm) throws Exception;
	List<MonthlyApptData> getOpenTimeSlots(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymm) throws Exception;
	Map<String, Long> getHolidaysMap(JdbcCustomTemplate jdbcCustomTemplate, String dateyyyymm) throws Exception;
	Map<String, Long> getClosedDaysMap(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, String dateyyyymm) throws Exception;
	int getMinBlocksByAdmin(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds) throws Exception;
	String getMonthFirstLastDate(JdbcCustomTemplate jdbcCustomTemplate, String calendarDateDB) throws Exception;
	String getNextDateAndIsContinueValue(JdbcCustomTemplate jdbcCustomTemplate, String currentDate, String lastDate) throws Exception;
	void getDailyCalendarData(JdbcCustomTemplate jdbcCustomTemplate, String date, Integer locationId, List<Integer> resourceIds, DailyCalendarResponse dailyCalendarResponse)
			throws Exception;
	public void updateTransId(JdbcCustomTemplate jdbcCustomTemplate, Long scheduleId, Long transId) throws Exception;
	void bookAppointment(JdbcCustomTemplate jdbcCustomTemplate, Long scheduleId, String langCode, Integer apptMethod,
			ClientDeploymentConfig cdConfig, ConfirmAppointmentResponse confirmAppointmentResponse) throws Exception;
	List<Customer> getCustomerList(JdbcCustomTemplate jdbcCustomTemplate, String customerName) throws Exception;
	void getI18nDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Map<String, Map<String, String>> map) throws Exception;

	HoldAppt holdAppointment(JdbcCustomTemplate jdbcCustomTemplate, String device, Long locationId, Long resourceId, Long procedureId, Long departmentId, Long serviceId,
			Long customerId, String apptDateTime, ClientDeploymentConfig cdConfig, Long transId) throws Exception;
	boolean customerExist(JdbcCustomTemplate jdbcCustomTemplate, List<CustomerRegistration> customerRegList, CustomerRequest customerRequest) throws Exception;
	long saveCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomerRequest customerRequest, ClientDeploymentConfig cdConfig) throws TelAppointException, Exception;
	public boolean updateCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomerRequest customerRequest) throws Exception;
	public void getFutureAppointments(JdbcCustomTemplate jdbcCustomTemplate, long customerId, ClientDeploymentConfig cdConfig, List<AppointmentDetails> apptList) throws Exception;
	boolean updateAppointStatus(JdbcCustomTemplate jdbcCustomTemplate, String screenedFlag, String accessedFlag, Integer status, Long scheduleId) throws Exception;
	RecordTimeResponse updateRecordTime(JdbcCustomTemplate jdbcCustomTemplate, String recordName, String recordType, long scheduleId, String timeZone) throws Exception;
	void getPledgeHistory(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Long customerId, CustomerPledgeResponse pledgeRes) throws Exception;
	public void getPastAppointments(JdbcCustomTemplate jdbcCustomTemplate, long customerId, ClientDeploymentConfig cdConfig, List<AppointmentDetails> apptList) throws Exception;
	public ApptStatusResponse getAppointmentStatus(JdbcCustomTemplate jdbcCustomTemplate, Long scheduleId) throws Exception;
	List<ServiceVO> getSameServiceBlockList(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, Integer serviceId, String filterKeyWord, Integer blockTimeInMins)
			throws Exception;
	boolean updateCustomerForPledge(JdbcCustomTemplate jdbcCustomTemplate, CustomerPledgeRequest customerPledgeReq) throws Exception;
	boolean addCustomerPledgeDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomerPledgeRequest customerPledgeReq, boolean fromAppt) throws Exception;
	boolean updateCustomerPledgeDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomerPledgeRequest customerPledgeReq) throws Exception;
	public List<OutLookAppointment> getApptsForOutlook(JdbcCustomTemplate jdbcCustomTemplate, String resourceIds, Integer blockTimeInMins) throws Exception;
	public boolean updateOutlookSyncStatus(JdbcCustomTemplate jdbcCustomTemplate, OutlookSyncReq outlookSyncReq) throws Exception;
	void getI18nPageContentMap(JdbcCustomTemplate jdbcCustomTemplate, Map<String, Map<String, String>> map) throws TelAppointException, Exception;
	List<CustomerRegistration> getCustomerRegistrationList(JdbcCustomTemplate jdbcCustomTemplate, String langCode, List<String> loginTypes, final Map<String, String> labelMap) throws Exception;
	List<Map<String,Object>> getResourceWorkingHoursHistory(JdbcCustomTemplate jdbcCustomTemplate, String locationId, String resourceIds, String fromDate, String toDate);
	List<Map<String,Object>> getResourceWorkingHoursHistory(JdbcCustomTemplate jdbcCustomTemplate, String locationId, String resourceIds);
	Map<String,Object> getApptSysConfigDefaultResourceWorkingHours(JdbcCustomTemplate jdbcCustomTemplate);
	List<Map<String,Object>> getResourceWorkingHours(JdbcCustomTemplate jdbcCustomTemplate, String locationId, String resourceIds);
	boolean updateCustomerIdInSchedule(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long customerId, Long scheduleId) throws Exception;
	public com.telappoint.admin.appt.common.model.AvailableDateTimes getAvailableDates(JdbcCustomTemplate jdbcCustomTemplate,String timeZone,
            Long locationId, Long departmentId, Long resourceId, Long serviceId, Long blockTimeMins) throws Exception;
	Map<String, Long> getJSNoOfOpenAppts(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymm) throws Exception;
	void releaseHoldAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long scheduleId) throws Exception;
	Long getCustomerIdIfExist(JdbcCustomTemplate jdbcCustomTemplate, List<CustomerRegistration> customerRegList, CustomerRequest customerRequest) throws Exception;
	public void updateSchedule(JdbcCustomTemplate jdbcCustomTemplate, ConfirmAppointmentRequest confirmApptReq) throws Exception;
	VerifyPageData getVerfiyPageData(JdbcCustomTemplate jdbcCustomTemplate, String device, String langCode, Long scheduleId, Map<String, String> aliasMap)
			throws Exception;
	public void updateCustomerIdInSchedule(JdbcCustomTemplate jdbcCustomTemplate, Long customerId, Long scheduleId) throws Exception;

	Boolean getCampaign(JdbcCustomTemplate jdbcCustomTemplate, int campaignId);

	Map<String, Object> getCampaignById(JdbcCustomTemplate jdbcCustomTemplate, int campaignId);

	List<Map<String,Object>> getCalMinAndMaxDateTime(JdbcCustomTemplate jdbcCustomTemplate, Set<Integer> resourceIds, Timestamp fromTimestamp);

	List<Map<String,Object>> getCalMinAndMaxDateTime(JdbcCustomTemplate jdbcCustomTemplate, Set<Integer> resourceIds, Timestamp fromTimestamp, Timestamp toTimestamp);

    List<Map<String,Object>> fetchBookedAppointments(JdbcCustomTemplate jdbcCustomTemplate, String sql);

	List<Map<String, Object>> getScheduleList(JdbcCustomTemplate jdbcCustomTemplate, Set<Long> scheduleIds);

    Schedule getScheduleById(JdbcCustomTemplate jdbcCustomTemplate, long scheduleId);

	boolean updateScheduleCancel(JdbcCustomTemplate jdbcCustomTemplate, ResourceWorkingHrsResponse baseApptRequest, long scheduleId, int status);

    Appointment getAppointmentByScheduleId(JdbcCustomTemplate jdbcCustomTemplate, long scheduleId);

	int updateAppointment(JdbcCustomTemplate jdbcCustomTemplate, Appointment appointment);

	void updateNotifyCancelStatus(JdbcCustomTemplate jdbcCustomTemplate, Long scheduleId);

	Appointment getAppointmentByScheduleId(JdbcCustomTemplate jdbcCustomTemplate, Long transId, Long scheduleId);

	Map<String,Object> getApptSysConfig(JdbcCustomTemplate jdbcCustomTemplate);

	List<Map<String,Object>> getResourceCalendarForApptDate(JdbcCustomTemplate jdbcCustomTemplate, Object resiurceId, Set<String> timeDateSet, long fetchId);

	void updateResourceCalendar(JdbcCustomTemplate jdbcCustomTemplate, List<Map<String,Object>> resourceCalendarList);

	int updateResourceCalendarClose(JdbcCustomTemplate jdbcCustomTemplate, String updateQueryClose);

	int updateResourceCalendarOpen(JdbcCustomTemplate jdbcCustomTemplate, String updateQueryOpen);

	void addResourceWorkingHrsHistory(JdbcCustomTemplate jdbcCustomTemplate, ResourceHoursRequest resourceHoursRequest, ResourceWorkingHrsHistory resourceWorkingHrsHistory);

	void saveNotify(JdbcCustomTemplate jdbcCustomTemplate, Notify notify);

	List<ResourceDisplayTime> getResourceDisplayTime(JdbcCustomTemplate jdbcCustomTemplate, String time, Integer resourceId);

	List<Map<String, Object>> getDisplayTimeInConfirmPage(JdbcCustomTemplate jdbcCustomTemplate, String time, Integer resourceId);
	public Timestamp getMaxDateTime(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	void insertResourceCalendar(JdbcCustomTemplate jdbcCustomTemplate, List<SqlParameterSource> paramSourceList) throws Exception;
	public List<Integer> getDepartmentIds(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	boolean addOrUpdateResourceService(JdbcCustomTemplate jdbcCustomTemplate, Resource resource, boolean isUpdate) throws Exception;
	public void addOrUpdateLocationDepartionResource(JdbcCustomTemplate jdbcCustomTemplate, Resource resource, boolean isUpate) throws Exception;
	boolean addResourceService(JdbcCustomTemplate jdbcCustomTemplate, ServiceVO service) throws Exception;
	List<Map<String, Object>> fetchBookedAppointments(JdbcCustomTemplate jdbcCustomTemplate, String sql, MapSqlParameterSource paramSource) throws Exception;
	int updateResourceCalendarClose(JdbcCustomTemplate jdbcCustomTemplate, String updateQueryClose, MapSqlParameterSource paramSource);
	int updateResourceCalendarOpen(JdbcCustomTemplate jdbcCustomTemplate, String updateQueryOpen, MapSqlParameterSource paramSource);
	void getMinMaxTime(JdbcCustomTemplate jdbcCustomTemplate, String date, Integer resourceId, OneDateWorkingHours oneDateWorkingHours, int blockTimeInMins) throws Exception;
	List<OneDateResourceWorkingHoursDetails> getOneDateResourceWorkingHoursDetails(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	boolean insertResourceWorkingHrsHistory(JdbcCustomTemplate jdbcCustomTemplate, ResourceWorkingHoursRequest resourceWorkingHoursReq, String date) throws Exception;
	public void updateResourceSpecificDate(JdbcCustomTemplate jdbcCustomTemplate, ResourceWorkingHoursRequest resourceWorkingHoursReq, String date) throws Exception;
	List<UserActivityLog> getUserActivityLog(JdbcCustomTemplate jdbcCustomTemplate, Integer userId, String startDate, String endDate) throws Exception;
	public List<AccessPrivilege> getAccessPrivilege(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	List<String> getPrivilegeMapping(JdbcCustomTemplate jdbcCustomTemplate, int accessPrivilegeId) throws Exception;
	public List<CustomerPledgeStatus> getCustomerPledgeStatusList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public List<CustomerPledgeFundSource> getCustomerPledgeFundSourceList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	public List<CustomerPledgeVendor> getCustomerPledgeVendorList(JdbcCustomTemplate jdbcCustomTemplate, String fundId) throws Exception;
	public List<CustomerPastAppts> getCustomerPastApptsList(JdbcCustomTemplate jdbcCustomTemplate, Long customerId) throws Exception;
	List<AppointmentStatusData> getAppointmentStatusReport(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	List<Integer> getDistinctApptStatusIds(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	List<AppointmentStatusData> getAppointmentStatusReportList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	boolean updateResourceCalendarWithScheduleId(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, String date, List<String> timeList, int scheduleId);

	boolean closeScheduledAppointment(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, String date, List<String> timeList, int scheduleIdValue);
	List<ItemizedReportGoal> getItemizedReportGoal(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;
	Map<String, List<JSPPagesPrivileges>> getPrivilegeByUserPrivilege(JdbcCustomTemplate jdbcCustomTemplate, String accessPrivilegeName) throws Exception;
	void deletePledge(JdbcCustomTemplate jdbcCustomTemplate, String customerPledgeId) throws Exception;
	public void updateLHEAPandPSEHelpRecievedStatus(JdbcCustomTemplate jdbcCustomTemplate, String houseHoldId, String fundName, String eligible) throws Exception;
}
