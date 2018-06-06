package com.telappoint.admin.appt.common.dao.impl;

import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2004;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2005;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2006;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2010;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2014;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2015;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2016;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2017;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2018;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2020;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2021;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2022;
import static com.telappoint.admin.appt.common.constants.ErrorConstants.ERROR_2023;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.appointmentMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.appointmentStatusMapper;

import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.apptReportMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.apptResultMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.apptSysConfigMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.customerActivityMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.customerMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.displayNamesMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.dynamicFieldsDisplayMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.dynamicIncludeReportMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.dynamicPledgeResultMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.dynamicToolTipDataMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.inBoundCallMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.locationByServiceIdTOClosedServiceMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.locationMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.outBoundCallMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.outLookAppointmentMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.pledgeDetailsMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.privilegeSettingMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.resourceMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.resourceMapperById;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.resourcePrefixMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.resourceServiceVOMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.resourceTitleMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.resourceTypeMapper;
import static com.telappoint.admin.appt.common.dao.impl.ResultSetMapperHelper.serviceVOMapper;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.telappoint.admin.appt.common.model.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.telappoint.admin.appt.common.constants.CommonDateContants;
import com.telappoint.admin.appt.common.constants.ErrorConstants;
import com.telappoint.admin.appt.common.constants.SPConstants;
import com.telappoint.admin.appt.common.dao.AdminDAO;
import com.telappoint.admin.appt.common.model.request.ConfirmAppointmentRequest;
import com.telappoint.admin.appt.common.model.request.CustomerPledgeRequest;
import com.telappoint.admin.appt.common.model.request.CustomerRequest;
import com.telappoint.admin.appt.common.model.request.OutlookSyncReq;
import com.telappoint.admin.appt.common.model.request.ResourceHoursRequest;
import com.telappoint.admin.appt.common.model.request.ResourceWorkingHoursRequest;
import com.telappoint.admin.appt.common.model.response.ApptStatusResponse;
import com.telappoint.admin.appt.common.model.response.CancelAppointResponse;
import com.telappoint.admin.appt.common.model.response.ConfirmAppointmentResponse;
import com.telappoint.admin.appt.common.model.response.CustomerPledgeResponse;
import com.telappoint.admin.appt.common.model.response.DailyCalendarResponse;
import com.telappoint.admin.appt.common.model.response.RecordTimeResponse;
import com.telappoint.admin.appt.common.model.response.ResourceWorkingHrsResponse;
import com.telappoint.admin.appt.common.model.response.StackedChartResponse;
import com.telappoint.admin.appt.common.model.response.SummaryReportResponse;
import com.telappoint.admin.appt.common.model.response.TablePrintViewResponse;
import com.telappoint.admin.appt.common.model.response.WeeklyCalendarResponse;
import com.telappoint.admin.appt.common.util.CoreUtils;
import com.telappoint.admin.appt.common.util.DateUtils;
import com.telappoint.admin.appt.handlers.exception.TelAppointException;

/**
 * 
 * @author Balaji N
 *
 */
