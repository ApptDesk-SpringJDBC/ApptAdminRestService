package com.telappoint.admin.appt.common.model.response;

import java.util.List;

public class UpdateResourceWorkingHrsResponse extends BaseResponse {
	private boolean alreadyAppointBooked;
	private boolean updateSucessfully;
	private List<CustomerBean> displacedCustomers;
	private String displayNotifyCheckBox="N";

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
}
