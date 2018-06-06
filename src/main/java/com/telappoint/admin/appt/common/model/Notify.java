package com.telappoint.admin.appt.common.model;

import java.io.Serializable;


public class Notify implements Serializable {

    private static final long serialVersionUID = 5103032064864327643L;

	public Notify() {
	}
	

	public long id;
	
	private String timestamp;
		
	private Integer campaignId;
	
	private String due_date_time;
	
	private Character call_now = new Character('N');
	
	private Character emergency_notify = new Character('N');
	
	private Character broadcast_mode = new Character('N');
	
	private Integer notify_status;

	private Integer resourceId;

	private Integer locationId;
	
	private Integer serviceId;

	private Integer customerId;
	
	private Integer scheduleId;
	
	private String first_name;
	
	private String middle_name;
	
	private String last_name;
	
	private String phone1;
	
	private String phone2;
	
	private String phone3;
	
	private String home_phone;
	
	private String work_phone;
	
	private String cell_phone;
	
	private Integer cell_phone_prov;
	
	private String email;
	
	private String email_cc;
	
	private String email_bcc;
	
	private Integer lang_id;
	
	private Integer notify_preference;
	
	private Character notify_by_phone;
	
	private Character notify_by_phone_confirm;
	
	private Character notify_by_sms;
	
	private Character notify_by_sms_confirm;
	
	private Character notify_by_email;
	
	private Character notify_by_email_confirm;
	
	private Character notify_by_push_notif;
	
	private Integer notify_phone_status;
	
	private Integer notify_sms_status;
	
	private Integer notify_email_status;
	
	private Integer notify_push_notification_status;
	
	private String include_audio_1;
	
	private String include_audio_2;
	
	private String include_audio_3;
	
	private String include_audio_4;
	
	private String include_audio_5;
	
	private Character do_not_notify = new Character('N');
	
	private String comment;
	
	private Character delete_flag = new Character('N');
	
	private String attrib1;

	private String attrib2;

	private String attrib3;

	private String attrib4;

	private String attrib5;

	private String attrib6;

	private String attrib7;

	private String attrib8;

	private String attrib9;

	private String attrib10;

	private String attrib11;

	private String attrib12;

	private String attrib13;

	private String attrib14;

	private String attrib15;

	private String attrib16;

	private String attrib17;

	private String attrib18;

	private String attrib19;

	private String attrib20;
	
