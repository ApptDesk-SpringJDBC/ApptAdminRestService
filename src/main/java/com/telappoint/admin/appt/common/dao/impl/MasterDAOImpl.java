package com.telappoint.admin.appt.common.dao.impl;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.telappoint.admin.appt.common.constants.ErrorConstants;
import com.telappoint.admin.appt.common.dao.MasterDAO;
import com.telappoint.admin.appt.common.model.AdminLogin;
import com.telappoint.admin.appt.common.model.AdminLoginConfig;
import com.telappoint.admin.appt.common.model.AppointmentReportConfig;
import com.telappoint.admin.appt.common.model.Client;
import com.telappoint.admin.appt.common.model.ClientDeploymentConfig;
import com.telappoint.admin.appt.common.model.JdbcCustomTemplate;
import com.telappoint.admin.appt.common.model.LoginAttempts;
import com.telappoint.admin.appt.common.model.ResetPassword;
import com.telappoint.admin.appt.handlers.exception.TelAppointException;

/**
 * @author Balaji, Koti
 */
@Repository
public class MasterDAOImpl implements MasterDAO {


	@Autowired
	private JdbcTemplate masterJdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * This will injected from spring content.
	 * @param jdbcTemplate
	 */
	public MasterDAOImpl(JdbcTemplate jdbcTemplate) {
		this.masterJdbcTemplate = jdbcTemplate;
	}

	public MasterDAOImpl() {
	}

