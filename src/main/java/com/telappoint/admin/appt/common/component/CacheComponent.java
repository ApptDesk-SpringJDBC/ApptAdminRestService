package com.telappoint.admin.appt.common.component;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.telappoint.admin.appt.common.constants.CacheConstants;
import com.telappoint.admin.appt.common.constants.ErrorConstants;
import com.telappoint.admin.appt.common.dao.AdminDAO;
import com.telappoint.admin.appt.common.dao.MasterDAO;
import com.telappoint.admin.appt.common.model.Client;
import com.telappoint.admin.appt.common.model.ClientDeploymentConfig;
import com.telappoint.admin.appt.common.model.JdbcCustomTemplate;
import com.telappoint.admin.appt.common.model.request.BaseRequest;
import com.telappoint.admin.appt.handlers.exception.TelAppointException;

@Component
public class CacheComponent {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	private static Map<String, Client> clientCacheMap = new HashMap<String, Client>();
	private static Map<String, Object> cacheObject = new HashMap<String, Object>();
	private static final Object lock = new Object();

	@Autowired
	private MasterDAO masterDAO;
	
	@Autowired
	private AdminDAO adminDAO;


	public Client getClient(String clientCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.CLIENT.getValue()).append("|").append(clientCode);
		Client client = clientCacheMap.get(key.toString());
		if (client != null && cache) {
			if (logger != null) {
				logger.debug("Client object returned from cache.");
			}
			return client;
		} else {
			logger.debug("Client object returned from DB.");
			synchronized (lock) {
				masterDAO.getClients(CacheConstants.CLIENT.getValue(), clientCacheMap);
			}
			client = clientCacheMap.get(key.toString());
			if (client == null) {
				if (logger != null) {
					logger.info("Client is not available to process - [clientCode:" + clientCode + "]");
				}
				BaseRequest baseRequest = new BaseRequest();
				baseRequest.setClientCode(clientCode);
				throw new TelAppointException(ErrorConstants.ERROR_2998.getCode(), ErrorConstants.ERROR_2998.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "client code is not valid.", baseRequest.toString());
			}
			return client;
		}
	}
	
	public ClientDeploymentConfig getClientDeploymentConfig(String clientCode, boolean cache) throws TelAppointException, Exception  {
		Client client = getClient(clientCode, true);
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.CLIENT_DEPLOYMENT_CONFIG.getValue()).append("|").append(clientCode);

		ClientDeploymentConfig clientDeploymentConfig = (ClientDeploymentConfig) cacheObject.get(key.toString());
		if (clientDeploymentConfig != null && cache) {
			logger.debug("ClientDeploymentConfig object returned from cache.");
			return clientDeploymentConfig;
		} else {
			logger.debug("ClientDeploymentConfig object returned from DB.");
			synchronized (lock) {
				masterDAO.getClientDeploymentConfig(CacheConstants.CLIENT_DEPLOYMENT_CONFIG.getValue(), clientCode, client.getClientId(), cacheObject);
			}
			clientDeploymentConfig = (ClientDeploymentConfig) cacheObject.get(key.toString());
			return clientDeploymentConfig;
		}
	}
	
	private Map<String, Map<String, String>> getDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, String mainKey, boolean cache) throws TelAppointException, Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			logger.debug("DisplayFieldLabels returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			logger.debug("DisplayFieldLabels returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			adminDAO.getI18nDisplayFieldLabelsMap(jdbcCustomTemplate, logger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	
	public Map<String, String> getDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, String device, String langCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_FIELD_LABEL.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getDisplayFieldLabelsMap(jdbcCustomTemplate, key.toString(), cache);
		key = new StringBuilder();
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}
	
	
	private Map<String, Map<String, String>> getDisplayPageContentsMap(JdbcCustomTemplate jdbcCustomTemplate, String mainKey, boolean cache) throws TelAppointException, Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			logger.debug("DisplayPageContents returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			logger.debug("DisplayPageContents returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			adminDAO.getI18nPageContentMap(jdbcCustomTemplate, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}
	
	public Map<String, String> getDisplayPageContentsMap(JdbcCustomTemplate jdbcCustomTemplate, String device, String langCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_PAGE_CONTENT.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getDisplayPageContentsMap(jdbcCustomTemplate, key.toString(), cache);
		key = new StringBuilder();
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}
	
	private Map<String, Map<String, String>> getI18nEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String mainKey, boolean cache) throws TelAppointException, Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			logger.debug("EmailTemplateMap returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			logger.debug("EmailTemplateMap returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			adminDAO.getI18nEmailTemplateMap(jdbcCustomTemplate, logger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public Map<String, String> getEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String langCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.EMAIL_TEMPLATE.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getI18nEmailTemplateMap(jdbcCustomTemplate, logger, key.toString(), cache);
		key.setLength(0);
		key.append(langCode);
		return map.get(key.toString());
	}
	
	
	public Client getClientById(Integer clientId) throws TelAppointException, Exception {
		String clientCode = masterDAO.getClientCode(clientId);
		return getClient(clientCode, true);
	}
}
