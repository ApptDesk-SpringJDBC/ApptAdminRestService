package com.telappoint.admin.appt.common.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertySource {

    @Value("${app.jdbc.clientdb.username}")
    private String clientUserName;

    @Value("${app.jdbc.clientdb.password}")
    private String clientPassword;

    @Value("${app.jdbc.masterdb.username}")
    private String masterUserName;

    @Value("${app.jdbc.masterdb.password}")
    private String masterPassword;

    @Value("${app.jdbc.masterdb.url}")
    private String masterConnectUri;

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
    }

    public String getMasterUserName() {
        return masterUserName;
    }

    public void setMasterUserName(String masterUserName) {
        this.masterUserName = masterUserName;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public String getMasterConnectUri() {
        return masterConnectUri;
    }

    public void setMasterConnectUri(String masterConnectUri) {
        this.masterConnectUri = masterConnectUri;
    }
}
