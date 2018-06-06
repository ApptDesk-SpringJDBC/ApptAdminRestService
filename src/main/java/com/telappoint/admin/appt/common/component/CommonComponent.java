package com.telappoint.admin.appt.common.component;

import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.telappoint.admin.appt.common.model.*;
import com.telappoint.admin.appt.common.model.response.ResourceWorkingHrsResponse;
import com.telappoint.admin.appt.common.util.DateUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telappoint.admin.appt.common.constants.FilterKeyWordContants;
import com.telappoint.admin.appt.common.dao.AdminDAO;
import com.telappoint.admin.appt.common.dao.MasterDAO;
import com.telappoint.admin.appt.common.util.AdminUtils;
import com.telappoint.admin.appt.handlers.exception.TelAppointException;

/**
 * 
 * @author Balaji N
 *
 */
@Component
public class CommonComponent {

	@Autowired
	private MasterDAO masterDAO;

	@Autowired
	private AdminDAO adminDAO;

	@Autowired
	private CacheComponent cacheComponent;
	
	private VelocityEngine ve = null;
	
	public StringWriter processTemplate(String templateName, String templateContent, Object data) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext();
		StringResourceRepository repo = StringResourceLoader.getRepository();
		repo.putStringResource(templateName, templateContent);
		if(data instanceof Map) {
			Map<String, Object> map = (Map<String, Object>)data; 
			 for ( Map.Entry<String, Object> e : map.entrySet() ) {
			        context.put(e.getKey(), e.getValue() ==null?"":e.getValue());
			 }
		} else {
			context.put("dynamicData", data);
		}
		Template t = ve.getTemplate(templateName);
		t.merge(context, writer);

