package com.telappoint.admin.appt.common.component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.telappoint.admin.appt.common.constants.PropertiesConstants;
import com.telappoint.admin.appt.common.model.EmailRequest;
import com.telappoint.admin.appt.common.util.CoreUtils;
import com.telappoint.admin.appt.common.util.PropertyUtils;
import com.telappoint.admin.appt.handlers.exception.TelAppointException;

/**
 * 
 * @author Balaji N
 *
 */
@Component
public class EmailComponent extends CommonComponent {
	
	private static Logger logger = Logger.getLogger(CoreUtils.class);
	
	@Async("mailExecutor")
	public void sendEmail(EmailRequest emailRequest, Map<String, String> data) {
		String calendarContent = null;
		try {
			if ("confirm".equals(emailRequest.getEmailType())) {
				logger.debug("Confirmation Mail:" + emailRequest.getToAddress());
				calendarContent = getConfirmEmailCalendarMessage(emailRequest, data);
				sendEmailICS(calendarContent, emailRequest);
			} else if ("cancel".equals(emailRequest.getEmailType())) {
				logger.info("Cancellation Mail:" + emailRequest.getToAddress());
				calendarContent = getCancelEmailCalendarMessage(emailRequest, data);
				sendEmailICS(calendarContent, emailRequest);
			} else if("error".equals(emailRequest.getEmailType())) {
				sendEmailInternal(emailRequest, null);
			} else if("other".equals(emailRequest.getEmailType())) {
				sendEmailInternal(emailRequest, null);
			}
		} catch (MailException | MessagingException | IOException  e) {
			logger.error("Error to send an email: " + e, e);
		}
	}

	public void sendEmailICS(String calendarContent, EmailRequest emailRequest) throws MailException, MessagingException, IOException {
		// calendar part
		BodyPart calendarPart = new MimeBodyPart();
		calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
		calendarPart.setContent(calendarContent, "text/calendar;method=" + emailRequest.getMethod());
		Multipart multipart = new MimeMultipart("alternative");
		multipart.addBodyPart(calendarPart);
		sendEmailInternal(emailRequest, multipart);
	}

	public void sendEmailInternal(EmailRequest emailRequest, Multipart multipart) throws MailException, IOException, MessagingException {
		if (multipart == null) {
			// email body part
			multipart = new MimeMultipart("alternative");
		}
		MimeBodyPart emailBodyPart = new MimeBodyPart();
		logger.debug("EmailBody:" + emailRequest.getEmailBody());
		emailBodyPart.setContent(emailRequest.getEmailBody(), "text/html");
		multipart.addBodyPart(emailBodyPart);

		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(PropertyUtils.getValueFromProperties("mail.smtp.hostname", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName()));
		sender.setPort(Integer.valueOf(PropertyUtils.getValueFromProperties("mail.smtp.port", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName())));
		sender.setUsername(PropertyUtils.getValueFromProperties("mail.smtp.user", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName()));
		sender.setPassword(PropertyUtils.getValueFromProperties("mail.smtp.password", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName()));
		sender.setJavaMailProperties(getSMTPMailProperties());
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(emailRequest.getToAddress());
		if (emailRequest.getCcAddresses() != null && emailRequest.getCcAddresses().length > 0) {
			helper.setCc(emailRequest.getCcAddresses());
		}
		helper.setFrom(emailRequest.getFromAddress());
		helper.setSubject(emailRequest.getSubject());
		helper.setText(emailRequest.getEmailBody(), true);
		message.setContent(multipart);
		sender.send(message);
	}

	/**
	 * Set the email preferences.
	 * @param logger
	 * @param emailRequest
	 * @throws TelAppointException
	 */
	public void setMailServerPreference(EmailRequest emailRequest) throws IOException {
		try {
			emailRequest.setFromAddress(PropertyUtils.getValueFromProperties("mail.fromaddress", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName()));
			emailRequest.setReplyAddress(PropertyUtils.getValueFromProperties("mail.replyaddress", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName()));
		} catch (IOException e) {
			logger.error("Error in getEmailRequest method. ", e);
			throw e;
		}
	}
	
