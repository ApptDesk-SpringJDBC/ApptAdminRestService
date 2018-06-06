package com.telappoint.admin.appt.common.util;

import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CoreUtils {
    private static Logger logger = Logger.getLogger(CoreUtils.class);
    
    public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		String stackTrace = sw.toString();
		return stackTrace.replace(System.getProperty("line.separator"), "<br/>\n");
	}
	
	public static String getMethodAndClassName(final Throwable cause) {
	     Throwable rootCause = cause;
	     while(rootCause.getCause() != null &&  rootCause.getCause() != rootCause) {
	          rootCause = rootCause.getCause();
	     }
	    StringBuilder sb = new StringBuilder();
	    sb.append("<br>").append("ClassName: ").append(rootCause.getStackTrace()[0].getClassName()); 
	    sb.append("<br>").append("MethodName: ").append(rootCause.getStackTrace()[0].getMethodName()); 
	    return sb.toString();
	} 
	
	public static void setPropertyValue(Object object, String propertyName, Object propertyValue) throws IllegalAccessException, InvocationTargetException, IllegalArgumentException, IntrospectionException {
		BeanInfo bi = Introspector.getBeanInfo(object.getClass());
		PropertyDescriptor pds[] = bi.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			if (pd.getName().equals(propertyName)) {
				Method setter = pd.getWriteMethod();
				if (setter != null) {
					setter.invoke(object, new Object[] { propertyValue });
				}
			}
		}
	}
	
	public static String addTimeSlotHHMMSS(String time, int minutes) {
		GregorianCalendar slot = DateUtils.formatHHMMSSStringToGC(time);
		slot.add(Calendar.MINUTE, minutes);
		return new String(DateUtils.formatGCDateToHH_MM_SS(slot));
	}
	
	public static Object getPropertyValue(Object object, String fieldName) throws NoSuchFieldException {
		try {
			BeanInfo info = Introspector.getBeanInfo(object.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				if (pd.getName().equals(fieldName)) {
					Method getter = pd.getReadMethod();
					if (getter != null) {
						getter.setAccessible(true);
						return getter.invoke(object, null);
					}

				}
			}
		} catch (Exception e) {
			throw new NoSuchFieldException(object.getClass() + " has no field " + fieldName);
		}
		return "";
	}
	
	public static void main(String[] args) {
		System.out.println(HtmlUtils.htmlEscape("__"));
		System.out.println(Arrays.stream("11,31,51,52,53,50,32,33,34,35,36,37,38,39,40,41,42,11,50".split(",")).map(Integer::parseInt).collect(Collectors.toList()));
	}
	
	public static String replaceAllPlaceHolders(String emailMessage, Map<String, String> emailData) {
		if (emailData != null) {
			Set<String> keySet = emailData.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = emailData.get(key);
				emailMessage = emailMessage.replace(key, value == null ? "" : value);
			}
		} else {
			logger.error("EmailPlaceHolder data is empty.!");
		}
		return emailMessage;
	}
	
	public static boolean isAdmin(String device) {
		return "admin".equalsIgnoreCase(device) ? true : false;
	}
	
	public static Object getInitCaseValue(Object value) {
		String name = (String) value;
		StringBuilder nameBuilder = new StringBuilder();
		String[] nameStrs = name.split("\\s+");
		if (nameStrs != null && nameStrs.length > 0) {
			for (String nameStr : nameStrs) {
				if (nameStr != null && !" ".equals(nameStr) && nameStr.length() > 0) {
					nameBuilder.append(nameStr.substring(0, 1) != null ? nameStr.substring(0, 1).toUpperCase() : "");
					nameBuilder.append(nameStr.substring(1));
					nameBuilder.append(" ");
				}
			}
		}
		if (nameBuilder.toString() != null && !"".equals(nameBuilder.toString().trim())) {
			value = nameBuilder.toString().trim();
		}
		return value;
	}

	public static String getFormatedPhoneNumber(String phoneNo, String format) {
		if (phoneNo != null && !"".equals(phoneNo)) {
			if ("US".equals(format) || "UK".equals(format)) {
				phoneNo = phoneNo.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
			} else {
				phoneNo = "";
			}
		} else {
			phoneNo = "";
		}
		return phoneNo;
	}
}