		return writer;
	}
	
	private final static Logger logger = Logger.getLogger(CommonComponent.class);
	public ResponseModel populateRMDSuccessData(Object data) {
		return populateRMDSuccessData(data, true);
	}

	public ResponseModel populateRMDData(Object data) {
		return populateRMDSuccessData(data, true);
	}

	public ResponseModel populateRMDSuccessData(Object data, boolean logging) {
		if (logging == false) {
			// TODO: log it
		}
		ResponseModel responseModel = new ResponseModel();
		responseModel.setData(data);
		return responseModel;
	}
	
	public List<Location> getLocationList(JdbcCustomTemplate jdbcCustomTemplate, Long loginUserId, String filterKeyWord, boolean isActiveList) throws TelAppointException {
		List<Location> locationList = new ArrayList<Location>();
		AdminLogin adminLogin = masterDAO.getUserDetailsByUserId(loginUserId);
		if (null != adminLogin) {
			String accessLevel = adminLogin.getAccessLevel();
			if (AdminUtils.isHighAccessLevelUser(accessLevel) || AdminUtils.isReadOnlyAccessLevelUser(accessLevel) || AdminUtils.isSchedulerLevelUser(accessLevel)) {
				locationList = adminDAO.getLocationList(jdbcCustomTemplate, filterKeyWord, isActiveList);
			} else {
				if (AdminUtils.isLocationAccessLevelUser(accessLevel)) {
					String locationIds = AdminUtils.getStringWithoutStartingAndEndingComma(adminLogin.getLocationIds());
					List<Integer> list = Pattern.compile(",").splitAsStream(locationIds).map(Integer::parseInt).collect(Collectors.toList());
					locationList = adminDAO.getLocationListByLocationIds(jdbcCustomTemplate, list, filterKeyWord,  isActiveList);
				} else if (AdminUtils.isResourceAccessLevelUser(accessLevel)) {
					String resourceIds = AdminUtils.getStringWithoutStartingAndEndingComma(adminLogin.getResourceIds());
					List<Integer> list = Pattern.compile(",").splitAsStream(resourceIds).map(Integer::parseInt).collect(Collectors.toList());
					locationList = adminDAO.getLocationListByResourceIds(jdbcCustomTemplate, list, filterKeyWord, isActiveList);
				}
			}
		}
		return locationList;
	}
	
	public List<Resource> getResourceList(JdbcCustomTemplate jdbcCustomTemplate, Long loginUserId, boolean isAcitveList) throws TelAppointException {
		List<Resource> resourceList = new ArrayList<Resource>();
		AdminLogin adminLogin = masterDAO.getUserDetailsByUserId(loginUserId);
		if (null != adminLogin) {
			String accessLevel = adminLogin.getAccessLevel();
			if (AdminUtils.isHighAccessLevelUser(accessLevel) || AdminUtils.isReadOnlyAccessLevelUser(accessLevel) || AdminUtils.isSchedulerLevelUser(accessLevel)) {
				resourceList = adminDAO.getResourceList(jdbcCustomTemplate, FilterKeyWordContants.RESOURCE_HOME_PAGE_DATA.getFilterKey(), isAcitveList);
			} else if (AdminUtils.isResourceAccessLevelUser(accessLevel)) {
				String resourceIds = AdminUtils.getStringWithoutStartingAndEndingComma(adminLogin.getResourceIds());
				List<Integer> list = Pattern.compile(",").splitAsStream(resourceIds).map(Integer::parseInt).collect(Collectors.toList());
				resourceList = adminDAO.getResourceListByIds(jdbcCustomTemplate, list, FilterKeyWordContants.RESOURCE_HOME_PAGE_DATA.getFilterKey(), true);
			}
		}
		return resourceList;
    }

	public List<ServiceVO> getServiceList(JdbcCustomTemplate jdbcCustomTemplate, Integer blockTimeInMins, Long loginUserId, String filterKeyWord, boolean isActiveList) throws Exception {
		List<ServiceVO> serviceList = new ArrayList<ServiceVO>();
		AdminLogin adminLogin = masterDAO.getUserDetailsByUserId(loginUserId);
		if (null != adminLogin) {
			String accessLevel = adminLogin.getAccessLevel();
			if (AdminUtils.isHighAccessLevelUser(accessLevel) || AdminUtils.isReadOnlyAccessLevelUser(accessLevel) || AdminUtils.isSchedulerLevelUser(accessLevel)) {
				serviceList = adminDAO.getServiceList(jdbcCustomTemplate, blockTimeInMins, filterKeyWord, isActiveList);
			} else {
				if (AdminUtils.isLocationAccessLevelUser(accessLevel)) {
					String locationIds = AdminUtils.getStringWithoutStartingAndEndingComma(adminLogin.getLocationIds());
					List<Integer> list = Pattern.compile(",").splitAsStream(locationIds).map(Integer::parseInt).collect(Collectors.toList());
					
					//TODO: below api's should be implemented.
					//serviceList = adminDAO.getServiceListByLocationIds(jdbcCustomTemplate, list);
				} else if (AdminUtils.isResourceAccessLevelUser(accessLevel)) {
					String resourceIds = AdminUtils.getStringWithoutStartingAndEndingComma(adminLogin.getResourceIds());
					List<Integer> list = Pattern.compile(",").splitAsStream(resourceIds).map(Integer::parseInt).collect(Collectors.toList());
					serviceList = adminDAO.getServiceListByResourceIds(jdbcCustomTemplate, blockTimeInMins, list, filterKeyWord, true);
				}
			}
		}
		return serviceList;
	}

	public boolean updateCancelApptStatus(JdbcCustomTemplate jdbcCustomTemplate, Schedule schedule, ResourceWorkingHrsResponse baseApptRequest, int status) throws Exception {

		long scheduleId = schedule.getScheduleId();
		// update cancel status to schedule table
		boolean isCancelledSchedule = adminDAO.updateScheduleCancel(jdbcCustomTemplate, baseApptRequest, scheduleId, status);

		// update appointment type to cancel in appointment table.
		Appointment appointment = adminDAO.getAppointmentByScheduleId(jdbcCustomTemplate, scheduleId);
		appointment.setAppt_type(AppointmentType.CANCEL.getType());
		adminDAO.updateAppointment(jdbcCustomTemplate, appointment);
		String clientCode = baseApptRequest.getClientCode();
		ClientDeploymentConfig clientDeploymentConfigTO = cacheComponent.getClientDeploymentConfig(clientCode, true);
		boolean isUpdatedCal = updateResourceCalendar(jdbcCustomTemplate, clientCode, schedule, clientDeploymentConfigTO.getBlockTimeInMins(), AppointmentStatus.NONE.getStatus());

		// update notify table with cancel status
		adminDAO.updateNotifyCancelStatus(jdbcCustomTemplate, schedule.getScheduleId());
		if (isCancelledSchedule && isUpdatedCal) {
			return true;
		} else {
			return false;
		}
	}

	private boolean updateResourceCalendar(JdbcCustomTemplate jdbcCustomTemplate, String clientCode, Schedule schedule, Integer blockTimeInMins, int updateTO) {
		try {
			long fetchId = 0;
			if (updateTO == 0) {
				fetchId = schedule.getScheduleId();
			}
			String dateTime = schedule.getApptDateTime();
			if (dateTime.length() > 19) {
				dateTime = dateTime.substring(0, 19);
			}
			Set<String> timeDateSet = DateUtils.getDateTimesSet(dateTime, blockTimeInMins, schedule.getBlocks());
			List<Map<String, Object>> resourceCalendarList = adminDAO.getResourceCalendarForApptDate(jdbcCustomTemplate, schedule.getResourceId(), timeDateSet, fetchId);
			for (Map<String, Object> rc : resourceCalendarList) {
				rc.put("schedule_id",updateTO);
			}
			logger.info("With Schedule_id resourceCalendarList = " + resourceCalendarList);
			adminDAO.updateResourceCalendar(jdbcCustomTemplate, resourceCalendarList);
			logger.debug("Successfully updated resourceCalendarList");
			return true;
		} catch (Exception e) {
			logger.error("Error while updateResourceCalendar :" + e, e);
			String subject = "Error in RESTws for Client - " + clientCode;
			StringBuilder errorMsg = new StringBuilder();
//			errorMsg.append("MethodName:" + CoreUtils.removeErrorNumber(CoreUtils.getMethodName(1)));
			errorMsg.append("<br/>");
			StringBuilder inputParams = new StringBuilder("scheduleId=[" + schedule.getScheduleId() + "]");
//			errorMsg.append(CoreUtils.getJSONString(inputParams.toString()));
			errorMsg.append("<br/> Caused By:" + ((e.getMessage() !=null)?e.getMessage():"") +e.toString() + "<br/>");
//			sendErrorEmail(errorMsg, e, subject);
		}
		return false;
	}
}
