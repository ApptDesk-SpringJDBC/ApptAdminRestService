package com.telappoint.admin.appt.common.controller;

import com.telappoint.admin.appt.common.constants.ErrorConstants;
import com.telappoint.admin.appt.common.constants.PropertiesConstants;
import com.telappoint.admin.appt.common.model.*;
import com.telappoint.admin.appt.common.model.request.*;
import com.telappoint.admin.appt.common.service.AdminRestService;
import com.telappoint.admin.appt.common.util.PropertyUtils;
import com.telappoint.admin.appt.handlers.exception.TelAppointException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Koti, Balaji
 */
@Controller
@RequestMapping("/service")
public class AdminRestController {
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private AdminRestService adminRestService;

	@RequestMapping(method = RequestMethod.GET, value = "getAdminHomePage", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getAdminHomePage(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Long loginUserId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getHomePageResponse(clientCode, loginUserId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getClientDetails", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getClientDetails(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getClientDetails(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "loginAuthenticate", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> loginAuthenticate(HttpServletRequest request, @RequestBody UserLogin userLogin) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.loginAuthenticate(userLogin);
		} catch (Exception e) {
			return adminRestService.handleException("From admin site login", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getDailyCalendar", produces = "application/json")
    public
    @ResponseBody ResponseEntity<ResponseModel> getDailyCalendar(HttpServletRequest request, @RequestParam("clientCode") String clientCode,                                
    		@RequestParam("calendarDate") String calendarDate,
            @RequestParam("locationId") Integer locationId,
            @RequestParam("resourceIds") String resourceIds) {
  
        try {
        	String ipAddress = request.getRemoteAddr();
     		if (checkIP(ipAddress)) {
     			sendEmailIPNotAllowed(ipAddress);
     		}
			return adminRestService.getDailyCalendar(clientCode, calendarDate, locationId, resourceIds);
        } catch (Exception e) {
        	return adminRestService.handleException(clientCode, e);
        }
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/getWeeklyCalendar", produces = "application/json")
    public
    @ResponseBody ResponseEntity<ResponseModel> getWeeklyCalendar(HttpServletRequest request, @RequestParam("clientCode") String clientCode,                                
    		@RequestParam("calendarDate") String calendarDate,
            @RequestParam("locationId") Integer locationId,
            @RequestParam("resourceIds") String resourceIds) {
  
        try {
        	String ipAddress = request.getRemoteAddr();
     		if (checkIP(ipAddress)) {
     			sendEmailIPNotAllowed(ipAddress);
     		}
			return adminRestService.getWeeklyCalendar(clientCode, calendarDate, locationId, resourceIds);
        } catch (Exception e) {
        	return adminRestService.handleException(clientCode, e);
        }
    }
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/getMonthlyCalendar", produces = "application/json")
    public
    @ResponseBody ResponseEntity<ResponseModel> getMonthlyCalendar(HttpServletRequest request, @RequestParam("clientCode") String clientCode,                                
    		@RequestParam("calendarDate") String calendarDate,
            @RequestParam("locationId") Integer locationId,
            @RequestParam("resourceIdStr") String resourceIdStr) {
        try {
        	String ipAddress = request.getRemoteAddr();
     		if (checkIP(ipAddress)) {
     			sendEmailIPNotAllowed(ipAddress);
     		}
			return adminRestService.getMonthlyCalendar(clientCode, calendarDate, locationId, resourceIdStr);
        } catch (Exception e) {
        	return adminRestService.handleException(clientCode, e);
        }
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/getJSCalendarAvailability", produces = "application/json")
    public
    @ResponseBody ResponseEntity<ResponseModel> getJSCalendarAvailability(HttpServletRequest request, @RequestParam("clientCode") String clientCode,                                
    		@RequestParam("calendarDate") String calendarDate,
            @RequestParam("locationId") Integer locationId,
            @RequestParam("resourceIdStr") String resourceIdStr,
            @RequestParam("serviceIdStr") String serviceIdStr) {
        try {
        	String ipAddress = request.getRemoteAddr();
     		if (checkIP(ipAddress)) {
     			sendEmailIPNotAllowed(ipAddress);
     		}
			return adminRestService.getJSCalendarAvailablity(clientCode, calendarDate, locationId, resourceIdStr, serviceIdStr);
        } catch (Exception e) {
        	return adminRestService.handleException(clientCode, e);
        }
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "getLocationList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getHomePageLocationList(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Long loginUserId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getHomePageLocationList(clientCode, loginUserId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAllLocationsBasicData", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getAllLocationsBasicData(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAllLocationsBasicData(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getLocationById", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getLocationById(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer locationId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getLocationById(clientCode, locationId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getCompleteLocationDataById", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getCompleteLocationDataById(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer locationId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getCompleteLocationDataById(clientCode, locationId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getDynamicFieldDisplayData", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getDynamicFieldDisplayData(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String pageName) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getDynamicFieldDisplayData(clientCode, pageName);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "updateLocation", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateLocation(HttpServletRequest request, @RequestBody Location location) {
		String clientCode = location.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateLocation(location);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "addLocation", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> addLocation(HttpServletRequest request, @RequestBody Location location) {
		String clientCode = location.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.addLocation(location);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getActiveLocationDropDownData", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getActiveLocationDropDownData(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getActiveLocationDropDownData(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getLocationsByServiceIdToCloseServiceStatus", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getLocationsByServiceIdToCloseServiceStatus(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer serviceId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getLocationsByServiceIdToCloseServiceStatus(clientCode, serviceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getActiveResourceDropDownData", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getActiveResourceDropDownData(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getActiveResourceDropDownData(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getActiveServiceDropDownData", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getActiveServiceDropDownData(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getActiveServiceDropDownData(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "deleteLocation", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> deleteLocation(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer locationId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.deleteLocation(clientCode, locationId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "unDeleteLocation", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> unDeleteLocation(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer locationId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.unDeleteLocation(clientCode, locationId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAllServicesBasicData", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getAllServicesBasicData(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAllServicesBasicData(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getResourceList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getResourceList(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Long loginUserId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getResourceList(clientCode, loginUserId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "deleteResource", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> deleteResource(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer resourceId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.deleteResource(clientCode, resourceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "unDeleteResource", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> unDeleteResource(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer resourceId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.unDeleteResource(clientCode, resourceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAllResourcesBasicData", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getAllResourcesBasicData(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAllResourcesBasicData(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getCompleteResourceDataById", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getCompleteResourceDataById(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer resourceId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getCompleteResourceDataById(clientCode, resourceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getResourcePrefixList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getResourcePrefixList(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getResourcePrefixList(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getResourceTitleList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getResourceTitleList(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getResourceTitleList(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getResourceTypeList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getResourceTypeList(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getResourceTypeList(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getResourceById", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getResourceById(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer resourceId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getResourceById(clientCode, resourceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getResourceListByLocationId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getResourceListByLocationId(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer locationId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getResourceListByLocationId(clientCode, locationId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "addResource", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> addResource(HttpServletRequest request, @RequestBody Resource resource) {
		String clientCode = resource.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.addResource(resource);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "updateResource", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateResource(HttpServletRequest request, @RequestBody Resource resource) {
		String clientCode = resource.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateResource(resource);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getServiceById", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getServiceById(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer serviceId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getServiceById(clientCode, serviceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getServiceDropDownList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getServiceDropDownList(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer locationId, @RequestParam Integer resourceId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getServiceDropDownList(clientCode, locationId, resourceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getCompleteServiceDataById", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getCompleteServiceDataById(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer serviceId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getCompleteServiceDataById(clientCode, serviceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "getServiceListByResourceId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getServiceListByResourceId(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer resourceId, @RequestParam Integer loginUserId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getServiceListByResourceId(clientCode, resourceId, loginUserId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getServiceListByLocationId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getServiceListByLocationId(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer locationId, @RequestParam Integer loginUserId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getServiceListByLocationId(clientCode, locationId, loginUserId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "getResourceServiceList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getResourceServiceList(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer locationId, @RequestParam Integer loginUserId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getResourceServiceList(clientCode, locationId, loginUserId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	

	@RequestMapping(method = RequestMethod.POST, value = "addService", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> addService(HttpServletRequest request, @RequestBody ServiceVO service) {
		String clientCode = service.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.addService(service);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "updateService", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateService(HttpServletRequest request, @RequestBody ServiceVO service) {
		String clientCode = service.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateService(service);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "deleteService", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> deleteService(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer serviceId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.deleteService(clientCode, serviceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "unDeleteService", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> unDeleteService(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer serviceId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.unDeleteService(clientCode, serviceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "addUser", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> addUser(HttpServletRequest request, @RequestBody AdminLogin adminLogin) {
		String clientCode = adminLogin.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.addUser(adminLogin);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "deleteUser", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> deleteUser(HttpServletRequest request, @RequestParam Integer userId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.deleteUser(userId);
		} catch (Exception e) {
			return adminRestService.handleException("MasterDB", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "updateUser", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateUser(HttpServletRequest request, @RequestBody AdminLogin adminLogin) {
		String clientCode = adminLogin.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateUser(adminLogin);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getUsers", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getUsers(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getUsers(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPasswordComplexity", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getPasswordComplexity(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getPasswordComplexity(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "validateUser", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> validateUser(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer userId, @RequestParam String userName) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.validateUser(clientCode, userId, userName);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getUserById", produces = "application/json") 
	public @ResponseBody ResponseEntity<ResponseModel> getUserById(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer userId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getUserById(clientCode, userId);
		} catch (Exception e) {
			return adminRestService.handleException(e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getInBoundCallLogs", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getInBoundCallLogs(HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam String fromDate, @RequestParam String toDate, @RequestParam(value="callerId",required=false) String callerId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getInBoundCallLogs(clientCode, fromDate, toDate, callerId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getOutBoundCallLogs", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getOutBoundCallLogs(HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam String fromDate, @RequestParam String toDate, @RequestParam(value="callerId",required=false) String callerId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getOutBoundCallLogs(clientCode, fromDate, toDate, callerId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "addAppointmentReportConfig", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> addAppointmentReportConfig(HttpServletRequest request, @RequestBody AppointmentReportConfig apptReportConfig) {
		String clientCode = apptReportConfig.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.addAppointmentReportConfig(apptReportConfig);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAppointmentReportConfig", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getAppointmentReportConfig(HttpServletRequest request, @RequestParam String userName) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAppointmentReportConfig(userName);
		} catch (Exception e) {
			return adminRestService.handleException(e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "deleteApptReportConfigById", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> deleteApptReportConfigById(HttpServletRequest request, @RequestParam Integer configId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.deleteApptReportConfigById(configId);
		} catch (Exception e) {
			return adminRestService.handleException(e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getDynamicIncludeReportsData", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getDynamicIncludeReportsData(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getDynamicIncludeReportsData(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAppointmentReport", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getAppointmentReport(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String fromDate,
			@RequestParam String toDate,
			@RequestParam String locationIds,
			@RequestParam String resourceIds,
			@RequestParam String serviceIds,
			@RequestParam String apptStatus) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAppointmentReport(clientCode, fromDate, toDate, locationIds, resourceIds, serviceIds, apptStatus);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "searchByFirstLastName", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> searchByFirstLastName(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String firstName,
			@RequestParam String lastName) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.searchByFirstLastName(clientCode, firstName, lastName);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@Deprecated
	@RequestMapping(method = RequestMethod.GET, value = "searchAppointmentsByFirstLastName", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> searchAppointmentsByFirstLastName(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String firstName,
			@RequestParam String lastName) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.searchAppointmentsByFirstLastName(clientCode, firstName, lastName);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@Deprecated
	@RequestMapping(method = RequestMethod.GET, value = "searchAppointmentsByAccountNumber", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> searchAppointmentsByAccountNumber(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String accountNumber) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.searchAppointmentsByAccountNumber(clientCode, accountNumber);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "searchByConfirmationNumber", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> searchByConfirmationNumber(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam Long confirmationNumber) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.searchByConfirmationNumber(clientCode, confirmationNumber);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "searchByContactPhone", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> searchByContactPhone(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String contactPhone) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.searchByContactPhone(clientCode, contactPhone);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "searchByCallerId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> searchByCallerId(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String callerId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.searchByCallerId(clientCode, callerId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "searchByAccountNumber", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> searchByAccountNumber(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String accountNumber) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.searchByAccountNumber(clientCode, accountNumber);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "searchByAttrib1", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> searchByAttrib1(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String attrib1) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.searchByAttrib1(clientCode, attrib1);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "searchByDOB", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> searchByDOB(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String dob) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.searchByDob(clientCode, dob);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "searchByHouseHoldId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> searchByHouseHoldId(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam Long houseHoldId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.searchByHouseHoldId(clientCode, houseHoldId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getSearchDropDownList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getSearchDropDownList(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getSearchDropDownList(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAppointmentsByCustomerId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getAppointmentsByCustomerId(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Long customerId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAppointmentsByCustomerId(clientCode, customerId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "updateConfirmAppointment", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateConfirmAppointment(HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam String comments,
			@RequestParam Long serviceId,
			@RequestParam Long scheduleId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateConfirmAppointment(clientCode, comments, serviceId, scheduleId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAppointmentStatusDropDownList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getAppointmentStatusDropDownList(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAppointmentStatusDropDownList(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAppointmentStatusReportList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getAppointmentStatusReportList(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAppointmentStatusReportList(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getCustomersById", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getCustomersById(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Long customerId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getCustomersById(clientCode, customerId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "createCustomer", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> createCustomer(HttpServletRequest request, @RequestBody CustomerRequest customerRequest) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.createCustomer(customerRequest);
		} catch (Exception e) {
			return adminRestService.handleException(customerRequest.getClientCode(), e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "createOrUpdateCustomer", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> createOrUpdateCustomer(HttpServletRequest request, @RequestBody CustomerRequest customerRequest) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.createOrUpdateCustomer(customerRequest);
		} catch (Exception e) {
			return adminRestService.handleException(customerRequest.getClientCode(), e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "updateCustomer", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateCustomer(HttpServletRequest request, @RequestBody CustomerRequest customerRequest) {
		String clientCode = customerRequest.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateCustomer(customerRequest);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getFutureAppointments", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getFutureAppointments(HttpServletRequest request, @RequestParam String clientCode, @RequestParam long customerId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getFutureAppointments(clientCode, customerId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPastAppointments", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getPastAppointments(HttpServletRequest request, @RequestParam String clientCode, @RequestParam long customerId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getPastAppointments(clientCode, customerId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getBlockedCustomers", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getBlockedCustomers(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getBlockedCustomers(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "updateCustomerBlockedReason", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateCustomerBlockedReason(HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam Long customerId, @RequestParam String reasonMessage) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateCustomerBlockedReason(clientCode, customerId, reasonMessage);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "unBlockCustomer", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> unBlockCustomer(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Long customerId ) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.unBlockCustomer(clientCode, customerId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "getCustomerActivitiesByCustomerId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getCustomerActivitiesByCustomerId(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Long customerId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getCustomerActivities(clientCode, customerId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getHouseHoldInfoByCustomerId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getHouseHoldInfoByCustomerId(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Long customerId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getHouseHoldInfo(clientCode, customerId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "mergeHouseHoldId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> mergeHouseHoldId(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String fromHouseHoldIds, @RequestParam String mergeToHouseHoldId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.mergeHouseHoldId(clientCode, fromHouseHoldIds, mergeToHouseHoldId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "splitHouseHoldId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> splitHouseHoldId(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String customerIds, @RequestParam String newHouseHoldId, @RequestParam String assignNewHouseholdID) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.splitHouseHoldIdWithAssignNewHouseHoldId(clientCode, customerIds, newHouseHoldId, assignNewHouseholdID);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "updateHouseHoldId", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateHouseHoldId(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String customerId, @RequestParam String newHouseHoldId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.splitHouseHoldId(clientCode, customerId, newHouseHoldId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getTransStates", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getTransStates(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam Long transId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getTransStates(clientCode, transId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPrivilegeSettings", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getPrivilegeSettings(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String  accessPrivilegeName) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getPrivilegeSettings(clientCode, accessPrivilegeName);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getTablePrintView", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getTablePrintView(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam Integer locationId, @RequestParam String resourceIds, @RequestParam String date) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getTablePrintView(clientCode, locationId, resourceIds, date);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "getSummaryReport", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getSummaryReport(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String fromDate, @RequestParam String toDate, @RequestParam String reportCategory) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getSummaryReport(clientCode, fromDate, toDate, reportCategory);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "holdAppointment", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> holdAppointment(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam(value="langCode", required=false) String langCode, 
			@RequestParam("locationId") Long locationId, 
			@RequestParam("resourceId") Long resourceId,
			@RequestParam("procedureId") Long procedureId,
			@RequestParam("departmentId") Long departmentId, @RequestParam("serviceId") Long serviceId, @RequestParam("customerId") Long customerId,
			@RequestParam("apptDateTime") String apptDateTime, @RequestParam("transId") Long transId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			 
			return adminRestService.holdAppointment(clientCode, locationId, resourceId, procedureId, departmentId, serviceId, customerId, apptDateTime, device, langCode, transId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "rescheduleAppointment", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> rescheduleAppointment(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam(value="langCode", required=false) String langCode, 
			@RequestParam("locationId") Long locationId, 
			@RequestParam("resourceId") Long resourceId,
			@RequestParam("procedureId") Long procedureId,
			@RequestParam("oldscheduleId") Long oldscheduleId,
			@RequestParam("departmentId") Long departmentId, @RequestParam("serviceId") Long serviceId, @RequestParam("customerId") Long customerId,
			@RequestParam("apptDateTime") String apptDateTime, @RequestParam("transId") Long transId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.rescheduleAppointment(clientCode, locationId, resourceId, procedureId, departmentId, serviceId, customerId, apptDateTime, device, langCode, transId, oldscheduleId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getVerifyPageData", produces = "application/json")
	public ResponseEntity<ResponseModel> getVerifyPageData(HttpServletRequest request, @RequestParam String clientCode,@RequestParam("device") String device, @RequestParam String langCode, 
			@RequestParam Long customerId, @RequestParam Long scheduleId, @RequestParam("transId") Long transId) {
		
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getVerifyPageData(clientCode, device, langCode, customerId, scheduleId);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "confirmAppointment", produces = "application/json", consumes = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> confirmAppointment(HttpServletRequest request, @RequestBody ConfirmAppointmentRequest confirmAppointmentRequest) {
		String clientCode = confirmAppointmentRequest.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.confirmAppointment(confirmAppointmentRequest);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "cancelAppointment", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> cancelAppointment(HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam String langCode, @RequestParam Long scheduleId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.cancelAppointment(clientCode, langCode, scheduleId);
		} catch (TelAppointException e) {
			return adminRestService.handleException(clientCode, e);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getItemizedReport", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getItemizedReport(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam Integer locationId, @RequestParam Integer serviceId,
			@RequestParam String fromDate, @RequestParam String toDate, @RequestParam String reportCategory) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getItemizedReport(clientCode, locationId, serviceId, fromDate, toDate, reportCategory);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getItemizedReportTemplate", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getItemizedReportTemplate(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam Integer locationId, @RequestParam Integer serviceId,
			@RequestParam String fromDate, @RequestParam String toDate, @RequestParam String reportCategory) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getItemizedReportTemplate(clientCode, locationId, serviceId, fromDate, toDate, reportCategory);
			
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getDynamicPledgeResults", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getDynamicPledgeResults(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getDynamicPledgeResults(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getAutoSuggestCustomerNames", produces = "application/json")
    public @ResponseBody ResponseEntity<ResponseModel> getCustomerNames(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String customerName) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAutoSuggestCustomerNames(clientCode, customerName);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/getCustomerRegistrationDetails", produces = "application/json")
    public @ResponseBody ResponseEntity<ResponseModel> getCustomerRegistrationDetails(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String langCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getCustomerRegistrationDetails(clientCode, langCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
    }
	
	
	@RequestMapping(method = RequestMethod.GET, value = "getPledgeReport", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getPledgeReport(HttpServletRequest request, @RequestParam String clientCode, 
			 @RequestParam String fromDate,
             @RequestParam String toDate,
             @RequestParam(required = false) Integer locationId, // 0 means all location, > 0 means specific location.
             @RequestParam(required = false, defaultValue = "N") String groupByIntake, // Y mean enable, N mean disable
             @RequestParam(required = false, defaultValue = "N") String groupByFundSource, // Y mean enable, N mean disable
             @RequestParam(required = false, defaultValue = "N") String groupByVendor, // Y mean enable, N mean disable
             @RequestParam(required = false) Integer resourceId, // 0 means all resourceId, > 0 means specific resource.
             @RequestParam(required = false) Integer fundSourceId // 0 means all fundSourceId, > 0 means specific fundSource.
			) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getPledgeReport(clientCode, fromDate, toDate, locationId,groupByIntake, groupByFundSource, groupByVendor, resourceId, fundSourceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getApptSysConfig", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getApptSysConfig(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			return adminRestService.getApptSysConfig(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getDisplayNames", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getDisplayNames(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			return adminRestService.getDisplayNames(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPrivilegedPageNames", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getPrivilegedPageNames(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			return adminRestService.getPrivilegedPageNames(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getServiceLocationApptDatesWindow", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getServiceLocationApptDatesWindow(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Integer locationId) {
		try {
			return adminRestService.getServiceLocationApptDatesWindow(clientCode, locationId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "updateServiceLocationApptDatesWindow", produces = "application/json", consumes="application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateServiceLocationApptDatesWindow(HttpServletRequest request, @RequestBody ServiceLocationApptDatesRequest serviceLocApptDatesReq) {
		try {
			return adminRestService.updateServiceLocationApptDatesWindow(serviceLocApptDatesReq);
		} catch (Exception e) {
			return adminRestService.handleException(serviceLocApptDatesReq.getClientCode(), e);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "updateApptRestrictDates", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateApptRestrictDates(HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam String apptStartDate, @RequestParam String apptEndDate) {
		try {
			return adminRestService.updateApptRestrictDates(clientCode, apptStartDate, apptEndDate);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "updateLocationsApptDates", produces = "application/json", consumes="application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateLocationsApptDates(HttpServletRequest request, @RequestBody LocationsApptDatesRequest locationApptReq) {
		try {
			return adminRestService.updateLocationsApptDates(locationApptReq);
		} catch (Exception e) {
			return adminRestService.handleException(locationApptReq.getClientCode(), e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "updateScheduleClosedStatus", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateScheduleClosedStatus(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String closedStatus) {
		try {
			return adminRestService.updateScheduleClosedStatus(clientCode, closedStatus);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "updateAppointmentStatus", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateAppointmentStatus(HttpServletRequest request, @RequestParam String clientCode, @RequestParam Long scheduleId, @RequestParam
			int status, @RequestParam String userName) {
		try {
			return adminRestService.updateAppointmentStatus(clientCode, scheduleId, status, userName);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "updateApptPerSeasonDetails", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateApptPerSeasonDetails(HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam String termStartDate, @RequestParam String termEndDate, @RequestParam Integer noApptPerTerm) {
		try {
			return adminRestService.updateApptPerSeasonDetails(clientCode, termStartDate, termEndDate, noApptPerTerm);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "updateAppointStatus", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateAppointStatus(HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam String screenedFlag, @RequestParam String accessedFlag, @RequestParam Integer status, @RequestParam Long scheduleId) {
		try {
			return adminRestService.updateAppointStatus(clientCode, screenedFlag, accessedFlag, status, scheduleId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAppointmentStatus", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getAppointmentStatus(HttpServletRequest request, @RequestParam String clientCode,
			 @RequestParam Long scheduleId) {
		try {
			return adminRestService.getAppointmentStatus(clientCode, scheduleId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getSameServiceBlockList", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getSameServiceBlockList(HttpServletRequest request,
			@RequestParam String clientCode, @RequestParam Integer locationId, @RequestParam Integer resourceId, @RequestParam Integer serviceId) {
		try {
			return adminRestService.getSameServiceBlockList(clientCode, locationId, resourceId, serviceId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "updateRecordTime", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateRecordTime(HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam String recordName, @RequestParam String recordType, @RequestParam Integer scheduleId) {
		try {
			return adminRestService.updateRecordTime(clientCode, recordName, recordType, scheduleId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPledgeHistory", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getPledgeHistory(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("customerId") Long customerId, @RequestParam("transId") Long transId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getPledgeHistory(clientCode, device, langCode, customerId, transId);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getGaugeChart", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getGaugeChart(HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam Integer locationId, @RequestParam String startDate, @RequestParam String endDate) {
		try {
			return adminRestService.getGaugeChart(clientCode, locationId, startDate, endDate);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getStackedChart ", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getStackedChart (HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam Integer locationId, @RequestParam Integer resourceId, @RequestParam String stackChartType) {
		try {
			return adminRestService.getStackedChart(clientCode, locationId, resourceId, stackChartType);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "getPieChart ", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getPieChart (HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam Integer locationId, @RequestParam String selectedDate) {
		try {
			return adminRestService.getPieChart(clientCode, locationId, selectedDate);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getApptsForOutlook", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getApptsForOutlook (HttpServletRequest request, @RequestParam String clientCode,
			@RequestParam String userName,@RequestParam String password) {
		try {
			return adminRestService.getApptsForOutlook(clientCode, userName, password);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "updateOutlookSyncStatus", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateOutlookSyncStatus (HttpServletRequest request, @RequestBody OutlookSyncReq outlookSyncReq) {
		try {
			return adminRestService.updateOutlookSyncStatus(outlookSyncReq);
		} catch (Exception e) {
			return adminRestService.handleException(outlookSyncReq.getClientCode(), e);
		}
	}


	@RequestMapping(method = RequestMethod.GET, value = "/getSuggestedResourceWorkingHours", produces = "application/json")
	public
	@ResponseBody
	ResponseEntity<ResponseModel> getSuggestedResourceWorkingHours(
			HttpServletRequest request,
			@RequestParam String clientCode,
			@RequestParam String locationId,
			@RequestParam String resourceIds,
			@RequestParam String fromDate,
			@RequestParam String toDate
	) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getSuggestedResourceWorkingHours(clientCode,locationId, resourceIds, fromDate, toDate);
		} catch (TelAppointException e) {
			return adminRestService.handleException( clientCode, e);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}


    @RequestMapping(method = RequestMethod.POST, value = "/updateResourceWorkingHours", consumes="application/json", produces = "application/json")
	public ResponseEntity<ResponseModel> updateResourceWorkingHours(HttpServletRequest request, @RequestBody  ResourceHoursRequest resourceHoursRequest) {
        try {
            String ipAddress = request.getRemoteAddr();
            if (checkIP(ipAddress)) {
               //  sendEmailIPNotAllowed(ipAddress);
            }
            return adminRestService.updateResourceWorkingHours(resourceHoursRequest);
        } catch (TelAppointException e) {
            return adminRestService.handleException( resourceHoursRequest.getClientCode(), e);
        } catch (Exception e) {
            return adminRestService.handleException(resourceHoursRequest.getClientCode(), e);
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/getOneDateResourceWorkingHrs", produces = "application/json")
	public ResponseEntity<ResponseModel> getOneDateResourceWorkingHrs(HttpServletRequest request,@RequestParam String clientCode, @RequestParam Integer locationId, @RequestParam Integer resourceId,
			@RequestParam String date) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getOneDateResourceWorkingHrs(clientCode,locationId, resourceId, date);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateOneDateResourceWorkingHrs", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseModel> updateOneDateResourceWorkingHrs(HttpServletRequest request, @RequestBody ResourceWorkingHoursRequest resourceWorkingHoursReq) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				 sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateOneDateResourceWorkingHrs(resourceWorkingHoursReq);
		} catch (Exception e) {
			return adminRestService.handleException(resourceWorkingHoursReq.getClientCode(), e);
		}
	}
		
	@RequestMapping(method = RequestMethod.GET, value = "/getOneDateResourceWorkingHoursDetails", produces = "application/json")
	public ResponseEntity<ResponseModel> getOneDateResourceWorkingHoursDetails(HttpServletRequest request,@RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getOneDateResourceWorkingHoursDetails(clientCode);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getUserActivityLogs", produces = "application/json")
	public ResponseEntity<ResponseModel> getUserActivityLogs(HttpServletRequest request,@RequestParam String clientCode,
			@RequestParam Integer userId,
			@RequestParam String startDate, @RequestParam String endDate) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getUserAcitivityLogs(clientCode, userId, startDate, endDate);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/updatePrivilegeSettings", consumes="application/json", produces = "application/json")
	public ResponseEntity<ResponseModel> updatePrivilegeSettings(
            HttpServletRequest request,@RequestBody  PrivilegeSettings privilegeSettings) {
        try {
            String ipAddress = request.getRemoteAddr();
            if (checkIP(ipAddress)) {
               //  sendEmailIPNotAllowed(ipAddress);
            }
            return adminRestService.updatePrivilegeSettings(privilegeSettings);
        } catch (Exception e) {
            return adminRestService.handleException(privilegeSettings.getClientCode(), e);
        }
    }
    
	@RequestMapping(method = RequestMethod.GET, value = "getAvailableDates", produces = "application/json")
	public ResponseEntity<ResponseModel> getAvailableDates(HttpServletRequest request,@RequestParam String clientCode, @RequestParam String device,
			    @RequestParam("locationId") Long locationId, @RequestParam("departmentId") Long departmentId,
			    @RequestParam("resourceIds") String resourceIds, @RequestParam("serviceIds") String serviceIds, @RequestParam("transId") Long transId) {
	
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAvailableDates(clientCode, device, locationId, departmentId, resourceIds, serviceIds);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "releaseHoldAppointment", produces = "application/json")
	public ResponseEntity<ResponseModel> releaseHoldAppointment(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String device,
			@RequestParam Long scheduleId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.releaseHoldAppointment(clientCode, device, scheduleId);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAccessPrivilege", produces = "application/json")
	public ResponseEntity<ResponseModel> getAccessPrivilege(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getAccessPrivilege(clientCode);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPrivilegePageMapping", produces = "application/json")
	public ResponseEntity<ResponseModel> getPrivilegePageMapping(HttpServletRequest request, @RequestParam String clientCode, int accessPrivilegeId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getPrivilegePageMapping(clientCode, accessPrivilegeId);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPasswordComplexityLogic", produces = "application/json")
	public ResponseEntity<ResponseModel> getPasswordComplexityLogic(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getPasswordComplexityLogic(clientCode);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPasswordComplexityLogicByUserName", produces = "application/json")
	public ResponseEntity<ResponseModel> getPasswordComplexityLogicByUserName(HttpServletRequest request, @RequestParam String userName) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getPasswordComplexityLogicByUserName(userName);
		}  catch (Exception e) {
			return adminRestService.handleException("MasterDB", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "updatePassword", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updatePassword(HttpServletRequest request, @RequestBody ResetPassword resetPassword) {
		String clientCode = resetPassword.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updatePassword(resetPassword);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "validateOldPassword", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> validateOldPassword(HttpServletRequest request, @RequestBody ChangePassword changepassword) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.validateOldPassword(changepassword);
		} catch (Exception e) {
			return adminRestService.handleException("MasterDB", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "checkValidUserName", produces = "application/json")
	public ResponseEntity<ResponseModel> checkValidUserName(HttpServletRequest request, @RequestParam String userName, @RequestParam Integer userId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.checkValidUserName(userName, userId);
		}  catch (Exception e) {
			return adminRestService.handleException("MasterDB", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "addCustomerPledgeDetails", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> addCustomerPledgeDetails(HttpServletRequest request, @RequestBody CustomerPledgeRequest customerPledgeReq) {
		String clientCode = customerPledgeReq.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.addCustomerPledgeDetails(customerPledgeReq);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "updateCustomerPledgeDetails", consumes="application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> updateCustomerPledgeDetails(HttpServletRequest request, @RequestBody CustomerPledgeRequest customerPledgeReq) {
		String clientCode = customerPledgeReq.getClientCode();
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateCustomerPledgeDetails(customerPledgeReq);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "deletePledge", produces = "application/json")
	public ResponseEntity<ResponseModel> deletePledge(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String customerPledgeId,
    		@RequestParam String fundName, @RequestParam String eligible, @RequestParam String houseHoldId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.deletePledge(clientCode, customerPledgeId, fundName, eligible, houseHoldId);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getCustomerPledgeStatusList", produces = "application/json")
	public ResponseEntity<ResponseModel> getCustomerPledgeStatusList(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getCustomerPledgeStatusList(clientCode);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "getCustomerPledgeFundSourceList", produces = "application/json")
	public ResponseEntity<ResponseModel> getCustomerPledgeFundSourceList(HttpServletRequest request, @RequestParam String clientCode) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getCustomerPledgeFundSourceList(clientCode);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getCustomerPledgeVendorList", produces = "application/json")
	public ResponseEntity<ResponseModel> getCustomerPledgeVendorList(HttpServletRequest request, @RequestParam String clientCode,@RequestParam String fundId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getCustomerPledgeVendorList(clientCode, fundId);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getCustomerPastApptsList", produces = "application/json")
	public ResponseEntity<ResponseModel> getCustomerPastApptsList(HttpServletRequest request, @RequestParam String clientCode,@RequestParam Long customerId) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getCustomerPastApptsList(clientCode, customerId);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "closeApptTimeSlot", produces = "application/json")
	public ResponseEntity<ResponseModel> closeApptTimeSlot(
			HttpServletRequest request, @RequestParam Integer resourceId, @RequestParam String clientCode,
			@RequestParam String date, @RequestParam String timeSlots) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateResourceCalendarWithScheduleId(clientCode, resourceId, date, timeSlots, "close", -1);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "openApptTimeSlot", produces = "application/json")
	public ResponseEntity<ResponseModel> openApptTimeSlot(
			HttpServletRequest request, @RequestParam Integer resourceId, @RequestParam String clientCode,
			@RequestParam String date, @RequestParam String timeSlots) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateResourceCalendarWithScheduleId(clientCode, resourceId, date, timeSlots, "opened", 0);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "reserveApptTimeSlot", produces = "application/json")
	public ResponseEntity<ResponseModel> reserveApptTimeSlot(
			HttpServletRequest request, @RequestParam Integer resourceId, @RequestParam String clientCode,
			@RequestParam String date, @RequestParam String timeSlots) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateResourceCalendarWithScheduleId(clientCode, resourceId, date, timeSlots, "reserved", -2);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "removeReserveApptTimeSlot", produces = "application/json")
	public ResponseEntity<ResponseModel> removeReserveApptTimeSlot(
			HttpServletRequest request, @RequestParam Integer resourceId, @RequestParam String clientCode,
			@RequestParam String date, @RequestParam String timeSlots) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.updateResourceCalendarWithScheduleId(clientCode, resourceId, date, timeSlots, "remove reserve", -1);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}


	@RequestMapping(method = RequestMethod.GET, value = "closeAppt", produces = "application/json")
	public ResponseEntity<ResponseModel> closeAppt(
			HttpServletRequest request, @RequestParam Integer resourceId, @RequestParam String clientCode,
			@RequestParam String date, @RequestParam String timeSlots) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.closeAppt(clientCode, resourceId, date, timeSlots);
		}  catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
	

	public void sendEmailIPNotAllowed(String ipaddress) throws TelAppointException {
		throw new TelAppointException(ErrorConstants.ERROR_2995.getCode(), ErrorConstants.ERROR_2995.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "IP Not Allowed:" + ipaddress,
				null);
	}

	private final String iptoCheck = "127.0.0.1";
	public boolean checkIP(String ipAddress) throws TelAppointException, Exception {
		try {
			String allowAnyIp = PropertyUtils.getValueFromProperties("ALLOW_ANY_IP", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
			if ("true".equals(allowAnyIp)) {
				return false;
			}
			return !ipAddress.equals(iptoCheck);
		} catch (IOException ioe) {
			throw new TelAppointException(ErrorConstants.ERROR_3000.getCode(), ErrorConstants.ERROR_3000.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ioe.getMessage(), null);
		}
	}
	
	//
	@RequestMapping(method = RequestMethod.GET, value = "getPrivilegeByUserPrivilege", produces = "application/json")
	public @ResponseBody ResponseEntity<ResponseModel> getPrivilegeByUserPrivilege(HttpServletRequest request, @RequestParam String clientCode, 
			@RequestParam String  accessPrivilegeName) {
		try {
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ipAddress);
			}
			return adminRestService.getPrivilegeByUserPrivilege(clientCode, accessPrivilegeName);
		} catch (Exception e) {
			return adminRestService.handleException(clientCode, e);
		}
	}
}
