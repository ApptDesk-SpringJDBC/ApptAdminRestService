package com.telappoint.admin.appt.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {
    private static final Log LOGGER = LogFactory.getLog(ApplicationProperties.class.getName());
    private static final Object lock = new Object();

    private static Map<String, Properties> propsMap = new TreeMap<String, Properties>();

    public static Properties getProperties(String name) throws Exception {
        Properties properties = propsMap.get(name);
        if (properties != null) {
            return properties;
        } else {
            try {
                properties = new Properties();
                properties.load(new FileInputStream("/apps/properties/" + name + ".properties"));
                synchronized (lock) {
                    propsMap.put(name, properties);
                    return properties;
                }
            } catch (IOException var5) {
                LOGGER.error(var5.getMessage(), var5);
                throw new Exception(name + " file is not found");
            }
        }
    }
}
