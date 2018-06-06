package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import java.util.Map;

import com.telappoint.admin.appt.common.model.BasicTablePrintData;
import com.telappoint.admin.appt.common.model.DynamicFieldLabelData;
import com.telappoint.admin.appt.common.model.TablePrintAppointmentData;

/**
 * 
 * @author Balaji N
 *
 */
public class TablePrintViewResponse extends BaseResponse {
	private String clientName;
	private Integer locationId;
	private String holiday="N";
	private String closedDay="N";
	private List<DynamicFieldLabelData> dynamicFieldLabels;
	private Map<BasicTablePrintData, List<TablePrintAppointmentData>> tablePrintViewData;
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public String getClosedDay() {
		return closedDay;
	}

	public void setClosedDay(String closedDay) {
		this.closedDay = closedDay;
	}

	public Map<BasicTablePrintData, List<TablePrintAppointmentData>> getTablePrintViewData() {
		return tablePrintViewData;
	}

	public void setTablePrintViewData(Map<BasicTablePrintData, List<TablePrintAppointmentData>> tablePrintViewData) {
		this.tablePrintViewData = tablePrintViewData;
	}

	public List<DynamicFieldLabelData> getDynamicFieldLabels() {
		return dynamicFieldLabels;
	}

	public void setDynamicFieldLabels(List<DynamicFieldLabelData> dynamicFieldLabels) {
		this.dynamicFieldLabels = dynamicFieldLabels;
	}
}