	@Override
	public void getClients(final String key, final Map<String, Client> clientCacheMap) throws TelAppointException, Exception {
		String query = "select * from client c where delete_flag='N'";
		try {
			masterJdbcTemplate.query(query.toString(), (ResultSetExtractor<Map<String, Client>>)rs -> {
                Client client;
                String clientCode;
                while (rs.next()) {
                    clientCode = rs.getString("client_code");
                    client = new Client();
                    client.setClientId(rs.getInt("id"));
                    client.setClientCode(clientCode);
                    client.setClientName(rs.getString("client_name"));
                    client.setWebsite(rs.getString("website"));
                    client.setContactEmail(rs.getString("contact_email"));
                    client.setFax(rs.getString("fax"));
                    client.setAddress(rs.getString("address"));
                    client.setAddress2(rs.getString("address2"));
                    client.setCity(rs.getString("city"));
                    client.setState(rs.getString("state"));
                    client.setZip(rs.getString("zip"));
                    client.setCountry(rs.getString("country"));
                    client.setDbName(rs.getString("db_name"));
                    client.setDbServer(rs.getString("db_server"));
                    client.setCacheEnabled(rs.getString("cache_enabled"));
                    client.setApptLink(rs.getString("appt_link"));
                    client.setDirectAccessNumber(rs.getString("direct_access_number"));
					client.setExtension(rs.getString("extension"));
					client.setAppcode(rs.getString("appcode"));
                    client.setExtLoginId(rs.getInt("ext_login_id"));
                    client.setExtLoginPassword(rs.getString("ext_login_password"));
                    client.setLocked(rs.getString("locked"));
                    client.setClientDnis1(rs.getString("client_dnis_1"));
                    client.setClientDnis2(rs.getString("client_dnis_2"));
                    client.setClientDnis3(rs.getString("client_dnis_3"));
                    client.setRedirectUrl(rs.getString("redirect_url"));
                    client.setLicenceKey(rs.getString("license_key"));
                    clientCacheMap.put(key + "|" + clientCode, client);
                }
                return clientCacheMap;
            });
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1000.getCode(), ErrorConstants.ERROR_1000.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public void getClientDeploymentConfig(final String key, final String clientCode, int clientId, final Map<String, Object> cacheObjectMap) throws TelAppointException, Exception {
		String query = "select * from client_deployment_config c where client_id = ?";

		try {
			masterJdbcTemplate.query(query.toString(), new Object[] { clientId }, (ResultSetExtractor<Map<String, Object>>) rs -> {
                ClientDeploymentConfig clientDeploymentConfig;
                if (rs.next()) {
                    clientDeploymentConfig = new ClientDeploymentConfig();
                    clientDeploymentConfig.setTimeZone(rs.getString("time_zone"));
                    clientDeploymentConfig.setDateFormat(rs.getString("date_format"));
                    clientDeploymentConfig.setTimeFormat(rs.getString("time_format"));
                    clientDeploymentConfig.setDateyyyyFormat(rs.getString("date_yyyy_format"));
                    clientDeploymentConfig.setFullDateFormat(rs.getString("full_date_format"));
                    clientDeploymentConfig.setFullDatetimeFormat(rs.getString("full_datetime_format"));
                    clientDeploymentConfig.setFullTextualdayFormat(rs.getString("full_textualday_format"));
                    clientDeploymentConfig.setPhoneFormat(rs.getString("phone_format"));
                    clientDeploymentConfig.setPopupCalendardateFormat(rs.getString("popup_calendardate_format"));
                    clientDeploymentConfig.setLeadTimeInSeconds(rs.getInt("notify_phone_lead_time"));
                    clientDeploymentConfig.setLagTimeInSeconds(rs.getInt("notify_phone_lag_time"));
                    clientDeploymentConfig.setBlockTimeInMins(rs.getInt("block_time_in_mins"));
                    clientDeploymentConfig.setDayStartTime(rs.getString("day_start_time"));
                    clientDeploymentConfig.setDayEndTime(rs.getString("day_end_time"));
                    clientDeploymentConfig.setResourceCalendarMonths(rs.getInt("resource_calendar_months"));
                    
                    cacheObjectMap.put(key + "|" + clientCode, clientDeploymentConfig);
                }
                return cacheObjectMap;
            });
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1001.getCode(), ErrorConstants.ERROR_1001.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public AdminLogin getUserDetailsByUserId(Long loginUserId) throws TelAppointException {
		String query = "select * from admin_login_new c where id = ?";
		try {
			return masterJdbcTemplate.queryForObject(query, new Object[]{loginUserId}, adminLoginMapper());
					
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1002.getCode(), ErrorConstants.ERROR_1002.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	
	private RowMapper<AdminLogin> adminLoginMapper() {
		return (rs, rowNum) -> {
			AdminLogin adminLogin = new AdminLogin();
			adminLogin.setAccessLevel(rs.getString("access_level"));
			adminLogin.setLocationIds(rs.getString("location_ids"));
			adminLogin.setResourceIds(rs.getString("resource_ids"));
			return adminLogin;
		};
	}

	// added by balaji = start
	@Override
	public String getClientCode(Integer clientId) throws TelAppointException {
		String sql = "select client_code from client where id=? and delete_flag='N'";
		return masterJdbcTemplate.queryForObject(sql, new Object[]{clientId}, String.class);
	}
	

	@Override
	public AdminLoginConfig getAdminLoginConfig(int clientId) throws TelAppointException {
		String sql = "select * from admin_login_config where client_id=?";
		try {
			return masterJdbcTemplate.query(sql, new Object[]{clientId}, new ResultSetExtractor<AdminLoginConfig>() {
				@Override
				public AdminLoginConfig extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						AdminLoginConfig adminLoginConfig = new AdminLoginConfig();
						adminLoginConfig.setUserRestrictIps(rs.getString("user_restrict_ips"));
						adminLoginConfig.setPasswordExpireDays(rs.getInt("password_expire_days"));
						adminLoginConfig.setMaxWrongLoginAttempts(rs.getInt("max_wrong_login_attempts"));
						return adminLoginConfig;
					}
					return null;
				}	
			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1003.getCode(), ErrorConstants.ERROR_1003.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}


	@Override
	public AdminLogin getAdminLogin(String userName) throws TelAppointException {
		String sql = "select * from admin_login_new c where username = ?";
		try {
			List<AdminLogin> adminLoginList = masterJdbcTemplate.query(sql, new Object[]{userName}, adminLoginMapperData());
			if(adminLoginList != null && !adminLoginList.isEmpty()) {
				return adminLoginList.get(0);
			}
			return null;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1002.getCode(), ErrorConstants.ERROR_1002.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	
	@Override
	public AdminLogin getAdminLoginByUserId(Integer clientId, Integer userId) throws TelAppointException {
		String sql = "select * from admin_login_new c where id = ? and client_id=?";
		try {
			List<AdminLogin> adminLoginList = masterJdbcTemplate.query(sql, new Object[]{userId,clientId}, adminLoginMapperData());
			if(adminLoginList != null && !adminLoginList.isEmpty()) {
				AdminLogin adminLogin = adminLoginList.get(0);
				adminLogin.setPassword(null);
				return adminLogin;
			}
			return null;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1002.getCode(), ErrorConstants.ERROR_1002.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	private RowMapper<AdminLogin> adminLoginMapperData() {
		return (rs, rowNum) -> {
			AdminLogin adminLogin = new AdminLogin();
			adminLogin.setUserLoginId(rs.getInt("id"));
			adminLogin.setClientId(rs.getInt("client_id"));
			adminLogin.setUsername(rs.getString("username"));
			adminLogin.setPassword(rs.getString("password"));
			adminLogin.setFirstName(rs.getString("first_name"));
			adminLogin.setLastName(rs.getString("last_name"));
			adminLogin.setContactPhone(rs.getString("contact_phone"));
			adminLogin.setContactEmail(rs.getString("contact_email"));
			adminLogin.setLocationIds(rs.getString("location_ids"));
			adminLogin.setResourceIds(rs.getString("resource_ids"));
			adminLogin.setStartDate(rs.getString("start_date"));
			adminLogin.setExpiryDate(rs.getString("expire_date"));
			adminLogin.setSuspend(rs.getString("suspend"));
			adminLogin.setAccessLevel(rs.getString("access_level"));
			adminLogin.setPasswordLastUpdateDate(rs.getString("password_last_update_date"));
			adminLogin.setWrongLoginMaxAttemptLocked(rs.getString("wrong_login_max_attempt_locked"));
			return adminLogin;
		};
	}
	

	@Override
	public void saveLoginAttempts(LoginAttempts loginAttempts) throws TelAppointException {
		String sql = "insert into login_attempts(user_id,timestamp, ip_address, login_status) values (?,now(),?,?) ";
		masterJdbcTemplate.update(sql.toLowerCase(), new Object[]{loginAttempts.getUserId(), loginAttempts.getIpAddress(), loginAttempts.getLoginStatus()});
	}
	

	@Override
	public void updateLoginStatus(String loginStatus, Integer userId) throws TelAppointException {
		String sql = "update set login_status=? where user_id=? ";
		masterJdbcTemplate.update(sql, new Object[]{loginStatus, userId});
	}

	@Override
	public int getLoginAttempts(int userId) throws TelAppointException {
		String sql = "SELECT count(*) FROM login_attempts WHERE user_id= ? and timestamp BETWEEN  now()-INTERVAL 30 MINUTE and now()";
		return masterJdbcTemplate.queryForObject(sql, new Object[]{}, Integer.class);
	}
	
	@Override
	public LoginAttempts getLoginAttemptBean(int userId) throws TelAppointException {
		String sql = "select ip_address from login_attempts where user_id=? order by id desc limit 1";
		return masterJdbcTemplate.query(sql, new Object[]{userId}, new ResultSetExtractor<LoginAttempts>() {
			@Override
			public LoginAttempts extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					LoginAttempts loginAttemps = new LoginAttempts();
					loginAttemps.setIpAddress(rs.getString("ip_address"));
					return loginAttemps;
				}
				return null;
			}	
		});
	}
	
	@Override
	public boolean isPassowrdExpired(Integer userLoginId, int expiryDays) throws TelAppointException {
		String sql = "select DATE_ADD(password_last_update_date, interval ? DAY) >= now() FROM admin_login_new where id=?";
		int result = masterJdbcTemplate.queryForObject(sql, new Object[]{expiryDays, userLoginId}, Integer.class);
		return result != 0;
	}
	
	@Override
	public boolean addUser(JdbcCustomTemplate jdbcCustomTemplate, AdminLogin adminLogin) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into admin_login_new ");
		sql.append("(");
		sql.append("client_id,username, password,first_name,last_name, start_date");
		sql.append(",location_ids,resource_ids,contact_phone, contact_email,access_level,suspend");
		sql.append(")");
		sql.append(" values ").append("(");
		sql.append(":clientId,:userName,:password,:firstName,:lastName,now()");
		sql.append(",:locationIds,:resourceIds,:contactPhone,:contactEmail,:accessLevel,:suspend");
		sql.append(")");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("clientId", adminLogin.getClientId());
		paramSource.addValue("userName", adminLogin.getUsername());
		paramSource.addValue("password", adminLogin.getPassword());
		paramSource.addValue("firstName", adminLogin.getFirstName());
		paramSource.addValue("lastName", adminLogin.getLastName());
		paramSource.addValue("locationIds", adminLogin.getLocationIds()==null?"":adminLogin.getLocationIds());
		paramSource.addValue("resourceIds", adminLogin.getResourceIds()==null?"":adminLogin.getResourceIds());
		paramSource.addValue("contactPhone", adminLogin.getContactPhone());
		paramSource.addValue("contactEmail", adminLogin.getContactEmail());
		paramSource.addValue("accessLevel", adminLogin.getAccessLevel());
		paramSource.addValue("suspend", adminLogin.getSuspend()==null?"N":adminLogin.getSuspend());
		return namedParameterJdbcTemplate.update(sql.toString(), paramSource)!=0;
	}
	
	public boolean userExist(Integer clientId, String username) throws Exception {
		String sql = "select count(id) from admin_login_new where username=?";
		return masterJdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class) != 0;
	}
	
	public boolean userExist(Integer clientId, Integer userId, String username) throws Exception {
		String sql = "select count(id) from admin_login_new where username=?";
		Integer count = masterJdbcTemplate.queryForObject(sql, new Object[]{username.toLowerCase()}, Integer.class);
		if( userId == 0 && count == 0) {
			//add user.
			return false;
		} else if(userId==0  && count > 0) {
			return true;
		} else if(userId>0) {
			AdminLogin adminLogin = getAdminLoginByUserId(clientId, userId);
			if(adminLogin != null && adminLogin.getUsername().equalsIgnoreCase(username)) {
				return false;
			} else if(count > 0) {
				return true;
			} 
		} 
		return false;
	}

	@Override
	public boolean updateUser(JdbcCustomTemplate jdbcCustomTemplate, AdminLogin adminLogin) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("update admin_login_new set client_id=:clientId");
		if("Y".equals(adminLogin.getPasswordUpdate())) {
			sql.append(",`password`=:password");
		}
		sql.append(",first_name=:firstName");
		sql.append(",last_name=:lastName");
		sql.append(",location_ids=:locationIds");
		sql.append(",resource_ids=:resourceIds");
		sql.append(",contact_phone=:contactPhone, contact_email=:contactEmail, access_level=:accessLevel");
		sql.append(",suspend=:suspend");
		sql.append(" where id=:userId");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("clientId", adminLogin.getClientId());
		if("Y".equals(adminLogin.getPasswordUpdate())) {
			paramSource.addValue("password", adminLogin.getPassword());
		}
		paramSource.addValue("firstName", adminLogin.getFirstName());
		paramSource.addValue("lastName", adminLogin.getLastName());
		paramSource.addValue("locationIds", adminLogin.getLocationIds()==null?"":adminLogin.getLocationIds());
		paramSource.addValue("resourceIds", adminLogin.getResourceIds()==null?"":adminLogin.getResourceIds());
		paramSource.addValue("contactPhone", adminLogin.getContactPhone());
		paramSource.addValue("contactEmail", adminLogin.getContactEmail());
		paramSource.addValue("accessLevel", adminLogin.getAccessLevel());
		paramSource.addValue("suspend", adminLogin.getSuspend()==null?"N":adminLogin.getSuspend());
		paramSource.addValue("userId", adminLogin.getUserLoginId());
		return namedParameterJdbcTemplate.update(sql.toString(), paramSource)!=0;
	}

	@Override
	public List<AdminLogin> getUserList(int clientId) throws TelAppointException {
		String sql = "select * from admin_login_new c where client_id = ? and suspend='N'";
		try {
			return masterJdbcTemplate.query(sql, new Object[]{clientId}, adminLoginMapperData());
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1002.getCode(), ErrorConstants.ERROR_1002.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	@Override
	public List<AdminLogin> getSuspendUserList(int clientId) throws TelAppointException {
		String sql = "select * from admin_login_new c where client_id = ? and suspend='Y'";
		try {
			return masterJdbcTemplate.query(sql, new Object[]{clientId}, adminLoginMapperData());
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1002.getCode(), ErrorConstants.ERROR_1002.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public boolean addAppointmentReportConfig(JdbcCustomTemplate jdbcCustomTemplate, AppointmentReportConfig apptReportConfig) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into appointment_report_config ").append("(");
		sql.append("timestamp,username,client_id,report_name,location_ids,resource_ids");
		sql.append(",service_ids,procedure_ids,department_ids,report_columns,apptstatus_fetch");
		sql.append(",report_path, email1, email2, email3, email4, email5, email6");
		sql.append(",sortby1,sortby2,sortby3,sortby4,sortby5,report_stop");
		sql.append(",no_interval_hrs,report_no_days,file_format, last_run_date, `enable`, time_of_report").append(")");
		sql.append(" value (now(),:userName, :clientId, :reportName,:locationIds,:resourceIds,:serviceIds,:procedureIds,:departmentIds,:reportColumns,:apptstatusFetch");
		sql.append(",:reportPath,:email1,:email2,:email3,:email4,:email5, :email6");
		sql.append(",:sortBy1,:sortBy2,:sortBy3,:sortBy4,:sortBy5,:reportStop");
		sql.append(",:noIntervalHrs,:reportNoDays,:fileFormat,now(),:enable,:timeOfReport");
		sql.append(")");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("userName",apptReportConfig.getUserName());
		paramSource.addValue("clientId", apptReportConfig.getClientId());
		paramSource.addValue("reportName", apptReportConfig.getReportName());
		paramSource.addValue("locationIds", apptReportConfig.getLocationIds() ==null?"":apptReportConfig.getLocationIds());
		paramSource.addValue("resourceIds", apptReportConfig.getResourceIds() ==null?"":apptReportConfig.getResourceIds());
		paramSource.addValue("serviceIds", apptReportConfig.getServiceIds() == null?"":apptReportConfig.getServiceIds());
		paramSource.addValue("procedureIds", apptReportConfig.getProcedureIds() == null?"":apptReportConfig.getProcedureIds());
		paramSource.addValue("departmentIds", apptReportConfig.getDepartmentIds() == null?"":apptReportConfig.getDepartmentIds());
		paramSource.addValue("reportColumns", apptReportConfig.getReportColumns());
		paramSource.addValue("apptstatusFetch", apptReportConfig.getApptStatusFetch());
		String reportPath = apptReportConfig.getReportPath()==null?apptReportConfig.getReportName():apptReportConfig.getReportPath().toUpperCase()+"/";
		paramSource.addValue("reportPath", "/appt_report/"+reportPath);
		paramSource.addValue("email1", apptReportConfig.getEmail1()==null?"":apptReportConfig.getEmail1());
		paramSource.addValue("email2", apptReportConfig.getEmail2()==null?"":apptReportConfig.getEmail2());
		paramSource.addValue("email3", apptReportConfig.getEmail3()==null?"":apptReportConfig.getEmail3());
		paramSource.addValue("email4", apptReportConfig.getEmail4()==null?"":apptReportConfig.getEmail4());
		paramSource.addValue("email5", apptReportConfig.getEmail5()==null?"":apptReportConfig.getEmail5());
		paramSource.addValue("email6", apptReportConfig.getEmail6()==null?"":apptReportConfig.getEmail6());
		paramSource.addValue("sortBy1", apptReportConfig.getSortBy1()==null?"":apptReportConfig.getSortBy1());
		paramSource.addValue("sortBy2", apptReportConfig.getSortBy2()==null?"":apptReportConfig.getSortBy2());
		paramSource.addValue("sortBy3", apptReportConfig.getSortBy3()==null?"":apptReportConfig.getSortBy3());
		paramSource.addValue("sortBy4", apptReportConfig.getSortBy4()==null?"":apptReportConfig.getSortBy4());
		paramSource.addValue("sortBy5", apptReportConfig.getSortBy5()==null?"":apptReportConfig.getSortBy5());
		paramSource.addValue("reportStop", apptReportConfig.getReportStop()==null?"":apptReportConfig.getReportStop());
		paramSource.addValue("noIntervalHrs", apptReportConfig.getNoIntervalHrs()==null?24:apptReportConfig.getNoIntervalHrs());
		paramSource.addValue("reportNoDays", apptReportConfig.getReportNoDays()==null?1:apptReportConfig.getReportNoDays());
		paramSource.addValue("fileFormat", apptReportConfig.getFileFormat()==null?"PDF":apptReportConfig.getFileFormat());
		paramSource.addValue("enable", apptReportConfig.getEnable()==null?"Y":apptReportConfig.getEnable());
		paramSource.addValue("timeOfReport", apptReportConfig.getTimeOfReport()==null?"06:00":apptReportConfig.getTimeOfReport());
		return namedParameterJdbcTemplate.update(sql.toString(), paramSource) != 0;
	}

	@Override
	public List<AppointmentReportConfig> getAppointmentReportConfig(String userName) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select DATE_FORMAT(arc.timestamp,'%m/%d/%Y %h:%i %p') as timestamp, arc.username, arc.report_name,arc.location_ids,arc.resource_ids,arc.service_ids,arc.apptstatus_fetch,arc.email1,arc.email2,arc.email3,arc.email4,arc.email5, arc.report_stop,arc.file_format,");
		sql.append(" DATE_FORMAT(arc.last_run_date,'%m/%d/%Y %l:%i %p') as last_run_date, TIME_FORMAT(arc.time_of_report,'%l:%i %p') as time_of_report,c.id as clientId, c.client_name");
		sql.append(" from appointment_report_config arc, client c where arc.username=? and arc.client_id=c.id");
		return masterJdbcTemplate.query(sql.toString(), new Object[]{userName}, appointmentReportConfigMapper());
	}

	private RowMapper<AppointmentReportConfig> appointmentReportConfigMapper() {
		return (rs, rowNum) -> {
			AppointmentReportConfig apptReportConfig = new AppointmentReportConfig();
			apptReportConfig.setClientId(rs.getInt("clientId"));
			apptReportConfig.setTimeStamp(rs.getString("timestamp"));
			apptReportConfig.setClientName(rs.getString("client_name"));
			apptReportConfig.setReportName(rs.getString("report_name"));
			apptReportConfig.setLocationIds(rs.getString("location_ids"));
			apptReportConfig.setResourceIds(rs.getString("resource_ids"));
			apptReportConfig.setServiceIds(rs.getString("service_ids"));
			apptReportConfig.setApptStatusFetch(rs.getString("apptstatus_fetch"));
			apptReportConfig.setEmail1(rs.getString("email1"));
			apptReportConfig.setEmail2(rs.getString("email2"));
			apptReportConfig.setEmail3(rs.getString("email3"));
			apptReportConfig.setEmail4(rs.getString("email4"));
			apptReportConfig.setEmail5(rs.getString("email5"));
			apptReportConfig.setReportStop(rs.getString("report_stop"));
			apptReportConfig.setFileFormat(rs.getString("file_format"));
			apptReportConfig.setLastRunDate(rs.getString("last_run_date"));
			apptReportConfig.setTimeOfReport(rs.getString("time_of_report"));
			apptReportConfig.setUserName(rs.getString("username"));
			return apptReportConfig;
		};
	}

	@Override
	public boolean deleteApptReportConfigById(Integer configId) throws Exception {
		String sql = "delete from appointment_report_config where id=?";
		return masterJdbcTemplate.update(sql.toString(), new Object[]{configId}) != 0 ;
	}

	@Override
	public String getPasswordComplexity(int clientId) throws Exception {
		String sql = "select password_complexity from admin_login_config where client_id=?";
		return masterJdbcTemplate.queryForObject(sql.toString(), new Object[]{clientId}, String.class);
	}

	@Override
	public String getOutLookClient(String clientCode, String userName, String password) throws Exception {
		StringBuilder sql = new StringBuilder();
	    sql.append("SELECT resource_id from outlook_login where username = :userName and password = PASSWORD(:password) and client_code = :clientCode");
	    MapSqlParameterSource paramSource = new MapSqlParameterSource();
	    paramSource.addValue("userName", userName);
	    paramSource.addValue("password", password);
	    paramSource.addValue("clientCode", clientCode);
		try {
			return namedParameterJdbcTemplate.queryForObject(sql.toString(), paramSource, String.class);
		} catch(EmptyResultDataAccessException erde) {
			return "0";
		}
	}
	
	@Override
	public String getPasswordComplexityLogic(String clientCode) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select password_complexity from admin_login_config where client_id in ");
		sql.append("  (select id from client where client_code=?)");
		return masterJdbcTemplate.queryForObject(sql.toString(), new Object[]{clientCode}, String.class);
	}
	
	@Override
	public String getPasswordComplexityLogicByUserName(String userName) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select password_complexity from admin_login_config where client_id in ");
		sql.append("  (select client_id from admin_login_new where username=?)");
		return masterJdbcTemplate.queryForObject(sql.toString(), new Object[]{userName}, String.class);
	}
	
	
	@Override
	public ResetPassword getPasswordComplexityLogic(int adminLoginNewId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select aln.username , alc.password_complexity , c.client_code ,alc.password_reset_algorithm ");
		sql.append(" from admin_login_new aln, admin_login_config alc ,client c  ");
		sql.append(" where alc.client_id=aln.client_id and aln.client_id=c.id and aln.id=?");
		List<ResetPassword> resetPasswordList =  masterJdbcTemplate.query(sql.toString(), new Object[]{adminLoginNewId}, passwordComplexityLogicMapper());
		if(resetPasswordList != null && resetPasswordList.isEmpty()) {
			return resetPasswordList.get(0);
		}
		return null;
	}
	
	@Override 
	public boolean isValidUser(String userName, Integer userId) throws Exception {
		String sql = "select count(*) from admin_login_new where id!=? and username=?";
		return masterJdbcTemplate.queryForObject(sql, new Object[]{userId, userName}, Integer.class) == 0;		
	}
	
	@Override
	public boolean updatePassword(ResetPassword resetPassword) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" update admin_login_new set password=?, password_last_update_date=now(), expire_date='2020-01-01 10:10:10' where username=?");
		return masterJdbcTemplate.update(sql.toString(), new Object[]{resetPassword.getNewpassword(), resetPassword.getUserName()}) != 0;
	}

	public static RowMapper<ResetPassword> passwordComplexityLogicMapper() {
		return (rs, rowNum) -> {
			ResetPassword resetPassword = new ResetPassword();
			resetPassword.setClientCode(rs.getString("client_code"));
			resetPassword.setUserName(rs.getString("username"));
			resetPassword.setPasswordComplexity(rs.getString("password_complexity"));
			resetPassword.setPasswordResetAlgorithm(rs.getString("password_reset_algorithm"));
			return resetPassword;
		};
	}

	@Override
	public void deletUser(Integer userId) throws Exception {
		String sql = "delete from admin_login_new where id=?";
		masterJdbcTemplate.update(sql, new Object[]{userId});
	}		
}