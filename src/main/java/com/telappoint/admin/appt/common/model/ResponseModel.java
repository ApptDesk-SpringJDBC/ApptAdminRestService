package com.telappoint.admin.appt.common.model;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.telappoint.admin.appt.common.component.EmailComponent;
import com.telappoint.admin.appt.common.constants.ErrorConstants;
import com.telappoint.admin.appt.common.constants.PropertiesConstants;
import com.telappoint.admin.appt.common.util.CoreUtils;
import com.telappoint.admin.appt.common.util.PropertyUtils;
import com.telappoint.admin.appt.handlers.exception.TelAppointException;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResponseModel {
	
	
	
	private final static Logger logger = Logger.getLogger(ResponseModel.class);
    private Object data;
	private Object errors;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static ResponseEntity<ResponseModel> exceptionResponse(String clientName, Exception e, EmailComponent emailComponent) {
		
		ResponseModel jsonData = new ResponseModel();
		String exceptionMessage;
		String inputData;
		Errors errors = new Errors();
		errors.setStatus(false);
		HttpStatus httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		if (e instanceof TelAppointException) {
			TelAppointException be = (TelAppointException) e;
			exceptionMessage = be.getExceptionMessage();
			inputData = be.getInputData() == null?"":be.getInputData().toString();
			errors.setMessage(be.getMessage());
			errors.setCode(be.getCode());
			if (be.getInputData() != null) {
				logger.info("InputParameters: " + be.getInputData());
			}
			httpStatusCode = be.getHttpStatus();
		} else {
			exceptionMessage = "Generic Exception catched! - Advice to handle this exception in application level.";
			inputData = "Unable to get the input data in this scenario.";
			errors.setMessage(ErrorConstants.ERROR_9999.getMessage());
			errors.setCode(ErrorConstants.ERROR_9999.getCode());
		}
		
		jsonData.setErrors(errors);
		logger.error("Root Cause of Exception:" + exceptionMessage);
		logger.error("HttpStatus code to front end: " + httpStatusCode);
		logger.error("Error JSON payload to front end: " + jsonData.toString());
		logger.error("Stack Trace: " + e, e);
		
		String sendErrorEmail = "N";
		try {
			sendErrorEmail = PropertyUtils.getValueFromProperties("error.mail.send", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
			if ("Y".equalsIgnoreCase(sendErrorEmail)) {
				String subject = "Error in ApptAdminRestService for ClientName - " + clientName;
				StringBuilder errorMsg = new StringBuilder();
				errorMsg.append("InputParameters: ").append(inputData);
				errorMsg.append("<br/> ");
				errorMsg.append(CoreUtils.getMethodAndClassName(e));
				errorMsg.append("<br/>");
				errorMsg.append("Root Cause of Exception:").append(exceptionMessage);
				errorMsg.append("<br/><br/>");
				errorMsg.append("Stack Trace:");
				errorMsg.append("<br/>");
				errorMsg.append("<br/>" + CoreUtils.getStackTrace(e));
				EmailRequest emailRequest = new EmailRequest();
				emailRequest.setEmailBody(errorMsg.toString());
				emailRequest.setSubject(subject);
				emailRequest.setEmailType("error");
				emailComponent.setErrorMailServerPreference(emailRequest);
				emailComponent.sendEmail(emailRequest, null);
			}
		} catch (Exception ioe) {
			logger.error("Error while sending an error email ::"+ioe,ioe);
		}
		return new ResponseEntity<ResponseModel>(jsonData, httpStatusCode);
	}
	
	public Object getErrors() {
		return errors;
	}

	public void setErrors(Object errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "ResponseModel [data=" + data + ", errors=" + errors + "]";
	}
}