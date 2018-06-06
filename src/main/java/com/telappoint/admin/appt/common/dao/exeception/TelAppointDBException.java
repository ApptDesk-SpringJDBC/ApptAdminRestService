package com.telappoint.admin.appt.common.dao.exeception;

public class TelAppointDBException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Integer exceptionCode;
    private String exceptionDesc;

	public TelAppointDBException(Integer exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public TelAppointDBException(Integer exceptionCode, String exceptionDesc) {
		this.exceptionCode = exceptionCode;
		this.exceptionDesc = exceptionDesc;
	}

	public void setExceptionDesc(String exceptionDesc) {
		this.exceptionDesc = exceptionDesc;
	}

	public String getExceptionDesc() {
		return exceptionDesc;
	}

	public void setExceptionCode(Integer exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public Integer getExceptionCode() {
		return exceptionCode;
	}
}