@Repository
public class AdminDAOImpl implements AdminDAO {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Override
    public List<Customer> getCustomerList(JdbcCustomTemplate jdbcCustomTemplate,String customerName) throws Exception {
        customerName = (String) com.telappoint.admin.appt.common.util.CoreUtils.getInitCaseValue(customerName);
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id, CONCAT(c.last_name,' ', c.first_name) as customerData from customer c where 1=1 ");
        if (!StringUtils.isEmpty(customerName)) {
            sql.append(" and (c.last_name LIKE (:customerLastName) ");
            sql.append(" or c.first_name like (:customerFirstName))");
        }
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("customerLastName", customerName+"%");
        paramSource.addValue("customerFirstName", "%"+customerName+"%");
        return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, customerAutoSuggestMapper());  
    }

	private RowMapper<Customer> customerAutoSuggestMapper() {
		return (rs, num) -> {
			Customer customer = new Customer();
			customer.setCustomerId(rs.getLong("id"));
			customer.setName(rs.getString("customerData"));
			return customer;
		};
	}
	
	@Override
	public boolean updateCustomerIdInSchedule(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long customerId, Long scheduleId) throws Exception {
		String sql = "update schedule set customer_id=? where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{customerId, scheduleId})!=0;
	}

	@Override
	public HoldAppt holdAppointment(JdbcCustomTemplate jdbcCustomTemplate, String device, Long locationId, Long resourceId, Long procedureId, Long departmentId,
			Long serviceId, Long customerId, String apptDateTime, ClientDeploymentConfig cdConfig, Long transId) throws Exception {
		Map<String, Object> inParameters = new HashMap<String, Object>();
		try {
			String spName = "hold_appointment_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);

			inParameters.put(SPConstants.APPT_DATE_TIME.getValue(), apptDateTime);
			inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
			inParameters.put(SPConstants.RESOURCE_ID.getValue(), resourceId);
			inParameters.put(SPConstants.PROCEDURE_ID.getValue(), procedureId);
			inParameters.put(SPConstants.DEPARTMENT_ID.getValue(), departmentId);
			inParameters.put(SPConstants.SERVICE_ID.getValue(), serviceId);
			inParameters.put(SPConstants.CUSTOMER_ID.getValue(), customerId);
			inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());
			inParameters.put(SPConstants.TRANS_ID.getValue(), transId);
			inParameters.put(SPConstants.DEVICE.getValue(), device);

			logger.info("holdAppointmentCallCenter input params: " + inParameters);
			long startTime = System.currentTimeMillis();
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
			long endTime = System.currentTimeMillis();
			logTimeTaken(spName, startTime, endTime);

			Object schedule_id = simpleJdbcCallResult.get(SPConstants.RETURN_SCHEDULE_ID.getValue());
			Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
			Object display_datetime = simpleJdbcCallResult.get(SPConstants.DISPLAY_DATETIME.getValue());
			
			if (schedule_id != null && display_datetime != null) {
				return new HoldAppt(Long.parseLong(schedule_id.toString()), display_datetime.toString(), errorMsg == null ? null : errorMsg.toString());
			} else {
				return new HoldAppt(null, null, errorMsg == null ? null : errorMsg.toString(), false);
			}
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2029.getCode(), ErrorConstants.ERROR_2029.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(),
					"holdAppointmentCallCenter input params: " + inParameters);
		}
	}
	
	private List<LoginPageFields> getLoginPageFields(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device) {
		StringBuilder sql = new StringBuilder("select distinct param_column from customer_registration");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<LoginPageFields>>() {
            final List<LoginPageFields> loginPageFieldList = new ArrayList<>();
            @Override
            public final List<LoginPageFields> extractData(ResultSet rs) throws SQLException, DataAccessException {
            	LoginPageFields loginPageFields = null;
                while (rs.next()) {
                	loginPageFields = new LoginPageFields();
                	String paramColumn = rs.getString("param_column");
                	loginPageFields.setParamColumn(paramColumn);
                	loginPageFields.setParamColumnForQuery("c."+paramColumn);
                	if("contact_phone".equals(paramColumn)) {
                		paramColumn = "IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone";
                		loginPageFields.setParamColumnForQuery(paramColumn);
                	}

                	if("home_phone".equals(paramColumn)) {
                		paramColumn = "IF(c.home_phone IS NOT NULL, CONCAT(LEFT(c.home_phone,3),'-',MID(c.home_phone,4,3),'-',RIGHT(c.home_phone,4)),'') as home_phone";
                		loginPageFields.setParamColumnForQuery(paramColumn);
                	}

                	loginPageFields.setJavaRef(getJavaField(paramColumn));
                	loginPageFieldList.add(loginPageFields);
                }
                return loginPageFieldList;
            }
        });
 	}


	@Override
	public VerifyPageData getVerfiyPageData(JdbcCustomTemplate jdbcCustomTemplate, String device, String langCode, Long scheduleId,Map<String, String> aliasMap) throws Exception {
		StringBuilder sql = new StringBuilder();
		List<LoginPageFields> loginPageFields = getLoginPageFields(jdbcCustomTemplate, logger, device);
		List<String> paramColumns = loginPageFields.stream().distinct().map(LoginPageFields::getParamColumnForQuery).collect(Collectors.toList());

		sql.append(" select DATE_FORMAT(sc.appt_date_time,'%m/%d/%Y %h:%i %p') as appt_date_time, concat(r.prefix,' ', r.first_name,' ', r.last_name) as resourceName ");
		sql.append(",s.service_name_online, l.location_name_online, lb.display_text, IF(p.procedure_name_online IS NULL,'',p.procedure_name_online) as procedure_name_online");
		sql.append(",IF(de.department_name_online IS NULL,'',de.department_name_online) as department_name_online, l.time_zone, IF(sc.comments IS NOT NULL,sc.comments,'') as comments");
		if(!loginPageFields.isEmpty()) {
			sql.append(","+StringUtils.join(paramColumns, ","));
		}
		sql.append(" from schedule sc");
		sql.append(" left outer join customer c on c.id=sc.customer_id");
		sql.append(" left outer join service s on s.id=sc.service_id");
		sql.append(" left outer join resource r on r.id=sc.resource_id");
		sql.append(" left outer join location l on l.id=sc.location_id");
		sql.append(" left outer join `procedure` p on p.id=sc.procedure_id");
		sql.append(" left outer join department de on de.id=sc.department_id");
		sql.append(" left outer join list_of_things_bring lb on lb.service_id=sc.service_id" );
		sql.append(" where sc.id=:scheduleId and lb.lang=:langCode");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue("scheduleId", scheduleId);
		paramSource.addValue("langCode", langCode);

		List<VerifyPageData> verifyPageData = jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, verifyPageMapper(logger,loginPageFields, aliasMap));
		if(!verifyPageData.isEmpty()) {
			return verifyPageData.get(0);
		}
		return null;
	}

	private RowMapper<VerifyPageData> verifyPageMapper(Logger logger, List<LoginPageFields> loginPageFields, Map<String,String> aliasMap) throws Exception {
		return (rs, num) -> {
			VerifyPageData verifyPageData = new VerifyPageData();
			verifyPageData.setCustomerId(null);
			verifyPageData.setApptDateTime(rs.getString("appt_date_time"));
			verifyPageData.setResourceName(rs.getString("resourceName"));
			//verifyPageData.setContactPhone(rs.getString("contact_phone"));
			//verifyPageData.setAccountNumber(rs.getString("account_number"));
			verifyPageData.setComments(rs.getString("comments")==null?"":rs.getString("comments"));
			 if (aliasMap != null) {
				 verifyPageData.setLocationName(aliasMap.get(rs.getString("location_name_online")) ==null?rs.getString("location_name_online"):aliasMap.get(rs.getString("location_name_online")));
				 verifyPageData.setServiceName(aliasMap.get(rs.getString("service_name_online"))==null?rs.getString("service_name_online"):aliasMap.get(rs.getString("service_name_online")));
				 verifyPageData.setProcedureName(aliasMap.get(rs.getString("procedure_name_online"))==null?rs.getString("procedure_name_online"):aliasMap.get(rs.getString("procedure_name_online")));
				 verifyPageData.setDepartmentName(aliasMap.get(rs.getString("department_name_online"))==null?rs.getString("department_name_online"):aliasMap.get(rs.getString("department_name_online")));
				 verifyPageData.setTimeZone(aliasMap.get(rs.getString("time_zone"))==null?rs.getString("time_zone"):aliasMap.get(rs.getString("time_zone")));
             } else {
            	 verifyPageData.setServiceName(rs.getString("service_name_online"));
     			 verifyPageData.setLocationName(rs.getString("location_name_online"));
     			 verifyPageData.setProcedureName(rs.getString("procedure_name_online"));
     			 verifyPageData.setDepartmentName(rs.getString("department_name_online"));
     			 verifyPageData.setTimeZone(rs.getString("time_zone"));
             }

			verifyPageData.setListOfDocsToBring(rs.getString("display_text"));
			for(LoginPageFields loginPageField : loginPageFields) {
				try {
					CoreUtils.setPropertyValue(verifyPageData, loginPageField.getJavaRef(), rs.getString(loginPageField.getParamColumn()));
				} catch (Exception e) {
					logger.error("Customer data population failed!.");
				}
			}
			return verifyPageData;
		};
	}

	 @Override
	    public void getI18nPageContentMap(JdbcCustomTemplate jdbcCustomTemplate,final Map<String, Map<String, String>> map) throws TelAppointException, Exception {
	        String sql = "select device,lang,message_key,message_value from i18n_display_page_content";
	        populateMap(jdbcCustomTemplate, logger, sql, map);
	    }
	
	@Override
    public void getI18nDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception {
        String sql = "select device,lang,message_key,message_value from i18n_display_field_labels";
        populateMap(jdbcCustomTemplate, logger, sql, map);
    }
	 
	 private void populateMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String sql, final Map<String, Map<String, String>> map) throws TelAppointException, Exception {
	        try {
	            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Map<String, String>>>() {
	                @Override
	                public Map<String, Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
	                    Map<String, String> subMap;
	                    StringBuilder key = null;
	                    while (rs.next()) {
	                    	key = new StringBuilder();
	                        key.append(rs.getString("device")).append("|").append(rs.getString("lang"));
	                        if (map.containsKey(key.toString())) {
	                            subMap = map.get(key.toString());
	                            subMap.put(rs.getString("message_key"), rs.getString("message_value"));
	                        } else {
	                            subMap = new HashMap<String, String>();
	                            subMap.put(rs.getString("message_key"), rs.getString("message_value"));
	                            map.put(key.toString(), subMap);
	                        }
	                    }
	                    return map;
	                }
	            });
	        } catch (DataAccessException dae) {
	            throw new TelAppointException(ErrorConstants.ERROR_2028.getCode(), ErrorConstants.ERROR_2028.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "");
	        }
	    }
	
	@Override
	public List<CustomerRegistration> getCustomerRegistrationList(JdbcCustomTemplate jdbcCustomTemplate,String langCode, List<String> loginTypes, final Map<String,String> labelMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from customer_registration rg where rg.device_type ='admin'");
		sql.append(" and rg.login_type in (:loginTypes)");
		sql.append(" order by rg.placement asc");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("loginTypes", loginTypes);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, customerRegMapper(labelMap));
	}
	
	private RowMapper<CustomerRegistration> customerRegMapper(final Map<String,String> labelMap) {
		return (rs, num) -> {
			CustomerRegistration customerReg = new CustomerRegistration();
			customerReg.setCustomerRegId(rs.getInt("id"));
			customerReg.setParamTable(rs.getString("param_table"));
			customerReg.setParamColumn(rs.getString("param_column"));
			customerReg.setLoginType(rs.getString("login_type"));
			customerReg.setParamType(rs.getString("param_type"));
			customerReg.setDisplayType(rs.getString("display_type"));
			
			customerReg.setDisplayTitle(labelMap == null?rs.getString("display_title"):labelMap.get(rs.getString("display_title")));
			customerReg.setDisplaySize(rs.getInt("display_size"));
			customerReg.setMaxChars(rs.getInt("max_chars"));
			customerReg.setTextareaRows(rs.getInt("textarea_rows"));
			customerReg.setTextareaCols(rs.getInt("textarea_cols"));
			customerReg.setEmptyMessageValue(labelMap == null?rs.getString("empty_error_msg"):labelMap.get(rs.getString("empty_error_msg")));
			customerReg.setInvalidErrorMsg(labelMap == null?rs.getString("invalid_error_msg"):labelMap.get(rs.getString("invalid_error_msg")));
			customerReg.setValidateRequired(rs.getString("validate_required"));
			customerReg.setValidationRules(rs.getString("validation_rules")); 
			customerReg.setValidateMaxChars(rs.getInt("validate_max_chars"));
			customerReg.setValidateMinValue(rs.getInt("validate_min_value"));  
			customerReg.setValidateMinValue(rs.getInt("validate_max_value"));    
			customerReg.setListLabels(rs.getString("list_labels"));                                                                                 
			customerReg.setListValues(rs.getString("list_values"));   
			customerReg.setListInitialValues(rs.getString("list_initial_values"));
			customerReg.setRequired(rs.getString("required"));
			return customerReg;
		};
	}

	@Override
	public List<Location> getLocationList(JdbcCustomTemplate jdbcCustomTemplate, String filterKeyWord, boolean isActiveList) throws TelAppointException {
		StringBuilder sql = new StringBuilder("select * from location");
		sql.append(" where delete_flag ='N' ");
		if (isActiveList) {
			sql.append(" and `enable` ='Y'");
		}
		sql.append(" order by placement asc");
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), locationMapper(filterKeyWord));
		} catch (Exception dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2019.getCode(), ErrorConstants.ERROR_2019.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	

	@Override
	public Location getLocationById(JdbcCustomTemplate jdbcCustomTemplate, String filterKeyWord, Integer locationId) throws TelAppointException {
		StringBuilder sql = new StringBuilder("select * from location where id = ?");
		try {
			return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql.toString(), new Object[] { locationId }, locationMapper(filterKeyWord));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2024.getCode(), ErrorConstants.ERROR_2024.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	// it will used for location permission or normal ways
	@Override
	public List<Location> getLocationListByLocationIds(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> locationIds, String filterKeyWord, boolean isActiveList) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from location where delete_flag ='N' and id in (:locationIds) order by placement asc");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("locationIds", locationIds);
		try {
			return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, locationMapper(filterKeyWord));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2014.getCode(), ERROR_2014.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "locationIds: " + locationIds);
		}
	}

	// locationList by provider permission or normal ways
	@Override
	public List<Location> getLocationListByResourceIds(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String filterKeyWord, boolean isActiveList) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select l.* from location l, (select distinct(r.location_id) from resource r where r.id in (:resourceIds) and r.delete_flag ='N'");
		sql.append(" and (r.duplicate_primary_resource_id IS NULL OR r.duplicate_primary_resource_id=0)) as location");
		sql.append("  where l.id = location.location_id order by l.location_name_online,l.placement asc ");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceIds", resourceIds);
		try {
			return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, locationMapper(filterKeyWord));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2015.getCode(), ERROR_2015.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "resourceIds: " + resourceIds);
		}
	}

	@Override
	public List<Resource> getResourceList(JdbcCustomTemplate jdbcCustomTemplate, String filterKeyWord, boolean isActiveList) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		//why isActiveList is needed?
		sql.append("select r.*,l.location_name_online,group_concat(distinct s.service_name_online) as serviceNames from resource r");
		sql.append(" LEFT OUTER JOIN resource_service rs on r.id=rs.resource_id LEFT OUTER JOIN service s on rs.service_id = s.id");
		sql.append(" LEFT OUTER JOIN location l on r.location_id=l.id where r.delete_flag ='N' and r.enable ='Y'");
		sql.append(" and (r.duplicate_primary_resource_id IS NULL or r.duplicate_primary_resource_id=0) and rs.`enable` = 'Y'");
		sql.append(" and s.delete_flag = 'N' and s.`enable` = 'Y' group by resource_id order by l.placement, r.placement asc");
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), resourceMapper(filterKeyWord));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2020.getCode(), ERROR_2020.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public List<Resource> getResourceListByIds(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String filterKeyWord, boolean isActiveList) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from resource");
		sql.append(" where delete_flag ='N' ");
		sql.append(" id in (:resourceIds) ");
		if (isActiveList) {
			sql.append(" and enable ='Y'");
		}
		sql.append(" and (duplicate_primary_resource_id IS NULL");
		sql.append(" or duplicate_primary_resource_id=0)");
		sql.append(" order by placement asc");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceIds", resourceIds);
		try {
			return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, resourceMapper(filterKeyWord));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2020.getCode(), ERROR_2020.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	@Override
	public Resource getResourceById(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, String filterKeyWord, boolean isActiveList) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select r.*,l.location_name_online from resource r, location l");
		sql.append(" where r.id = :resourceId ");
		//sql.append(" and r.delete_flag ='N' ");
		if (isActiveList) {
			sql.append(" and r.enable ='Y'");
		}
		sql.append(" and (r.duplicate_primary_resource_id IS NULL");
		sql.append(" or r.duplicate_primary_resource_id=0) and l.id=r.location_id");
		sql.append(" order by r.placement asc");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceId", resourceId);
		try {
			Resource resource = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, resourceMapper(filterKeyWord));
			resource.setSelectedServiceIds(getResourceServiceEnableServiceIds(jdbcCustomTemplate, resourceId));
			return resource;
		}  catch (EmptyResultDataAccessException dae) {
			logger.error("No data for resourceId: "+resourceId);
			return null;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2020.getCode(), ERROR_2020.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	@Override
	public Resource getResourceById(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, boolean isActiveList) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from resource r");
		sql.append(" where id = :resourceId ");
		sql.append(" and r.delete_flag ='N' ");
		if (isActiveList) {
			sql.append(" and r.enable ='Y'");
		}
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceId", resourceId);
		try {
			return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, resourceMapperById());
		}  catch (EmptyResultDataAccessException dae) {
			logger.error("No data for resourceId: "+resourceId);
			return null;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2020.getCode(), ERROR_2020.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	private String getResourceServiceEnableServiceIds(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId) {
		StringBuilder sql = new StringBuilder("select group_concat(distinct rs.service_id) as serviceIds from resource_service rs, service s");
		sql.append(" where rs.resource_id=:resourceId and rs.service_id=s.id");
		sql.append(" and s.delete_flag='N' and s.enable='Y' and rs.enable='Y'");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceId", resourceId);
		try {
			return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class);
		} catch (EmptyResultDataAccessException dae) {
			logger.error("No data for resourceId: "+resourceId);
			return "";
		}
	}

	//not used
	private List<ServiceVO> getResouceService(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId) {
		StringBuilder sql = new StringBuilder("select rs.service_id, rs.enable,s.service_name_online from resource_service rs, service s");
		sql.append(" where rs.resource_id=:resourceId and rs.service_id=s.id");
		sql.append(" and s.delete_flag='N' and s.enable='Y' and rs.enable='Y'");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceId", resourceId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, resourceServiceVOMapper());
	}

	/**
	 * Below query used to check duplicate primary resource logic.. so don't add
	 * esource.duplicate_primary_resource_id IS NULL") or
	 * resource.duplicate_primary_resource_id=0 in below query.
	 */
	@Override
	public List<Resource> getResourcesByLocationId(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, String filterKeyWord, boolean isActiveList) throws TelAppointException {
		try {
			StringBuilder sql = new StringBuilder();
			Object obj[]=null;
			sql.append(" select * from resource ");
			if(locationId > 0) {
				sql.append(" where location_id = ? ");
				obj = new Object[] { locationId };
			} else {
				sql.append(" where 1=1 ");
			}
			sql.append(" and delete_flag ='N'");
			if(isActiveList) {
				sql.append(" and `enable`='Y'");
			}
			sql.append(" order by placement asc");
			
			
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), obj , new ResultSetExtractor<List<Resource>>() {
				@Override
				public List<Resource> extractData(ResultSet rs) throws SQLException, DataAccessException {
					final List<Resource> resources = new ArrayList<Resource>();
					Resource resource;
					while (rs.next()) {
						resource = new Resource();
						resource.setResourceId(rs.getInt("id"));
						resource.setPrefix(rs.getString("prefix"));
						resource.setFirstName(rs.getString("first_name"));
						resource.setLastName(rs.getString("last_name"));
						resource.setTitle(rs.getString("title"));
						resources.add(resource);
					}
					return resources;
				}
			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2007.getCode(), ErrorConstants.ERROR_2007.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public List<ServiceVO> getServiceList(JdbcCustomTemplate jdbcCustomTemplate, int blockTimeInMins, String filterKeyWord,  boolean isActiveList) throws TelAppointException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select * from service ");
			sql.append(" where delete_flag ='N'");
			if (isActiveList) {
				sql.append(" and enable ='Y'");
			}
			sql.append(" order by placement asc");
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), serviceVOMapper(filterKeyWord, blockTimeInMins));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2021.getCode(), ERROR_2021.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	@Override
	public List<ServiceVO> getServiceDropDownList(JdbcCustomTemplate jdbcCustomTemplate, String filterKeyWord, int blockTimeInMins, Integer locationId, Integer serviceId,  boolean isActiveList) throws TelAppointException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select * from service ");
			sql.append(" where delete_flag ='N'");
			if (isActiveList) {
				sql.append(" and enable ='Y'");
			}
			sql.append(" order by placement asc");
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), serviceVOMapper(filterKeyWord, blockTimeInMins));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2021.getCode(), ERROR_2021.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public List<ServiceVO> getServiceListByIds(JdbcCustomTemplate jdbcCustomTemplate, int blockTimeInMins, List<Integer> serviceIds,String filterKeyWord, boolean isActiveList) throws TelAppointException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select * from service where delete_flag ='N'");
			sql.append(" and id in (:serviceIds)");
			sql.append(" order by placement asc");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("serviceIds", serviceIds);
			return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, serviceVOMapper(filterKeyWord, blockTimeInMins));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2018.getCode(), ERROR_2018.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	@Override
	public ServiceVO getServiceById(JdbcCustomTemplate jdbcCustomTemplate, Integer serviceId, int blockTimeInMins, String filterKeyWord, boolean isIncludingSuspended, boolean isActiveList) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select * from service where 1=1");
			if(!isIncludingSuspended) {
				sql.append(" and delete_flag ='N'");
			} 	
			if(isActiveList) {
				sql.append(" and `enable` = 'Y'");
			}
			sql.append(" and id = :serviceId");
			sql.append(" order by placement asc");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("serviceId", serviceId);
			List<ServiceVO> serviceVOList =  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, serviceVOMapper(filterKeyWord, blockTimeInMins));
			if(serviceVOList != null && !serviceVOList.isEmpty()) {
				return serviceVOList.get(0);
			}
			return null;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2022.getCode(), ERROR_2022.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	@Override
    public String getResourceTimeSlots(JdbcCustomTemplate jdbcCustomTemplate,Integer resourceId, String startDateTime, String endDateTime) throws Exception {
       StringBuilder sql = new StringBuilder("select CONCAT(rc.date_time,'|',rc.schedule_id) as dateTimeScheduleId from resource_calendar rc");
       sql.append(" where rc.resource_id in (:resourceId) and rc.date_time >= :startDateTime and rc.date_time <= :endDateTime order by rc.date_time asc");
       MapSqlParameterSource paramSource = new MapSqlParameterSource();
	   paramSource.addValue("resourceId", resourceId);
	   paramSource.addValue("startDateTime", startDateTime);
	   paramSource.addValue("endDateTime", endDateTime);
	   return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class); 
    }
    
    
    @Override
    public String getFirstAvailableDateOrBooked(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String timeZone) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select min(date_time) from resource_calendar c");
        sql.append(" where c.date_time >= CONVERT_TZ(now(),'US/Central','").append(timeZone).append("')");
        sql.append(" and c.resource_id in (:resourceIds)");
        sql.append(" and c.schedule_id >= 0");
        sql.append(" order by c.date_time asc");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
 	    paramSource.addValue("resourceIds", resourceIds);
 	    return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class); 
    }
    
    
    @Override
    public String getCalStartAndEndDateTime(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds,String startDateTime, String calendarDate, boolean fetchAvailable) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select TIME(min(c.date_time)) as startDateTime, TIME(max(c.date_time)) as endDateTime ");
        sql.append(" from resource_calendar c ");
        sql.append(" where DATE(date_time) = :calendarDate");
        if (fetchAvailable) {
            sql.append(" and (c.schedule_id >= 0 or c.schedule_id = -2)");
        }
        sql.append(" and c.resource_id in (:resourceIds)");
        sql.append(" order by c.date_time asc");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
 	    paramSource.addValue("resourceIds", resourceIds);
 	    paramSource.addValue("calendarDate", calendarDate);
 	    return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class); 
    }
	
	@Override
	public List<ServiceVO> getServiceListByResourceId(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, int blockTimeInMins, String filterKeyWord, boolean isActiveList) throws Exception {
		try {
		StringBuilder sql = new StringBuilder();
		sql.append(" select s.* from service s, ");
		sql.append(" (select distinct service_id from resource_service rs where rs.allow_admin='Y' and rs.allow_selfservice='Y'");
		if(isActiveList) {
			sql.append(" and rs.enable='Y'");
		}
		sql.append("  and rs.resource_id=:resourceId) as rsAlias");
		sql.append(" where s.delete_flag='N'");
		if(isActiveList) {
			sql.append(" and s.enable='Y'");
		}
		sql.append(" and s.id=rsAlias.service_id order by placement asc");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceId", resourceId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, serviceVOMapper(filterKeyWord, blockTimeInMins));
	} catch (DataAccessException dae) {
		throw new TelAppointException(ERROR_2023.getCode(), ERROR_2023.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "ResourceId: "+resourceId);
	}
	}

	@Override
	public ApptSysConfig getAppSysConfig(JdbcCustomTemplate jdbcCustomTemplate) throws TelAppointException {
		String sql = "select * from appt_sys_config where 1=1";
		try {
			return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, apptSysConfigMapper());
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2010.getCode(), ERROR_2010.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public String getFirstAvailableDate(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateStr, boolean isPastDate) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		if (isPastDate) {
			sql.append(" select DATE(max(date_time)) from resource_calendar where DATE(date_time) < :dateStr");
		} else {
			sql.append(" select DATE(min(date_time)) from resource_calendar where DATE(date_time) >= :dateStr");
		}
		sql.append("  and  schedule_id >= 0 and resource_id in (:resourceIds) ");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("dateStr", dateStr);
		paramSource.addValue("resourceIds", resourceIds);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class);
	}

	@Override
	public Long getApptOpenTimeSlotsCount(JdbcCustomTemplate jdbcCustomTemplate, int locationId, List<Integer> resourceIds, String startDate, String endDate)
			throws TelAppointException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select count(rc.id) from resource_calendar rc ");
			sql.append(" where DATE(rc.date_time) >=:startDate and DATE(rc.date_time) <= :endDate");
			sql.append(" and rc.schedule_id=0 ");
			sql.append(" and rc.resource_id in (:resourceIds) ");
			sql.append(" and DATE(rc.date_time) NOT IN ");
			sql.append(" (select cd.date from closed_days cd where cd.location_id=").append(locationId);
			sql.append(" and cd.date >= :startDate and cd.date <= :endDate UNION ");
			sql.append(" select h.date from holidays h where h.date >= :startDate  and h.date<=:endDate)");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("startDate", startDate);
			paramSource.addValue("endDate", endDate);
			paramSource.addValue("resourceIds", resourceIds);

			Long count = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, Long.class);
			return count;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2004.getCode(), ERROR_2004.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public int getMinBlocks(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select min(blocks) from service s where s.enable='Y' and s.delete_flag ='N'");
		sql.append(" and id in (select res_service.service_id  from  resource_service res_service");
		sql.append(" where res_service.enable ='Y' and res_service.resource_id in (:resourceIds))");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceIds", resourceIds);
		Integer minBlocks = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, Integer.class);
		return minBlocks;
	}

	@Override
	public List<Integer> getResourceIds(JdbcCustomTemplate jdbcCustomTemplate, int locationId) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select group_concat(rc.id) from resource rc");
		sql.append(" where rc.delete_flag='N'");
		sql.append(" and rc.enable='Y'");
		sql.append(" and rc.location_id = :locationId");
		sql.append(" order by rc.id");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("locationId", locationId);
		String resourceIds = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class);
		List<Integer> resourceIdList = new ArrayList<Integer>();
		if (resourceIds == null || "".equals(resourceIds)) {
			return resourceIdList;
		}
		for (String resourceId : resourceIds.split(",")) {
			resourceIdList.add(Integer.valueOf(resourceId));
		}
		return resourceIdList;
	}

	@Override
	public Long getHoldAppointmentsCount(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String startDate, String endDate) throws TelAppointException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select count(s.id) from schedule s where s.appt_date_time ");
			sql.append(" between :startDate");
			sql.append(" and :endDate ");
			sql.append(" and s.id > 0 and s.status=1 ");
			sql.append(" and s.resource_id in (:resourceIds) ");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("startDate", startDate);
			paramSource.addValue("endDate", endDate);
			paramSource.addValue("resourceIds", resourceIds);
			Long count = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, Long.class);
			return count;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2006.getCode(), ERROR_2006.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public Long getBookedAppointmentsCount(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String startDate, String endDate) throws TelAppointException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select count(s.id) from schedule s where s.appt_date_time >=:startDate ");
			sql.append(" and s.appt_date_time <=:endDate ");
			sql.append(" and s.id > 0 and s.status>=11 and s.status<=19 ");
			sql.append(" and s.resource_id in (:resourceIds) ");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("startDate", startDate);
			paramSource.addValue("endDate", endDate);
			paramSource.addValue("resourceIds", resourceIds);

			Long count = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, Long.class);
			return count;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2005.getCode(), ERROR_2005.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	public void getStackChartInfo(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Integer locationId, Integer resourceId, String stackChartType,
			StackedChartResponse stackChartResponse) throws TelAppointException {
		try {
			String spName = "get_stackchart_info_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
			Map<String, Object> inParameters = new HashMap<String, Object>();
			inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
			inParameters.put(SPConstants.RESOURCE_ID.getValue(), resourceId);
			inParameters.put(SPConstants.STACK_CHAR_TYPE.getValue(), stackChartType);
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);

			Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
			Object stackedChartData = simpleJdbcCallResult.get(SPConstants.STACKEDCHART_INFO.getValue());
			String errorMessage = (errorMsg != null) ? (String) errorMsg : "";

			if (stackedChartData != null && !"".equals(stackedChartData)) {
				String stackedChartDataStr = (String) stackedChartData;
				logger.info("::" + stackedChartDataStr);
				System.out.println(stackedChartDataStr);
				parseReponse(stackedChartDataStr, stackChartResponse);
			} else {
				logger.error("Error while fetching get_stackchart_info: " + errorMessage);
				stackChartResponse.setMessage(errorMessage);
				stackChartResponse.setStatus(false);
			}
		} catch (DataAccessException dae) {
			StringBuilder inputData = new StringBuilder();
			inputData.append("resourceId: [").append(resourceId).append("]").append(",");
			inputData.append("locationId:[").append(locationId).append("]");
			throw new TelAppointException(ErrorConstants.ERROR_2008.getCode(), ErrorConstants.ERROR_2008.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(),
					inputData.toString());
		}
	}

	private void parseReponse(String stackedResponseData, StackedChartResponse stackedChartResponse) {
		String data[] = stackedResponseData.split("\\|", -1);

		for (String key : data) {
			String chartData[] = key.split("=", -1);
			if (chartData[0].contains("stackedChartDays")) {
				stackedChartResponse.setStackedChartDays(chartData[1]);
			}

			if (chartData[0].contains("noOfApptsBooked")) {
				stackedChartResponse.setNoOfApptsBooked(chartData[1]);

			}

			if (chartData[0].contains("noOfApptsOpened")) {
				stackedChartResponse.setNoOfApptsOpened(chartData[1]);
			}

			if (chartData[0].contains("noOfConfirmedNotifications")) {
				stackedChartResponse.setNoOfConfirmedNotifications(chartData[1]);
			}

			if (chartData[0].contains("noOfUnConfirmedNotifications")) {
				stackedChartResponse.setNoOfUnConfirmedNotifications(chartData[1]);
			}
		}
	}

	@Override
	public Map<String, List<String>> getPrivilegedPageNames(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select pr.privilege, pm.page_name from privilege_page_mapping pm, access_privilege pr where pm.privilege_id = pr.id";
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Map<String, List<String>>>() {
				@Override
				public Map<String, List<String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
					final Map<String, List<String>> pageNames = new HashMap<String, List<String>>();
					while (rs.next()) {
						String key = rs.getString("privilege");
						if (pageNames.containsKey(key)) {
							List<String> list = pageNames.get(key);
							list.add(rs.getString("page_name"));
						} else {
							List<String> list = new ArrayList<String>();
							list.add(rs.getString("page_name"));
							pageNames.put(key, list);
						}
					}
					return pageNames;
				}

			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2009.getCode(), ErrorConstants.ERROR_2009.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public List<ServiceLocation> getServiceLocationDates(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId) throws Exception {
		String sql = "select sl.id, s.service_name_online, sl.start_date, sl.end_date from service_location sl, service s where sl.location_id=? and sl.service_id=s.id";
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { locationId }, new ResultSetExtractor<List<ServiceLocation>>() {
				@Override
				public List<ServiceLocation> extractData(ResultSet rs) throws SQLException, DataAccessException {
					final List<ServiceLocation> serviceLocatinList = new ArrayList<ServiceLocation>();
					ServiceLocation serviceLoc;
					while (rs.next()) {
						serviceLoc = new ServiceLocation();
						serviceLoc.setServiceLocationId(rs.getInt("id"));
						serviceLoc.setLocationId(rs.getInt("location_id"));
						serviceLoc.setServiceName(rs.getString("service_name_online"));
						serviceLoc.setStartDate(rs.getString("start_date"));
						serviceLoc.setEndDate(rs.getString("end_date"));
						serviceLocatinList.add(serviceLoc);
					}
					return serviceLocatinList;
				}

			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2011.getCode(), ErrorConstants.ERROR_2011.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public boolean updateApptRestrictDates(JdbcCustomTemplate jdbcCustomTemplate, String startDate, String endDate) throws TelAppointException {
		String sql = "update appt_sys_config set appt_start_date=? , appt_end_date=?";
		int count = jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[] { startDate, endDate });
		return (count != 0);
	}
	
	public void updateTransId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId, Long transId) throws Exception {
		String sql = "update appointment set trans_id=? where schedule_id=?";
		if(transId != null && transId.longValue() > 0 ) {
			jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{transId, scheduleId});
		}
	}
	
	@Override
	public void bookAppointment(JdbcCustomTemplate jdbcCustomTemplate, Long scheduleId, String langCode, Integer apptMethod, ClientDeploymentConfig cdConfig, ConfirmAppointmentResponse confirmAppointmentResponse) throws Exception {
		try {
			String spName = "book_appointment_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
			logger.info("book_appointment_sp input params: ScheduleId: "+scheduleId+", BlockedTimeInMins:"+cdConfig.getBlockTimeInMins());
			
			Map<String,Object> inParameters = new HashMap<String,Object>();
			inParameters.put(SPConstants.SCHEDULE_ID.getValue(), scheduleId);
			inParameters.put(SPConstants.LANG_CODE.getValue(), langCode);
			inParameters.put(SPConstants.DEVICE.getValue(), "admin");
			inParameters.put(SPConstants.APPT_METHOD.getValue(), apptMethod);
			inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());
			long startTime = System.currentTimeMillis();
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
			long endTime = System.currentTimeMillis();
			logTimeTaken(spName, startTime, endTime);
			
			Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
			String errorMessage = (errorMsg!=null)?(String)errorMsg:"";
			
			if(errorMessage !=null && !"".equals(errorMessage)) {
				logger.error("Error from book appointment storedprocedure: "+errorMessage);
				confirmAppointmentResponse.setMessage(errorMessage);
				confirmAppointmentResponse.setStatus(false);
			}
		}  catch(DataAccessException dae) {
			StringBuilder inputData = new StringBuilder();
			inputData.append("scheduleId: [").append(scheduleId).append("]").append(",");
			inputData.append("langCode:[").append(langCode).append("]");
			throw new TelAppointException(ErrorConstants.ERROR_2027.getCode(), ErrorConstants.ERROR_2027.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), inputData.toString());
		}
	}

	@Override
	public boolean updateApptPerSeasonDetails(JdbcCustomTemplate jdbcCustomTemplate, String termStartDate, String termEndDate, Integer noApptPerTerm) throws TelAppointException {
		String sql = "update appt_sys_config set term_start_date=? , term_end_date=?, no_appt_per_term=?";
		int count = jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[] { termStartDate, termEndDate, noApptPerTerm });
		return (count != 0);
	}

	@Override
	public boolean updateServiceLocationApptDatesWindow(JdbcCustomTemplate jdbcCustomTemplate, List<ServiceLocation> serviceLocationList) throws TelAppointException {
		String sql = "update location_service set start_date=:startDate, end_date =:endDate where id=:serviceLocationId";
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		MapSqlParameterSource mapSQLParameterSource = null;
		try {
			for (ServiceLocation serviceLocation : serviceLocationList) {
				mapSQLParameterSource = new MapSqlParameterSource();
				mapSQLParameterSource.addValue("startDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(serviceLocation.getStartDate()));
				mapSQLParameterSource.addValue("endDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(serviceLocation.getEndDate()));
				mapSQLParameterSource.addValue("serviceLocationId", serviceLocation.getServiceLocationId());
				list.add(mapSQLParameterSource);
			}
			SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
			SqlParameterSource batchArray[] = list.toArray(mapArray);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		} catch (DataAccessException | ParseException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2012.getCode(), ErrorConstants.ERROR_2012.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
		return true;
	}

	@Override
	public boolean updateLocationsApptDates(JdbcCustomTemplate jdbcCustomTemplate, List<Location> locations) throws TelAppointException {
		String sql = "update location set appt_start_date=:apptStartDate, appt_end_date =:apptEndDate where id=:locationId";
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		MapSqlParameterSource mapSQLParameterSource = null;
		try {
			for (Location location : locations) {
				mapSQLParameterSource = new MapSqlParameterSource();
				mapSQLParameterSource.addValue("apptStartDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(location.getApptStartDate()));
				mapSQLParameterSource.addValue("apptEndDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(location.getApptEndDate()));
				mapSQLParameterSource.addValue("locationId", location.getLocationId());
				list.add(mapSQLParameterSource);
			}
			SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
			SqlParameterSource batchArray[] = list.toArray(mapArray);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		} catch (DataAccessException | ParseException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2013.getCode(), ErrorConstants.ERROR_2013.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
		return true;
	}

	@Override
	public void updateScheduleClosedStatus(JdbcCustomTemplate jdbcCustomTemplate, String closedStatus) throws TelAppointException {
		String sql = "update appt_sys_config set scheduler_closed=?";
		jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[] { closedStatus });
	}

	@Override
	public List<ServiceVO> getServiceListByResourceIds(JdbcCustomTemplate jdbcCustomTemplate, int blockTimeInMins, List<Integer> resourceIds,String filterKeyWord, boolean isSelfService) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select s.* from  service s, (select distinct(rs.service_id) from resource_service rs where rs.resource_id in (:resourceIds) and rs.enable ='Y'");
		if (isSelfService) {
			sql.append(" and rs.allow_selfservice ='Y') as rsAlias ");
		}
		sql.append(" where s.delete_flag='N' ");
		sql.append(" order by s.id asc");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceIds", resourceIds);
		try {
			return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, serviceVOMapper(filterKeyWord, blockTimeInMins));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2016.getCode(), ERROR_2016.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "resourceIds: " + resourceIds);
		}
	}

	@Override
	public DisplayNames getDisplayNames(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select * from display_names ";
		try {
			return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, displayNamesMapper());
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2017.getCode(), ERROR_2017.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public boolean updateLocation(JdbcCustomTemplate jdbcCustomTemplate, Location location) throws Exception {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		sql.append("update location ");
		sql.append("set location_name_online=:locationName");
		paramSource.addValue("locationName", location.getLocationNameOnline());
		
		sql.append(", location_name_mobile=:locationNameMobile");
		paramSource.addValue("locationNameMobile", location.getLocationNameMobile()==null?"":location.getLocationNameMobile());
		
		sql.append(", location_name_sms=:locationNameSMS");
		paramSource.addValue("locationNameSMS", location.getLocationNameSMS()==null?"":location.getLocationNameSMS());
		
		sql.append(", location_name_remind_sms=:locationNameRemindSMS");
		paramSource.addValue("locationNameRemindSMS", location.getLocationNameRemindSMS()==null?"":location.getLocationNameRemindSMS());
		
		sql.append(", location_name_ivr_tts=:locationNameIvrTts");
		paramSource.addValue("locationNameIvrTts", location.getLocationNameIvrTts());

		sql.append(", location_name_ivr_audio=:locationNameIvrAudio");
		paramSource.addValue("locationNameIvrAudio", location.getLocationNameIvrAudio()==null?"":location.getLocationNameIvrAudio());

		sql.append(", address=:address");
		paramSource.addValue("address", location.getAddress());

		sql.append(", city=:city");
		paramSource.addValue("city", location.getCity());

		sql.append(", state=:state");
		paramSource.addValue("state", location.getState());

		sql.append(", zip=:zip");
		paramSource.addValue("zip", location.getZip());

		sql.append(", work_phone=:workPhone");
		paramSource.addValue("workPhone", location.getWorkPhone());

		sql.append(", location_google_map=:locationGoogleMap");
		paramSource.addValue("locationGoogleMap", location.getLocationGoogleMap() == null?"":location.getLocationGoogleMap());

		sql.append(", location_google_map_link=:locationGoogleMapLink");
		paramSource.addValue("locationGoogleMapLink", location.getLocationGoogleMapLink()==null?"":location.getLocationGoogleMapLink());

		sql.append(", time_zone=:timeZone");
		paramSource.addValue("timeZone", location.getTimeZone()==null?"":location.getTimeZone());

		sql.append(", comments=:comments");
		paramSource.addValue("comments", location.getComment()==null?"":location.getComment());

		sql.append(", delete_flag=:deleteFlag");
		paramSource.addValue("deleteFlag", location.getDeleteFlag()==null?"N":location.getDeleteFlag());

		sql.append(", `enable`=:enable");
		paramSource.addValue("enable", location.getEnable()==null?"Y":location.getEnable());

		sql.append(", closed=:closed");
		paramSource.addValue("closed", location.getClosed()==null?"N":location.getClosed());

		sql.append(", closed_message=:closedMessage");
		paramSource.addValue("closedMessage", location.getClosedMessage()==null?"":location.getClosedMessage());

		sql.append(", closed_audio=:closedAudio");
		paramSource.addValue("closedAudio", location.getClosedAudio()==null?"":location.getClosedAudio());

		sql.append(", closed_tts=:closedTts");
		paramSource.addValue("closedTts", location.getClosedTts()==null?"":location.getClosedTts());

		if(location.getApptStartDate() != null && !"".equals(location.getApptStartDate())) {
			sql.append(", appt_start_date=:apptStartDate");
			paramSource.addValue("apptStartDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(location.getApptStartDate()));
		}
		
		if(location.getApptEndDate() != null && !"".equals(location.getApptEndDate())) {
			sql.append(", appt_end_date=:apptEndDate");
			paramSource.addValue("apptEndDate",  DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(location.getApptEndDate()));
		}

		sql.append(" where id=:locationId");
		paramSource.addValue("locationId", location.getLocationId());
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource) != 0;
	}
	
	@Override
	public List<ServiceVO> getDeletedServiceList(JdbcCustomTemplate jdbcCustomTemplate, Integer blockTimeInMins, String filterKey) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from service where delete_flag ='Y'");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), serviceVOMapper(filterKey, blockTimeInMins));
	}
	
	@Override
	public List<Resource> getDeletedResourceList(JdbcCustomTemplate jdbcCustomTemplate, String filterKey) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select r.*,l.location_name_online,group_concat(distinct s.service_name_online) as serviceNames from resource r LEFT OUTER JOIN resource_service rs on r.id=rs.resource_id LEFT OUTER JOIN service s on rs.service_id = s.id LEFT OUTER JOIN location l on r.location_id=l.id where r.delete_flag ='Y' and rs.`enable` = 'Y' and s.delete_flag = 'N' and s.`enable` = 'Y' group by resource_id order by l.placement, r.placement asc");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), resourceMapper(filterKey));
	}
	
	@Override
	public List<Location> getDeletedLocationList(JdbcCustomTemplate jdbcCustomTemplate, String filterKey) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from location where delete_flag ='Y'");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), locationMapper(filterKey));
	}

	@Override
	public List<ResourcePrefix> getResourcePrefixList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select * from resource_prefix_options";	
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), resourcePrefixMapper());
	}
	
	@Override
	public List<ResourceTitle> getResourceTitleList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select * from resource_title_options";	
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), resourceTitleMapper());
	}
	
	@Override
	public List<ResourceType> getResourceTypeList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select * from resource_type_options";	
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), resourceTypeMapper());
	}

	@Override
	public Map<String, DynamicFieldDisplay> getDynamicFieldDisplay(JdbcCustomTemplate jdbcCustomTemplate, String pageName) throws Exception {
		String sql = "select * from dynamic_fields_display where page_name=?";
		List<DynamicFieldDisplay> dynamicFieldDisplayList = jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{pageName}, dynamicFieldsDisplayMapper());
		Map<String, DynamicFieldDisplay> dynamicFieldsmap = dynamicFieldDisplayList.stream().collect(Collectors.toMap(DynamicFieldDisplay::getColumnName, Function.identity()));
		return dynamicFieldsmap;
	}

	@Override
	public boolean deleteLocation(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId) throws Exception {
		String sql = "update location set delete_flag='Y' where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{locationId}) != 0;
	}
	
	@Override
	public boolean unDeleteLocation(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId) throws Exception {
		String sql = "update location set delete_flag='N' where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{locationId}) != 0;
	}
	
	
	
	@Override
	public Integer addLocation(JdbcCustomTemplate jdbcCustomTemplate, Location location) throws Exception {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		int placement = getMaxPlacementVal(jdbcCustomTemplate, "location");
		sql.append("insert into location (location_name_online, location_name_mobile, location_name_sms, location_name_remind_sms, location_name_ivr_tts, location_name_ivr_audio,");
		sql.append("address, city, state, zip, work_phone, location_google_map, location_google_map_link, time_zone, comments,");
		sql.append("delete_flag, `enable`, closed, closed_message, closed_audio, closed_tts");
		
		if(location.getApptStartDate() != null && !"".equals(location.getApptStartDate())) {
			sql.append(",appt_start_date ");
		}
		
		if(location.getApptEndDate() != null && !"".equals(location.getApptEndDate())) {
			sql.append(",appt_end_date");
		}
		sql.append(",placement)");
		sql.append(" value (:locationName, :locationNameMobile,:locationNameSMS,:locationNameRemindSMS, :locationNameIvrTts, :locationNameIvrAudio,:address, :city, :state, ");
		sql.append(":zip,:workPhone,:locationGoogleMap,:locationGoogleMapLink, :timeZone, :comments, :deleteFlag, :enable,");
		sql.append(":closed,:closedMessage,:closedAudio,:closedTts");

		if(location.getApptStartDate() != null && !"".equals(location.getApptStartDate())) {
			sql.append(",:apptStartDate ");
		}
		
		if(location.getApptEndDate() != null && !"".equals(location.getApptEndDate())) {
			sql.append(",:apptEndDate");
		}
		sql.append(", :placement)");
		
		paramSource.addValue("locationName", location.getLocationNameOnline() == null?"":location.getLocationNameOnline());
		paramSource.addValue("locationNameMobile", location.getLocationNameMobile()==null?"": location.getLocationNameMobile());
		paramSource.addValue("locationNameSMS", location.getLocationNameSMS() == null?"":location.getLocationNameSMS());
		paramSource.addValue("locationNameRemindSMS", location.getLocationNameRemindSMS() == null?"":location.getLocationNameRemindSMS());
		paramSource.addValue("locationNameIvrTts", location.getLocationNameIvrTts() == null?"":location.getLocationNameIvrTts());
		
		String locationNameIvrAudio = location.getLocationNameIvrAudio();
		int index = (locationNameIvrAudio!=null)?locationNameIvrAudio.lastIndexOf("/")+1:-1;
		locationNameIvrAudio = (locationNameIvrAudio != null)?locationNameIvrAudio.substring(index).replaceAll(".wav",""):"";
		
		paramSource.addValue("locationNameIvrAudio", locationNameIvrAudio);
		paramSource.addValue("address", location.getAddress()==null?"":location.getAddress());
		paramSource.addValue("city", location.getCity()==null?"":location.getCity());
		paramSource.addValue("state", location.getState()==null?"":location.getState());
		paramSource.addValue("zip", location.getZip()==null?"":location.getZip());
		paramSource.addValue("workPhone", location.getWorkPhone() == null?"":location.getWorkPhone());
		paramSource.addValue("locationGoogleMap", location.getLocationGoogleMap()==null?"":location.getLocationGoogleMap());
		paramSource.addValue("locationGoogleMapLink", location.getLocationGoogleMapLink() == null?"":location.getLocationGoogleMapLink());
		paramSource.addValue("timeZone", location.getTimeZone() == null?"":location.getTimeZone());
		paramSource.addValue("comments", location.getComment() == null ? "":location.getComment());
		paramSource.addValue("deleteFlag", location.getDeleteFlag() == null?"N":location.getDeleteFlag());
		paramSource.addValue("enable", location.getEnable() == null?"Y":location.getEnable());
		paramSource.addValue("closed", location.getClosed() == null?"N":location.getClosed());
		paramSource.addValue("closedMessage", location.getClosedMessage() == null?"":location.getClosedMessage());
		
		String closedAudio = location.getClosedAudio();
		index = (closedAudio!=null)?closedAudio.lastIndexOf("/")+1:-1;
		closedAudio = (closedAudio != null)?closedAudio.substring(index).replaceAll(".wav",""):"";
		paramSource.addValue("closedAudio", closedAudio);
		paramSource.addValue("closedTts", location.getClosedTts() == null?"":location.getClosedTts());
		
		if(location.getApptStartDate() != null && !"".equals(location.getApptStartDate())) {
			paramSource.addValue("apptStartDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(location.getApptStartDate()));
		}
		
		if(location.getApptEndDate() != null && !"".equals(location.getApptEndDate())) {
			paramSource.addValue("apptEndDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(location.getApptEndDate()));
		}
		paramSource.addValue("placement", placement);
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource, holder);
		return holder.getKey().intValue();
	}
	
	
	@Override
	public Integer addResource(JdbcCustomTemplate jdbcCustomTemplate, Resource resource) throws Exception {
		StringBuilder sql = new StringBuilder("insert into resource ");
		sql.append("(resource_code, location_id, resource_type, prefix, first_name, last_name, title,email,resource_audio, call_center_logic,delete_flag, enable,allow_selfservice, placement)");
		sql.append(" values (:resourceCode, :locationId,:resourceType,:prefix,:firstName,:lastName,:title,:email,:resourceAudio,:callCenterLogic,:deleteFlag,:enable,:allowSelfService,:placement)");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceCode", resource.getResourceCode() == null?"":resource.getResourceCode());
		paramSource.addValue("locationId", resource.getLocationId());
		paramSource.addValue("resourceType", resource.getResourceType() ==null?"":resource.getResourceType());
		paramSource.addValue("prefix", resource.getPrefix());
		paramSource.addValue("firstName", resource.getFirstName());
		paramSource.addValue("lastName", resource.getLastName());
		paramSource.addValue("title", resource.getTitle());
		paramSource.addValue("email", resource.getEmail());
		String resourceAudit = resource.getResourceAudio();
		int index = (resourceAudit!=null)?resourceAudit.lastIndexOf("/")+1:-1;
		resourceAudit = (resourceAudit != null)?resourceAudit.substring(index).replaceAll(".wav",""):"";
		paramSource.addValue("resourceAudio", resourceAudit);
		paramSource.addValue("callCenterLogic", resource.getCallCenterLogic() == null ? "N" : resource.getCallCenterLogic());
		paramSource.addValue("deleteFlag", resource.getDeleteFlag() == null ? "N" : resource.getDeleteFlag());
		paramSource.addValue("enable", resource.getEnable() == null ? "Y" : resource.getEnable());
		paramSource.addValue("allowSelfService", resource.getAllowSelfService() == null ? "Y" : resource.getAllowSelfService());
		paramSource.addValue("placement", getMaxPlacementVal(jdbcCustomTemplate, "resource"));
		KeyHolder holder = new GeneratedKeyHolder();

		jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource, holder);
		Integer resourceId = holder.getKey().intValue();
		resource.setResourceId(resourceId);

		return resourceId;
		
	}

	
	@Override
	public List<Integer> getDepartmentIds(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql ="select GROUP_CONCAT(id) from department where delete_flag='N'";
		String departmentIds = jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, String.class);
		if(departmentIds != null && !"".equals(departmentIds)) {
			return Arrays.asList(departmentIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
	
	private List<Integer> getResourceIds(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql ="select GROUP_CONCAT(id) from resource where delete_flag='N'";
		String resourceIds = jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, String.class);
		if(resourceIds != null && !"".equals(resourceIds)) {
			return Arrays.asList(resourceIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
	
	private List<Integer> getServiceIds(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql ="select GROUP_CONCAT(id) from service where delete_flag='N'";
		String serviceIds = jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, String.class);
		if(serviceIds != null && !"".equals(serviceIds)) {
			return Arrays.asList(serviceIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
	

	@Override
	public boolean addOrUpdateResourceService(JdbcCustomTemplate jdbcCustomTemplate, Resource resource, boolean isUpdate) throws Exception {
		StringBuilder sql = new StringBuilder();
		if(isUpdate) {
			sql.append("update resource_service set `enable`=:enable where resource_id=:resourceId and service_id=:serviceId");
		} else {
			sql.append("insert into resource_service (resource_id,service_id,`enable`) values (:resourceId, :serviceId, :enable)");
		}
		logger.info("Query: "+sql.toString());
		List<Integer> selectedServiceIdList = null;
		logger.info("selected serviceIds: "+resource.getSelectedServiceIds());
		if(resource.getSelectedServiceIds() != null && !"".equals(resource.getSelectedServiceIds())) {
			selectedServiceIdList = Arrays.asList(resource.getSelectedServiceIds().split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		} else {
			selectedServiceIdList = new ArrayList<>();
		}
		List<Integer> allServiceIdList = getServiceIds(jdbcCustomTemplate);
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		MapSqlParameterSource paramSource = null;
		for(Integer serviceId : allServiceIdList) {
			paramSource = new MapSqlParameterSource();
			paramSource.addValue("resourceId", resource.getResourceId());
			paramSource.addValue("serviceId", serviceId);
			if(selectedServiceIdList.contains(serviceId)) {
				paramSource.addValue("enable", "Y");
			} else {
				paramSource.addValue("enable", "N");
			}
			list.add(paramSource);
		}

		if(!list.isEmpty()) {
			SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
			SqlParameterSource batchArray[] = list.toArray(mapArray);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		}
		
		return true;
	}

	@Override
	public void addOrUpdateLocationDepartionResource(JdbcCustomTemplate jdbcCustomTemplate, Resource resource, boolean isUpdate) throws Exception {
		StringBuilder sql = new StringBuilder();
		if(isUpdate) {
			sql.append("update location_department_resource set `enable`=:enable where location_id=:locationId and resource_id=:resourceId and department_id=:departmentId");
		} else {
			sql.append("insert into location_department_resource (location_id, department_id, resource_id, `enable`) values (:locationId, :departmentId, :resourceId,:enable)");
		}
		logger.info("Query:"+sql.toString());
		List<Integer> allDepartmentIds = getDepartmentIds(jdbcCustomTemplate); 
		logger.info("selected departmentIds: "+resource.getSelectedDepartmentIds());
		List<Integer> selectedDepartmentIds = null;
		if(resource.getSelectedDepartmentIds() != null && !"".equals(resource.getSelectedDepartmentIds())) {
			selectedDepartmentIds =Arrays.asList(resource.getSelectedDepartmentIds().split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		} else {
			selectedDepartmentIds = new ArrayList<>();
		}
		
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		
		MapSqlParameterSource paramSource = null;
		for(Integer departmentId : allDepartmentIds) {
			paramSource = new MapSqlParameterSource();
			paramSource.addValue("locationId", resource.getLocationId());
			paramSource.addValue("departmentId", departmentId);
			paramSource.addValue("resourceId", resource.getResourceId());
			if(selectedDepartmentIds.contains(departmentId)) {
				paramSource.addValue("enable", "Y");
			} else {
				paramSource.addValue("enable", "N");
			}
			list.add(paramSource);
		}

		if(!list.isEmpty()) {
			SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
			SqlParameterSource batchArray[] = list.toArray(mapArray);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		}
	}

	@Override
	public boolean updateResource(JdbcCustomTemplate jdbcCustomTemplate, Resource resource) throws Exception {
		StringBuilder sql = new StringBuilder("update resource set ");
		sql.append("location_id=:locationId");
		//sql.append(",resource_type=:resourceType");
		//sql.append(", resource_code=:resourceCode");
		sql.append(",prefix=:prefix");
		sql.append(",first_name=:firstName");
		sql.append(",last_name=:lastName");
		sql.append(",title=:title");
		sql.append(",email=:email");
		sql.append(",resource_audio=:resourceAudio");
		sql.append(",call_center_logic=:callCenterLogic");
		sql.append(",delete_flag=:deleteFlag");
		sql.append(",enable=:enable");
		sql.append(",allow_selfservice=:allowSelfService");
		sql.append(" where id=:resourceId");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		//paramSource.addValue("resourceCode", resource.getResourceCode()==null?"":resource.getResourceCode());
		paramSource.addValue("locationId", resource.getLocationId());
		paramSource.addValue("resourceId", resource.getResourceId());
		//paramSource.addValue("resourceType", resource.getResourceType()==null?"":resource.getResourceType());
		paramSource.addValue("prefix", resource.getPrefix());
		paramSource.addValue("firstName", resource.getFirstName());
		paramSource.addValue("lastName", resource.getLastName());
		paramSource.addValue("title", resource.getTitle());
		paramSource.addValue("email", resource.getEmail());
		paramSource.addValue("resourceAudio", resource.getResourceAudio()==null?"":resource.getResourceAudio());
		paramSource.addValue("callCenterLogic",resource.getCallCenterLogic()==null?"N":resource.getCallCenterLogic());
		paramSource.addValue("deleteFlag", resource.getDeleteFlag()==null?"N":resource.getDeleteFlag());
		paramSource.addValue("enable",resource.getEnable()==null?"Y":resource.getEnable());
		paramSource.addValue("allowSelfService", resource.getAllowSelfService()==null?"Y":resource.getAllowSelfService());
		boolean isSuccess = jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource) != 0;
		return isSuccess;

	}
	
	public int getMaxPlacementVal(JdbcCustomTemplate jdbcCustomTemplate, String table) {	
		String sql = "select max(placement)+1 from " + table;
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, Integer.class);
	}

	@Override
	public boolean deleteResource(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId) throws Exception {
		String sql = "update resource set delete_flag='Y' where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{resourceId}) != 0;
	}

	@Override
	public boolean unDeleteResource(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId) throws Exception {
		String sql = "update resource set delete_flag='N' where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{resourceId}) != 0;
	}

	@Override
	public boolean deleteService(JdbcCustomTemplate jdbcCustomTemplate, Integer serviceId) throws Exception {
		String sql = "update service set delete_flag='Y' where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{serviceId}) != 0;
	}

	@Override
	public boolean unDeleteService(JdbcCustomTemplate jdbcCustomTemplate, Integer serviceId) throws Exception {
		String sql = "update service set delete_flag='N' where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{serviceId}) != 0;
	}

	@Override
	public Integer addService(JdbcCustomTemplate jdbcCustomTemplate, ServiceVO service, Integer blockTimeInMins) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into service ");
		sql.append("(");
		sql.append("service_name_online,service_name_mobile, service_name_sms,service_name_ivr_tts,service_name_ivr_audio");
		sql.append(",custom_msg_tts,custom_msg_audio, blocks,buffer,min_charge,price");
		sql.append(",delete_flag,`enable`, allow_duplicate_appt, skip_date_time,closed, closed_message,closed_audio,closed_tts");
		sql.append(",is_sun_open, is_mon_open, is_tue_open, is_wed_open, is_thu_open, is_fri_open, is_sat_open ");
		sql.append(",appt_start_date");
		sql.append(",appt_end_date");
		sql.append(", ser_custom_msg, closed_location_ids ");
		sql.append(")");
		sql.append(" values ").append("(");
		sql.append(":serviceNameOnline,:serviceNameMobile,:serviceNameSMS,:serviceNameIvrTts,:serviceNameIvrAudio,:customMsgTts ");
		sql.append(",:customMsgAudio,:blocks,:buffer,:minCharge,:price,:deleteFlag,:enable,:allowDuplicateAppt");
		sql.append(",:skipDateTime,:closed,:closedMessage,:closedAudio,:closedTts");
		sql.append(",:isSunOpen,:isMonOpen,:isTueOpen,:isWedOpen,:isThuOpen,:isFriOpen,:isSatOpen");
		
		if(service.getApptStartDate() == null || "".equals(service.getApptStartDate())) {
			sql.append(",appt_start_date = NULL");
		} else {
			sql.append(",:apptStartDate");
		}
		
		if(service.getApptEndDate() == null || "".equals(service.getApptEndDate())) {
			sql.append(",appt_end_date = NULL");
		} else {
			sql.append(",:apptEndDate");
		}
		
		sql.append(",:serCustomMsg");
		sql.append(",:closedLocationIds");
		sql.append(")");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("serviceNameOnline", service.getServiceNameOnline());
		paramSource.addValue("serviceNameMobile", service.getServiceNameMobile() == null?"":service.getServiceNameMobile());
		paramSource.addValue("serviceNameSMS", service.getServiceNameSMS()==null?"":service.getServiceNameMobile());
		paramSource.addValue("serviceNameIvrTts", service.getServiceNameIvrTts()==null?"":service.getServiceNameIvrTts());
		
		String serviceNameIvrAudio = service.getServiceNameIvrAudio();
		int index = (serviceNameIvrAudio!=null)?serviceNameIvrAudio.lastIndexOf("/")+1:-1;
		serviceNameIvrAudio = (serviceNameIvrAudio != null)?serviceNameIvrAudio.substring(index).replaceAll(".wav",""):"";
		
		paramSource.addValue("serviceNameIvrAudio", serviceNameIvrAudio);
		
		paramSource.addValue("customMsgTts", service.getCustomMsgTts()==null?"":service.getCustomMsgTts());
		
		String customMsgAudit = service.getCustomMsgAudio();
		index = (customMsgAudit!=null)?customMsgAudit.lastIndexOf("/")+1:-1;
		customMsgAudit = (customMsgAudit != null)?customMsgAudit.substring(index).replaceAll(".wav",""):"";
		
		paramSource.addValue("customMsgAudio", customMsgAudit);
		int blocks = service.getDuration()/blockTimeInMins;
		paramSource.addValue("blocks", blocks);
		paramSource.addValue("buffer", service.getBuffer()==null?0:service.getBuffer());
		
		paramSource.addValue("minCharge", service.getMinCharge()==null?0.0:service.getMinCharge());
		paramSource.addValue("price", service.getPrice()==null?0.0:service.getPrice());
		paramSource.addValue("deleteFlag", service.getDeleteFlag()==null?"N":service.getDeleteFlag());
		paramSource.addValue("enable",service.getEnable()==null?"Y":service.getEnable());
		paramSource.addValue("allowDuplicateAppt", service.getAllowDuplicateAppt()==null?"Y":service.getAllowDuplicateAppt());
		
		paramSource.addValue("skipDateTime", service.getSkipDateTIme()==null?"N":service.getSkipDateTIme());
		paramSource.addValue("closed", service.getClosed()==null?"N":service.getClosed());
		paramSource.addValue("closedMessage", service.getClosedMessage()==null?"":service.getClosedMessage());
		
		String closedAudio = service.getClosedAudio();
		index = (closedAudio!=null)?closedAudio.lastIndexOf("/")+1:-1;
		closedAudio = (closedAudio != null)?closedAudio.substring(index).replaceAll(".wav",""):"";
		
		paramSource.addValue("closedAudio", closedAudio);
		paramSource.addValue("closedTts", service.getClosedTts()==null?"":service.getClosedTts());
		
		paramSource.addValue("isSunOpen", service.getIsSunOpen());
		paramSource.addValue("isMonOpen", service.getIsMonOpen());
		paramSource.addValue("isTueOpen", service.getIsTueOpen());
		paramSource.addValue("isWedOpen", service.getIsWedOpen());
		paramSource.addValue("isThuOpen", service.getIsThuOpen());
		paramSource.addValue("isFriOpen", service.getIsFriOpen());
		paramSource.addValue("isSatOpen", service.getIsSatOpen());
		
		if(service.getApptStartDate() != null && !"".equals(service.getApptStartDate())) {
			paramSource.addValue("apptStartDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(service.getApptStartDate()));
		}
		
		
		if(service.getApptEndDate() != null && !"".equals(service.getApptEndDate())) {
			paramSource.addValue("apptEndDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(service.getApptStartDate()));
		}
		paramSource.addValue("serCustomMsg", service.getSerCustomMsg()==null?"":service.getSerCustomMsg());
		paramSource.addValue("closedLocationIds", service.getClosedLocationIds()==null?"":service.getClosedLocationIds());
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource, holder);
		return holder.getKey().intValue();
	}

	@Override
	public boolean updateService(JdbcCustomTemplate jdbcCustomTemplate, ServiceVO service, Integer blockTimeInMins) throws Exception {
		StringBuilder sql = new StringBuilder("update service set service_name_online=:serviceNameOnline");
		sql.append(",service_name_mobile=:serviceNameMobile");
		sql.append(",service_name_sms=:serviceNameSMS");
		sql.append(",service_name_ivr_tts=:serviceNameIvrTts");
		sql.append(",service_name_ivr_audio=:serviceNameIvrAudio");
		sql.append(",custom_msg_tts=:customMsgTts");
		sql.append(",custom_msg_audio=:customMsgAudio");
		if(service.getDuration() != 0) {
			int blocks = service.getDuration()/blockTimeInMins;
			sql.append(",blocks="+blocks);
		}
		
		sql.append(",buffer=:buffer");
		sql.append(",min_charge=:minCharge");
		sql.append(",price=:price");
		sql.append(",delete_flag=:deleteFlag");
		sql.append(",enable=:enable");
		sql.append(",allow_duplicate_appt=:allowDuplicateAppt");
		sql.append(",skip_date_time=:skipDateTime");
		sql.append(",closed=:closed");
		sql.append(",closed_message=:closedMessage");
		sql.append(",closed_audio=:closedAudio");
		sql.append(",closed_tts=:closedTts");
		//sql.append(",bucket_style=:bucketStyle");
		//sql.append(",service_css_color=:serviceCSSColor");
		//sql.append(",send_reminder=:sendReminder");
		sql.append(",is_sun_open=:isSunOpen");
		sql.append(",is_mon_open=:isMonOpen");
		sql.append(",is_tue_open=:isTueOpen");
		sql.append(",is_wed_open=:isWedOpen");
		sql.append(",is_thu_open=:isThuOpen");
		sql.append(",is_fri_open=:isFriOpen");
		sql.append(",is_sat_open=:isSatOpen");
		if(service.getApptStartDate() != null && !"".equals(service.getApptStartDate())) {
			sql.append(",appt_start_date=:apptStartDate");
		} else {
			sql.append(",appt_start_date=NULL");
		}
		
		if(service.getApptEndDate()!= null && !"".equals(service.getApptEndDate())) {
			sql.append(",appt_end_date=:apptEndDate");
		} else {
			sql.append(",appt_end_date=NULL");
		}
		sql.append(",ser_custom_msg=:serCustomMsg where id=:serviceId");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("serviceNameOnline", service.getServiceNameOnline());
		paramSource.addValue("serviceNameMobile", service.getServiceNameMobile());
		paramSource.addValue("serviceNameSMS", service.getServiceNameSMS());
		paramSource.addValue("serviceNameIvrTts", service.getServiceNameIvrTts());
		String serviceNameIvrAudio = service.getServiceNameIvrAudio();
		int index = (serviceNameIvrAudio!=null)?serviceNameIvrAudio.lastIndexOf("/")+1:-1;
		serviceNameIvrAudio = (serviceNameIvrAudio != null)?serviceNameIvrAudio.substring(index).replaceAll(".wav",""):"";
		paramSource.addValue("serviceNameIvrAudio", serviceNameIvrAudio);
		paramSource.addValue("customMsgTts", service.getCustomMsgTts());
		
		String customMsgAudit = service.getCustomMsgAudio();
		index = (customMsgAudit!=null)?customMsgAudit.lastIndexOf("/")+1:-1;
		customMsgAudit = (customMsgAudit != null)?customMsgAudit.substring(index).replaceAll(".wav",""):"";
		paramSource.addValue("customMsgAudio", customMsgAudit);
		paramSource.addValue("blocks", service.getBlocks());
		paramSource.addValue("buffer", service.getBuffer());
		paramSource.addValue("minCharge", service.getMinCharge());
		paramSource.addValue("price", service.getPrice());
		paramSource.addValue("deleteFlag", service.getDeleteFlag()==null?"N":service.getDeleteFlag());
		paramSource.addValue("enable",service.getEnable());
		paramSource.addValue("allowDuplicateAppt", service.getAllowDuplicateAppt());
		paramSource.addValue("skipDateTime", service.getSkipDateTIme());
		paramSource.addValue("closed", service.getClosed());
		paramSource.addValue("closedMessage", service.getClosedMessage());
		
		String closedAudio = service.getClosedAudio();
		index = (closedAudio!=null)?closedAudio.lastIndexOf("/")+1:-1;
		closedAudio = (closedAudio != null)?closedAudio.substring(index).replaceAll(".wav",""):"";
		
		paramSource.addValue("closedAudio", closedAudio);
		paramSource.addValue("closedTts", service.getClosedTts());
		paramSource.addValue("isSunOpen", service.getIsSunOpen());
		paramSource.addValue("isMonOpen", service.getIsMonOpen());
		paramSource.addValue("isTueOpen", service.getIsTueOpen());
		paramSource.addValue("isWedOpen", service.getIsWedOpen());
		paramSource.addValue("isThuOpen", service.getIsThuOpen());
		paramSource.addValue("isFriOpen", service.getIsFriOpen());
		paramSource.addValue("isSatOpen", service.getIsSatOpen());
		if(service.getApptStartDate() != null && !"".equals(service.getApptStartDate())) {
			paramSource.addValue("apptStartDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(service.getApptStartDate()));
		}
		
		if(service.getApptEndDate()!= null && !"".equals(service.getApptEndDate())) {
			paramSource.addValue("apptEndDate", DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(service.getApptEndDate()));
		}
		paramSource.addValue("serCustomMsg", service.getSerCustomMsg());
		paramSource.addValue("serviceId", service.getServiceId());
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource)!=0;
	}
	

	@Override
	public List<IvrCalls> getInBoundCallLogs(JdbcCustomTemplate jdbcCustomTemplate, String fromDate, String toDate, String callerId) throws Exception {
		StringBuilder sql =new StringBuilder("select a.conf_number,c.first_name as customerFirstName, c.last_name as customerLastName, IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-', RIGHT(c.contact_phone,4)),'') as contact_phone, ");
		sql.append("l.location_name_online, CONCAT(r.prefix,' ',r.first_name,' ',r.last_name) as resourceName,");
		sql.append("m.caller_id, s.service_name_online, ic.seconds, ic.trans_id ");
		sql.append(" from ivr_calls ic");
		sql.append(" LEFT OUTER JOIN resource r ON ic.resource_id=r.id");
		sql.append(" LEFT OUTER JOIN location l ON ic.location_id=l.id");
		sql.append(" LEFT OUTER JOIN service s ON ic.service_id=s.id"); 
		sql.append(" LEFT OUTER JOIN customer c ON ic.customer_id=c.id");
		sql.append(" LEFT OUTER JOIN main m ON ic.trans_id=m.trans_id");
		sql.append(" LEFT OUTER JOIN schedule sc ON ic.trans_id=sc.trans_id");
		sql.append(" LEFT OUTER JOIN appointment a ON ic.conf_num=a.conf_number");
		sql.append(" where DATE(ic.start_time)>=? and DATE(ic.end_time) <=?");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{fromDate,toDate}, inBoundCallMapper());
	}
	
	@Override
	public List<OutBoundCalls> getOutBoundCallLogs(JdbcCustomTemplate jdbcCustomTemplate, String fromDate, String toDate, String callerId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT nps.attempt_id, n.first_name, n.last_name, nps.seconds, DATE_FORMAT(n.due_date_time,'%m/%d/%Y %l:%i %p') as apptDateTime, DATE_FORMAT(nps.call_timestamp,'%m/%d/%Y %l:%i %p') as callTime,");
		sql.append("CONCAT(LEFT(nps.phone,3),'-',MID(nps.phone,4,3),'-', RIGHT(nps.phone,4)) as phone,");
		sql.append("l.location_name_online, s.service_name_online, CONCAT(r.prefix,'',r.first_name,'',r.last_name) as resourceName");
		sql.append(" FROM notify_phone_status nps");
		sql.append(" LEFT OUTER JOIN notify n ON nps.notify_id=n.id");
		sql.append(" LEFT OUTER JOIN location l ON n.location_id=l.id");
		sql.append(" LEFT OUTER JOIN service s ON n.service_id=s.id");
		sql.append(" LEFT OUTER JOIN resource r ON n.resource_id=r.id");
		sql.append(" where DATE(nps.call_timestamp) >=:fromDate and DATE(nps.call_timestamp) <= :toDate");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("fromDate", fromDate);
		paramSource.addValue("toDate", toDate);
		if(null!=callerId && !"".equals(callerId.trim())){
        	sql.append(" and nps.phone like (:callerId)");
        	paramSource.addValue("callerId", callerId+"%");
        }
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, outBoundCallMapper());
	}

	@Override
	public boolean addLocationWorkingHrs(JdbcCustomTemplate jdbcCustomTemplate, ApptSysConfig apptSysConfig, Integer locationId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into location_working_hrs ");
		sql.append("(");
		sql.append("location_id");
		if ("Y".equals(apptSysConfig.getDefaultIsSunOpen())) {
		  sql.append(",sun_start_time, sun_end_time");
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsMonOpen())) {
			sql.append(",mon_start_time,mon_end_time");
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsTueOpen())) {
			sql.append(",tues_start_time,tues_end_time");
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsWedOpen())) {
			sql.append(",wed_start_time, wed_end_time");
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsThuOpen())) {
			sql.append(",thurs_start_time,thurs_end_time");
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsFriOpen())) {
			sql.append(",fri_start_time,fri_end_time");
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsSatOpen())) {
				sql.append(",sat_start_time,sat_end_time");
		}
		sql.append(")");
		sql.append(" values ").append("(:locationId");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("locationId", locationId);
		if ("Y".equals(apptSysConfig.getDefaultIsSunOpen())) {
		  sql.append(",:sun_start_time, :sun_end_time");
		  paramSource.addValue("sun_start_time", apptSysConfig.getDefaultDayStartTime());
		  paramSource.addValue("sun_end_time", apptSysConfig.getDefaultDayEndTime());
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsMonOpen())) {
			sql.append(",:mon_start_time,:mon_end_time");
			 paramSource.addValue("mon_start_time", apptSysConfig.getDefaultDayStartTime());
			  paramSource.addValue("mon_end_time", apptSysConfig.getDefaultDayEndTime());
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsTueOpen())) {
			sql.append(",:tues_start_time,:tues_end_time");
			 paramSource.addValue("tues_start_time", apptSysConfig.getDefaultDayStartTime());
			  paramSource.addValue("tues_end_time", apptSysConfig.getDefaultDayEndTime());
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsWedOpen())) {
			sql.append(",:wed_start_time, :wed_end_time");
			 paramSource.addValue("wed_start_time", apptSysConfig.getDefaultDayStartTime());
			  paramSource.addValue("wed_end_time", apptSysConfig.getDefaultDayEndTime());
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsThuOpen())) {
			sql.append(",:thurs_start_time,:thurs_end_time");
			 paramSource.addValue("thurs_start_time", apptSysConfig.getDefaultDayStartTime());
			  paramSource.addValue("thurs_end_time", apptSysConfig.getDefaultDayEndTime());
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsFriOpen())) {
			sql.append(",:fri_start_time,:fri_end_time");
			 paramSource.addValue("fri_start_time", apptSysConfig.getDefaultDayStartTime());
			  paramSource.addValue("fri_end_time", apptSysConfig.getDefaultDayEndTime());
		}
		
		if ("Y".equals(apptSysConfig.getDefaultIsSatOpen())) {
			sql.append(",:sat_start_time,:sat_end_time");
			 paramSource.addValue("sat_start_time", apptSysConfig.getDefaultDayStartTime());
			 paramSource.addValue("sat_end_time", apptSysConfig.getDefaultDayEndTime());
		}
		
		sql.append(")");
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource)!=0;
	}

	@Override
	public List<Procedure> getProcedureList(JdbcCustomTemplate jdbcCustomTemplate, boolean isActiveList) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from `procedure` where delete_flag='N' ");
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), procedureMapper());
	}

	private RowMapper<Procedure> procedureMapper() {
		return (rs,i) -> {
			Procedure proc = new Procedure();
			proc.setProcedureId(rs.getInt("id"));
			proc.setProcedureNameOnline(rs.getString("procedure_name_online"));
			return proc;
		};
	}

	@Override
	public boolean addProcedureLocation(JdbcCustomTemplate jdbcCustomTemplate, List<Procedure> procedureList, Integer locationId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into procedure_location (procedure_id,location_id,`enable`) values (:procedureId, :locationId, :enable )");
		
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		MapSqlParameterSource paramSource = null;
		for(Procedure proc : procedureList) {
			paramSource = new MapSqlParameterSource();
			paramSource.addValue("procedureId", proc.getProcedureId());
			paramSource.addValue("locationId", locationId);
			paramSource.addValue("enable", "Y");
			list.add(paramSource);
		}
		
		if(!list.isEmpty()) {
			SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
			SqlParameterSource batchArray[] = list.toArray(mapArray);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		}	
		return true;
	}

	@Override
	public List<Location> getLocationsByServiceIdToCloseServiceStatus(JdbcCustomTemplate jdbcCustomTemplate, Integer serviceId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct l.id,l.location_name_online ");
		sql.append(" from resource_service rs");
		sql.append(" LEFT OUTER JOIN resource r ON r.id=rs.resource_id");
		sql.append(" LEFT OUTER JOIN location l ON l.id=r.location_id");
		sql.append(" LEFT OUTER JOIN service s ON s.id=rs.service_id");
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), locationByServiceIdTOClosedServiceMapper());
	}
	
	public List<AppointmentReportData> getAppointmentReport(JdbcCustomTemplate jdbcCustomTemplate, String fromDate, String toDate, 
			String locationIds, String resourceIds, String serviceIds, String apptStatus) throws Exception {
		
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		List<Integer> locationIdList = null;
		List<Integer> resourceIdList = null;
		List<Integer> serviceIdList = null;
		if(locationIds != null && !"ALL".equals(locationIds) && !"".equals(locationIds)&& !"-1".equals(locationIds)) {
			locationIdList = Arrays.stream(locationIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		} 
		
		if(resourceIds != null && !"ALL".equals(resourceIds) && !"".equals(resourceIds) && !"-1".equals(resourceIds)) {
			resourceIdList = Arrays.stream(resourceIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		} 
		
		if(serviceIds != null && !"ALL".equals(serviceIds) && !"".equals(serviceIds) && !"-1".equals(serviceIds)) {
			serviceIdList = Arrays.stream(serviceIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		} 
		
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("select DATE_FORMAT(sc.appt_date_time,'%m/%d/%Y %h:%i %p') as apptDateTime, IF(sc.walk_in='N','No','Yes') as walkIn,");
		sql.append("concat(r.prefix,' ',r.first_name,' ',r.last_name) as resourceName, ");
		sql.append("l.location_name_online, s.service_name_online, c.account_number, sc.id as scheduleId, DATE_FORMAT(sc.timestamp,'%m/%d/%Y %h:%i %p') as apptTimestamp, "); 
		sql.append("sc.comments, d.department_name_online, sc.resource_record_start_time,sc.resource_record_end_time,sc.frontdesk_record_start_time,sc.frontdesk_record_end_time, sc.payment_amt, ");
		sql.append("c.first_name,c.last_name, IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone, c.email, c.zip_postal as zipCode,"); 
		sql.append("IF(sc.status=11,'CONFIRMED',IF(sc.status=21,'CANCELLED','')) as apptStatus,");
		sql.append("IF(a.appt_method=1,'online',IF(a.appt_method=2,'ivr',IF(a.appt_method=3,'admin',''))) as apptMethod,"); 
		sql.append("IF(sc.accessed='N','Not Assessed','Assessed') as accessed,a.conf_number, c.attrib1");
		sql.append(" from schedule sc, resource r, service s, appointment a, location l, customer c, department d");
		sql.append(" where DATE(sc.timestamp)>=:fromDate and DATE(sc.timestamp)<=:toDate ");
		
		List<Integer> apptStatusList = null;
		if(apptStatus != null && !"".equals(apptStatus)) {
			sql.append(" and sc.status in (:status) ");
			apptStatusList = Arrays.asList(apptStatus.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		} 
		
		
		if(locationIdList != null) {
			sql.append(" and sc.location_id in (:locationIds)");
			parameterSource.addValue("locationIds", locationIdList);
		}
		if(resourceIdList != null) {
			sql.append(" and sc.resource_id in (:resourceIds)");
			parameterSource.addValue("resourceIds", resourceIdList);
		}
		if(serviceIdList != null) {
			sql.append(" and sc.service_id in (:serviceIds)");
			parameterSource.addValue("serviceIds", serviceIdList);
		}
		sql.append(" and a.schedule_id=sc.id and r.id=sc.resource_id and l.id=sc.location_id and s.id=sc.service_id and c.id=sc.customer_id and sc.department_id=d.id ");
		parameterSource.addValue("fromDate", fromDate);
		parameterSource.addValue("toDate", toDate);
		if(apptStatus != null && !"".equals(apptStatus)) {
			parameterSource.addValue("status", apptStatusList);
		}
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, apptReportMapper());
	}

	@Override
	public List<DynamicIncludeReport> getDynamicIncludeReportsData(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select * from dynamic_include_reports where table_name='includeReportNew' order by placement";
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), dynamicIncludeReportMapper());
	}
		
	@Override
	public List<SearchAppointmentData> searchByFirstLastName(JdbcCustomTemplate jdbcCustomTemplate, String firstName, String lastName) throws Exception {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id,c.household_id, c.account_number, c.first_name,c.last_name,IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("c.email, c.zip_postal as zipCode,c.address,c.city,c.state, c.attrib1, c.dob");
		sql.append(" from customer c where 1=1");
		if(firstName != null && !"".equals(firstName)) {
			sql.append(" and c.first_name LIKE :firstName");
			paramSource.addValue("firstName", firstName+'%');
		}
		
		if(lastName != null && !"".equals(lastName)) {
			sql.append(" and c.last_name LIKE :lastName");
			paramSource.addValue("lastName", lastName+'%');
		}
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, apptResultMapper(""));
	}

	@Override
	public List<SearchAppointmentData> searchByConfirmationNumber(JdbcCustomTemplate jdbcCustomTemplate, Long confirmationNumber) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id, c.household_id, c.first_name,c.last_name, IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone, c.email, c.zip_postal as zipCode,");
		sql.append("c.account_number,c.attrib1,c.address,c.city,c.state,c.dob");
		sql.append(",l.location_name_online, concat(r.prefix,' ',r.first_name,' ',r.last_name) as resourceName, ");
		sql.append(" s.service_name_online,  "); 
		sql.append(" DATE_FORMAT(sc.appt_date_time,'%m/%d/%Y %h:%i %p') as apptDateTime,"); 
		sql.append("IF(sc.status=11,'CONFIRMED',IF(sc.status=21,'CANCELLED','')) as apptStatus,");
		sql.append("IF(a.appt_method=1,'online',IF(a.appt_method=2,'ivr',IF(a.appt_method=3,'admin',''))) as apptMethod,"); 
		sql.append("a.conf_number");
		sql.append(" from schedule sc, resource r, service s, appointment a, location l, customer c");
		sql.append(" where a.conf_number=:confirmationNumber ");
		sql.append(" and a.schedule_id=sc.id and r.id=sc.resource_id and l.id=sc.location_id and s.id=sc.service_id and c.id=sc.customer_id ");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("confirmationNumber", confirmationNumber);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, apptResultMapper("INCLUDE_APPT_DATA"));
	}

	@Override
	public List<SearchAppointmentData> searchByAccountNumber(JdbcCustomTemplate jdbcCustomTemplate, String accountNumber) throws Exception {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id,c.household_id, c.account_number, c.first_name,c.last_name,  IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("c.email, c.zip_postal as zipCode,c.attrib1, c.dob,c.address,c.city,c.state");
		sql.append(" from customer c where 1=1");
		if(accountNumber != null && !"".equals(accountNumber)) {
			sql.append(" and c.account_number=:accountNumber");
			paramSource.addValue("accountNumber", accountNumber);
		}
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, apptResultMapper(""));
	}
	
	@Override
	public List<SearchAppointmentData> searchByContactPhone(JdbcCustomTemplate jdbcCustomTemplate, String contactPhone) throws Exception {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id,c.household_id, c.account_number, c.first_name,c.last_name, IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("c.email, c.zip_postal as zipCode,c.attrib1, c.dob,c.address,c.city,c.state");
		sql.append(" from customer c where 1=1");
		if(contactPhone != null && !"".equals(contactPhone)) {
			sql.append(" and c.contact_phone=:contactPhone");
			paramSource.addValue("contactPhone", contactPhone);
		}
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, apptResultMapper(""));
	}
	
	@Override
	public List<SearchAppointmentData> searchByCallerId(JdbcCustomTemplate jdbcCustomTemplate, String callerId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct c.id,c.household_id, c.account_number, c.first_name,c.last_name, IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("c.email, c.zip_postal as zipCode,c.attrib1, c.dob,c.address,c.city,c.state");
		sql.append(" from customer c, main m, schedule sc ");
		sql.append(" where m.caller_id=:callerId ");
		sql.append(" and c.id=sc.customer_id and m.trans_id=sc.trans_id");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("callerId", callerId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, apptResultMapper(""));
	}

	@Override
	public List<SearchAppointmentData> searchByAttrib1(JdbcCustomTemplate jdbcCustomTemplate, String attrib1) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id,c.household_id, c.account_number, c.first_name,c.last_name, IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("c.email, c.zip_postal as zipCode,c.attrib1, c.dob,c.address,c.city,c.state");
		sql.append(" from customer c where ");
		sql.append(" c.attrib1=:attrib1 ");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("attrib1", attrib1);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, apptResultMapper(""));
	}
	
	@Override
	public List<SearchAppointmentData> searchByDOB(JdbcCustomTemplate jdbcCustomTemplate, String dob) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id,c.household_id, c.account_number, c.first_name,c.last_name, IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("c.email, c.zip_postal as zipCode,c.attrib1, c.dob,c.address,c.city,c.state");
		sql.append(" from customer c where ");
		sql.append(" c.dob=:dob ");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("dob", dob);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, apptResultMapper(""));
	}
	
	@Override
	public List<SearchAppointmentData> searchByHouseHoldId(JdbcCustomTemplate jdbcCustomTemplate, Long houseHoldId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id,c.household_id, c.account_number, c.first_name,c.last_name, IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("c.email, c.zip_postal as zipCode,c.attrib1, c.dob,c.address,c.city,c.state");
		sql.append(" from customer c where ");
		sql.append(" c.household_id=:houseHoldId ");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("houseHoldId", houseHoldId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, apptResultMapper(""));
	}
	
	@Override
	public List<SearchAppointmentData> getAppointmentsByCustomerId(JdbcCustomTemplate jdbcCustomTemplate, String timeZone, Long customerId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select l.location_name_online, concat(r.prefix,' ',r.first_name,' ',r.last_name) as resourceName, ");
		sql.append(" s.service_name_online,  "); 
		sql.append(" DATE_FORMAT(sc.appt_date_time,'%m/%d/%Y %h:%i %p') as apptDateTime, sc.id as scheduleId, DATE_FORMAT(sc.timestamp,'%m/%d/%Y %h:%i %p') as apptTimestamp,"); 
		sql.append("IF(sc.status=11,'CONFIRMED',IF(sc.status=21,'CANCELLED','')) as apptStatus,");
		sql.append("IF(a.appt_method=1,'online',IF(a.appt_method=2,'ivr',IF(a.appt_method=3,'admin',''))) as apptMethod,"); 
		sql.append("a.conf_number, IF(sc.appt_date_time > CONVERT_TZ(now(),'US/Central','").append(timeZone).append("'), 'Y','N') as isFutureAppt");
		sql.append(" from schedule sc, resource r, service s, appointment a, location l, customer c");
		sql.append(" where sc.customer_id=:customerId ");
		sql.append(" and a.schedule_id=sc.id and r.id=sc.resource_id and l.id=sc.location_id and s.id=sc.service_id and c.id=sc.customer_id ");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("customerId", customerId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, appointmentMapper());
	}
	
	@Override
	public List<DynamicSearchByFields> getSearchDropDownList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select * from dynamic_search_by_fields order by placement";
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), dynamicSearchByFieldMapper());
	}
	
	private RowMapper<DynamicSearchByFields> dynamicSearchByFieldMapper() {
		return (rs, i) -> {
			DynamicSearchByFields dynamicSearchByFields = new DynamicSearchByFields();
			dynamicSearchByFields.setId(rs.getInt("id"));
			dynamicSearchByFields.setDisplay(rs.getString("display"));
			dynamicSearchByFields.setTitle(rs.getString("title"));
			return dynamicSearchByFields;
		};
	}
	
	@Override
	public List<Customer> getCustomersById(JdbcCustomTemplate jdbcCustomTemplate, Long customerId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id,c.household_id, c.account_number, c.first_name,c.last_name,  IF(contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("IF(c.email IS NOT NULL,c.email,'') as email, IF(c.zip_postal IS NOT NULL,c.zip_postal,'') as zipCode,IF(c.attrib1 IS NOT NULL,c.attrib1,'') as attrib1, IF(c.attrib1 IS NOT NULL,c.dob,'') as dob, IF(c.address IS NOT NULL, c.address,'') as address,");
		sql.append("IF(c.city IS NOT NULL,c.city,'') as city,IF(c.state IS NOT NULL,c.state,'') as state");
		sql.append(" from customer c where ");
		sql.append(" c.id=:customerId ");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("customerId", customerId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, customerMapper(false));
	}
	
	@Override
	public List<CustomerActivity> getCustomerActivities(JdbcCustomTemplate jdbcCustomTemplate, Long customerId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id,c.household_id, c.account_number, c.first_name,c.last_name, IF(contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("IF(c.email IS NOT NULL,c.email,'') as email, IF(c.zip_postal IS NOT NULL,c.zip_postal,'') as zipCode,IF(c.attrib1 IS NOT NULL,c.attrib1,'') as attrib1, IF(c.attrib1 IS NOT NULL,c.dob,'') as dob, IF(c.address IS NOT NULL, c.address,'') as address,");
		sql.append("IF(c.city IS NOT NULL,c.city,'') as city,IF(c.state IS NOT NULL,c.state,'') as state,");
		sql.append(" IF(m.caller_id IS NOT NULL,m.caller_id,'') as caller_id, m.device, sc.updated_by, m.username,IF(m.ip_address IS NOT NULL,m.ip_address,'') as ip_address ");
		sql.append(", l.location_name_online, concat(r.prefix,' ',r.first_name,' ',r.last_name) as resourceName, ");
		sql.append(" s.service_name_online,  "); 
		sql.append(" DATE_FORMAT(sc.appt_date_time,'%m/%d/%Y %h:%i %p') as apptDateTime, DATE_FORMAT(sc.timestamp,'%m/%d/%Y %h:%i %p') as ctimestamp, "); 
		sql.append("IF(sc.status=11,'CONFIRMED',IF(sc.status=21,'CANCELLED','')) as apptStatus,");
		sql.append("IF(a.appt_method=1,'online',IF(a.appt_method=2,'ivr',IF(a.appt_method=3,'admin',''))) as apptMethod,"); 
		sql.append("a.conf_number, IF(sc.comments IS NOT NULL,sc.comments,'') as comments, sc.screened, IF(sc.notes IS NOT NULL,sc.notes,'') as notes, IF(m.uuid IS NOT NULL,m.uuid,'') as uuid,");
		sql.append("DATE_FORMAT(sc.timestamp,'%m/%d/%Y %h:%i %p') as timestamp");
		sql.append(" from schedule sc, resource r, service s, appointment a, location l, customer c,main m where ");
		sql.append(" c.id=:customerId and a.schedule_id=sc.id and r.id=sc.resource_id and l.id=sc.location_id and s.id=sc.service_id and c.id=sc.customer_id and m.trans_id=sc.trans_id and m.trans_id=a.trans_id ");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("customerId", customerId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, customerActivityMapper());
	}
	
	
	@Override
	public List<Customer> getHouseHoldInfo(JdbcCustomTemplate jdbcCustomTemplate, Long customerId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id,c.household_id, c.account_number, c.first_name,c.last_name, IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("IF(c.email IS NOT NULL,c.email,'') as email, IF(c.zip_postal IS NOT NULL,c.zip_postal,'') as zipCode,IF(c.attrib1 IS NOT NULL,c.attrib1,'') as attrib1, IF(c.attrib1 IS NOT NULL,c.dob,'') as dob, IF(c.address IS NOT NULL, c.address,'') as address,");
		sql.append("IF(c.city IS NOT NULL,c.city,'') as city,IF(c.state IS NOT NULL,c.state,'') as state");
		sql.append(" from customer c where ");
		sql.append(" c.household_id=(select household_id from customer cc where id=:customerId)");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("customerId", customerId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, customerMapper(false));
	}
	
	@Override
	public List<AppointmentStatusData> getAppointmentStatusDropDownList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select * from appointmentstatus where delete_flag='N'";
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), appointmentStatusMapper());
	}
	
	@Override
	public List<AppointmentStatusData> getAppointmentStatusReportList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select * from appointment_status_report where report_display='Y' ";
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), appointmentStatusReportMapper(jdbcCustomTemplate));
	}

    public static RowMapper<AppointmentStatusData> appointmentStatusReportMapper(JdbcCustomTemplate jdbcCustomTemplate) {
		return (rs, i) -> {
			AppointmentStatusData apptStatusData = new AppointmentStatusData();
			apptStatusData.setId(rs.getInt("id"));
			apptStatusData.setStatus(rs.getString("column-name"));
			String statusValue = getStatusVal(jdbcCustomTemplate, rs.getString("appointmentstatus_ids"));
			apptStatusData.setStatusValStr(statusValue);
			apptStatusData.setDenied(rs.getString("is_denied"));
			apptStatusData.setReportDisplay(rs.getString("report_display"));
			apptStatusData.setPlaceHolderName(rs.getString("place_holder_name"));
			return apptStatusData;
		};
	}
	
	private static String getStatusVal(JdbcCustomTemplate jdbcCustomTemplate, String apptstatusIds) {
		List<String> idsStrList = Arrays.asList(apptstatusIds.split(","));
		List<Integer> ids = idsStrList.stream().map(Integer::valueOf).collect(Collectors.toList());
		String sql = "select group_concat(status_val) as statusVal from appointmentstatus where id in (:ids) and delete_flag='N'";
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("ids", ids);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(),parameterSource, String.class);
	}
	
	
	@Override
	public List<AppointmentStatusData> getAppointmentStatusReport(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		List<Integer> ids = getDistinctApptStatusIds(jdbcCustomTemplate);
		String sql = "select * from appointmentstatus where delete_flag='N' and id in (:ids) ";
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("ids", ids);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, appointmentStatusMapper());
	}
	
	@Override
	public List<Integer> getDistinctApptStatusIds(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select group_concat(distinct(appointmentstatus_ids)) as ids from appointment_status_report";
		String ids = jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql.toString(), String.class);
		List<Integer> idList = new ArrayList<>();
		if(ids != null && !"".equals(ids)) {
			List<String> idsStrList = Arrays.asList(ids.split(","));
			return idsStrList.stream().map(Integer::valueOf).collect(Collectors.toList());
		}
		return idList;
	}

	@Override
	public boolean mergeHouseHoldId(JdbcCustomTemplate jdbcCustomTemplate, String fromHouseHoldIds, String mergeToHouseHoldId) throws Exception {
		StringBuilder sql = new StringBuilder("update customer set household_id=:mergeToHouseHoldId");
		sql.append(" where household_id in (:fromHouseHoldIds)");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("mergeToHouseHoldId", mergeToHouseHoldId);
		paramSource.addValue("fromHouseHoldIds", Arrays.asList(fromHouseHoldIds.split(",")));
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource) != 0;
	}

	@Override
	public Long getNextHouseHoldId(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql ="SELECT nextval('sq_my_sequence') as next_sequence";
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, Long.class);
	}

	@Override
	public boolean splitHouseHoldId(JdbcCustomTemplate jdbcCustomTemplate, String customerIds, String newHouseHoldId) throws Exception {
		StringBuilder sql = new StringBuilder("update customer set household_id=:houseHoldId");
		sql.append(" where id in (:customerIds)");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("houseHoldId", newHouseHoldId);
		paramSource.addValue("customerIds", Arrays.asList(customerIds.split(",")));
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource) != 0;
	}

	@Override
	public List<Customer> getBlockedCustomers(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id, c.account_number, c.first_name,c.last_name,IF(c.contact_phone IS NOT NULL,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone,");
		sql.append("c.email,c.blocked_flag");
		sql.append(" from customer c where c.blocked_flag='Y'");
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), customerMapper(true));
	}

	@Override
	public boolean updateCustomerBlockedReason(JdbcCustomTemplate jdbcCustomTemplate, Long customerId, String reasonMessage) throws Exception {
		String sql = "update customer set attrib20=? where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{reasonMessage, customerId}) != 0;
	}

	@Override
	public boolean unBlockCustomer(JdbcCustomTemplate jdbcCustomTemplate, Long customerId) throws Exception {
		String sql = "update customer set blocked_flag='N' where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{customerId}) != 0;
	}	
	
	@Deprecated
	@Override
	public Map<Customer, List<AppointmentData>> searchAppointmentsByFirstLastName(JdbcCustomTemplate jdbcCustomTemplate, String firstName, String lastName) throws Exception {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		sql.append("select c.id, c.household_id, c.first_name,c.last_name, CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)) as contact_phone, c.email, c.zip_postal as zipCode,");
		sql.append("c.account_number,c.attrib1,c.address,c.city,c.state,c.dob");
		sql.append(",l.location_name_online, concat(r.prefix,' ',r.first_name,' ',r.last_name) as resourceName, ");
		sql.append(" s.service_name_online,  "); 
		sql.append(" DATE_FORMAT(sc.appt_date_time,'%m/%d/%Y %h:%i %p') as apptDateTime,"); 
		sql.append("IF(sc.status=11,'CONFIRMED',IF(sc.status=21,'CANCELLED','')) as apptStatus,");
		sql.append("IF(a.appt_method=1,'online',IF(a.appt_method=2,'ivr',IF(a.appt_method=3,'admin',''))) as apptMethod,"); 
		sql.append("a.conf_number");
		sql.append(" from schedule sc, resource r, service s, appointment a, location l, customer c where 1=1 ");
		if(firstName != null && !"".equals(firstName)) {
			sql.append(" and c.first_name LIKE :firstName");
			parameterSource.addValue("firstName", firstName+'%');
		}
		
		if(lastName != null && !"".equals(lastName)) {
			sql.append(" and c.last_name LIKE :lastName");
			parameterSource.addValue("lastName", lastName+'%');
		}
		sql.append(" and a.schedule_id=sc.id and r.id=sc.resource_id and l.id=sc.location_id and s.id=sc.service_id and c.id=sc.customer_id ");
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, new ResultSetExtractor<Map<Customer, List<AppointmentData>>>() {
			Map<Customer, List<AppointmentData>> apptSearchMap = new HashMap<>();
            @Override
            public Map<Customer, List<AppointmentData>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Customer customer;
                AppointmentData appointmentData;
                while (rs.next()) {
                    customer = new Customer();
                    appointmentData = new AppointmentData();
                    customer.setCustomerId(rs.getLong("id"));
                    if(apptSearchMap.containsKey(customer)) {
                    	List<AppointmentData> appointmentDataList = apptSearchMap.get(customer);
                    	
                    	appointmentData.setApptDateTime(rs.getString("apptDateTime"));
        				appointmentData.setLocationName(rs.getString("location_name_online"));
        				appointmentData.setResourceName(rs.getString("resourceName"));
        				appointmentData.setServiceName(rs.getString("service_name_online"));
        				appointmentData.setApptStatus(rs.getString("apptStatus"));
        				appointmentData.setApptMethod(rs.getString("apptMethod"));
        				appointmentData.setConfirmNumber(rs.getLong("conf_number"));
        				appointmentDataList.add(appointmentData);
                    } else {
                    	customer.setSsn(rs.getString("account_number"));
            			customer.setFirstName(rs.getString("first_name"));
            			customer.setLastName(rs.getString("last_name"));
            			customer.setContactPhone(rs.getString("contact_phone"));
            			customer.setEmail(rs.getString("email"));
            			customer.setHouseHoldId(rs.getLong("household_id"));
        				customer.setDob(rs.getString("dob"));
        				customer.setAddress(rs.getString("address"));
        				customer.setCity(rs.getString("city"));
        				customer.setState(rs.getString("state"));
        				customer.setZipCode(rs.getString("zipCode"));
        				customer.setAttrib1(rs.getString("attrib1"));
        				
        				appointmentData.setApptDateTime(rs.getString("apptDateTime"));
        				appointmentData.setLocationName(rs.getString("location_name_online"));
        				appointmentData.setResourceName(rs.getString("resourceName"));
        				appointmentData.setServiceName(rs.getString("service_name_online"));
        				appointmentData.setApptStatus(rs.getString("apptStatus"));
        				appointmentData.setApptMethod(rs.getString("apptMethod"));
        				appointmentData.setConfirmNumber(rs.getLong("conf_number"));
        				List<AppointmentData> appointmentDataList = new ArrayList<>();
        				appointmentDataList.add(appointmentData);
        				apptSearchMap.put(customer, appointmentDataList);
                    }
                }
                return apptSearchMap;
            }
        });
	}

	@Deprecated
	@Override
	public Map<Customer, List<AppointmentData>> searchAppointmentsByAccountNumber(JdbcCustomTemplate jdbcCustomTemplate, String accountNumber) throws Exception {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		sql.append("select c.id, c.household_id, c.first_name,c.last_name, CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)) as contact_phone, c.email, c.zip_postal as zipCode,");
		sql.append("c.account_number,c.attrib1,c.address,c.city,c.state,c.dob");
		sql.append(",l.location_name_online, concat(r.prefix,' ',r.first_name,' ',r.last_name) as resourceName, ");
		sql.append(" s.service_name_online,  "); 
		sql.append(" DATE_FORMAT(sc.appt_date_time,'%m/%d/%Y %h:%i %p') as apptDateTime,"); 
		sql.append("IF(sc.status=11,'CONFIRMED',IF(sc.status=21,'CANCELLED','')) as apptStatus,");
		sql.append("IF(a.appt_method=1,'online',IF(a.appt_method=2,'ivr',IF(a.appt_method=3,'admin',''))) as apptMethod,"); 
		sql.append("a.conf_number");
		sql.append(" from schedule sc, resource r, service s, appointment a, location l, customer c where 1=1 ");
		if(accountNumber != null && !"".equals(accountNumber)) {
			sql.append(" and c.account_number=:accountNumber");
			parameterSource.addValue("accountNumber", accountNumber);
		}
		sql.append(" and a.schedule_id=sc.id and r.id=sc.resource_id and l.id=sc.location_id and s.id=sc.service_id and c.id=sc.customer_id ");
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, new ResultSetExtractor<Map<Customer, List<AppointmentData>>>() {
			Map<Customer, List<AppointmentData>> apptSearchMap = new HashMap<>();
            @Override
            public Map<Customer, List<AppointmentData>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Customer customer;
                AppointmentData appointmentData;
                while (rs.next()) {
                    customer = new Customer();
                    appointmentData = new AppointmentData();
                    customer.setCustomerId(rs.getLong("id"));
                    if(apptSearchMap.containsKey(customer)) {
                    	List<AppointmentData> appointmentDataList = apptSearchMap.get(customer);
                    	
                    	appointmentData.setApptDateTime(rs.getString("apptDateTime"));
        				appointmentData.setLocationName(rs.getString("location_name_online"));
        				appointmentData.setResourceName(rs.getString("resourceName"));
        				appointmentData.setServiceName(rs.getString("service_name_online"));
        				appointmentData.setApptStatus(rs.getString("apptStatus"));
        				appointmentData.setApptMethod(rs.getString("apptMethod"));
        				appointmentData.setConfirmNumber(rs.getLong("conf_number"));
        				appointmentDataList.add(appointmentData);
                    } else {
                    	customer.setSsn(rs.getString("account_number"));
            			customer.setFirstName(rs.getString("first_name"));
            			customer.setLastName(rs.getString("last_name"));
            			customer.setContactPhone(rs.getString("contact_phone"));
            			customer.setEmail(rs.getString("email"));
            			customer.setHouseHoldId(rs.getLong("household_id"));
        				customer.setDob(rs.getString("dob"));
        				customer.setAddress(rs.getString("address"));
        				customer.setCity(rs.getString("city"));
        				customer.setState(rs.getString("state"));
        				customer.setZipCode(rs.getString("zipCode"));
        				customer.setAttrib1(rs.getString("attrib1"));
        				
        				appointmentData.setApptDateTime(rs.getString("apptDateTime"));
        				appointmentData.setLocationName(rs.getString("location_name_online"));
        				appointmentData.setResourceName(rs.getString("resourceName"));
        				appointmentData.setServiceName(rs.getString("service_name_online"));
        				appointmentData.setApptStatus(rs.getString("apptStatus"));
        				appointmentData.setApptMethod(rs.getString("apptMethod"));
        				appointmentData.setConfirmNumber(rs.getLong("conf_number"));
        				List<AppointmentData> appointmentDataList = new ArrayList<>();
        				appointmentDataList.add(appointmentData);
        				apptSearchMap.put(customer, appointmentDataList);
                    }
                }
                return apptSearchMap;
            }
        });
	}
	
	private RowMapper<TransState> transStateMapper() {
		return (rs, i) -> {
			TransState transState = new TransState();
			transState.setTransStateId(rs.getLong("id"));
			transState.setTimestamp(rs.getString("timestamp"));
			transState.setState(rs.getInt("state"));
			return transState;
		};
	}
	
	@Override
	public List<TransState> getTransStateList(JdbcCustomTemplate jdbcCustomTemplate, Long transId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, DATE_FORMAT(timestamp,'%m/%d/%Y %h:%i %p') as timestamp, state from trans_state where trans_id=? and state !=-1 order by timestamp");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{transId}, transStateMapper());
	}
	
	
	
	@Override
	public Map<String, List<JSPPagesPrivileges>> getPrivilegeSettings(JdbcCustomTemplate jdbcCustomTemplate, String accessPrivilegeName) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from jsp_pages_privileges");
		sql.append(" where enable_flag='Y'");
		if(accessPrivilegeName.equalsIgnoreCase("administrator")) {
			sql.append(" and administrator='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("manager")) {
			sql.append(" and manager='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("location")) {
			sql.append(" and location='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("provider")) {
			sql.append(" and provider='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("scheduler")) {
			sql.append(" and scheduler='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("read_only")) {
			sql.append(" and read_only='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("super-user")) {
			sql.append(" and super_user='Y'");
		}
		List<JSPPagesPrivileges> jspPagePrivileges = jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), privilegeSettingMapper());
		Map<String, List<JSPPagesPrivileges>> pageMap = new LinkedHashMap<>();
		for(JSPPagesPrivileges jspPagePrivilege : jspPagePrivileges) {
			if(pageMap.containsKey(jspPagePrivilege.getGroupTitle())) {
				List<JSPPagesPrivileges> jspPagesPrivileges = pageMap.get(jspPagePrivilege.getGroupTitle());
				jspPagesPrivileges.add(jspPagePrivilege);
				pageMap.put(jspPagePrivilege.getGroupTitle(), jspPagesPrivileges);
			} else {
				List<JSPPagesPrivileges> jspPagesPrivileges = new ArrayList<>();
				jspPagesPrivileges.add(jspPagePrivilege);
				pageMap.put(jspPagePrivilege.getGroupTitle(), jspPagesPrivileges);
			}
		}
		return pageMap;
	}
	
	
	@Override
	public TablePrintViewResponse getTablePrintViewData(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, String resourceIds, String date) throws Exception {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		
		List<Integer> resourceIdList = new ArrayList<>(); 
		if(resourceIds != null && !"".equals(resourceIds)) {
			resourceIdList = Arrays.asList(resourceIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		}
		sql.append("select DATE_FORMAT(sc.appt_date_time, '%h:%m %p') as time, l.id as locationId, sc.resource_id, concat(r.prefix,' ', r.first_name,' ', r.last_name) as resourceName");
		sql.append(",CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)) as contact_phone");
		sql.append(",c.account_number, p.procedure_name_online as zipCode ");
		sql.append(",s.service_name_online, IF(n.notify_status=1 or 0,'Pending',IF(n.notify_status=2,'In Progress',IF(n.notify_status=3,'Completed',''))) as notify_status, n.notify_status, c.first_name, c.last_name,c.state ");
		sql.append(" from schedule sc ");
		sql.append(" LEFT OUTER JOIN customer c on c.id=sc.customer_id ");
		sql.append(" LEFT OUTER JOIN notify n on n.schedule_id=sc.id ");
		
		sql.append(" LEFT OUTER JOIN location l on l.id=sc.location_id");
		sql.append(" LEFT OUTER JOIN `procedure` p on p.id=sc.procedure_id");
		sql.append(" LEFT OUTER JOIN service s on s.id=sc.service_id ");
		sql.append(" LEFT OUTER JOIN resource r on r.id=sc.resource_id ");
		sql.append(" LEFT OUTER JOIN appointment a on a.schedule_id=sc.id ");
		sql.append(" where DATE(sc.appt_date_time)=:dateValue and sc.status=11 and sc.location_id=:locationId and sc.resource_id in (:resourceIds) ");
		
		paramSource.addValue("dateValue", date);
		paramSource.addValue("locationId", locationId);
		paramSource.addValue("resourceIds", resourceIdList);
		TablePrintViewResponse tablePrintViewResponse = new TablePrintViewResponse();
		tablePrintViewResponse.setDynamicFieldLabels(getDynamicFieldLabelData(jdbcCustomTemplate, "table_print_view"));
		Map<BasicTablePrintData, List<TablePrintAppointmentData>> tablePrintViewMap = new LinkedHashMap<>();
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, new ResultSetExtractor<TablePrintViewResponse>() {
            @Override
            public TablePrintViewResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
            	
            	
            	BasicTablePrintData basicTablePrintData;
                TablePrintAppointmentData tablePrintAppointmentData;
                while (rs.next()) {
                	basicTablePrintData = new BasicTablePrintData();
                	tablePrintViewResponse.setLocationId(rs.getInt("locationId"));
                	basicTablePrintData.setResourceId(rs.getLong("resource_id"));
                	basicTablePrintData.setResourceName(rs.getString("resourceName"));
                	tablePrintAppointmentData = new TablePrintAppointmentData();
                	tablePrintAppointmentData.setTime(rs.getString("time"));
                	tablePrintAppointmentData.setServiceName(rs.getString("service_name_online"));
                	tablePrintAppointmentData.setNotificationStatus(rs.getString("notify_status"));
                	tablePrintAppointmentData.setFirstName(rs.getString("first_name"));
                	tablePrintAppointmentData.setLastName(rs.getString("last_name"));
                	tablePrintAppointmentData.setState(rs.getString("state"));
                	tablePrintAppointmentData.setAccountNumber(rs.getString("account_number"));
                	tablePrintAppointmentData.setContactPhone(rs.getString("contact_phone"));
                	tablePrintAppointmentData.setZipCode(rs.getString("zipCode"));

                    if(tablePrintViewMap.containsKey(basicTablePrintData)) {
                    	List<TablePrintAppointmentData> list = tablePrintViewMap.get(basicTablePrintData);
                    	list.add(tablePrintAppointmentData);
                    	
                    } else {
                    	List<TablePrintAppointmentData> tablePrintViewDataList = new ArrayList<>();
                    	tablePrintViewDataList.add(tablePrintAppointmentData);
                    	tablePrintViewMap.put(basicTablePrintData, tablePrintViewDataList);
                    }
                }
                
                // to set the total booked appts.
                Set<BasicTablePrintData> sets =  tablePrintViewMap.keySet();
                Iterator<BasicTablePrintData> it = sets.iterator();
                while(it.hasNext()) {
                	BasicTablePrintData key = it.next();
                	key.setTotalBookedAppts((long)tablePrintViewMap.get(key).size());
                }
                
                tablePrintViewResponse.setTablePrintViewData(tablePrintViewMap);
                return tablePrintViewResponse;
            }
           
        });
	}
	
	@Override
	public boolean isHoliday(JdbcCustomTemplate jdbcCustomTemplate, String date) throws Exception {
		String sql = "select count(1) from holidays where date=?";
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, new Object[]{date}, Integer.class) > 0;
	}
	
	@Override
	public boolean isClosedDays(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, String date) throws Exception {
		String sql = "select count(1) from closed_days where date=? and location_id=?";
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, new Object[]{date,locationId}, Integer.class) > 0;
	}
	
	
	@Override
	public StatisticsReportResult getLocationServiceStatisticsData(JdbcCustomTemplate jdbcCustomTemplate,String fromDate, String toDate) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT s.location_id,loc.location_name_online,s.service_id as id,ser.service_name_online as name,s.status,COUNT(s.service_id) AS apptCount ");
		sql.append(" FROM schedule s,service ser,location loc ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotEmpty(fromDate)) {
			sql.append(" and date(s.appt_date_time) >=:fromDate");
		}
		
		if (StringUtils.isNotEmpty(toDate)) {
			sql.append("  and DATE(s.appt_date_time) <=:toDate");
		}
		sql.append(" and s.status not in (1,2,21) ");
		sql.append(" and s.service_id=ser.id and s.location_id=loc.id and s.status not in (21,1,2) ");
		sql.append(" GROUP BY s.location_id,s.service_id,s.status ");
		sql.append(" order by loc.location_name_online,ser.service_name_online,s.status ");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("fromDate", fromDate);
		paramSource.addValue("toDate", toDate);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, new ResultSetExtractor<StatisticsReportResult>() {
			StatisticsReportResult statisticsResportResult = new StatisticsReportResult();
            @Override
            public StatisticsReportResult extractData(ResultSet rs) throws SQLException, DataAccessException {
            	Map<String, StatisticReport> reportMap = new LinkedHashMap<>();
            	StatisticReport statisticReport = null;
            	Integer totalAppts = 0;
            	while(rs.next()) {
            		Integer locationId= rs.getInt("location_id");
            		String locationName = rs.getString("location_name_online");
            		Integer id = rs.getInt("id"); // serviceId
            		String key = locationId+"_"+id;
            		String name = rs.getString("name"); // serviceName
            		Integer apptStatus=rs.getInt("status");
            		Long apptCount = rs.getLong("apptCount");
	            	if(reportMap.containsKey(key)) {
	    				statisticReport = reportMap.get(key);
	    				if(apptStatus.intValue() == 11) {
	    					statisticReport.setNoOfBookedAppts(statisticReport.getNoOfBookedAppts().intValue() + apptCount.intValue());
	    				} else {
	    					statisticReport.setNoOfOtherAppts(statisticReport.getNoOfOtherAppts() == null?0:statisticReport.getNoOfOtherAppts().intValue() + apptCount.intValue());
	    				}
	    				statisticReport.setTotalNoOfAppts(statisticReport.getTotalNoOfAppts().longValue()+apptCount.longValue());
	    			} else {
	    				statisticReport = new StatisticReport();
	    				statisticReport.setName(name);
	    				statisticReport.setLocationName(locationName);
	    				if(apptStatus.intValue() == 11) {
	    					statisticReport.setNoOfBookedAppts(apptCount.intValue());
	    				} else {
	    					statisticReport.setNoOfOtherAppts(apptCount.intValue());
	    				}
	    				statisticReport.setTotalNoOfAppts(apptCount.longValue());
	    				reportMap.put(key, statisticReport);
	    			}
	            	totalAppts = totalAppts + apptCount.intValue();
            	}
    			
    			List<StatisticReport> result = reportMap.entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList());
    			statisticsResportResult.setTotalNoOfAppts(totalAppts);
    			statisticsResportResult.setStatisticsReportList(result);
               return statisticsResportResult;
            }
        });
	}
	
	@Override
	public SummaryReportResponse getSummaryStatisticReportData(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, Integer serviceId, String fromDate, String toDate, 
			String reportCategory, List<Integer> apptStatusList, SummaryReportResponse summaryReportResponse) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DATE_FORMAT(s.appt_date_time,'%b') as apptMonthName, ");
		if("D".equalsIgnoreCase(reportCategory)){
			sql.append(" DAY(s.appt_date_time) as `key` , ");
		} else if("W".equalsIgnoreCase(reportCategory)){
			sql.append(" CONCAT('Week ' ,WEEK(s.appt_date_time)) as `key`, ");
		} else if("M".equalsIgnoreCase(reportCategory)){
			sql.append(" MONTHNAME(s.appt_date_time) as `key`, ");
		} else if("Q".equals(reportCategory)) {
			sql.append(" IF(QUARTER(DATE(s.appt_date_time))=1, 'Jan-Mar', IF(QUARTER(DATE(s.appt_date_time))=2,'Apr-Jun', IF(QUARTER(DATE(s.appt_date_time))=3,'July-Sep','Oct-Dec'))) as `key`, YEAR(date(s.appt_date_time)) as year, ");
		} else if("Y".equals(reportCategory)) {
			sql.append(" YEAR(date(s.appt_date_time)) as `key`, ");
		} else if("D".equals(reportCategory)){
			sql.append(" DAY(s.appt_date_time) as `key`, ");
		} else {
			sql.append(" MONTHNAME(s.appt_date_time) as `key`, ");
		}
		sql.append(" s.status, COUNT(*) as apptCount FROM schedule s ");
		sql.append(" WHERE 1=1 ");
	
		if (StringUtils.isNotEmpty(fromDate)) {
			sql.append(" and date(s.appt_date_time) >=:fromDate");
		}
		
		if (StringUtils.isNotEmpty(toDate)) {
			sql.append("  and DATE(s.appt_date_time) <=:toDate");
		}
		
		sql.append(" and s.status in (:apptStatus)");
		if (locationId > 0) {
			sql.append(" and s.location_id= :locationId");
		}
		
		if (serviceId > 0) {
			sql.append(" and s.service_id =:serviceId");
		}
		
		sql.append(" GROUP BY ");
		if("D".equalsIgnoreCase(reportCategory)){
			sql.append(" DATE");
		} else if("W".equalsIgnoreCase(reportCategory)){
			sql.append(" WEEK");
		} else if("M".equalsIgnoreCase(reportCategory)){
			sql.append(" MONTH");
		} else if("D".equals(reportCategory)){
			sql.append(" DATE");
		} else if("Q".equals(reportCategory)) {
			sql.append(" QUARTER");
		} else if("Y".equals(reportCategory)) {
			sql.append(" YEAR");
		} else {
			sql.append(" MONTH");
		}
		
		sql.append(" (s.appt_date_time),s.status ");
		sql.append("  order by s.appt_date_time, s.status ");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("fromDate", fromDate);
		paramSource.addValue("toDate", toDate);
		if (locationId > 0) {
			paramSource.addValue("locationId", locationId);
		}
		if(serviceId > 0) {
			paramSource.addValue("serviceId", serviceId);
		}
	
		paramSource.addValue("apptStatus", apptStatusList);
		
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, new ResultSetExtractor<SummaryReportResponse>() {
            @Override
            public SummaryReportResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
            	Map<String,SummaryStatisticsResult> statisticsReportSummaryMap = new LinkedHashMap<>();
            	Map<Integer,Integer> apptStatusWithApptCount = null;
            	SummaryStatisticsResult summaryReportResult = null;
            	int summaryTotalNoOfAppts = 0;
            	while(rs.next()) {
            		String apptMonthName = rs.getString("apptMonthName");
            		String key = rs.getString("key");
            		Integer apptCount = rs.getInt("apptCount");
            		Integer apptStatus = rs.getInt("status");
            		summaryReportResult = statisticsReportSummaryMap.get(key);
            		
    				if(summaryReportResult == null){
    					summaryReportResult = new SummaryStatisticsResult();
    					if("D".equalsIgnoreCase(reportCategory)){
    						summaryReportResult.setDay(key);
    						summaryReportResult.setMonthName(apptMonthName);
    					} else if("W".equalsIgnoreCase(reportCategory)){
    						summaryReportResult.setWeek(key);
    						summaryReportResult.setMonthName(apptMonthName);
    					} else if("M".equalsIgnoreCase(reportCategory)){
    						summaryReportResult.setMonth(key);
    						summaryReportResult.setMonthName(apptMonthName);
    					} else if("D".equalsIgnoreCase(reportCategory)){
    						summaryReportResult.setDay(key);
    						summaryReportResult.setMonthName(apptMonthName);
    					} else if("Q".equalsIgnoreCase(reportCategory)){
    						summaryReportResult.setQuarter(key);
    						summaryReportResult.setYear(rs.getString("year"));
    					} else if("Y".equalsIgnoreCase(reportCategory)){
    						summaryReportResult.setYear(key);
    					}else{
    						summaryReportResult.setMonth(key);
    						summaryReportResult.setMonthName(apptMonthName);
    					}
    					
    					apptStatusWithApptCount = new HashMap<>();
    					summaryReportResult.setApptStatusWithApptCount(apptStatusWithApptCount);
    				}
    				apptStatusWithApptCount = summaryReportResult.getApptStatusWithApptCount();
    				summaryTotalNoOfAppts = summaryTotalNoOfAppts + apptCount;
    				summaryReportResult.setTotalAppointments(summaryReportResult.getTotalAppointments()+ apptCount);
    				apptStatusWithApptCount.put(apptStatus,apptCount);
    				summaryReportResult.setApptStatusWithApptCount(apptStatusWithApptCount);
    				statisticsReportSummaryMap.put(key,summaryReportResult);
            	}
            	
            	
            	List<SummaryStatisticsResult> summaryStatisticsResultList = statisticsReportSummaryMap.entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList());
            	summaryReportResponse.setSummaryStatisticsResults(summaryStatisticsResultList);
            	summaryReportResponse.setSummaryTotalNoOfAppts(summaryTotalNoOfAppts);
            	return summaryReportResponse;
            }
        });
		
	}
	
	
	public StatisticsReportResult getStatisticsData(JdbcCustomTemplate jdbcCustomTemplate,String fromDate, String toDate,String summaryReportFor) {
		StringBuilder sql = new StringBuilder();
		if("procedure".equals(summaryReportFor)){
			sql.append(" SELECT s.procedure_id as id,p.procedure_name_online as name,s.status,COUNT(s.procedure_id) AS apptCount ");
			sql.append(" FROM schedule s,`procedure` p ");
		}else if("department".equals(summaryReportFor)){
			sql.append(" SELECT s.department_id as id,d.department_name_online as name,s.status,COUNT(s.department_id) AS apptCount ");
			sql.append(" FROM schedule s,department d ");
		}else if("location".equals(summaryReportFor)){
			sql.append(" SELECT s.location_id as id,l.location_name_online as name,s.status,COUNT(s.location_id) AS apptCount ");
			sql.append(" FROM schedule s,location l ");
		}else if("resource".equals(summaryReportFor)){
			sql.append(" SELECT s.resource_id as id,TRIM(CONCAT(IF(IFNULL(r.prefix,'')!='',concat(r.prefix,' '),' '), r.first_name, ' ', r.last_name)) as name,s.status,COUNT(s.resource_id) AS apptCount ");
			sql.append(" FROM schedule s,resource r ");
		}else if("service".equals(summaryReportFor)){
			sql.append(" SELECT s.service_id as id,ser.service_name_online as name,s.status,COUNT(s.service_id) AS apptCount ");
			sql.append(" FROM schedule s,service ser ");
		}
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotEmpty(fromDate)) {
			sql.append(" and date(s.appt_date_time) >=:fromDate");
		}
		
		if (StringUtils.isNotEmpty(toDate)) {
			sql.append("  and DATE(s.appt_date_time) <=:toDate");
		}
		sql.append(" and s.status not in (1,2,21) ");
		
		if("procedure".equals(summaryReportFor)){
			sql.append(" and s.procedure_id=p.id ");
			sql.append(" GROUP BY s.procedure_id,s.status ");
			sql.append(" order by p.procedure_name_online,s.status ");
		}else if("department".equals(summaryReportFor)){
			sql.append(" and s.department_id=d.id ");
			sql.append(" GROUP BY s.department_id,s.status ");
			sql.append(" order by d.department_name_online,s.status ");
		}else if("location".equals(summaryReportFor)){
			sql.append(" and s.location_id=l.id ");
			sql.append(" GROUP BY s.location_id,s.status ");
			sql.append(" order by l.location_name_online,s.status ");
		}else if("resource".equals(summaryReportFor)){
			sql.append(" and s.resource_id=r.id ");
			sql.append(" GROUP BY s.resource_id,s.status ");
			sql.append(" order by r.first_name,s.status ");
		}else if("service".equals(summaryReportFor)){
			sql.append(" and s.service_id=ser.id ");
			sql.append(" GROUP BY s.service_id,s.status ");
			sql.append(" order by ser.service_name_online,s.status ");
		}
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("fromDate", fromDate);
		paramSource.addValue("toDate", toDate);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, new ResultSetExtractor<StatisticsReportResult>() {
			StatisticsReportResult statisticsResportResult = new StatisticsReportResult();
            @Override
            public StatisticsReportResult extractData(ResultSet rs) throws SQLException, DataAccessException {
            	Map<Integer, StatisticReport> reportMap = new LinkedHashMap<>();
            	StatisticReport statisticReport = null;
            	Integer totalAppts = 0;
            	while(rs.next()) {
            		Integer id = rs.getInt("id");
            		String name = rs.getString("name");
            		Integer apptStatus=rs.getInt("status");
            		Long apptCount = rs.getLong("apptCount");
	            	if(reportMap.containsKey(id)) {
	    				statisticReport = reportMap.get(id);
	    				if(apptStatus.intValue() == 11) {
	    					statisticReport.setNoOfBookedAppts(statisticReport.getNoOfBookedAppts().intValue() + apptCount.intValue());
	    				} else {
	    					statisticReport.setNoOfOtherAppts(statisticReport.getNoOfOtherAppts() == null?0:statisticReport.getNoOfOtherAppts().intValue() + apptCount.intValue());
	    				}
	    				statisticReport.setTotalNoOfAppts(statisticReport.getTotalNoOfAppts().longValue()+apptCount.longValue());
	    			} else {
	    				statisticReport = new StatisticReport();
	    				statisticReport.setName(name);
	    				if(apptStatus.intValue() == 11) {
	    					statisticReport.setNoOfBookedAppts(apptCount.intValue());
	    				} else {
	    					statisticReport.setNoOfOtherAppts(apptCount.intValue());
	    				}
	    				statisticReport.setTotalNoOfAppts(apptCount.longValue());
	    				reportMap.put(id, statisticReport);
	    			}
	            	totalAppts = totalAppts + apptCount.intValue();
            	}
    			
    			List<StatisticReport> result = reportMap.entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList());
    			statisticsResportResult.setTotalNoOfAppts(totalAppts);
    			statisticsResportResult.setStatisticsReportList(result);
               return statisticsResportResult;
            }
        });
	}
	
	public void logTimeTaken(String spName, long startTime, long endTime) {
		long timeTaken = (endTime - startTime)/1000;
		if(timeTaken >= 3) {
			System.out.println(spName+" Time taken:: "+timeTaken);
		}
	}

	@Override
    public void getI18nEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception {
        String sql = "select lang,message_key,message_value from i18n_email_templates";
        jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Map<String, String>>>() {
            @Override
            public Map<String, Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, String> subMap = null;
                StringBuilder key = new StringBuilder();
                while (rs.next()) {
                    key.append(rs.getString("lang"));
                    if (map.containsKey(key.toString())) {
                        subMap = map.get(key.toString());
                        subMap.put(rs.getString("message_key"), rs.getString("message_value"));
                    } else {
                        subMap = new HashMap<String, String>();
                        subMap.put(rs.getString("message_key"), rs.getString("message_value"));
                        map.put(key.toString(), subMap);
                    }
                    key.setLength(0);
                }
                return map;
            }
        });
    }
	
	public void cancelAppointment(JdbcCustomTemplate jdbcCustomTemplate, Long scheduleId, Integer cancelMethod, String langCode,ClientDeploymentConfig cdConfig, CancelAppointResponse cancelAppointResponse) throws Exception {
		try {
			String spName = "cancel_appointment_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
			logger.info("cancel_appointment_sp input params: ScheduleId: "+scheduleId+" , BlockedTimeInMins:"+cdConfig.getBlockTimeInMins());
			Map<String,Object> inParameters = new HashMap<String,Object>();
			inParameters.put(SPConstants.SCHEDULE_ID.getValue(), scheduleId);
			inParameters.put(SPConstants.CANCEL_METHOD.getValue(), cancelMethod);
			inParameters.put(SPConstants.LANG_CODE.getValue(), langCode);
			inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());
			long startTime = System.currentTimeMillis();
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
			long endTime = System.currentTimeMillis();
			logTimeTaken(spName, startTime, endTime);
			
			Object success = simpleJdbcCallResult.get(SPConstants.SUCCESS.getValue());
			Object displayKeys = simpleJdbcCallResult.get(SPConstants.DISPLAY_KEYS.getValue());
			Object displayValues = simpleJdbcCallResult.get(SPConstants.DISPLAY_VALUES.getValue());
			Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
			String errorMessage = (errorMsg!=null)?(String)errorMsg:"";
			if(success != null) {
				if("Y".equals((String)success)) {
					cancelAppointResponse.setCancelled(true);
					cancelAppointResponse.setDisplayKeys((displayKeys!=null)?(String)displayKeys:"");
					cancelAppointResponse.setDisplayValues(displayValues!=null?(String)displayValues:"");
				} else {
					cancelAppointResponse.setCancelled(false);
					cancelAppointResponse.setMessage(errorMessage);
				}
			} else {
				logger.error("Error from book appointment storedprocedure:"+errorMessage);
			}
		}  catch(DataAccessException dae) {
			StringBuilder inputData = new StringBuilder();
			inputData.append("scheduleId: [ ").append(scheduleId).append(" ] ").append(",");
			inputData.append("langCode:[ ").append(langCode).append(" ]");
			throw new TelAppointException(ErrorConstants.ERROR_2026.getCode(), ErrorConstants.ERROR_2026.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), inputData.toString());
		}
	}
	
	@Override
	public boolean updateAppointmentStatus(JdbcCustomTemplate jdbcCustomTemplate, long scheduleId, int status, String userName) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("update schedule set status=:status, updated_by=IF(updated_by IS NOT NULL, CONCAT(updated_by,', status chg:"+userName+"'),'"+userName+"') where id=:scheduleId");
		MapSqlParameterSource paramSource =  new MapSqlParameterSource();
		paramSource.addValue("status", status);
		paramSource.addValue("scheduleId", scheduleId);
		
		
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus tStatus = dsTransactionManager.getTransaction(def);
		try {
			boolean updateScheduleStatus = jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource) !=0;
			boolean updatedCustomerBlocked = isBlockedFromFutureAppts(jdbcCustomTemplate, status)?updateCustomerBlockStatus(jdbcCustomTemplate, scheduleId):true;
			if(updatedCustomerBlocked && updateScheduleStatus) {
				dsTransactionManager.commit(tStatus);
				return true;
			} else {
				dsTransactionManager.rollback(tStatus);
			}
		} catch(DataAccessException dae) {
			dsTransactionManager.rollback(tStatus);
		}
		return false;
	}
	

	@Override
	public boolean isBlockedFromFutureAppts(JdbcCustomTemplate jdbcCustomTemplate, int status) throws Exception {
		List<AppointmentStatusData> apptStatusList = getAppointmentStatusDropDownList(jdbcCustomTemplate);
		for(AppointmentStatusData data : apptStatusList) {
			if(data.getStatusVal() == status) {
				if("Y".equals(data.getBlockedFromFutureAppts())) {
					return true;
				} else {
					continue;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean updateCustomerBlockStatus(JdbcCustomTemplate jdbcCustomTemplate, long scheduleId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE customer set  blocked_flag = 'Y' where id=(select customer_id from schedule sc where sc.id=?)");
		return jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[]{scheduleId}) !=0;
	}
	
	@Override
    public List<PledgeDetails> getPledgeReport(JdbcCustomTemplate jdbcCustomTemplate, String fromDate, String toDate, 
    		Integer locationId, String groupByIntake, String groupByFundSource, 
    		String groupByVendor, Integer resourceId, Integer fundSourceId) throws Exception {
       
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		StringBuilder sql = new StringBuilder();
        if ("Y".equalsIgnoreCase(groupByVendor)) {
            sql.append("select c.id, c.household_id, c.account_number, c.first_name, c.last_name, c.address, c.city, ");
            sql.append(" c.state, c.zip_postal, f.fund_name, cpv.vendor_id as vendorIds, cv.vendor_name as vendorNames, cpv.vendor_pledge_amount as vendorPAmounts, IF(cp.pledge_datetime IS NULL,'', DATE_FORMAT(cp.pledge_datetime,'%m/%d/%Y %h:%i %p')) as pledge_datetime,");
            sql.append(" DATE_FORMAT(s.appt_date_time,'%m/%d/%Y %h:%i %p') as appt_date_time, l.location_name_online,");
            sql.append(" r.id as resource_id, concat(r.first_name, ' ', r.last_name)  as resource_name, ser.service_name_online, cp.total_amount, cps.`status`,cpv.account_number as vendorAccountNumbers");
            sql.append(",cp.urgent_status , cp.updated_status ,cp.calledin_status,cp.primary_status,cp.secondary_status");
            sql.append(" from customer_pledge cp ");
            sql.append(" LEFT OUTER " );
            sql.append(" JOIN customer c ON c.id = cp.customer_id ");
            sql.append(" LEFT OUTER ");
            sql.append(" JOIN customer_pledge_fund_source f ON cp.fund_id = f.id ");
            sql.append(" LEFT OUTER ");
            sql.append(" JOIN customer_pledge_vendor cpv ON cp.id = cpv.customer_pledge_id ");
            sql.append(" LEFT OUTER ");
            sql.append(" JOIN customer_vendor cv ON cv.id = cpv.vendor_id ");
            sql.append(" LEFT OUTER ");
            sql.append(" JOIN customer_pledge_status cps ON cp.pledge_status_id = cps.id ");
            sql.append(" LEFT OUTER ");
            sql.append(" JOIN `schedule` s ON cp.schedule_id = s.id ");
            sql.append(" LEFT OUTER ");
            sql.append(" JOIN location l ON cp.location_id = l.id ");
            sql.append(" LEFT OUTER "); 
            sql.append(" JOIN resource r ON cp.resource_id = r.id ");
            sql.append(" LEFT OUTER ");
            sql.append(" JOIN service ser ON s.service_id = ser.id ");
            sql.append(" where 1=1 ");
            if(locationId != null && locationId > 0) {
            	sql.append(" and l.id = :locationId");
            	paramSource.addValue("locationId", locationId);
            }
            if(resourceId != null && resourceId > 0) {
            	sql.append(" and r.first_name = (SELECT first_name from resource where id=:resourceId) and r.last_name = (SELECT last_name from resource where id=:resourceId)");
            	paramSource.addValue("resourceId", resourceId);
            }
            
            if(fundSourceId != null && fundSourceId > 0) {
            	sql.append(" and f.id = :fundSourceId");
            	paramSource.addValue("fundSourceId", fundSourceId);
            }
            sql.append(" and DATE(cp.pledge_datetime) >=:fromDate and DATE(cp.pledge_datetime) <= :toDate  order by cv.vendor_name, cp.pledge_datetime");
            paramSource.addValue("fromDate", fromDate);
            paramSource.addValue("toDate", toDate);
        } else {
        	sql.append("select c.id, c.household_id, c.account_number, c.first_name, c.last_name, c.address, c.city,");
        	sql.append(" c.state, c.zip_postal, f.fund_name, group_concat(cpv.vendor_id ORDER BY cpv.id) as vendorIds, " );
        	sql.append("group_concat(cv.vendor_name ORDER BY cpv.id) as vendorNames, ");
        	sql.append("group_concat(cpv.vendor_pledge_amount ORDER BY cpv.id) as vendorPAmounts, IF(cp.pledge_datetime IS NULL,'', DATE_FORMAT(cp.pledge_datetime,'%m/%d/%Y %h:%i %p')) as pledge_datetime,");
        	sql.append(" DATE_FORMAT(s.appt_date_time,'%m/%d/%Y %h:%i %p') as appt_date_time, l.location_name_online, ");
        	sql.append(" r.id as resource_id, concat(r.first_name, ' ', r.last_name)  as resource_name, ser.service_name_online, cp.total_amount,cps.`status`,");
        	sql.append("group_concat(cpv.account_number ORDER BY cpv.id) as vendorAccountNumbers ,cp.urgent_status , cp.updated_status ,cp.calledin_status,cp.primary_status,cp.secondary_status ");
        	sql.append("from customer_pledge cp");
        	sql.append(" LEFT OUTER ");
        	sql.append(" JOIN customer c ON c.id = cp.customer_id ");
        	sql.append(" LEFT OUTER ");
        	sql.append(" JOIN customer_pledge_fund_source f ON cp.fund_id = f.id ");
        	sql.append(" LEFT OUTER ");
        	sql.append(" JOIN customer_pledge_vendor cpv ON cp.id = cpv.customer_pledge_id");
        	sql.append(" LEFT OUTER ");
        	sql.append(" JOIN customer_vendor cv ON cv.id = cpv.vendor_id ");
        	sql.append(" LEFT OUTER ");
        	sql.append("JOIN customer_pledge_status cps ON cp.pledge_status_id = cps.id ");
        	sql.append(" LEFT OUTER ");
        	sql.append("JOIN `schedule` s ON cp.schedule_id = s.id ");
        	sql.append("LEFT OUTER ");
        	sql.append("JOIN location l ON cp.location_id = l.id ");
        	sql.append("LEFT OUTER ");
        	sql.append("JOIN resource r ON cp.resource_id = r.id ");
        	sql.append("LEFT OUTER ");
        	sql.append("JOIN service ser ON s.service_id = ser.id ");
        	sql.append(" where 1=1 ");
            if(locationId != null && locationId > 0) {
            	sql.append(" and l.id = :locationId");
            	paramSource.addValue("locationId", locationId);
            }
            if(resourceId != null && resourceId > 0) {
            	sql.append(" and r.first_name = (SELECT first_name from resource where id=:resourceId) and r.last_name = (SELECT last_name from resource where id=:resourceId)");
            	paramSource.addValue("resourceId", resourceId);
            }
            
            if(fundSourceId !=null && fundSourceId > 0) {
            	sql.append(" and f.id = :fundSourceId");
            	paramSource.addValue("fundSourceId", fundSourceId);
            }
            sql.append(" and DATE(cp.pledge_datetime) >=:fromDate and DATE(cp.pledge_datetime) <= :toDate  group by cp.id");
            paramSource.addValue("fromDate", fromDate);
            paramSource.addValue("toDate", toDate);
            String orderBy = " order by cp.pledge_datetime";
            if ("Y".equalsIgnoreCase(groupByIntake)) {
                orderBy = " order by r.id, cp.pledge_datetime";
            } else if ("Y".equalsIgnoreCase(groupByFundSource)) {
                orderBy = " order by f.id, cp.pledge_datetime";
            }
            sql.append(orderBy);
        }
        return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, pledgeDetailsMapper());
    }
	
	
	@Override
	public Map<Resource, List<ServiceVO>> getResourceServiceList(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, Integer blockTimeInMins) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select r.id as resourceId, concat(r.prefix,' ',r.first_name,' ', r.last_name) as resourceName, s.id as serviceId, s.service_name_online, s.blocks");
		sql.append(" from resource r, service s, resource_service rs ");
		sql.append(" where r.location_id=? and r.id=rs.resource_id and s.id=rs.service_id and s.enable='Y' and s.delete_flag='N' and rs.enable='Y' and r.delete_flag='N' and r.enable='Y' order by r.placement asc");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{locationId},new ResultSetExtractor<Map<Resource, List<ServiceVO>>>() {
			
            @Override
            public Map<Resource, List<ServiceVO>> extractData(ResultSet rs) throws SQLException, DataAccessException {
            	Map<Resource, List<ServiceVO>> resourceServiceMap = new LinkedHashMap<>();
            	Resource resource;
            	ServiceVO service;
            	while(rs.next()) {
            		resource = new Resource();
            		service = new ServiceVO();
            		resource.setResourceId(rs.getInt("resourceId"));
            		resource.setResourceName(rs.getString("resourceName"));
            		
            		service.setServiceId(rs.getInt("serviceId"));
            		service.setServiceNameOnline(rs.getString("service_name_online"));
            		int blocks = rs.getInt("blocks");
            		service.setBlocks(blocks);
            		service.setDuration(blocks * blockTimeInMins);
            		
            		if(resourceServiceMap.containsKey(resource)) {
            			List<ServiceVO> services = resourceServiceMap.get(resource);
            			services.add(service);
            		} else {
            			List<ServiceVO> services = new ArrayList<>();
            			services.add(service);
            			resourceServiceMap.put(resource, services);
            		}
            	}
            	return resourceServiceMap;
            }
        });
	}

	private String getMinMaxDate(JdbcCustomTemplate jdbcCustomTemplate, String date, List<Integer> resourceIds) throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append("select CONCAT(min(rc.date_time),'|',max(rc.date_time)) as minMaxDate");
		sql.append(" from resource_calendar rc");
		sql.append(" where rc.resource_id in (:resourceIds) ");
		sql.append(" and DATE(rc.date_time) = :date ");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceIds", resourceIds);
		paramSource.addValue("date", date);
		try {
			String minMaxDate =  jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString()+" and rc.schedule_id >=0", paramSource, String.class);
			if(minMaxDate == null) {
				minMaxDate = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class);
			}
			return minMaxDate;
		} catch(DataAccessException dae) {
			throw dae;
		}
	}
	
	@Override
	public void getMinMaxTime(JdbcCustomTemplate jdbcCustomTemplate, String date, Integer resourceId, OneDateWorkingHours oneDateWorkingHours, int blockTimeInMins) throws Exception {
		String blockTimeInMinsStr= String.valueOf(blockTimeInMins).length()==1?"0"+blockTimeInMins:""+blockTimeInMins;
		StringBuilder sql = new StringBuilder();
		sql.append("select TIME(min(rc.date_time)) as minTime ,ADDTIME(TIME(max(rc.date_time)),'00:"+blockTimeInMinsStr+":00') as maxTime, count(1) as rows");
		sql.append(", DATE_FORMAT(TIME(min(rc.date_time)),'%h:%i %p') as  displayMinTime ,DATE_FORMAT(ADDTIME(TIME(max(rc.date_time)),'00:"+blockTimeInMinsStr+":00'),'%h:%i %p') as displayMaxTime");
		sql.append(" from resource_calendar rc");
		sql.append(" where rc.resource_id in (:resourceId) ");
		sql.append(" and DATE(rc.date_time) = :dateStr ");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceId", resourceId);
		paramSource.addValue("dateStr", date);
		jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString()+" and rc.schedule_id >=0", paramSource, minMaxMapper(oneDateWorkingHours));
		if(!oneDateWorkingHours.isDayOpen()) {
			jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, minMaxMapper(oneDateWorkingHours));
			oneDateWorkingHours.setBreakTimeOpen(false);
		} else {
			paramSource.addValue("startDateTime", date+" "+oneDateWorkingHours.getSelectedStartTime());
			paramSource.addValue("endDateTime", date+" "+oneDateWorkingHours.getSelectedEndTime());
			jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString()+" and rc.date_time between :startDateTime and :endDateTime and rc.schedule_id = -1 order by id asc", paramSource, minMaxBreakTimeMapper(oneDateWorkingHours,blockTimeInMins));
		}
		oneDateWorkingHours.setSelectedStartTime(oneDateWorkingHours.getTempSelectedStartTime());
		oneDateWorkingHours.setSelectedEndTime(oneDateWorkingHours.getTempSelectedEndTime());
		return;
	}
	
	private RowMapper<MonthlyApptData> minMaxMapper(OneDateWorkingHours oneDateWorkingHours) {
		return (rs, num) -> {
			if(rs.getString("minTime") != null && rs.getString("maxTime") != null) {
				oneDateWorkingHours.setTempSelectedStartTime(rs.getString("displayMinTime"));
				oneDateWorkingHours.setTempSelectedEndTime(rs.getString("displayMaxTime"));
				oneDateWorkingHours.setSelectedStartTime(rs.getString("minTime"));
				oneDateWorkingHours.setSelectedEndTime(rs.getString("maxTime"));
				oneDateWorkingHours.setDayOpen(true);
			} else {
				oneDateWorkingHours.setDayOpen(false);
			}
			return null;
		};
	}
	
	
	private RowMapper<MonthlyApptData> minMaxBreakTimeMapper(OneDateWorkingHours oneDateWorkingHours, int blockTimeInMins) {
		return (rs, num) -> {
			if(rs.getString("minTime") != null && rs.getString("maxTime") != null) {
				oneDateWorkingHours.setSelectedBreakTime(rs.getString("displayMinTime"));
				oneDateWorkingHours.setSelectedDuration(blockTimeInMins * (rs.getInt("rows") - 1));
				oneDateWorkingHours.setBreakTimeOpen(true);
			} else {
				oneDateWorkingHours.setBreakTimeOpen(false);
			}
			return null;
		};
	}
	

	@Override
	public void getDailyCalendarData(JdbcCustomTemplate jdbcCustomTemplate, String date, Integer locationId, List<Integer> resourceIds,DailyCalendarResponse dailyCalendarResponse) throws Exception {
		dailyCalendarResponse.setDynamicToolTipData(getDynamicFieldLabelData(jdbcCustomTemplate,"calendar_tooltip"));
		String minMaxDate = getMinMaxDate(jdbcCustomTemplate, date, resourceIds);
		if(minMaxDate == null || "".equals(minMaxDate)) {
			dailyCalendarResponse.setErrorFlag("Y");
			dailyCalendarResponse.setErrorMessage("Invalid Data present in resource calendar data.");
			return;
		}
		
		String minMaxDateArray[] = minMaxDate.split("\\|");
		StringBuilder sql = new StringBuilder();
		sql.append("select TIME_FORMAT(time(date_time),'%h:%i %p') as ctime, rc.schedule_id as scheduleId,IF(sc.blocks IS NULL,0,sc.blocks) as blocks,");
		sql.append(" DATE_FORMAT(sc.appt_date_time,'%m/%d/%Y %h:%i %p') as apptDateTime,");
		sql.append( "s.service_name_online as serviceName, c.account_number, c.attrib1,");
		sql.append("IF(sc.id > 0,c.first_name,'') as first_name,IF(sc.id > 0,c.last_name,'') as last_name,c.id as customerId,");
		sql.append("IF(sc.id > 0,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone");
		sql.append(" from resource_calendar rc");
		sql.append(" LEFT OUTER JOIN schedule sc ON sc.id=rc.schedule_id");
		sql.append(" LEFT OUTER JOIN customer c ON c.id=sc.customer_id");
		sql.append(" LEFT OUTER JOIN service s ON s.id=sc.service_id");
		sql.append(" where rc.resource_id=:resourceId");
		sql.append(" and rc.resource_id in (:resourceIds)");
		sql.append(" and rc.date_time >=:minDate ");
		sql.append(" and DATE(rc.date_time) = :date ");
		sql.append(" and rc.date_time <=:maxDate");
		sql.append(" order by rc.date_time");
		
		MapSqlParameterSource paramSource;
		List<ResourceCalendarData> resourceCalendarDataList = new ArrayList<>();
		for(Integer resourceId : resourceIds) {
			paramSource = new MapSqlParameterSource();
			paramSource.addValue("resourceId", resourceId);
			paramSource.addValue("resourceIds", resourceIds);
			paramSource.addValue("date", date);
			paramSource.addValue("minDate", minMaxDateArray[0]);
			paramSource.addValue("maxDate", minMaxDateArray[1]);
			
			Resource resource = getResourceById(jdbcCustomTemplate, resourceId, false);
			ResourceCalendarData rcData = new ResourceCalendarData();
			rcData.setResourceFirstName(resource.getFirstName());
			rcData.setResourceLastName(resource.getLastName());
			rcData.setResourceId(resourceId);
			boolean isHoliday = isHoliday(jdbcCustomTemplate, date);
			boolean isClosedDay = isClosedDays(jdbcCustomTemplate, locationId, date);

			List<CalendarData> calendarDataList = new ArrayList<>();
			ResourceCalendarData resourceCalendarData = jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, new ResultSetExtractor<ResourceCalendarData>() {
				@Override
				public ResourceCalendarData extractData(ResultSet rs) throws SQLException, DataAccessException {
					boolean isFirstBookedSlotOfConsecutive;
					Long oldScheduleId = null;
					CalendarData cdData;
					while (rs.next()) {
						cdData = new CalendarData();
						cdData.setTime(rs.getString("ctime"));
						Long scheduleId = rs.getLong("scheduleId");
						int blocks = rs.getInt("blocks");

						if(isHoliday || isClosedDay) {
							cdData.setRowSpan(1);
							if(isHoliday) {
								cdData.setApptStatus("holiday");
							} else {
								cdData.setApptStatus("closed");
							}
							calendarDataList.add(cdData);
							continue;
						}
						isFirstBookedSlotOfConsecutive = scheduleId.longValue() > 0  && !scheduleId.equals(oldScheduleId);
						oldScheduleId = scheduleId;
						if (isFirstBookedSlotOfConsecutive) {
							AppointmentReportData appointmentData = new AppointmentReportData();
							appointmentData.setScheduleId(scheduleId);
							appointmentData.setAccountNumber(rs.getString("account_number"));
							appointmentData.setFirstName(rs.getString("first_name"));
							appointmentData.setLastName(rs.getString("last_name"));
							appointmentData.setContactPhone(rs.getString("contact_phone"));
							appointmentData.setAttrib1(rs.getString("attrib1"));
							appointmentData.setServiceName(rs.getString("serviceName"));
							appointmentData.setApptDateTime(rs.getString("apptDateTime"));
							appointmentData.setCustomerId(rs.getLong("customerId"));
							cdData.setAppointmentData(appointmentData);
							cdData.setRowSpan(blocks);
							cdData.setApptStatus("booked");
						} else if (scheduleId.longValue() == 0) {
							cdData.setRowSpan(1);
							cdData.setApptStatus("open");
						} else if(scheduleId.longValue() == -1) {
							cdData.setRowSpan(1);
							cdData.setApptStatus("NA");
						} else if(scheduleId.longValue() == -2) {
							cdData.setRowSpan(1);
							cdData.setApptStatus("reserved");
						} else {
							cdData.setRowSpan(0);
							cdData.setApptStatus("booked");
						}
						calendarDataList.add(cdData);
					} 
					rcData.setCalendarDataList(calendarDataList);
					return rcData;
				}
			});
			
			// checking each resource size, it should be same size for all resources.
			int size = resourceCalendarDataList.size();
			if(size > 0) {
				ResourceCalendarData previousResourceCalendarData = resourceCalendarDataList.get(size-1);
				int previousCalendarDataSize = previousResourceCalendarData.getCalendarDataList().size();
				
				if(previousCalendarDataSize != calendarDataList.size()) {
					logger.error("Resource Calendar data is not proper for the date "+date+", resourceId: "+resourceId);
					//TODO: we will enable later - This is required.
					//dailyCalendarResponse.setStatus(false);
					//dailyCalendarResponse.setMessage("Resource Calendar data is not proper!");
					//break;
				}
			}
			resourceCalendarDataList.add(resourceCalendarData);
		}
		dailyCalendarResponse.setCalendarDataList(resourceCalendarDataList);
	}
	
	
	@Override
	public void getWeeklyCalendarData(JdbcCustomTemplate jdbcCustomTemplate, String date, Integer locationId, List<Integer> resourceIds, WeeklyCalendarResponse weeklyCalendarResponse) throws Exception {
		List<String> dateList = getDateList(jdbcCustomTemplate, date);
		weeklyCalendarResponse.setDynamicToolTipData(getDynamicFieldLabelData(jdbcCustomTemplate,"calendar_tooltip"));
		StringBuilder sql = new StringBuilder();
		sql.append("select TIME_FORMAT(time(date_time),'%h:%i %p') as ctime, rc.schedule_id as scheduleId,IF(sc.blocks IS NULL,0,sc.blocks) as blocks,");
		sql.append(" DATE_FORMAT(sc.appt_date_time,'%m/%d/%Y %h:%i %p') as apptDateTime, ");
		sql.append( "s.service_name_online as serviceName, c.account_number, c.attrib1, ");
		sql.append("IF(sc.id > 0,c.first_name,'') as first_name,IF(sc.id > 0,c.last_name,'') as last_name,c.id as customerId,");
		sql.append("IF(sc.id > 0,CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)),'') as contact_phone");
		sql.append(" from resource_calendar rc");
		sql.append(" LEFT OUTER JOIN schedule sc ON sc.id=rc.schedule_id");
		sql.append(" LEFT OUTER JOIN customer c ON c.id=sc.customer_id");
		sql.append(" LEFT OUTER JOIN service s ON s.id=sc.service_id");
		sql.append(" where rc.resource_id=:resourceId");
		sql.append(" and rc.date_time >= :minDateTime");
		sql.append(" and rc.date_time <= :maxDateTime");
		sql.append(" order by rc.date_time");
		
		MapSqlParameterSource paramSource;
		List<ResourceCalendarData> resourceCalendarDataList = new ArrayList<>();
		
		for(Integer resourceId : resourceIds) {
			String minTime = getMinTime(jdbcCustomTemplate, resourceId, dateList);
			String maxTime = getMaxTime(jdbcCustomTemplate, resourceId, dateList);
			for(String dateIn : dateList) {
				paramSource = new MapSqlParameterSource();
				paramSource.addValue("resourceId", resourceId);
				paramSource.addValue("minDateTime", dateIn+" "+minTime);
				paramSource.addValue("maxDateTime", dateIn+" "+maxTime);
				
				Resource resource = getResourceById(jdbcCustomTemplate, resourceId, false);
				ResourceCalendarData rcData = new ResourceCalendarData();
				rcData.setResourceFirstName(resource.getFirstName());
				rcData.setResourceLastName(resource.getLastName());
				rcData.setDate(DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(dateIn));
				rcData.setResourceId(resourceId);
				
				boolean isHoliday = isHoliday(jdbcCustomTemplate, dateIn);
				boolean isClosedDay = isClosedDays(jdbcCustomTemplate, locationId, dateIn);

				List<CalendarData> calendarDataList = new ArrayList<>();
				ResourceCalendarData resourceCalendarData = jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, new ResultSetExtractor<ResourceCalendarData>() {
					@Override
					public ResourceCalendarData extractData(ResultSet rs) throws SQLException, DataAccessException {
						boolean isFirstBookedSlotOfConsecutive;
						Long oldScheduleId = null;
						CalendarData cdData;
						while (rs.next()) {
							cdData = new CalendarData();
							cdData.setTime(rs.getString("ctime"));
							Long scheduleId = rs.getLong("scheduleId");
							int blocks = rs.getInt("blocks");
							if(isHoliday || isClosedDay) {
								cdData.setRowSpan(1);
								if(isHoliday) {
									cdData.setApptStatus("holiday");
								} else {
									cdData.setApptStatus("closed");
								}
								calendarDataList.add(cdData);
								continue;
							}
							isFirstBookedSlotOfConsecutive = scheduleId.longValue() > 0  && !scheduleId.equals(oldScheduleId);
							oldScheduleId = scheduleId;
							if (isFirstBookedSlotOfConsecutive) {
								AppointmentReportData appointmentData = new AppointmentReportData();
								appointmentData.setScheduleId(scheduleId);
								appointmentData.setAccountNumber(rs.getString("account_number"));
								appointmentData.setFirstName(rs.getString("first_name"));
								appointmentData.setLastName(rs.getString("last_name"));
								appointmentData.setContactPhone(rs.getString("contact_phone"));
								appointmentData.setAttrib1(rs.getString("attrib1"));
								appointmentData.setServiceName(rs.getString("serviceName"));
								appointmentData.setApptDateTime(rs.getString("apptDateTime"));
								appointmentData.setCustomerId(rs.getLong("customerId"));
								cdData.setAppointmentData(appointmentData);
								cdData.setRowSpan(blocks);
								cdData.setApptStatus("booked");
							} else if (scheduleId.longValue() == 0) {
								cdData.setRowSpan(1);
								cdData.setApptStatus("open");
							} else if(scheduleId.longValue() == -1) {
								cdData.setRowSpan(1);
								cdData.setApptStatus("NA");
							} else if(scheduleId.longValue() == -2) {
								cdData.setRowSpan(1);
								cdData.setApptStatus("reserved");
							} else {
								cdData.setRowSpan(0);
								cdData.setApptStatus("booked");
							}
							calendarDataList.add(cdData);
						} 
						rcData.setCalendarDataList(calendarDataList);
						return rcData;
					}
				});
				
				// checking each resource size, it should be same size for all resources.
				int size = resourceCalendarDataList.size();
				if(size > 0) {
					ResourceCalendarData previousResourceCalendarData = resourceCalendarDataList.get(size-1);
					int previousCalendarDataSize = previousResourceCalendarData.getCalendarDataList().size();
					
					if(previousCalendarDataSize != calendarDataList.size()) {
						logger.error("Resource Calendar data is not proper for the date: "+dateIn+", resourceId: "+resourceId);
						//TODO: we will enable later - This is required.
						//weeklyCalendarResponse.setStatus(false);
						//weeklyCalendarResponse.setMessage("Resource Calendar data is not proper!");
						//break;
					}
				}
				resourceCalendarDataList.add(resourceCalendarData);
			}
		}
		weeklyCalendarResponse.setCalendarDataList(resourceCalendarDataList);
	}
	

	private String getMaxTime(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, List<String> dateList) {
		String sql = "select time(max(date_time)) from resource_calendar rc1 where rc1.resource_id = :resourceId and DATE(rc1.date_time) in (:dates) and rc1.schedule_id >= 0";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("dates", dateList);
		paramSource.addValue("resourceId", resourceId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class);
	}



	private String getMinTime(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, List<String> dateList) {
		String sql = "select time(min(date_time)) from resource_calendar rc1 where rc1.resource_id = :resourceId and DATE(rc1.date_time) in (:dates) and rc1.schedule_id >= 0";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("dates", dateList);
		paramSource.addValue("resourceId", resourceId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class);
	}

	private List<String> getDateList(JdbcCustomTemplate jdbcCustomTemplate, String date) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CONCAT(DATE_ADD(:date, INTERVAL(1-DAYOFWEEK(:date)) DAY),',',");
	    sql.append("DATE_ADD(:date, INTERVAL(2-DAYOFWEEK(:date)) DAY),',',");
	    sql.append("DATE_ADD(:date, INTERVAL(3-DAYOFWEEK(:date)) DAY),',',");
	    sql.append("DATE_ADD(:date, INTERVAL(4-DAYOFWEEK(:date)) DAY),',',");
	    sql.append("DATE_ADD(:date, INTERVAL(5-DAYOFWEEK(:date)) DAY),',',");
	    sql.append("DATE_ADD(:date, INTERVAL(6-DAYOFWEEK(:date)) DAY),',',");
	    sql.append("DATE_ADD(:date, INTERVAL(7-DAYOFWEEK(:date)) DAY)) as dates");
	    sql.append(" FROM dual");
	    MapSqlParameterSource paramSource = new MapSqlParameterSource();
	    paramSource.addValue("date", date);
	    String dates = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class);
		return Arrays.asList(dates.split(","));
	}

	@Override
    public Map<String, Long> getNoOfOpenAppts(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymm) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select concat(DATE(date_time),'|',resource_id) as `date`,count(schedule_id) as count from resource_calendar");
        sql.append(" where DATE_FORMAT(date_time, '%Y-%m') = DATE_FORMAT(:dateyyyymm, '%Y-%m')");
        sql.append(" and resource_id in (:resourceIds)");
        sql.append(" and schedule_id = 0 group by DATE(date_time) order by date_time");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("dateyyyymm", dateyyyymm);
        paramSource.addValue("resourceIds", resourceIds);
        return  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, noOfApptMpapper()).stream().collect(Collectors.toMap(x -> x.getDate(), x -> x.getCount()));
    }
	
	@Override
    public Map<String, Long> getNoOfBookedAppts(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymmdd) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select concat(DATE(date_time),'|',resource_id) as `date`,count(distinct schedule_id) as count from resource_calendar");
        sql.append(" where DATE_FORMAT(date_time, '%Y-%m') = DATE_FORMAT(:dateyyyymmdd, '%Y-%m')");
        sql.append(" and resource_id in (:resourceIds)");
        sql.append(" and schedule_id > 0 group by DATE(date_time) order by date_time");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("dateyyyymmdd", dateyyyymmdd);
        paramSource.addValue("resourceIds", resourceIds);
        return  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, noOfApptMpapper()).stream().collect(Collectors.toMap(x -> x.getDate(), x -> x.getCount()));
    }
	

	@Override
    public Map<String, Long> getJSNoOfOpenAppts(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymm) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select DATE(date_time) as `date`,count(schedule_id) as count from resource_calendar");
        sql.append(" where DATE_FORMAT(date_time, '%Y-%m') = DATE_FORMAT(:dateyyyymm, '%Y-%m')");
        sql.append(" and resource_id in (:resourceIds)");
        sql.append(" and schedule_id = 0 group by DATE(date_time) order by date_time");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("dateyyyymm", dateyyyymm);
        paramSource.addValue("resourceIds", resourceIds);
        return  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, noOfApptMpapper()).stream().collect(Collectors.toMap(x -> x.getDate(), x -> x.getCount()));
    }

	@Override
    public Map<String, Long> getNoOfClosedTimeSlots(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymmdd) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select DATE(date_time) as `date`,count(schedule_id) as count from resource_calendar");
        sql.append(" where DATE_FORMAT(date_time, '%Y-%m') = DATE_FORMAT(:dateyyyymmdd, '%Y-%m')");
        sql.append(" and resource_id in (:resourceIds)");
        sql.append(" and schedule_id < 0 group by DATE(date_time) order by date_time");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("dateyyyymmdd", dateyyyymmdd);
        paramSource.addValue("resourceIds", resourceIds);
        return  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, noOfApptMpapper()).stream().collect(Collectors.toMap(x -> x.getDate(), x -> x.getCount()));
    }
	
	@Override
    public Map<String, Long> getNoOfTotalTimeSlots(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymmdd) {
        StringBuilder sql = new StringBuilder();
        sql.append("select DATE(date_time) as `date`,count(schedule_id) as count from resource_calendar");
        sql.append(" where DATE_FORMAT(date_time, '%Y-%m') = DATE_FORMAT(:dateyyyymmdd, '%Y-%m')");
        sql.append(" and resource_id in (:resourceIds)");
        sql.append(" group by DATE(date_time) order by date_time");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("dateyyyymmdd", dateyyyymmdd);
        paramSource.addValue("resourceIds", resourceIds);
        return  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, noOfApptMpapper()).stream().collect(Collectors.toMap(x -> x.getDate(), x -> x.getCount()));
    }
	
	@Override
    public List<MonthlyApptData> getOpenTimeSlots(JdbcCustomTemplate jdbcCustomTemplate, List<Integer> resourceIds, String dateyyyymmdd) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select date_time as `date`,schedule_id as count from resource_calendar");
        sql.append(" where DATE_FORMAT(date_time, '%Y-%m') = DATE_FORMAT(:dateyyyymmdd, '%Y-%m')");
        sql.append(" and resource_id in (:resourceIds)");
        sql.append(" and schedule_id = 0 order by resource_id,date_time asc");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("dateyyyymmdd", dateyyyymmdd);
        paramSource.addValue("resourceIds", resourceIds);
        return  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, noOfApptMpapper());
    }
	
	@Override
    public Map<String,Long> getHolidaysMap(JdbcCustomTemplate jdbcCustomTemplate,String dateyyyymmdd) throws Exception {
    	StringBuilder sql = new StringBuilder();
        sql.append("select distinct(`date`) as `date`,1 as count from holidays");
        sql.append(" where DATE_FORMAT(`date`, '%Y-%m') = DATE_FORMAT(:dateyyyymmdd, '%Y-%m')");
        sql.append(" order by date");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("dateyyyymmdd", dateyyyymmdd);
        return  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, noOfApptMpapper()).stream().collect(Collectors.toMap(x -> x.getDate(), x -> x.getCount()));
    }
	
	@Override
    public Map<String,Long> getClosedDaysMap(JdbcCustomTemplate jdbcCustomTemplate,Integer locationId, String dateyyyymmdd) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct(`date`) as `date`,1 as count from closed_days");
		sql.append(" where DATE_FORMAT(`date`, '%Y-%m') = DATE_FORMAT(:dateyyyymmdd, '%Y-%m')");
		sql.append(" and location_id=:locationId order by `date`");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
	    paramSource.addValue("dateyyyymmdd", dateyyyymmdd);
	    paramSource.addValue("locationId", locationId);
	    return  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, noOfApptMpapper()).stream().collect(Collectors.toMap(x -> x.getDate(), x -> x.getCount()));
    }
	
	@Override
	public int getMinBlocksByAdmin(JdbcCustomTemplate jdbcCustomTemplate,List<Integer> resourceIds) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select min(blocks) from service s where s.delete_flag ='N'");
		sql.append(" and id in (select res_service.service_id  from  resource_service res_service");
		sql.append(" where res_service.resource_id in (:resourceIds))");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceIds", resourceIds);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, Integer.class);
	}

	private RowMapper<MonthlyApptData> noOfApptMpapper() {
		return (rs, num) -> {
			MonthlyApptData monthlyApptData = new MonthlyApptData();
			monthlyApptData.setDate(rs.getString("date"));
			monthlyApptData.setCount(rs.getLong("count"));
			return monthlyApptData;
		};
	}

	@Override
	public List<ServiceVO> getServiceListByLocationId(JdbcCustomTemplate jdbcCustomTemplate,  Integer locationId, Integer blockTimeInMins, String filterKeyWord, boolean onlyActive) throws Exception {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		sql.append("select s.id as id, s.service_name_online from service_location rl, service s ");
		sql.append("where rl.location_id=:locationId and rl.service_id=s.id  ");
		if(onlyActive) {
			sql.append(" and s.enable='Y'");
		}
		sql.append(" and s.delete_flag='N' order by s.placement asc");
		paramSource.addValue("locationId", locationId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, serviceVOMapper(filterKeyWord, blockTimeInMins));
	}

	@Override
	public List<DynamicPledgeResult> getDynamicPledgeResultList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from dynamic_pledge_results ");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), dynamicPledgeResultMapper());
	}
	
	
	private List<DynamicFieldLabelData> getDynamicFieldLabelData(JdbcCustomTemplate jdbcCustomTemplate, String pageName) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from dynamic_field_labels where page_name=? order by placement ");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{pageName},dynamicToolTipDataMapper());
	}



	@Override
	public String getMonthFirstLastDate(JdbcCustomTemplate jdbcCustomTemplate, String calendarDateDB) throws Exception {
		String sql = "select concat(subdate(:date, (day(:date)-1)),'|', LAST_DAY(:date)) firstAndDate";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("date", calendarDateDB);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class);
	}

	@Override
	public String getNextDateAndIsContinueValue(JdbcCustomTemplate jdbcCustomTemplate, String currentDate, String lastDate) throws Exception {
		String sql = "SELECT CONCAT(DATE_ADD(:currentDate,INTERVAL 1 DAY),'|', IF(DATE_ADD(:currentDate,INTERVAL 1 DAY) <= DATE(:lastDate),1,0)) as nextDateAndIsContinue";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("currentDate", currentDate);
		paramSource.addValue("lastDate", lastDate);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, String.class);
	}



	@Override
	public void updateTransId(JdbcCustomTemplate jdbcCustomTemplate, Long scheduleId, Long transId) throws Exception {
		String sql = "update appointment set trans_id=? where schedule_id=?";
		if(transId != null && transId.longValue() > 0 ) {
			jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{transId, scheduleId});
		}
	}

	@Override
	public boolean customerExist(JdbcCustomTemplate jdbcCustomTemplate, List<CustomerRegistration> customerRegList, CustomerRequest customerRequest) throws Exception {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		sql.append("select count(id) from customer where 1=1 ");
		for(CustomerRegistration customerReg: customerRegList) {
			String paramColumn = customerReg.getParamColumn();	
			String javaRefFieldName = getJavaField(paramColumn);
			Object value = CoreUtils.getPropertyValue(customerRequest.getCustomer(), javaRefFieldName);
			if(value != null && !"".equals(value)) {
				sql.append(" and ").append(paramColumn).append("=:").append(paramColumn);
				paramSource.addValue(paramColumn, value);
			}
		}
		Customer customer = customerRequest.getCustomer();
		if(customer.getCustomerId() != null && customer.getCustomerId() > 0) {
			sql.append(" and id != :customerId");
			paramSource.addValue("customerId", CoreUtils.getPropertyValue(customer, "customerId"));
		}

		return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, Integer.class) > 0;
	}

	@Override
	public Long getCustomerIdIfExist(JdbcCustomTemplate jdbcCustomTemplate, List<CustomerRegistration> customerRegList, CustomerRequest customerRequest) throws Exception {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		sql.append("select count(id) from customer where 1=1 ");
		for(CustomerRegistration customerReg: customerRegList) {
			String paramColumn = customerReg.getParamColumn();
			String javaRefFieldName = getJavaField(paramColumn);
			Object value = CoreUtils.getPropertyValue(customerRequest.getCustomer(), javaRefFieldName);
			if(value != null && !"".equals(value)) {
				sql.append(" and ").append(paramColumn).append("=:").append(paramColumn);
				paramSource.addValue(paramColumn, value);
			}
		}
		Customer customer = customerRequest.getCustomer();
		if(customer.getCustomerId() != null && customer.getCustomerId() > 0) {
			sql.append(" and id != :customerId");
			paramSource.addValue("customerId", CoreUtils.getPropertyValue(customer, "customerId"));
		}

		boolean isExist = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, Integer.class) > 0;
		if(isExist) {

			String cutomerIdSQL = sql.toString();
			cutomerIdSQL=cutomerIdSQL.replace("count(id)","id");
			return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(cutomerIdSQL.toString(), paramSource,Long.class);
		}
		return (long)0;
	}

	private String getJavaField(String paramColumn) {
		int index = paramColumn.indexOf("_");
		if(index == -1) {
			return paramColumn;
		} else {
			paramColumn = paramColumn.replace("_", "");
			String first = paramColumn.substring(0,index);
			String last = paramColumn.substring(index);
			return first+CoreUtils.getInitCaseValue(last);
		}
	}
	
	@Override
	public long saveCustomer(JdbcCustomTemplate jdbcCustomTemplate, final CustomerRequest customerRequest, ClientDeploymentConfig cdConfig) throws TelAppointException, Exception {
		Customer customer = customerRequest.getCustomer();
		final StringBuilder sql = new StringBuilder();
		List<String> loginTypes = new ArrayList<>();
		loginTypes.add("registration");
		List<String> mandatoryColumns = getColumnList(jdbcCustomTemplate, loginTypes);
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		loginTypes = new ArrayList<>();
		loginTypes.add("update");
		List<String> updatedColumns = getColumnList(jdbcCustomTemplate, loginTypes);
		
		sql.append("insert into customer (");
		
		for(String mandatroyColumn : mandatoryColumns) {
			sql.append(mandatroyColumn).append(",");
		}
		
		for(String updatedColumn : updatedColumns) {
			sql.append(updatedColumn).append(",");
		}
		
		sql.append("create_datetime, update_datetime,");
		sql.append("household_id)");
		
		sql.append(" values ");
		sql.append(" (");
		for(String placeHolder : mandatoryColumns) {
			sql.append(":").append(placeHolder).append(",");
			String javaFieldName = getJavaField(placeHolder); 
			paramSource.addValue(placeHolder, CoreUtils.getPropertyValue(customerRequest.getCustomer(), javaFieldName));
		}
		
		for(String placeHolder : updatedColumns) {
			String javaFieldName = getJavaField(placeHolder); 
			sql.append(":").append(placeHolder).append(",");
			paramSource.addValue(placeHolder, CoreUtils.getPropertyValue(customerRequest.getCustomer(), javaFieldName));
		}
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')").append(",");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')").append(",");
		sql.append("(SELECT nextval('sq_my_sequence') as next_sequence)");
		sql.append(" )");
		
		logger.debug("saveCustomer SQL: " + sql.toString());
		
		
		KeyHolder holder = new GeneratedKeyHolder();
		try {
			jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource, holder);
			return holder.getKey().longValue();
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2030.getCode(), ErrorConstants.ERROR_2030.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "CustomerData:"+customer.toString());
		}
	}

	private List<String> getColumnList(JdbcCustomTemplate jdbcCustomTemplate, List<String> loginTypes) {
		String sql = "select GROUP_CONCAT(param_column) from customer_registration where login_type in (:loginTypes) order by placement";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("loginTypes", loginTypes);
		String paramColumns = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(),paramSource, String.class);
		if(paramColumns!=null) {
			return Arrays.asList(paramColumns.split(","));
		}
		return new ArrayList<>();
	}
	
	

	@Override
	public boolean updateCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomerRequest customerRequest) throws Exception {
		Customer customer = customerRequest.getCustomer();
		final StringBuilder sql = new StringBuilder();
		List<String> loginTypes = new ArrayList<>();
		loginTypes.add("update");
		loginTypes.add("registration");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		List<String> updateColumns = getColumnList(jdbcCustomTemplate, loginTypes);
		
		sql.append("update customer set id=:customerId");
		for(String columnName : updateColumns) {
			String javaFieldName = getJavaField(columnName); 
			sql.append(", ").append(columnName).append("=:").append(javaFieldName);
			paramSource.addValue(javaFieldName, CoreUtils.getPropertyValue(customer, javaFieldName));
		}
		sql.append(" where id=:customerId");
		paramSource.addValue("customerId", customer.getCustomerId());
		jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource);
		return true;
	}

	@Override
	public void getFutureAppointments(JdbcCustomTemplate jdbcCustomTemplate, long customerId, ClientDeploymentConfig cdConfig, List<AppointmentDetails> apptList) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select DISTINCT cu.account_number, cu.first_name, cu.last_name, cu.contact_phone, cu.home_phone, cu.cell_phone, cu.work_phone,cu.email, cu.attrib1, cu.attrib2,");
		sql.append("cu.attrib3, cu.attrib4,cu.attrib5, cu.attrib6,cu.attrib7, cu.attrib8,cu.attrib9, cu.attrib10,");
		sql.append("cu.attrib11, cu.attrib12,cu.attrib13, cu.attrib14,cu.attrib15, cu.attrib16,cu.attrib17, cu.attrib18,cu.attrib19, cu.attrib20,");
		sql.append("l.location_name_online, l.address, l.city, l.state, l.zip, l.time_zone, ");
		sql.append("CONCAT(r.first_name,'',r.last_name) as resourceName, s.service_name_online,");
		sql.append("DATE_FORMAT(sc.appt_date_time,").append("'").append("%m/%d/%Y %h:%i %p").append("'").append(") as apptDateTime, sc.comments, ");
		sql.append(" sc.id as scheduleId, a.conf_number from customer cu, schedule sc, appointment a, location l, resource r, service s");
		sql.append(" where 1=1");
		sql.append(" and sc.appt_date_time > CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
		sql.append(" and sc.status=11");
		sql.append(" and cu.id=? and cu.id=sc.customer_id and sc.id=a.schedule_id and sc.resource_id=r.id and l.id=sc.location_id and s.id=sc.service_id order by sc.appt_date_time asc");

		logger.debug("getBookedAppointment query: " + sql.toString());
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { customerId }, new ResultSetExtractor<Long>() {
				@Override
				public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
					AppointmentDetails apptDetails = null;
					while (rs.next()) {
						apptDetails = new AppointmentDetails();
						apptDetails.setAccountNumber(rs.getString("account_number"));
						apptDetails.setFirstName(rs.getString("first_name"));
						apptDetails.setLastName(rs.getString("last_name"));
						apptDetails.setHomePhone(rs.getString("home_phone"));
						apptDetails.setContactPhone(rs.getString("contact_phone"));
						apptDetails.setCellPhone(rs.getString("cell_phone"));
						apptDetails.setWorkPhone(rs.getString("work_phone"));
						apptDetails.setEmail(rs.getString("email"));
						apptDetails.setLocationAddress(rs.getString("address"));
						apptDetails.setCity(rs.getString("city"));
						apptDetails.setState(rs.getString("state"));
						apptDetails.setZip(rs.getString("zip"));
						apptDetails.setLocationName(rs.getString("location_name_online"));
						apptDetails.setServiceName(rs.getString("service_name_online"));
						apptDetails.setAttrib1(rs.getString("attrib1"));
						apptDetails.setAttrib2(rs.getString("attrib2"));
						apptDetails.setAttrib3(rs.getString("attrib3"));
						apptDetails.setAttrib4(rs.getString("attrib4"));
						apptDetails.setAttrib5(rs.getString("attrib5"));
						apptDetails.setAttrib6(rs.getString("attrib6"));
						apptDetails.setAttrib7(rs.getString("attrib7"));
						apptDetails.setAttrib8(rs.getString("attrib8"));
						apptDetails.setAttrib9(rs.getString("attrib9"));
						apptDetails.setAttrib10(rs.getString("attrib10"));
						apptDetails.setAttrib11(rs.getString("attrib11"));
						apptDetails.setAttrib12(rs.getString("attrib12"));
						apptDetails.setAttrib13(rs.getString("attrib13"));
						apptDetails.setAttrib14(rs.getString("attrib14"));
						apptDetails.setAttrib15(rs.getString("attrib15"));
						apptDetails.setAttrib16(rs.getString("attrib16"));
						apptDetails.setAttrib17(rs.getString("attrib17"));
						apptDetails.setAttrib18(rs.getString("attrib18"));
						apptDetails.setAttrib19(rs.getString("attrib19"));
						apptDetails.setAttrib20(rs.getString("attrib20"));
						apptDetails.setResourceName(rs.getString("resourceName"));
						apptDetails.setApptDateTime(rs.getString("apptDateTime"));
						apptDetails.setScheduleId(rs.getLong("scheduleId"));
						apptDetails.setConfirmationNumber(rs.getLong("conf_number"));
						apptDetails.setComments(rs.getString("comments"));
						apptList.add(apptDetails);
					}
					return (long) 0;
				}
			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2031.getCode(), ErrorConstants.ERROR_2031.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(),
					"CustomerId:[" + customerId + "]");
		}
	}
	
	@Override
	public boolean updateAppointStatus(JdbcCustomTemplate jdbcCustomTemplate, String screenedFlag, String accessedFlag, Integer status, Long scheduleId) throws Exception {
		StringBuilder sql = new StringBuilder("update schedule set screened=:screened, accessed=:accessed");
		sql.append(",status=:status");
		sql.append(" where id=:scheduleId");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("screened", screenedFlag);
		paramSource.addValue("scheduleId", scheduleId);
		paramSource.addValue("accessed", accessedFlag);
		paramSource.addValue("status", status);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(),paramSource) !=0;
	}
	
	
	@Override
	public RecordTimeResponse updateRecordTime(JdbcCustomTemplate jdbcCustomTemplate, String recordName, String recordType, long scheduleId, String timeZone) throws Exception {
		RecordTimeResponse recordTimeResponse = new RecordTimeResponse();
		StringBuilder sql = new StringBuilder();
		sql.append("update schedule set id=:scheduleId");
		boolean isEndTimeUpdate = "stop".equalsIgnoreCase(recordType) ? true : false;
		boolean isStartTimeUpdate = "start".equalsIgnoreCase(recordType) ? true : false;
		boolean isResetTimeUpdate = "reset".equalsIgnoreCase(recordType) ? true : false;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("timeZone", timeZone);
		String currentDateTime = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject("SELECT CONVERT_TZ(now(),'US/Central',:timeZone) from dual", paramSource, String.class);
		String time = DateUtils.convert24To12HMMAFormat(currentDateTime.substring(11));
		if (isStartTimeUpdate) {
			if ("frontdesk".equals(recordName)) {
				sql.append(",frontdesk_record_start_time=:currentDateTime");
			} else if ("resource".equals(recordName)) {
				sql.append(",resource_record_start_time=:currentDateTime");
			}
			paramSource.addValue("currentDateTime", currentDateTime);
			recordTimeResponse.setStartTime(time);
		} else if (isEndTimeUpdate) {
			if ("frontdesk".equals(recordName)) {
				sql.append(",frontdesk_record_end_time=:currentDateTime");
			} else if ("resource".equals(recordName)) {
				sql.append(",resource_record_end_time=:currentDateTime");
			}
			paramSource.addValue("currentDateTime", currentDateTime);
			recordTimeResponse.setEndTime(time);
		} else if (isResetTimeUpdate) {
			if ("frontdesk".equals(recordName)) {
				sql.append(",frontdesk_record_start_time=NULL");
				sql.append(",frontdesk_record_end_time=NULL");
			} else if ("resource".equals(recordName)) {
				sql.append(",resource_record_start_time=NULL");
				sql.append(",resource_record_end_time=NULL");
			}
		}
		sql.append(" where id=:scheduleId");
		paramSource.addValue("scheduleId", scheduleId);
		jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(),paramSource);
		if (isEndTimeUpdate) {
			recordTimeResponse.setDuration(getRecordMins(jdbcCustomTemplate, scheduleId, recordName));
		}
		return recordTimeResponse;
	}
	
	private Integer getRecordMins(JdbcCustomTemplate jdbcCustomTemplate, long scheduleId, String recordName) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select TIMESTAMPDIFF(MINUTE," + recordName + "_record_start_time," + recordName+"_record_end_time) from schedule where id=:scheduleId");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("scheduleId", scheduleId);
		Integer mins = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql.toString(), paramSource, Integer.class);
		return mins;
	}
	
	@Override
	public void getPledgeHistory(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Long customerId, CustomerPledgeResponse pledgeRes) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT cp.id as cpId, c.id as cId, c.household_id, RIGHT(c.account_number,4) as account_number, c.first_name, c.last_name, DATE_FORMAT(s.appt_date_time, '%m/%d/%y %l:%i %p') as apptDateTime, f.id as fid, ");
		sql.append(" f.fund_name, f.fund_name_tts, f.fund_name_audio,");
		sql.append("group_concat(cv.vendor_name_tts ORDER BY cv.id) as vendorNameTts,");
		sql.append("group_concat(cv.vendor_name_audio ORDER BY cv.id) as vendorNameAudio,");
        sql.append("cp.total_amount,group_concat(cpv.vendor_id ORDER BY cpv.id) as vendorIds,group_concat(cv.vendor_name ORDER BY cpv.id) as vendorNames,group_concat(cpv.vendor_pledge_amount ORDER BY cpv.id) as vendorAmounts,");
       	sql.append(" DATE_FORMAT(cp.pledge_datetime,'%M %d, %Y') as pledge_datetime,");
        sql.append("cps.`status`, CASE c.liheap_fund WHEN 'Y' THEN 'No' WHEN 'N' THEN 'Yes' ELSE 'Yes' END as liheapFund, CASE c.psehelp_fund WHEN 'Y' THEN 'No' WHEN 'N' THEN 'Yes' ELSE 'Yes' END as psehelpFund, s.id as scheduleId ");
        sql.append(" FROM  customer c ");
        sql.append(" LEFT OUTER JOIN customer_pledge cp ON c.id = cp.customer_id ");
        sql.append(" LEFT OUTER JOIN customer_pledge_fund_source f ON cp.fund_id = f.id"); 
        sql.append(" LEFT OUTER JOIN customer_pledge_status cps ON cp.pledge_status_id = cps.id ");
        sql.append(" LEFT OUTER JOIN customer_pledge_vendor cpv ON cp.id = cpv.customer_pledge_id ");
        sql.append(" LEFT OUTER JOIN customer_vendor cv ON cpv.vendor_id = cv.id");
        sql.append(" LEFT OUTER JOIN `schedule` s ON cp.schedule_id = s.id");
        sql.append(" WHERE c.id IN (SELECT c2.id FROM customer c2 WHERE c2.household_id = (SELECT c1.household_id FROM customer c1 WHERE c1.id = ?))");
        sql.append(" and cp.total_amount > 0.00 group by c.id,cp.id ORDER BY cp.id,cp.pledge_datetime DESC");
        logger.debug("getPledgeHistoryList ::: SQL  ::: " + sql.toString());
        try {
        	List<CustomerPledge> customerPledgeList = new ArrayList<CustomerPledge>();
            jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(),new Object[]{customerId}, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                	CustomerPledge customerPledge;
                    while (rs.next()) {
                    	customerPledge = new CustomerPledge();
                    	customerPledge.setCustomerPledgeId(""+rs.getLong("cpId"));
                    	customerPledge.setHouseHoldId(rs.getLong("household_id"));
                    	customerPledge.setAccountNumber(rs.getString("account_number"));
                    	customerPledge.setFirstName(rs.getString("first_name"));
                    	customerPledge.setLastName(rs.getString("last_name"));
                    	customerPledge.setApptDateTime(rs.getString("apptDateTime"));
                    	customerPledge.setPledgeDateTime(rs.getString("pledge_datetime"));
                    	customerPledge.setCustomerId(rs.getLong("cId"));
                    	customerPledge.setFundName(rs.getString("fund_name"));
						try {
							
							
		                    	customerPledge.setFundId(rs.getLong("fid"));
		                    	customerPledge.setTotalPledgeAmt(String.format("%.2f",rs.getDouble("total_amount")));
		                    	customerPledge.setPledgeStatus(rs.getString("status"));
		                    	customerPledge.setLiheapFund(rs.getString("liheapFund"));
		                    	customerPledge.setPseHelpFund(rs.getString("psehelpFund"));
		                    	customerPledge.setScheduleId(rs.getLong("scheduleId"));
		                    	customerPledge.setPledgeStatus(rs.getString("status"));
		                    	
								String vendorIds = rs.getString("vendorIds");
								if (vendorIds != null) {
									String vendorIdArr[] = vendorIds.split(",");
									for (int index = 0; index < vendorIdArr.length; index++) {
										int id = index + 1;
										CoreUtils.setPropertyValue(customerPledge, "vendor" + (id)+"Id", vendorIdArr[index]);
									}
								}
	
								String vendorNames = rs.getString("vendorNames");
								if (vendorNames != null) {
									String vendorNameArr[] = vendorNames.split(",");
									for (int index = 0; index < vendorNameArr.length; index++) {
										int id = index + 1;
										CoreUtils.setPropertyValue(customerPledge, "vendor" + (id)+"Name", vendorNameArr[index]);
									}
								}
							

							String vendorPAmounts = rs.getString("vendorAmounts");
							if (vendorPAmounts != null) {
								String pledgeAmountsArr[] = vendorPAmounts.split(",");
								for (int index = 0; index < pledgeAmountsArr.length; index++) {
									int id = index + 1;
									if(pledgeAmountsArr[index] != null) {
										CoreUtils.setPropertyValue(customerPledge, "vendor" + id + "Payment", pledgeAmountsArr[index]);
									} else {
										CoreUtils.setPropertyValue(customerPledge, "vendor" + id + "Payment", "");
									}
								}
							}
						} catch (Exception e) {
							logger.error("Error: "+e,e);
						}
                    	customerPledgeList.add(customerPledge);
                    } 
                    pledgeRes.setCustomerPledgeList(customerPledgeList);
                    return (long) 0;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_2033.getCode(), ErrorConstants.ERROR_2033.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
		
	}

	@Override
	public void getPastAppointments(JdbcCustomTemplate jdbcCustomTemplate, long customerId, ClientDeploymentConfig cdConfig, List<AppointmentDetails> apptList) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select DISTINCT cu.account_number, cu.first_name, cu.last_name, cu.contact_phone, cu.home_phone, cu.cell_phone, cu.work_phone,cu.email, cu.attrib1, cu.attrib2,");
		sql.append("cu.attrib3, cu.attrib4,cu.attrib5, cu.attrib6,cu.attrib7, cu.attrib8,cu.attrib9, cu.attrib10,");
		sql.append("cu.attrib11, cu.attrib12,cu.attrib13, cu.attrib14,cu.attrib15, cu.attrib16,cu.attrib17, cu.attrib18,cu.attrib19, cu.attrib20,");
		sql.append("l.location_name_online, l.address, l.city, l.state, l.zip, l.time_zone, ");
		sql.append("CONCAT(r.first_name,'',r.last_name) as resourceName, s.service_name_online,");
		sql.append("DATE_FORMAT(sc.appt_date_time,").append("'").append("%m/%d/%Y %h:%i %p").append("'").append(") as apptDateTime,");
		sql.append(" sc.id as scheduleId, a.conf_number from customer cu, schedule sc, appointment a, location l, resource r, service s");
		sql.append(" where 1=1");
		sql.append(" and sc.appt_date_time < CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
		sql.append(" and sc.status=11");
		sql.append(" and cu.id=? and cu.id=sc.customer_id and sc.id=a.schedule_id and sc.resource_id=r.id and l.id=sc.location_id and s.id=sc.service_id order by sc.appt_date_time asc");
		logger.debug("getBookedAppointment query: " + sql.toString());
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { customerId }, new ResultSetExtractor<Long>() {
				@Override
				public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
					AppointmentDetails apptDetails = null;
					while (rs.next()) {
						apptDetails = new AppointmentDetails();
						apptDetails.setAccountNumber(rs.getString("account_number"));
						apptDetails.setFirstName(rs.getString("first_name"));
						apptDetails.setLastName(rs.getString("last_name"));
						apptDetails.setHomePhone(rs.getString("home_phone"));
						apptDetails.setContactPhone(rs.getString("contact_phone"));
						apptDetails.setCellPhone(rs.getString("cell_phone"));
						apptDetails.setWorkPhone(rs.getString("work_phone"));
						apptDetails.setEmail(rs.getString("email"));
						apptDetails.setLocationAddress(rs.getString("address"));
						apptDetails.setCity(rs.getString("city"));
						apptDetails.setState(rs.getString("state"));
						apptDetails.setZip(rs.getString("zip"));
						apptDetails.setLocationName(rs.getString("location_name_online"));
						apptDetails.setServiceName(rs.getString("service_name_online"));
						apptDetails.setAttrib1(rs.getString("attrib1"));
						apptDetails.setAttrib2(rs.getString("attrib2"));
						apptDetails.setAttrib3(rs.getString("attrib3"));
						apptDetails.setAttrib4(rs.getString("attrib4"));
						apptDetails.setAttrib5(rs.getString("attrib5"));
						apptDetails.setAttrib6(rs.getString("attrib6"));
						apptDetails.setAttrib7(rs.getString("attrib7"));
						apptDetails.setAttrib8(rs.getString("attrib8"));
						apptDetails.setAttrib9(rs.getString("attrib9"));
						apptDetails.setAttrib10(rs.getString("attrib10"));
						apptDetails.setAttrib11(rs.getString("attrib11"));
						apptDetails.setAttrib12(rs.getString("attrib12"));
						apptDetails.setAttrib13(rs.getString("attrib13"));
						apptDetails.setAttrib14(rs.getString("attrib14"));
						apptDetails.setAttrib15(rs.getString("attrib15"));
						apptDetails.setAttrib16(rs.getString("attrib16"));
						apptDetails.setAttrib17(rs.getString("attrib17"));
						apptDetails.setAttrib18(rs.getString("attrib18"));
						apptDetails.setAttrib19(rs.getString("attrib19"));
						apptDetails.setAttrib20(rs.getString("attrib20"));
						apptDetails.setResourceName(rs.getString("resourceName"));
						apptDetails.setApptDateTime(rs.getString("apptDateTime"));
						apptDetails.setScheduleId(rs.getLong("scheduleId"));
						apptDetails.setConfirmationNumber(rs.getLong("conf_number"));
						apptList.add(apptDetails);
					}
					return (long) 0;
				}
			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_2031.getCode(), ErrorConstants.ERROR_2031.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(),
					"CustomerId:[" + customerId + "]");
		}
	}

	@Override
	public ApptStatusResponse getAppointmentStatus(JdbcCustomTemplate jdbcCustomTemplate, Long scheduleId) throws Exception {
		String sql = "select status, screened, accessed from schedule where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{scheduleId},new ResultSetExtractor<ApptStatusResponse>() {
			ApptStatusResponse apptStatusRes = new ApptStatusResponse();
            @Override
            public ApptStatusResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
            	if(rs.next()) {
            		apptStatusRes.setApptStatus(rs.getInt("status"));
            		apptStatusRes.setScreened(rs.getString("screened"));
            		apptStatusRes.setAccessed(rs.getString("accessed"));
            		return apptStatusRes;
            	}
            	return null;
            }
        });
	}

	@Override
	public List<ServiceVO> getSameServiceBlockList(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, Integer serviceId, String filterKeyWord, Integer blockTimeInMins) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select s.id, s.service_name_online,s.blocks from resource_service rs, service s ");
			sql.append(" where rs.resource_id =:resourceId");
			sql.append(" and rs.service_id = s.id ");
			sql.append(" and s.delete_flag = 'N' ");
			sql.append(" and s.blocks = (select blocks from service where id =:serviceId)");			
			sql.append(" order by s.placement asc");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("resourceId", resourceId);
			paramSource.addValue("serviceId", serviceId);
			return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, serviceVOMapper(filterKeyWord, blockTimeInMins));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ERROR_2021.getCode(), ERROR_2021.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	public boolean updateLHEAPandPSEHelpRecievedStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomerPledgeRequest customerPledgeReq) throws Exception {
	 List<CustomerPledge> customerPledgeList = customerPledgeReq.getCustomerPledgeList().stream().filter(e -> e.getTotalPledgeAmt()!=null && !"".equals(e.getTotalPledgeAmt()) && !"null".equals(e.getTotalPledgeAmt()) && !"0".equals(e.getTotalPledgeAmt())).collect(Collectors.toList());
	 List<Long> fundIdList =  customerPledgeList.stream().map(CustomerPledge::getFundId).collect(Collectors.toList());
	 String fundNames = getFundNames(jdbcCustomTemplate, fundIdList);
   	 if(fundNames != null && !"".equals(fundNames)) {
   		 StringBuilder sql = new StringBuilder();
   		 sql.append(" update customer set ");
	     if(fundNames.contains("LIHEAP")) {
	       sql.append("liheap_fund = 'Y'");
	     }
	     
	     if(fundNames.contains("PSE HELP")) {
	       sql.append("psehelp_fund = 'Y'");
	     }         
	     sql.append(" where household_id = :houseHoldId");
	     MapSqlParameterSource paramSource = new MapSqlParameterSource();
	     paramSource.addValue("houseHoldId",customerPledgeReq.getCustomer().getHouseHoldId());
	     return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource) != 0;
   	 }
   	 return true;
   }
	
	public void updateLHEAPandPSEHelpRecievedStatus(JdbcCustomTemplate jdbcCustomTemplate, String houseHoldId, String findName, String eligible) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" update customer set ");
		if (findName.contains("LIHEAP")) {

			if ("No".equals(eligible)) {
				sql.append("liheap_fund = 'Y'");
			} else {
				sql.append("liheap_fund = 'N'");
			}
		}
		if (findName.contains("PSE HELP")) {
			if ("No".equals(eligible)) {
				sql.append("psehelp_fund = 'Y'");
			} else {
				sql.append("psehelp_fund = 'N'");
			}
		}
		sql.append(" where household_id = ");
		sql.append(houseHoldId);
		jdbcCustomTemplate.getJdbcTemplate().update(sql.toString());
	}
	
	private String getFundNames(JdbcCustomTemplate jdbcCustomTemplate, List<Long> fundIds) {
		if(!fundIds.isEmpty()) {
			String sql = "select group_concat(fund_name) as fundNames where id in (:fundIds)";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("fundIds", fundIds);
			return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql, paramSource, String.class);
		}
		return null;
	}
	
	
	@Override
	public List<OutLookAppointment> getApptsForOutlook(JdbcCustomTemplate jdbcCustomTemplate, String resourceIds, Integer blockTimeInMins) throws Exception {
		StringBuilder sql = new StringBuilder("");
        sql.append("SELECT a.conf_number,a.appt_type , s.appt_date_time , srv.service_name_online , srv.blocks+srv.buffer as blocksPlusBuffer,loc.location_name_online, c.first_name as cfn,");
        sql.append(" c.last_name as cln,c.account_number,c.home_phone,c.contact_phone, c.email, res.first_name as resfn, res.last_name  as resln, s.comments");
        sql.append(" from schedule s, resource res ,service srv,location loc, customer c, appointment a where s.id = a.schedule_id and  s.resource_id =  res.id");
        sql.append(" and s.location_id = res.location_id and s.location_id = loc.id and s.service_id = srv.id and s.customer_id = c.id and ( s.status = 11 or s.status = 21 )");
        sql.append(" and res.id in (:resourceIds) and a.outlook_google_sync = 'N' ");
        sql.append(" order by a.appt_type,a.conf_number asc");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceIds", Arrays.asList(resourceIds.split(",")));
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, outLookAppointmentMapper(blockTimeInMins));
	}
	
	public boolean updateOutlookSyncStatus(JdbcCustomTemplate jdbcCustomTemplate, OutlookSyncReq outlookSyncReq) throws Exception {
		StringBuilder sql = new StringBuilder();
        sql.append("UPDATE appointment set  outlook_google_sync = 'Y' where conf_number in (:confNumbers)");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("confNumbers", outlookSyncReq.getConfNumberList());
        return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource)!=0;
	}

	@Override
	public List<Map<String, Object>> getResourceWorkingHoursHistory(JdbcCustomTemplate jdbcCustomTemplate, String locationId,
																	String resourceIds, String fromDate, String toDate) {
		resourceIds = resourceIds == null || resourceIds.trim().isEmpty() ? "0" : resourceIds.replaceAll("\\|", ",");
		String sql = "select * from resource_working_hrs_history where resource_id IN ("+resourceIds+") and location_id = "+locationId+" and  " +
				"effective_date >= '"+fromDate+" ' and end_date <= '"+toDate+"' order by timestamp desc";
		System.out.println("sql = " + sql);
		return jdbcCustomTemplate.getJdbcTemplate().queryForList(sql);

	}

	@Override
	public List<Map<String, Object>> getResourceWorkingHoursHistory(JdbcCustomTemplate jdbcCustomTemplate, String locationId, String resourceIds) {
		resourceIds = resourceIds == null || resourceIds.trim().isEmpty() ? "0" : resourceIds.replaceAll("\\|", ",");
		String sql = "select * from resource_working_hrs_history where resource_id IN ("+resourceIds+") and location_id = "+locationId+
				" order by timestamp desc, resource_id asc ";
		System.out.println("sql = " + sql);
		return jdbcCustomTemplate.getJdbcTemplate().queryForList(sql);
	}

	@Override
	public Map<String, Object> getApptSysConfigDefaultResourceWorkingHours(JdbcCustomTemplate jdbcCustomTemplate) {
		String SQL = "select default_day_start_time, default_day_end_time, default_break_time_1, default_break_time_1_mins, default_break_time_2, default_break_time_2_mins,\n" +
				" default_break_time_3, default_break_time_3_mins, default_break_time_4, default_break_time_4_mins, \n" +
				" default_is_sun_open, default_is_mon_open , default_is_tue_open, default_is_wed_open, default_is_thu_open, " +
				" default_is_fri_open , default_is_sat_open from `appt_sys_config`";
		System.out.println("SQL = " + SQL);

		return jdbcCustomTemplate.getJdbcTemplate().queryForMap(SQL);
	}

	@Override
	public List<Map<String, Object>> getResourceWorkingHours(JdbcCustomTemplate jdbcCustomTemplate, String locationId, String resourceIds) {
		resourceIds = resourceIds == null || resourceIds.trim().isEmpty() ? "0" : resourceIds.replaceAll("\\|", ",");
		String sql = "select * from resource_working_hrs where resource_id IN ("+resourceIds+") and location_id = "+locationId+
				"  order by resource_id asc";
		return jdbcCustomTemplate.getJdbcTemplate().queryForList(sql);
	}

	@Override
    public AvailableDateTimes getAvailableDates(JdbcCustomTemplate jdbcCustomTemplate, String timeZone,
                                                          Long locationId, Long departmentId, Long resourceId, Long serviceId, Long blockTimeMins) throws Exception {
        Map<String, Object> inParameters = new HashMap<>();
        try {
        	String spName = "get_available_dates_sp";
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);

            inParameters.put(SPConstants.TIME_ZONE.getValue(), timeZone);
            inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
            inParameters.put(SPConstants.DEPARTMENT_ID.getValue(), departmentId);
            inParameters.put(SPConstants.RESOURCE_ID.getValue(), resourceId);
            inParameters.put(SPConstants.SERVICE_ID.getValue(), serviceId);
            inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), blockTimeMins);

            logger.info("getAvailableDatesCallcenter input params = " + inParameters);
            long startTime = System.currentTimeMillis();
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
            long endTime = System.currentTimeMillis();
            logTimeTaken(spName, startTime, endTime);

            Object statusResult = simpleJdbcCallResult.get(SPConstants.AVAILABLE_DATES.getValue());
            Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
            return new AvailableDateTimes(statusResult == null ? "" : statusResult.toString(), errorMsg == null ? null : errorMsg.toString());
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_2034.getCode(), ErrorConstants.ERROR_2034.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "getAvailableDates input params = " + inParameters);
        }

    }

	@Override
    public void releaseHoldAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long scheduleId) throws Exception {
        try {
            logger.info("releaseHoldAppointment input params: scheduleId: " + scheduleId);
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName("release_hold_appt_sp");
            Map<String, Object> inParameters = new HashMap<String, Object>();
            inParameters.put(SPConstants.SCHEDULE_ID.getValue(), scheduleId);
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
            Object statusResult = simpleJdbcCallResult.get(SPConstants.STATUS_RESULT.getValue());
            Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
            if (statusResult != null && "N".equals((String) statusResult)) {
                logger.error("ReleaseHoldAppointment failed!");
            }
            if (errorMsg != null && !"".equals(errorMsg)) {
                logger.error("Error Message from release hold appt is: " + errorMsg);
            }
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_2035.getCode(), ErrorConstants.ERROR_2035.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "ScheduleId: " + scheduleId);
        }
    }

	@Override
	public void updateSchedule(JdbcCustomTemplate jdbcCustomTemplate, ConfirmAppointmentRequest confirmApptReq) throws Exception {
		String sql = "update schedule set comments=:comments, service_id=:serviceId where id=:scheduleId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("comments", confirmApptReq.getComments());
		paramSource.addValue("serviceId", confirmApptReq.getServiceId());
		paramSource.addValue("scheduleId", confirmApptReq.getScheduleId());
		jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql, paramSource);
	}

	@Override
	public void updateCustomerIdInSchedule(JdbcCustomTemplate jdbcCustomTemplate, Long customerId, Long scheduleId) throws Exception {
		String sql = "update schedule set customer_id=? where id=?";
		jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{customerId, scheduleId});
	}

    @Override
    public Boolean getCampaign(JdbcCustomTemplate jdbcCustomTemplate,  int campaignId) {

		String sql = "select id from campaign where name = 'Cancel by Admin'";
		List<Long> campaignList = jdbcCustomTemplate.getJdbcTemplate().query(sql, (resultSet, i) -> resultSet.getLong("id"));
		return !(campaignList == null || campaignList.isEmpty());

    }

	@Override
	public Map<String, Object> getCampaignById(JdbcCustomTemplate jdbcCustomTemplate, int campaignId) {
		return jdbcCustomTemplate.getJdbcTemplate().queryForMap("select id from campaign where id = ?", new Object[]{campaignId});
	}

	@Override
	public List<Map<String,Object>> getCalMinAndMaxDateTime(JdbcCustomTemplate jdbcCustomTemplate, Set<Integer> resourceIds, Timestamp fromDateTime) {

		String resourceIdsStr = org.apache.commons.lang3.StringUtils.join(resourceIds, ",");
		StringBuilder sql = new StringBuilder();
		sql.append("select min(date_time) min_date_time,max(date_time) max_date_time from resource_calendar c");
		sql.append(" where c.date_time >= '" + fromDateTime);
		sql.append("' and c.resource_id in (" + resourceIdsStr + ")");
		sql.append(" order by c.date_time asc");

		return jdbcCustomTemplate.getJdbcTemplate().queryForList(sql.toString());
	}

	@Override
	public List<Map<String,Object>> getCalMinAndMaxDateTime(JdbcCustomTemplate jdbcCustomTemplate, Set<Integer> resourceIds, Timestamp fromTimestamp, Timestamp toTimestamp) {
		String resourceIdsStr = org.apache.commons.lang3.StringUtils.join(resourceIds, ",");
		StringBuilder sql = new StringBuilder();
		sql.append("select min(date_time) min_date_time,max(date_time) max_date_time from resource_calendar c");
		sql.append(" where c.date_time >= '" + fromTimestamp);
		sql.append("' and c.date_time <= '" + toTimestamp);
		sql.append("' and c.resource_id in (" + resourceIdsStr + ")");
		sql.append(" order by c.date_time asc");

		return jdbcCustomTemplate.getJdbcTemplate().queryForList(sql.toString());
	}

	@Override
	public List<Map<String, Object>> fetchBookedAppointments(JdbcCustomTemplate jdbcCustomTemplate, String sql) {
		return jdbcCustomTemplate.getJdbcTemplate().queryForList(sql);
	}
	
	@Override
	public List<Map<String, Object>> fetchBookedAppointments(JdbcCustomTemplate jdbcCustomTemplate, String sql, MapSqlParameterSource paramSource) {
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForList(sql, paramSource);
	}

	@Override
	public List<Map<String, Object>> getScheduleList(JdbcCustomTemplate jdbcCustomTemplate, Set<Long> scheduleIds) {
		return jdbcCustomTemplate.getJdbcTemplate().queryForList("select id, customer_id, appt_date_time from schedule where id in ("+ String.join(",", new ArrayList(scheduleIds))+ ")");
	}

    @Override
    public Schedule getScheduleById(JdbcCustomTemplate jdbcCustomTemplate, long scheduleId) {
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject("select id, customer_id, appt_date_time, blocks, resource_id from schedule where id = ?", Schedule.class, new Object[] {scheduleId});
	}

	@Override
	public boolean updateScheduleCancel(JdbcCustomTemplate jdbcCustomTemplate, ResourceWorkingHrsResponse baseApptRequest, long scheduleId, int status) {
		Schedule schedule = getScheduleById(jdbcCustomTemplate, scheduleId);

		String action = "cancel:";
		if (status == AppointmentStatus.DISPLACEMENT_CANCEL.getStatus()) {
			action = "disp:";
		}
		String updatedBy = schedule.getUpdatedBy();
		if (updatedBy == null) updatedBy = "";
		if ("".equals(baseApptRequest.getUserName()) || baseApptRequest.getUserName() == null) {
			updatedBy = "".equals(updatedBy) ? action + baseApptRequest.getDeviceType() : updatedBy + ", " + action + baseApptRequest.getDeviceType();
			updatedBy = updatedBy + "@" + DateUtils.getCurrentDateMMDDYYYY_HHMM_TWELWE_HOURS(new Date());
		} else {
			updatedBy = updatedBy + ", " + action + baseApptRequest.getDeviceType() + ":" + baseApptRequest.getUserName();
			updatedBy = updatedBy + "@" + DateUtils.getCurrentDateMMDDYYYY_HHMM_TWELWE_HOURS(new Date());
		}
		updatedBy = updatedBy.replaceAll("ivr_audio", "ivr");
		schedule.setUpdatedBy(updatedBy);

		if (null != schedule) {
//			schedule.setStatus(status);
			int updateCount = update(jdbcCustomTemplate, schedule);
			if (updateCount > 0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public Appointment getAppointmentByScheduleId(JdbcCustomTemplate jdbcCustomTemplate, long scheduleId) {
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject("select * from appointment where schedule_id = ? ", Appointment.class, new Object[]{scheduleId});
	}

	@Override
	public int updateAppointment(JdbcCustomTemplate jdbcCustomTemplate, Appointment appointment) {
		String sql = "update appointment set appt_type = ? , timestamp = now() " +
				appointment.getOutlook_google_sync() == null ? "" : " , outlook_google_sync = ? "
				+ " where conf_number  =? and schedule_id = ? ";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql,
				appointment.getOutlook_google_sync() == null  ? new Object[]{appointment.getAppt_type(), appointment.getConf_number(), appointment.getScheduleId()}
				: new Object[]{appointment.getAppt_type(), appointment.getOutlook_google_sync(), appointment.getConf_number(), appointment.getScheduleId()});

	}

	@Override
	public Appointment getAppointmentByScheduleId(JdbcCustomTemplate jdbcCustomTemplate, Long transId, Long scheduleId) {
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject("select * from appointment where schedule_id = ? and trans_id = ?",
				Appointment.class, new Object[]{scheduleId, transId});
	}

	@Override
	public Map<String, Object> getApptSysConfig(JdbcCustomTemplate jdbcCustomTemplate) {
		String SQL = "select * from `appt_sys_config`";
		System.out.println("SQL = " + SQL);
		return jdbcCustomTemplate.getJdbcTemplate().queryForMap(SQL);
	}

	@Override
	public List<Map<String, Object>> getResourceCalendarForApptDate(JdbcCustomTemplate jdbcCustomTemplate, Object resourceId, Set<String> dateTimes, long fetchId) {

		StringBuilder sql = new StringBuilder();
		String timesStr = String.join(",", dateTimes);
		String singleQuoteStr = singleQuoteString(timesStr);
		sql.append("select * from resource_calendar res ");
		sql.append(" where res.date_time in (" + singleQuoteStr + ")");
		sql.append(" and res.resource_id=" + resourceId);
		sql.append(" and res.schedule_id=" + fetchId);

		return jdbcCustomTemplate.getJdbcTemplate().queryForList(sql.toString());
	}


	public static String singleQuoteString(String input) {
		String loginSplit[] = input.split("\\,");
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < loginSplit.length; i++) {
			sb.append("'" + loginSplit[i] + "'");
			if (i + 1 < loginSplit.length == true)
				sb.append(",");
		}
		return sb.toString();
	}


	@Override
	public void updateResourceCalendar(JdbcCustomTemplate jdbcCustomTemplate, List<Map<String, Object>> resourceCalendarList) {
		String SQL = "update resource_calendar set schedule_id = ? where id = ?";
		jdbcCustomTemplate.getJdbcTemplate().batchUpdate(SQL, new ResourceCalendarBatchUpdater(resourceCalendarList));
	}

	private int update(JdbcCustomTemplate jdbcCustomTemplate, Schedule schedule) {
		String SQL = "update schedule set status = ? , updated_by = ?  where id = ?";
		return jdbcCustomTemplate.getJdbcTemplate().update(SQL, new Object[]{schedule.getStatus(), schedule.getUpdatedBy(), schedule.getScheduleId()});
	}


	@Override
	public void updateNotifyCancelStatus(JdbcCustomTemplate jdbcCustomTemplate, Long scheduleId) {

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE notify n SET n.delete_flag='Y',n.notify_status=");
			sql.append(NotifyStatusConstants.NOTIFY_STATUS_COMPLETE.getNotifyStatus());
			sql.append(",n.notify_phone_status=");
			sql.append(NotifyStatusConstants.NOTIFY_STATUS_SUSPENDED.getNotifyStatus());
			sql.append(" where n.schedule_id=");
			sql.append(scheduleId);
			jdbcCustomTemplate.getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			logger.error("SQLError:" + e, e);
		}
	}

	
	
	@Override
	public int updateResourceCalendarClose(JdbcCustomTemplate jdbcCustomTemplate, String updateQueryClose) {
		return jdbcCustomTemplate.getJdbcTemplate().update(updateQueryClose);
	}
	
	@Override
	public int updateResourceCalendarClose(JdbcCustomTemplate jdbcCustomTemplate, String updateQueryClose, MapSqlParameterSource paramSource) {
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(updateQueryClose, paramSource);
	}

	@Override
	public int updateResourceCalendarOpen(JdbcCustomTemplate jdbcCustomTemplate, String updateQueryOpen) {
		return jdbcCustomTemplate.getJdbcTemplate().update(updateQueryOpen);
	}
	
	@Override
	public int updateResourceCalendarOpen(JdbcCustomTemplate jdbcCustomTemplate, String updateQueryOpen, MapSqlParameterSource paramSource) {
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(updateQueryOpen, paramSource);
	}

	@Override
	public void addResourceWorkingHrsHistory(JdbcCustomTemplate jdbcCustomTemplate, ResourceHoursRequest resourceHoursRequest, ResourceWorkingHrsHistory resourceWorkingHrsHistory) {

		String SQL = "INSERT INTO `resource_working_hrs_history` (`username`, `resource_id`, `location_id`,`effective_date`, `end_date`, " +
				"`sun_start_time`, `sun_end_time`, `sun_break_time_1`, `sun_break_time_1_mins`, `sun_break_time_2`, `sun_break_time_2_mins`, `sun_break_time_3`, `sun_break_time_3_mins`, `sun_break_time_4`, `sun_break_time_4_mins`, " +
				"`mon_start_time`, `mon_end_time`, `mon_break_time_1`, `mon_break_time_1_mins`, `mon_break_time_2`, `mon_break_time_2_mins`, `mon_break_time_3`, `mon_break_time_3_mins`, `mon_break_time_4`, `mon_break_time_4_mins`, " +
				"`tue_start_time`, `tue_end_time`, `tue_break_time_1`, `tue_break_time_1_mins`, `tue_break_time_2`, `tue_break_time_2_mins`, `tue_break_time_3`, `tue_break_time_3_mins`, `tue_break_time_4`, `tue_break_time_4_mins`, " +
				"`wed_start_time`, `wed_end_time`, `wed_break_time_1`, `wed_break_time_1_mins`, `wed_break_time_2`, `wed_break_time_2_mins`, `wed_break_time_3`, `wed_break_time_3_mins`, `wed_break_time_4`, `wed_break_time_4_mins`, " +
				"`thu_start_time`, `thu_end_time`, `thu_break_time_1`, `thu_break_time_1_mins`, `thu_break_time_2`, `thu_break_time_2_mins`, `thu_break_time_3`, `thu_break_time_3_mins`, `thu_break_time_4`, `thu_break_time_4_mins`, " +
				"`fri_start_time`, `fri_end_time`, `fri_break_time_1`, `fri_break_time_1_mins`, `fri_break_time_2`, `fri_break_time_2_mins`, `fri_break_time_3`, `fri_break_time_3_mins`, `fri_break_time_4`, `fri_break_time_4_mins`, " +
				"`sat_start_time`, `sat_end_time`, `sat_break_time_1`, `sat_break_time_1_mins`, `sat_break_time_2`, `sat_break_time_2_mins`, `sat_break_time_3`, `sat_break_time_3_mins`, `sat_break_time_4`, `sat_break_time_4_mins`, `timestamp`)\n" +
				"VALUES\n" +
				"\t(?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"now())";

		jdbcCustomTemplate.getJdbcTemplate().update(SQL, new Object[]{resourceHoursRequest.getUserName(), resourceWorkingHrsHistory.getResourceId(), resourceWorkingHrsHistory.getLocationId(),resourceHoursRequest.getStartDate(), resourceHoursRequest.getEndDate(),
				resourceWorkingHrsHistory.getSun_start_time(), resourceWorkingHrsHistory.getSun_end_time(),resourceWorkingHrsHistory.getSun_break_time_1(), resourceWorkingHrsHistory.getSun_break_time_1_mins(), null, 0, null, 0, null, 0,
				resourceWorkingHrsHistory.getMon_start_time(), resourceWorkingHrsHistory.getMon_end_time(),resourceWorkingHrsHistory.getMon_break_time_1(), resourceWorkingHrsHistory.getMon_break_time_1_mins(), null, 0, null, 0, null, 0,
				resourceWorkingHrsHistory.getTue_start_time(), resourceWorkingHrsHistory.getTue_end_time(),resourceWorkingHrsHistory.getTue_break_time_1(), resourceWorkingHrsHistory.getTue_break_time_1_mins(), null, 0, null, 0, null, 0,
				resourceWorkingHrsHistory.getWed_start_time(), resourceWorkingHrsHistory.getWed_end_time(),resourceWorkingHrsHistory.getWed_break_time_1(), resourceWorkingHrsHistory.getWed_break_time_1_mins(), null, 0, null, 0, null, 0,
				resourceWorkingHrsHistory.getThu_start_time(), resourceWorkingHrsHistory.getThu_end_time(),resourceWorkingHrsHistory.getThu_break_time_1(), resourceWorkingHrsHistory.getThu_break_time_1_mins(), null, 0, null, 0, null, 0,
				resourceWorkingHrsHistory.getFri_start_time(), resourceWorkingHrsHistory.getFri_end_time(),resourceWorkingHrsHistory.getFri_break_time_1(), resourceWorkingHrsHistory.getFri_break_time_1_mins(), null, 0, null, 0, null, 0,
				resourceWorkingHrsHistory.getSat_start_time(), resourceWorkingHrsHistory.getSat_end_time(),resourceWorkingHrsHistory.getSat_break_time_1(), resourceWorkingHrsHistory.getSat_break_time_1_mins(), null, 0, null, 0, null, 0
		});
	}
	
	

	@Override
	public void saveNotify(JdbcCustomTemplate jdbcCustomTemplate, Notify notify) {
		String SQL = "INSERT INTO `notify` (`timestamp`, `campaign_id`, `call_now`, `emergency_notify`, `broadcast_mode`, `notify_status`, " +
				"`resource_id`, `location_id`, `service_id`, `customer_id`, `schedule_id`, `first_name`, `middle_name`, `last_name`, " +
				"`phone1`, `phone2`, `phone3`, `home_phone`, `work_phone`, `cell_phone`, `cell_phone_prov`, " +
				"`email`, `email_cc`, `email_bcc`, `lang_id`, `notify_preference`, `notify_by_phone`, `notify_by_phone_confirm`, " +
				"`notify_by_sms`, `notify_by_sms_confirm`, `notify_by_email`, `notify_by_email_confirm`, `notify_by_push_notif`, " +
				"`notify_phone_status`, `notify_sms_status`, `notify_email_status`, `notify_push_notification_status`, " +
				"`include_audio_1`, `include_audio_2`, `include_audio_3`, `include_audio_4`, `include_audio_5`, " +
				"`do_not_notify`, `comment`, `delete_flag`, `due_date_time`, `attrib1`, `attrib2`, `attrib3`, `attrib4`, " +
				"`attrib5`, `attrib6`, `attrib7`, `attrib8`, `attrib9`, `attrib10`, `attrib11`, `attrib12`, `attrib13`, " +
				"`attrib14`, `attrib15`, `attrib16`, `attrib17`, `attrib18`, `attrib19`, `attrib20`, `notes`)\n" +
				"VALUES\n" +
				"\t(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";

		jdbcCustomTemplate.getJdbcTemplate().update(SQL, new Object[]{notify.getTimestamp(), notify.getCampaignId()
				, notify.getCall_now(), notify.getEmergency_notify(), notify.getBroadcast_mode(), notify.getNotify_status(),
				notify.getResourceId(), notify.getLocationId(), notify.getServiceId(), notify.getCustomerId(), notify.getScheduleId(), notify.getFirst_name(), notify.getMiddle_name(), notify.getLast_name(),
				notify.getPhone1(), notify.getPhone2(), notify.getPhone3(), notify.getHome_phone(), notify.getCell_phone(), notify.getCell_phone_prov(),
				notify.getEmail(), notify.getEmail_cc(), notify.getEmail_bcc(), notify.getLang_id(), notify.getNotify_preference(), notify.getNotify_by_phone(),
				notify.getNotify_by_phone_confirm(), notify.getNotify_by_sms(), notify.getNotify_by_sms_confirm(), notify.getNotify_by_email(), notify.getNotify_by_email_confirm(),
				notify.getNotify_by_push_notif(), notify.getNotify_phone_status(), notify.getNotify_sms_status(), notify.getNotify_email_status(),
				notify.getNotify_push_notification_status(), notify.getInclude_audio_1(), notify.getInclude_audio_2(), notify.getInclude_audio_3(), notify.getInclude_audio_4(), notify.getInclude_audio_5(),
				notify.getDo_not_notify(), notify.getComment(), notify.getDelete_flag(), notify.getDue_date_time(), notify.getAttrib1(), notify.getAttrib2(), notify.getAttrib3(), notify.getAttrib4()
				, notify.getAttrib5(), notify.getAttrib6(), notify.getAttrib7(), notify.getAttrib8(), notify.getAttrib9(), notify.getAttrib10()
				, notify.getAttrib11(), notify.getAttrib12(), notify.getAttrib12(), notify.getAttrib13(), notify.getAttrib14(), notify.getAttrib15(), notify.getAttrib16(), notify.getAttrib17()
				, notify.getAttrib18(), notify.getAttrib19(), notify.getAttrib20(), notify.getNotes()});
	}

	@Override
	public List<ResourceDisplayTime> getResourceDisplayTime(JdbcCustomTemplate jdbcCustomTemplate, String time, Integer resourceId) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select * from resource_display_time where '").append(time);
		sql.append("' >= start_time and '").append(time);
		sql.append("' <= end_time and resource_id =").append(resourceId);

		return jdbcCustomTemplate.getJdbcTemplate().queryForList(sql.toString(), ResourceDisplayTime.class);
	}

	@Override
	public List<Map<String, Object>> getDisplayTimeInConfirmPage(JdbcCustomTemplate jdbcCustomTemplate, String bookedTime, Integer resourceId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select display_time, display_time_range from resource_display_time");
		sql.append(" where 1=1");
		sql.append(" and resource_id =").append(resourceId);
		sql.append(" and '"+bookedTime+"' between start_time and end_time");
		sql.append(" order by display_time asc");
		return jdbcCustomTemplate.getJdbcTemplate().queryForList(sql.toString());

	}


	private class ResourceCalendarBatchUpdater implements BatchPreparedStatementSetter {
		private List<Map<String, Object>> resourceCalendarList;

		public ResourceCalendarBatchUpdater(List<Map<String, Object>> resourceCalendarList) {
			this.resourceCalendarList = resourceCalendarList;
		}

		@Override
		public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
			Map<String, Object> resourceCalendar = resourceCalendarList.get(i);
			preparedStatement.setInt(1, (Integer) resourceCalendar.get("schedule_id"));
			preparedStatement.setInt(2, (Integer) resourceCalendar.get("id"));
		}

		@Override
		public int getBatchSize() {
			return resourceCalendarList.size();
		}
	}


	@Override
	public Timestamp getMaxDateTime(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select max(date_time) from resource_calendar";
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, Timestamp.class);
	}

	@Override
	public void insertResourceCalendar(JdbcCustomTemplate jdbcCustomTemplate, List<SqlParameterSource> paramSourceList) throws Exception {
		String sql = "insert into resource_calendar (resource_id, date_time, schedule_id) values (:resourceId, :dateTime, :scheduleId)"; 		
		if(!paramSourceList.isEmpty()) {
			SqlParameterSource mapArray[] = new SqlParameterSource[paramSourceList.size()];
			SqlParameterSource batchArray[] = paramSourceList.toArray(mapArray);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		}
	}

	@Override
	public boolean addResourceService(JdbcCustomTemplate jdbcCustomTemplate, ServiceVO service) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into resource_service (resource_id,service_id,`enable`) values (:resourceId, :serviceId, :enable)");
		
		List<Integer> allResourceIdList = getResourceIds(jdbcCustomTemplate);
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		MapSqlParameterSource paramSource = null;
		for(Integer resourceId : allResourceIdList) {
			paramSource = new MapSqlParameterSource();
			paramSource.addValue("resourceId", resourceId);
			paramSource.addValue("serviceId", service.getServiceId());
			paramSource.addValue("enable", "N");
			list.add(paramSource);
		}

		if(!list.isEmpty()) {
			SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
			SqlParameterSource batchArray[] = list.toArray(mapArray);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		}
		
		return true;
	}
	
	public String getTimeInDBFormat(String time) throws Exception {
		return DateUtils.convert12To24HoursHHMMSSFormat(time);
	}
	
	@Override
	public boolean insertResourceWorkingHrsHistory(JdbcCustomTemplate jdbcCustomTemplate, ResourceWorkingHoursRequest resourceWorkingHoursReq, String date) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO `resource_working_hrs_history` (`username`, `resource_id`, `location_id`,`effective_date`, `end_date`, `timestamp`");
		
		String day = "";
		if(resourceWorkingHoursReq.isDayOpen()) {
			day = DateUtils.getWeekDay(DateUtils.getDateFromString(date, CommonDateContants.DATE_FORMAT_YYYYMMDD.getValue()));
			day = day.toLowerCase();
			sql.append(",").append(day).append("_start_time");
			sql.append(",").append(day).append("_end_time");
			
			if(resourceWorkingHoursReq.isBreakTimeOpen()) {
				sql.append(",").append(day).append("_break_time_1");
				sql.append(",").append(day).append("_break_time_1_mins");
			}
		}
		
		sql.append(") VALUES (:userName,:resourceId,:locationId,:effectiveDate,:endDate,now()");
		if(resourceWorkingHoursReq.isDayOpen()) {
			sql.append(",:").append(day).append("StartTime");
			sql.append(",:").append(day).append("EndTime");
			
			if(resourceWorkingHoursReq.isBreakTimeOpen()) {
				sql.append(",:").append(day).append("BreakTime1");
				sql.append(",:").append(day).append("BreakTimeInMins");
			}
		}
		sql.append(")");
		
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		MapSqlParameterSource paramSource = null;
		for(Integer resourceId : resourceWorkingHoursReq.getSelectedResourceIds()) {
			paramSource = new MapSqlParameterSource();
			paramSource.addValue("userName", resourceWorkingHoursReq.getUserName());
			paramSource.addValue("resourceId", resourceId);
			paramSource.addValue("locationId", resourceId);
			paramSource.addValue("effectiveDate", date);
			paramSource.addValue("endDate", date);
			if(resourceWorkingHoursReq.isDayOpen()) {
				paramSource.addValue(day+"StartTime", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedStartTime()));
				paramSource.addValue(day+"EndTime", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedEndTime()));
				
				if(resourceWorkingHoursReq.isBreakTimeOpen()) {
					paramSource.addValue(day+"BreakTime1", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedBreakTime()));
					paramSource.addValue(day+"BreakTimeInMins", resourceWorkingHoursReq.getSelectedDuration());
				}
			}
			list.add(paramSource);
		}
		if(!list.isEmpty()) {
			SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
			SqlParameterSource batchArray[] = list.toArray(mapArray);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		}
		
		return true;
	}
	
	@Override
	public void updateResourceSpecificDate(JdbcCustomTemplate jdbcCustomTemplate, ResourceWorkingHoursRequest resourceWorkingHoursReq, String date) throws Exception {
		updateDeleteFlagIfExist(jdbcCustomTemplate, resourceWorkingHoursReq, date);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into resource_specific_date (resource_id,`date`,start_time_1, end_time_1,");
		if(resourceWorkingHoursReq.isBreakTimeOpen()) {
			sql.append("start_time_2, end_time_2,");
		}
		sql.append("timestamp,username) values (:resourceId,:dateStr,:startTime1,:endTime1,");
		if(resourceWorkingHoursReq.isBreakTimeOpen()) {
			sql.append(":startTime2, :endTime2,");
		}
		sql.append("now(), :userName) ");
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		MapSqlParameterSource paramSource = null;
		for(Integer resourceId : resourceWorkingHoursReq.getSelectedResourceIds()) {
			paramSource = new MapSqlParameterSource();
			paramSource.addValue("userName", resourceWorkingHoursReq.getUserName());
			paramSource.addValue("resourceId", resourceId);
			paramSource.addValue("dateStr", date);
			paramSource.addValue("startTime1", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedStartTime()));
			paramSource.addValue("endTime1", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedEndTime()));
			if(resourceWorkingHoursReq.isBreakTimeOpen()) {
				paramSource.addValue("startTime2", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedBreakTime()));
				String endBreakTime =  CoreUtils.addTimeSlotHHMMSS(getTimeInDBFormat(resourceWorkingHoursReq.getSelectedBreakTime()), resourceWorkingHoursReq.getSelectedDuration());
				paramSource.addValue("endTime2", endBreakTime);
				
			}
			paramSource.addValue("userName",resourceWorkingHoursReq.getUserName());
			list.add(paramSource);
		}
		if(!list.isEmpty()) {
			SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
			SqlParameterSource batchArray[] = list.toArray(mapArray);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		}
	}
	
	private void updateDeleteFlagIfExist(JdbcCustomTemplate jdbcCustomTemplate, ResourceWorkingHoursRequest resourceWorkingHoursReq, String date) {
		StringBuilder sql = new StringBuilder();
		sql.append("update resource_specific_date set delete_flag='Y' where resource_id=:resourceId and `date`>=:dateStr order by id desc limit 1");
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		MapSqlParameterSource paramSource = null;
		for(Integer resourceId : resourceWorkingHoursReq.getSelectedResourceIds()) {
			paramSource = new MapSqlParameterSource();
			paramSource.addValue("resourceId", resourceId);
			paramSource.addValue("dateStr", date);
			list.add(paramSource);
		}
		if(!list.isEmpty()) {
			SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
			SqlParameterSource batchArray[] = list.toArray(mapArray);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		}
		
	}

	@Override
	public List<OneDateResourceWorkingHoursDetails> getOneDateResourceWorkingHoursDetails(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		StringBuilder sql = new StringBuilder(" select r.id,r.prefix,r.first_name,r.last_name,r.title,r.email, l.location_name_online, ");
		sql.append("rsd.date,IF(rsd.start_time_1 IS NOT NULL,date_format(rsd.start_time_1,'%l:%i %p'),'') as start_time_1,");
		sql.append("IF(rsd.end_time_1 IS NOT NULL,date_format(rsd.end_time_1,'%l:%i %p'),'') as end_time_1,");
		sql.append(" IF(rsd.start_time_2 IS NOT NULL,date_format(rsd.start_time_2,'%l:%i %p'),'') as start_time_2,");
		sql.append(" IF(rsd.end_time_2 IS NOT NULL,date_format(rsd.end_time_2,'%l:%i %p'),'') as end_time_2, ");
		sql.append(" rsd.timestamp, rsd.username, rsd.delete_flag ");
		sql.append(" from resource r, resource_specific_date rsd , location l ");
		sql.append(" where r.id=rsd.resource_id and r.location_id=l.id and rsd.date >= DATE_FORMAT(now(),'%Y-%m-%d')");
		sql.append(" order by date asc");
		logger.info("sql:"+sql.toString());
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), oneDateResWorkingHoursDetailsMapper());
	}
	
	 public static RowMapper<OneDateResourceWorkingHoursDetails> oneDateResWorkingHoursDetailsMapper() {
        return (rs, rowNum) -> {
        	OneDateResourceWorkingHoursDetails oneDateRes = new OneDateResourceWorkingHoursDetails();
        	oneDateRes.setResourceId(rs.getInt("id"));
        	oneDateRes.setFirstName(rs.getString("first_name"));
            oneDateRes.setLastName(rs.getString("last_name"));
            oneDateRes.setPrefix(rs.getString("prefix"));
            oneDateRes.setTitle(rs.getString("title"));
            oneDateRes.setEmail(rs.getString("email"));
            oneDateRes.setLocationName(rs.getString("location_name_online"));
            oneDateRes.setDateStr(rs.getString("date"));
            oneDateRes.setStartTime1(rs.getString("start_time_1"));
            oneDateRes.setEndTime1(rs.getString("end_time_1"));
            oneDateRes.setStartTime2(rs.getString("start_time_2"));
            oneDateRes.setEndTime2(rs.getString("end_time_2"));
            oneDateRes.setTimestamp(rs.getString("timestamp"));
            oneDateRes.setUserName(rs.getString("username"));
            return oneDateRes;
        };
    }
	 
	 
	@Override
	public List<UserActivityLog> getUserActivityLog(JdbcCustomTemplate jdbcCustomTemplate, Integer userId, String startDate, String endDate) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from activity_log where user_id=:userId and DATE(timestamp) >=:startDate and DATE(timestamp) <=:endDate order by timestamp desc");
		logger.info("sql:" + sql.toString());
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("userId", userId);
		paramSource.addValue("startDate", startDate);
		paramSource.addValue("endDate", endDate);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, userActivityLogMapper());
	}

	public static RowMapper<UserActivityLog> userActivityLogMapper() {
		return (rs, rowNum) -> {
			UserActivityLog userActivityLog = new UserActivityLog();
			userActivityLog.setUserId(rs.getInt("user_id"));
			userActivityLog.setUserName(rs.getString("user_name"));
			userActivityLog.setUserFirstName(rs.getString("user_first_name"));
			userActivityLog.setUserLastName(rs.getString("user_last_name"));
			userActivityLog.setSessionId(rs.getString("session_id"));
			userActivityLog.setPageId(rs.getInt("page_id"));
			userActivityLog.setPageName(rs.getString("page_name"));
			userActivityLog.setClickId(rs.getInt("click_id"));
			userActivityLog.setClickName(rs.getString("click_name"));
			userActivityLog.setSummaryLog(rs.getString("summary_log"));
			return userActivityLog;
		};
	}
	
	@Override
	public List<AccessPrivilege> getAccessPrivilege(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql ="select * from access_privilege";
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), accessPrivilege());
	}
	
	public static RowMapper<AccessPrivilege> accessPrivilege() {
		return (rs, rowNum) -> {
			AccessPrivilege accessPrivilege = new AccessPrivilege();
			accessPrivilege.setId(rs.getLong("id"));
			accessPrivilege.setPrivilege(rs.getString("privilege"));
			return accessPrivilege;
		};
	}
	
	@Override
	public List<String> getPrivilegeMapping(JdbcCustomTemplate jdbcCustomTemplate, int privilageId) throws Exception {
		String sql ="select page_name from privilege_page_mapping where privilege_id=?";
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{privilageId},accessPrivilegeMapping());
	}
	
	public static RowMapper<String> accessPrivilegeMapping() {
		return (rs, rowNum) -> {
			return rs.getString("page_name");
		};
	}

	@Override
	public List<CustomerPledgeStatus> getCustomerPledgeStatusList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		StringBuilder sql = new StringBuilder();
	    sql.append("select id, `status` from customer_pledge_status where delete_flag = 'N' order by placement");
	    return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), customerPledgeStatusMapper());
	}

	private RowMapper<CustomerPledgeStatus> customerPledgeStatusMapper() {
		return (rs, num) -> {
			CustomerPledgeStatus customerPledgeStatus = new CustomerPledgeStatus();
			customerPledgeStatus.setId(""+rs.getInt("id"));
			customerPledgeStatus.setStatus(rs.getString("status"));
			return customerPledgeStatus; 
		};
	}
	
	@Override
    public boolean updateCustomerForPledge(JdbcCustomTemplate jdbcCustomTemplate, CustomerPledgeRequest customerPledgeReq) throws Exception {
    	Customer customer = customerPledgeReq.getCustomer();
        StringBuilder sql = new StringBuilder();
        sql.append(" update customer set first_name = :firstName");
        sql.append(", last_name = :lastName");
        sql.append(", contact_phone = :contactPhone");
        sql.append(", email = :email");
        sql.append(", attrib1 = :attrib1");
        sql.append(", address = :address");
        sql.append(", city = :city");
        sql.append(", zip_postal = :zipCode");
        sql.append(", household_id = :houseHoldId");
        sql.append(" where id = :customerId");
  
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("firstName",StringEscapeUtils.escapeSql(customer.getFirstName()));
        paramSource.addValue("lastName",StringEscapeUtils.escapeSql(customer.getLastName()));
        paramSource.addValue("contactPhone",customer.getContactPhone());
        paramSource.addValue("email",customer.getEmail());
        paramSource.addValue("attrib1",customer.getAttrib1());
        paramSource.addValue("address",customer.getAddress());
        paramSource.addValue("city",customer.getCity());
        paramSource.addValue("zipCode",customer.getZipCode());
        paramSource.addValue("houseHoldId",customer.getHouseHoldId());
        paramSource.addValue("customerId",customer.getCustomerId());
        return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource) !=0;
    }
	
	 @Override
	 public boolean addCustomerPledgeDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomerPledgeRequest customerPledgeReq, boolean fromAppt) throws Exception {
		Customer customer = customerPledgeReq.getCustomer();
		List<CustomerPledge> customerPledgeList = customerPledgeReq.getCustomerPledgeList();
		Timestamp timestamp = DateUtils.getCurrentTimeStampYYYYMMDDHHMMSSCAPS(customerPledgeReq.getTimeZone());
		TransactionDefinition def = new DefaultTransactionDefinition();
		PlatformTransactionManager transactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionStatus status = transactionManager.getTransaction(def);
        try {
        	updateCustomerForPledge(jdbcCustomTemplate, customerPledgeReq);
        	updateLHEAPandPSEHelpRecievedStatus(jdbcCustomTemplate, customerPledgeReq);
			for (int i = 0; i < customerPledgeList.size(); i++) {
				CustomerPledge customerPledge = customerPledgeList.get(i);
				StringBuilder sql = new StringBuilder();
				MapSqlParameterSource paramSource = new MapSqlParameterSource();
				if (customerPledge.getTotalPledgeAmt() != null && !"".equals(customerPledge.getTotalPledgeAmt()) && !"null".equals(customerPledge.getTotalPledgeAmt())
						&& !"0".equals(customerPledge.getTotalPledgeAmt())) {
					sql.append(" insert into customer_pledge (customer_id,pledge_datetime,");
					if (customerPledge.getScheduleId() != null && customerPledge.getScheduleId().longValue() > 0) {
						sql.append("schedule_id,");
	
					}
					sql.append("fund_id,total_amount, ");
					sql.append(" pledge_status_id, pmt_updated_by,pmt_updated_by_name");
	
					if (customerPledge.getLocationId() != null && !"".equals(customerPledge.getLocationId())) {
						sql.append(",location_id");
					}
					if (customerPledge.getResourceId() != null && !"".equals(customerPledge.getResourceId())) {
						sql.append(",resource_id");
					}
	
					if (customerPledge.getServiceId() != null && !"".equals(customerPledge.getServiceId())) {
						sql.append(",service_id");
					}
	
					sql.append(",urgent_status");
					sql.append(",updated_status");
					sql.append(",calledin_status");
	
					if (customerPledge.getPrimaryStatus() != null && customerPledge.getPrimaryStatus().length() > 0 && !"Select".equalsIgnoreCase(customerPledge.getPrimaryStatus())) {
						sql.append(",primary_status");
					}
					if (customerPledge.getSecondaryStatus() != null && customerPledge.getSecondaryStatus().length() > 0
							&& !"Select".equalsIgnoreCase(customerPledge.getSecondaryStatus())) {
						sql.append(",secondary_status");
					}
	
					sql.append(") values ");
					sql.append(" ( ");
					sql.append(":customerId");
					sql.append(", ");
					sql.append(":timestamp");
					paramSource.addValue("customerId", customer.getCustomerId());
					paramSource.addValue("timestamp", timestamp.toString());
					if (customerPledge.getScheduleId() != null && customerPledge.getScheduleId() > 0) {
						sql.append(", :scheduleId");
						paramSource.addValue("scheduleId", customerPledge.getScheduleId());
					}
	
					sql.append(",:fundId ");
					paramSource.addValue("fundId", customerPledge.getFundId());
					sql.append(",:totalPledgeAmt");
					if (customerPledge.getTotalPledgeAmt() == null || "".equals(customerPledge.getTotalPledgeAmt())) {
						paramSource.addValue("totalPledgeAmt", Double.parseDouble("0.00"));
					} else {
						paramSource.addValue("totalPledgeAmt", Double.parseDouble(customerPledge.getTotalPledgeAmt()));
					}
	
					sql.append(", :pledgeStatusId ");
					paramSource.addValue("pledgeStatusId", customerPledge.getPledgeStatusId());
	
					sql.append(", :pmtUpdatedBy");
					paramSource.addValue("pmtUpdatedBy", customerPledge.getPmtUpdateBy());
	
					sql.append(", :pmtUpdateByName");
					paramSource.addValue("pmtUpdateByName", customerPledge.getPmtUpdateByName());
	
					if (customerPledge.getLocationId() != null && customerPledge.getLocationId() > 0) {
						sql.append(",:locationId");
						paramSource.addValue("locationId", customerPledge.getLocationId());
					}
	
					if (customerPledge.getResourceId() != null && customerPledge.getResourceId() > 0) {
						sql.append(",:resourceId");
						paramSource.addValue("resourceId", customerPledge.getResourceId());
					}
	
					if (customerPledge.getServiceId() != null && !"".equals(customerPledge.getServiceId())) {
						sql.append(",:serviceId");
						paramSource.addValue("serviceId", customerPledge.getServiceId());
					}
	
					if (customerPledge.getUrgentStatus() != null && customerPledge.getUrgentStatus().length() > 0) {
						sql.append(", :urgentStatus");
						paramSource.addValue("urgentStatus", customerPledge.getUrgentStatus());
					} else {
						sql.append(", 'N'");
					}
	
					if (customerPledge.getUpdatedStatus() != null && customerPledge.getUpdatedStatus().length() > 0) {
						sql.append(", :updatedStatus");
						paramSource.addValue("updatedStatus", customerPledge.getUpdatedStatus());
					} else {
						sql.append(", 'N'");
					}
					if (customerPledge.getCalledinStatus() != null && customerPledge.getCalledinStatus().length() > 0) {
						sql.append(", :calledInStatus");
						paramSource.addValue("updatedStatus", customerPledge.getCalledinStatus());
					} else {
						sql.append(", 'N'");
					}
	
					if (customerPledge.getPrimaryStatus() != null && customerPledge.getPrimaryStatus().length() > 0 && !"Select".equalsIgnoreCase(customerPledge.getPrimaryStatus())) {
						sql.append(", :primaryStatus");
						paramSource.addValue("primaryStatus", customerPledge.getPrimaryStatus());
					}
					if (customerPledge.getSecondaryStatus() != null && customerPledge.getSecondaryStatus().length() > 0
							&& !"Select".equalsIgnoreCase(customerPledge.getSecondaryStatus())) {
						sql.append(", :secondaryStatus");
						paramSource.addValue("secondaryStatus", customerPledge.getSecondaryStatus());
					}
	
					sql.append(" ) ");
					jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource);
	
				}
	
				Long customerPledgeId = jdbcCustomTemplate.getJdbcTemplate().queryForObject("select LAST_INSERT_ID() from customer_pledge", Long.class);
				for (int j = 0; j < 3; j++) {
					sql = new StringBuilder();
					sql.append("insert into customer_pledge_vendor(customer_pledge_id, vendor_id, vendor_pledge_amount, account_number) values ( ");
					sql.append(customerPledgeId.longValue());
					sql.append(", ");
					int id = j + 1;
					sql.append(CoreUtils.getPropertyValue(customerPledge, "vendor" + id + "Id"));
					sql.append(", ");
					Object payment = CoreUtils.getPropertyValue(customerPledge, "vendor" + id + "Payment");
					if (payment == null || "".equals(payment)) {
						sql.append(Double.parseDouble("0.00"));
						continue;
					} else {
						sql.append((String) payment);
						if (Double.parseDouble((String) payment) <= 0.00) {
							continue;
						}
					}
					sql.append(", ");
					sql.append("'" + CoreUtils.getPropertyValue(customerPledge, "vendor" + id + "AccountNumber") + "'");
					sql.append(")");
					jdbcCustomTemplate.getJdbcTemplate().update(sql.toString());
				}
			}
			transactionManager.commit(status);
			return true;
        } catch(Exception e) {
        	transactionManager.rollback(status);
        	throw e;
        }
	}
	 
	@Override
	public boolean updateCustomerPledgeDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomerPledgeRequest customerPledgeReq) throws Exception {
		List<CustomerPledge> customerPledgeList = customerPledgeReq.getCustomerPledgeList();
		int i = 0;
		TransactionDefinition def = new DefaultTransactionDefinition();
		PlatformTransactionManager transactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			updateCustomerForPledge(jdbcCustomTemplate, customerPledgeReq);
			updateLHEAPandPSEHelpRecievedStatus(jdbcCustomTemplate, customerPledgeReq);
			for (CustomerPledge customerPledge : customerPledgeList) {
				if (i == 0) {
					StringBuilder sql = new StringBuilder();
					MapSqlParameterSource paramSource = new MapSqlParameterSource();
					Timestamp timestamp = DateUtils.getCurrentTimeStampYYYYMMDDHHMMSSCAPS(customerPledgeReq.getTimeZone());
					sql.append("update customer_pledge set pledge_datetime = :timestamp");
					sql.append(", fund_id").append("=:fundId");
					sql.append(", total_amount").append("=:totalAmount");
					sql.append(", pledge_status_id =:pledgeStatusId");
					sql.append(", pmt_updated_by = :pmtUpdatedBy");
					sql.append(", pmt_updated_by_name = :pmtUpdateByName");
					
					paramSource.addValue("timestamp", timestamp.toString());
					paramSource.addValue("fundId", customerPledge.getFundId());
					paramSource.addValue("totalAmount", customerPledge.getTotalPledgeAmt());
					paramSource.addValue("pledgeStatusId",customerPledge.getPledgeStatusId());
					paramSource.addValue("pmtUpdatedBy",customerPledge.getPmtUpdateBy());
					paramSource.addValue("pmtUpdateByName",customerPledge.getPmtUpdateByName());

					if (customerPledge.getScheduleId() != null && customerPledge.getScheduleId() > 0) {
						sql.append(", schedule_id = :scheduleId");
						paramSource.addValue("scheduleId", customerPledge.getScheduleId());
					} else {
						sql.append(", schedule_id = NULL");
					}

					if (customerPledge.getLocationId() != null && customerPledge.getLocationId() > 0) {
						sql.append(", location_id =:locationId");
						paramSource.addValue("locationId", customerPledge.getLocationId());
					}
					if (customerPledge.getResourceId() != null && customerPledge.getResourceId() > 0) {
						sql.append(",resource_id=:resourceId");
						paramSource.addValue("resourceId", customerPledge.getResourceId());
					}

					if (customerPledge.getServiceId() != null && customerPledge.getServiceId() > 0) {
						sql.append(",service_id=:serviceId");
						paramSource.addValue("serviceId", customerPledge.getServiceId());
					}

					if (customerPledge.getUrgentStatus() != null && customerPledge.getUrgentStatus().length() > 0) {
						sql.append(",urgent_status=:urgentStatus");
						paramSource.addValue("urgentStatus", customerPledge.getUrgentStatus());
					} else {
						sql.append(",urgent_status='N'");
					}

					if (customerPledge.getUpdatedStatus() != null && customerPledge.getUpdatedStatus().length() > 0) {
						sql.append(",updated_status=:updatedStatus").append(customerPledge.getUpdatedStatus());
						paramSource.addValue("updatedStatus", customerPledge.getUpdatedStatus());
					} else {
						sql.append(",updated_status='N'");
					}

					if (customerPledge.getCalledinStatus() != null && customerPledge.getCalledinStatus().length() > 0) {
						sql.append(",calledin_status=:calledInStatus");
						paramSource.addValue("calledInStatus",customerPledge.getCalledinStatus());
					} else {
						sql.append(",calledin_status='N'");
					}

					if (customerPledge.getPrimaryStatus() != null && customerPledge.getPrimaryStatus().length() > 0) {
						sql.append(",primary_status=:primaryStatus");
						paramSource.addValue("primaryStatus", customerPledge.getPrimaryStatus());
					}
					if (customerPledge.getSecondaryStatus() != null && customerPledge.getSecondaryStatus().length() > 0) {
						sql.append(",secondary_status=:secondaryStatus");
						paramSource.addValue("secondaryStatus", customerPledge.getSecondaryStatus());
					}
					
					sql.append(" where id = :customerPledgeId");
					paramSource.addValue("customerPledgeId", customerPledge.getCustomerPledgeId());
					jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource);

					sql = new StringBuilder();
					paramSource = new MapSqlParameterSource();
					sql.append("delete from customer_pledge_vendor where customer_pledge_id=:customerPledgeId");
					paramSource.addValue("customerPledgeId", customerPledge.getCustomerPledgeId());
					jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource);
				}
				i = i + 1;

				for (int j = 0; j < 3; j++) {
					StringBuilder sql = new StringBuilder();
					sql.append("insert into customer_pledge_vendor(customer_pledge_id, vendor_id, vendor_pledge_amount, account_number) values ( ");
					sql.append(customerPledge.getCustomerPledgeId());
					sql.append(", ");
					int id = j + 1;
					sql.append(CoreUtils.getPropertyValue(customerPledge, "vendor" + id + "Id"));

					sql.append(", ");

					Object payment = CoreUtils.getPropertyValue(customerPledge, "vendor" + id + "Payment");
					if (payment == null || "".equals(payment)) {
						sql.append(Double.parseDouble("0.00"));
						continue;
					} else {
						sql.append((String) payment);
						if (Double.parseDouble((String) payment) <= 0.00) {
							continue;
						}
					}

					sql.append(", ");
					sql.append("'" + CoreUtils.getPropertyValue(customerPledge, "vendor" + id + "AccountNumber") + "'");

					sql.append(")");
					jdbcCustomTemplate.getJdbcTemplate().update(sql.toString());
				}
			}
			transactionManager.commit(status);
			return true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public List<CustomerPledgeFundSource> getCustomerPledgeFundSourceList(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		StringBuilder sql = new StringBuilder();
        sql.append("select id, fund_name from customer_pledge_fund_source");
        return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), customerPledgeFundSourceMapper());
	}
	
	private RowMapper<CustomerPledgeFundSource> customerPledgeFundSourceMapper() {
		return (rs, num) -> {
			CustomerPledgeFundSource customerPledgeFundSource = new CustomerPledgeFundSource();
			customerPledgeFundSource.setFundId(""+rs.getInt("id"));
			customerPledgeFundSource.setFundName(rs.getString("fund_name"));
			return customerPledgeFundSource; 
		};
	}

	@Override
	public List<CustomerPledgeVendor> getCustomerPledgeVendorList(JdbcCustomTemplate jdbcCustomTemplate, String fundId) throws Exception {
		StringBuilder sql = new StringBuilder();
	    sql.append("select id, vendor_name from customer_vendor where fund_id = ?").append(" and delete_flag = 'N' order by placement");
	    return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{Integer.valueOf(fundId)}, customerPledgeVendorMapper());
	}
	
	private RowMapper<CustomerPledgeVendor> customerPledgeVendorMapper() {
		return (rs, num) -> {
			CustomerPledgeVendor customerPledgeVendor = new CustomerPledgeVendor();
			customerPledgeVendor.setVendorId(""+rs.getInt("id"));
			customerPledgeVendor.setVendorName(rs.getString("vendor_name"));
			return customerPledgeVendor; 
		};
	}

	@Override
	public List<CustomerPastAppts> getCustomerPastApptsList(JdbcCustomTemplate jdbcCustomTemplate, Long customerId) throws Exception {
		StringBuilder sql = new StringBuilder();
        sql.append("select s.id, DATE_FORMAT(s.appt_date_time, '%m/%d/%y %l:%i %p') as appt_date_time  from schedule s where s.customer_id =?");
        sql.append(" and s.`status` not in (1,2) and DATE(s.appt_date_time) <= CURDATE()");
        return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{customerId}, customerPastApptListMapper());
	}
	
	
	private RowMapper<CustomerPastAppts> customerPastApptListMapper() {
		return (rs, num) -> {
			CustomerPastAppts customerPastAppts = new CustomerPastAppts();
			customerPastAppts.setScheduleId(""+rs.getInt("id"));
			customerPastAppts.setApptDateTime(rs.getString("appt_date_time"));
			return customerPastAppts; 
		};
	}
	

	@Override
	public List<ItemizedReportGoal> getItemizedReportGoal(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		StringBuilder sql = new StringBuilder();
        sql.append("select id,row_goal  from itemized_report_goal");
        return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), reportGoalPercentageMapper());
	}
	
	
	private RowMapper<ItemizedReportGoal> reportGoalPercentageMapper() {
		return (rs, num) -> {
			ItemizedReportGoal goal = new ItemizedReportGoal();
			goal.setId(rs.getInt("id"));
			goal.setGoalPercentage(rs.getString("row_goal"));
			return goal; 
		};
	}
	
	public Customer getCustomerDetails(JdbcCustomTemplate jdbcCustomTemplate, Long customerId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.household_id, c.id, c.account_number, c.first_name, c.last_name, c.contact_phone, c.email, c.attrib1, c.address,  ");
        sql.append("c.city, c.state, c.zip_postal from customer c where c.id = ?");
        List<Customer> customers = jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{customerId}, customerDetailsMapper());
        if(customers != null && !customers.isEmpty()) {
        	return customers.get(0);
        }
        return null;
	}
	
	private RowMapper<Customer> customerDetailsMapper() {
		return (rs, num) -> {
			Customer customer = new Customer();
			customer.setHouseHoldId(rs.getLong("household_id"));
			customer.setCustomerId(rs.getLong("id"));
			customer.setAccountNumber(rs.getString("account_number"));
			customer.setFirstName(rs.getString("first_name"));
			customer.setLastName(rs.getString("last_name"));
			customer.setContactPhone(rs.getString("contact_phone"));
			customer.setEmail(rs.getString("email"));
			customer.setAttrib1(rs.getString("attrib1"));
			customer.setAddress(rs.getString("address"));
			customer.setCity(rs.getString("city"));
			customer.setState(rs.getString("state"));
			customer.setZipCode(rs.getString("zip_postal"));
			return customer; 
		};
	}

	@Override
	public boolean updateResourceCalendarWithScheduleId(
			JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, String date, List<String> timeList, int scheduleId) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("resourceId", resourceId);
		mapSqlParameterSource.addValue("date", date);
		mapSqlParameterSource.addValue("timeList", timeList);
		mapSqlParameterSource.addValue("scheduleId", scheduleId);

		String sql = "update resource_calendar set schedule_id= :scheduleId where resource_id = :resourceId " +
				"and DATE(date_time) = :date and TIME(date_time) in (:timeList)";

		return jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql, mapSqlParameterSource) > 0;
	}

	@Override
	public boolean closeScheduledAppointment(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, String date, List<String> timeList, int scheduleIdValue) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("resourceId", resourceId);
		mapSqlParameterSource.addValue("date", date);
		mapSqlParameterSource.addValue("timeList", timeList);

		String sql = "select * from resource_calendar where resource_id in( :resourceId)\n" +
				"and DATE(date_time) = :date and TIME(date_time) in (:timeList) ";

		List<Map<String, Object>> resourceCalendarList = jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForList(sql, mapSqlParameterSource);

		try{
			if (resourceCalendarList !=null && !resourceCalendarList.isEmpty()) {

				for (Map<String, Object> map : resourceCalendarList) {
					Long scheduleId = (Long) map.get("schedule_id");

					jdbcCustomTemplate.getJdbcTemplate().update("update resource_calendar set schedule_id = ? where id = " +
							"?",  AppointmentStatus.NOT_OPEN.getStatus(), map.get("id"));

					jdbcCustomTemplate.getJdbcTemplate().update("update schedule set status = ? , updated_by = ? , timestamp = now() where id = ?",
							AppointmentStatus.CANCEL.getStatus(), "close_appointment:online:admin@"+new Date().toString(), scheduleId);

					jdbcCustomTemplate.getJdbcTemplate().update("update appointment set appt_type = ? , timestamp = now() where schedule_id = ? ",
							AppointmentType.CANCEL.getType(), scheduleId);

					updateNotifyCancelStatus(jdbcCustomTemplate, Long.valueOf(scheduleId));

				}
			}
		} catch (Exception ex) {
			return false;
		}

		return true;
	}
	
	@Override
	public void deletePledge(JdbcCustomTemplate jdbcCustomTemplate, String customerPledgeId) throws Exception {
		String sql = "delete from customer_pledge_vendor where customer_pledge_id=?";
		jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{customerPledgeId});
		
		sql = "delete from customer_pledge where id=?";
		jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{customerPledgeId});
	}
	
	
	@Override
	public Map<String, List<JSPPagesPrivileges>> getPrivilegeByUserPrivilege(JdbcCustomTemplate jdbcCustomTemplate, String accessPrivilegeName) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from jsp_pages_privileges");
		sql.append(" where enable_flag='Y'");
		if(accessPrivilegeName.equalsIgnoreCase("administrator")) {
			sql.append(" and administrator='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("manager")) {
			sql.append(" and manager='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("location")) {
			sql.append(" and location='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("provider")) {
			sql.append(" and provider='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("scheduler")) {
			sql.append(" and scheduler='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("read_only")) {
			sql.append(" and read_only='Y'");
		} else if(accessPrivilegeName.equalsIgnoreCase("super-user")) {
			sql.append(" and super_user='Y'");
		}
		List<JSPPagesPrivileges> jspPagePrivileges = jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), privilegeSettingMapper());
		Map<String, List<JSPPagesPrivileges>> pageMap = new LinkedHashMap<>();
		for(JSPPagesPrivileges jspPagePrivilege : jspPagePrivileges) {
			if(pageMap.containsKey(jspPagePrivilege.getGroupTitle())) {
				List<JSPPagesPrivileges> jspPagesPrivileges = pageMap.get(jspPagePrivilege.getGroupTitle());
				jspPagesPrivileges.add(jspPagePrivilege);
				pageMap.put(jspPagePrivilege.getGroupTitle(), jspPagesPrivileges);
			} else {
				List<JSPPagesPrivileges> jspPagesPrivileges = new ArrayList<>();
				jspPagesPrivileges.add(jspPagePrivilege);
				pageMap.put(jspPagePrivilege.getGroupTitle(), jspPagesPrivileges);
			}
		}
		return pageMap;
	}	
	
}