	private String notes;
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setTimestamp(String value) {
		this.timestamp = value;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setDue_date_time(String timestamp) {
		this.due_date_time = timestamp;
	}
	public String getDue_date_time() {
		return due_date_time;
	}
	
	public void setCall_now(char value) {
		setCall_now(new Character(value));
	}
	
	public void setCall_now(Character value) {
		this.call_now = value;
	}
	
	public Character getCall_now() {
		return call_now;
	}
	
	public void setEmergency_notify(char value) {
		setEmergency_notify(new Character(value));
	}
	
	public void setEmergency_notify(Character value) {
		this.emergency_notify = value;
	}
	
	public Character getEmergency_notify() {
		return emergency_notify;
	}
	
	public void setBroadcast_mode(char value) {
		setBroadcast_mode(new Character(value));
	}
	
	public void setBroadcast_mode(Character value) {
		this.broadcast_mode = value;
	}
	
	public Character getBroadcast_mode() {
		return broadcast_mode;
	}
	
	public void setNotify_status(int value) {
		setNotify_status(new Integer(value));
	}
	
	public void setNotify_status(Integer value) {
		this.notify_status = value;
	}
	
	public Integer getNotify_status() {
		return notify_status;
	}
	
	public void setFirst_name(String value) {
		this.first_name = value;
	}
	
	public String getFirst_name() {
		return first_name;
	}
	
	public void setMiddle_name(String value) {
		this.middle_name = value;
	}
	
	public String getMiddle_name() {
		return middle_name;
	}
	
	public void setLast_name(String value) {
		this.last_name = value;
	}
	
	public String getLast_name() {
		return last_name;
	}
	
	public void setPhone1(String value) {
		this.phone1 = value;
	}
	
	public String getPhone1() {
		return phone1;
	}
	
	public void setPhone2(String value) {
		this.phone2 = value;
	}
	
	public String getPhone2() {
		return phone2;
	}
	
	public void setPhone3(String value) {
		this.phone3 = value;
	}
	
	public String getPhone3() {
		return phone3;
	}
	
	public void setHome_phone(String value) {
		this.home_phone = value;
	}
	
	public String getHome_phone() {
		return home_phone;
	}
	
	public void setWork_phone(String value) {
		this.work_phone = value;
	}
	
	public String getWork_phone() {
		return work_phone;
	}
	
	public void setCell_phone(String value) {
		this.cell_phone = value;
	}
	
	public String getCell_phone() {
		return cell_phone;
	}
	
	public void setCell_phone_prov(int value) {
		setCell_phone_prov(new Integer(value));
	}
	
	public void setCell_phone_prov(Integer value) {
		this.cell_phone_prov = value;
	}
	
	public Integer getCell_phone_prov() {
		return cell_phone_prov;
	}
	
	public void setEmail(String value) {
		this.email = value;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail_cc(String value) {
		this.email_cc = value;
	}
	
	public String getEmail_cc() {
		return email_cc;
	}
	
	public void setEmail_bcc(String value) {
		this.email_bcc = value;
	}
	
	public String getEmail_bcc() {
		return email_bcc;
	}
	
	public void setLang_id(int value) {
		setLang_id(new Integer(value));
	}
	
	public void setLang_id(Integer value) {
		this.lang_id = value;
	}
	
	public Integer getLang_id() {
		return lang_id;
	}
	
	public void setNotify_preference(int value) {
		setNotify_preference(new Integer(value));
	}
	
	public void setNotify_preference(Integer value) {
		this.notify_preference = value;
	}
	
	public Integer getNotify_preference() {
		return notify_preference;
	}
	
	public void setNotify_by_phone(char value) {
		setNotify_by_phone(new Character(value));
	}
	
	public void setNotify_by_phone(Character value) {
		this.notify_by_phone = value;
	}
	
	public Character getNotify_by_phone() {
		return notify_by_phone;
	}
	
	public void setNotify_by_phone_confirm(char value) {
		setNotify_by_phone_confirm(new Character(value));
	}
	
	public void setNotify_by_phone_confirm(Character value) {
		this.notify_by_phone_confirm = value;
	}
	
	public Character getNotify_by_phone_confirm() {
		return notify_by_phone_confirm;
	}
	
	public void setNotify_by_sms(char value) {
		setNotify_by_sms(new Character(value));
	}
	
	public void setNotify_by_sms(Character value) {
		this.notify_by_sms = value;
	}
	
	public Character getNotify_by_sms() {
		return notify_by_sms;
	}
	
	public void setNotify_by_sms_confirm(char value) {
		setNotify_by_sms_confirm(new Character(value));
	}
	
	public void setNotify_by_sms_confirm(Character value) {
		this.notify_by_sms_confirm = value;
	}
	
	public Character getNotify_by_sms_confirm() {
		return notify_by_sms_confirm;
	}
	
	public void setNotify_by_email(char value) {
		setNotify_by_email(new Character(value));
	}
	
	public void setNotify_by_email(Character value) {
		this.notify_by_email = value;
	}
	
	public Character getNotify_by_email() {
		return notify_by_email;
	}
	
	public void setNotify_by_email_confirm(char value) {
		setNotify_by_email_confirm(new Character(value));
	}
	
	public void setNotify_by_email_confirm(Character value) {
		this.notify_by_email_confirm = value;
	}
	
	public Character getNotify_by_email_confirm() {
		return notify_by_email_confirm;
	}
	
	public void setNotify_by_push_notif(char value) {
		setNotify_by_push_notif(new Character(value));
	}
	
	public void setNotify_by_push_notif(Character value) {
		this.notify_by_push_notif = value;
	}
	
	public Character getNotify_by_push_notif() {
		return notify_by_push_notif;
	}
	
	public void setNotify_phone_status(int value) {
		setNotify_phone_status(new Integer(value));
	}
	
	public void setNotify_phone_status(Integer value) {
		this.notify_phone_status = value;
	}
	
	public Integer getNotify_phone_status() {
		return notify_phone_status;
	}
	
	public void setNotify_sms_status(int value) {
		setNotify_sms_status(new Integer(value));
	}
	
	public void setNotify_sms_status(Integer value) {
		this.notify_sms_status = value;
	}
	
	public Integer getNotify_sms_status() {
		return notify_sms_status;
	}
	
	public void setNotify_email_status(int value) {
		setNotify_email_status(new Integer(value));
	}
	
	public void setNotify_email_status(Integer value) {
		this.notify_email_status = value;
	}
	
	public Integer getNotify_email_status() {
		return notify_email_status;
	}
	
	public void setNotify_push_notification_status(int value) {
		setNotify_push_notification_status(new Integer(value));
	}
	
	public void setNotify_push_notification_status(Integer value) {
		this.notify_push_notification_status = value;
	}
	
	public Integer getNotify_push_notification_status() {
		return notify_push_notification_status;
	}
	
	public void setInclude_audio_1(String value) {
		this.include_audio_1 = value;
	}
	
	public String getInclude_audio_1() {
		return include_audio_1;
	}
	
	public void setInclude_audio_2(String value) {
		this.include_audio_2 = value;
	}
	
	public String getInclude_audio_2() {
		return include_audio_2;
	}
	
	public void setInclude_audio_3(String value) {
		this.include_audio_3 = value;
	}
	
	public String getInclude_audio_3() {
		return include_audio_3;
	}
	
	public void setInclude_audio_4(String value) {
		this.include_audio_4 = value;
	}
	
	public String getInclude_audio_4() {
		return include_audio_4;
	}
	
	public void setInclude_audio_5(String value) {
		this.include_audio_5 = value;
	}
	
	public String getInclude_audio_5() {
		return include_audio_5;
	}
	
	public void setDo_not_notify(char value) {
		setDo_not_notify(new Character(value));
	}
	
	public void setDo_not_notify(Character value) {
		this.do_not_notify = value;
	}
	
	public Character getDo_not_notify() {
		return do_not_notify;
	}
	
	public void setComment(String value) {
		this.comment = value;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setDelete_flag(char value) {
		setDelete_flag(new Character(value));
	}
	
	public void setDelete_flag(Character value) {
		this.delete_flag = value;
	}
	
	public Character getDelete_flag() {
		return delete_flag;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getAttrib1() {
		return attrib1;
	}

	public void setAttrib1(String attrib1) {
		this.attrib1 = attrib1;
	}

	public String getAttrib2() {
		return attrib2;
	}

	public void setAttrib2(String attrib2) {
		this.attrib2 = attrib2;
	}

	public String getAttrib3() {
		return attrib3;
	}

	public void setAttrib3(String attrib3) {
		this.attrib3 = attrib3;
	}

	public String getAttrib4() {
		return attrib4;
	}

	public void setAttrib4(String attrib4) {
		this.attrib4 = attrib4;
	}

	public String getAttrib5() {
		return attrib5;
	}

	public void setAttrib5(String attrib5) {
		this.attrib5 = attrib5;
	}

	public String getAttrib6() {
		return attrib6;
	}

	public void setAttrib6(String attrib6) {
		this.attrib6 = attrib6;
	}

	public String getAttrib7() {
		return attrib7;
	}

	public void setAttrib7(String attrib7) {
		this.attrib7 = attrib7;
	}

	public String getAttrib8() {
		return attrib8;
	}

	public void setAttrib8(String attrib8) {
		this.attrib8 = attrib8;
	}

	public String getAttrib9() {
		return attrib9;
	}

	public void setAttrib9(String attrib9) {
		this.attrib9 = attrib9;
	}

	public String getAttrib10() {
		return attrib10;
	}

	public void setAttrib10(String attrib10) {
		this.attrib10 = attrib10;
	}

	public String getAttrib11() {
		return attrib11;
	}

	public void setAttrib11(String attrib11) {
		this.attrib11 = attrib11;
	}

	public String getAttrib12() {
		return attrib12;
	}

	public void setAttrib12(String attrib12) {
		this.attrib12 = attrib12;
	}

	public String getAttrib13() {
		return attrib13;
	}

	public void setAttrib13(String attrib13) {
		this.attrib13 = attrib13;
	}

	public String getAttrib14() {
		return attrib14;
	}

	public void setAttrib14(String attrib14) {
		this.attrib14 = attrib14;
	}

	public String getAttrib15() {
		return attrib15;
	}

	public void setAttrib15(String attrib15) {
		this.attrib15 = attrib15;
	}

	public String getAttrib16() {
		return attrib16;
	}

	public void setAttrib16(String attrib16) {
		this.attrib16 = attrib16;
	}

	public String getAttrib17() {
		return attrib17;
	}

	public void setAttrib17(String attrib17) {
		this.attrib17 = attrib17;
	}

	public String getAttrib18() {
		return attrib18;
	}

	public void setAttrib18(String attrib18) {
		this.attrib18 = attrib18;
	}

	public String getAttrib19() {
		return attrib19;
	}

	public void setAttrib19(String attrib19) {
		this.attrib19 = attrib19;
	}

	public String getAttrib20() {
		return attrib20;
	}

	public void setAttrib20(String attrib20) {
		this.attrib20 = attrib20;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}

