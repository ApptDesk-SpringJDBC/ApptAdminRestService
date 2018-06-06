package com.telappoint.admin.appt.common.service.impl;

import com.google.common.collect.Lists;
import com.telappoint.admin.appt.common.component.CacheComponent;
import com.telappoint.admin.appt.common.component.CommonComponent;
import com.telappoint.admin.appt.common.component.ConnectionPoolUtil;
import com.telappoint.admin.appt.common.component.EmailComponent;
import com.telappoint.admin.appt.common.constants.*;
import com.telappoint.admin.appt.common.core.AdminInstance;
import com.telappoint.admin.appt.common.dao.AdminDAO;
import com.telappoint.admin.appt.common.dao.MasterDAO;
import com.telappoint.admin.appt.common.model.*;
import com.telappoint.admin.appt.common.model.request.*;
import com.telappoint.admin.appt.common.model.response.*;
import com.telappoint.admin.appt.common.service.AdminRestService;
import com.telappoint.admin.appt.common.util.*;
import com.telappoint.admin.appt.handlers.exception.TelAppointException;
import com.telappoint.apptdeskrestws.common.model.BaseResponse;
import com.telappoint.apptdeskrestws.utils.CoreUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.telappoint.admin.appt.common.util.TimeUtils.geHourMinMeridian;

/**
 * @author: Balaji
 */

@Service
public class AdminRestServiceImpl implements AdminRestService {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private AdminDAO adminDAO;

    @Autowired
    private EmailComponent emailComponent;

    @Autowired
    private ConnectionPoolUtil connectionPoolUtil;

    @Autowired
    private CacheComponent cacheComponent;

    @Autowired
    private CommonComponent commonComponent;

    @Autowired
    private MasterDAO masterDAO;

    public ResponseEntity<ResponseModel> handleException(String clientCode, Exception tae) {
        String clientName = "";
        try {
            if (clientCode != null && !"".equals(clientCode)) {
                Client client = cacheComponent.getClient(clientCode, true);
                if (client != null) {
                    clientName = client.getClientName();
                }
            }
        } catch (Exception e) {
            logger.error("Error: " + e, e);
        }
        return ResponseModel.exceptionResponse(clientName, tae, emailComponent);
    }

    public ResponseEntity<ResponseModel> handleException(Exception tae) {
        return ResponseModel.exceptionResponse("Master ", tae, emailComponent);
    }

    /**
     * Get the home page response
     *
     * @param clientCode
     * @param loginUserId
     * @return
     * @throws Exception
     */

