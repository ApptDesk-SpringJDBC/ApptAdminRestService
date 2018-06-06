package com.telappoint.admin.appt.common.dao.impl;

import static com.telappoint.admin.appt.common.constants.CommonDateContants.MM_DD_YYYY_DATE_FORMAT;
import static com.telappoint.admin.appt.common.constants.FilterKeyWordContants.LOCATIONS_BASIC_DATA;
import static com.telappoint.admin.appt.common.constants.FilterKeyWordContants.LOCATIONS_HOME_PAGE_DATA;
import static com.telappoint.admin.appt.common.constants.FilterKeyWordContants.LOCATION_COMPLETE_DATA;
import static com.telappoint.admin.appt.common.constants.FilterKeyWordContants.RESOURCES_BASIC_DATA;
import static com.telappoint.admin.appt.common.constants.FilterKeyWordContants.RESOURCE_COMPLETE_DATA;
import static com.telappoint.admin.appt.common.constants.FilterKeyWordContants.RESOURCE_HOME_PAGE_DATA;
import static com.telappoint.admin.appt.common.constants.FilterKeyWordContants.SERVICES_BASIC_DATA;
import static com.telappoint.admin.appt.common.constants.FilterKeyWordContants.SERVICES_DROP_DOWN_DATA;
import static com.telappoint.admin.appt.common.constants.FilterKeyWordContants.SERVICE_COMPLETE_DATA;
import static com.telappoint.admin.appt.common.util.DateUtils.getDateStringFromDate;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.telappoint.admin.appt.common.constants.PropertiesConstants;
import com.telappoint.admin.appt.common.model.AppointmentReportData;
import com.telappoint.admin.appt.common.model.AppointmentStatusData;
import com.telappoint.admin.appt.common.model.ApptSysConfig;
import com.telappoint.admin.appt.common.model.Customer;
import com.telappoint.admin.appt.common.model.CustomerActivity;
import com.telappoint.admin.appt.common.model.DisplayNames;
import com.telappoint.admin.appt.common.model.DynamicFieldDisplay;
import com.telappoint.admin.appt.common.model.DynamicFieldLabelData;
import com.telappoint.admin.appt.common.model.DynamicIncludeReport;
import com.telappoint.admin.appt.common.model.DynamicPledgeResult;
import com.telappoint.admin.appt.common.model.IvrCalls;
import com.telappoint.admin.appt.common.model.JSPPagesPrivileges;
import com.telappoint.admin.appt.common.model.Location;
import com.telappoint.admin.appt.common.model.OutBoundCalls;
import com.telappoint.admin.appt.common.model.OutLookAppointment;
import com.telappoint.admin.appt.common.model.PledgeDetails;
import com.telappoint.admin.appt.common.model.Resource;
import com.telappoint.admin.appt.common.model.ResourcePrefix;
import com.telappoint.admin.appt.common.model.ResourceTitle;
import com.telappoint.admin.appt.common.model.ResourceType;
import com.telappoint.admin.appt.common.model.SearchAppointmentData;
import com.telappoint.admin.appt.common.model.ServiceVO;
import com.telappoint.admin.appt.common.util.PropertyUtils;


/**
 * @author Balaji
 */
public class ResultSetMapperHelper {
	private static final Logger logger = Logger.getLogger(ResultSetMapperHelper.class);
	
