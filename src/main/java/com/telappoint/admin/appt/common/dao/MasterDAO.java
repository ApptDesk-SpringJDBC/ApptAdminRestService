package com.telappoint.admin.appt.common.dao;

import java.util.List;
import java.util.Map;

import com.telappoint.admin.appt.common.model.AdminLogin;
import com.telappoint.admin.appt.common.model.AdminLoginConfig;
import com.telappoint.admin.appt.common.model.AppointmentReportConfig;
import com.telappoint.admin.appt.common.model.Client;
import com.telappoint.admin.appt.common.model.JdbcCustomTemplate;
import com.telappoint.admin.appt.common.model.LoginAttempts;
import com.telappoint.admin.appt.common.model.ResetPassword;
import com.telappoint.admin.appt.handlers.exception.TelAppointException;

/**
 * @author Balaji,Koti
 */

public interface MasterDAO {
	
	public AdminLogin getUserDetailsByUserId(Long loginUserId) throws TelAppointException;
	public void getClients(final String key, final Map<String, Client> clientCacheMap) throws TelAppointException, Exception;
	public void getClientDeploymentConfig(final String key, String clientCode, int clientId,final Map<String, Object> cacheMap) throws TelAppointException, Exception;
	public String getClientCode(Integer clientId) throws TelAppointException;
	public void saveLoginAttempts(LoginAttempts loginAttempts) throws TelAppointException;
	public AdminLoginConfig getAdminLoginConfig(int clientId) throws TelAppointException;
	public AdminLogin getAdminLogin(String userName) throws TelAppointException;
	public List<AdminLogin> getUserList(int clientId) throws TelAppointException;
	public void updateLoginStatus(String loginStatus, Integer userId) throws TelAppointException;
	public int getLoginAttempts(int userId) throws TelAppointException;
	public boolean isPassowrdExpired(Integer userLoginId, int expiryDays) throws TelAppointException;
	public LoginAttempts getLoginAttemptBean(int userId) throws TelAppointException;
	boolean addUser(JdbcCustomTemplate jdbcCustomTemplate, AdminLogin adminLogin) throws Exception;
	public boolean userExist(Integer clientId, String username) throws Exception;
	public boolean userExist(Integer clientId, Integer userId, String username) throws Exception;
	public boolean updateUser(JdbcCustomTemplate jdbcCustomTemplate, AdminLogin adminLogin) throws Exception;
	public List<AdminLogin> getSuspendUserList(int clientId) throws TelAppointException;
	public boolean addAppointmentReportConfig(JdbcCustomTemplate jdbcCustomTemplate, AppointmentReportConfig apptReportConfig) throws Exception;
	public List<AppointmentReportConfig> getAppointmentReportConfig(String userName) throws Exception;
	public boolean deleteApptReportConfigById(Integer configId) throws Exception;
	public AdminLogin getAdminLoginByUserId(Integer clientId, Integer userId) throws TelAppointException;
	public String getPasswordComplexity(int clientId) throws Exception;
	public String getOutLookClient(String clientCode, String userName, String password) throws Exception;
	String getPasswordComplexityLogic(String clientCode) throws Exception;
	String getPasswordComplexityLogicByUserName(String userName) throws Exception;
	ResetPassword getPasswordComplexityLogic(int adminLoginNewIdId) throws Exception;
	boolean updatePassword(ResetPassword resetPassword) throws Exception;
	public void deletUser(Integer userId) throws Exception;
	boolean isValidUser(String userName, Integer userId) throws Exception;
}
