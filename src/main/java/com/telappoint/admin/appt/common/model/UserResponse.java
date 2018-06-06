package com.telappoint.admin.appt.common.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.admin.appt.common.model.response.BaseResponse;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserResponse extends BaseResponse {
	private List<AdminLogin> userList;
	private List<AdminLogin> suspendedUserList;
	private Boolean validUser;
	
	//user exist response
	private AdminLogin adminLogin;

	public AdminLogin getAdminLogin() {
		return adminLogin;
	}

	public void setAdminLogin(AdminLogin adminLogin) {
		this.adminLogin = adminLogin;
	}
	
	public List<AdminLogin> getUserList() {
		return userList;
	}
	public void setUserList(List<AdminLogin> userList) {
		this.userList = userList;
	}
	public List<AdminLogin> getSuspendedUserList() {
		return suspendedUserList;
	}
	public void setSuspendedUserList(List<AdminLogin> suspendedUserList) {
		this.suspendedUserList = suspendedUserList;
	}

	public Boolean getValidUser() {
		return validUser;
	}

	public void setValidUser(Boolean validUser) {
		this.validUser = validUser;
	}
}