	/**
	 * Set the email preferences.
	 * @param logger
	 * @param emailRequest
	 * @throws TelAppointException
	 */
	public void setErrorMailServerPreference(EmailRequest emailRequest) throws IOException {
		try {
			emailRequest.setFromAddress(PropertyUtils.getValueFromProperties("mail.fromaddress", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName()));
			emailRequest.setReplyAddress(PropertyUtils.getValueFromProperties("mail.replyaddress", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName()));
			emailRequest.setToAddress(PropertyUtils.getValueFromProperties("error.mail.to", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName()));
			String ccAddress = PropertyUtils.getValueFromProperties("error.mail.cc", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
			if(ccAddress != null && !"".equals(ccAddress.trim())) {
				String cc[] = new String[]{ccAddress};
				emailRequest.setCcAddresses(cc);
			}
		} catch (IOException e) {
			logger.error("Error in getEmailRequest method. ", e);
			throw e;
		}
	}

	private String getConfirmEmailCalendarMessage(EmailRequest emailRequest, Map<String, String> data) {
		String startDate = data.get("%STARTDATE%") == null ? "" : data.get("%STARTDATE%");
		String endDate = data.get("%ENDDATE%") == null ? "" : data.get("%ENDDATE%");
		StringBuilder calendarContent = new StringBuilder("BEGIN:VCALENDAR").append("\n");
		calendarContent.append("METHOD:").append(emailRequest.getMethod()).append("\n");
		calendarContent.append("PRODID: BCP - Meeting").append("\n");
		calendarContent.append("VERSION:2.0").append("\n");
		calendarContent.append("BEGIN:VEVENT").append("\n");
		calendarContent.append("DTSTART:").append(startDate).append("\n");
		calendarContent.append("DTEND:").append(endDate).append("\n");
		calendarContent.append("DTSTAMP:").append(startDate).append("\n");
		calendarContent.append("ORGANIZER:MAILTO:").append(emailRequest.getFromAddress()).append("\n");
		calendarContent.append("UID:").append(data.get("%SCHEDULEID%") == null ? "" : data.get("%SCHEDULEID%")).append("\n");
		calendarContent.append("CREATED:").append(startDate).append("\n");
		calendarContent.append("DESCRIPTION:").append(emailRequest.getEmailBody()).append("\n");
		calendarContent.append("LOCATION:").append(data.get("%LOCATIONNAME%") == null ? "" : data.get("%LOCATIONNAME%")).append(",");
		calendarContent.append(data.get("%CLIENTNAME%") == null ? "" : data.get("%CLIENTNAME%")).append("\n");
		calendarContent.append("SUMMARY:").append(emailRequest.getSubject()).append("\n");
		calendarContent.append("SEQUENCE:0").append("\n");
		calendarContent.append("PRIORITY:5").append("\n");
		calendarContent.append("CLASS:PUBLIC").append("\n");
		calendarContent.append("STATUS:").append(emailRequest.getStatus()).append("\n");
		calendarContent.append("TRANSP:OPAQUE").append("\n");
		calendarContent.append("END:VEVENT").append("\n");
		calendarContent.append("END:VCALENDAR");
		logger.debug("ConfirmEmail Calendar: " + calendarContent.toString());
		return calendarContent.toString();
	}

	private String getCancelEmailCalendarMessage(EmailRequest emailRequest, Map<String, String> data) {
		String startDate = data.get("%STARTDATE%") == null ? "" : data.get("%STARTDATE%");
		String endDate = data.get("%ENDDATE%") == null ? "" : data.get("%ENDDATE%");
		StringBuilder calendarContent = new StringBuilder("BEGIN:VCALENDAR").append("\n");
		calendarContent.append("PRODID: BCP - Meeting").append("\n");
		calendarContent.append("VERSION:2.0").append("\n");
		calendarContent.append("CALSCALE:GREGORIAN").append("\n");
		calendarContent.append("METHOD:CANCEL").append("\n");
		calendarContent.append("BEGIN:VEVENT").append("\n");
		calendarContent.append("DTSTART:").append(startDate).append("\n");
		calendarContent.append("DTEND:").append(endDate).append("\n");
		calendarContent.append("DTSTAMP:").append(startDate).append("\n");
		calendarContent.append("ORGANIZER:MAILTO:").append(emailRequest.getFromAddress()).append("\n");
		calendarContent.append("UID:").append(emailRequest.getFromAddress()).append("\n");
		calendarContent.append("CREATED:").append(startDate).append("\n");
		calendarContent.append("DESCRIPTION:").append(emailRequest.getEmailBody()).append("\n");
		calendarContent.append("LOCATION:").append(data.get("%LOCATIONNAME%") == null ? "" : data.get("%LOCATIONNAME%")).append(",");
		calendarContent.append(data.get("%CLIENTNAME%") == null ? "" : data.get("%CLIENTNAME%")).append("\n");
		calendarContent.append("SEQUENCE:0").append("\n");
		calendarContent.append("STATUS:").append(emailRequest.getStatus()).append("\n");
		calendarContent.append("SUMMARY:").append(emailRequest.getSubject()).append("\n");
		calendarContent.append("TRANSP:OPAQUE").append("\n");
		calendarContent.append("END:VEVENT").append("\n");
		calendarContent.append("END:VCALENDAR");
		logger.debug("CancelEmail Calendar: "+calendarContent.toString());
		return calendarContent.toString();
	}
	
	public String getDate(String timeZone, String langCode, String dateTime, String dateTimeFormat) {
		DateFormat sdfFormator = getSimpleDateFormat(langCode);
		DateFormat sdfParsor = getSimpleDateParser(timeZone, langCode, dateTimeFormat);
		String startDate = "";
		try {
			if (dateTime != null) {
				Date dateStr = sdfParsor.parse(dateTime);
				startDate = sdfFormator.format(dateStr);
			} else {
				startDate = dateTime;
			}
		} catch (ParseException pe) {
			logger.error("Date format or Parse is failed:" + pe, pe);
		}
		return startDate;
	}
	
	public DateFormat getSimpleDateParser(String timeZone, String langCode, String dateTimeFormat) {
		SimpleDateFormat sdfParsor;
		if ("us-es".equals(langCode)) {
			Locale locale = new Locale("es", "ES");
			sdfParsor = new SimpleDateFormat(dateTimeFormat, locale);
			sdfParsor.setTimeZone(TimeZone.getTimeZone(timeZone));
		} else {
			sdfParsor = new SimpleDateFormat(dateTimeFormat);
			sdfParsor.setTimeZone(TimeZone.getTimeZone(timeZone));
		}
		return sdfParsor;
	}
	
	public DateFormat getSimpleDateFormat(String langCode) {
		SimpleDateFormat sdfFormator;
		if ("us-es".equals(langCode)) {
			Locale locale = new Locale("es", "ES");
			sdfFormator = new SimpleDateFormat("yyyyMMdd'T'HHmm'00Z'", locale);
			sdfFormator.setTimeZone(TimeZone.getTimeZone("UTC"));
		} else {
			sdfFormator = new SimpleDateFormat("yyyyMMdd'T'HHmm'00Z'");
			sdfFormator.setTimeZone(TimeZone.getTimeZone("UTC"));
		}
		return sdfFormator;
	}


	public DateFormat getSimpleDateParser(Logger logger, String timeZone, String langCode, String dateTimeFormat) {
		SimpleDateFormat sdfParsor;
		if ("us-es".equals(langCode)) {
			Locale locale = new Locale("es", "ES");
			sdfParsor = new SimpleDateFormat(dateTimeFormat, locale);
			sdfParsor.setTimeZone(TimeZone.getTimeZone(timeZone));
		} else {
			sdfParsor = new SimpleDateFormat(dateTimeFormat);
			sdfParsor.setTimeZone(TimeZone.getTimeZone(timeZone));
		}
		return sdfParsor;
	}

	private Properties getSMTPMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "false");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.debug", "false");
		return properties;
	}

	public String getEmailBody(String emailBodyTemplate, Map<String, String> emailData) {
		return CoreUtils.replaceAllPlaceHolders(emailBodyTemplate, emailData);
	}

	public String getEmailSubject(String emailSubjectTemplate, Map<String, String> emailData) {
		return CoreUtils.replaceAllPlaceHolders(emailSubjectTemplate, emailData);
	}
}