    @Override
    public ResponseEntity<ResponseModel> getHomePageResponse(String clientCode, Long loginUserId) throws Exception {
        HomePageResponse homePageResponse = new HomePageResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        List<Location> locationList = commonComponent.getLocationList(jdbcCustomTemplate, loginUserId, FilterKeyWordContants.LOCATIONS_HOME_PAGE_DATA.getFilterKey(), true);
        List<Resource> resourceList = commonComponent.getResourceList(jdbcCustomTemplate, loginUserId, true);
        List<ServiceVO> serviceList = commonComponent.getServiceList(jdbcCustomTemplate, cdConfig.getBlockTimeInMins(), loginUserId, FilterKeyWordContants.SERVICES_HOME_PAGE_DATA.getFilterKey(), true);

        int selectedlocId;
        if (locationList != null && !locationList.isEmpty()) {
            selectedlocId = locationList.get(0).getLocationId();
        } else {
            selectedlocId = -1;
        }

        List<Integer> locationIds = new ArrayList<Integer>();
        locationIds.add(selectedlocId);
        homePageResponse.setLocationList(locationList);
        homePageResponse.setResourceList(resourceList);
        homePageResponse.setServiceList(serviceList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(homePageResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getClientDetails(String clientCode) throws Exception {
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(cacheComponent.getClient(clientCode, true)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getHomePageLocationList(String clientCode, Long loginUserId) throws Exception {
        LocationResponse locRes = new LocationResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Location> locations = commonComponent.getLocationList(jdbcCustomTemplate, loginUserId, FilterKeyWordContants.LOCATIONS_HOME_PAGE_DATA.getFilterKey(), true);
        locRes.setLocationList(locations);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(locRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getActiveLocationDropDownData(String clientCode) throws Exception {
        LocationResponse locRes = new LocationResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Location> locations = adminDAO.getLocationList(jdbcCustomTemplate, FilterKeyWordContants.LOCATIONS_DROP_DOWN_DATA.getFilterKey(), true);
        locRes.setLocationList(locations);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(locRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getActiveResourceDropDownData(String clientCode) throws Exception {
        ResourceResponse locRes = new ResourceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Resource> resources = adminDAO.getResourceList(jdbcCustomTemplate, FilterKeyWordContants.RESOURCE_DROP_DOWN_DATA.getFilterKey(), true);
        locRes.setResourceList(resources);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(locRes), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseModel> getActiveServiceDropDownData(String clientCode) throws Exception {
        ServiceResponse serviceResponse = new ServiceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        List<ServiceVO> services = adminDAO.getServiceList(jdbcCustomTemplate, cdConfig.getBlockTimeInMins(), FilterKeyWordContants.SERVICES_DROP_DOWN_DATA.getFilterKey(), true);
        serviceResponse.setServiceList(services);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(serviceResponse), HttpStatus.OK);
    }

    /**
     * Return all location basic data to show the location details page. here it will return active + inactive location details.
     *
     * @param clientCode
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<ResponseModel> getAllLocationsBasicData(String clientCode) throws Exception {
        LocationResponse locRes = new LocationResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Location> locations = adminDAO.getLocationList(jdbcCustomTemplate, FilterKeyWordContants.LOCATIONS_BASIC_DATA.getFilterKey(), false);
        List<Location> deletedLocations = adminDAO.getDeletedLocationList(jdbcCustomTemplate, FilterKeyWordContants.LOCATIONS_BASIC_DATA.getFilterKey());
        locRes.setLocationList(locations);
        locRes.setDeletedLocationList(deletedLocations);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(locRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getLocationById(String clientCode, Integer locationId) throws Exception {
        LocationResponse locListRes = new LocationResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        Location location = adminDAO.getLocationById(jdbcCustomTemplate, FilterKeyWordContants.LOCATIONS_HOME_PAGE_DATA.getFilterKey(), locationId);
        locListRes.setLocation(location);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(locListRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getCompleteLocationDataById(String clientCode, Integer locationId) throws Exception {
        LocationResponse locListRes = new LocationResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        Location location = adminDAO.getLocationById(jdbcCustomTemplate, FilterKeyWordContants.LOCATION_COMPLETE_DATA.getFilterKey(), locationId);
        locListRes.setLocation(location);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(locListRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getDynamicFieldDisplayData(String clientCode, String pageName) throws Exception {
        DynamicFieldDisplayResponse dynamicFieldDisplayResponse = new DynamicFieldDisplayResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        dynamicFieldDisplayResponse.setDynamicFieldDisplay(adminDAO.getDynamicFieldDisplay(jdbcCustomTemplate, pageName));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(dynamicFieldDisplayResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getResourceList(String clientCode, Long loginUserId) throws Exception {
        ResourceResponse resourceListRes = new ResourceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Resource> resources = commonComponent.getResourceList(jdbcCustomTemplate, loginUserId, true);
        resourceListRes.setResourceList(resources);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(resourceListRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getAllResourcesBasicData(String clientCode) throws Exception {
        ResourceResponse resourceListRes = new ResourceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Resource> resources = adminDAO.getResourceList(jdbcCustomTemplate, FilterKeyWordContants.RESOURCES_BASIC_DATA.getFilterKey(), false);
        List<Resource> deletedResources = adminDAO.getDeletedResourceList(jdbcCustomTemplate, FilterKeyWordContants.RESOURCES_BASIC_DATA.getFilterKey());
        resourceListRes.setResourceList(resources);
        resourceListRes.setDeletedResourceList(deletedResources);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(resourceListRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getResourceById(String clientCode, Integer resourceId) throws Exception {
        ResourceResponse resourceRes = new ResourceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        Resource resource = adminDAO.getResourceById(jdbcCustomTemplate, resourceId, FilterKeyWordContants.RESOURCE_HOME_PAGE_DATA.getFilterKey(), true);
        resourceRes.setResource(resource);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(resourceRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getCompleteResourceDataById(String clientCode, Integer resourceId) throws Exception {
        ResourceResponse resourceRes = new ResourceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        Resource resource = adminDAO.getResourceById(jdbcCustomTemplate, resourceId, FilterKeyWordContants.RESOURCE_COMPLETE_DATA.getFilterKey(), true);
        resourceRes.setResource(resource);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(resourceRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getResourceListByLocationId(String clientCode, Integer locationId) throws Exception {
        ResourceResponse resourceRes = new ResourceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Resource> resourceList = adminDAO.getResourcesByLocationId(jdbcCustomTemplate, locationId, FilterKeyWordContants.RESOURCE_HOME_PAGE_DATA.getFilterKey(), true);
        resourceRes.setResourceList(resourceList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(resourceRes), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseModel> getServiceById(String clientCode, Integer serviceId) throws Exception {
        ServiceResponse serviceRes = new ServiceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        ServiceVO serviceVO = adminDAO.getServiceById(jdbcCustomTemplate, serviceId, cdConfig.getBlockTimeInMins(), FilterKeyWordContants.SERVICES_HOME_PAGE_DATA.getFilterKey(), false, true);
        serviceRes.setService(serviceVO);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(serviceRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getCompleteServiceDataById(String clientCode, Integer serviceId) throws Exception {
        ServiceResponse serviceRes = new ServiceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        ServiceVO serviceVO = adminDAO.getServiceById(jdbcCustomTemplate, serviceId, cdConfig.getBlockTimeInMins(), FilterKeyWordContants.SERVICE_COMPLETE_DATA.getFilterKey(), true, true);
        serviceRes.setBlocksTimeInMins(cdConfig.getBlockTimeInMins());
        serviceRes.setService(serviceVO);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(serviceRes), HttpStatus.OK);
    }

    /**
     * Return all services basic data to show the service details page. here it will return active + inactive service details.
     *
     * @param clientCode
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<ResponseModel> getAllServicesBasicData(String clientCode) throws Exception {
        ServiceResponse serviceRes = new ServiceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        List<ServiceVO> serviceList = adminDAO.getServiceList(jdbcCustomTemplate, cdConfig.getBlockTimeInMins(), FilterKeyWordContants.SERVICES_BASIC_DATA.getFilterKey(), false);
        List<ServiceVO> deletedServiceList = adminDAO.getDeletedServiceList(jdbcCustomTemplate, cdConfig.getBlockTimeInMins(), FilterKeyWordContants.SERVICES_BASIC_DATA.getFilterKey());
        serviceRes.setBlocksTimeInMins(cdConfig.getBlockTimeInMins());
        serviceRes.setServiceList(serviceList);
        serviceRes.setDeletedServiceList(deletedServiceList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(serviceRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getServiceListByResourceId(String clientCode, Integer resourceId, Integer loginUserId) throws Exception {
        ServiceResponse serviceRes = new ServiceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);

        //TODO: need to userId, later.
        List<ServiceVO> serviceList = adminDAO.getServiceListByResourceId(jdbcCustomTemplate, resourceId, cdConfig.getBlockTimeInMins(), FilterKeyWordContants.SERVICES_HOME_PAGE_DATA.getFilterKey(), true);
        serviceRes.setServiceList(serviceList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(serviceRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getGaugeChart(String clientCode, Integer locationId, String startDate, String endDate) throws Exception {
        GaugeChartResponse gaugeChartRes = new GaugeChartResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        if (null == endDate || "".equals(endDate)) {
            endDate = startDate;
        }
        String yyyyMMddDateStartDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(startDate);
        String yyyyMMddDateEndDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(endDate);

        List<Integer> resourceIds = adminDAO.getResourceIds(jdbcCustomTemplate, locationId);
        Long openCount = (long) 0;
        Long holdCount = (long) 0;
        Long bookedCount = (long) 0;
        if (!resourceIds.isEmpty()) {
            int minBlocks = adminDAO.getMinBlocks(jdbcCustomTemplate, resourceIds);
            openCount = adminDAO.getApptOpenTimeSlotsCount(jdbcCustomTemplate, locationId, resourceIds, yyyyMMddDateStartDate, yyyyMMddDateEndDate);
            holdCount = adminDAO.getHoldAppointmentsCount(jdbcCustomTemplate, resourceIds, yyyyMMddDateStartDate, yyyyMMddDateEndDate);
            openCount = openCount + holdCount;
            openCount = openCount / minBlocks;
            bookedCount = adminDAO.getBookedAppointmentsCount(jdbcCustomTemplate, resourceIds, yyyyMMddDateStartDate, yyyyMMddDateEndDate);
        }

        gaugeChartRes.setGaugeOpenedAppts(openCount);
        gaugeChartRes.setGaugeBookedAppts(bookedCount);
        gaugeChartRes.setGaugeStartDate(startDate);
        gaugeChartRes.setGaugeEndDate(endDate);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(gaugeChartRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getStackedChart(String clientCode, Integer locationId, Integer resourceId, String stackChartType) throws Exception {
        StackedChartResponse stackChartRes = new StackedChartResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        adminDAO.getStackChartInfo(jdbcCustomTemplate, logger, locationId, resourceId, stackChartType, stackChartRes);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(stackChartRes), HttpStatus.OK);
    }


    public String appenedResult(String inputSb, Object str1, boolean isPastDay) {
        StringBuilder sb = new StringBuilder(inputSb);
        if (!isPastDay) {
            if (sb.length() > 0) {
                sb = sb.append(",");
            }
            sb = sb.append(str1);
        } else {
            StringBuilder tempsb = new StringBuilder(String.valueOf(str1));
            if (sb.length() > 0) {
                tempsb.append(",");
            }
            sb = tempsb.append(sb);
        }
        return sb.toString();
    }

    @Override
    public ResponseEntity<ResponseModel> getPieChart(String clientCode, Integer locationId, String selectedDate) throws Exception {
        PieChartResponse pieChartRes = new PieChartResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        String yyyyMMddDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(selectedDate);
        String MMddYYDate = DateUtils.convertYYYYMMDD_TO_MMDDYYFormat(yyyyMMddDate);
        pieChartRes.setPieChartDate(MMddYYDate);
        pieChartRes.setSelectedDate(selectedDate);
        pieChartRes.setSelectedLocationId(locationId);
        List<Resource> resourceList = adminDAO.getResourcesByLocationId(jdbcCustomTemplate, locationId, FilterKeyWordContants.RESOURCE_HOME_PAGE_DATA.getFilterKey(), true);
        if (resourceList != null && !resourceList.isEmpty()) {
            for (Resource resource : resourceList) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(resource.getResourceId());
                Long bookedCount = adminDAO.getBookedAppointmentsCount(jdbcCustomTemplate, list, yyyyMMddDate, yyyyMMddDate);
                pieChartRes.setResources(appenedResult(pieChartRes.getResources(), AdminUtils.getResourceDisplayName(resource, true), false));
                if (bookedCount > 0) {
                    pieChartRes.setNoOfConfirmedAppts(appenedResult(pieChartRes.getNoOfConfirmedAppts(), bookedCount, false));
                } else {

                    //pieChartRes.setNoOfConfirmedAppts("0");
                }

            }
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(pieChartRes), HttpStatus.OK);
    }

    private boolean addLoginAttempts(int userid, String ipAddress) throws Exception {
        LoginAttempts loginAttempts = new LoginAttempts();
        loginAttempts.setIpAddress(ipAddress);
        loginAttempts.setLoginStatus(LoginConstants.LOGIN_ATEMPTS_LOGIN_STATUS_SUCESS.getValue());
        loginAttempts.setUserId(userid);
        masterDAO.saveLoginAttempts(loginAttempts);
        return true;
    }

    private boolean validateUserRestrictIPs(String user_restrict_ips, String ipAddress) {
        boolean valid = false;
        if (user_restrict_ips != null && user_restrict_ips != "") {
            List<String> ips = Arrays.asList(user_restrict_ips.split("\\s*,\\s*"));
            valid = ips.contains(ipAddress);
        } else {
            valid = true;
        }
        return valid;
    }

    private boolean validateUserPassword(AdminLogin adminLogin, String password, AdminLoginConfig adminLoginConfig) {
        try {
            String decriptPassword = AdminInstance.getInstance().decrypt(adminLogin.getPassword());
            if (password != null && adminLogin.getPassword() != null && password.equals(decriptPassword)) {
                return true;
            } else {
                int allowedWrongAttempts = adminLoginConfig.getMaxWrongLoginAttempts();
                masterDAO.updateLoginStatus(LoginConstants.LOGIN_ATEMPTS_LOGIN_STATUS_FAILURE.getValue(), adminLogin.getUserLoginId());
                if (allowedWrongAttempts != -1) {
                    int count = masterDAO.getLoginAttempts(adminLogin.getUserLoginId());

                    if (count > allowedWrongAttempts) {
                        adminLogin.setWrongLoginMaxAttemptLocked("Y");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while validating user", e);
        }
        return false;
    }

    @Override
    public ResponseEntity<ResponseModel> loginAuthenticate(UserLogin userLogin) throws Exception {
        UserLoginResponse userLoginRes = new UserLoginResponse();
        AdminLogin adminLogin = masterDAO.getAdminLogin(userLogin.getUsername().trim());
        if (adminLogin != null) {
            AdminLoginConfig adminLoginConfig = masterDAO.getAdminLoginConfig(adminLogin.getClientId());
            if (adminLoginConfig != null) {
                Client client = cacheComponent.getClientById(adminLogin.getClientId());
                ClientDeploymentConfig cdconfig = cacheComponent.getClientDeploymentConfig(client.getClientCode(), true);
                userLoginRes.setBlockTimeInMins(cdconfig.getBlockTimeInMins());

                if (!"Y".equals(client.getLocked())) {
                    addLoginAttempts(adminLogin.getUserLoginId(), userLogin.getIpAddress());
                    boolean valid = validateUserRestrictIPs(adminLoginConfig.getUserRestrictIps(), userLogin.getIpAddress());
                    if (valid) {
                        if (validateUserPassword(adminLogin, userLogin.getPassword(), adminLoginConfig)) {
                            if (!"Y".equals(adminLogin.getSuspend())) {
                                int expiryDays = adminLoginConfig.getPasswordExpireDays();
                                boolean isPassWordExpired = masterDAO.isPassowrdExpired(adminLogin.getUserLoginId(), expiryDays);
                                if (!isPassWordExpired) {
                                    if (!"Y".equals(adminLogin.getWrongLoginMaxAttemptLocked())) {
                                        userLoginRes.setAuthStatus(true);
                                        userLoginRes.setMessage(LoginConstants.LOGIN_SUCESSES_RESPONSE.getValue());
                                        populateLoginResponse(client, adminLogin, adminLoginConfig, userLoginRes);
                                    } else {
                                        userLoginRes.setMessage(LoginConstants.LOGIN_WRONG_LOGIN_ATTEMPTS_EXCEEDED_RESPONSE.getValue());
                                    }
                                } else {
                                    userLoginRes.setMessage(LoginConstants.LOGIN_PASSWORD_EXPIRED_RESPONSE.getValue());
                                }
                            } else {
                                userLoginRes.setMessage(LoginConstants.LOGIN_INVALID_PASSWORD_RESPONSE.getValue());
                            }
                        } else {
                            userLoginRes.setMessage(LoginConstants.LOGIN_PASSWORD_EXPIRED_RESPONSE.getValue());
                        }
                    } else {
                        userLoginRes.setMessage(LoginConstants.LOGIN_RESTRICT_IPS_RESPONSE.getValue());
                    }
                } else {
                    userLoginRes.setMessage(LoginConstants.LOGIN_USER_LOCKED_RESPONSE.getValue());
                }
            }
        } else {
            userLoginRes.setMessage(LoginConstants.LOGIN_FAILURE_RESPONSE.getValue());
        }

        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(userLoginRes), HttpStatus.OK);
    }

    private void populateLoginResponse(Client client, AdminLogin adminLogin, AdminLoginConfig adminLoginConfig, UserLoginResponse userLoginRes) throws TelAppointException {
        userLoginRes.setAccessLevel(adminLogin.getAccessLevel());
        userLoginRes.setClientCode(client.getClientCode());
        userLoginRes.setFirstName(adminLogin.getFirstName());
        userLoginRes.setLastName(adminLogin.getLastName());
        userLoginRes.setLastLoginDateTime(adminLogin.getPasswordLastUpdateDate());
        userLoginRes.setLoginUserId(adminLogin.getUserLoginId());
        LoginAttempts loginAttempts = masterDAO.getLoginAttemptBean(adminLogin.getUserLoginId());
        userLoginRes.setLastLoginIP(loginAttempts != null ? loginAttempts.getIpAddress() : "");
        userLoginRes.setVersionNumber(client.getLicenceKey());
        userLoginRes.setUserName(adminLogin.getUsername());
    }

    @Override
    public ResponseEntity<ResponseModel> getApptSysConfig(String clientCode) throws Exception {
        ApptSysConfigResponse apptSysConfigRes = new ApptSysConfigResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ApptSysConfig apptSysConfig = adminDAO.getAppSysConfig(jdbcCustomTemplate);
        apptSysConfig.setTermStartDate((apptSysConfig.getTermStartDate() != null && !"".equals(apptSysConfig.getTermStartDate())) ? DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(apptSysConfig.getTermStartDate()) : "");
        apptSysConfig.setTermEndDate(apptSysConfig.getTermEndDate() != null && !"".equals(apptSysConfig.getTermEndDate()) ? DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(apptSysConfig.getTermEndDate()) : "");
        apptSysConfig.setApptStartDate(apptSysConfig.getApptStartDate() != null && !"".equals(apptSysConfig.getApptStartDate()) ? DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(apptSysConfig.getApptStartDate()) : "");
        apptSysConfig.setApptEndDate(apptSysConfig.getApptEndDate() != null && !"".equals(apptSysConfig.getApptEndDate()) ? DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(apptSysConfig.getApptEndDate()) : "");
        apptSysConfigRes.setApptSysConfig(apptSysConfig);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(apptSysConfigRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getDisplayNames(String clientCode) throws Exception {
        DisplayNamesResponse displayNamesRes = new DisplayNamesResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        DisplayNames displayNames = adminDAO.getDisplayNames(jdbcCustomTemplate);
        displayNamesRes.setDisplayNames(displayNames);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(displayNamesRes), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseModel> getPrivilegedPageNames(String clientCode) throws Exception {
        PrivilegedPageNamesResponse previlegePageNames = new PrivilegedPageNamesResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        Map<String, List<String>> previllegedPageNames = adminDAO.getPrivilegedPageNames(jdbcCustomTemplate);
        previlegePageNames.setPrevilegePageNames(previllegedPageNames);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(previlegePageNames), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getServiceLocationApptDatesWindow(String clientCode, Integer locationId) throws Exception {
        ServiceLocationApptDatesResponse serviceLocationApptDatesRes = new ServiceLocationApptDatesResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<ServiceLocation> serviceLocationList = adminDAO.getServiceLocationDates(jdbcCustomTemplate, locationId);
        serviceLocationApptDatesRes.setServiceLocationList(serviceLocationList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(serviceLocationApptDatesRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateApptRestrictDates(String clientCode, String apptStartDate, String apptEndDate) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

        String yyyyMMddStartDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(apptStartDate);
        String yyyyMMddEndDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(apptEndDate);

        boolean updated = adminDAO.updateApptRestrictDates(jdbcCustomTemplate, yyyyMMddStartDate, yyyyMMddEndDate);

        if (!updated) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("updateApptRestrictDates update failed!.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseModel> updateApptPerSeasonDetails(String clientCode, String termStartDate, String termEndDate, Integer noApptPerTerm) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

        String yyyyMMddTermStartDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(termStartDate);
        String yyyyMMddTermEndDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(termEndDate);

        boolean updated = adminDAO.updateApptPerSeasonDetails(jdbcCustomTemplate, yyyyMMddTermStartDate, yyyyMMddTermEndDate, noApptPerTerm);

        if (!updated) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("updateApptPerSeasonDetails update failed!.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateServiceLocationApptDatesWindow(ServiceLocationApptDatesRequest serviceLocApptDatesReq) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(serviceLocApptDatesReq.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        adminDAO.updateServiceLocationApptDatesWindow(jdbcCustomTemplate, serviceLocApptDatesReq.getServiceLocationList());
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateLocationsApptDates(LocationsApptDatesRequest locationApptReq) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(locationApptReq.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        adminDAO.updateLocationsApptDates(jdbcCustomTemplate, locationApptReq.getLocations());
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateScheduleClosedStatus(String clientCode, String closedStatus) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        adminDAO.updateScheduleClosedStatus(jdbcCustomTemplate, closedStatus);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateLocation(Location location) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(location.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean isUpdated = adminDAO.updateLocation(jdbcCustomTemplate, location);
        if (!isUpdated) {
            baseResponse.setMessage("Location update failed.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getResourcePrefixList(String clientCode) throws Exception {
        ResourcePrefixResponse prefixRes = new ResourcePrefixResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<ResourcePrefix> resourcePrefixList = adminDAO.getResourcePrefixList(jdbcCustomTemplate);
        prefixRes.setResourcePrefixList(resourcePrefixList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(prefixRes), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseModel> getResourceTitleList(String clientCode) throws Exception {
        ResourceTitleResponse titlesRes = new ResourceTitleResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<ResourceTitle> resourceTitleList = adminDAO.getResourceTitleList(jdbcCustomTemplate);
        titlesRes.setResourceTitleList(resourceTitleList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(titlesRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getResourceTypeList(String clientCode) throws Exception {
        ResourceTypeResponse typeRes = new ResourceTypeResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<ResourceType> resourceTypeList = adminDAO.getResourceTypeList(jdbcCustomTemplate);
        typeRes.setResourceTypeList(resourceTypeList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(typeRes), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseModel> deleteLocation(String clientCode, Integer locationId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean isDeleted = adminDAO.deleteLocation(jdbcCustomTemplate, locationId);
        if (!isDeleted) {
            baseResponse.setMessage("Delete location failed!");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> unDeleteLocation(String clientCode, Integer locationId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean isUnDeleted = adminDAO.unDeleteLocation(jdbcCustomTemplate, locationId);
        if (!isUnDeleted) {
            baseResponse.setMessage("UnDelete location failed!");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> addLocation(Location location) throws Exception {
        LocationResponse baseResponse = new LocationResponse();
        Client client = cacheComponent.getClient(location.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus tStatus = dsTransactionManager.getTransaction(def);
        try {
            Integer locationId = adminDAO.addLocation(jdbcCustomTemplate, location);
            ApptSysConfig apptSysConfig = adminDAO.getAppSysConfig(jdbcCustomTemplate);
            adminDAO.addLocationWorkingHrs(jdbcCustomTemplate, apptSysConfig, locationId);
            List<Procedure> procedureList = adminDAO.getProcedureList(jdbcCustomTemplate, true);
            adminDAO.addProcedureLocation(jdbcCustomTemplate, procedureList, locationId);
            baseResponse.setLocationId(locationId);
            dsTransactionManager.commit(tStatus);
        } catch (Exception e) {
            dsTransactionManager.rollback(tStatus);
            baseResponse.setErrorFlag("Y");
            baseResponse.setErrorMessage("Failed to add location!! !!");
            throw e;
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> addResource(Resource resource) throws Exception {
        ResourceResponse baseResponse = new ResourceResponse();
        Client client = cacheComponent.getClient(resource.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(resource.getClientCode(), cache);

        DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus tStatus = dsTransactionManager.getTransaction(def);
        try {
            Integer resourceId = adminDAO.addResource(jdbcCustomTemplate, resource);
            resource.setResourceId(resourceId);
            adminDAO.addOrUpdateResourceService(jdbcCustomTemplate, resource, false);
            adminDAO.addOrUpdateLocationDepartionResource(jdbcCustomTemplate, resource, false);
            addResourceCalendarData(jdbcCustomTemplate, resourceId, cdConfig);
            baseResponse.setResourceId(resourceId);
            dsTransactionManager.commit(tStatus);
        } catch (Exception e) {
            dsTransactionManager.rollback(tStatus);
            baseResponse.setErrorFlag("Y");
            baseResponse.setErrorMessage("Failed to add resource!!");
            throw e;
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateResource(Resource resource) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(resource.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus tStatus = dsTransactionManager.getTransaction(def);
        try {
            adminDAO.updateResource(jdbcCustomTemplate, resource);
            adminDAO.addOrUpdateResourceService(jdbcCustomTemplate, resource, true);
            adminDAO.addOrUpdateLocationDepartionResource(jdbcCustomTemplate, resource, true);
            dsTransactionManager.commit(tStatus);
        } catch (Exception e) {
            dsTransactionManager.rollback(tStatus);
            baseResponse.setErrorFlag("Y");
            baseResponse.setErrorMessage("Failed to update Resource.");
            throw e;
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> deleteResource(String clientCode, Integer resourceId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean isDeleted = adminDAO.deleteResource(jdbcCustomTemplate, resourceId);
        if (!isDeleted) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Delete resource failed!");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> unDeleteResource(String clientCode, Integer resourceId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean isUnDeleted = adminDAO.unDeleteResource(jdbcCustomTemplate, resourceId);
        if (!isUnDeleted) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("UnDelete resource failed!");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> deleteService(String clientCode, Integer serviceId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean isDeleted = adminDAO.deleteService(jdbcCustomTemplate, serviceId);
        if (!isDeleted) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Delete service failed!");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> unDeleteService(String clientCode, Integer serviceId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean isDeleted = adminDAO.unDeleteService(jdbcCustomTemplate, serviceId);
        if (!isDeleted) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Un Delete service failed!");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> addService(ServiceVO service) throws Exception {
        ServiceResponse baseResponse = new ServiceResponse();
        Client client = cacheComponent.getClient(service.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus tStatus = dsTransactionManager.getTransaction(def);
        try {
            Integer serviceId = adminDAO.addService(jdbcCustomTemplate, service, cdConfig.getBlockTimeInMins());
            service.setServiceId(serviceId);
            adminDAO.addResourceService(jdbcCustomTemplate, service);
            baseResponse.setServiceId(serviceId);
            dsTransactionManager.commit(tStatus);
        } catch (Exception e) {
            dsTransactionManager.rollback(tStatus);
            baseResponse.setErrorFlag("Y");
            baseResponse.setErrorMessage("Failed to add Service.");
            throw e;
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateService(ServiceVO service) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(service.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        boolean isAdded = adminDAO.updateService(jdbcCustomTemplate, service, cdConfig.getBlockTimeInMins());
        if (!isAdded) {
            baseResponse.setMessage("Service add failed.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> addUser(AdminLogin adminLogin) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(adminLogin.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        adminLogin.setPassword(AdminInstance.getInstance().encrypt(adminLogin.getPassword()));
        adminLogin.setClientId(client.getClientId());

        if (masterDAO.userExist(adminLogin.getClientId(), adminLogin.getUsername())) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("UserName already exists");
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
        }
        boolean isAdded = masterDAO.addUser(jdbcCustomTemplate, adminLogin);
        if (!isAdded) {
            baseResponse.setMessage("User add failed.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> validateUser(String clientCode, Integer userId, String userName) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        if (masterDAO.userExist(client.getClientId(), userId, userName)) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("UserName already exists");
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateUser(AdminLogin adminLogin) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(adminLogin.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

        if ("Y".equals(adminLogin.getPasswordUpdate())) {
            adminLogin.setPassword(AdminInstance.getInstance().encrypt(adminLogin.getPassword()));
        }
        adminLogin.setClientId(client.getClientId());
        if (masterDAO.userExist(adminLogin.getClientId(), adminLogin.getUserLoginId(), adminLogin.getUsername())) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("UserName already exists");
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
        }
        boolean isAdded = masterDAO.updateUser(jdbcCustomTemplate, adminLogin);
        if (!isAdded) {
            baseResponse.setMessage("User Update failed.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getUsers(String clientCode) throws Exception {
        UserResponse baseResponse = new UserResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        baseResponse.setUserList(masterDAO.getUserList(client.getClientId()));
        baseResponse.setSuspendedUserList(masterDAO.getSuspendUserList(client.getClientId()));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getInBoundCallLogs(String clientCode, String fromDate, String toDate, String callerId) throws Exception {
        InBoundCallsResponse inBoundCallsResponse = new InBoundCallsResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        String startDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(fromDate);
        String endDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(toDate);
        List<IvrCalls> ivrCallList = adminDAO.getInBoundCallLogs(jdbcCustomTemplate, startDate, endDate, callerId);
        inBoundCallsResponse.setIvrCallLogs(ivrCallList);
        DecimalFormat df = new DecimalFormat("#.##");
        inBoundCallsResponse.setTotalMinutes(df.format(ivrCallList.stream().mapToLong(o -> o.getSeconds()).sum() / 60));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(inBoundCallsResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getOutBoundCallLogs(String clientCode, String fromDate, String toDate, String callerId) throws Exception {
        OutBoundCallsResponse outBoundCallsResponse = new OutBoundCallsResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        String startDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(fromDate);
        String endDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(toDate);
        List<OutBoundCalls> outBoundCalls = adminDAO.getOutBoundCallLogs(jdbcCustomTemplate, startDate, endDate, callerId);
        outBoundCallsResponse.setOutBoundCallLogs(outBoundCalls);
        DecimalFormat df = new DecimalFormat("#.##");
        outBoundCallsResponse.setTotalMinutes(df.format(outBoundCalls.stream().mapToLong(o -> o.getSeconds()).sum() / 60));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(outBoundCallsResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getLocationsByServiceIdToCloseServiceStatus(String clientCode, Integer serviceId) throws Exception {
        LocationResponse locationResponse = new LocationResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Location> locationList = adminDAO.getLocationsByServiceIdToCloseServiceStatus(jdbcCustomTemplate, serviceId);
        locationResponse.setLocationList(locationList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(locationResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> addAppointmentReportConfig(AppointmentReportConfig apptReportConfig) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(apptReportConfig.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        apptReportConfig.setClientId(client.getClientId());
        boolean isAdded = masterDAO.addAppointmentReportConfig(jdbcCustomTemplate, apptReportConfig);
        if (!isAdded) {
            baseResponse.setMessage("ApptReportConfig add failed.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getAppointmentReportConfig(String userName) throws Exception {
        AppointmentReportConfigResponse apptRes = new AppointmentReportConfigResponse();
        List<AppointmentReportConfig> apptReportConfigList = masterDAO.getAppointmentReportConfig(userName);
        apptRes.setApptReportConfigList(apptReportConfigList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(apptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> deleteApptReportConfigById(Integer configId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        boolean isDeleted = masterDAO.deleteApptReportConfigById(configId);
        if (!isDeleted) {
            baseResponse.setMessage("ApptReportConfig delete failed.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getDynamicIncludeReportsData(String clientCode) throws Exception {
        DynamicIncludeReportResponse dynamicIncludeReportRes = new DynamicIncludeReportResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<DynamicIncludeReport> dynamicIncludeReportList = adminDAO.getDynamicIncludeReportsData(jdbcCustomTemplate);
        dynamicIncludeReportRes.setDynamicIncludeReportList(dynamicIncludeReportList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(dynamicIncludeReportRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getAppointmentReport(String clientCode, String fromDate, String toDate, String locationIds, String resourceIds, String serviceIds,
                                                              String apptStatus) throws Exception {
        AppointmentReportResponse apptReportRes = new AppointmentReportResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        String startDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(fromDate);
        String endDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(toDate);
        List<AppointmentReportData> appointmentReportDataList = adminDAO.getAppointmentReport(jdbcCustomTemplate, startDate, endDate, locationIds, resourceIds, serviceIds, apptStatus);
        apptReportRes.setAppointmentReportDataList(appointmentReportDataList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(apptReportRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> searchByFirstLastName(String clientCode, String firstName, String lastName) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<SearchAppointmentData> searchApptList = adminDAO.searchByFirstLastName(jdbcCustomTemplate, firstName, lastName);
        searchApptRes.setSearchApptList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> searchByConfirmationNumber(String clientCode, Long confirmationNumber) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<SearchAppointmentData> searchApptList = adminDAO.searchByConfirmationNumber(jdbcCustomTemplate, confirmationNumber);
        searchApptRes.setSearchApptList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getUserById(String clientCode, Integer userId) throws Exception {
        UserResponse userResponse = new UserResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        AdminLogin adminLogin = masterDAO.getAdminLoginByUserId(client.getClientId(), userId);
        userResponse.setAdminLogin(adminLogin);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(userResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getPasswordComplexity(String clientCode) throws Exception {
        PasswordComplexity complexity = new PasswordComplexity();
        Client client = cacheComponent.getClient(clientCode, true);
        String passwordComplexity = masterDAO.getPasswordComplexity(client.getClientId());
        complexity.setComplexityValue(passwordComplexity);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(complexity), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> searchByAccountNumber(String clientCode, String accountNumber) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<SearchAppointmentData> searchApptList = adminDAO.searchByAccountNumber(jdbcCustomTemplate, accountNumber);
        searchApptRes.setSearchApptList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> searchByContactPhone(String clientCode, String contactPhone) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<SearchAppointmentData> searchApptList = adminDAO.searchByContactPhone(jdbcCustomTemplate, contactPhone);
        searchApptRes.setSearchApptList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> searchByCallerId(String clientCode, String callerId) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<SearchAppointmentData> searchApptList = adminDAO.searchByCallerId(jdbcCustomTemplate, callerId);
        searchApptRes.setSearchApptList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> searchByAttrib1(String clientCode, String attrib1) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<SearchAppointmentData> searchApptList = adminDAO.searchByAttrib1(jdbcCustomTemplate, attrib1);
        searchApptRes.setSearchApptList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> searchByDob(String clientCode, String dob) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        String dbDOB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(dob);
        List<SearchAppointmentData> searchApptList = adminDAO.searchByDOB(jdbcCustomTemplate, dbDOB);
        searchApptRes.setSearchApptList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> searchByHouseHoldId(String clientCode, Long houseHoldId) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<SearchAppointmentData> searchApptList = adminDAO.searchByHouseHoldId(jdbcCustomTemplate, houseHoldId);
        searchApptRes.setSearchApptList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getSearchDropDownList(String clientCode) throws Exception {
        DynamicSearchDropDownResponse dynamicSearchRes = new DynamicSearchDropDownResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<DynamicSearchByFields> dynamicSearchByFields = adminDAO.getSearchDropDownList(jdbcCustomTemplate);
        dynamicSearchRes.setDynamicSearchByFields(dynamicSearchByFields);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(dynamicSearchRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getAppointmentsByCustomerId(String clientCode, Long customerId) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        List<SearchAppointmentData> searchApptList = adminDAO.getAppointmentsByCustomerId(jdbcCustomTemplate, cdConfig.getTimeZone(), customerId);
        searchApptRes.setSearchApptList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getCustomersById(String clientCode, Long customerId) throws Exception {
        CustomersResponse customersRes = new CustomersResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Customer> customerList = adminDAO.getCustomersById(jdbcCustomTemplate, customerId);
        customersRes.setCustomerList(customerList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(customersRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getBlockedCustomers(String clientCode) throws Exception {
        CustomersResponse customersRes = new CustomersResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Customer> customerList = adminDAO.getBlockedCustomers(jdbcCustomTemplate);
        customersRes.setCustomerList(customerList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(customersRes), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseModel> getCustomerActivities(String clientCode, Long customerId) throws Exception {
        CustomerActivityResponse customersRes = new CustomerActivityResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<CustomerActivity> customerActivityList = adminDAO.getCustomerActivities(jdbcCustomTemplate, customerId);
        customersRes.setCustomerActivityList(customerActivityList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(customersRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getHouseHoldInfo(String clientCode, Long houseHoldId) throws Exception {
        CustomersResponse customersRes = new CustomersResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<Customer> customerList = adminDAO.getHouseHoldInfo(jdbcCustomTemplate, houseHoldId);
        customersRes.setCustomerList(customerList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(customersRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getAppointmentStatusDropDownList(String clientCode) throws Exception {
        AppointmentStatusDropDownResponse res = new AppointmentStatusDropDownResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<AppointmentStatusData> appointmentStatusData = adminDAO.getAppointmentStatusDropDownList(jdbcCustomTemplate);
        res.setAppointmentStatusList(appointmentStatusData);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(res), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> mergeHouseHoldId(String clientCode, String fromHouseHoldIds, String mergeToHouseHoldId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        if (fromHouseHoldIds != null && fromHouseHoldIds.length() > 0 && mergeToHouseHoldId != null && mergeToHouseHoldId.length() > 0) {
            boolean mergeStatus = adminDAO.mergeHouseHoldId(jdbcCustomTemplate, fromHouseHoldIds, mergeToHouseHoldId);
            baseResponse.setStatus(mergeStatus);
            if (!mergeStatus) {
                baseResponse.setMessage("Merge householdId failed.");
            }
        } else {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Invalid request - fromHouseHoldIds: " + fromHouseHoldIds + " , mergeToHouseHoldId: " + mergeToHouseHoldId);
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseModel> splitHouseHoldId(String clientCode, String customerIds, String newHouseHoldId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        if (customerIds != null && customerIds.length() > 0 && newHouseHoldId != null && newHouseHoldId.length() > 0) {
            adminDAO.splitHouseHoldId(jdbcCustomTemplate, customerIds, newHouseHoldId);
            baseResponse.setStatus(true);
        } else {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Invalid request - fromHouseHoldIds: " + customerIds + " , mergeToHouseHoldId: " + newHouseHoldId);
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> splitHouseHoldIdWithAssignNewHouseHoldId(String clientCode, String customerIds, String newHouseHoldId, String assignNewHouseholdID) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        if (customerIds != null && customerIds.length() > 0) {
            if ("yes".equalsIgnoreCase(assignNewHouseholdID)) {
                newHouseHoldId = Long.toString(adminDAO.getNextHouseHoldId(jdbcCustomTemplate).longValue());
                adminDAO.splitHouseHoldId(jdbcCustomTemplate, customerIds, newHouseHoldId);
            } else if (newHouseHoldId != null && newHouseHoldId.length() > 0) {
                adminDAO.splitHouseHoldId(jdbcCustomTemplate, customerIds, newHouseHoldId);
            }
            baseResponse.setStatus(true);
        } else {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Invalid request - fromHouseHoldIds: " + customerIds + " , mergeToHouseHoldId: " + newHouseHoldId);
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateCustomerBlockedReason(String clientCode, Long customerId, String reasonMessage) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        adminDAO.updateCustomerBlockedReason(jdbcCustomTemplate, customerId, reasonMessage);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> unBlockCustomer(String clientCode, Long customerId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        adminDAO.unBlockCustomer(jdbcCustomTemplate, customerId);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Deprecated
    @Override
    public ResponseEntity<ResponseModel> searchAppointmentsByFirstLastName(String clientCode, String firstName, String lastName) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        Map<Customer, List<AppointmentData>> searchApptList = adminDAO.searchAppointmentsByFirstLastName(jdbcCustomTemplate, firstName, lastName);
        searchApptRes.setSearchAppointmentList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> searchAppointmentsByAccountNumber(String clientCode, String accountNumber) throws Exception {
        SearchAppointmentResponse searchApptRes = new SearchAppointmentResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        Map<Customer, List<AppointmentData>> searchApptList = adminDAO.searchAppointmentsByAccountNumber(jdbcCustomTemplate, accountNumber);
        searchApptRes.setSearchAppointmentList(searchApptList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(searchApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getTransStates(String clientCode, Long transId) throws Exception {
        TransStateResponse transResponse = new TransStateResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<TransState> transStateList = adminDAO.getTransStateList(jdbcCustomTemplate, transId);
        transResponse.setTransStateList(transStateList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(transResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getPrivilegeSettings(String clientCode, String accessPrivilegeName) throws Exception {
        PrivilegeSettingResponse privilegeSettingRes = new PrivilegeSettingResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        Map<String, List<JSPPagesPrivileges>> transStateList = adminDAO.getPrivilegeSettings(jdbcCustomTemplate, accessPrivilegeName);
        privilegeSettingRes.setPrivilegeSetting(transStateList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(privilegeSettingRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getTablePrintView(String clientCode, Integer locationId, String resourceIds, String date) throws Exception {
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        String dateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(date);
        boolean isHoliday = adminDAO.isHoliday(jdbcCustomTemplate, dateDB);
        boolean isClosedDay = adminDAO.isClosedDays(jdbcCustomTemplate, locationId, dateDB);
        TablePrintViewResponse tablePrintViewResponse = null;
        if (isHoliday || isClosedDay) {
            tablePrintViewResponse = new TablePrintViewResponse();
        } else {
            tablePrintViewResponse = adminDAO.getTablePrintViewData(jdbcCustomTemplate, locationId, resourceIds, dateDB);
        }
        tablePrintViewResponse.setClientName(client.getClientName());
        tablePrintViewResponse.setHoliday(isHoliday ? "Y" : "N");
        tablePrintViewResponse.setClosedDay(isClosedDay ? "Y" : "N");

        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(tablePrintViewResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getSummaryReport(String clientCode, String fromDate, String toDate, String reportCategory) throws Exception {
        SummaryReportResponse summaryReportResponse = new SummaryReportResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        String fromDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(fromDate);
        String toDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(toDate);

        StatisticsReportResult statisticsResportResult = adminDAO.getStatisticsData(jdbcCustomTemplate, fromDateDB, toDateDB, "location");
        summaryReportResponse.setLocationStatReports(statisticsResportResult);

        if ("All".equalsIgnoreCase(reportCategory)) {
            statisticsResportResult = adminDAO.getStatisticsData(jdbcCustomTemplate, fromDateDB, toDateDB, "procedure");
            summaryReportResponse.setProcedureStatReports(statisticsResportResult);

            statisticsResportResult = adminDAO.getStatisticsData(jdbcCustomTemplate, fromDateDB, toDateDB, "resource");
            summaryReportResponse.setResourceStatReports(statisticsResportResult);

            statisticsResportResult = adminDAO.getStatisticsData(jdbcCustomTemplate, fromDateDB, toDateDB, "service");
            summaryReportResponse.setServiceStatReports(statisticsResportResult);

            statisticsResportResult = adminDAO.getLocationServiceStatisticsData(jdbcCustomTemplate, fromDateDB, toDateDB);
            summaryReportResponse.setLocationServiceStatReports(statisticsResportResult);
        }

        List<AppointmentStatusData> apptStatusDataList = adminDAO.getAppointmentStatusDropDownList(jdbcCustomTemplate);
        List<Integer> statusList = apptStatusDataList.stream().map(AppointmentStatusData::getStatusVal).collect(Collectors.toList());

        if (statusList == null) {
            statusList = new ArrayList<>();
            statusList.add(11);
        }
        adminDAO.getSummaryStatisticReportData(jdbcCustomTemplate, -1, -1, fromDateDB, toDateDB, reportCategory, statusList, summaryReportResponse);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(summaryReportResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getItemizedReport(String clientCode, Integer locationId, Integer serviceId, String fromDate, String toDate, String reportCategory) throws Exception {
        SummaryReportResponse summaryReportResponse = new SummaryReportResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        String fromDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(fromDate);
        String toDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(toDate);
        List<AppointmentStatusData> apptStatusDataList = adminDAO.getAppointmentStatusReport(jdbcCustomTemplate);
        List<Integer> statusList = apptStatusDataList.stream().map(AppointmentStatusData::getStatusVal).collect(Collectors.toList());
        if (statusList == null) {
            statusList = new ArrayList<>();
            statusList.add(11);
        }

        adminDAO.getSummaryStatisticReportData(jdbcCustomTemplate, locationId, serviceId, fromDateDB, toDateDB, reportCategory, statusList, summaryReportResponse);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(summaryReportResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getItemizedReportTemplate(String clientCode, Integer locationId, Integer serviceId, String fromDate, String toDate, String reportCategory)
            throws Exception {
        SummaryReportResponse summaryReportResponse = new SummaryReportResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        String fromDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(fromDate);
        String toDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(toDate);
        List<AppointmentStatusData> apptStatusDataList = adminDAO.getAppointmentStatusReport(jdbcCustomTemplate);
        List<Integer> statusList = apptStatusDataList.stream().map(AppointmentStatusData::getStatusVal).collect(Collectors.toList());
        if (statusList == null) {
            statusList = new ArrayList<>();
            statusList.add(11);
        }

        ReportTemplate itReportTemplate = new ReportTemplate();
        adminDAO.getSummaryStatisticReportData(jdbcCustomTemplate, locationId, serviceId, fromDateDB, toDateDB, reportCategory, statusList, summaryReportResponse);
        String template = getTemplate(jdbcCustomTemplate, summaryReportResponse, reportCategory);

        itReportTemplate.setReportTemplate(template);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(itReportTemplate), HttpStatus.OK);
    }

    private String getTemplate(JdbcCustomTemplate jdbcCustomTemplate, SummaryReportResponse summaryReportResponse, String reportCategory) throws Exception {
    	List<ItemizedReportGoal> goals = adminDAO.getItemizedReportGoal(jdbcCustomTemplate);
    	
        Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, "admin", "us-en", true);
        if (pageContent != null) {
            String template = pageContent.get("REPORT_TEMPLATE");
            String headerTemplate = PropertyUtils.getValueFromProperties("ITEMIZED_REPORT_ROWS_HEADER", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
            String bodyFirstPart = PropertyUtils.getValueFromProperties("ITEMIZED_REPORT_FIRST_PART", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());

            String tempHeaderTemplate = headerTemplate;
            String tempBodyFirstPart = bodyFirstPart;
            if ("D".equalsIgnoreCase(reportCategory)) {
                String dateTemplate = tempHeaderTemplate;
                tempHeaderTemplate = tempHeaderTemplate.replaceAll("%REPORT_NAME%", "Month");
                dateTemplate = dateTemplate.replaceAll("%REPORT_NAME%", "Date");
                template = template.replaceAll("%ITEMIZED_REPORT_ROWS_HEADER%", tempHeaderTemplate + dateTemplate);
            } else if ("W".equalsIgnoreCase(reportCategory)) {
                String weekTemplate = tempHeaderTemplate;
                tempHeaderTemplate = tempHeaderTemplate.replaceAll("%REPORT_NAME%", "Month");
                weekTemplate = weekTemplate.replaceAll("%REPORT_NAME%", "Week");
                template = template.replaceAll("%ITEMIZED_REPORT_ROWS_HEADER%", tempHeaderTemplate + weekTemplate);
            } else if ("M".equalsIgnoreCase(reportCategory)) {
                tempHeaderTemplate = tempHeaderTemplate.replaceAll("%REPORT_NAME%", "Month");
                template = template.replaceAll("%ITEMIZED_REPORT_ROWS_HEADER%", tempHeaderTemplate);
            } else if ("Q".equalsIgnoreCase(reportCategory)) {
                tempHeaderTemplate = tempHeaderTemplate.replaceAll("%REPORT_NAME%", "Quarterly");
                template = template.replaceAll("%ITEMIZED_REPORT_ROWS_HEADER%", tempHeaderTemplate);
            } else if ("Y".equalsIgnoreCase(reportCategory)) {
                tempHeaderTemplate = tempHeaderTemplate.replaceAll("%REPORT_NAME%", "Year");
                template = template.replaceAll("%ITEMIZED_REPORT_ROWS_HEADER%", tempHeaderTemplate);
            } else {
                tempHeaderTemplate = tempHeaderTemplate.replaceAll("%REPORT_NAME%", "Month");
                template = template.replaceAll("%ITEMIZED_REPORT_ROWS_HEADER%", tempHeaderTemplate);
            }
            StringBuilder data = new StringBuilder();
            String reportFirstData = null;
            Map<String, Integer> totalCountMap = new HashMap<>();
            List<AppointmentStatusData> apptStatusDataList = adminDAO.getAppointmentStatusReportList(jdbcCustomTemplate);
            for (SummaryStatisticsResult summaryReportResult : summaryReportResponse.getSummaryStatisticsResults()) {

                if ("D".equalsIgnoreCase(reportCategory)) {
                    String tempBodyFirstPart1 = tempBodyFirstPart.replaceAll("%REPORT_FIRST_DATA%", summaryReportResult.getMonthName());
                    String tempBodyFirstPart2 = tempBodyFirstPart.replaceAll("%REPORT_FIRST_DATA%", summaryReportResult.getDay());
                    reportFirstData = tempBodyFirstPart1 + tempBodyFirstPart2;
                } else if ("W".equalsIgnoreCase(reportCategory)) {
                    String tempBodyFirstPart1 = tempBodyFirstPart.replaceAll("%REPORT_FIRST_DATA%", summaryReportResult.getMonthName());
                    String tempBodyFirstPart2 = tempBodyFirstPart.replaceAll("%REPORT_FIRST_DATA%", summaryReportResult.getWeek());
                    reportFirstData = tempBodyFirstPart1 + tempBodyFirstPart2;
                } else if ("M".equalsIgnoreCase(reportCategory)) {
                    reportFirstData = tempBodyFirstPart.replaceAll("%REPORT_FIRST_DATA%", summaryReportResult.getMonthName());
                } else if ("Q".equalsIgnoreCase(reportCategory)) {
                    reportFirstData = tempBodyFirstPart.replaceAll("%REPORT_FIRST_DATA%", summaryReportResult.getQuarter());
                } else if ("Y".equalsIgnoreCase(reportCategory)) {
                    reportFirstData = tempBodyFirstPart.replaceAll("%REPORT_FIRST_DATA%", summaryReportResult.getYear());
                } else {
                    reportFirstData = tempBodyFirstPart.replaceAll("%REPORT_FIRST_DATA%", summaryReportResult.getMonthName());
                }
                prepareTemplateBodyData(summaryReportResult, reportFirstData, data, totalCountMap, jdbcCustomTemplate, apptStatusDataList);
            }

            template = template.replaceAll("%ITEMIZED_REPORT_ROWS_BODY%", "".equals(data.toString()) ? "" : data.toString());
            data = new StringBuilder();
            prepareTemplateTotalData(data, totalCountMap, apptStatusDataList);
            template = template.replaceAll("%ITEMIZED_REPORT_TOTAL_ROWS_BODY%", "".equals(data.toString()) ? "" : data.toString());
           
            
            int goalSize = goals.size();
            
            String goal1 = goalSize > 0? goals.get(0).getGoalPercentage():"0";
            String goal2 = goalSize > 1?goals.get(1).getGoalPercentage():"0";
            String goal3 = goalSize > 2?goals.get(2).getGoalPercentage():"0";
            String goal4 = goalSize > 3? goals.get(3).getGoalPercentage():"0";
            String goal5 = goalSize > 4? goals.get(4).getGoalPercentage():"0";
            
            Integer pencentageRow1 = totalCountMap.get("%TOTAL_SERVED%")/totalCountMap.get("%TOTAL_NO_OF_BOOKED%");
    		String templateRow1 = "<tr><td width=\"10%\">Of the</td><td width=\"8%\">"+totalCountMap.get("%TOTAL_NO_OF_BOOKED%")+"</td><td>Scheduled</td><td width=\"8%\" align=\"right\">"+totalCountMap.get("%TOTAL_SERVED%")+"</td><td width=\"8%\" align=\"right\">"+pencentageRow1+" %</td><td width=\"32%\">Were served</td><td align=\"right\">"+goal1+"%</td></tr>";
    		Integer topThreePrecen = (totalCountMap.get("%TOTAL_IN%") + totalCountMap.get("%TOTAL_SS%") + totalCountMap.get("%TOTAL_ID_DL%"));
    		Integer pencentageRow2 = topThreePrecen/totalCountMap.get("%TOTAL_SERVED%");
    		
    		String templateRow2 = "<tr><td>Of</td><td>"+totalCountMap.get("%TOTAL_SERVED%")+"</td><td>Served</td><td align=\"right\">"+topThreePrecen+"</td><td align=\"right\">"+pencentageRow2+" %</td><td>denied for top 3 pre screening docs</td><td align=\"right\">"+goal2+"%</td></tr>";
    		Integer lastTwo = totalCountMap.get("%TOTAL_OI%") + totalCountMap.get("%TOTAL_PA%");
    		Integer pencentageRow3 = lastTwo/totalCountMap.get("%TOTAL_SERVED%");
    		String templateRow3="<tr><td>Of</td><td>"+totalCountMap.get("%TOTAL_SERVED%")+"</td><td>served</td><td align=\"right\">"+lastTwo+"</td><td align=\"right\">"+pencentageRow3+" %  </td><td>denied for info that couldn't be prescreened</td><td align=\"right\">"+goal3+"%</td></tr>";
    		
    		Integer pencentageRow4 = totalCountMap.get("%TOTAL_NOSHOW%")/totalCountMap.get("%TOTAL_NO_OF_BOOKED%");
    		String templateRow4="<tr><td>Of the</td><td>"+totalCountMap.get("%TOTAL_NO_OF_BOOKED%")+"</td><td>scheduled</td><td align=\"right\">"+totalCountMap.get("%TOTAL_NOSHOW%")+"</td><td align=\"right\">"+pencentageRow4+" %</td><td>did not show up for their appointments</td><td align=\"right\">"+goal4+"%</td></tr>";
    		
    		Integer last = totalCountMap.get("%TOTAL_SERVED%") - totalCountMap.get("%TOTAL_IN%") - totalCountMap.get("%TOTAL_SS%") - totalCountMap.get("%TOTAL_ID_DL%") - totalCountMap.get("%TOTAL_OI%") - totalCountMap.get("%TOTAL_PA%");
    		Integer pencentageRow5 = last/totalCountMap.get("%TOTAL_SERVED%");
    		String templateRow5="<tr><td>Of</td><td>"+totalCountMap.get("%TOTAL_SERVED%")+"</td><td>served</td><td align=\"right\">"+last+"</td><td align=\"right\">"+pencentageRow5+" %</td><td>were served at the first visit</td><td align=\"right\">"+goal5+"%</td></tr>";

    		
    		template = template.replaceAll("%PERCENTAGE_TEMPLATE_ROW1%",templateRow1);
    		template = template.replaceAll("%PERCENTAGE_TEMPLATE_ROW2%",templateRow2);
    		template = template.replaceAll("%PERCENTAGE_TEMPLATE_ROW3%",templateRow3);
    		template = template.replaceAll("%PERCENTAGE_TEMPLATE_ROW4%",templateRow4);
    		template = template.replaceAll("%PERCENTAGE_TEMPLATE_ROW5%",templateRow5);

            return template;
        }
        return "";
    }

    private void prepareTemplateTotalData(StringBuilder data, Map<String, Integer> totalCountMap, List<AppointmentStatusData> apptStatusDataList) throws Exception {
        String bodyTemplate = PropertyUtils.getValueFromProperties("ITEMIZED_REPORT_TOTAL_ROWS_BODY", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
        String tempBodyTemplate = bodyTemplate;
        for (AppointmentStatusData apptStatusData : apptStatusDataList) {
            tempBodyTemplate = tempBodyTemplate.replaceAll("%TOTAL_" + apptStatusData.getPlaceHolderName() + "%", "" + totalCountMap.get("%TOTAL_" + apptStatusData.getPlaceHolderName() + "%"));
        }
        data.append(tempBodyTemplate);
    }

    private void prepareTemplateBodyData(SummaryStatisticsResult summaryReportResult, String reportFirstData, StringBuilder bodyData, Map<String, Integer> countMap, JdbcCustomTemplate jdbcCustomTemplate,
                                         List<AppointmentStatusData> apptStatusDataList) throws Exception {
        String bodyTemplate = PropertyUtils.getValueFromProperties("ITEMIZED_REPORT_ROWS_BODY", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
        Map<Integer, Integer> apptStatusWIthApptCount = summaryReportResult.getApptStatusWithApptCount();
        String tempBodyTemplate = bodyTemplate;
        tempBodyTemplate = tempBodyTemplate.replaceAll("%ITEMIZED_REPORT_FIRST_PART%", reportFirstData);

        for (AppointmentStatusData apptStatusData : apptStatusDataList) {
            String statusVal = apptStatusData.getStatusValStr();
            String statusValues[] = statusVal.split(",");
            Integer value = 0;
            for (String statusV : statusValues) {
                String valueStr = apptStatusWIthApptCount.get(Integer.valueOf(statusV)) == null ? "0" : "" + apptStatusWIthApptCount.get(Integer.valueOf(statusV));
                value = value + Integer.valueOf(valueStr);
            }
            tempBodyTemplate = tempBodyTemplate.replaceAll("%" + apptStatusData.getPlaceHolderName() + "%", "" + value);

            countMap.put("%TOTAL_" + apptStatusData.getPlaceHolderName() + "%", countMap.get("%TOTAL_" + apptStatusData.getPlaceHolderName() + "%") == null ? value : countMap.get("%TOTAL_" + apptStatusData.getPlaceHolderName() + "%") + value);

        }
        bodyData.append(tempBodyTemplate);
    }

    public ResponseEntity<ResponseModel> cancelAppointment(String clientCode, String langCode, Long scheduleId) throws Exception {
        CancelAppointResponse cancelAppointResponse = new CancelAppointResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        Integer cancelMethod = AppointmentMethod.ADMIN.getMethod();
        ;
        adminDAO.cancelAppointment(jdbcCustomTemplate, scheduleId, cancelMethod, langCode, cdConfig, cancelAppointResponse);
        String errorMessage = cancelAppointResponse.getMessage();
        EmailRequest emailRequest = new EmailRequest();
        sendCancelEmail(logger, errorMessage, jdbcCustomTemplate, client, cdConfig, cancelAppointResponse, scheduleId, langCode, emailRequest);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(cancelAppointResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateAppointmentStatus(String clientCode, Long scheduleId, int status, String userName) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean updateStatus = adminDAO.updateAppointmentStatus(jdbcCustomTemplate, scheduleId, status, userName);
        if (!updateStatus) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Update Appointment Status failed!");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getPledgeReport(String clientCode, String fromDate, String toDate, Integer locationId, String groupByIntake, String groupByFundSource,
                                                         String groupByVendor, Integer resourceId, Integer fundSourceId) throws Exception {
        PledgeReportResponse pledgeReportRes = new PledgeReportResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        String fromDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(fromDate);
        String toDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(toDate);
        List<PledgeDetails> pledgeReportList = adminDAO.getPledgeReport(jdbcCustomTemplate, fromDateDB, toDateDB, locationId, groupByIntake, groupByFundSource, groupByVendor, resourceId, fundSourceId);
        String groupBy = "All";
        if ("Y".equalsIgnoreCase(groupByIntake)) {
            groupBy = "Intake";
        } else if ("Y".equalsIgnoreCase(groupByFundSource)) {
            groupBy = "FundSource";
        } else if ("Y".equalsIgnoreCase(groupByVendor)) {
            groupBy = "Vendor";
        }
        pledgeReportRes.setPledgeReportData(PledgeReportUtil.separateByPledgeReport(pledgeReportList, groupBy));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(pledgeReportRes), HttpStatus.OK);
    }

    private void sendCancelEmail(Logger logger, String errorMessage, JdbcCustomTemplate jdbcCustomTemplate, Client client, ClientDeploymentConfig cdConfig,
                                 CancelAppointResponse cancelAppointResponse, long scheduleId, String langCode, EmailRequest emailRequest) {
        try {
            if (errorMessage == null || "".equals(errorMessage)) {
                Map<String, String> emailData = new HashMap<String, String>();

                ApptSysConfig apptSysConfig = adminDAO.getAppSysConfig(jdbcCustomTemplate);
                String ccConfirmEmail[] = null;
                if (apptSysConfig != null && apptSysConfig.getCcConfirmEmails() != null && apptSysConfig.getCcConfirmEmails().trim().length() > 0) {
                    ccConfirmEmail = apptSysConfig.getCcConfirmEmails().split(",");
                }
                if (ccConfirmEmail != null) {
                    emailRequest.setCcAddresses(ccConfirmEmail);
                }
                emailData.put("%CLIENTAPPTLINK%", client.getApptLink() == null ? "" : client.getApptLink());
                emailData.put("%CLIENTNAME%", client.getClientName() == null ? "" : client.getClientName());
                emailData.put("%CLIENTADDRESS%", client.getAddress() == null ? "" : client.getAddress());
                populateDataForEmail(logger, emailData, cdConfig, langCode, cancelAppointResponse.getDisplayKeys(), cancelAppointResponse.getDisplayValues());
                emailData.put("%SCHEDULEID%", "" + scheduleId);
                String email = emailData.get("%CMAIL%");
                if (email != null && !"".equals(email)) {
                    Map<String, String> emailTemplateMap = cacheComponent.getEmailTemplateMap(jdbcCustomTemplate, logger, langCode, false);
                    if (emailTemplateMap != null) {
                        String emailSubjectTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_APPT_CANCEL_SUBJECT.getValue());
                        String emailBodyTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_APPT_CANCEL_BODY.getValue());
                        if ((emailSubjectTemplate != null && !"".equals(emailSubjectTemplate)) || (emailBodyTemplate != null && !"".equals(emailBodyTemplate))) {
                            String emailSubject = emailComponent.getEmailSubject(emailSubjectTemplate, emailData);
                            String emailBody = emailComponent.getEmailBody(emailBodyTemplate, emailData);
                            emailRequest.setSubject(emailSubject);
                            emailRequest.setEmailBody(emailBody);
                            emailRequest.setToAddress(email);
                            emailRequest.setMethod("REQUEST");
                            emailRequest.setStatus("CANCELLED");
                            emailRequest.setEmailType("cancel");
                            emailComponent.setMailServerPreference(emailRequest);
                            emailComponent.sendEmail(emailRequest, emailData);
                        }
                    } else {
                        logger.error("Email templates not configured properly");
                    }
                } else {
                    logger.warn("Customer email address not available. So email not sending!!!");
                }
            } else {
                logger.error("Cancel appointment failed. Response recieved from cancel appointment stored procedure ::" + errorMessage);
            }
        } catch (Exception e) {
            logger.error("Cancellation email failed to send. " + e, e);
        }
    }

    /**
     * Used to prepare the email place holder data.
     *
     * @param logger
     * @param emailData
     * @param cdConfig
     * @param langCode
     * @param displayKey
     * @param displayValues
     * @throws TelAppointException , Exception
     */
    private void populateDataForEmail(Logger logger, Map<String, String> emailData, ClientDeploymentConfig cdConfig, String langCode, String displayKey, String displayValues)
            throws TelAppointException, Exception {
        try {
            String displayKeyArray[] = displayKey != null ? displayKey.split("\\|") : null;
            String displayValuesArray[] = displayKey != null ? displayValues.split("\\|") : null;
            if (displayKeyArray != null && displayValuesArray != null) {
                String rFirstName = null;
                String rLastName = null;
                String rPrefix = null;

                for (int i = 0; i < displayKeyArray.length; i++) {
                    String key = displayKeyArray[i];
                    String value = displayValuesArray[i] == null ? "" : displayValuesArray[i];
                    if (key.contains("appt_date_time_display")) {
                        String dateTime = displayValuesArray[i];
                        emailData.put("%APPTDATE%", dateTime.substring(0, dateTime.length() - 8).trim());
                        emailData.put("%APPTTIME%", dateTime.substring(dateTime.length() - 8, dateTime.length()));
                        emailData.put("%SHORTAPPTDATE%", (dateTime.split(","))[1]);
                    }

                    if (key.equals("s.appt_date_time")) {
                        String dateTime = value;
                        emailData.put("%DB_DATE_TIME%", dateTime);
                    }

                    if (key.equals("s.appt_date_time_start")) {
                        String dateTime = value;
                        emailData.put("%STARTDATE%",
                                emailComponent.getDate(cdConfig.getTimeZone(), langCode, dateTime, CommonDateContants.DATETIME_FORMAT_YYYYMMDDHHMMSS_CAP.getValue()));
                    }

                    if (key.equals("s.appt_date_time_end")) {
                        String dateTime = value;
                        emailData.put("%ENDDATE%",
                                emailComponent.getDate(cdConfig.getTimeZone(), langCode, dateTime, CommonDateContants.DATETIME_FORMAT_YYYYMMDDHHMMSS_CAP.getValue()));
                    }

                    if (key.equals("r.first_name")) {
                        rFirstName = value;
                    }

                    if (key.equals("r.last_name")) {
                        rLastName = value;
                    }

                    if (key.equals("r.email")) {
                        emailData.put("%RESOURCEEMAIL%", value);
                    }

                    if (key.equals("c.first_name")) {
                        emailData.put("%FIRSTNAME%", value);
                    }

                    if (key.equals("c.last_name")) {
                        emailData.put("%LASTNAME%", value);
                    }

                    if (key.contains("location_name_online")) {
                        emailData.put("%LOCATION%", value);
                    }

                    if (key.equals("l.address")) {
                        emailData.put("%LOCATIONADDRESS%", value);
                    }

                    if (key.equals("l.city")) {
                        emailData.put("%LOCATIONCITY%", value);
                    }

                    if (key.equals("l.state")) {
                        emailData.put("%LOCATIONSTATE%", value);
                    }

                    if (key.equals("l.zip")) {
                        emailData.put("%LOCATIONZIP%", value);
                    }

                    if (key.equals("l.location_google_map")) {
                        emailData.put("%LOCATIONGOOGLEMAP%", value);
                    }

                    if (key.equals("l.location_google_map_link")) {
                        emailData.put("%LOCATIONGOOGLEMAPLINK%", value);
                    }

                    if (key.equals("p.procedure_name_online")) {
                        emailData.put("%PROCEDURE%", value);
                    }

                    if (key.equals("ia.message_value.service")) {
                        emailData.put("%SERVICE%", value);
                    }

                    if (key.equals("d.department_name_online")) {
                        emailData.put("%DEPARTMENT%", value);
                    }

                    if (key.equals("c.account_number")) {
                        emailData.put("%ACCOUNTNUMBER%", value);
                    }

                    if (key.equals("c.contact_phone")) {
                        emailData.put("%CONTACTPHONE%", value);
                    }

                    if (key.equals("c.home_phone")) {
                        emailData.put("%HOMEPHONE%", value);
                    }

                    if (key.equals("c.work_phone")) {
                        emailData.put("%WORKPHONE%", value);
                    }

                    if (key.equals("c.cell_phone")) {
                        emailData.put("%CELLPHONE%", value);
                    }

                    if (key.equals("c.email")) {
                        emailData.put("%CMAIL%", value);
                    }

                    if (key.equals("c.attrib1")) {
                        emailData.put("%ATTRIB1%", value);
                    }

                    if (key.equals("c.attrib2")) {
                        emailData.put("%ATTRIB2%", value);
                    }

                    if (key.equals("c.attrib3")) {
                        emailData.put("%ATTRIB3%", value);
                    }

                    if (key.equals("c.attrib4")) {
                        emailData.put("%ATTRIB4%", value);
                    }

                    if (key.equals("c.attrib5")) {
                        emailData.put("%ATTRIB5%", value);
                    }

                    if (key.equals("c.attrib6")) {
                        emailData.put("%ATTRIB6%", value);
                    }

                    if (key.equals("c.attrib7")) {
                        emailData.put("%ATTRIB7%", value);
                    }

                    if (key.equals("c.attrib8")) {
                        emailData.put("%ATTRIB8%", value);
                    }

                    if (key.equals("c.attrib9")) {
                        emailData.put("%ATTRIB9%", value);
                    }

                    if (key.equals("c.attrib10")) {
                        emailData.put("%ATTRIB10%", value);
                    }

                    if (key.equals("doc.display_text")) {
                        emailData.put("%DISPLAYTEXT%", value);
                    }

                    if (key.contains("conf_number")) {
                        emailData.put("%CONFNUM%", value);
                    }

                    if (key.contains("l.time_zone")) {
                        emailData.put("%TIMEZONE%", value);
                    }
                }
                emailData.put("%RESOURCE%", rPrefix + " " + rLastName + " " + rFirstName);
            } else {
                logger.error("DispalyKeys and DisplayValues should not be empty!");
            }
        } catch (ArrayIndexOutOfBoundsException aiob) {
            throw new TelAppointException(ErrorConstants.ERROR_2003.getCode(), ErrorConstants.ERROR_2003.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, aiob.getMessage(),
                    "DisplayKeys or Display values format not valid.");
        }
    }

    @Override
    public ResponseEntity<ResponseModel> getServiceDropDownList(String clientCode, Integer locationId, Integer resourceId) throws Exception {
        ServiceResponse serviceResponse = new ServiceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        serviceResponse.setServiceList(adminDAO.getServiceDropDownList(jdbcCustomTemplate, FilterKeyWordContants.SERVICES_DROP_DOWN_DATA.getFilterKey(), cdConfig.getBlockTimeInMins(), locationId, resourceId, true));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(serviceResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getResourceServiceList(String clientCode, Integer locationId, Integer loginUserId) throws Exception {
        ResourceServiceResponse rsResponse = new ResourceServiceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        rsResponse.setResourceServices(adminDAO.getResourceServiceList(jdbcCustomTemplate, locationId, cdConfig.getBlockTimeInMins()));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(rsResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getDailyCalendar(String clientCode, String calendarDate, Integer locationId, String resourceIds) throws Exception {
        DailyCalendarResponse dailyCalendarResponse = new DailyCalendarResponse();
        dailyCalendarResponse.setCalendarDate(calendarDate);
        dailyCalendarResponse.setLocationId(locationId);
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        dailyCalendarResponse.setBlockTimeInMins(cdConfig.getBlockTimeInMins());
        String calendarDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(calendarDate);
        List<Integer> resourceIdList = Stream.of(resourceIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        adminDAO.getDailyCalendarData(jdbcCustomTemplate, calendarDateDB, locationId, resourceIdList, dailyCalendarResponse);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(dailyCalendarResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getWeeklyCalendar(String clientCode, String calendarDate, Integer locationId, String resourceIds) throws Exception {
        WeeklyCalendarResponse weeklyCalendarResponse = new WeeklyCalendarResponse();
        weeklyCalendarResponse.setLocationId(locationId);
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        weeklyCalendarResponse.setBlockTimeInMins(cdConfig.getBlockTimeInMins());
        String calendarDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(calendarDate);
        List<Integer> resourceIdList = Stream.of(resourceIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        adminDAO.getWeeklyCalendarData(jdbcCustomTemplate, calendarDateDB, locationId, resourceIdList, weeklyCalendarResponse);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(weeklyCalendarResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getServiceListByLocationId(String clientCode, Integer locationId, Integer loginUserId) throws Exception {
        ServiceResponse serviceResponse = new ServiceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);

        serviceResponse.setServiceList(adminDAO.getServiceListByLocationId(jdbcCustomTemplate, locationId, cdConfig.getBlockTimeInMins(), FilterKeyWordContants.SERVICES_DROP_DOWN_DATA.getFilterKey(), true));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(serviceResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getDynamicPledgeResults(String clientCode) throws Exception {
        DynamicPledgeResultResponse dynamicPledgeResponse = new DynamicPledgeResultResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        dynamicPledgeResponse.setDynamicPledgeResultList(adminDAO.getDynamicPledgeResultList(jdbcCustomTemplate));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(dynamicPledgeResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getMonthlyCalendar(String clientCode, String calendarDate, Integer locationId, String resourceIdStr) throws Exception {
        MonthlyCalendarResponse monthlyCalendarResponse = new MonthlyCalendarResponse();
        monthlyCalendarResponse.setLocationId(locationId);
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        monthlyCalendarResponse.setBlockTimeInMins(cdConfig.getBlockTimeInMins());
        List<Resource> resourceList = adminDAO.getResourcesByLocationId(jdbcCustomTemplate, locationId, FilterKeyWordContants.RESOURCE_DROP_DOWN_DATA.getFilterKey(), false);
        String calendarDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(calendarDate);
        List<Integer> resourceIds = null;
        if (resourceIdStr != null && !"".equals(resourceIdStr)) {
            resourceIds = Stream.of(resourceIdStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        } else {
            resourceIds = resourceList.stream().map(Resource::getResourceId).distinct().collect(Collectors.toList());
        }

        Map<String, Long> openTimeSlotsMap = adminDAO.getNoOfOpenAppts(jdbcCustomTemplate, resourceIds, calendarDateDB);
        Map<String, Long> bookedApptMap = adminDAO.getNoOfBookedAppts(jdbcCustomTemplate, resourceIds, calendarDateDB);
        Map<String, Long> closedTimeSlotMap = adminDAO.getNoOfClosedTimeSlots(jdbcCustomTemplate, resourceIds, calendarDateDB);
        Map<String, Long> totalTimeSlotsMap = adminDAO.getNoOfTotalTimeSlots(jdbcCustomTemplate, resourceIds, calendarDateDB);

        Map<String, Long> holidaysMap = adminDAO.getHolidaysMap(jdbcCustomTemplate, calendarDateDB);
        Map<String, Long> closeDaysMap = adminDAO.getClosedDaysMap(jdbcCustomTemplate, locationId, calendarDateDB);

        List<MonthlyApptData> openTimeSlots = adminDAO.getOpenTimeSlots(jdbcCustomTemplate, resourceIds, calendarDateDB);
        int minBlocks = adminDAO.getMinBlocksByAdmin(jdbcCustomTemplate, resourceIds);

        Map<String, PerDateAppts> map = new LinkedHashMap<String, PerDateAppts>();
        PerDateAppts perDateAppts = null;

        String firstLastDateOfMonth = adminDAO.getMonthFirstLastDate(jdbcCustomTemplate, calendarDateDB);
        String date = firstLastDateOfMonth.split("\\|")[0];
        String lastDate = firstLastDateOfMonth.split("\\|")[1];
        boolean isContinue = true;
        while (isContinue) {
            ResourceData resourceData = null;

            for (Integer resourceId : resourceIds) {
                perDateAppts = new PerDateAppts();
                Long noOfOpenSlots = (openTimeSlotsMap.get(date + "|" + resourceId) == null) ? new Long(0) : openTimeSlotsMap.get(date + "|" + resourceId);
                noOfOpenSlots = Long.valueOf(noOfOpenSlots.longValue() / minBlocks);
                Long noOfBookedAppt = (bookedApptMap.get(date + "|" + resourceId) == null) ? new Long(0) : bookedApptMap.get(date + "|" + resourceId);

                Long noOfClosedTimeSlots = (closedTimeSlotMap.get(date) == null) ? new Long(0) : closedTimeSlotMap.get(date);
                Long noOfTotalSlots = (totalTimeSlotsMap.get(date) == null) ? new Long(0) : totalTimeSlotsMap.get(date);

                boolean isHoliday = holidaysMap.containsKey(date);
                boolean isClosed = closeDaysMap.containsKey(date);

                perDateAppts.setNumberOfBookedAppts(null);
                perDateAppts.setTotalTimeSlots(null);
                perDateAppts.setNumberOfOpenSlots(null);
                perDateAppts.setNumberOfNotAvailable(null);
                perDateAppts.setIsFullyBooked(null);
                perDateAppts.setIsDateDisplay(null);
                perDateAppts.setIsSlotAvailable(null);

                if (isHoliday) {
                    perDateAppts.setIsHoliday("Y");
                    map.put(date, perDateAppts);
                } else if (isClosed) {
                    perDateAppts.setIsClosed("Y");
                } else {
                    //perDateAppts.setNumberOfBookedAppts(noOfBookedAppt);
                    //perDateAppts.setTotalTimeSlots(noOfTotalSlots);
                    //perDateAppts.setNumberOfOpenSlots(noOfOpenSlots);
                    //perDateAppts.setNumberOfNotAvailable(noOfClosedTimeSlots);


                    List<MonthlyApptData> filterArray = filterOpenTimeSlotsByDate(date, openTimeSlots);
                    boolean oneSlotAvailable = checkConsecutiveSlot(filterArray, minBlocks, cdConfig.getBlockTimeInMins());
                    if (oneSlotAvailable) {
                        //perDateAppts.setIsSlotAvailable("Y");
                        perDateAppts.setIsNotAvailable("N");
                        //perDateAppts.setIsFullyBooked("N");
                        //perDateAppts.setIsDateDisplay("Y");
                    } else {
                        if (noOfTotalSlots.equals(noOfClosedTimeSlots)) {
                            perDateAppts.setIsNotAvailable("Y");
                            //perDateAppts.setIsFullyBooked("N");
                            //perDateAppts.setIsSlotAvailable("N");
                            //perDateAppts.setIsDateDisplay("N");
                        } else {
                            //perDateAppts.setIsFullyBooked("Y");
                            //perDateAppts.setIsDateDisplay("N");
                            perDateAppts.setIsNotAvailable("N");
                            //perDateAppts.setIsSlotAvailable("N");
                        }
                    }

//					if(noOfTotalSlots.equals(noOfClosedTimeSlots)) {
//						perDateAppts.setIsNotAvailable("Y");
//					} else {
//						perDateAppts.setIsNotAvailable("N");
//					}


                    if (map.containsKey(date)) {
                        //PerDateAppts perDateAppts1 = map.get(date);
                        resourceData = new ResourceData();
                        resourceData.setNumberOfOpenSlots(noOfOpenSlots);
                        resourceData.setNumberOfBookedAppts(noOfBookedAppt);
                        resourceData.setResourceId(resourceId);
                        map.get(date).getResourceDataList().add(resourceData);
                        //map.put(date, perDateAppts1);
                    } else {
                        List<ResourceData> resourceDataList = new ArrayList<>();
                        resourceData = new ResourceData();
                        resourceData.setNumberOfOpenSlots(noOfOpenSlots);
                        resourceData.setNumberOfBookedAppts(noOfBookedAppt);
                        resourceData.setResourceId(resourceId);
                        resourceDataList.add(resourceData);
                        perDateAppts.setResourceDataList(resourceDataList);
                        map.put(date, perDateAppts);
                    }

                }
            }
            String nextDateAndIsContinue = adminDAO.getNextDateAndIsContinueValue(jdbcCustomTemplate, date, lastDate);
            date = nextDateAndIsContinue.split("\\|")[0];
            isContinue = "1".equals(nextDateAndIsContinue.split("\\|")[1]);
        }
        monthlyCalendarResponse.setPerDateAppts(map);
        monthlyCalendarResponse.setCalendarLastDate(DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(lastDate));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(monthlyCalendarResponse), HttpStatus.OK);
    }

    public List<MonthlyApptData> filterOpenTimeSlotsByDate(String dateyyyyMMdd, List<MonthlyApptData> openTimeSlotsList) {
        List<MonthlyApptData> list = new ArrayList<>();
        boolean flag = false;
        for (MonthlyApptData monthlyApptData : openTimeSlotsList) {
            String yyyymmdd = monthlyApptData.getDate();
            if (yyyymmdd.equals(dateyyyyMMdd)) {
                flag = true;
                list.add(monthlyApptData);
            } else {
                if (flag) {
                    break;
                } else {
                    continue;
                }
            }
        }
        return list;
    }

    @Override
    public ResponseEntity<ResponseModel> getJSCalendarAvailablity(String clientCode, String calendarDate, Integer locationId, String resourceIdStr, String serviceIdStr) throws Exception {
        JSCalendarResponse jsCalendarResponse = new JSCalendarResponse();
        jsCalendarResponse.setLocationId(locationId);
        jsCalendarResponse.setResourceIds(resourceIdStr);
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        if (calendarDate == null || "".equals(calendarDate)) {
            calendarDate = getFirstAvaialbleDate(jdbcCustomTemplate, locationId, resourceIdStr, serviceIdStr);
            calendarDate = DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(calendarDate);
            if (calendarDate == null) {
                jsCalendarResponse.setErrorFlag("Y");
                jsCalendarResponse.setErrorMessage("No Available dates for selection - locationId:" + locationId + ", resourceIds: " + resourceIdStr + ", serviceIds:" + serviceIdStr);
                return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(jsCalendarResponse), HttpStatus.OK);
            }
            jsCalendarResponse.setFirstAvailableDate(calendarDate);
        }

        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        List<Resource> resourceList = adminDAO.getResourcesByLocationId(jdbcCustomTemplate, locationId, FilterKeyWordContants.RESOURCE_DROP_DOWN_DATA.getFilterKey(), false);
        String calendarDateDB = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(calendarDate);
        List<Integer> resourceIds = null;
        if (resourceIdStr != null && !"".equals(resourceIdStr)) {
            resourceIds = Stream.of(resourceIdStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        } else {
            resourceIds = resourceList.stream().map(Resource::getResourceId).distinct().collect(Collectors.toList());
        }

        Map<String, Long> openTimeSlotsMap = adminDAO.getJSNoOfOpenAppts(jdbcCustomTemplate, resourceIds, calendarDateDB);
        Map<String, Long> closedTimeSlotMap = adminDAO.getNoOfClosedTimeSlots(jdbcCustomTemplate, resourceIds, calendarDateDB);
        Map<String, Long> totalTimeSlotsMap = adminDAO.getNoOfTotalTimeSlots(jdbcCustomTemplate, resourceIds, calendarDateDB);

        Map<String, Long> holidaysMap = adminDAO.getHolidaysMap(jdbcCustomTemplate, calendarDateDB);
        Map<String, Long> closeDaysMap = adminDAO.getClosedDaysMap(jdbcCustomTemplate, locationId, calendarDateDB);

        List<MonthlyApptData> openTimeSlots = adminDAO.getOpenTimeSlots(jdbcCustomTemplate, resourceIds, calendarDateDB);
        int minBlocks = adminDAO.getMinBlocksByAdmin(jdbcCustomTemplate, resourceIds);

        Map<String, PerDateAppts> map = new LinkedHashMap<String, PerDateAppts>();
        PerDateAppts perDateAppts = null;

        String firstLastDateOfMonth = adminDAO.getMonthFirstLastDate(jdbcCustomTemplate, calendarDateDB);
        String date = firstLastDateOfMonth.split("\\|")[0];
        String lastDate = firstLastDateOfMonth.split("\\|")[1];
        boolean isContinue = true;
        while (isContinue) {
            perDateAppts = new PerDateAppts();
            Long noOfOpenSlots = (openTimeSlotsMap.get(date) == null) ? new Long(0) : openTimeSlotsMap.get(date);
            //TODO: is not correct logic - as per anantha, we need to refine correct logic by using consecutive time blocks and etc.
            noOfOpenSlots = Long.valueOf(noOfOpenSlots.longValue() / minBlocks);
            Long noOfClosedTimeSlots = (closedTimeSlotMap.get(date) == null) ? new Long(0) : closedTimeSlotMap.get(date);
            Long noOfTotalSlots = (totalTimeSlotsMap.get(date) == null) ? new Long(0) : totalTimeSlotsMap.get(date);

            boolean isHoliday = holidaysMap.containsKey(date);
            boolean isClosed = closeDaysMap.containsKey(date);
            perDateAppts.setNumberOfBookedAppts(null);
            perDateAppts.setTotalTimeSlots(null);
            perDateAppts.setNumberOfOpenSlots(null);
            perDateAppts.setNumberOfNotAvailable(null);
            perDateAppts.setIsDateDisplay(null);

            if (isHoliday) {
                perDateAppts.setIsHoliday("Y");
            } else if (isClosed) {
                perDateAppts.setIsClosed("Y");
            } else {
                List<MonthlyApptData> filterArray = filterOpenTimeSlotsByDate(date, openTimeSlots);
                boolean oneSlotAvailable = checkConsecutiveSlot(filterArray, minBlocks, cdConfig.getBlockTimeInMins());
                if (oneSlotAvailable) {
                    perDateAppts.setIsSlotAvailable("Y");
                    perDateAppts.setIsFullyBooked("N");
                } else {
                    if (noOfTotalSlots.equals(noOfClosedTimeSlots)) {
                        perDateAppts.setIsNotAvailable("Y");
                        perDateAppts.setIsFullyBooked("N");
                        perDateAppts.setIsSlotAvailable("N");
                    } else {
                        perDateAppts.setIsFullyBooked("Y");
                        perDateAppts.setIsNotAvailable("N");
                        perDateAppts.setIsSlotAvailable("N");
                    }
                }

                if (noOfTotalSlots.equals(noOfClosedTimeSlots)) {
                    perDateAppts.setIsNotAvailable("Y");
                } else {
                    perDateAppts.setIsNotAvailable("N");
                }
            }
            map.put(date, perDateAppts);

            String nextDateAndIsContinue = adminDAO.getNextDateAndIsContinueValue(jdbcCustomTemplate, date, lastDate);
            date = nextDateAndIsContinue.split("\\|")[0];
            isContinue = "1".equals(nextDateAndIsContinue.split("\\|")[1]);
        }
        jsCalendarResponse.setPerDateAppts(map);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(jsCalendarResponse), HttpStatus.OK);
    }

    private String getFirstAvaialbleDate(JdbcCustomTemplate jdbcCustomTemplate, Integer locationId, String resourceIdStr, String serviceIdStr) throws Exception {
        List<String> availableDates = getAvailableDates(jdbcCustomTemplate, Long.valueOf(locationId), (long) 1, resourceIdStr, serviceIdStr);
        if (!availableDates.isEmpty()) {
            return availableDates.get(0);
        }
        return null;
    }

    /**
     * Used to check the firstAvailable consecutive time blocks.
     *
     * @param list
     * @param blocks
     * @param blocksInMins
     * @return true, if conSecutiveslot available. otherwise - false
     */

    public boolean checkConsecutiveSlot(List<MonthlyApptData> list, int blocks, int blocksInMins) {
        boolean isFirstLoop = true;
        int blockCount = blocks;
        int prevTimeSlotsInMins = 0;
        int timeSlotInMins = 0;

        for (MonthlyApptData monthlyApptData : list) {
            Long scheduleId = monthlyApptData.getCount();
            String timestr = monthlyApptData.getDate().split(" ")[1];
            String minutes = timestr.substring(timestr.indexOf(':') + 1).trim();
            String hours = timestr.substring(0, 2).trim();
            timeSlotInMins = Integer.valueOf(hours) * 60 + Integer.valueOf(minutes);
            if (scheduleId == 0) {
                if (isFirstLoop) {
                    isFirstLoop = false;
                    blockCount--;
                    if (blockCount == 0) {
                        return true;
                    }
                } else {
                    if ((timeSlotInMins - prevTimeSlotsInMins) == blocksInMins) {
                        blockCount--;
                        if (blockCount == 0) {
                            return true;
                        }
                    } else {
                        blockCount = blocks;
                        isFirstLoop = true;
                    }
                }
            } else {
                blockCount = blocks;
                isFirstLoop = true;
            }
            prevTimeSlotsInMins = timeSlotInMins;
        }
        return false;
    }

    @Override
    public ResponseEntity<ResponseModel> holdAppointment(String clientCode, Long locationId, Long resourceId,
                                                         Long procedureId, Long departmentId, Long serviceId, Long customerId,
                                                         String apptDateTime, String device, String langCode, Long transId) throws Exception {

        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        HoldAppt holdAppt = getHoldAppt(jdbcCustomTemplate, locationId, resourceId, procedureId, departmentId, serviceId, customerId, apptDateTime, device, langCode, transId);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(holdAppt), HttpStatus.OK);
    }

    private HoldAppt getHoldAppt(JdbcCustomTemplate jdbcCustomTemplate, Long locationId, Long resourceId,
                                 Long procedureId, Long departmentId, Long serviceId, Long customerId,
                                 String apptDateTime, String device, String langCode, Long transId) throws Exception {
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        HoldAppt holdAppt = adminDAO.holdAppointment(jdbcCustomTemplate, device, locationId, resourceId, procedureId, departmentId, serviceId, customerId, apptDateTime, cdConfig,
                transId);

        String errorMsg = holdAppt.getErrorMessage();
        if (errorMsg != null && "DUPLICATE_APPT".equalsIgnoreCase(errorMsg.toString())) {
            holdAppt.setErrorFlag("Y");
            if (langCode == null || "".equals(langCode)) {
                langCode = "us-en";
            }
            Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, device, langCode, true);
            if (labelMap != null) {
                holdAppt.setErrorMessage(labelMap.get("DUPLICATE_APPT"));
            }
        } else if (errorMsg != null && "SELECTED_DATE_TIME_NOT_AVAILABLE".equalsIgnoreCase(errorMsg.toString())) {
            holdAppt.setErrorFlag("Y");
            if (langCode == null || "".equals(langCode)) {
                langCode = "us-en";
            }
            Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, device, langCode, true);
            if (labelMap != null) {
                holdAppt.setErrorMessage(labelMap.get("SELECTED_DATE_TIME_NOT_AVAILABLE"));
            }
        } else if (errorMsg != null && "HOLD_NOT_RELEASED".equalsIgnoreCase(errorMsg.toString())) {
            holdAppt.setErrorFlag("Y");
            if (langCode == null || "".equals(langCode)) {
                langCode = "us-en";
            }
            Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, device, langCode, true);
            if (labelMap != null) {
                holdAppt.setErrorMessage(labelMap.get("HOLD_NOT_RELEASED"));
            }
        } else if (errorMsg != null) {
            logger.error("Error from hold appointment call center stored procedure: " + errorMsg);
        }
        return holdAppt;
    }

    /**
     * Used to confirm the appointment, It will do with two steps. 1. Confirm
     * the appointment using book appointment stored procedure. 2. Send an email
     * with Asynchronous call. if any error it will log it without throwing the
     * exception.
     *
     * @throws TelAppointException , Exception
     */
    @Override
    public ResponseEntity<ResponseModel> confirmAppointment(ConfirmAppointmentRequest confirmApptReq) throws Exception {
        if (confirmApptReq.getCustomerId() <= 0) {
            ConfirmAppointmentResponse confirmApptRes = new ConfirmAppointmentResponse();
            confirmApptRes.setStatus(false);
            confirmApptRes.setErrorFlag("Y");
            confirmApptRes.setErrorMessage("CustomerId should be greater then zero. Passed customerId: " + confirmApptReq.getCustomerId());
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(confirmApptRes), HttpStatus.OK);
        }

        Client client = cacheComponent.getClient(confirmApptReq.getClientCode(), true);
        boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(confirmApptReq.getClientCode(), cache);
        ConfirmAppointmentResponse confirmApptRes = bookppointment(jdbcCustomTemplate, confirmApptReq, cdConfig);
        String errorMessage = confirmApptRes.getMessage();
        if (confirmApptRes.isStatus() && ("".equals(errorMessage) || errorMessage == null)) {
            adminDAO.updateCustomerIdInSchedule(jdbcCustomTemplate, logger, confirmApptReq.getCustomerId(), confirmApptReq.getScheduleId());
            adminDAO.updateSchedule(jdbcCustomTemplate, confirmApptReq);
            Map<String, String> emailData = new HashMap<String, String>();
            String emailType = "confirm";
            sendConfirmEmail(errorMessage, jdbcCustomTemplate, client, cdConfig, confirmApptReq, confirmApptRes, emailData, emailType);
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(confirmApptRes), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(confirmApptRes), HttpStatus.OK);
        }

    }

    private ConfirmAppointmentResponse bookppointment(JdbcCustomTemplate jdbcCustomTemplate, ConfirmAppointmentRequest confirmApptReq,
                                                      ClientDeploymentConfig cdConfig) throws TelAppointException, Exception {
        ConfirmAppointmentResponse confirmApptRes = new ConfirmAppointmentResponse();
        Integer apptMethod = AppointmentMethod.ADMIN.getMethod();

        adminDAO.bookAppointment(jdbcCustomTemplate, confirmApptReq.getScheduleId(), confirmApptReq.getLangCode(), apptMethod, cdConfig, confirmApptRes);
        return confirmApptRes;
    }

    private void sendConfirmEmail(String errorMessage, JdbcCustomTemplate jdbcCustomTemplate, Client client, ClientDeploymentConfig cdConfig,
                                  ConfirmAppointmentRequest confirmApptReq, ConfirmAppointmentResponse confirmApptRes, Map<String, String> emailData, String emailType) {
        try {
            if (errorMessage == null) {
                adminDAO.updateTransId(jdbcCustomTemplate, confirmApptReq.getScheduleId(), confirmApptReq.getTransId());

                EmailRequest emailRequest = new EmailRequest();
                ApptSysConfig apptSysConfig = adminDAO.getAppSysConfig(jdbcCustomTemplate);
                String ccConfirmEmail[] = null;
                if (apptSysConfig != null && apptSysConfig.getCcConfirmEmails() != null && apptSysConfig.getCcConfirmEmails().trim().length() > 0) {
                    ccConfirmEmail = apptSysConfig.getCcConfirmEmails().split(",");
                }
                if (ccConfirmEmail != null) {
                    emailRequest.setCcAddresses(ccConfirmEmail);
                }

                String langCode = confirmApptReq.getLangCode();
                emailData.put("%CLIENTAPPTLINK%", client.getApptLink() == null ? "" : client.getApptLink());
                emailData.put("%CLIENTNAME%", client.getClientName() == null ? "" : client.getClientName());
                emailData.put("%CLIENTADDRESS%", client.getAddress() == null ? "" : client.getAddress());
                populateDataForEmail(logger, emailData, cdConfig, confirmApptReq.getLangCode(), confirmApptRes.getDisplayKeys(), confirmApptRes.getDisplayValues());
                emailData.put("%SCHEDULEID%", "" + confirmApptReq.getScheduleId());
                String email = emailData.get("%CMAIL%");
                if (email != null && !"".equals(email)) {
                    Map<String, String> emailTemplateMap = cacheComponent.getEmailTemplateMap(jdbcCustomTemplate, logger, langCode, false);
                    if (emailTemplateMap != null) {
                        String emailSubjectTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_APPT_CONFIRM_SUBJECT.getValue());
                        String emailBodyTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_APPT_CONFIRM_BODY.getValue());
                        String emailSubject = emailComponent.getEmailSubject(emailSubjectTemplate, emailData);
                        String emailBody = emailComponent.getEmailBody(emailBodyTemplate, emailData);
                        emailRequest.setSubject(emailSubject);
                        emailRequest.setEmailBody(emailBody);
                        logger.info("Email Body: " + emailBody);
                        emailRequest.setToAddress(email);
                        emailRequest.setMethod("REQUEST");
                        emailRequest.setStatus("CONFIRMED");
                        emailRequest.setEmailType(emailType);
                        emailComponent.setMailServerPreference(emailRequest);
                        emailComponent.sendEmail(emailRequest, emailData);
                    } else {
                        logger.error("Email templates not configured properly");
                    }
                } else {
                    logger.warn("Customer email address not available. So email not sending!!!");
                }
            } else {
                logger.error("Book appointment failed. Response recieved from book appointment stored procedure ::" + errorMessage);
            }
        } catch (Exception e) {
            logger.error("Confirmation email failed to send. " + e, e);
        }
    }

    private void sendReScheduleEmail(String errorMessage, JdbcCustomTemplate jdbcCustomTemplate, Client client, ClientDeploymentConfig cdConfig,
                                     ConfirmAppointmentRequest confirmApptReq, ConfirmAppointmentResponse confirmApptRes, Map<String, String> emailData, String emailType) {
        try {
            if (errorMessage == null) {
                adminDAO.updateTransId(jdbcCustomTemplate, confirmApptReq.getScheduleId(), confirmApptReq.getTransId());

                EmailRequest emailRequest = new EmailRequest();
                ApptSysConfig apptSysConfig = adminDAO.getAppSysConfig(jdbcCustomTemplate);
                String ccConfirmEmail[] = null;
                if (apptSysConfig != null && apptSysConfig.getCcConfirmEmails() != null && apptSysConfig.getCcConfirmEmails().trim().length() > 0) {
                    ccConfirmEmail = apptSysConfig.getCcConfirmEmails().split(",");
                }
                if (ccConfirmEmail != null) {
                    emailRequest.setCcAddresses(ccConfirmEmail);
                }

                String langCode = confirmApptReq.getLangCode();
                emailData.put("%CLIENTAPPTLINK%", client.getApptLink() == null ? "" : client.getApptLink());
                emailData.put("%CLIENTNAME%", client.getClientName() == null ? "" : client.getClientName());
                emailData.put("%CLIENTADDRESS%", client.getAddress() == null ? "" : client.getAddress());
                populateDataForEmail(logger, emailData, cdConfig, confirmApptReq.getLangCode(), confirmApptRes.getDisplayKeys(), confirmApptRes.getDisplayValues());
                emailData.put("%SCHEDULEID%", "" + confirmApptReq.getScheduleId());
                String email = emailData.get("%CMAIL%");
                if (email != null && !"".equals(email)) {
                    Map<String, String> emailTemplateMap = cacheComponent.getEmailTemplateMap(jdbcCustomTemplate, logger, langCode, false);
                    if (emailTemplateMap != null) {
                        String emailSubjectTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_APPT_RESCHEDULE_SUBJECT.getValue());
                        String emailBodyTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_APPT_RESCHEDULE_BODY.getValue());
                        String emailSubject = emailComponent.getEmailSubject(emailSubjectTemplate, emailData);
                        String emailBody = emailComponent.getEmailBody(emailBodyTemplate, emailData);
                        emailRequest.setSubject(emailSubject);
                        emailRequest.setEmailBody(emailBody);
                        logger.info("Email Body: " + emailBody);
                        emailRequest.setToAddress(email);
                        emailRequest.setMethod("REQUEST");
                        emailRequest.setStatus("RESCHEDULE");
                        emailRequest.setEmailType(emailType);
                        emailComponent.setMailServerPreference(emailRequest);
                        emailComponent.sendEmail(emailRequest, emailData);
                    } else {
                        logger.error("Email templates not configured properly");
                    }
                } else {
                    logger.warn("Customer email address not available. So email not sending!!!");
                }
            } else {
                logger.error("Book appointment failed. Response recieved from book appointment stored procedure ::" + errorMessage);
            }
        } catch (Exception e) {
            logger.error("Confirmation email failed to send. " + e, e);
        }
    }

    @Override
    public ResponseEntity<ResponseModel> getAutoSuggestCustomerNames(String clientCode, String customerName) throws Exception {
        CustomerNamesResponse customerNamesRes = new CustomerNamesResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        customerNamesRes.setCustomerNames(adminDAO.getCustomerList(jdbcCustomTemplate, customerName));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(customerNamesRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getCustomerRegistrationDetails(String clientCode, String langCode) throws Exception {
        CustomerRegistrationRepsonse custRegResponse = new CustomerRegistrationRepsonse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<String> loginTypes = new ArrayList<>();
        loginTypes.add("registration");
        Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, "admin", langCode, true);
        custRegResponse.setCustomerRegistrationList(adminDAO.getCustomerRegistrationList(jdbcCustomTemplate, langCode, loginTypes, labelMap));


        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(custRegResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> createCustomer(CustomerRequest customerRequest) throws Exception {
        logger.info("create Customer");
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(customerRequest.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        boolean exist = customerExist(jdbcCustomTemplate, customerRequest);
        if (exist) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Customer already exist!");
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
        }
        long customerId = adminDAO.saveCustomer(jdbcCustomTemplate, customerRequest, cdConfig);
        if (customerId == 0) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Customer create failed!");
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> createOrUpdateCustomer(CustomerRequest customerRequest) throws Exception {
        logger.info("createOrUpdate Customer");
        CustomerResponse baseResponse = new CustomerResponse();
        Client client = cacheComponent.getClient(customerRequest.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        long customerId = getCustomerIdIfExist(jdbcCustomTemplate, customerRequest);
        if (customerId > 0) {
            customerRequest.getCustomer().setCustomerId(customerId);
            adminDAO.updateCustomer(jdbcCustomTemplate, customerRequest);
        } else {
            customerId = adminDAO.saveCustomer(jdbcCustomTemplate, customerRequest, cdConfig);
        }
        if (customerId == 0) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("createOrUpateCustomer failed!");
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
        }
        baseResponse.setCustomerId(customerId);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    private boolean customerExist(JdbcCustomTemplate jdbcCustomTemplate, CustomerRequest customerRequest) throws Exception {
        List<String> loginTypes = new ArrayList<>();
        loginTypes.add("registration");
        List<CustomerRegistration> customerRegList = adminDAO.getCustomerRegistrationList(jdbcCustomTemplate, "us-en", loginTypes, null);
        return adminDAO.customerExist(jdbcCustomTemplate, customerRegList, customerRequest);
    }

    private Long getCustomerIdIfExist(JdbcCustomTemplate jdbcCustomTemplate, CustomerRequest customerRequest) throws Exception {
        List<String> loginTypes = new ArrayList<>();
        loginTypes.add("registration");
        List<CustomerRegistration> customerRegList = adminDAO.getCustomerRegistrationList(jdbcCustomTemplate, "us-en", loginTypes, null);
        return adminDAO.getCustomerIdIfExist(jdbcCustomTemplate, customerRegList, customerRequest);
    }

    @Override
    public ResponseEntity<ResponseModel> updateCustomer(CustomerRequest customerRequest) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(customerRequest.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean exist = customerExist(jdbcCustomTemplate, customerRequest);
        if (exist) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Customer already exist!");
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
        }
        boolean isUpated = adminDAO.updateCustomer(jdbcCustomTemplate, customerRequest);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getFutureAppointments(String clientCode, long customerId) throws Exception {
        AppointmentsDataResponse apptDataResponse = new AppointmentsDataResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<AppointmentDetails> apptList = new ArrayList<AppointmentDetails>();
        boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, cache);
        adminDAO.getFutureAppointments(jdbcCustomTemplate, customerId, cdConfig, apptList);
        apptDataResponse.setBookedAppts(apptList);
        if (apptList.isEmpty()) {
            apptDataResponse.setMessage("No Booked Appointments.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(apptDataResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getPastAppointments(String clientCode, long customerId) throws Exception {
        AppointmentsDataResponse apptDataResponse = new AppointmentsDataResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<AppointmentDetails> apptList = new ArrayList<AppointmentDetails>();
        boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, cache);
        adminDAO.getPastAppointments(jdbcCustomTemplate, customerId, cdConfig, apptList);
        apptDataResponse.setBookedAppts(apptList);
        if (apptList.isEmpty()) {
            apptDataResponse.setMessage("No Booked Appointments.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(apptDataResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> rescheduleAppointment(String clientCode, Long locationId, Long resourceId, Long procedureId, Long departmentId, Long serviceId,
                                                               Long customerId, String apptDateTime, String device, String langCode, Long transId, Long oldscheduleId) throws Exception {
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

        boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, cache);

        CancelAppointResponse cancelAppointResponse = new CancelAppointResponse();
        Integer cancelMethod = AppointmentMethod.ADMIN.getMethod();
        ;
        adminDAO.cancelAppointment(jdbcCustomTemplate, oldscheduleId, cancelMethod, langCode, cdConfig, cancelAppointResponse);
        String cancelErrorMessage = cancelAppointResponse.getMessage();
        if (cancelErrorMessage == null || "".equals(cancelErrorMessage)) {
            EmailRequest emailRequest = new EmailRequest();
            sendCancelEmail(logger, cancelErrorMessage, jdbcCustomTemplate, client, cdConfig, cancelAppointResponse, oldscheduleId, langCode, emailRequest);
        } else {
            cancelAppointResponse.setErrorFlag(null);
            logger.error("Cancel appointment failed. passed scheduleId: " + oldscheduleId);
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(cancelAppointResponse), HttpStatus.OK);
        }

        HoldAppt holdAppt = getHoldAppt(jdbcCustomTemplate, locationId, resourceId, procedureId, departmentId, serviceId, customerId, apptDateTime, device, langCode, transId);
        if ("Y".equals(holdAppt.getErrorFlag())) {
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(holdAppt), HttpStatus.OK);
        }
        ConfirmAppointmentRequest confirmApptReq = new ConfirmAppointmentRequest();
        confirmApptReq.setClientCode(clientCode);
        confirmApptReq.setLangCode(langCode);
        confirmApptReq.setScheduleId(holdAppt.getScheduleId());
        confirmApptReq.setTransId(transId);
        confirmApptReq.setCustomerId(customerId);
        ConfirmAppointmentResponse confirmApptRes = bookppointment(jdbcCustomTemplate, confirmApptReq, cdConfig);
        String errorMessage = confirmApptRes.getMessage();
        if (confirmApptRes != null && !"".equals(confirmApptRes)) {
            return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(confirmApptRes), HttpStatus.OK);
        } else {
            Map<String, String> emailData = new HashMap<String, String>();
            String emailType = "reschedule";
            sendReScheduleEmail(errorMessage, jdbcCustomTemplate, client, cdConfig, confirmApptReq, confirmApptRes, emailData, emailType);
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(confirmApptRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getVerifyPageData(String clientCode, String device, String langCode, Long customerId, Long scheduleId) throws Exception {
        VerifyPageResponse verifyPageResponse = new VerifyPageResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        if (customerId.longValue() <= 0) {
            verifyPageResponse.setStatus(false);
            verifyPageResponse.setErrorFlag("Y");
            verifyPageResponse.setErrorMessage("CustomerId should be passed to backend.");
        } else {
            adminDAO.updateCustomerIdInSchedule(jdbcCustomTemplate, customerId, scheduleId);
        }
        verifyPageResponse.setVerifyPageData(adminDAO.getVerfiyPageData(jdbcCustomTemplate, device, langCode, scheduleId, null));
        return new ResponseEntity<>(commonComponent.populateRMDData(verifyPageResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateAppointStatus(String clientCode, String screenedFlag, String accessedFlag, Integer status, Long scheduleId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        boolean isUpdated = adminDAO.updateAppointStatus(jdbcCustomTemplate, screenedFlag, accessedFlag, status, scheduleId);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getAppointmentStatus(String clientCode, Long scheduleId) throws Exception {
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ApptStatusResponse apptStatusRes = adminDAO.getAppointmentStatus(jdbcCustomTemplate, scheduleId);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(apptStatusRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateRecordTime(String clientCode, String recordName, String recordType, long scheduleId) throws Exception {
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, true);
        RecordTimeResponse recordTimeResponse = adminDAO.updateRecordTime(jdbcCustomTemplate, recordName, recordType, scheduleId, cdConfig.getTimeZone());
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(recordTimeResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getPledgeHistory(String clientCode, String device, String langCode, Long customerId, Long transId) throws Exception {
        CustomerPledgeResponse pledgeRes = new CustomerPledgeResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        adminDAO.getPledgeHistory(jdbcCustomTemplate, logger, device, langCode, customerId, pledgeRes);
        List<CustomerPledge> customerPledgeList = pledgeRes.getCustomerPledgeList();
        if (customerPledgeList != null && customerPledgeList.isEmpty()) {
            pledgeRes.setErrorFlag("Y");
            pledgeRes.setErrorMessage("No Pledge History found!");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(pledgeRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getSameServiceBlockList(String clientCode, Integer locationId, Integer resourceId, Integer serviceId) throws Exception {
        ServiceResponse serviceRes = new ServiceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, true);
        List<ServiceVO> serviceList = adminDAO.getSameServiceBlockList(jdbcCustomTemplate, resourceId, serviceId, "", cdConfig.getBlockTimeInMins());
        serviceRes.setServiceList(serviceList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(serviceRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getApptsForOutlook(String clientCode, String userName, String password) throws Exception {
        OutLookResponse outlookRes = new OutLookResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, true);
        String resourceIds = masterDAO.getOutLookClient(clientCode, userName, password);
        outlookRes.setOutLookApptList(adminDAO.getApptsForOutlook(jdbcCustomTemplate, resourceIds, cdConfig.getBlockTimeInMins()));
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(outlookRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateOutlookSyncStatus(OutlookSyncReq outlookSyncReq) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(outlookSyncReq.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        adminDAO.updateOutlookSyncStatus(jdbcCustomTemplate, outlookSyncReq);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getSuggestedResourceWorkingHours(
            String clientCode, String locationId, String resourceIds, String fromDate, String toDate) throws Exception {
        ResourceWorkingHrs resourceWorkingHrs = new ResourceWorkingHrs();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

        List<Map<String, Object>> getResourceWorkingHoursList = adminDAO.getResourceWorkingHoursHistory(jdbcCustomTemplate, locationId, resourceIds, fromDate, toDate);
        if (getResourceWorkingHoursList != null && !getResourceWorkingHoursList.isEmpty()) {
            resourceWorkingHrs = prepareSuggestedResourceWorkingHours(resourceIds, resourceWorkingHrs, getResourceWorkingHoursList);
        } else if (getResourceWorkingHoursList == null || getResourceWorkingHoursList.isEmpty()) {
            getResourceWorkingHoursList = adminDAO.getResourceWorkingHoursHistory(jdbcCustomTemplate, locationId, resourceIds);
            if (getResourceWorkingHoursList != null && !getResourceWorkingHoursList.isEmpty()) {
                resourceWorkingHrs = prepareSuggestedResourceWorkingHours(resourceIds, resourceWorkingHrs, getResourceWorkingHoursList);
            } else if (getResourceWorkingHoursList == null || getResourceWorkingHoursList.isEmpty()) {
                getResourceWorkingHoursList = adminDAO.getResourceWorkingHours(jdbcCustomTemplate, locationId, resourceIds);
                resourceWorkingHrs = prepareSuggestedResourceWorkingHours(resourceIds, resourceWorkingHrs, getResourceWorkingHoursList);
            } else {
                resourceWorkingHrs = prepareDefaultSuggestedResourceWorkingHours(logger, resourceWorkingHrs, jdbcCustomTemplate);
            }
        } else {
            resourceWorkingHrs = prepareDefaultSuggestedResourceWorkingHours(logger, resourceWorkingHrs, jdbcCustomTemplate);
        }

        if (resourceWorkingHrs == null) {
            resourceWorkingHrs = new ResourceWorkingHrs();
            resourceWorkingHrs = prepareDefaultSuggestedResourceWorkingHours(logger, resourceWorkingHrs, jdbcCustomTemplate);
        }

        if (resourceWorkingHrs != null) {
            resourceWorkingHrs.setEffective_date(fromDate);
            resourceWorkingHrs.setEnd_date(toDate);
            ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, true);
            resourceWorkingHrs.setBlockTimeInMins(cdConfig.getBlockTimeInMins());

        }
        return new ResponseEntity<>(commonComponent.populateRMDData(resourceWorkingHrs), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateResourceWorkingHours(ResourceHoursRequest resourceHoursRequest) throws Exception {
        ResourceWorkingHrsResponse resourceWorkingHrsResponse = new ResourceWorkingHrsResponse();
        try {
            String clientCode = resourceHoursRequest.getClientCode();
            Client client = cacheComponent.getClient(clientCode, true);
            JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
            Boolean isCampaignExists = adminDAO.getCampaign(jdbcCustomTemplate, 8);

            if (isCampaignExists) {
                resourceWorkingHrsResponse.setDisplayNotifyCheckBox("Y");
            } else {
                resourceWorkingHrsResponse.setDisplayNotifyCheckBox("N");
            }
            // preparing resourceIds which was selected in front end.

            Set<Integer> resourceIds = new HashSet<>(resourceHoursRequest.getSelectedResourceIds());
            logger.debug("ResourceIds:" + resourceIds);
            // convert startDate and endDate to yyyy-mm-dd
            String startDateTimeStr = resourceHoursRequest.getStartDate() + " 00:00:00";

            Timestamp toTimestamp = null;
            String endDateTimeStr = resourceHoursRequest.getEndDate();


            // forever selected in front end - not required to prepare the
            // endDate.
            if (endDateTimeStr != null && "".equals(endDateTimeStr) == false) {
                endDateTimeStr = endDateTimeStr + " 23:59:59";
                endDateTimeStr = DateUtils.convertMMDDYYYY_TO_YYYYMMDDHHMMSSFormat(endDateTimeStr);
                Calendar endDateTime = DateUtils.formatSqlStringToGC(endDateTimeStr);
                toTimestamp = new Timestamp(endDateTime.getTimeInMillis());
            }

            startDateTimeStr = DateUtils.convertMMDDYYYY_TO_YYYYMMDDHHMMSSFormat(startDateTimeStr);
            Calendar startDateTime = DateUtils.formatSqlStringToGC(startDateTimeStr);
            Timestamp fromTimestamp = new Timestamp(startDateTime.getTimeInMillis());

            Timestamp minDateTime = null;
            Timestamp maxDateTime = null;
            List<Map<String, Object>> calMinMaxDateTimeList = null;
            if ("Forever".equals(resourceHoursRequest.getEndDateType())) {
                calMinMaxDateTimeList = adminDAO.getCalMinAndMaxDateTime(jdbcCustomTemplate, resourceIds, fromTimestamp);
            } else {
                calMinMaxDateTimeList = adminDAO.getCalMinAndMaxDateTime(jdbcCustomTemplate, resourceIds, fromTimestamp, toTimestamp);
            }
            if (calMinMaxDateTimeList != null && calMinMaxDateTimeList.size() > 0) {
                for (Map<String, Object> calMinMaxDateTime : calMinMaxDateTimeList) {
                    if (calMinMaxDateTime != null && calMinMaxDateTime.size() > 0) {
                        minDateTime = (Timestamp) calMinMaxDateTime.get("min_date_time");
                        maxDateTime = (Timestamp) calMinMaxDateTime.get("max_date_time");
                        break;
                    }
                }
            }
            String startDate = resourceHoursRequest.getStartDate();
            String endDate = resourceHoursRequest.getEndDate();
            startDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(startDate);

            if (endDate != null && !"".equals(endDate)) {
                endDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(endDate);
                resourceHoursRequest.setEndDate(endDate);
            }
            resourceHoursRequest.setStartDate(startDate);

            // checking resource calendar, if found, booked appointment
            // then return customer list and show the displacement page.
            boolean isUpdate = true;
            convert12To24hrs(resourceHoursRequest);
            String resourceIdStr = resourceIds.stream().map(Object::toString)
                    .collect(Collectors.joining(", "));
            String displacementQuery = prepareDynamicDisplacedApptsQuery(resourceHoursRequest, resourceIdStr);
            System.out.println("displacementQuery::" + displacementQuery.toString());


            List<Map<String, Object>> bookedApptsList = adminDAO.fetchBookedAppointments(jdbcCustomTemplate, displacementQuery.toString());

            if (resourceHoursRequest.isContinueUpdate() == false) {
                if (bookedApptsList != null && bookedApptsList.size() > 0) {
                    isUpdate = false;
                    resourceWorkingHrsResponse.setAlreadyAppointBooked(true);
                    List<CustomerBean> displacedCustomers = prepareDisplaceCustomerList(jdbcCustomTemplate, clientCode, bookedApptsList);
                    logger.debug("DisplacedCustomers: " + displacedCustomers);
                    resourceWorkingHrsResponse.setDisplacedCustomers(displacedCustomers);
                }
            } else {
                logger.info("cancel the displacement appointments");
                if (bookedApptsList != null && bookedApptsList.size() > 0) {
                    Map<String, Object> disArray = bookedApptsList.get(0);
                    if (disArray.size() > 0) {
                        resourceWorkingHrsResponse.setClientCode(resourceHoursRequest.getClientCode());
                        resourceWorkingHrsResponse.setUserName(resourceHoursRequest.getUserName());
                        resourceWorkingHrsResponse.setDisplayNotifyCheckBox(resourceHoursRequest.getNotifyCheckBox());
                        cancelDisplacedAppointmentsNew(jdbcCustomTemplate, resourceWorkingHrsResponse, bookedApptsList);
                    } else {
                        logger.info("There is no displacement appointments!!");
                    }
                }
            }

            if (isUpdate) {
                /**
                 * else check continue flag, if true - check resourceTimeOff and
                 * update schedule_id=0 in resource calendar based on the
                 * minDateTime and maxDateTime
                 */
                updateResourceHistory(jdbcCustomTemplate, resourceHoursRequest, resourceIds, minDateTime, maxDateTime);
                String updateQueryClose = updateResourceCalendarClose(resourceHoursRequest, resourceIdStr);
                System.out.println("updateQueryClose:" + updateQueryClose);
                int countOne = adminDAO.updateResourceCalendarClose(jdbcCustomTemplate, updateQueryClose);

                String updateQueryOpen = updateResourceCalendarOpen(resourceHoursRequest, resourceIdStr);
                System.out.println("updateQueryOpen:" + updateQueryOpen);
                int countTwo = 0;
                // we were not suppose to open up resource calendar if all days
                // closed radio button selected.
                // - gracefully stop executing the updateResponseOpen query.
                if ("".equals(updateQueryOpen) == false) {
                    countTwo = adminDAO.updateResourceCalendarOpen(jdbcCustomTemplate, updateQueryOpen);
                }
                if (countOne > 0 || countTwo > 0) {
                    resourceWorkingHrsResponse.setUpdateSucessfully(true);
                } else {
                    resourceWorkingHrsResponse.setUpdateSucessfully(false);
                }
            }

        } catch (Exception e) {
            logger.error("Error:" + e, e);

            String clientCode = resourceHoursRequest.getClientCode();
            String subject = "Error in RESTws for Client - " + clientCode;
            StringBuilder errorMsg = new StringBuilder();
//			errorMsg.append("MethodName:" + CoreUtils.removeErrorNumber(CoreUtils.getMethodName(1)));
            errorMsg.append("<br/>");
//			errorMsg.append(CoreUtils.getJSONString(resourceHoursRequest));
            errorMsg.append("<br/> Caused By:" + e.getMessage() + "<br/>");
//			sendErrorEmail(errorMsg, e, subject);

            throw new TelAppointException("1052", "Error while - Edit Hours - Date Range.", HttpStatus.BAD_REQUEST, errorMsg);
        }
        return new ResponseEntity<>(commonComponent.populateRMDData(resourceWorkingHrsResponse), HttpStatus.OK);

    }

    private String updateResourceCalendarOpen(ResourceHoursRequest editResourceReq, String resourceIds) throws Exception {
        String startDate = editResourceReq.getStartDate();
        String endDate = editResourceReq.getEndDate();

        if ("Forever".equals(editResourceReq.getEndDateType())) {
            endDate = null;
        }

        String isSunOpen = (String) CoreUtils.getPropertyValue(editResourceReq, "is_sun_open");
        String isMonOpen = (String) CoreUtils.getPropertyValue(editResourceReq, "is_mon_open");
        String isTueOpen = (String) CoreUtils.getPropertyValue(editResourceReq, "is_tue_open");
        String isWedOpen = (String) CoreUtils.getPropertyValue(editResourceReq, "is_wed_open");
        String isThuOpen = (String) CoreUtils.getPropertyValue(editResourceReq, "is_thu_open");
        String isFriOpen = (String) CoreUtils.getPropertyValue(editResourceReq, "is_fri_open");
        String isSatOpen = (String) CoreUtils.getPropertyValue(editResourceReq, "is_sat_open");

        boolean isTrueSunMon = "Y".equalsIgnoreCase(isSunOpen) || "Y".equalsIgnoreCase(isMonOpen) ? true : false;
        boolean isTrueTueWed = "Y".equalsIgnoreCase(isTueOpen) || "Y".equalsIgnoreCase(isWedOpen) ? true : false;
        boolean isTrueThuFri = "Y".equalsIgnoreCase(isThuOpen) || "Y".equalsIgnoreCase(isFriOpen) ? true : false;
        boolean isTrueSat = "Y".equalsIgnoreCase(isSatOpen);
        boolean isTrue = (isTrueSunMon || isTrueTueWed || isTrueThuFri || isTrueSat) ? true : false;

        StringBuilder sql = new StringBuilder(" update resource_calendar set schedule_id = 0 where resource_id in (" + resourceIds + ") and DATE(date_time) >= '" + startDate + "'")
                .append(endDate != null ? " and DATE(date_time) <= '" + endDate + "'" : "").append(" and schedule_id = -1 ");

        if (isTrue) {
            sql.append("and ( ");
        } else {
            // we were not suppose to open up resource calendar - gracefully
            // stop executing the updateResponseOpen query.
            return "";
        }
        appenedQueryTwo(startDate, 0, "sun", editResourceReq, sql);
        appenedQueryTwo(startDate, 1, "mon", editResourceReq, sql);
        appenedQueryTwo(startDate, 2, "tue", editResourceReq, sql);
        appenedQueryTwo(startDate, 3, "wed", editResourceReq, sql);
        appenedQueryTwo(startDate, 4, "thu", editResourceReq, sql);
        appenedQueryTwo(startDate, 5, "fri", editResourceReq, sql);
        appenedQueryTwo(startDate, 6, "sat", editResourceReq, sql);
        String finalSQL = sql.toString();
        if (isTrue) {
            finalSQL = finalSQL.substring(0, sql.length() - 3) + ")";
        }
        return finalSQL;
    }

    public void appenedQueryTwo(String startDate, Integer intDay, String day, ResourceHoursRequest editResourceReq, StringBuilder sql) throws Exception {
        String defaultStartTime = "00:00:00";
        String defaultEndTime = "23:59:59";
        String dayStartTime = null;
        String dayEndTime = null;
        String breakStartTime = null;
        String breakEndTime = null;

        Integer dayBreakTime1Mins = (Integer) CoreUtils.getPropertyValue(editResourceReq, day + "_break_time_1_mins");
        String isdayOpen = (String) CoreUtils.getPropertyValue(editResourceReq, "is_" + day + "_open");

        Object noBreakTimeObj = (String) CoreUtils.getPropertyValue(editResourceReq, "is_" + day + "_no_break_time");
        String noBreakTime = "N";
        if (noBreakTimeObj != null) {
            noBreakTime = (String) CoreUtils.getPropertyValue(editResourceReq, "is_" + day + "_no_break_time");
        }

        if ("Y".equalsIgnoreCase(isdayOpen)) {
            dayStartTime = (String) CoreUtils.getPropertyValue(editResourceReq, day + "_start_time");
            dayEndTime = (String) CoreUtils.getPropertyValue(editResourceReq, day + "_end_time");
            if ("N".equalsIgnoreCase(noBreakTime)) {
                Object breakTime1 = CoreUtils.getPropertyValue(editResourceReq, day + "_break_time_1");
                if (breakTime1 != null) {
                    breakStartTime = (String) breakTime1;
                    if (dayBreakTime1Mins == null)
                        dayBreakTime1Mins = 0;
                    breakEndTime = getEndTimeByMins(breakStartTime, dayBreakTime1Mins);
                } else {
                    logger.error("break time should not be null");
                    breakEndTime = null;
                }
            } else {
                breakStartTime = null;
                breakEndTime = null;
            }
        } else {
            dayStartTime = defaultStartTime;
            dayEndTime = defaultEndTime;
        }

        if ("Y".equals(isdayOpen)) {
            sql.append(" (DATE_FORMAT(date_time, '%w') = ").append(intDay);
            sql.append(" and ((TIME(date_time) >= '").append(dayStartTime).append("'");
            if (breakStartTime != null) {
                sql.append(" and TIME(date_time) <").append("'" + breakStartTime + "')");
                sql.append(" or (TIME(date_time) >=").append("'" + breakEndTime + "'");
            }
            sql.append(" and TIME(date_time) < '").append(dayEndTime).append("')");
            sql.append(")");
            sql.append(")");
            sql.append(" or ");
        }
    }

    private String getEndTimeByMins(String timestr, int timeSlotInMins) {
        timestr = timestr.substring(0, 5);
        String minutes = timestr.substring(timestr.indexOf(':') + 1).trim();
        String hours = timestr.substring(0, 2).trim();
        int timeInMins = Integer.valueOf(hours) * 60 + Integer.valueOf(minutes);
        timeInMins = timeInMins + timeSlotInMins;

        String hrs = String.valueOf(timeInMins / 60);
        String mins = String.valueOf(timeInMins % 60);
        hrs = hrs.length() == 0 ? "00" : hrs.length() == 1 ? "0" + hrs : hrs;
        mins = mins.length() == 0 ? "00" : mins.length() == 1 ? "0" + mins : mins;
        String key = hrs + ":" + mins + ":00";
        return key;
    }

    private String updateResourceCalendarClose(ResourceHoursRequest editResourceReq, String resourceIds) throws Exception {
        String startDate = editResourceReq.getStartDate();
        String endDate = editResourceReq.getEndDate();

        if ("Forever".equals(editResourceReq.getEndDateType())) {
            endDate = null;
        }
        StringBuilder sql = new StringBuilder(" update resource_calendar set schedule_id = -1 where resource_id in (" + resourceIds + ") and DATE(date_time) >= '" + startDate
                + "'").append(endDate != null ? " and DATE(date_time) <= '" + endDate + "'" : "").append(" and schedule_id = 0 and ");
        sql.append(" ( ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 0, "sun", editResourceReq, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 1, "mon", editResourceReq, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 2, "tue", editResourceReq, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 3, "wed", editResourceReq, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 4, "thu", editResourceReq, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 5, "fri", editResourceReq, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 6, "sat", editResourceReq, sql);
        sql.append(" ) ");
        sql.append(" ) ");

        return sql.toString();
    }


    private void updateResourceHistory(JdbcCustomTemplate jdbcCustomTemplate, ResourceHoursRequest resourceHoursRequest,
                                       Set<Integer> resourceIds, Timestamp fromTimestamp, Timestamp toTimestamp) {
        try {
            Integer locationId = resourceHoursRequest.getLocation_id();
            if (locationId != null && locationId != 0) {
                if (resourceIds != null && resourceIds.size() > 0) {
                    Location location = adminDAO.getLocationById(jdbcCustomTemplate, FilterKeyWordContants.LOCATIONS_HOME_PAGE_DATA.getFilterKey(), locationId);
                    for (Integer resourceId : resourceIds) {
                        Resource resource = adminDAO.getResourceById(jdbcCustomTemplate, resourceId, true);
                        Date endDate = new Date(toTimestamp.getTime());

                        Calendar startCal = new GregorianCalendar();
                        Date stDate = new Date(fromTimestamp.getTime());
                        startCal.setTime(stDate);

                        Calendar endCal = new GregorianCalendar();
                        logger.info("end time:" + endCal.getTime());
                        endCal.setTime(endDate);

                        // forever date calculation
                        Calendar forEverEndDate = new GregorianCalendar();
                        logger.info("end time:" + endCal.getTime());
                        forEverEndDate.setTime(endDate);

                        if ("Forever".equals(resourceHoursRequest.getEndDateType())) {
                            forEverEndDate.add(Calendar.YEAR, 40);
                            forEverEndDate.set(Calendar.DATE, forEverEndDate.getActualMaximum(Calendar.DATE));
                        }
                        String endDateStr = DateUtils.formatGCDateToYYYYMMDD(forEverEndDate);

                        String effectiveDate = fromTimestamp.toString().substring(0, 10);
                        logger.info("start time:" + new Date(fromTimestamp.getTime()));
                        logger.info("end time:" + new Date(toTimestamp.getTime()));
                        logger.info("resourceId:" + resourceId);

                        ResourceWorkingHrsHistory resourceWorkingHrsHistory = getResourceWorkingHrsHistory(resourceHoursRequest, resource, location, effectiveDate, endDateStr);
                        resourceWorkingHrsHistory.setTimestamp(new Timestamp(System.currentTimeMillis()));
                        adminDAO.addResourceWorkingHrsHistory(jdbcCustomTemplate, resourceHoursRequest, resourceWorkingHrsHistory);
                        logger.info("start time:" + startCal.getTime());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error:" + e, e);
            String clientCode = resourceHoursRequest.getClientCode();
            String subject = "Error in RESTws for Client - " + clientCode;
            StringBuilder errorMsg = new StringBuilder();
//			errorMsg.append("MethodName:" + CoreUtils.removeErrorNumber(CoreUtils.getMethodName(1)));
            errorMsg.append("<br/>");
//			errorMsg.append(CoreUtils.getJSONString(editResourceReq));
            errorMsg.append("<br/> Caused By:" + e.getMessage() + "<br/>");
//			sendErrorEmail(errorMsg, e, subject);

        }
    }

    private ResourceWorkingHrsHistory getResourceWorkingHrsHistory(ResourceHoursRequest resourceHoursRequest, Resource resource,
                                                                   Location location, String effectiveDate, String endDateStr) {

        ResourceWorkingHrsHistory resWorkingHrsHistory = new ResourceWorkingHrsHistory();

        if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_sun_open())) {
            resWorkingHrsHistory.setSun_start_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getSun_start_time()).toString());
            resWorkingHrsHistory.setSun_end_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getSun_end_time()).toString());
            if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_sun_no_break_time()) == false) {
                resWorkingHrsHistory.setSun_break_time_1(DateUtils.prepareTimeFormString(resourceHoursRequest.getSun_break_time_1()).toString());
            }
        }
        resWorkingHrsHistory.setSun_break_time_1_mins(resourceHoursRequest.getSun_break_time_1_mins());

        if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_mon_open())) {
            resWorkingHrsHistory.setMon_start_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getMon_start_time()).toString());
            resWorkingHrsHistory.setMon_end_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getMon_end_time()).toString());
            if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_mon_no_break_time()) == false) {
                resWorkingHrsHistory.setMon_break_time_1(DateUtils.prepareTimeFormString(resourceHoursRequest.getMon_break_time_1()).toString());
            }
        }
        resWorkingHrsHistory.setMon_break_time_1_mins(resourceHoursRequest.getMon_break_time_1_mins());

        if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_tue_open())) {
            resWorkingHrsHistory.setTue_start_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getTue_start_time()).toString());
            resWorkingHrsHistory.setTue_end_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getTue_end_time()).toString());

            if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_tue_no_break_time()) == false) {
                resWorkingHrsHistory.setTue_break_time_1(DateUtils.prepareTimeFormString(resourceHoursRequest.getTue_break_time_1()).toString());
            }
        }
        resWorkingHrsHistory.setTue_break_time_1_mins(resourceHoursRequest.getTue_break_time_1_mins());

        if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_wed_open())) {
            resWorkingHrsHistory.setWed_start_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getWed_start_time()).toString());
            resWorkingHrsHistory.setWed_end_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getWed_end_time()).toString());
            if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_wed_no_break_time()) == false) {
                resWorkingHrsHistory.setWed_break_time_1(DateUtils.prepareTimeFormString(resourceHoursRequest.getWed_break_time_1()).toString());
            }
        }
        resWorkingHrsHistory.setWed_break_time_1_mins(resourceHoursRequest.getWed_break_time_1_mins());

        if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_thu_open())) {
            resWorkingHrsHistory.setThu_start_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getThu_start_time()).toString());
            resWorkingHrsHistory.setThu_end_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getThu_end_time()).toString());
            if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_thu_no_break_time()) == false) {
                resWorkingHrsHistory.setThu_break_time_1(DateUtils.prepareTimeFormString(resourceHoursRequest.getThu_break_time_1()).toString());
            }
        }
        resWorkingHrsHistory.setThu_break_time_1_mins(resourceHoursRequest.getThu_break_time_1_mins());

        if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_fri_open())) {
            resWorkingHrsHistory.setFri_start_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getFri_start_time()).toString());
            resWorkingHrsHistory.setFri_end_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getFri_end_time()).toString());
            if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_fri_no_break_time()) == false) {
                resWorkingHrsHistory.setFri_break_time_1(DateUtils.prepareTimeFormString(resourceHoursRequest.getFri_break_time_1()).toString());
            }
        }
        resWorkingHrsHistory.setFri_break_time_1_mins(resourceHoursRequest.getFri_break_time_1_mins());

        if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_sat_open())) {
            resWorkingHrsHistory.setSat_start_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getSat_start_time()).toString());
            resWorkingHrsHistory.setSat_end_time(DateUtils.prepareTimeFormString(resourceHoursRequest.getSat_end_time()).toString());
            if ("Y".equalsIgnoreCase(resourceHoursRequest.getIs_sat_no_break_time()) == false) {
                resWorkingHrsHistory.setSat_break_time_1(DateUtils.prepareTimeFormString(resourceHoursRequest.getSat_break_time_1()).toString());
            }
        }
        resWorkingHrsHistory.setSat_break_time_1_mins(resourceHoursRequest.getSat_break_time_1_mins());

        resWorkingHrsHistory.setLocationId(location.getLocationId());
        resWorkingHrsHistory.setResourceId(resource.getResourceId());
        resWorkingHrsHistory.setUsername(resourceHoursRequest.getUserName());
        resWorkingHrsHistory.setEffective_date(DateUtils.getDateObject(effectiveDate).toString());
        resWorkingHrsHistory.setEnd_date(DateUtils.getDateObject(endDateStr).toString());
        return resWorkingHrsHistory;
    }

    public boolean cancelDisplacedAppointmentsNew(JdbcCustomTemplate jdbcCustomTemplate, ResourceWorkingHrsResponse baseApptRequest, List<Map<String, Object>> bookedApptsList) {
        boolean isSuccess = false;
        if (bookedApptsList != null && bookedApptsList.size() > 0) {
            int successedCount = 0;
            try {
                Schedule schedule = null;
                Appointment apptointment = null;
                for (Map<String, Object> objArr : bookedApptsList) {
                    if (objArr != null) {
                        BigInteger bigScheduleId = (BigInteger) objArr.get("id");
                        long scheduleId = bigScheduleId.longValue();
                        schedule = adminDAO.getScheduleById(jdbcCustomTemplate, scheduleId);

                        // updating displaced cancel status
                        // Sending cancel mail
                        baseApptRequest.setDeviceType("online");
                        baseApptRequest.setLangCode("us-en");
                        baseApptRequest.setClientCode(baseApptRequest.getClientCode());
                        baseApptRequest.setScheduleId(schedule.getScheduleId());
                        baseApptRequest.setUserName(baseApptRequest.getUserName());
                        baseApptRequest.setDisplayNotifyCheckBox(baseApptRequest.getDisplayNotifyCheckBox());

                        boolean isCancelled = commonComponent.updateCancelApptStatus(jdbcCustomTemplate, schedule, baseApptRequest, AppointmentStatus.DISPLACEMENT_CANCEL.getStatus());
                        if (isCancelled) {
                            successedCount++;
                            try {
                                apptointment = adminDAO.getAppointmentByScheduleId(jdbcCustomTemplate, schedule.getTransId(), schedule.getScheduleId());

                                //TODO: check - before this. make sure that there should be row in campaign table with id = 9
                                if ("Y".equals(baseApptRequest.getDisplayNotifyCheckBox())) {
                                    Boolean isCampaignExists = adminDAO.getCampaign(jdbcCustomTemplate, 8);
                                    if (isCampaignExists != null) {
                                        Map<String, Object> apptSysConfigMap = adminDAO.getApptSysConfig(jdbcCustomTemplate);

                                        addNotifyAppointmentDetails(jdbcCustomTemplate, apptointment, 8, apptSysConfigMap, baseApptRequest.getClientCode(), schedule);
                                    } else {
                                        logger.warn("Since campaign Id 8 is not available so ignoring to insert into notify table.");
                                    }
                                }

                                // Added to update Outlook_google_sync
                                apptointment.setOutlook_google_sync("N");
                                ClientDeploymentConfig clientDeploymentConfigTO = cacheComponent.getClientDeploymentConfig(baseApptRequest.getClientCode(), true);

                                adminDAO.updateAppointment(jdbcCustomTemplate, apptointment);
//								String time = clientComponent.getApptTime(jdbcCustomTemplate,apptointment,clientDeploymentConfigTO.getTimeFormat(),null);
//								mailComponent.sendCancelAppointmentDetailsMail(jdbcCustomTemplate, baseApptRequest, apptointment,
//										EmailContants.ONLINE_EMAIL_APPT_DISPLACED_SUBJECT.getValue(),
//										EmailContants.ONLINE_EMAIL_APPT_DISPLACED_BODY.getValue(),clientComponent.getApptSysConfiguration(entityManager),time);
                            } catch (Exception e) {
//								logger.error(
//										"Error in while sending displaced appointments mail : " + (apptointment != null ? " to - " + customer.getEmail() : ""), e);
                            }
                        }
                    }
                }
                if (successedCount == bookedApptsList.size()) {
                    isSuccess = true;
                }
                logger.debug("Successfully canceled DisplacedAppointments ");
            } catch (Exception e) {
                logger.error("Error in cancelDisplacedAppointments method : " + e, e);
                isSuccess = false;
            }
        } else {
            isSuccess = true;
        }
        return isSuccess;
    }

    private void addNotifyAppointmentDetails(JdbcCustomTemplate jdbcCustomTemplate, Appointment apptointment,
                                             int campaignId, Map<String, Object> apptSysConfigMap, String clientCode, Schedule schedule) throws Exception {

        Notify notify = new Notify();
        notify.setTimestamp(new Timestamp(new Date().getTime()).toString());
        notify.setCampaignId(campaignId);
        notify.setCall_now('N');
        notify.setEmergency_notify('N');
        notify.setBroadcast_mode('N');
        notify.setNotify_status(1);
        notify.setNotify_email_status(0);
        notify.setNotify_sms_status(0);
        notify.setNotify_phone_status(0);
        notify.setNotify_preference(0);

        notify.setResourceId((Integer) schedule.getResourceId());
        notify.setLocationId(schedule.getLocationId());
        notify.setServiceId(schedule.getServiceId());
        notify.setCustomerId(schedule.getCustomerId().intValue());
        notify.setScheduleId(schedule.getScheduleId().intValue());
        List<Customer> customerList = adminDAO.getCustomersById(jdbcCustomTemplate, schedule.getCustomerId());
        Customer customer = customerList.get(0);
        notify.setFirst_name(customer.getFirstName());
        notify.setMiddle_name(schedule.getMiddleName());
        notify.setLast_name(customer.getLastName());

        notify.setCell_phone(customer.getContactPhone());
        notify.setWork_phone(customer.getContactPhone());

        if (apptSysConfigMap != null && "Y".equals(apptSysConfigMap.get("hide_time_slots"))) {
            ClientDeploymentConfig clientDeploymentConfig = cacheComponent.getClientDeploymentConfig(clientCode, true);
            String apptTime = getApptTime(jdbcCustomTemplate, apptointment, clientDeploymentConfig.getTimeFormat(), apptSysConfigMap,
                    clientCode, schedule);
            if (apptTime != null && apptTime.length() <= 28) {
                notify.setInclude_audio_2(DateUtils.convert24To12HoursFormat(apptTime));
            } else {
                notify.setInclude_audio_2(apptTime);
            }
        }

        notify.setEmail(customer.getEmail());
        Map<String, Object> campaign = adminDAO.getCampaignById(jdbcCustomTemplate, campaignId);
        if (campaign != null && (Character) campaign.get("notify_by_phone") == 'Y') {
            notify.setNotify_by_phone('Y');
            notify.setNotify_by_phone_confirm('Y');
        } else {
            notify.setNotify_by_phone('N');
            notify.setNotify_by_phone_confirm('N');
        }

        if (campaign != null && (Character) campaign.get("notify_by_sms") == 'Y') {
            notify.setNotify_by_sms('Y');
            notify.setNotify_by_sms_confirm('Y');
        } else {
            notify.setNotify_by_sms('N');
            notify.setNotify_by_sms_confirm('N');
        }

        if (campaign != null && (Character) campaign.get("notify_by_email") == 'Y') {
            notify.setNotify_by_email('Y');
            notify.setNotify_by_email_confirm('Y');
        } else {
            notify.setNotify_by_email('N');
            notify.setNotify_by_email_confirm('N');
        }

        notify.setDo_not_notify('N');
        notify.setDelete_flag('N');
        notify.setDue_date_time(schedule.getApptDateTime());
        adminDAO.saveNotify(jdbcCustomTemplate, notify);
        logger.debug("Notification Saved");
    }

    public String getApptTime(JdbcCustomTemplate jdbcCustomTemplate, Appointment appointment,
                              String timeFormat, Map<String, Object> apptSysConfigMap, String clientCode, Schedule schedule) throws Exception {
        String appt_time = "";
        Integer serviceId = schedule.getServiceId();
        ClientDeploymentConfig clientDeploymentConfig = cacheComponent.getClientDeploymentConfig(clientCode, true);

        ServiceVO serviceById = adminDAO.getServiceById(jdbcCustomTemplate, serviceId, clientDeploymentConfig.getBlockTimeInMins(), FilterKeyWordContants.SERVICES_HOME_PAGE_DATA.getFilterKey(), false, true);
        if ("Y".equals(serviceById.getSkipDateTIme())) {
            String time = DateUtils.getSimpleDateFormat(CommonDateContants.TIME_FORMAT_HHMMSS_CAP.getValue()).get().format(schedule.getApptDateTime());
            List<ResourceDisplayTime> resourceDisplayTimes = adminDAO.getResourceDisplayTime(jdbcCustomTemplate, time, schedule.getResourceId());
            if (null != resourceDisplayTimes && resourceDisplayTimes.size() > 0) {
                appt_time = DateUtils.getSimpleDateFormat(timeFormat).get().format(resourceDisplayTimes.get(0).getDisplay_time());
            } else {
                appt_time = DateUtils.getSimpleDateFormat(timeFormat).get().format(schedule.getApptDateTime());
            }
        } else if (apptSysConfigMap != null && "Y".equals(apptSysConfigMap.get("hide_time_slots"))) {
            String time = DateUtils.getSimpleDateFormat(CommonDateContants.TIME_FORMAT_HHMMSS_CAP.getValue()).get().format(schedule.getApptDateTime());

            List<Map<String, Object>> displayTimeInConfirmPage = adminDAO.getDisplayTimeInConfirmPage(jdbcCustomTemplate, time, schedule.getResourceId());
            if (displayTimeInConfirmPage != null && displayTimeInConfirmPage.size() > 0) {
                Map<String, Object> resDisplayTimeObjArray = displayTimeInConfirmPage.get(0);
                String displayTimeRange = resDisplayTimeObjArray.get("display_time_range").toString();
                String displayTime = resDisplayTimeObjArray.get("display_time").toString();
                if (displayTimeRange != null || !"".equals(displayTimeRange)) {
                    appt_time = displayTimeRange;
                } else if (displayTime != null || !"".equals(displayTime)) {
                    appt_time = displayTime;
                } else {
                    appt_time = time;
                }
            } else {
                appt_time = DateUtils.convert24To12HoursFormat(time);
            }
        } else {
            appt_time = DateUtils.getSimpleDateFormat(timeFormat).get().format(schedule.getApptDateTime());
        }
        if (appt_time != null && appt_time.startsWith("0")) {
            appt_time = appt_time.substring(1);
        }
        return appt_time;
    }


    public List<CustomerBean> prepareDisplaceCustomerList(JdbcCustomTemplate jdbcCustomTemplate, String clientCode, List<Map<String, Object>> calDataList) throws Exception {
        List<CustomerBean> displacedCustomers = new ArrayList<CustomerBean>();
        CustomerBean cust = null;
        Set<Long> scheduleIds = new HashSet<Long>();
        if (calDataList != null && !calDataList.isEmpty()) {

            for (Map<String, Object> calData : calDataList) {
                scheduleIds.add((Long) calData.get("schedule_id"));
            }

            if (scheduleIds != null && !scheduleIds.isEmpty()) {
                List<Map<String, Object>> scheduleList = adminDAO.getScheduleList(jdbcCustomTemplate, scheduleIds);
                for (Map<String, Object> schedule : scheduleList) {
                    Long customerId = (Long) schedule.get("customer_id");
                    List<Customer> customerList = adminDAO.getCustomersById(jdbcCustomTemplate, customerId);
                    Customer customer = customerList.get(0);
                    cust = new CustomerBean();
                    cust.setCustomerId(customer.getCustomerId());
                    cust.setPatientFirstName(customer.getFirstName());
                    cust.setPatientLastName(customer.getLastName());

                    Timestamp apptDateTime = (Timestamp) schedule.get("appt_date_time");
                    if (customer.getContactPhone() != null) {
                        cust.setPatientHP(getPhoneNumber(clientCode, customer.getContactPhone()));
                    }
                    if (customer.getContactPhone() != null) {
                        cust.setPatientCP(getPhoneNumber(clientCode, customer.getContactPhone()));
                    }
                    if (customer.getContactPhone() != null) {
                        cust.setPatientConP(getPhoneNumber(clientCode, customer.getContactPhone()));
                    }

                    if (customer.getEmail() != null) {
                        cust.setPatientEmail(customer.getEmail());
                    }
                    if (apptDateTime != null) {
                        cust.setApptDateTime(DateUtils.getStringFromDate(apptDateTime, CommonDateContants.DATETIME_FORMAT_YYYYMMDDHHMMSS_TWELWE_HOURS.getValue()));// appt_date_time
                    }
                    displacedCustomers.add(cust);
                }
            }
        }

        return displacedCustomers;
    }

    public String getPhoneNumber(String clientCode, String phoneNumber) {
        try {
            String phoneFormat = getPhoneFormat(clientCode);
            return CoreUtils.getFormatedPhoneNumber(phoneNumber, phoneFormat);
        } catch (Exception e) {
            logger.error("Error:" + e, e);
        }
        return "";
    }

    public String getPhoneFormat(String clientCode) throws Exception {
        ClientDeploymentConfig clientDeploymentConfigTO = cacheComponent.getClientDeploymentConfig(clientCode, true);
        return clientDeploymentConfigTO.getPhoneFormat();
    }

    private String prepareDynamicDisplacedApptsQuery(ResourceHoursRequest resourceHoursRequest, String resourceIds) throws Exception {
        String startDate = resourceHoursRequest.getStartDate();
        String endDate = resourceHoursRequest.getEndDate();
        if ("Forever".equals(resourceHoursRequest.getEndDateType())) {
            endDate = null;
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct schedule_id,0 from resource_calendar where resource_id in (" + resourceIds + ") and DATE(date_time) >= '" + startDate + "'")
                .append(endDate != null ? " and DATE(date_time) <= '" + endDate + "'" : "").append(" and schedule_id > 0 and ");
        sql.append(" ( ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 0, "sun", resourceHoursRequest, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 1, "mon", resourceHoursRequest, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 2, "tue", resourceHoursRequest, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 3, "wed", resourceHoursRequest, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 4, "thu", resourceHoursRequest, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 5, "fri", resourceHoursRequest, sql);
        sql.append(" ) ");
        sql.append(" or ");
        sql.append(" ( ");
        appenedQueryOne(startDate, 6, "sat", resourceHoursRequest, sql);
        sql.append(" ) ");
        sql.append(" ) ");
        return sql.toString();
    }

    public void appenedQueryOne(String startDate, Integer intDay, String day, ResourceHoursRequest resourceHoursRequest, StringBuilder sql) throws Exception {
        String defaultStartTime = "00:00:00";
        String defaultEndTime = "23:59:59";
        String dayStartTime = null;
        String dayEndTime = null;
        String breakStartTime = null;
        String breakEndTime = null;

        Integer blockTimeInMins = cacheComponent.getClientDeploymentConfig(resourceHoursRequest.getClientCode(), true).getBlockTimeInMins();

        Integer dayBreakTime1Mins = (Integer) CoreUtils.getPropertyValue(resourceHoursRequest, day + "_break_time_1_mins");
        String isdayOpen = (String) CoreUtils.getPropertyValue(resourceHoursRequest, "is_" + day + "_open");

        Object noBreakTimeObj = CoreUtils.getPropertyValue(resourceHoursRequest, "is_" + day + "_no_break_time");
        String noBreakTime = "N";
        if (noBreakTimeObj != null) {
            noBreakTime = (String) CoreUtils.getPropertyValue(resourceHoursRequest, "is_" + day + "_no_break_time");
        }

        if ("Y".equalsIgnoreCase(isdayOpen)) {
            dayStartTime = (String) CoreUtils.getPropertyValue(resourceHoursRequest, day + "_start_time");
            dayEndTime = (String) CoreUtils.getPropertyValue(resourceHoursRequest, day + "_end_time");
            if ("N".equalsIgnoreCase(noBreakTime)) {
                Object breakTime1 = CoreUtils.getPropertyValue(resourceHoursRequest, day + "_break_time_1");
                if (breakTime1 != null) {
                    breakStartTime = (String) breakTime1;
                    if (dayBreakTime1Mins == null)
                        dayBreakTime1Mins = 0;
                    breakEndTime = getEndTimeByMinsForDisplacement(breakStartTime, dayBreakTime1Mins, blockTimeInMins);
                } else {
                    logger.error("break time should not be null");
                    breakEndTime = null;
                }
            } else {
                breakStartTime = null;
                breakEndTime = null;
            }
        } else {
            dayStartTime = defaultStartTime;
            dayEndTime = defaultEndTime;
        }
        if ("N".equalsIgnoreCase(isdayOpen)) {
            sql.append(" DATE_FORMAT(date_time, '%w') = ").append(intDay);
        } else {
            sql.append(" DATE_FORMAT(date_time, '%w') = ").append(intDay);
            sql.append(" and (TIME(date_time) < '").append(dayStartTime).append("'");
            sql.append(breakStartTime != null ? " or (TIME(date_time) between '" + breakStartTime + "' and '" + breakEndTime + "')" : "").append(" or TIME(date_time) >= '");
            sql.append(dayEndTime).append("')");
        }
    }

    private void convert12To24hrs(ResourceHoursRequest baseResourceReq) throws Exception {

        if ("Y".equalsIgnoreCase(baseResourceReq.getIs_sun_open())) {
            String sunDayStartTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getSun_start_time());
            String sunDayEndTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getSun_end_time());
            if (baseResourceReq.getSun_break_time_1() != null) {
                String sunDayBreakTime1 = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getSun_break_time_1());
                baseResourceReq.setSun_break_time_1(sunDayBreakTime1);
            }
            baseResourceReq.setSun_start_time(sunDayStartTime);
            baseResourceReq.setSun_end_time(sunDayEndTime);
        }

        if ("Y".equalsIgnoreCase(baseResourceReq.getIs_mon_open())) {
            String monDayStartTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getMon_start_time());
            String monDayEndTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getMon_end_time());
            if (baseResourceReq.getMon_break_time_1() != null) {
                String monDayBreakTime1 = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getMon_break_time_1());
                baseResourceReq.setMon_break_time_1(monDayBreakTime1);
            }
            baseResourceReq.setMon_start_time(monDayStartTime);
            baseResourceReq.setMon_end_time(monDayEndTime);
        }

        if ("Y".equalsIgnoreCase(baseResourceReq.getIs_tue_open())) {
            String tueDayStartTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getTue_start_time());
            String tueDayEndTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getTue_end_time());

            if (baseResourceReq.getTue_break_time_1() != null) {
                String tueDayBreakTime1 = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getTue_break_time_1());
                baseResourceReq.setTue_break_time_1(tueDayBreakTime1);
            }
            baseResourceReq.setTue_start_time(tueDayStartTime);
            baseResourceReq.setTue_end_time(tueDayEndTime);
        }

        if ("Y".equalsIgnoreCase(baseResourceReq.getIs_wed_open())) {
            String wedDayStartTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getWed_start_time());
            String wedDayEndTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getWed_end_time());
            if (baseResourceReq.getWed_break_time_1() != null) {
                String wedDayBreakTime1 = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getWed_break_time_1());
                baseResourceReq.setWed_break_time_1(wedDayBreakTime1);
            }
            baseResourceReq.setWed_start_time(wedDayStartTime);
            baseResourceReq.setWed_end_time(wedDayEndTime);
        }

        if ("Y".equalsIgnoreCase(baseResourceReq.getIs_thu_open())) {
            String thuDayStartTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getThu_start_time());
            String thuDayEndTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getThu_end_time());
            if (baseResourceReq.getThu_break_time_1() != null) {
                String thuDayBreakTime1 = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getThu_break_time_1());
                baseResourceReq.setThu_break_time_1(thuDayBreakTime1);
            }
            baseResourceReq.setThu_start_time(thuDayStartTime);
            baseResourceReq.setThu_end_time(thuDayEndTime);
        }

        if ("Y".equalsIgnoreCase(baseResourceReq.getIs_fri_open())) {
            String friDayStartTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getFri_start_time());
            String friDayEndTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getFri_end_time());

            if (baseResourceReq.getFri_break_time_1() != null) {
                String friDayBreakTime1 = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getFri_break_time_1());
                baseResourceReq.setFri_break_time_1(friDayBreakTime1);
            }
            baseResourceReq.setFri_start_time(friDayStartTime);
            baseResourceReq.setFri_end_time(friDayEndTime);
        }

        if ("Y".equalsIgnoreCase(baseResourceReq.getIs_sat_open())) {
            String satDayStartTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getSat_start_time());
            String satDayEndTime = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getSat_end_time());
            if (baseResourceReq.getSat_break_time_1() != null) {
                String satDayBreakTime1 = DateUtils.convert12To24HoursHHMMSSFormat(baseResourceReq.getSat_break_time_1());
                baseResourceReq.setSat_break_time_1(satDayBreakTime1);
            }
            baseResourceReq.setSat_start_time(satDayStartTime);
            baseResourceReq.setSat_end_time(satDayEndTime);
        }
    }

    private String getEndTimeByMinsForDisplacement(String timestr, int timeSlotInMins, int blockTimeInMins) {
        timestr = timestr.substring(0, 5);
        String minutes = timestr.substring(timestr.indexOf(':') + 1).trim();
        String hours = timestr.substring(0, 2).trim();
        int timeInMins = Integer.valueOf(hours) * 60 + Integer.valueOf(minutes);
        timeInMins = timeInMins + timeSlotInMins - blockTimeInMins;

        String hrs = String.valueOf(timeInMins / 60);
        String mins = String.valueOf(timeInMins % 60);
        hrs = hrs.length() == 0 ? "00" : hrs.length() == 1 ? "0" + hrs : hrs;
        mins = mins.length() == 0 ? "00" : mins.length() == 1 ? "0" + mins : mins;
        String key = hrs + ":" + mins + ":00";
        return key;
    }

    private ResourceWorkingHrs prepareDefaultSuggestedResourceWorkingHours(Logger logger, ResourceWorkingHrs resourceWorkingHrs, JdbcCustomTemplate jdbcCustomTemplate) {
        Map<String, Object> apptSysConfigMap = adminDAO.getApptSysConfigDefaultResourceWorkingHours(jdbcCustomTemplate);

        resourceWorkingHrs.setDefault_is_mon_open(apptSysConfigMap.get("default_is_mon_open").toString());
        resourceWorkingHrs.setDefault_is_tue_open(apptSysConfigMap.get("default_is_tue_open").toString());
        resourceWorkingHrs.setDefault_is_wed_open(apptSysConfigMap.get("default_is_wed_open").toString());
        resourceWorkingHrs.setDefault_is_thu_open(apptSysConfigMap.get("default_is_thu_open").toString());
        resourceWorkingHrs.setDefault_is_fri_open(apptSysConfigMap.get("default_is_fri_open").toString());
        resourceWorkingHrs.setDefault_is_sat_open(apptSysConfigMap.get("default_is_sat_open").toString());
        resourceWorkingHrs.setDefault_is_sun_open(apptSysConfigMap.get("default_is_sun_open").toString());

        Object default_day_start_time = apptSysConfigMap.get("default_day_start_time");
        Object default_day_end_time = apptSysConfigMap.get("default_day_end_time");
        resourceWorkingHrs.setIs_mon_open(resourceWorkingHrs.getDefault_is_mon_open());

        Object default_break_time_1 = apptSysConfigMap.get("default_break_time_1");

        if ("Y".equals(resourceWorkingHrs.getDefault_is_mon_open())) {

            if (default_break_time_1 != null) {
                resourceWorkingHrs.setMon_break_time_hour((Integer) geHourMinMeridian(default_break_time_1.toString()).get("hour"));
                resourceWorkingHrs.setMon_break_time_min((Integer) geHourMinMeridian(default_break_time_1.toString()).get("min"));
                resourceWorkingHrs.setMon_break_time_meridian((String) geHourMinMeridian(default_break_time_1.toString()).get("meridian"));

                resourceWorkingHrs.setMon_break_time_1_mins((Integer) apptSysConfigMap.get("default_break_time_1_mins"));

                resourceWorkingHrs.setIs_mon_no_break_time("N");
            }
            resourceWorkingHrs.setMon_start_hour((Integer) geHourMinMeridian(default_day_start_time.toString()).get("hour"));
            resourceWorkingHrs.setMon_start_min((Integer) geHourMinMeridian(default_day_start_time.toString()).get("min"));
            resourceWorkingHrs.setMon_start_meridian((String) geHourMinMeridian(default_day_start_time.toString()).get("meridian"));

            resourceWorkingHrs.setMon_end_hour((Integer) geHourMinMeridian(default_day_end_time.toString()).get("hour"));
            resourceWorkingHrs.setMon_end_min((Integer) geHourMinMeridian(default_day_end_time.toString()).get("min"));
            resourceWorkingHrs.setMon_end_meridian((String) geHourMinMeridian(default_day_end_time.toString()).get("meridian"));
        }

        resourceWorkingHrs.setIs_tue_open(resourceWorkingHrs.getDefault_is_tue_open());
        if ("Y".equals(resourceWorkingHrs.getDefault_is_tue_open())) {

            if (default_break_time_1 != null) {
                resourceWorkingHrs.setTue_break_time_hour((Integer) geHourMinMeridian(default_break_time_1.toString()).get("hour"));
                resourceWorkingHrs.setTue_break_time_min((Integer) geHourMinMeridian(default_break_time_1.toString()).get("min"));
                resourceWorkingHrs.setTue_break_time_meridian((String) geHourMinMeridian(default_break_time_1.toString()).get("meridian"));

                resourceWorkingHrs.setTue_break_time_1_mins((Integer) apptSysConfigMap.get("default_break_time_1_mins"));
                resourceWorkingHrs.setIs_tue_no_break_time("N");
            }
            resourceWorkingHrs.setTue_start_hour((Integer) geHourMinMeridian(default_day_start_time.toString()).get("hour"));
            resourceWorkingHrs.setTue_start_min((Integer) geHourMinMeridian(default_day_start_time.toString()).get("min"));
            resourceWorkingHrs.setTue_start_meridian((String) geHourMinMeridian(default_day_start_time.toString()).get("meridian"));

            resourceWorkingHrs.setTue_end_hour((Integer) geHourMinMeridian(default_day_end_time.toString()).get("hour"));
            resourceWorkingHrs.setTue_end_min((Integer) geHourMinMeridian(default_day_end_time.toString()).get("min"));
            resourceWorkingHrs.setTue_end_meridian((String) geHourMinMeridian(default_day_end_time.toString()).get("meridian"));
        }

        resourceWorkingHrs.setIs_wed_open(resourceWorkingHrs.getDefault_is_wed_open());
        if ("Y".equals(resourceWorkingHrs.getDefault_is_wed_open())) {

            if (default_break_time_1 != null) {
                resourceWorkingHrs.setWed_break_time_hour((Integer) geHourMinMeridian(default_break_time_1.toString()).get("hour"));
                resourceWorkingHrs.setWed_break_time_min((Integer) geHourMinMeridian(default_break_time_1.toString()).get("min"));
                resourceWorkingHrs.setWed_break_time_meridian((String) geHourMinMeridian(default_break_time_1.toString()).get("meridian"));

                resourceWorkingHrs.setWed_break_time_1_mins((Integer) apptSysConfigMap.get("default_break_time_1_mins"));
                resourceWorkingHrs.setIs_wed_no_break_time("N");
            }

            resourceWorkingHrs.setWed_start_hour((Integer) geHourMinMeridian(default_day_start_time.toString()).get("hour"));
            resourceWorkingHrs.setWed_start_min((Integer) geHourMinMeridian(default_day_start_time.toString()).get("min"));
            resourceWorkingHrs.setWed_start_meridian((String) geHourMinMeridian(default_day_start_time.toString()).get("meridian"));

            resourceWorkingHrs.setWed_end_hour((Integer) geHourMinMeridian(default_day_end_time.toString()).get("hour"));
            resourceWorkingHrs.setWed_end_min((Integer) geHourMinMeridian(default_day_end_time.toString()).get("min"));
            resourceWorkingHrs.setWed_end_meridian((String) geHourMinMeridian(default_day_end_time.toString()).get("meridian"));
        }

        resourceWorkingHrs.setIs_thu_open(resourceWorkingHrs.getDefault_is_thu_open());
        if ("Y".equals(resourceWorkingHrs.getDefault_is_thu_open())) {
            if (default_break_time_1 != null) {
                resourceWorkingHrs.setThu_break_time_hour((Integer) geHourMinMeridian(default_break_time_1.toString()).get("hour"));
                resourceWorkingHrs.setThu_break_time_min((Integer) geHourMinMeridian(default_break_time_1.toString()).get("min"));
                resourceWorkingHrs.setThu_break_time_meridian((String) geHourMinMeridian(default_break_time_1.toString()).get("meridian"));
                resourceWorkingHrs.setThu_break_time_1_mins((Integer) apptSysConfigMap.get("default_break_time_1_mins"));
                resourceWorkingHrs.setIs_thu_no_break_time("N");

            }
            resourceWorkingHrs.setThu_start_hour((Integer) geHourMinMeridian(default_day_start_time.toString()).get("hour"));
            resourceWorkingHrs.setThu_start_min((Integer) geHourMinMeridian(default_day_start_time.toString()).get("min"));
            resourceWorkingHrs.setThu_start_meridian((String) geHourMinMeridian(default_day_start_time.toString()).get("meridian"));

            resourceWorkingHrs.setThu_end_hour((Integer) geHourMinMeridian(default_day_end_time.toString()).get("hour"));
            resourceWorkingHrs.setThu_end_min((Integer) geHourMinMeridian(default_day_end_time.toString()).get("min"));
            resourceWorkingHrs.setThu_end_meridian((String) geHourMinMeridian(default_day_end_time.toString()).get("meridian"));
        }
        resourceWorkingHrs.setIs_fri_open(resourceWorkingHrs.getDefault_is_fri_open());
        if ("Y".equals(resourceWorkingHrs.getDefault_is_fri_open())) {
            if (default_break_time_1 != null) {
                resourceWorkingHrs.setFri_break_time_hour((Integer) geHourMinMeridian(default_break_time_1.toString()).get("hour"));
                resourceWorkingHrs.setFri_break_time_min((Integer) geHourMinMeridian(default_break_time_1.toString()).get("min"));
                resourceWorkingHrs.setFri_break_time_meridian((String) geHourMinMeridian(default_break_time_1.toString()).get("meridian"));
                resourceWorkingHrs.setFri_break_time_1_mins((Integer) apptSysConfigMap.get("default_break_time_1_mins"));
                resourceWorkingHrs.setIs_fri_no_break_time("N");

            }
            resourceWorkingHrs.setFri_start_hour((Integer) geHourMinMeridian(default_day_start_time.toString()).get("hour"));
            resourceWorkingHrs.setFri_start_min((Integer) geHourMinMeridian(default_day_start_time.toString()).get("min"));
            resourceWorkingHrs.setFri_start_meridian((String) geHourMinMeridian(default_day_start_time.toString()).get("meridian"));

            resourceWorkingHrs.setFri_end_hour((Integer) geHourMinMeridian(default_day_end_time.toString()).get("hour"));
            resourceWorkingHrs.setFri_end_min((Integer) geHourMinMeridian(default_day_end_time.toString()).get("min"));
            resourceWorkingHrs.setFri_end_meridian((String) geHourMinMeridian(default_day_end_time.toString()).get("meridian"));
        }

        resourceWorkingHrs.setIs_sat_open(resourceWorkingHrs.getDefault_is_sat_open());
        if ("Y".equals(resourceWorkingHrs.getDefault_is_sat_open())) {
            if (default_break_time_1 != null) {
                resourceWorkingHrs.setSat_break_time_hour((Integer) geHourMinMeridian(default_break_time_1.toString()).get("hour"));
                resourceWorkingHrs.setSat_break_time_min((Integer) geHourMinMeridian(default_break_time_1.toString()).get("min"));
                resourceWorkingHrs.setSat_break_time_meridian((String) geHourMinMeridian(default_break_time_1.toString()).get("meridian"));
                resourceWorkingHrs.setSat_break_time_1_mins((Integer) apptSysConfigMap.get("default_break_time_1_mins"));
                resourceWorkingHrs.setIs_sat_no_break_time("N");

            }
            resourceWorkingHrs.setSat_start_hour((Integer) geHourMinMeridian(default_day_start_time.toString()).get("hour"));
            resourceWorkingHrs.setSat_start_min((Integer) geHourMinMeridian(default_day_start_time.toString()).get("min"));
            resourceWorkingHrs.setSat_start_meridian((String) geHourMinMeridian(default_day_start_time.toString()).get("meridian"));

            resourceWorkingHrs.setSat_end_hour((Integer) geHourMinMeridian(default_day_end_time.toString()).get("hour"));
            resourceWorkingHrs.setSat_end_min((Integer) geHourMinMeridian(default_day_end_time.toString()).get("min"));
            resourceWorkingHrs.setSat_end_meridian((String) geHourMinMeridian(default_day_end_time.toString()).get("meridian"));
        }

        resourceWorkingHrs.setIs_sun_open(resourceWorkingHrs.getDefault_is_sun_open());
        if ("Y".equals(resourceWorkingHrs.getDefault_is_sun_open())) {
            if (default_break_time_1 != null) {
                resourceWorkingHrs.setSun_break_time_hour((Integer) geHourMinMeridian(default_break_time_1.toString()).get("hour"));
                resourceWorkingHrs.setSun_break_time_min((Integer) geHourMinMeridian(default_break_time_1.toString()).get("min"));
                resourceWorkingHrs.setSun_break_time_meridian((String) geHourMinMeridian(default_break_time_1.toString()).get("meridian"));
                resourceWorkingHrs.setSun_break_time_1_mins((Integer) apptSysConfigMap.get("default_break_time_1_mins"));
                resourceWorkingHrs.setIs_sun_no_break_time("N");
            }
            resourceWorkingHrs.setSun_start_hour((Integer) geHourMinMeridian(default_day_start_time.toString()).get("hour"));
            resourceWorkingHrs.setSun_start_min((Integer) geHourMinMeridian(default_day_start_time.toString()).get("min"));
            resourceWorkingHrs.setSun_start_meridian((String) geHourMinMeridian(default_day_start_time.toString()).get("meridian"));

            resourceWorkingHrs.setSun_end_hour((Integer) geHourMinMeridian(default_day_end_time.toString()).get("hour"));
            resourceWorkingHrs.setSun_end_min((Integer) geHourMinMeridian(default_day_end_time.toString()).get("min"));
            resourceWorkingHrs.setSun_end_meridian((String) geHourMinMeridian(default_day_end_time.toString()).get("meridian"));

        }

        return resourceWorkingHrs;
    }


    private ResourceWorkingHrs prepareSuggestedResourceWorkingHours(
            String resourceIds,
            ResourceWorkingHrs resourceWorkingHrs,
            List<Map<String, Object>> getResourceWorkingHoursList
    ) throws IllegalAccessException, InvocationTargetException {

        if (getResourceWorkingHoursList == null || getResourceWorkingHoursList.isEmpty()) {
            return null;
        }
        outer:
        for (String resourceId : resourceIds.split("\\|")) {
            inner:
            for (Map<String, Object> resourceWorkingHoursMap : getResourceWorkingHoursList) {
                Object resourceIdFromDb = resourceWorkingHoursMap.get("resource_id");
                if (resourceIdFromDb != null && resourceId.equals(resourceIdFromDb.toString())) {
                    resourceWorkingHrs = new ResourceWorkingHrs();
                    BeanUtils.copyProperties(resourceWorkingHrs, resourceWorkingHoursMap);
                    if (resourceWorkingHrs.getMon_start_time() == null
                            || resourceWorkingHrs.getMon_end_time() == null
                            || resourceWorkingHrs.getTue_start_time() == null
                            || resourceWorkingHrs.getTue_end_time() == null
                            || resourceWorkingHrs.getWed_start_time() == null
                            || resourceWorkingHrs.getWed_end_time() == null
                            || resourceWorkingHrs.getThu_start_time() == null
                            || resourceWorkingHrs.getThu_end_time() == null
                            || resourceWorkingHrs.getFri_start_time() == null
                            || resourceWorkingHrs.getFri_end_time() == null) {
                        resourceWorkingHrs = null;
                        continue;
                    }

                    if (resourceWorkingHrs != null) {
                        if (resourceWorkingHoursMap.get("sun_start_time") != null) {
                            resourceWorkingHrs.setIs_sun_open("Y");
                        }

                        if (resourceWorkingHoursMap.get("mon_start_time") != null) {
                            resourceWorkingHrs.setIs_mon_open("Y");
                        }

                        if (resourceWorkingHoursMap.get("tue_start_time") != null) {
                            resourceWorkingHrs.setIs_tue_open("Y");
                        }

                        if (resourceWorkingHoursMap.get("wed_start_time") != null) {
                            resourceWorkingHrs.setIs_wed_open("Y");
                        }

                        if (resourceWorkingHoursMap.get("thu_start_time") != null) {
                            resourceWorkingHrs.setIs_thu_open("Y");
                        }
                        if (resourceWorkingHoursMap.get("fri_start_time") != null) {
                            resourceWorkingHrs.setIs_fri_open("Y");
                        }

                        if (resourceWorkingHoursMap.get("sat_start_time") != null) {
                            resourceWorkingHrs.setIs_sat_open("Y");
                        }
                        break inner;
                    }
                }
            }
            if (resourceWorkingHrs != null) {
                break outer;
            }
        }
        return resourceWorkingHrs;
    }


    @Override
    public ResponseEntity<ResponseModel> getAvailableDates(String clientCode, String device, Long locationId, Long departmentId, String resourceIds, String serviceIds)
            throws Exception {
        AvailableDateTimes availableDates = new AvailableDateTimes();
        Client client = cacheComponent.getClient(clientCode, true);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<String> availableDatesList = new ArrayList<>();
        String resourceId[] = resourceIds.split(",");
        String serviceId[] = serviceIds.split(",");
        if (resourceId.length == serviceId.length) {
            for (int i = 0; i < resourceId.length; i++) {
                availableDates = adminDAO.getAvailableDates(jdbcCustomTemplate, cdConfig.getTimeZone(), locationId, departmentId, Long.valueOf(resourceId[0]), Long.valueOf(serviceId[0]), Long.valueOf(cdConfig.getBlockTimeInMins()));
                String dates = availableDates.getAvailableDates();
                if (dates != null && !"".equals(dates)) {
                    availableDatesList.addAll(Arrays.asList(dates.split(",")));
                }
            }
        }
        Set<String> uniqueDates = availableDatesList.stream().collect(Collectors.toSet());
        availableDates.setAvailableDatesArray(Lists.newArrayList(uniqueDates));
        return new ResponseEntity<>(commonComponent.populateRMDData(availableDates), HttpStatus.OK);
    }


    private List<String> getAvailableDates(JdbcCustomTemplate jdbcCustomTemplate, Long locationId, Long departmentId, String resourceIds, String serviceIds)
            throws Exception {
        AvailableDateTimes availableDates = new AvailableDateTimes();
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(jdbcCustomTemplate.getClientCode(), true);
        List<String> availableDatesList = new ArrayList<>();
        String resourceId[] = resourceIds.split(",");
        String serviceId[] = serviceIds.split(",");
        if (resourceId.length == serviceId.length) {
            for (int i = 0; i < resourceId.length; i++) {
                availableDates = adminDAO.getAvailableDates(jdbcCustomTemplate, cdConfig.getTimeZone(), locationId, departmentId, Long.valueOf(resourceId[0]), Long.valueOf(serviceId[0]), Long.valueOf(cdConfig.getBlockTimeInMins()));
                String dates = availableDates.getAvailableDates();
                if (dates != null && !"".equals(dates)) {
                    availableDatesList.addAll(Arrays.asList(dates.split(",")));
                }
            }
        }
        Set<String> uniqueDates = availableDatesList.stream().collect(Collectors.toSet());
        return Lists.newArrayList(uniqueDates);
    }

    @Override
    public ResponseEntity<ResponseModel> releaseHoldAppointment(String clientCode, String device, Long scheduleId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        adminDAO.releaseHoldAppointment(jdbcCustomTemplate, logger, device, scheduleId);
        return new ResponseEntity<>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    public void addResourceCalendarData(JdbcCustomTemplate jdbcCustomTemplate, Integer resourceId, ClientDeploymentConfig cdConfig) throws Exception {
        Calendar calendar = new GregorianCalendar();
        Object maxDateTime = adminDAO.getMaxDateTime(jdbcCustomTemplate);
        Calendar endCal = null;
        if (maxDateTime != null) {
            Timestamp maxTimestamp = (Timestamp) maxDateTime;
            endCal = DateUtils.convertTOCalendar(maxTimestamp);
        } else {
            endCal = DateUtils.addMonthsAndGetCalendar(cdConfig.getResourceCalendarMonths());
        }

        Calendar currentDate = calendar;
        Calendar startCal = calendar;
        int firstDayOfWeek = currentDate.getFirstDayOfWeek();
        int days = (startCal.get(Calendar.DAY_OF_WEEK) + 7 - firstDayOfWeek) % 7;
        startCal.add(Calendar.DATE, -days);

        int endDayOfWeek = endCal.get(Calendar.DAY_OF_WEEK);
        endCal.add(Calendar.DATE, 7 - (endDayOfWeek - 1));
        String date = null;
        int batchSize = 900 * 5;
        List<SqlParameterSource> sqlParamList = new ArrayList<>();
        while (startCal.before(endCal)) {
            date = DateUtils.formatGCDateToYYYYMMDD(startCal);
            populateSQLParameterSource(date, cdConfig, resourceId, sqlParamList);
            if (sqlParamList.size() >= batchSize) {
                adminDAO.insertResourceCalendar(jdbcCustomTemplate, sqlParamList);
                sqlParamList.clear();
            }
            startCal = DateUtils.addDaysAndGetCalendar(startCal, 1);
        }
        if (sqlParamList.size() > 0) {
            adminDAO.insertResourceCalendar(jdbcCustomTemplate, sqlParamList);
            sqlParamList.clear();
        }
    }


    public void populateSQLParameterSource(String date, ClientDeploymentConfig cdConfig, Integer resourceId, List<SqlParameterSource> sqlParameterSource) {
        String startDayTime = cdConfig.getDayStartTime();
        String endDayTime = cdConfig.getDayEndTime();
        Timestamp startDayTimeStamp = DateUtils.getTimestampFromString(date + " " + startDayTime);
        Timestamp endDayTimeStamp = DateUtils.getTimestampFromString(date + " " + endDayTime);
        MapSqlParameterSource paramSource = null;
        while (startDayTimeStamp.before(endDayTimeStamp)) {
            paramSource = new MapSqlParameterSource();
            paramSource.addValue("scheduleId", (long) -1);
            paramSource.addValue("dateTime", startDayTimeStamp.toString());
            paramSource.addValue("resourceId", resourceId);
            sqlParameterSource.add(paramSource);
            startDayTimeStamp = DateUtils.getNextBlockTime(startDayTimeStamp, cdConfig.getBlockTimeInMins());
            if (startDayTimeStamp.before(endDayTimeStamp) == false) {
                paramSource = new MapSqlParameterSource();
                paramSource.addValue("scheduleId", (long) -1);
                paramSource.addValue("dateTime", startDayTimeStamp.toString());
                paramSource.addValue("resourceId", resourceId);
                sqlParameterSource.add(paramSource);
            }
        }
    }


    @Override
    public ResponseEntity<ResponseModel> getOneDateResourceWorkingHrs(String clientCode, Integer locationId, Integer resourceId, String date) throws Exception {
        OneDateWorkingHoursResponse response = new OneDateWorkingHoursResponse();
        OneDateWorkingHours oneDateWorkingHours = new OneDateWorkingHours();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, true);
        adminDAO.getMinMaxTime(jdbcCustomTemplate, date, resourceId, oneDateWorkingHours, cdConfig.getBlockTimeInMins());
        response.setOneDateWorkingHours(oneDateWorkingHours);
        return new ResponseEntity<>(commonComponent.populateRMDData(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseModel> updateConfirmAppointment(String clientCode, String comments, Long serviceId, Long scheduleId) throws Exception {
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ConfirmAppointmentRequest confirmAppointmentRequest = new ConfirmAppointmentRequest();
        confirmAppointmentRequest.setServiceId(serviceId);
        confirmAppointmentRequest.setScheduleId(scheduleId);
        confirmAppointmentRequest.setComments(comments);
        adminDAO.updateSchedule(jdbcCustomTemplate, confirmAppointmentRequest);
        return new ResponseEntity<>(commonComponent.populateRMDData(new BaseResponse()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateOneDateResourceWorkingHrs(ResourceWorkingHoursRequest resourceWorkingHoursReq) throws Exception {
        List<String> dates = resourceWorkingHoursReq.getDates();
        ResourceWorkingHrsResponse resourceWorkingHrsResponse = null;
        for (String date : dates) {
            resourceWorkingHrsResponse = updateSpecificDateResourceWorkingHrs(resourceWorkingHoursReq, date);
            if (resourceWorkingHrsResponse.isUpdateSucessfully() == false) {
                logger.error("One date working hours failed with the date: " + date + " So skipped remaining " + date);
            } else {
                logger.info("updated one date hours for the date:" + date);
            }
        }
        return new ResponseEntity<>(commonComponent.populateRMDData(resourceWorkingHrsResponse), HttpStatus.OK);
    }

    public String getTimeInDBFormat(String time) throws Exception {
        return DateUtils.convert12To24HoursHHMMSSFormat(time);
    }

    /**
     * This method used to update resource working hours - Edit Hours - One
     * Date.
     *
     * @param resourceWorkingHoursReq
     * @param date
     * @return
     * @throws Exception
     */
    public ResourceWorkingHrsResponse updateSpecificDateResourceWorkingHrs(ResourceWorkingHoursRequest resourceWorkingHoursReq, String date) throws Exception {
        ResourceWorkingHrsResponse resourceWorkingHrsResponse = new ResourceWorkingHrsResponse();
        String clientCode = resourceWorkingHoursReq.getClientCode();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

        Boolean isCampaignExists = adminDAO.getCampaign(jdbcCustomTemplate, 8);

        if (isCampaignExists) {
            resourceWorkingHrsResponse.setDisplayNotifyCheckBox("Y");
        } else {
            resourceWorkingHrsResponse.setDisplayNotifyCheckBox("N");
        }

        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        getDynamicDisplacedApptsQueryForOneDate(resourceWorkingHoursReq, date, sql, paramSource);
        System.out.println("displacementQuery::" + sql.toString());

        List<Map<String, Object>> bookedApptsList = adminDAO.fetchBookedAppointments(jdbcCustomTemplate, sql.toString(), paramSource);
        boolean isUpdate = true;
        if (!resourceWorkingHoursReq.isContinueUpdate()) {
            if (bookedApptsList != null && bookedApptsList.size() > 0) {
                isUpdate = false;
                resourceWorkingHrsResponse.setAlreadyAppointBooked(true);
                List<CustomerBean> displacedCustomers = prepareDisplaceCustomerList(jdbcCustomTemplate, clientCode, bookedApptsList);
                logger.debug("DisplacedCustomers: " + displacedCustomers);
                resourceWorkingHrsResponse.setDisplacedCustomers(displacedCustomers);
            }
        } else {
            logger.info("cancel the displacement appointments");
            if (bookedApptsList != null && bookedApptsList.size() > 0) {
                Map<String, Object> disArray = bookedApptsList.get(0);
                if (disArray.size() > 0) {
                    resourceWorkingHrsResponse.setUserName(resourceWorkingHoursReq.getUserName());
                    resourceWorkingHrsResponse.setClientCode(resourceWorkingHoursReq.getClientCode());
                    resourceWorkingHrsResponse.setDisplayNotifyCheckBox(resourceWorkingHoursReq.getNotifyCheckBox());
                    cancelDisplacedAppointmentsNew(jdbcCustomTemplate, resourceWorkingHrsResponse, bookedApptsList);
                } else {
                    logger.info("There is no displacement appointments!!");
                }
            }
        }

        if (isUpdate) {
            /**
             * else check continue flag, if true - check resourceTimeOff and
             * update schedule_id=0 in resource calendar based on the
             * minDateTime and maxDateTime
             */

            updateOneDateResourceHistory(jdbcCustomTemplate, resourceWorkingHoursReq, date);

            StringBuilder updateQueryClose = new StringBuilder();
            paramSource = new MapSqlParameterSource();
            updateResourceCalendarClose(resourceWorkingHoursReq, date, updateQueryClose, paramSource);
            System.out.println("updateQueryClose:" + updateQueryClose);
            int countOne = adminDAO.updateResourceCalendarClose(jdbcCustomTemplate, updateQueryClose.toString(), paramSource);

            StringBuilder updateQueryOpen = new StringBuilder();
            paramSource = new MapSqlParameterSource();
            updateResourceCalendarOpen(resourceWorkingHoursReq, date, updateQueryOpen, paramSource);
            System.out.println("updateQueryOpen:" + updateQueryOpen.toString());
            int countTwo = 0;
            if ("".equals(updateQueryOpen) == false) {
                countTwo = adminDAO.updateResourceCalendarOpen(jdbcCustomTemplate, updateQueryOpen.toString(), paramSource);
            }

            if (resourceWorkingHoursReq.isDayOpen()) {
                adminDAO.updateResourceSpecificDate(jdbcCustomTemplate, resourceWorkingHoursReq, date);
            }

            if (countOne > 0 || countTwo > 0) {
                resourceWorkingHrsResponse.setUpdateSucessfully(true);
            } else {
                resourceWorkingHrsResponse.setUpdateSucessfully(false);
            }
        }
        return resourceWorkingHrsResponse;
    }

    private void updateOneDateResourceHistory(JdbcCustomTemplate jdbcCustomTemplate, ResourceWorkingHoursRequest resourceWorkingHoursReq, String date) throws Exception {
        adminDAO.insertResourceWorkingHrsHistory(jdbcCustomTemplate, resourceWorkingHoursReq, date);
    }

    private void getDynamicDisplacedApptsQueryForOneDate(ResourceWorkingHoursRequest resourceWorkingHoursReq, String date, StringBuilder sql, MapSqlParameterSource paramSource) throws Exception {
        sql.append(" select distinct schedule_id,0 from resource_calendar where resource_id in (:resourceIds) and DATE(date_time) >= :dateStr");
        sql.append(" and DATE(date_time) <= :dateStr");
        sql.append(" and schedule_id > 0 ");
        paramSource.addValue("resourceIds", resourceWorkingHoursReq.getSelectedResourceIds());
        paramSource.addValue("dateStr", date);
        if (resourceWorkingHoursReq.isDayOpen()) {
            sql.append(" and (TIME(date_time) < :startTime");
            paramSource.addValue("startTime", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedStartTime()));
            if (resourceWorkingHoursReq.isBreakTimeOpen()) {
                sql.append(" or (TIME(date_time) between :startBreakTime and :endBreakTime)");

                String startBreakTime = getTimeInDBFormat(resourceWorkingHoursReq.getSelectedBreakTime());
                int breakTimeDuration = resourceWorkingHoursReq.getSelectedDuration();
                String endBreakTime = CoreUtils.addTimeSlotHHMMSS(startBreakTime, breakTimeDuration);
                paramSource.addValue("startBreakTime", startBreakTime);
                paramSource.addValue("endBreakTime", endBreakTime);
            }
            sql.append(" or TIME(date_time) >= :endtime)");
            paramSource.addValue("endtime", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedEndTime()));

        }
    }


    private void updateResourceCalendarClose(ResourceWorkingHoursRequest resourceWorkingHoursReq, String date, StringBuilder sql, MapSqlParameterSource paramSource) throws Exception {
        sql.append(" update resource_calendar set schedule_id = -1 where resource_id in (:resourceIds) and DATE(date_time) >= :dateStr");
        sql.append(" and DATE(date_time) <= :dateStr");
        sql.append(" and schedule_id = 0 ");
        paramSource.addValue("resourceIds", resourceWorkingHoursReq.getSelectedResourceIds());
        paramSource.addValue("dateStr", date);
        if (resourceWorkingHoursReq.isDayOpen()) {
            sql.append(" and (TIME(date_time) < :startTime");
            paramSource.addValue("startTime", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedStartTime()));
            if (resourceWorkingHoursReq.isBreakTimeOpen()) {
                sql.append(" or (TIME(date_time) between :startBreakTime and :endBreakTime)");

                String startBreakTime = getTimeInDBFormat(resourceWorkingHoursReq.getSelectedBreakTime());
                int breakTimeDuration = resourceWorkingHoursReq.getSelectedDuration();
                String endBreakTime = CoreUtils.addTimeSlotHHMMSS(startBreakTime, breakTimeDuration);
                paramSource.addValue("startBreakTime", startBreakTime);
                paramSource.addValue("endBreakTime", endBreakTime);
            }
            sql.append(" or TIME(date_time) >= :endtime)");
            paramSource.addValue("endtime", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedEndTime()));
        }
    }

    private void updateResourceCalendarOpen(ResourceWorkingHoursRequest resourceWorkingHoursReq, String date, StringBuilder sql, MapSqlParameterSource paramSource) throws Exception {
        sql.append(" update resource_calendar set schedule_id = 0 where resource_id in (:resourceIds) and DATE(date_time) >= :dateStr");
        sql.append(" and DATE(date_time) <= :dateStr");
        sql.append(" and schedule_id = -1 ");
        paramSource.addValue("resourceIds", resourceWorkingHoursReq.getSelectedResourceIds());
        paramSource.addValue("dateStr", date);
        if (resourceWorkingHoursReq.isDayOpen()) {
            sql.append(" and (TIME(date_time) >= :startTime");
            paramSource.addValue("startTime", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedStartTime()));
            if (resourceWorkingHoursReq.isBreakTimeOpen()) {
                sql.append(" and (TIME(date_time) < :startBreakTime) or (TIME(date_time) >=:endBreakTime)");

                String startBreakTime = getTimeInDBFormat(resourceWorkingHoursReq.getSelectedBreakTime());
                int breakTimeDuration = resourceWorkingHoursReq.getSelectedDuration();
                String endBreakTime = CoreUtils.addTimeSlotHHMMSS(startBreakTime, breakTimeDuration);
                paramSource.addValue("startBreakTime", startBreakTime);
                paramSource.addValue("endBreakTime", endBreakTime);
            }
            sql.append(" and TIME(date_time) < :endtime)");
            paramSource.addValue("endtime", getTimeInDBFormat(resourceWorkingHoursReq.getSelectedEndTime()));

        }
    }

    @Override
    public ResponseEntity<ResponseModel> getOneDateResourceWorkingHoursDetails(String clientCode) throws Exception {
        OneDateResourceWorkingHoursResponse oneDateResourceWorkingHoursResponse = new OneDateResourceWorkingHoursResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        oneDateResourceWorkingHoursResponse.setOneDateResourceWorkingHoursList(adminDAO.getOneDateResourceWorkingHoursDetails(jdbcCustomTemplate));
        return new ResponseEntity<>(commonComponent.populateRMDData(oneDateResourceWorkingHoursResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getUserAcitivityLogs(String clientCode, Integer userId, String startDate, String endDate) throws Exception {
        UserActivityLogsResponse userAcitivityLogResponse = new UserActivityLogsResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        userAcitivityLogResponse.setUserActivityLogs(adminDAO.getUserActivityLog(jdbcCustomTemplate, userId, startDate, endDate));
        return new ResponseEntity<>(commonComponent.populateRMDData(userAcitivityLogResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updatePrivilegeSettings(PrivilegeSettings privilegeSettings) throws Exception {
        Client client = cacheComponent.getClient(privilegeSettings.getClientCode(), true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

        List<PrivilegePageMapping> privilegePageMappingList = privilegeSettings.getPrivilegePageMappingList();
        if (privilegePageMappingList != null && privilegePageMappingList.size() > 0) {
            String jspPages = null;
            String[] jspPagesArr = null;

            String deleteSQL = "delete from privilege_page_mapping where privilege_id=(select id from access_privilege where privilege=?)";
            jdbcCustomTemplate.getJdbcTemplate().update(deleteSQL, new Object[]{privilegeSettings.getSelectedAccessPrivilege()});

            StringBuilder sql = new StringBuilder();
            sql.append("insert into privilege_page_mapping(privilege_id,page_name) values ((select id from access_privilege where privilege=:privilegeName),:pageName)");
            MapSqlParameterSource parameterSource = null;
            List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
            for (PrivilegePageMapping privilegePageMapping : privilegePageMappingList) {
                if (privilegePageMapping.isSelected()) {
                    jspPages = privilegePageMapping.getPageName();
                    if (jspPages != null) {
                        jspPagesArr = jspPages.split(",");
                        if (jspPagesArr != null && jspPagesArr.length > 0) {
                            for (String jspPage : jspPagesArr) {
                                parameterSource = new MapSqlParameterSource();
                                parameterSource.addValue("privilegeName", privilegeSettings.getSelectedAccessPrivilege());
                                parameterSource.addValue("pageName", jspPage);
                            }
                        }
                    }
                }
            }

            if (!list.isEmpty()) {
                SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
                SqlParameterSource batchArray[] = list.toArray(mapArray);
                jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
            }
        }
        return new ResponseEntity<>(commonComponent.populateRMDData(new BaseResponse()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getAccessPrivilege(String clientCode) throws Exception {
        AccessPrivilegeResponse accessPrivilegeRes = new AccessPrivilegeResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<AccessPrivilege> accesPrivList = adminDAO.getAccessPrivilege(jdbcCustomTemplate);
        accessPrivilegeRes.setAccessPrivilegeList(accesPrivList);
        return new ResponseEntity<>(commonComponent.populateRMDData(accessPrivilegeRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getPrivilegePageMapping(String clientCode, int accessPrivilegeId) throws Exception {
        AccessPrivilegeResponse accessPrivilegeRes = new AccessPrivilegeResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<String> accesPrivList = adminDAO.getPrivilegeMapping(jdbcCustomTemplate, accessPrivilegeId);
        accessPrivilegeRes.setPrivilegeNames(accesPrivList);
        return new ResponseEntity<>(commonComponent.populateRMDData(accessPrivilegeRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getPasswordComplexityLogic(String clientCode) throws Exception {
        PasswordResponse passwordRes = new PasswordResponse();
        passwordRes.setPasswordComplexity(masterDAO.getPasswordComplexityLogic(clientCode));
        return new ResponseEntity<>(commonComponent.populateRMDData(passwordRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getPasswordComplexityLogicByUserName(String userName) throws Exception {
        PasswordResponse passwordRes = new PasswordResponse();
        passwordRes.setPasswordComplexity(masterDAO.getPasswordComplexityLogicByUserName(userName));
        return new ResponseEntity<>(commonComponent.populateRMDData(passwordRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updatePassword(ResetPassword resetPassword) throws Exception {
        PasswordResponse passwordRes = new PasswordResponse();
        resetPassword.setNewpassword(AdminInstance.getInstance().encrypt(resetPassword.getNewpassword()));
        masterDAO.updatePassword(resetPassword);
        return new ResponseEntity<>(commonComponent.populateRMDData(passwordRes), HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<ResponseModel> addCustomerPledgeDetails(CustomerPledgeRequest customerPledgeReq) throws Exception {
        String clientCode = customerPledgeReq.getClientCode();
        CustomerPledgeResponse customerPledgeRes = new CustomerPledgeResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, true);
        customerPledgeReq.setTimeZone(cdConfig.getTimeZone());
        boolean isAdded = adminDAO.addCustomerPledgeDetails(jdbcCustomTemplate, customerPledgeReq, true);
        if (!isAdded) {
            customerPledgeRes.setStatus(false);
            customerPledgeRes.setErrorFlag("Y");
            customerPledgeRes.setErrorMessage("Add CustomerPledge failed!");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(customerPledgeRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> updateCustomerPledgeDetails(CustomerPledgeRequest customerPledgeReq) throws Exception {
        String clientCode = customerPledgeReq.getClientCode();
        CustomerPledgeResponse customerPledgeRes = new CustomerPledgeResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(clientCode, true);
        customerPledgeReq.setTimeZone(cdConfig.getTimeZone());
        boolean isAdded = adminDAO.updateCustomerForPledge(jdbcCustomTemplate, customerPledgeReq);
        if (!isAdded) {
            customerPledgeRes.setStatus(false);
            customerPledgeRes.setErrorFlag("Y");
            customerPledgeRes.setErrorMessage("Update CustomerPledge failed!");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(customerPledgeRes), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseModel> getCustomerPledgeStatusList(String clientCode) throws Exception {
        CustomerPledgeStatusResponse customerPledgeStatusResponse = new CustomerPledgeStatusResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<CustomerPledgeStatus> customerPledgeStatusList = adminDAO.getCustomerPledgeStatusList(jdbcCustomTemplate);
        customerPledgeStatusResponse.setCustomerPledgeStatusList(customerPledgeStatusList);
        return new ResponseEntity<>(commonComponent.populateRMDData(customerPledgeStatusResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getCustomerPledgeFundSourceList(String clientCode) throws Exception {
        CustomerPledgeFundSourceResponse customerPledgeFundSourceReponse = new CustomerPledgeFundSourceResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<CustomerPledgeFundSource> customerPledgeFundSourceList = adminDAO.getCustomerPledgeFundSourceList(jdbcCustomTemplate);
        customerPledgeFundSourceReponse.setCustomerPledgeFundSourceList(customerPledgeFundSourceList);
        return new ResponseEntity<>(commonComponent.populateRMDData(customerPledgeFundSourceReponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getCustomerPledgeVendorList(String clientCode, String fundId) throws Exception {
        CustomerPledgeVendorResponse customerPledgeVendorResponse = new CustomerPledgeVendorResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<CustomerPledgeVendor> customerPledgeVendorList = adminDAO.getCustomerPledgeVendorList(jdbcCustomTemplate, fundId);
        customerPledgeVendorResponse.setCustomerPledgeVendorList(customerPledgeVendorList);
        return new ResponseEntity<>(commonComponent.populateRMDData(customerPledgeVendorResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> validateOldPassword(ChangePassword changepassword) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        AdminLogin adminLogin = masterDAO.getAdminLogin(changepassword.getUserName());
        if (adminLogin != null) {
            if (adminLogin.getPassword() != null && adminLogin.getPassword() != "" && changepassword.getOldpassword() != null && changepassword.getOldpassword() != ""
                    && AdminInstance.getInstance().decrypt(adminLogin.getPassword()).equals(changepassword.getOldpassword())) {
                System.out.println("Valid Old Password");
            } else {
                baseResponse.setErrorFlag("Y");
                baseResponse.setErrorMessage("Invalid old password. Please enter correct old password");
            }
        }
        return new ResponseEntity<>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    public ResponseEntity<ResponseModel> checkValidUserName(String userName, Integer userId) throws Exception {
        UserResponse userResponse = new UserResponse();
        userResponse.setValidUser(masterDAO.isValidUser(userName, userId));
        return new ResponseEntity<>(commonComponent.populateRMDData(userResponse), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<ResponseModel> getCustomerPastApptsList(String clientCode, Long customerId) throws Exception {
        CustomerPastApptsResponse customerPastApptsResponse = new CustomerPastApptsResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<CustomerPastAppts> customerPastApptsList = adminDAO.getCustomerPastApptsList(jdbcCustomTemplate, customerId);
        customerPastApptsResponse.setCustomerPastAppts(customerPastApptsList);
        return new ResponseEntity<>(commonComponent.populateRMDData(customerPastApptsResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getAppointmentStatusReportList(String clientCode) throws Exception {
        AppointmentStatusDropDownResponse res = new AppointmentStatusDropDownResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        List<AppointmentStatusData> appointmentStatusData = adminDAO.getAppointmentStatusReportList(jdbcCustomTemplate);
        res.setAppointmentStatusList(appointmentStatusData);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(res), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> deleteUser(Integer userId) throws Exception {
        masterDAO.deletUser(userId);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(new BaseResponse()), HttpStatus.OK);
    }

    /**
     *
     *
     * Below code update the schedule id in resource calendar, -1 slot is closed, 0 mean open slot for booking, -2 for reserve.
     * @see com.telappoint.admin.appt.common.model.AppointmentStatus
     *
     * @param clientCode
     * @param resourceId
     * @param date
     * @param timeSlots
     * @param action
     * @param scheduleId
     * @return
     * @throws Exception
     */

    @Override
    public ResponseEntity<ResponseModel> updateResourceCalendarWithScheduleId(
            String clientCode, Integer resourceId, String date, String timeSlots, String action, int scheduleId) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

        boolean updated = adminDAO.updateResourceCalendarWithScheduleId(jdbcCustomTemplate, resourceId, date, prepareTimeList(timeSlots), scheduleId);
        baseResponse.setMessage("SUCCESS");

        if (!updated) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Time slot is not "+action+" properly.");
        }
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }

    private List<String> prepareTimeList(String timeSlots) {
        List<String> timeList = new ArrayList<>();
        if (timeSlots != null) {
            timeList.addAll(Arrays.asList(timeSlots.split(",")));
        }

        timeList = timeList.stream().map(x ->  x.trim() + ":00").collect(Collectors.toList());
        return timeList;
    }

    /**
     *  This function will do following operations :
     *  i) Get the scheduled appointment.
     *  ii) update the resource_calendar to -1 (Not opened appointment)
     *  @see com.telappoint.admin.appt.common.model.AppointmentStatus#NOT_OPEN
     *  iii) update the schedule table to 21 (cancel the appointment)
     *  @see com.telappoint.admin.appt.common.model.AppointmentStatus#CANCEL
     *  iv) update the appointment table to 2 (cancel the appointment)
     *  @see com.telappoint.admin.appt.common.model.AppointmentType#CANCEL
     *  v) update the notify table.
     *  delete_flag to “y”, notify_status to 3 (“Complete”) and notify_phone_status to 6 (“Suspended”)
     *
     *  @see com.telappoint.admin.appt.common.model.NotifyStatusConstants#NOTIFY_STATUS_COMPLETE
     *  @see com.telappoint.admin.appt.common.model.NotifyStatusConstants#NOTIFY_STATUS_SUSPENDED
     *
     * @param clientCode
     * @param resourceId
     * @param date
     * @param timeSlots
     * @return
     * @throws Exception
     */

    @Override
    public ResponseEntity<ResponseModel> closeAppt(String clientCode, Integer resourceId, String date, String timeSlots) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

        boolean updated = adminDAO.closeScheduledAppointment(jdbcCustomTemplate, resourceId, date, prepareTimeList(timeSlots), -1);
        baseResponse.setMessage("SUCCESS");

        if (!updated) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("Appointment(s) slot is not closed properly.");
        }

        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);
    }
    
    
    @Override
    public ResponseEntity<ResponseModel> getPrivilegeByUserPrivilege(String clientCode, String accessPrivilegeName) throws Exception {
        PrivilegeSettingResponse privilegeSettingRes = new PrivilegeSettingResponse();
        Client client = cacheComponent.getClient(clientCode, true);
        JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
        Map<String, List<JSPPagesPrivileges>> transStateList = adminDAO.getPrivilegeByUserPrivilege(jdbcCustomTemplate, accessPrivilegeName);
        privilegeSettingRes.setPrivilegeSetting(transStateList);
        return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(privilegeSettingRes), HttpStatus.OK);
    }
    
    @Override
	public ResponseEntity<ResponseModel> deletePledge(String clientCode, String customerPledgeId, String fundName, String eligible, String houseHoldId) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		Client client = cacheComponent.getClient(clientCode, true);
	    JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		adminDAO.deletePledge(jdbcCustomTemplate, customerPledgeId);
		adminDAO.updateLHEAPandPSEHelpRecievedStatus(jdbcCustomTemplate, houseHoldId, fundName, eligible);
		return new ResponseEntity<ResponseModel>(commonComponent.populateRMDData(baseResponse), HttpStatus.OK);			
	}
}
