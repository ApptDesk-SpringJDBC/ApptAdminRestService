package com.telappoint.admin.appt.common.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.telappoint.admin.appt.common.model.PledgeDetails;

/**
 * @author Balaji 
 */

public class PledgeReportUtil  {
    public static Map<String, List<PledgeDetails>> separateByPledgeReport(List<PledgeDetails> pledgeReportList, String separateBy) {
        return parseJsonBySeparator(pledgeReportList, separateBy);
    }
    private static Map<String, List<PledgeDetails>> parseJsonBySeparator(List<PledgeDetails> pledgeReportList, String separateBy) {
        Map<String, List<PledgeDetails>> pledgeReportListMap = new LinkedHashMap<String, List<PledgeDetails>>();
       
        if(!separateBy.equals("Vendor")) {
        	String tempKey = "";
            for (PledgeDetails pledgeDetails : pledgeReportList) {
                String key = "";
                switch (separateBy) {
                    case "Intake":
                        key = pledgeDetails.getResourceNameOnline();
                        break;
                    case "Vendor":
                        key = pledgeDetails.getVendorName1();
                        break;
                    case "FundSource":
                        key = pledgeDetails.getFundName();
                        break;
                    default:
                        key = "All";
                        break;
                }

                if (tempKey != null && key != null && !tempKey.equalsIgnoreCase(key)) {
                    tempKey = key;
                }
                 if (key == null) {
                     key = "All";
                 }

                 
                 
                List<PledgeDetails> pledgeDetailses = pledgeReportListMap.get(key);
                if (pledgeDetailses == null) {
                    pledgeDetailses = new ArrayList<>();
                }
                pledgeDetailses.add(pledgeDetails);

                if (!pledgeReportListMap.containsKey(key)) {
                    pledgeReportListMap.put(key, pledgeDetailses);
                }
            }
        } else {
            for (PledgeDetails pledgeDetails : pledgeReportList) {
                String key = pledgeDetails.getVendorName1();
                 if("PSE HELP".equals(pledgeDetails.getFundName()) && pledgeDetails.getVendorName1().startsWith("PSE")) {
                	 key = "PSE Electric & PSE Gas";
                 }
                 
                 if (key == null) {
                     key = "All";
                 }
 
                List<PledgeDetails> pledgeDetailses = pledgeReportListMap.get(key);
                if (pledgeDetailses == null) {
                    pledgeDetailses = new ArrayList<>();
                }
                pledgeDetailses.add(pledgeDetails);

                if (!pledgeReportListMap.containsKey(key)) {
                    pledgeReportListMap.put(key, pledgeDetailses);
                }
            }
        }
           
        return pledgeReportListMap;
    }

}