    public static RowMapper<Resource> resourceMapper(String filterKeyWord) {
        return (rs, rowNum) -> {
            Resource resource = new Resource();
            resource.setResourceId(rs.getInt("id"));
            resource.setLocationName(rs.getString("location_name_online"));
            if(RESOURCES_BASIC_DATA.getFilterKey().equals(filterKeyWord)) {
            	resource.setEnable(rs.getString("enable"));
            	resource.setPlacement(rs.getInt("placement"));   
            	resource.setResourceName(rs.getString("prefix")+" "+rs.getString("first_name")+" "+rs.getString("last_name"));
            	String serviceNames = rs.getString("serviceNames");
            	if(serviceNames != null && !"".equals(serviceNames)) {
            		resource.setServiceNames(Arrays.asList(serviceNames.split(",")));
            	}
            } if(RESOURCE_HOME_PAGE_DATA.getFilterKey().equals(filterKeyWord) || RESOURCE_COMPLETE_DATA.getFilterKey().equals(filterKeyWord)) {
            	  resource.setFirstName(rs.getString("first_name"));
                  resource.setLastName(rs.getString("last_name"));
                  resource.setTitle(rs.getString("title"));
                  resource.setPrefix(rs.getString("prefix"));
                  resource.setEmail(rs.getString("email"));
                  resource.setPlacement(rs.getInt("placement"));   
                  
                  if(RESOURCE_COMPLETE_DATA.getFilterKey().equals(filterKeyWord)) {
                	  String audioFilePath = null;
                  	try {
                  		audioFilePath = PropertyUtils.getValueFromProperties("IVR_AUDIO_FILE_PATH", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
                  	} catch(Exception e) {
                  		logger.error("Errro to read IVR_AUDIO_FILE_PATH property.");
                  	}
                  	final String baseAudioFilePath = audioFilePath;
                	  resource.setResourceCode(rs.getString("resource_code"));
                	  resource.setLocationId(rs.getInt("location_id"));
                	  resource.setEnable(rs.getString("enable"));
                	  resource.setResourceType(rs.getString("resource_type"));
					  String fileName = rs.getString("resource_audio");
					  if (fileName!=null && !fileName.contains(baseAudioFilePath)) {
						resource.setResourceAudio(baseAudioFilePath + "/" + fileName);
					  } else {
						resource.setResourceAudio((fileName !=null && fileName.contains(".wav")) ? fileName : (fileName == null)?"":fileName + ".wav");
					  }
                	//resource.setResourceAudio(baseAudioFilePath!=null?baseAudioFilePath+"/"+rs.getString("resource_audio")+".wav":rs.getString("resource_audio")+".wav");
                	  resource.setEnable(rs.getString("enable"));
                	  resource.setAllowSelfService(rs.getString("allow_selfservice"));
                  }
            }
           
            return resource;
        };
    }
    
    public static RowMapper<Resource> resourceMapperById() {
        return (rs, rowNum) -> {
            Resource resource = new Resource();
            resource.setResourceId(rs.getInt("id"));
            resource.setFirstName(rs.getString("first_name"));
            resource.setLastName(rs.getString("last_name"));
            resource.setPrefix(rs.getString("prefix"));
            return resource;
        };
    }

    public static RowMapper<ServiceVO> serviceVOMapper(String filterKeyWord, final int blockTimeInMins) {	
        return (rs, rowNum) -> {
            ServiceVO serviceVO = new ServiceVO();
            serviceVO.setServiceId(rs.getInt("id"));
            serviceVO.setServiceNameOnline(rs.getString("service_name_online"));
            if(!SERVICES_DROP_DOWN_DATA.getFilterKey().equals(filterKeyWord)) {
            	serviceVO.setDuration(rs.getInt("blocks") * blockTimeInMins);
            }
            
            if(SERVICES_BASIC_DATA.getFilterKey().equals(filterKeyWord)) {
            	 serviceVO.setEnable(rs.getString("enable"));
            	 serviceVO.setClosed(rs.getString("closed"));
            }
            
            if(SERVICE_COMPLETE_DATA.getFilterKey().equals(filterKeyWord)) {
            	String audioFilePath = null;
            	try {
            		audioFilePath = PropertyUtils.getValueFromProperties("IVR_AUDIO_FILE_PATH", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
            	} catch(Exception e) {
            		logger.error("Errro to read IVR_AUDIO_FILE_PATH property.");
            	}
				final String baseAudioFilePath = audioFilePath;
				serviceVO.setServiceNameIvrTts(rs.getString("service_name_ivr_tts") == null ? "" : rs.getString("service_name_ivr_tts"));
				String fileName = rs.getString("service_name_ivr_audio");
				if (!fileName.contains(baseAudioFilePath)) {
					serviceVO.setServiceNameIvrAudio(baseAudioFilePath + "/" + fileName);
				} else {
					serviceVO.setServiceNameIvrAudio((fileName != null && fileName.contains(".wav"))?fileName:fileName ==null?"":fileName+".wav");
				}
            	 
                // serviceVO.setServiceNameIvrAudio(baseAudioFilePath!=null?baseAudioFilePath+"/"+rs.getString("service_name_ivr_audio")+".wav":rs.getString("service_name_ivr_audio")+".wav");
                serviceVO.setCustomMsgTts(rs.getString("custom_msg_tts")==null?"":rs.getString("custom_msg_tts"));
                String customerMsgAudit = rs.getString("custom_msg_audio");
 				if (customerMsgAudit !=null && !customerMsgAudit.contains(baseAudioFilePath)) {
 					serviceVO.setCustomMsgAudio(baseAudioFilePath + "/" + fileName);
 				} else {
 					serviceVO.setCustomMsgAudio((customerMsgAudit != null && customerMsgAudit.contains(".wav"))?customerMsgAudit:customerMsgAudit == null?"":customerMsgAudit+".wav");
 				}
                 //serviceVO.setCustomMsgAudio(rs.getString("custom_msg_audio")!=null?baseAudioFilePath+"/"+rs.getString("custom_msg_audio")+".wav":rs.getString("custom_msg_audio"));
                 serviceVO.setBlocks(rs.getInt("blocks"));
                 serviceVO.setBuffer(rs.getInt("buffer"));
                 serviceVO.setMinCharge(rs.getFloat("min_charge"));
                 serviceVO.setPrice(rs.getFloat("price"));
                 serviceVO.setDeleteFlag(rs.getString("delete_flag"));
                 serviceVO.setEnable(rs.getString("enable"));
                 serviceVO.setAllowDuplicateAppt(rs.getString("allow_duplicate_appt"));
                 serviceVO.setSkipDateTIme(rs.getString("skip_date_time"));
                 serviceVO.setClosed(rs.getString("closed"));
                 serviceVO.setClosedMessage(rs.getString("closed_message")==null?"":rs.getString("closed_message"));
                 serviceVO.setClosedTts(rs.getString("closed_tts")==null?"":rs.getString("closed_tts"));
                 String closedAudit = rs.getString("closed_audio");
  				 if (closedAudit !=null && !closedAudit.contains(baseAudioFilePath)) {
  					serviceVO.setClosedAudio(baseAudioFilePath + "/" + fileName);
  				 } else {
  					serviceVO.setClosedAudio(closedAudit != null && closedAudit.contains(".wav")?closedAudit: closedAudit ==null?"":closedAudit+".wav");
  				 } 
                 //serviceVO.setClosedAudio(baseAudioFilePath!=null?baseAudioFilePath+"/"+rs.getString("closed_audio")+".wav":rs.getString("closed_audio")+".wav");
                 serviceVO.setIsSunOpen(rs.getString("is_sun_open"));
                 serviceVO.setIsMonOpen(rs.getString("is_mon_open"));
                 serviceVO.setIsTueOpen(rs.getString("is_tue_open"));
                 serviceVO.setIsWedOpen(rs.getString("is_wed_open"));
                 serviceVO.setIsThuOpen(rs.getString("is_thu_open"));
                 serviceVO.setIsFriOpen(rs.getString("is_fri_open"));
                 serviceVO.setIsSatOpen(rs.getString("is_sat_open"));
                 serviceVO.setClosedLocationIds(rs.getString("closed_location_ids")==null?"":rs.getString("closed_location_ids"));
            }
            return serviceVO;
        };
    }
    
    public static RowMapper<ServiceVO> resourceServiceVOMapper() {	
        return (rs, rowNum) -> {
            ServiceVO serviceVO = new ServiceVO();
            serviceVO.setServiceId(rs.getInt("service_id"));
            serviceVO.setServiceNameOnline(rs.getString("service_name_online"));
            serviceVO.setEnable(rs.getString("enable"));
            return serviceVO;
        };
    }
    
    

    public static RowMapper<Location> locationMapper(String filterKey) {	
        return (rs, i) -> {
            Location location = new Location();
            // used for all cases.
            location.setLocationId(rs.getInt("id"));
            location.setLocationNameOnline(rs.getString("location_name_online"));
            if(LOCATIONS_BASIC_DATA.getFilterKey().equals(filterKey)) {
            	location.setAddress(rs.getString("address"));
                location.setCity(rs.getString("city"));
                location.setState(rs.getString("state"));
                location.setZip(rs.getString("zip"));
                location.setEnable(rs.getString("enable"));
            	location.setClosed(rs.getString("closed"));
            	 location.setPlacement(rs.getInt("placement"));
            }
            if(LOCATIONS_HOME_PAGE_DATA.getFilterKey().equals(filterKey) || LOCATION_COMPLETE_DATA.getFilterKey().equals(filterKey)) {
            	location.setApptStartDate(getDateStringFromDate(rs.getDate("appt_start_date"), MM_DD_YYYY_DATE_FORMAT.getValue()));
            	location.setApptEndDate(getDateStringFromDate(rs.getDate("appt_end_date"), MM_DD_YYYY_DATE_FORMAT.getValue()));
            	location.setWorkPhone(rs.getString("work_phone"));
                location.setPlacement(rs.getInt("placement"));
                
                location.setAddress(rs.getString("address"));
                location.setCity(rs.getString("city"));
                location.setState(rs.getString("state"));
                location.setZip(rs.getString("zip"));
                location.setEnable(rs.getString("enable"));
            	location.setClosed(rs.getString("closed"));
                
                if(LOCATION_COMPLETE_DATA.getFilterKey().equals(filterKey)) {
                	String audioFilePath = null;
                	try {
                		audioFilePath = PropertyUtils.getValueFromProperties("IVR_AUDIO_FILE_PATH", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
                	} catch(Exception e) {
                		logger.error("Errro to read IVR_AUDIO_FILE_PATH property.");
                	}
                	final String baseAudioFilePath = audioFilePath;
                	location.setLocationNameMobile(rs.getString("location_name_mobile"));
                	location.setLocationNameSMS(rs.getString("location_name_sms"));
                	location.setLocationNameRemindSMS(rs.getString("location_name_remind_sms"));
                	location.setLocationNameIvrTts(rs.getString("location_name_ivr_tts"));
                	String fileName = rs.getString("location_name_ivr_audio");
                	if(fileName != null && !fileName.contains(baseAudioFilePath)) {
                		location.setLocationNameIvrAudio(baseAudioFilePath+"/"+fileName);	
                	} else {
                		location.setLocationNameIvrAudio(fileName!=null && fileName.contains(".wav")?fileName:fileName == null?"":fileName+".wav");
                	}
                	//location.setLocationNameIvrAudio(baseAudioFilePath!=null?baseAudioFilePath+"/"+fileName:fileName);
                	location.setLocationGoogleMap(rs.getString("location_google_map"));
                	location.setLocationGoogleMapLink(rs.getString("location_google_map_link"));
                	location.setTimeZone(rs.getString("time_zone"));
                	location.setComment(rs.getString("comments"));
                	location.setDeleteFlag(rs.getString("delete_flag"));
                	location.setEnable(rs.getString("enable"));
                	location.setClosed(rs.getString("closed"));
                	location.setClosedMessage(rs.getString("closed_message"));
                	location.setClosedTts(rs.getString("closed_tts"));
                	
                	 String closedAudit = rs.getString("closed_audio");
       				if (closedAudit!=null && closedAudit.contains(baseAudioFilePath)) {
       					location.setClosedAudio(baseAudioFilePath + "/" + fileName);
       				} else {
       					location.setClosedAudio(closedAudit !=null && closedAudit.contains(".wav")?closedAudit:closedAudit==null?"":closedAudit+".wav");
       				}
                	//location.setClosedAudio(baseAudioFilePath!=null?baseAudioFilePath+"/"+rs.getString("closed_audio")+".wav":rs.getString("closed_audio")+".wav");
                }
            }
            return location;
        };
    }
    
    public static RowMapper<ResourcePrefix> resourcePrefixMapper() {
        return (rs, i) -> {
            ResourcePrefix prefix = new ResourcePrefix();
            prefix.setOptionName(rs.getString("option_name"));
            prefix.setOptionValue(rs.getString("option_value"));           
            return prefix;
        };
    }
    
    public static RowMapper<DynamicFieldDisplay> dynamicFieldsDisplayMapper() {
        return (rs, i) -> {
        	DynamicFieldDisplay dynamicFieldDisplay = new DynamicFieldDisplay();
        	dynamicFieldDisplay.setColumnName(rs.getString("column_name"));
        	dynamicFieldDisplay.setDispay(rs.getString("display"));   
        	return dynamicFieldDisplay;
        };
    }
    
    public static RowMapper<ResourceTitle> resourceTitleMapper() {
        return (rs, i) -> {
            ResourceTitle title = new ResourceTitle();
            title.setOptionName(rs.getString("option_name"));
            title.setOptionValue(rs.getString("option_value"));           
            return title;
        };
    }
    
    public static RowMapper<ResourceType> resourceTypeMapper() {
        return (rs, i) -> {
            ResourceType type = new ResourceType();
            type.setOptionName(rs.getString("option_name"));
            type.setOptionValue(rs.getString("option_value"));           
            return type;
        };
    }
    
    public static RowMapper<DynamicPledgeResult> dynamicPledgeResultMapper() {	
        return (rs, rowNum) -> {
        	DynamicPledgeResult dynamicPledgeResult = new DynamicPledgeResult();
        	dynamicPledgeResult.setColumn(rs.getString("column"));
        	dynamicPledgeResult.setTitle(rs.getString("title"));
        	dynamicPledgeResult.setDisplayFor(rs.getString("display_for"));
        	dynamicPledgeResult.setEnable(rs.getString("enable"));
        	return dynamicPledgeResult;
        };
    }
    
    public static RowMapper<DynamicFieldLabelData> dynamicToolTipDataMapper() {	
        return (rs, rowNum) -> {
        	DynamicFieldLabelData toolTipData = new DynamicFieldLabelData();
        	toolTipData.setFieldName(rs.getString("field_name"));
        	toolTipData.setTitle(rs.getString("title"));
        	toolTipData.setDisplay(rs.getString("display"));
        	return toolTipData;
        };
    }
    
    public static RowMapper<JSPPagesPrivileges> privilegeSettingMapper() {
		return (rs, i) -> {
			JSPPagesPrivileges pagePrivilege = new JSPPagesPrivileges();
			pagePrivilege.setPrivilegeId(rs.getLong("id"));
			pagePrivilege.setGroupTitle(rs.getString("group_title"));
			pagePrivilege.setPagesTitle(rs.getString("pages_title"));
			pagePrivilege.setJspPages(rs.getString("jsp_pages"));
			pagePrivilege.setJspPageDesc(rs.getString("jsp_pages_description"));
			pagePrivilege.setEnableFlag(rs.getString("enable_flag"));
			return pagePrivilege;
		};
	}
    
	public static RowMapper<AppointmentStatusData> appointmentStatusMapper() {
		return (rs, i) -> {
			AppointmentStatusData apptStatusData = new AppointmentStatusData();
			apptStatusData.setId(rs.getInt("id"));
			apptStatusData.setStatus(rs.getString("status"));
			apptStatusData.setStatusVal(rs.getInt("status_val"));
			apptStatusData.setBlockedFromFutureAppts(rs.getString("block_from_future_appts"));
			apptStatusData.setDenied(rs.getString("denied"));
			apptStatusData.setReportDisplay(rs.getString("report_display"));
			return apptStatusData;
		};
	}
	
	

	public static RowMapper<ApptSysConfig> apptSysConfigMapper() {
		return (rs, rowNum) -> {
			ApptSysConfig apptSysConfig = new ApptSysConfig();
			apptSysConfig.setDisplayDepartment(rs.getString("display_department"));
			apptSysConfig.setDisplayCompany(rs.getString("display_company"));
			apptSysConfig.setDisplayLocation(rs.getString("display_location"));
			apptSysConfig.setDisplayService(rs.getString("display_service"));
			apptSysConfig.setAllowAnyResource(rs.getString("allow_any_resource"));
			apptSysConfig.setSchedulerClosed(rs.getString("scheduler_closed"));

			apptSysConfig.setRestrictApptWindow(rs.getString("restrict_appt_window"));
			apptSysConfig.setOneApptPerTerm(rs.getString("one_appt_per_term"));
			apptSysConfig.setTermStartDate(rs.getString("term_start_date"));
			apptSysConfig.setTermEndDate(rs.getString("term_end_date"));
			apptSysConfig.setNoApptPerTerm(rs.getString("no_appt_per_term"));
			
			apptSysConfig.setApptStartDate(rs.getString("appt_start_date"));
			apptSysConfig.setApptEndDate(rs.getString("appt_end_date"));
			apptSysConfig.setRestrictLocApptWindow(rs.getString("restrict_loc_appt_window"));
			apptSysConfig.setRestrictLocSerApptWindow(rs.getString("restrict_loc_ser_appt_window"));
			apptSysConfig.setFundingBasedScheduler(rs.getString("funding_based_scheduler"));
			apptSysConfig.setDefaultDayStartTime(rs.getString("default_day_start_time"));
			apptSysConfig.setDefaultDayEndTime(rs.getString("default_day_end_time"));
			apptSysConfig.setDefaultIsSunOpen(rs.getString("default_is_sun_open"));
			apptSysConfig.setDefaultIsMonOpen(rs.getString("default_is_mon_open"));
			apptSysConfig.setDefaultIsTueOpen(rs.getString("default_is_tue_open"));
			apptSysConfig.setDefaultIsWedOpen(rs.getString("default_is_wed_open"));
			apptSysConfig.setDefaultIsThuOpen(rs.getString("default_is_thu_open"));
			apptSysConfig.setDefaultIsFriOpen(rs.getString("default_is_fri_open"));
			apptSysConfig.setDefaultIsSatOpen(rs.getString("default_is_sat_open"));
			apptSysConfig.setCcConfirmEmails(rs.getString("cc_confirm_email"));
			apptSysConfig.setCcCancalEmails(rs.getString("cc_cancel_email"));
			apptSysConfig.setSendReschdEmail(rs.getString("send_reschd_email"));
			return apptSysConfig;
		};
	}
	
	public static RowMapper<Customer> customerMapper(boolean includeBlockedFlag) {
		return (rs, i) -> {
			Customer customer = new Customer();
			customer.setCustomerId(rs.getLong("id"));
			customer.setSsn(rs.getString("account_number"));
			customer.setFirstName(rs.getString("first_name"));
			customer.setLastName(rs.getString("last_name"));
			customer.setContactPhone(rs.getString("contact_phone"));
			customer.setEmail(rs.getString("email"));
			if(includeBlockedFlag) {
				customer.setBlockedFlag(rs.getString("blocked_flag"));
			} else {
				customer.setHouseHoldId(rs.getLong("household_id"));
				customer.setDob(rs.getString("dob"));
				customer.setAddress(rs.getString("address"));
				customer.setCity(rs.getString("city"));
				customer.setState(rs.getString("state"));
				customer.setZipCode(rs.getString("zipCode"));
				customer.setAttrib1(rs.getString("attrib1"));
			}
			return customer;
		};
	}
	
	public static RowMapper<DisplayNames> displayNamesMapper() {
		return (rs, rowNum) -> {
			DisplayNames displayNames = new DisplayNames();
			displayNames.setProcedureName(rs.getString("procedure_name"));
			displayNames.setProcedureSelect(rs.getString("procedure_select"));
			displayNames.setProceduresName(rs.getString("procedures_name"));

			displayNames.setLocationName(rs.getString("location_name"));
			displayNames.setLocationSelect(rs.getString("location_select"));
			displayNames.setLocationsName(rs.getString("locations_name"));

			displayNames.setDepartmentName(rs.getString("department_name"));
			displayNames.setDepartmentSelect(rs.getString("department_select"));
			displayNames.setDepartmentsName(rs.getString("departments_name"));

			displayNames.setServiceName(rs.getString("service_name"));
			displayNames.setServiceSelect(rs.getString("service_select"));
			displayNames.setServicesName(rs.getString("services_name"));

			displayNames.setCommentsName(rs.getString("comments_name"));

			displayNames.setResourceName(rs.getString("resource_name"));
			displayNames.setResourceSelect(rs.getString("resource_select"));
			displayNames.setResourcesName(rs.getString("resources_name"));

			displayNames.setCustomerName(rs.getString("customer_name"));
			displayNames.setCustomerSelect(rs.getString("customer_select"));
			displayNames.setCustomersName(rs.getString("customers_name"));
			return displayNames;
		};
	}
	
	public static RowMapper<IvrCalls> inBoundCallMapper() {
		return (rs, i) -> {
            IvrCalls ivrCalls = new IvrCalls();
            ivrCalls.setCustomerFirstName(rs.getString("customerFirstName"));
            ivrCalls.setCustomerLastName(rs.getString("customerLastName"));
            ivrCalls.setLocation(rs.getString("location_name_online"));
            ivrCalls.setResource(rs.getString("resourceName"));
            ivrCalls.setCallerId(rs.getString("caller_id"));
            ivrCalls.setHomePhone(rs.getString("contact_phone"));
            ivrCalls.setService(rs.getString("service_name_online"));
            ivrCalls.setSeconds(rs.getLong("seconds"));
            ivrCalls.setTransId(rs.getLong("trans_id"));
            return ivrCalls;
        };
	}
	
	public static RowMapper<OutBoundCalls> outBoundCallMapper() {
		return (rs, i) -> {
			OutBoundCalls outBoundCalls = new OutBoundCalls();
			//outBoundCalls.setTransId(rs.getLong("trans_id"));
			outBoundCalls.setAttemptId(rs.getInt("attempt_id"));
			outBoundCalls.setApptDateTime(rs.getString("apptDateTime"));
			outBoundCalls.setCallTime(rs.getString("callTime"));
			outBoundCalls.setCustomerFirstName(rs.getString("first_name"));
			outBoundCalls.setCustomerLastName(rs.getString("last_name"));
			outBoundCalls.setSeconds(rs.getLong("seconds"));
			outBoundCalls.setDailedPhone(rs.getString("phone"));
			outBoundCalls.setLocation(rs.getString("location_name_online"));
			outBoundCalls.setResource(rs.getString("resourceName"));
			outBoundCalls.setService(rs.getString("service_name_online"));
			return outBoundCalls;
		};
	}
	
	
	
	public static RowMapper<OutLookAppointment> outLookAppointmentMapper(Integer blockTimeInMins) {
		return (rs, i) -> {
			OutLookAppointment outlookAppt = new OutLookAppointment();
			outlookAppt.setConfNumber(rs.getLong("conf_number"));
			outlookAppt.setApptType(rs.getInt("appt_type"));
			outlookAppt.setDateTime(rs.getString("appt_date_time")==null?"":rs.getString("appt_date_time"));
			outlookAppt.setServiceName(rs.getString("service_name_online")==null?"":rs.getString("service_name_online"));
			outlookAppt.setDuration(rs.getInt("blocksPlusBuffer") * blockTimeInMins);
			outlookAppt.setLocationName(rs.getString("location_name_online")==null?"":rs.getString("location_name_online"));
			outlookAppt.setFirstName(rs.getString("cfn")==null?"":rs.getString("cfn"));
			outlookAppt.setLastName(rs.getString("cln")==null?"":rs.getString("cln"));
			outlookAppt.setAccountNumber(rs.getString("account_number")==null?"":rs.getString("account_number"));
			outlookAppt.setHomePhone(rs.getString("home_phone")==null?(rs.getString("contact_phone")==null?"":rs.getString("contact_phone")):rs.getString("home_phone"));
			outlookAppt.setResFirstName(rs.getString("resfn")==null?"":rs.getString("resfn"));
			outlookAppt.setResLastName(rs.getString("resln")==null?"":rs.getString("resln"));
			outlookAppt.setEmail(rs.getString("email")==null?"":rs.getString("email"));
			outlookAppt.setComments(rs.getString("comments")==null?"":rs.getString("comments"));
			return outlookAppt;
		};
	}
	

	public static RowMapper<Location> locationByServiceIdTOClosedServiceMapper() {
		return (rs, i) -> {
			Location location = new Location();
			location.setLocationId(rs.getInt("id"));
			location.setLocationNameOnline(rs.getString("location_name_online"));
			return location;
		};
	}
	
	public static RowMapper<DynamicIncludeReport> dynamicIncludeReportMapper() {
		return (rs, i) -> {
			DynamicIncludeReport dynamicIncludeReport = new DynamicIncludeReport();
			dynamicIncludeReport.setTableColumn(rs.getString("table_column"));
			dynamicIncludeReport.setCheckBoxValue(rs.getString("checkbox_status"));
			dynamicIncludeReport.setTitle(rs.getString("title"));
			return dynamicIncludeReport;
		};
	}
	
	public static RowMapper<AppointmentReportData> apptReportMapper() {
		return (rs, i) -> {
			AppointmentReportData appointmentReportData = new AppointmentReportData();
			appointmentReportData.setApptDateTime(rs.getString("apptDateTime"));
			appointmentReportData.setWalkIn(rs.getString("walkIn"));
			appointmentReportData.setResourceName(rs.getString("resourceName"));
			appointmentReportData.setLocationName(rs.getString("location_name_online"));
			appointmentReportData.setServiceName(rs.getString("service_name_online"));
			appointmentReportData.setSsn(rs.getString("account_number"));
			appointmentReportData.setFirstName(rs.getString("first_name"));
			appointmentReportData.setLastName(rs.getString("last_name"));
			appointmentReportData.setContactPhone(rs.getString("contact_phone"));
			appointmentReportData.setEmail(rs.getString("email"));
			appointmentReportData.setZipCode(rs.getString("zipCode"));
			appointmentReportData.setApptStatus(rs.getString("apptStatus"));
			appointmentReportData.setApptMethod(rs.getString("apptMethod"));
			appointmentReportData.setAccessed(rs.getString("accessed"));
			appointmentReportData.setConfirmNumber(rs.getLong("conf_number"));
			appointmentReportData.setAttrib1(rs.getString("attrib1"));
			appointmentReportData.setDepartmentName(rs.getString("department_name_online"));
			appointmentReportData.setComments(rs.getString("comments"));
			appointmentReportData.setPaymentAmount(rs.getString("payment_amt"));
			
			//Populating resource and appointment record time
			Timestamp resourceRecordStartTime = rs.getTimestamp("resource_record_start_time");
			Timestamp resourceRecordEndTime = rs.getTimestamp("resource_record_end_time");
			if (resourceRecordStartTime != null && resourceRecordEndTime != null) {				
				long diff = resourceRecordEndTime.getTime() - resourceRecordStartTime.getTime();
				float diffMinutes = diff / (60 * 1000);		
				if(diffMinutes>0){
					appointmentReportData.setResourceApptDuration(String.format("%.1f",diffMinutes));
				} else{
					appointmentReportData.setResourceApptDuration("0");
				}
			} else {	
				appointmentReportData.setResourceApptDuration("0");
			}
			
			//Populating Frontdesk record time
			Timestamp frontRecordStartTime = rs.getTimestamp("frontdesk_record_start_time");
			Timestamp frontRecordEndTime = rs.getTimestamp("frontdesk_record_end_time");
			if (frontRecordStartTime != null && frontRecordEndTime != null) {				
				long diff = frontRecordEndTime.getTime() - frontRecordStartTime.getTime();
				float diffMinutes = diff / (60 * 1000);		
				if(diffMinutes>0){
					appointmentReportData.setFrontDeskApptDuration(String.format("%.1f",diffMinutes));
				}else{
					appointmentReportData.setFrontDeskApptDuration("0");
				}
			}else{
				appointmentReportData.setFrontDeskApptDuration("0");
			}
			
			return appointmentReportData;
		};
	}
	
	public static RowMapper<SearchAppointmentData> apptResultMapper(String searchBy) {
		return (rs, i) -> {
			SearchAppointmentData searchApptData = new SearchAppointmentData();
			searchApptData.setCustomerId(rs.getLong("id"));
			searchApptData.setHouseHoldId(rs.getLong("household_id"));
			searchApptData.setSsn(rs.getString("account_number"));
			searchApptData.setFirstName(rs.getString("first_name"));
			searchApptData.setLastName(rs.getString("last_name"));
			searchApptData.setContactPhone(rs.getString("contact_phone"));
			searchApptData.setDob(rs.getString("dob"));
			searchApptData.setAddress(rs.getString("address"));
			searchApptData.setCity(rs.getString("city"));
			searchApptData.setState(rs.getString("state"));
			searchApptData.setZipCode(rs.getString("zipCode"));
			searchApptData.setAttrib1(rs.getString("attrib1"));
			
			if(searchBy.contains("INCLUDE_APPT_DATA")) {
				searchApptData.setApptDateTime(rs.getString("apptDateTime"));
				searchApptData.setLocationName(rs.getString("location_name_online"));
				searchApptData.setResourceName(rs.getString("resourceName"));
				searchApptData.setServiceName(rs.getString("service_name_online"));
				searchApptData.setApptStatus(rs.getString("apptStatus"));
				searchApptData.setApptMethod(rs.getString("apptMethod"));
				searchApptData.setConfirmNumber(rs.getLong("conf_number"));
			}
			return searchApptData;
		};
	}
	
	public static RowMapper<SearchAppointmentData> appointmentMapper() {
		return (rs, i) -> {
			SearchAppointmentData searchApptData = new SearchAppointmentData();
			searchApptData.setApptDateTime(rs.getString("apptDateTime"));
			searchApptData.setLocationName(rs.getString("location_name_online"));
			searchApptData.setResourceName(rs.getString("resourceName"));
			searchApptData.setServiceName(rs.getString("service_name_online"));
			searchApptData.setApptStatus(rs.getString("apptStatus"));
			searchApptData.setApptMethod(rs.getString("apptMethod"));
			searchApptData.setConfirmNumber(rs.getLong("conf_number"));
			searchApptData.setSchedulerId(rs.getLong("scheduleId"));
			searchApptData.setApptTimeStamp(rs.getString("apptTimeStamp"));
			searchApptData.setFutureAppt("Y".equals(rs.getString("isFutureAppt"))?true:false);
			return searchApptData;
		};
	}

	
	public static RowMapper<CustomerActivity> customerActivityMapper() {
		return (rs, i) -> {
			CustomerActivity customerActivity = new CustomerActivity();
			customerActivity.setCustomerId(rs.getLong("id"));
			customerActivity.setTimestamp(rs.getString("timestamp"));
			customerActivity.setHouseHoldId(rs.getLong("household_id"));
			customerActivity.setSsn(rs.getString("account_number"));
			customerActivity.setFirstName(rs.getString("first_name"));
			customerActivity.setLastName(rs.getString("last_name"));
			customerActivity.setContactPhone(rs.getString("contact_phone"));
			customerActivity.setDob(rs.getString("dob"));
			customerActivity.setAddress(rs.getString("address"));
			customerActivity.setCity(rs.getString("city"));
			customerActivity.setState(rs.getString("state"));
			customerActivity.setZipCode(rs.getString("zipCode"));
			customerActivity.setAttrib1(rs.getString("attrib1"));
			customerActivity.setUserName(rs.getString("username"));
			customerActivity.setCallerId(rs.getString("caller_id"));
			customerActivity.setUpdatedBy(rs.getString("updated_by"));
			customerActivity.setIpAddress(rs.getString("ip_address"));
			customerActivity.setApptDateTime(rs.getString("apptDateTime"));
			customerActivity.setLocationName(rs.getString("location_name_online"));
			customerActivity.setResourceName(rs.getString("resourceName"));
			customerActivity.setServiceName(rs.getString("service_name_online"));
			customerActivity.setApptStatus(rs.getString("apptStatus"));
			customerActivity.setApptMethod(rs.getString("apptMethod"));
			customerActivity.setConfirmNumber(rs.getLong("conf_number"));
			customerActivity.setDevice(rs.getString("device"));
			customerActivity.setUuid(rs.getString("uuid"));
			customerActivity.setComments(rs.getString("comments"));
			customerActivity.setNotes(rs.getString("notes"));
			customerActivity.setScreened(rs.getString("screened"));
			return customerActivity;
		};
	}
    
    public static RowMapper<PledgeDetails> pledgeDetailsMapper() throws IllegalAccessException, InvocationTargetException {
		return (rs, num) -> {
			 PledgeDetails pledgeDetails = new PledgeDetails();
	                pledgeDetails.setCustomerId(rs.getLong("id"));
	                pledgeDetails.setHouseHoldId(rs.getLong("household_id"));
	                pledgeDetails.setAccountNumber(rs.getString("account_number"));
	                pledgeDetails.setFirstName(rs.getString("first_name"));
	                pledgeDetails.setLastName(rs.getString("last_name"));
	                pledgeDetails.setAddress(rs.getString("address"));
	                pledgeDetails.setCity(rs.getString("city"));
	                pledgeDetails.setState(rs.getString("state"));
	                pledgeDetails.setZipCode(rs.getString("zip_postal"));
	                pledgeDetails.setFundName(rs.getString("fund_name"));
					String vendorId = rs.getString("vendorIds");
					if (vendorId != null && vendorId instanceof String) {
						String[] vendors = vendorId.toString().split(",");
						for (int i = 0; i < vendors.length; i++) {
							try {
								BeanUtils.copyProperty(pledgeDetails, "vendorId"+ (i+1), vendors[i]);
							} catch (Exception e) {
								logger.error("Error in vendorId populate!!!"); 
							}
							
						}
					} 

					String vendorName =  rs.getString("vendorNames");
					if (vendorName != null && vendorName instanceof String) {
						String[] vendors = vendorName.toString().split(",");
						for (int i = 0; i < vendors.length; i++) {
							try {
								BeanUtils.copyProperty(pledgeDetails, "vendorName"+ (i+1), vendors[i]);
							} catch (Exception e) {
								logger.error("Error in vendorName populate!!!"); 
							}
						}
					}

					String vendorPledgeAmount = rs.getString("vendorPAmounts");
					if (vendorPledgeAmount != null && vendorPledgeAmount instanceof String) {
						String[] pledgeAmountArr = vendorPledgeAmount.toString().split(",");
						for (int i = 0; i < pledgeAmountArr.length; i++) {
							if (pledgeAmountArr[i] != null && pledgeAmountArr[i].trim().length() > 0) {
								try {
									BeanUtils.copyProperty(pledgeDetails, "vendor"+ (i+1)+"PledgeAmount", String.format("%.2f", new BigDecimal(pledgeAmountArr[i])));
								} catch (Exception e) {
									logger.error("Error in vendorAmount populate!!!"); 
								}
							}
						}
					}
					pledgeDetails.setPledgeDatetime(rs.getString("pledge_datetime"));
	                pledgeDetails.setApptDateTime(rs.getString("appt_date_time"));
	                pledgeDetails.setLocationNameOnline(rs.getString("location_name_online"));
	                pledgeDetails.setResourceNameOnline(rs.getString("resource_name"));
	                pledgeDetails.setServiceNameOnline(rs.getString("service_name_online"));
	                pledgeDetails.setPledgeTotalAmount(String.format("%.2f", rs.getDouble("total_amount")));
	                pledgeDetails.setCustPledgeStatus(rs.getString("status"));
					String vendorAccount =  rs.getString("vendorAccountNumbers");
					if (vendorAccount != null && vendorAccount instanceof String) {
						String[] accounts = vendorAccount.toString().split(",");
						if (accounts.length>0) {
							for (int i = 0; i < accounts.length; i++) {
								try {
									BeanUtils.copyProperty(pledgeDetails, "vendor"+ (i+1)+"AccountNumber", accounts[i]);
								} catch (Exception e) {
									logger.error("Error in vendorAccountNumber populate!!!"); 
								}
							}
						}
					}
					pledgeDetails.setUpdatedStatus(rs.getString("urgent_status"));
					pledgeDetails.setUpdatedStatus(rs.getString("updated_status"));
					pledgeDetails.setCalledinStatus(rs.getString("calledin_status"));
					pledgeDetails.setPrimaryStatus(rs.getString("primary_status"));
					pledgeDetails.setSecondaryStatus(rs.getString("secondary_status"));
					return pledgeDetails;
		};
	}
}
