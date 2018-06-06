package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.admin.appt.common.model.response.BaseResponse;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HoldAppt extends BaseResponse {
    private Long scheduleId;
    private String displayDateTime;
   
    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getDisplayDateTime() {
        return displayDateTime;
    }

    public void setDisplayDateTime(String displayDateTime) {
        this.displayDateTime = displayDateTime;
    }


    public HoldAppt(Long scheduleId, String displayDateTime, String errorMessage) {
        this.scheduleId = scheduleId;
        this.displayDateTime = displayDateTime;
        if(errorMessage !=null && !"".equals(errorMessage)) {
               this.errorMessage = errorMessage;
               this.errorFlag="Y";
        }
    }


    public HoldAppt(Long scheduleId, String displayDateTime, String errorMessage, boolean status) {
        this.scheduleId = scheduleId;
        this.displayDateTime = displayDateTime;
        this.status=status;
        if(errorMessage !=null && !"".equals(errorMessage)) {
            this.errorMessage = errorMessage;
            this.errorFlag="Y";
     }
    }

    @Override
    public String toString() {
        return "HoldAppt [scheduleId=" + scheduleId + ", errorMessage=" + errorMessage + ", displayDateTime=" + displayDateTime + "]";
    }

	public String getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
}