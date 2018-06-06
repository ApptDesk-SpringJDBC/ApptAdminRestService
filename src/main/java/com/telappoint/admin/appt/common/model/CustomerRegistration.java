package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class CustomerRegistration  {
	private Integer customerRegId;
	private String displayContext;
	private String paramTable;	
	private String paramColumn;
	private String paramType;
	private String loginType;
	private String displayType;
	private String displayTitle;
	private String fieldNotes;
	private String displayFormat;
	private Integer displaySize;
	private Integer maxChars;
	private Integer textareaRows;
	private Integer textareaCols;
	private String displayHint;	
	private String displayTooltip;
	private String emptyErrorMsg;	
	private String invalidErrorMsg;
	private String validateRequired;
	private String validationRules;
	private Integer validateMaxChars;
	private Integer validateMinValue;
	private Integer validateMaxValue;
	private String listLabels;
	private String listValues;
	private String listInitialValues;
	private String required;
	private String titleMessageKey;
	private String titleMessageValue;
	private Integer fieldLabelsTitleId;
	private String emptyMessageKey;
	private String emptyMessageValue;
	private Integer fieldLabelsEmptyId;
	private String invalidMessageKey;
	private String invalid_messageValue;
	private Integer fieldLabelsInvalidId;
	private Integer placement;
	public Integer getCustomerRegId() {
		return customerRegId;
	}
	public void setCustomerRegId(Integer customerRegId) {
		this.customerRegId = customerRegId;
	}
	public String getDisplayContext() {
		return displayContext;
	}
	public void setDisplayContext(String displayContext) {
		this.displayContext = displayContext;
	}
	public String getParamTable() {
		return paramTable;
	}
	public void setParamTable(String paramTable) {
		this.paramTable = paramTable;
	}
	public String getParamColumn() {
		return paramColumn;
	}
	public void setParamColumn(String paramColumn) {
		this.paramColumn = paramColumn;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public String getDisplayTitle() {
		return displayTitle;
	}
	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}
	public String getFieldNotes() {
		return fieldNotes;
	}
	public void setFieldNotes(String fieldNotes) {
		this.fieldNotes = fieldNotes;
	}
	public String getDisplayFormat() {
		return displayFormat;
	}
	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}
	public Integer getDisplaySize() {
		return displaySize;
	}
	public void setDisplaySize(Integer displaySize) {
		this.displaySize = displaySize;
	}
	public Integer getMaxChars() {
		return maxChars;
	}
	public void setMaxChars(Integer maxChars) {
		this.maxChars = maxChars;
	}
	public Integer getTextareaRows() {
		return textareaRows;
	}
	public void setTextareaRows(Integer textareaRows) {
		this.textareaRows = textareaRows;
	}
	public Integer getTextareaCols() {
		return textareaCols;
	}
	public void setTextareaCols(Integer textareaCols) {
		this.textareaCols = textareaCols;
	}
	public String getDisplayHint() {
		return displayHint;
	}
	public void setDisplayHint(String displayHint) {
		this.displayHint = displayHint;
	}
	public String getDisplayTooltip() {
		return displayTooltip;
	}
	public void setDisplayTooltip(String displayTooltip) {
		this.displayTooltip = displayTooltip;
	}
	public String getEmptyErrorMsg() {
		return emptyErrorMsg;
	}
	public void setEmptyErrorMsg(String emptyErrorMsg) {
		this.emptyErrorMsg = emptyErrorMsg;
	}
	public String getInvalidErrorMsg() {
		return invalidErrorMsg;
	}
	public void setInvalidErrorMsg(String invalidErrorMsg) {
		this.invalidErrorMsg = invalidErrorMsg;
	}
	public String getValidateRequired() {
		return validateRequired;
	}
	public void setValidateRequired(String validateRequired) {
		this.validateRequired = validateRequired;
	}
	public String getValidationRules() {
		return validationRules;
	}
	public void setValidationRules(String validationRules) {
		this.validationRules = validationRules;
	}
	public Integer getValidateMaxChars() {
		return validateMaxChars;
	}
	public void setValidateMaxChars(Integer validateMaxChars) {
		this.validateMaxChars = validateMaxChars;
	}
	public Integer getValidateMinValue() {
		return validateMinValue;
	}
	public void setValidateMinValue(Integer validateMinValue) {
		this.validateMinValue = validateMinValue;
	}
	public Integer getValidateMaxValue() {
		return validateMaxValue;
	}
	public void setValidateMaxValue(Integer validateMaxValue) {
		this.validateMaxValue = validateMaxValue;
	}
	public String getListLabels() {
		return listLabels;
	}
	public void setListLabels(String listLabels) {
		this.listLabels = listLabels;
	}
	public String getListValues() {
		return listValues;
	}
	public void setListValues(String listValues) {
		this.listValues = listValues;
	}
	public String getListInitialValues() {
		return listInitialValues;
	}
	public void setListInitialValues(String listInitialValues) {
		this.listInitialValues = listInitialValues;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public String getTitleMessageKey() {
		return titleMessageKey;
	}
	public void setTitleMessageKey(String titleMessageKey) {
		this.titleMessageKey = titleMessageKey;
	}
	public String getTitleMessageValue() {
		return titleMessageValue;
	}
	public void setTitleMessageValue(String titleMessageValue) {
		this.titleMessageValue = titleMessageValue;
	}
	public Integer getFieldLabelsTitleId() {
		return fieldLabelsTitleId;
	}
	public void setFieldLabelsTitleId(Integer fieldLabelsTitleId) {
		this.fieldLabelsTitleId = fieldLabelsTitleId;
	}
	public String getEmptyMessageKey() {
		return emptyMessageKey;
	}
	public void setEmptyMessageKey(String emptyMessageKey) {
		this.emptyMessageKey = emptyMessageKey;
	}
	public String getEmptyMessageValue() {
		return emptyMessageValue;
	}
	public void setEmptyMessageValue(String emptyMessageValue) {
		this.emptyMessageValue = emptyMessageValue;
	}
	public Integer getFieldLabelsEmptyId() {
		return fieldLabelsEmptyId;
	}
	public void setFieldLabelsEmptyId(Integer fieldLabelsEmptyId) {
		this.fieldLabelsEmptyId = fieldLabelsEmptyId;
	}
	public String getInvalidMessageKey() {
		return invalidMessageKey;
	}
	public void setInvalidMessageKey(String invalidMessageKey) {
		this.invalidMessageKey = invalidMessageKey;
	}
	public String getInvalid_messageValue() {
		return invalid_messageValue;
	}
	public void setInvalid_messageValue(String invalid_messageValue) {
		this.invalid_messageValue = invalid_messageValue;
	}
	public Integer getFieldLabelsInvalidId() {
		return fieldLabelsInvalidId;
	}
	public void setFieldLabelsInvalidId(Integer fieldLabelsInvalidId) {
		this.fieldLabelsInvalidId = fieldLabelsInvalidId;
	}
	public Integer getPlacement() {
		return placement;
	}
	public void setPlacement(Integer placement) {
		this.placement = placement;
	}
	
	
}
