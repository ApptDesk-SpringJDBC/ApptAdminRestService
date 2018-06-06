package com.telappoint.admin.appt.common.model.response;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kotaiah on 26/04/17.
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResourceWorkingHrsResponse implements Serializable{


    private boolean alreadyAppointBooked;
    private boolean updateSucessfully;
    private List<CustomerBean> displacedCustomers;
    private String displayNotifyCheckBox="N";
    private String clientCode;
    private String userName;
    private String deviceType;
    private String langCode;
    private Long scheduleId;

    public boolean isAlreadyAppointBooked() {
        return alreadyAppointBooked;
    }

    public void setAlreadyAppointBooked(boolean alreadyAppointBooked) {
        this.alreadyAppointBooked = alreadyAppointBooked;
    }

    public boolean isUpdateSucessfully() {
        return updateSucessfully;
    }

    public void setUpdateSucessfully(boolean updateSucessfully) {
        this.updateSucessfully = updateSucessfully;
    }

    public List<CustomerBean> getDisplacedCustomers() {
        return displacedCustomers;
    }

    public void setDisplacedCustomers(List<CustomerBean> displacedCustomers) {
        this.displacedCustomers = displacedCustomers;
    }

    public String getDisplayNotifyCheckBox() {
        return displayNotifyCheckBox;
    }

    public void setDisplayNotifyCheckBox(String displayNotifyCheckBox) {
        this.displayNotifyCheckBox = displayNotifyCheckBox;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }


    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}



