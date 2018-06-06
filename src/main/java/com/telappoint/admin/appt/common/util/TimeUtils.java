package com.telappoint.admin.appt.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class TimeUtils {

    public static Map<String, Object> geHourMinMeridian(String timeString) {
        Map<String, Object> out = new LinkedHashMap<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date parse = sdf.parse(timeString);
            int hour = 0;
            int min = 0;
            String meridian = "";
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(parse.getTime());

            hour = cal.get(Calendar.HOUR);
            min = cal.get(Calendar.MINUTE);

            if (cal.get(Calendar.AM_PM) == 0)
                meridian = "AM";
            else
                meridian = "PM";

            if ("PM".equals(meridian) && hour == 0 && min == 0) {
                hour = 12;
            }

            out.put("hour", hour);
            out.put("min", min);
            out.put("meridian", meridian);

        } catch (Exception ex) {
            //
        }
        return out;
    }
}