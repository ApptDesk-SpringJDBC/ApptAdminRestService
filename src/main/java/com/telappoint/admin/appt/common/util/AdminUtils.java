package com.telappoint.admin.appt.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.telappoint.admin.appt.common.constants.CommonApptDeskConstants;
import com.telappoint.admin.appt.common.constants.CommonDateContants;
import com.telappoint.admin.appt.common.model.Location;
import com.telappoint.admin.appt.common.model.Resource;

public class AdminUtils {

	/** The log. */
	private static Logger log = Logger.getLogger(AdminUtils.class);

	/**
	 * Format phone number.
	 * 
	 * @param ph
	 *            the phone
	 *
	 * @return the string
	 */
	public static String formatPhoneNumber(String ph) {
		StringBuffer buf = new StringBuffer("");

		if (ph == null) {
			buf.append("NA.");
		} else if (ph.length() == 7) {
			buf.append(ph.substring(0, 3));
			buf.append("-");
			buf.append(ph.substring(3, 7));
			ph = ph.substring(7);
		} else if (ph.length() == 10) {
			buf.append(ph.substring(0, 3));
			buf.append("-");
			buf.append(ph.substring(3, 6));
			buf.append("-");
			buf.append(ph.substring(6, 10));
		} else if (ph.length() > 10) {
			buf.append(ph.substring(0, 3));
			buf.append("-");
			buf.append(ph.substring(3, 6));
			buf.append("-");
			buf.append(ph.substring(6, 10));
			buf.append("Ext");
			buf.append(ph.substring(10));
		}

		return buf.toString();
	}

	/**
	 * Checks if is valid dob.
	 * 
	 * @param str
	 *            the str
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if is valid dob
	 */
	public static boolean isValidDOB(String str, String timeZone) {

		if (!isValidDate(str, "MM/dd/yyyy", timeZone))
			return false;

		try {

			GregorianCalendar dobGC = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
			sdf.setCalendar(dobGC);
			java.util.Date dob = sdf.parse(str);
			dobGC.setTime(dob);
			dobGC.set(Calendar.HOUR_OF_DAY, 0);
			dobGC.set(Calendar.MINUTE, 0);
			dobGC.set(Calendar.SECOND, 0);

			GregorianCalendar todateGC = new GregorianCalendar(TimeZone.getTimeZone(timeZone));

			if (dobGC.before(todateGC))
				return true;
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * This method checks to see if the supplied dateValue format matches the
	 * supplied dateFormat.
	 * 
	 * @param dateValue
	 *            the date value
	 * @param dateFormat
	 *            the date format
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if checks if is valid date
	 */
	public static boolean isValidDate(String dateValue, String dateFormat, String timeZone) {
		GregorianCalendar gcDate = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sim = new SimpleDateFormat(dateFormat);
		sim.setCalendar(gcDate);
		try {
			sim.setLenient(false);
			sim.parse(dateValue);
		} catch (Exception e) {
			System.out.println("Exception :" + e.toString());
			// e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Checks if is future date.
	 * 
	 * @param dateValue
	 *            the date value
	 * @param dateFormat
	 *            the date format
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if is future date
	 */
	public static boolean isFutureDate(String dateValue, String dateFormat, String timeZone) {
		GregorianCalendar gcToday = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		GregorianCalendar gcAppt = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sim = new SimpleDateFormat(dateFormat);
		sim.setCalendar(gcAppt);
		Date date = null;
		try {
			date = sim.parse(dateValue);
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
		}
		gcAppt.setTime(date);
		if (gcToday.before(gcAppt))
			return true;
		return false;
	}

	/**
	 * Checks if is future date.
	 * 
	 * @param dateValue1
	 *            the date value1
	 * @param dateValue2
	 *            the date value2
	 * @param dateFormat
	 *            the date format
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if is future date
	 */
	public static boolean isFutureDate(String dateValue1, String dateValue2, String dateFormat, String timeZone) {
		GregorianCalendar gcDate1 = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		GregorianCalendar gcDate2 = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sim = new SimpleDateFormat(dateFormat);
		sim.setCalendar(gcDate1);
		Date date1 = null;
		try {
			date1 = sim.parse(dateValue1);
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
		}
		gcDate1.setTime(date1);

		sim.setCalendar(gcDate2);
		Date date2 = null;
		try {
			date2 = sim.parse(dateValue2);
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
		}
		gcDate2.setTime(date2);

		if (gcDate1.after(gcDate2))
			return false;
		return true;
	}

	/**
	 * Checks if is valid time.
	 * 
	 * @param time
	 *            the time
	 * 
	 * @return true, if is valid time
	 */
	public static boolean isValidTime(String time) {
		int hour, min = -1;
		try {
			int indColon = time.indexOf(':');
			hour = Integer.parseInt(time.substring(0, indColon));
			String minutes = time.substring(indColon + 1);
			if (minutes.length() != 2)
				return false;
			min = Integer.parseInt(minutes);
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}
		if ((hour >= 0 && hour <= 12) && (min >= 0 && min <= 59))
			return true;
		return false;
	}

	/**
	 * Checks if is valid phone.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return true, if is valid phone
	 */
	public static boolean isValidPhone(String str) {
		if (str == null || str.length() != 10)
			return false;
		if (!isNumeric(str))
			return false;
		return true;
	}

	/**
	 * Checks if is valid phone.
	 * 
	 * @param str1
	 *            the str1
	 * @param str2
	 *            the str2
	 * 
	 * @return true, if is valid phone
	 */
	public static boolean isValidPhone(String str1, String str2) {
		if (str1 == null || str1.length() != 10)
			return false;
		if (!isNumeric(str1 + str2))
			return false;
		return true;
	}

	/**
	 * Checks if is valid ssn.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return true, if is valid ssn
	 */
	public static boolean isValidSSN(String str) {
		if (str == null || str.length() != 9)
			return false;
		if (!isNumeric(str))
			return false;
		return true;
	}

	// only numeric characters or empty string
	// no leading or trailing spaces
	// no alpha, space, special characters, minus, plus, null are allowed.
	/**
	 * Checks if is numeric.
	 * 
	 * @param instr
	 *            the instr
	 * 
	 * @return true, if is numeric
	 */
	public static boolean isNumeric(String instr) {
		if (instr == null)
			return false;
		try {
			char x[] = instr.toCharArray();
			for (int i = 0; i < x.length; i++) {
				if (!Character.isDigit(x[i]))
					return false;
			}
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Gets the date time display.
	 * 
	 * @param date
	 *            the date
	 * 
	 * @return the date time display
	 */
	public static String getDateTimeDisplay(String date) {
		String yy = date.substring(2, 4);
		String mm = date.substring(5, 7);
		String dd = date.substring(8, 10);
		String hh = date.substring(11, 13);
		String min = date.substring(14, 16);
		int hour = Integer.parseInt(hh);
		String ampm = "AM";
		if (hour >= 12)
			ampm = "PM";
		if (hour > 12)
			hour = hour - 12;
		String display = mm + "/" + dd + "/" + yy + "   " + hour + ":" + min + " " + ampm;
		return display;
	}

	/**
	 * Gets the date time display full.
	 * 
	 * @return the date time display full
	 */
	public static String getDateTimeDisplayWithSeconds(String dateTime) {
		String date = dateTime;
		String yy = date.substring(2, 4);
		String mm = date.substring(5, 7);
		String dd = date.substring(8, 10);
		String hh = date.substring(11, 13);
		String min = date.substring(14, 16);
		String sec = date.substring(17, 19);
		int hour = Integer.parseInt(hh);
		String ampm = "AM";
		if (hour >= 12)
			ampm = "PM";
		if (hour > 12)
			hour = hour - 12;
		String display = mm + "/" + dd + "/" + yy + "   " + hour + ":" + min + ":" + sec + " " + ampm;
		return display;
	}

	/**
	 * Gets the time display.
	 * 
	 * @param date
	 *            the date
	 * 
	 * @return the time display
	 */
	public static String getTimeDisplay(String date) {
		String hh = date.substring(11, 13);
		String min = date.substring(14, 16);
		int hour = Integer.parseInt(hh);
		String ampm = "AM";
		if (hour >= 12)
			ampm = "PM";
		if (hour > 12)
			hour = hour - 12;
		String display = hour + ":" + min + " " + ampm;
		return display;
	}

	/**
	 * Gets the date display.
	 * 
	 * @param date
	 *            the date
	 * 
	 * @return the date display
	 */
	public static String getDateDisplay(String date) {
		String yy = date.substring(2, 4);
		String mm = date.substring(5, 7);
		String dd = date.substring(8, 10);
		String display = mm + "/" + dd + "/" + yy;
		return display;
	}

	/**
	 * Format m_ d_ yyy yto m m_ d d_ yyyy.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return the string
	 */
	public static String formatM_D_YYYYtoMM_DD_YYYY(String str) {
		if (str.length() == 2)
			return new String("1900-01-01");// null date
		int mIndex = str.indexOf('/');
		int dIndex = str.indexOf('/', mIndex + 1);

		String m = str.substring(0, mIndex);
		String d = str.substring(mIndex + 1, dIndex);
		String y = str.substring(dIndex + 1);

		if (m.length() == 1)
			m = "0" + m;
		if (d.length() == 1)
			d = "0" + d;
		return new String(y + "-" + m + "-" + d);
	}

	/**
	 * Checks if is alpha numeric hypen space apostrophe period.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return true, if is alpha numeric hypen space apostrophe period
	 */
	public static boolean isAlphaNumericHypenSpaceApostrophePeriod(String str) {
		if (str == null)
			return false;
		try {
			char x[] = str.toCharArray();
			for (int i = 0; i < x.length; i++) {
				if (!(Character.isLetterOrDigit(x[i]) || Character.isSpaceChar(x[i]) || x[i] == '-' || x[i] == '\'' || x[i] == '.'))
					return false;
			}
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Format gc date to h h_ mm.
	 * 
	 * @param dateGC
	 *            the date gc
	 * 
	 * @return the string
	 */
	public static String formatGCDateToHH_MM(GregorianCalendar dateGC) {
		String dateString = "";
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		try {
			dateString = formatter.format(dateGC.getTime());
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
		}

		return dateString;
	}

	/**
	 * Format gc date to h h_ m m_ ss.
	 * 
	 * @param dateGC
	 *            the date gc
	 * 
	 * @return the string
	 */
	public static String formatGCDateToHH_MM_SS(GregorianCalendar dateGC) {
		String dateString = "";
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		try {
			dateString = formatter.format(dateGC.getTime());
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
		}

		return dateString;
	}

	/**
	 * Format yyy y_ m m_ d d_to_ m m_ d d_ yyyy.
	 * 
	 * @param date
	 *            the date
	 * 
	 * @return the string
	 */
	public static String formatYYYY_MM_DD_to_MM_DD_YYYY(String date) {
		String y = date.substring(0, 4);
		String m = date.substring(5, 7);
		String d = date.substring(8, 10);
		String dt = m + "/" + d + "/" + y;
		return dt;
	}

	/**
	 * Format m m_ d d_ yyy y_to_ gc.
	 * 
	 * @param date
	 *            the date
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the gregorian calendar
	 */
	public static GregorianCalendar formatMM_DD_YYYY_to_GC(String date, String timeZone) {
		GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		int m = Integer.parseInt(date.substring(0, 2));
		int d = Integer.parseInt(date.substring(3, 5));
		int y = Integer.parseInt(date.substring(6, 10));
		gc.set(Calendar.YEAR, y);
		gc.set(Calendar.MONTH, m - 1);
		gc.set(Calendar.DATE, d);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		return gc;
	}

	/**
	 * New format yyy y_ m m_ d d_to_ m m_ d d_ yyyy.
	 * 
	 * @param date
	 *            the date
	 * 
	 * @return the string
	 */
	public static String newFormatYYYY_MM_DD_to_MM_DD_YYYY(String date) {
		String y = Integer.toString(Integer.parseInt(date.substring(0, 4)));
		String m = Integer.toString(Integer.parseInt(date.substring(5, 7)));
		String d = Integer.toString(Integer.parseInt(date.substring(8, 10)));

		if (m.length() == 1)
			m = "0" + m;
		if (d.length() == 1)
			d = "0" + d;
		String dt = m + "/" + d + "/" + y;
		return dt;
	}

	/**
	 * Gets the current date.
	 * 
	 * @param format
	 *            the format
	 * 
	 * @return the current date
	 */
	public static String getCurrentDate(String format) {
		String dateString = "";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		java.util.Date currentDate = new java.util.Date();
		try {
			dateString = formatter.format(currentDate);
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
		}
		return dateString;
	}// method

	/**
	 * Checks if is valid zip.
	 * 
	 * @param zipstr
	 *            the zipstr
	 * 
	 * @return true, if is valid zip
	 */
	public static boolean isValidZip(String zipstr) {
		zipstr = zipstr.trim();
		zipstr = removeNonDigits(zipstr);
		int len = zipstr.length();
		if (!(len == 5 || len == 9))
			return false;
		try {
			Long.parseLong(zipstr);
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Removes the non digits.
	 * 
	 * @param input
	 *            the input
	 * 
	 * @return the string
	 */
	public static String removeNonDigits(String input) {
		StringBuffer sb = new StringBuffer("");
		if (input != null) {
			char letter;
			for (int i = 0; i < input.length(); i++) {
				letter = input.charAt(i);
				if (Character.isDigit(letter)) {
					sb.append(letter);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Checks if is valid postal.
	 * 
	 * @param postalstr
	 *            the postalstr
	 * 
	 * @return true, if is valid postal
	 */
	public static boolean isValidPostal(String postalstr) {
		postalstr = postalstr.trim();
		postalstr = removeWhiteSpaces(postalstr);
		int len = postalstr.length();
		if (!(len == 6))
			return false;
		try {
			Long.parseLong(postalstr);
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
			return true;
		}
		return false;
	}

	/**
	 * Removes the white spaces.
	 * 
	 * @param input
	 *            the input
	 * 
	 * @return the string
	 */
	public static String removeWhiteSpaces(String input) {
		StringBuffer sb = new StringBuffer("");
		if (input != null) {
			char letter;
			for (int i = 0; i < input.length(); i++) {
				letter = input.charAt(i);
				if (!Character.isSpaceChar(letter)) {
					sb.append(letter);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Removes the non letter digit.
	 * 
	 * @param input
	 *            the input
	 * 
	 * @return the string
	 */
	public static String removeNonLetterDigit(String input) {
		StringBuffer sb = new StringBuffer("");
		if (input != null) {
			char letter;
			for (int i = 0; i < input.length(); i++) {
				letter = input.charAt(i);
				if (Character.isLetterOrDigit(letter)) {
					sb.append(letter);
				}
			}
		}
		return sb.toString();
	}

	public static String removeSpecialCharactersForName(String input) {
		StringBuffer sb = new StringBuffer("");
		if (input != null) {
			char letter;
			for (int i = 0; i < input.length(); i++) {
				letter = input.charAt(i);
				if (letter == '&') {
					sb.append(" and ");
				} else if (letter == '(' || letter == ')' || letter == '#' || letter == '?') {
					sb.append("");
				}
				// else if(letter == '\'') {
				// sb.append("\'\'");
				// }
				else {
					sb.append(letter);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Checks if is valid email.
	 * 
	 * @param email
	 *            the email
	 * 
	 * @return true, if is valid email
	 */
	public static boolean isValidEmail(String email) {
		if (email == null)
			return false;
		email = email.trim();
		if (email.length() == 0)
			return true;// blank emails are allowed!

		for (int i = 0; i < email.length(); i++) {
			char ch = email.charAt(i);
			if (!((Character.isLetterOrDigit(ch)) || (ch == '@') || (ch == '.') || (ch == '_') || (ch == '-')))
				return false;
		}

		StringTokenizer tok1 = new StringTokenizer(email, "@");
		int count = 0;
		String str = "";
		while (tok1.hasMoreTokens()) {
			str = tok1.nextToken();
			if (str.trim().length() == 0)
				return false;
			++count;
		}
		if (count != 2)
			return false;

		StringTokenizer tok2 = new StringTokenizer(str, ".");
		count = 0;
		while (tok2.hasMoreTokens()) {
			str = tok2.nextToken();
			if (str.trim().length() == 0)
				return false;
			++count;
		}
		if (count < 2)
			return false;

		return true;
	}

	/**
	 * Checks if is alpha hypen space apostrophe period.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return true, if is alpha hypen space apostrophe period
	 */
	public static boolean isAlphaHypenSpaceApostrophePeriod(String str) {
		if (str == null)
			return false;
		try {
			char x[] = str.toCharArray();
			for (int i = 0; i < x.length; i++) {
				if (!(Character.isLetter(x[i]) || Character.isSpaceChar(x[i]) || x[i] == '-' || x[i] == '\'' || x[i] == '.'))
					return false;
			}
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Checks if is alpha.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return true, if is alpha
	 */
	public static boolean isAlpha(String str) {
		if ((str == null) || str.length() == 0)
			return false;
		try {
			char x[] = str.toCharArray();
			for (int i = 0; i < x.length; i++) {
				if (!(Character.isLetter(x[i])))
					return false;
			}
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Checks if is all digits.
	 * 
	 * @param s
	 *            the s
	 * @param exceptChars
	 *            the except chars
	 * 
	 * @return true, if is all digits
	 */
	public static boolean isAllDigits(String s, String exceptChars) {
		boolean retval = true;

		for (int i = 0; i < s.length(); i++) {
			// Check that current character is number.
			char c = s.charAt(i);
			if (!((c >= '0' && c <= '9') || (exceptChars.indexOf(c) != -1)))
				retval = false;
		}
		// All characters are numbers.
		return retval;
	}

	/**
	 * Gets the display working hours.
	 * 
	 * @param st1
	 *            the st1
	 * @param et1
	 *            the et1
	 * @param st2
	 *            the st2
	 * @param et2
	 *            the et2
	 * 
	 * @return the display working hours
	 */
	public static String getDisplayWorkingHours(String st1, String et1, String st2, String et2) {
		StringBuffer retval = new StringBuffer();
		if (st1 == null || et1 == null || st2 == null || et2 == null) {
			retval.append("NA");
		} else {
			retval.append(trimTime(st1));
			retval.append("-");
			retval.append(trimTime(et2));
		}
		return retval.toString();
	}

	/**
	 * Gets the display break hours.
	 * 
	 * @param st1
	 *            the st1
	 * @param et1
	 *            the et1
	 * @param st2
	 *            the st2
	 * @param et2
	 *            the et2
	 * 
	 * @return the display break hours
	 */
	public static String getDisplayBreakHours(String st1, String et1, String st2, String et2) {
		StringBuffer retval = new StringBuffer();
		if (st1 == null || et1 == null || st2 == null || et2 == null) {
		} else {
			if (st2.equals(et1)) {
				// Zero break time - do not display anything
			} else {
				retval.append(trimTime(et1));
				retval.append("-");
				retval.append(trimTime(st2));
			}
		}
		return retval.toString();
	}

	/**
	 * Trim time.
	 * 
	 * @param time
	 *            the time
	 * 
	 * @return the string
	 */
	public static String trimTime(String time) {
		// hh:mm:ss
		StringBuffer retval = new StringBuffer();
		String hh = time.substring(0, 2);
		String mm = time.substring(3, 5);
		int hours = Integer.valueOf(hh).intValue();
		int mins = Integer.valueOf(mm).intValue();

		if (hours > 12) {
			retval.append(Integer.toString(hours - 12));
		} else {
			retval.append(Integer.toString(hours));
		}
		if (mins > 0) {
			retval.append(":");
			retval.append(mm);
		}
		return retval.toString();
	}

	/**
	 * Gets the time hr.
	 * 
	 * @param time
	 *            the time
	 * 
	 * @return the time hr
	 */
	public static String getTimeHr(String time) {
		String hh = time.substring(0, 2);
		int hours = Integer.valueOf(hh).intValue();
		if (hours > 12)
			hours -= 12;
		return Integer.toString(hours);
	}

	/**
	 * Gets the time min.
	 * 
	 * @param time
	 *            the time
	 * 
	 * @return the time min
	 */
	public static String getTimeMin(String time) {
		String mm = time.substring(3, 5);
		int min = Integer.valueOf(mm).intValue();
		return Integer.toString(min);
	}

	/**
	 * Gets the time am pm.
	 * 
	 * @param time
	 *            the time
	 * 
	 * @return the time am pm
	 */
	public static String getTimeAmPm(String time) {
		String retval;
		String hh = time.substring(0, 2);
		int hours = Integer.valueOf(hh).intValue();
		if (hours >= 12) {
			retval = new String("PM");
		} else {
			retval = new String("AM");
		}
		return retval;
	}

	/**
	 * Gets the hHMM diff in min.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * 
	 * @return the hHMM diff in min
	 */
	public static int getHHMMDiffInMin(String start, String end) {
		String hhStart = start.substring(0, 2);
		String mmStart = start.substring(3, 5);
		String hhEnd = end.substring(0, 2);
		String mmEnd = end.substring(3, 5);

		int hhs = Integer.valueOf(hhStart).intValue();
		int mms = Integer.valueOf(mmStart).intValue();
		int hhe = Integer.valueOf(hhEnd).intValue();
		int mme = Integer.valueOf(mmEnd).intValue();

		return ((hhe * 60 + mme) - (hhs * 60 + mms));
	}

	/**
	 * Checks if is empty.
	 * 
	 * @param string
	 *            the string
	 * 
	 * @return true, if is empty
	 */
	public static boolean isEmpty(String string) {
		return ((string == null) || string.equals(""));
	}

	/**
	 * Checks if is valid name.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return true, if is valid name
	 */
	public static boolean isValidName(String str) {
		return (AdminUtils.isAlphaHypenSpaceApostrophePeriod(str));
	}

	/**
	 * Checks if is valid pin.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return true, if is valid pin
	 */
	public static boolean isValidPin(String str) {
		if (str.length() < 4 || str.length() > 8)
			return false;
		return (AdminUtils.isNumeric(str));
	}

	/**
	 * Format hhmmamp m_to_ hhmmss.
	 * 
	 * @param hh
	 *            the hh
	 * @param mm
	 *            the mm
	 * @param amPm
	 *            the am pm
	 * 
	 * @return the string
	 */
	public static String formatHHMMAMPM_to_HHMMSS(String hh, String mm, String amPm) {
		StringBuffer sb = new StringBuffer();
		int hour = Integer.valueOf(hh).intValue();
		int min = Integer.valueOf(mm).intValue();

		// GregorianCalendar dateGC = new
		// GregorianCalendar(TimeZone.getTimeZone(timeZone));
		if ("PM".equals(amPm) && (hour != 12)) {
			hour += 12;
		}
		// dateGC.set(Calendar.HOUR_OF_DAY,hour);
		// dateGC.set(Calendar.MINUTE,min);
		// dateGC.set(Calendar.SECOND,0);
		String prependHour = "";
		String prependMin = "";
		if (hour < 10)
			prependHour = "0";
		if (min < 10)
			prependMin = "0";

		sb.append(prependHour + hour + ":" + prependMin + min + ":" + "00");
		return sb.toString();
	}

	/**
	 * Adds the min to_ hhm m_ ampm.
	 * 
	 * @param hh
	 *            the hh
	 * @param mm
	 *            the mm
	 * @param amPm
	 *            the am pm
	 * @param minutes
	 *            the minutes
	 * 
	 * @return the string
	 */
	public static String addMinTo_HHMM_AMPM(String hh, String mm, String amPm, int minutes) {
		StringBuffer sb = new StringBuffer();
		int hour = Integer.valueOf(hh).intValue();
		int min = Integer.valueOf(mm).intValue();

		// GregorianCalendar dateGC = new
		// GregorianCalendar(TimeZone.getTimeZone(timeZone));

		if ("PM".equals(amPm) && (hour != 12)) {
			hour += 12;
		}
		// dateGC.set(Calendar.HOUR_OF_DAY,hour);
		// dateGC.set(Calendar.MINUTE,min);
		// dateGC.set(Calendar.SECOND,0);
		// dateGC.add(Calendar.MINUTE, minutes);
		int totalMinutes = hour * 60 + min + minutes;
		hour = totalMinutes / 60;
		min = totalMinutes % 60;

		String prependHour = "";
		String prependMin = "";
		if (hour < 10)
			prependHour = "0";
		if (min < 10)
			prependMin = "0";

		sb.append(prependHour + hour + ":" + prependMin + min + ":" + "00");

		return sb.toString();
	}

	/**
	 * Adds the min to_ hhmm.
	 * 
	 * @param time
	 *            the time
	 * @param minutes
	 *            the minutes
	 * 
	 * @return the string
	 */
	public static String addMinTo_HHMM(String time, int minutes) {
		StringBuffer sb = new StringBuffer();
		int hour = Integer.valueOf(time.substring(0, 2)).intValue();
		int min = Integer.valueOf(time.substring(3, 5)).intValue();

		int totalMinutes = hour * 60 + min + minutes;
		hour = totalMinutes / 60;
		min = totalMinutes % 60;

		String prependHour = "";
		String prependMin = "";
		if (hour < 10)
			prependHour = "0";
		if (min < 10)
			prependMin = "0";

		sb.append(prependHour + hour + ":" + prependMin + min);

		return sb.toString();
	}

	/**
	 * Checks if is time within boundary.
	 * 
	 * @param gcDate
	 *            the gc date
	 * @param st1
	 *            the st1
	 * @param et1
	 *            the et1
	 * @param st2
	 *            the st2
	 * @param et2
	 *            the et2
	 * 
	 * @return true, if is time within boundary
	 */
	public static boolean isTimeWithinBoundary(GregorianCalendar gcDate, String st1, String et1, String st2, String et2) {
		boolean retval = true;
		String apptTime = AdminUtils.formatGCDateToHH_MM_SS(gcDate);
		if (apptTime != null && st1 != null && et1 != null && st2 != null && et2 != null) {
			if (AdminUtils.compareTime(apptTime, st1) == -1)
				retval = false; // apptTime < st1
			else if ((AdminUtils.compareTime(apptTime, et2) == 1))
				retval = false; // apptTime > et2
			else if ((AdminUtils.compareTime(apptTime, et1) == 1) && AdminUtils.compareTime(apptTime, st2) == -1)
				retval = false; // apptTime > et1 && apptTime < st2
		}
		// log.info("==> apptTime " + apptTime+" st1 " + st1+" et1 " +
		// et1+" st2 " + st2+" et2 " + et2+" retval " + retval);
		return retval;
	}

	/**
	 * Compare time.
	 * 
	 * @param time1
	 *            the time1
	 * @param time2
	 *            the time2
	 * 
	 * @return the int
	 */
	public static int compareTime(String time1, String time2) {
		int retval = -2;
		if (time1 == null && time2 == null) {
			retval = 0;
		} else if (time1 != null && time2 != null) {
			// hh:mm:ss
			int hour1 = Integer.valueOf(time1.substring(0, 2)).intValue();
			int min1 = Integer.valueOf(time1.substring(3, 5)).intValue();
			int sec1 = Integer.valueOf(time1.substring(6, 8)).intValue();

			// hh:mm:ss
			int hour2 = Integer.valueOf(time2.substring(0, 2)).intValue();
			int min2 = Integer.valueOf(time2.substring(3, 5)).intValue();
			int sec2 = Integer.valueOf(time2.substring(6, 8)).intValue();

			int timeInSeconds1 = hour1 * 60 * 60 + min1 * 60 + sec1;
			int timeInSeconds2 = hour2 * 60 * 60 + min2 * 60 + sec2;

			if (timeInSeconds1 == timeInSeconds2)
				retval = 0;
			else if (timeInSeconds1 < timeInSeconds2)
				retval = -1;
			else
				retval = 1;
		}
		return retval;
	}

	/**
	 * Checks if is open closed day consistent.
	 * 
	 * @param resourceTime
	 *            the resource time
	 * @param locationTime
	 *            the location time
	 * 
	 * @return true, if is open closed day consistent
	 */
	public static boolean isOpenClosedDayConsistent(String resourceTime, String locationTime) {
		if ((locationTime == null) && (resourceTime != null))
			return false;
		else
			return true;
	}

	/**
	 * Checks if is timing within boundary.
	 * 
	 * @param st1
	 *            the st1
	 * @param et1
	 *            the et1
	 * @param st2
	 *            the st2
	 * @param et2
	 *            the et2
	 * 
	 * @return true, if is timing within boundary
	 */
	public static boolean isTimingWithinBoundary(String st1, String et1, String st2, String et2) {
		boolean retval = true;
		if (st1 != null && et1 != null && st2 != null && et2 != null) {
			if (AdminUtils.compareTime(st1, st2) == -1)
				retval = false;// st1 less than st2
			if (AdminUtils.compareTime(et1, et2) == 1)
				retval = false;// et1 greater than et2
		}
		return retval;
	}

	/**
	 * Gets the location id.
	 * 
	 * @param locationList
	 *            the location list
	 * @param location
	 *            the location
	 * 
	 * @return the location id
	 */
	public static int getLocationId(List<Location> locationList, String location) {
		int retval = -1;
		for (int i = 0; i < locationList.size(); i++) {
			if (location.equals((locationList.get(i)).getLocationNameOnline())) {
				retval = (locationList.get(i)).getLocationId();
				break;
			}
		}
		return retval;
	}

	/**
	 * Gets the date specific time slots.
	 * 
	 * @param GCDate
	 *            the gC date
	 * @param st1
	 *            the st1
	 * @param et1
	 *            the et1
	 * @param st2
	 *            the st2
	 * @param et2
	 *            the et2
	 * @param dayStartTime
	 *            the day start time
	 * @param dayEndTime
	 *            the day end time
	 * @param apptBlockTimeInMins
	 *            the appt block time in mins
	 * 
	 * @return the date specific time slots
	 */
	public static List<String> getDateSpecificTimeSlots(GregorianCalendar GCDate, String st1, String et1, String st2, String et2, String dayStartTime, String dayEndTime,
			int apptBlockTimeInMins) {
		List<String> timeSlotList = null;
		return timeSlotList;
	}

	/**
	 * Gets the block time slots.
	 * 
	 * @param dateTime
	 *            the date time
	 * @param blockSize
	 *            the block size
	 * @param apptBlockTimeInMins
	 *            the appt block time in mins
	 * 
	 * @return the block time slots
	 */
	public static List<String> getBlockTimeSlots(String dateTime, int blockSize, int apptBlockTimeInMins) {
		List<String> timeSlotList = new ArrayList<String>();
		try {
			GregorianCalendar gcIndex = AdminUtils.formatSqlStringToGC(dateTime);
			for (int i = 0; i < blockSize; i++) {
				String timeSlot = new String();
				timeSlot = formatGCDateToYYYYMMDD(gcIndex) + " " + formatGCDateToHH_MM_SS(gcIndex);
				timeSlotList.add(timeSlot);
				gcIndex.add(Calendar.MINUTE, apptBlockTimeInMins);
			}
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
			return null;
		}
		return timeSlotList;
	}

	/**
	 * Gets the day name.
	 * 
	 * @param i
	 *            the i
	 * 
	 * @return the day name
	 */
	public static String getDayName(int i) {
		String day = null;
		switch (i) {
		case 1:
			day = "Sunday";
			break;
		case 2:
			day = "Monday";
			break;
		case 3:
			day = "Tuesday";
			break;
		case 4:
			day = "Wednesday";
			break;
		case 5:
			day = "Thursday";
			break;
		case 6:
			day = "Friday";
			break;
		case 7:
			day = "Saturday";
			break;
		}

		return day;

	}

	/**
	 * Gets the calendar last day.
	 * 
	 * @param resourceCalendarMonths
	 *            the resource calendar months
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the calendar last day
	 */
	public static GregorianCalendar getCalendarLastDay(int resourceCalendarMonths, String timeZone) {
		GregorianCalendar gcLastDay = AdminUtils.getFirstSunday(AdminUtils.getCurrentDate("MM/dd/yyyy", timeZone), timeZone);
		gcLastDay.add(Calendar.MONTH, resourceCalendarMonths);
		String calendarLastDay = AdminUtils.formatGCDateToMMDDYYYY(gcLastDay);
		gcLastDay = AdminUtils.getFirstSaturday(calendarLastDay, timeZone);
		return gcLastDay;
	}

	/**
	 * Gets the calendar header.
	 * 
	 * @param day
	 *            the day
	 * @param date
	 *            the date
	 * 
	 * @return the calendar header
	 */
	public static String getCalendarHeader(String day, String date) {
		StringBuffer sb = new StringBuffer("");
		sb.append("<th width=\"14%\">" + day + "<br>");
		sb.append("<span class=\"");
		sb.append("nobold\">");
		sb.append(date);
		sb.append("</span></th>");
		return sb.toString();
	}

	/**
	 * Gets the calendar left css.
	 * 
	 * @param time
	 *            the time
	 * @param apptBlockTimeInMins
	 *            the appt block time in mins
	 * 
	 * @return the calendar left css
	 */
	public static String getCalendarLeftCSS(String time, int apptBlockTimeInMins) {
		StringBuffer sb = new StringBuffer("");
		sb.append("<td class=\"");
		if (!wholeHour(time, apptBlockTimeInMins))
			sb.append("dotdiv rline");
		else
			sb.append("solid rline");
		sb.append("\">");
		sb.append(time);
		sb.append("</td>");
		return sb.toString();
	}

	/**
	 * Whole hour.
	 * 
	 * @param time
	 *            the time
	 * @param apptBlockTimeInMins
	 *            the appt block time in mins
	 * 
	 * @return true, if successful
	 */
	public static boolean wholeHour(String time, int apptBlockTimeInMins) {// 8:00,
																			// 9:00,
																			// 10:00,
																			// 11:00,
																			// 12:00
																			// etc
		boolean result = false;
		int mins = 60 - apptBlockTimeInMins;
		String minutes = String.valueOf(mins);
		if (time.endsWith(minutes))
			result = true; // To split block by block
		return result;
	}

	// to find day from week
	/**
	 * Gets the week day.
	 * 
	 * @param d
	 *            the d
	 * 
	 * @return the week day
	 */
	public static String getWeekDay(int d) {
		String day = null;
		switch (d) {
		case Calendar.SUNDAY:
			day = "Sun";
			break;
		case Calendar.MONDAY:
			day = "Mon";
			break;
		case Calendar.TUESDAY:
			day = "Tue";
			break;
		case Calendar.WEDNESDAY:
			day = "Wed";
			break;
		case Calendar.THURSDAY:
			day = "Thu";
			break;
		case Calendar.FRIDAY:
			day = "Fri";
			break;
		case Calendar.SATURDAY:
			day = "Sat";
			break;
		}
		return day;
	}

	// To get name format for J.Doe [ie,First name ]
	/**
	 * Gets the name format.
	 * 
	 * @param firstName
	 *            the first name
	 * 
	 * @return the name format
	 */
	public static String getNameFormat(String firstName) {
		StringBuffer name = new StringBuffer();

		try {
			if (firstName == null || firstName.equals("")) {
				name.append(" ");
			} else {
				name.append(firstName.toUpperCase().charAt(0));
				name.append(". ");
			}
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
		}
		return name.toString();
	}

	/**
	 * Checks if is within calendar.
	 * 
	 * @param sdate
	 *            the sdate
	 * @param edate
	 *            the edate
	 * @param date
	 *            the date
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if is within calendar
	 */
	public static boolean isWithinCalendar(String sdate, String edate, String date, String timeZone) {
		boolean result = true;
		GregorianCalendar startGC = AdminUtils.formatMM_DD_YYYY_to_GC(sdate, timeZone);
		GregorianCalendar endGC = AdminUtils.formatMM_DD_YYYY_to_GC(edate, timeZone);
		GregorianCalendar dateGC = AdminUtils.formatMM_DD_YYYY_to_GC(date, timeZone);

		if (dateGC.after(endGC) || dateGC.before(startGC)) {
			result = false;
		}
		return result;
	}

	/**
	 * Checks if is previous week.
	 * 
	 * @param sdate
	 *            the sdate
	 * @param edate
	 *            the edate
	 * @param date
	 *            the date
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if is previous week
	 */
	public static boolean isPreviousWeek(String sdate, String edate, String date, String timeZone) {
		boolean result = false;
		date = AdminUtils.addDays(date, -7, timeZone);
		result = isWithinCalendar(sdate, edate, date, timeZone);
		return result;
	}

	/**
	 * Checks if is next week.
	 * 
	 * @param sdate
	 *            the sdate
	 * @param edate
	 *            the edate
	 * @param date
	 *            the date
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if is next week
	 */
	public static boolean isNextWeek(String sdate, String edate, String date, String timeZone) {
		boolean result = false;
		date = AdminUtils.addDays(date, 7, timeZone);
		result = isWithinCalendar(sdate, edate, date, timeZone);
		return result;
	}

	/**
	 * Checks if is next month.
	 * 
	 * @param sdate
	 *            the sdate
	 * @param edate
	 *            the edate
	 * @param date
	 *            the date
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if is next month
	 */
	public static boolean isNextMonth(String sdate, String edate, String date, String timeZone) {
		boolean result = false;
		date = AdminUtils.addMonths(date, 1, timeZone);
		date = AdminUtils.setDaysToDate(date, 01, timeZone);
		result = isWithinCalendar(sdate, edate, date, timeZone);
		return result;
	}

	/**
	 * Checks if is previous month.
	 * 
	 * @param sdate
	 *            the sdate
	 * @param edate
	 *            the edate
	 * @param date
	 *            the date
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if is previous month
	 */
	public static boolean isPreviousMonth(String sdate, String edate, String date, String timeZone) {
		boolean result = false;
		date = AdminUtils.addMonths(date, -1, timeZone);
		date = AdminUtils.setDaysToDate(date, 01, timeZone);
		result = isWithinCalendar(sdate, edate, date, timeZone);
		return result;
	}

	/**
	 * Gets the diff in days.
	 * 
	 * @param sdate
	 *            the sdate
	 * @param edate
	 *            the edate
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the diff in days
	 */
	public static int getDiffInDays(String sdate, String edate, String timeZone) {
		int dateDiff = 0;
		String sdateStr = AdminUtils.formatYYYY_MM_DD_to_MM_DD_YYYY(sdate);
		String edateStr = AdminUtils.formatYYYY_MM_DD_to_MM_DD_YYYY(edate);
		GregorianCalendar startGC = AdminUtils.formatMM_DD_YYYY_to_GC(sdateStr, timeZone);
		GregorianCalendar endGC = AdminUtils.formatMM_DD_YYYY_to_GC(edateStr, timeZone);
		long ldate1 = startGC.getTimeInMillis();
		long ldate2 = endGC.getTimeInMillis();
		// Use integer calculation, truncate the decimals
		int hr1 = (int) (ldate1 / 3600000); // 60*60*1000
		int hr2 = (int) (ldate2 / 3600000);
		int days1 = (int) hr1 / 24;
		int days2 = (int) hr2 / 24;
		if (days2 > days1) {
			dateDiff = days2 - days1;
		} else {
			dateDiff = days1 - days2;
		}
		return dateDiff;
	}

	/**
	 * Gets the month.
	 * 
	 * @param gc
	 *            the gc
	 * 
	 * @return the month
	 */
	public static String getMonth(GregorianCalendar gc) {
		String month = null;

		switch (gc.get(Calendar.MONTH)) {
		case Calendar.JANUARY:
			month = "January";
			break;
		case Calendar.FEBRUARY:
			month = "February";
			break;
		case Calendar.MARCH:
			month = "March";
			break;
		case Calendar.APRIL:
			month = "April";
			break;
		case Calendar.MAY:
			month = "May";
			break;
		case Calendar.JUNE:
			month = "June";
			break;
		case Calendar.JULY:
			month = "July";
			break;
		case Calendar.AUGUST:
			month = "August";
			break;
		case Calendar.SEPTEMBER:
			month = "September";
			break;
		case Calendar.OCTOBER:
			month = "October";
			break;
		case Calendar.NOVEMBER:
			month = "November";
			break;
		case Calendar.DECEMBER:
			month = "December";
			break;

		}
		return month;
	}

	/**
	 * Gets the 2nd default working hour.
	 * 
	 * @param dateGC
	 *            the date gc
	 * @param resourceId
	 *            the resource id
	 * @param secondDefaultResourceWorkingHourList
	 *            the second default resource working hour list
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the 2nd default working hour
	 */
	public static int get2ndDefaultWorkingHour(GregorianCalendar dateGC, int resourceId, List<String> secondDefaultResourceWorkingHourList, String timeZone) {
		int index = -1;
		return index;
	}

	/**
	 * Gets the specific date working hour.
	 * 
	 * @param dateGC
	 *            the date gc
	 * @param resourceId
	 *            the resource id
	 * @param specificDateWorkingHourList
	 *            the specific date working hour list
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the specific date working hour
	 */
	public static int getSpecificDateWorkingHour(GregorianCalendar dateGC, int resourceId, List<String> specificDateWorkingHourList, String timeZone) {
		return 0;
	}

	/**
	 * Apply multi block filter.
	 * 
	 * @param calendarTimeList
	 *            the calendar time list
	 * @param blockSize
	 *            the block size
	 * @param apptSlotsPerDay
	 *            the appt slots per day
	 * 
	 * @return the array list
	 */
	public static List<String> applyMultiBlockFilter(List<String> calendarTimeList, int blockSize, int apptSlotsPerDay) {
		return null;
	}

	/**
	 * Format name.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the string
	 */
	public static String formatName(String name) {
		StringBuffer result = new StringBuffer();
		StringTokenizer st = new StringTokenizer(name, " ");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			token = token.trim();
			if (token.toUpperCase().equals(token)) {
				token = token.toLowerCase();// all upper case chars in name
			}
			result.append(token.substring(0, 1).toUpperCase() + token.substring(1));
			if (st.hasMoreTokens())
				result.append(" ");
		}
		return result.toString();
	}

	/**
	 * Checks if is past date.
	 * 
	 * @param dateValue
	 *            the date value
	 * @param dateFormat
	 *            the date format
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if is past date
	 */
	public static boolean isPastDate(String dateValue, String dateFormat, String timeZone) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		GregorianCalendar gcAppt = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		format.setCalendar(gcAppt);
		gcAppt.set(Calendar.HOUR_OF_DAY, 0);
		gcAppt.set(Calendar.MINUTE, 0);
		gcAppt.set(Calendar.SECOND, 0);
		gcAppt.set(Calendar.MILLISECOND, 0);

		try {
			Date date = format.parse(dateValue);
			gcAppt.setTime(date);// gcAppt is now ready with both timezone and
									// timestamp
		} catch (Exception e) {
		}

		GregorianCalendar gcToday = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		gcToday.set(Calendar.HOUR_OF_DAY, 0);
		gcToday.set(Calendar.MINUTE, 0);
		gcToday.set(Calendar.SECOND, 0);
		gcToday.set(Calendar.MILLISECOND, 0);

		if (gcAppt.before(gcToday))
			return true;
		return false;
	}

	/**
	 * Checks if is past date time.
	 * 
	 * @param dateValue
	 *            the date value
	 * @param dateFormat
	 *            the date format
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return true, if is past date time
	 */
	public static boolean isPastDateTime(String dateValue, String dateFormat, String timeZone) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		GregorianCalendar gcAppt = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		format.setCalendar(gcAppt);
		try {
			Date date = format.parse(dateValue);
			gcAppt.setTime(date);// gcAppt is now ready with both timezone and
									// timestamp
		} catch (Exception e) {
		}

		GregorianCalendar gcToday = new GregorianCalendar(TimeZone.getTimeZone(timeZone));

		if (gcAppt.before(gcToday))
			return true;
		return false;
	}

	/**
	 * Adds the days.
	 * 
	 * @param dateStr
	 *            the date str
	 * @param days
	 *            the days
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String addDays(String dateStr, int days, String timeZone) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		// set timezone
		sdf.setCalendar(cal);
		try {
			cal.setTime(sdf.parse(dateStr));
		} catch (Exception e) {
		}
		cal.add(Calendar.DATE, days);
		// set timestamp
		String dateString = sdf.format(cal.getTime());
		return dateString;
	}// method

	/**
	 * Format sql date time to mmddyy.
	 * 
	 * @param dateStr
	 *            the date str
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String formatSQLDateTimeToMMDDYY(String dateStr, String timeZone) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// set timezone
		sdf.setCalendar(cal);

		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yy");
		// set timezone
		sdf2.setCalendar(cal);

		try {
			cal.setTime(sdf.parse(dateStr));
		} catch (Exception e) {
		}

		// set timestamp
		String dateString = sdf2.format(cal.getTime());
		return dateString;
	}// method

	/**
	 * Adds the months.
	 * 
	 * @param dateStr
	 *            the date str
	 * @param months
	 *            the months
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String addMonths(String dateStr, int months, String timeZone) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		// set timezone
		sdf.setCalendar(cal);
		try {
			cal.setTime(sdf.parse(dateStr));
		} catch (Exception e) {
		}
		cal.add(Calendar.MONTH, months);
		// set timestamp
		String dateString = sdf.format(cal.getTime());
		return dateString;
	}// method

	/**
	 * Format date to string.
	 * 
	 * @param date
	 *            the date
	 * @param format
	 *            the format
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String formatDateToString(Date date, String format, String timeZone) {
		String dateString = "";
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setCalendar(cal);
		try {
			dateString = formatter.format(date);
		} catch (Exception e) {
		}
		return dateString;
	}

	/**
	 * Format full date to12 h h_ mm.
	 * 
	 * @param indate
	 *            the indate
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String formatFullDateTo12HH_MM(String indate, String timeZone) {
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = null;
		java.util.Date dateObject = null;
		GregorianCalendar dateGC = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		String retstr = "";
		try {
			sdf = new SimpleDateFormat(format, Locale.US);
			sdf.setCalendar(dateGC);
			dateObject = sdf.parse(indate);
			dateGC.setTime(dateObject);
			String h = String.valueOf(dateGC.get(Calendar.HOUR));
			if (h.length() == 1)
				h = "0" + h;
			String M = String.valueOf(dateGC.get(Calendar.MINUTE));
			if (M.length() == 1)
				M = "0" + M;
			retstr = h + ":" + M;

			if (retstr.startsWith("00")) {
				retstr = 12 + ":" + M;
			}
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
		}

		return retstr;
	}

	/**
	 * Format gc date to mmddyyyy.
	 * 
	 * @param dateGC
	 *            the date gc
	 * 
	 * @return the string
	 */
	public static String formatGCDateToMMDDYYYY(GregorianCalendar dateGC) {
		String dateString = "";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		formatter.setCalendar(dateGC);
		try {
			dateString = formatter.format(dateGC.getTime());
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
		}

		return dateString;
	}

	/**
	 * Format gc date to yyyymmdd.
	 * 
	 * @param dateGC
	 *            the date gc
	 * 
	 * @return the string
	 */
	public static String formatGCDateToYYYYMMDD(GregorianCalendar dateGC) {
		String dateString = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setCalendar(dateGC);
		try {
			dateString = formatter.format(dateGC.getTime());
		} catch (Exception e) {
		}

		return dateString;
	}

	/**
	 * Format sql date.
	 * 
	 * @param date
	 *            the date
	 * @param format
	 *            the format
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String formatSQLDate(String date, String format, String timeZone) {
		Date dateObject = parseDateString(date, format, timeZone);
		return formatDateToString(dateObject, "yyyy-MM-dd", timeZone);// default
																		// MySQL
																		// Date
	}

	/**
	 * Format sql date time.
	 * 
	 * @param date
	 *            the date
	 * @param format
	 *            the format
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String formatSQLDateTime(String date, String format, String timeZone) {
		Date dateObject = parseDateString(date, format, timeZone);
		return formatDateToString(dateObject, "yyyy-MM-dd HH:mm", timeZone);// default
																			// MySQL
																			// DateTime
	}

	/**
	 * Gets the current date.
	 * 
	 * @param format
	 *            the format
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the current date
	 */
	public static String getCurrentDate(String format, String timeZone) {
		String dateString = "";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		GregorianCalendar gcAppt = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		formatter.setCalendar(gcAppt);
		try {
			dateString = formatter.format(gcAppt.getTime());
		} catch (Exception e) {
		}
		return dateString;
	}// method

	/**
	 * Sets the days to date.
	 * 
	 * @param dateStr
	 *            the date str
	 * @param days
	 *            the days
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String setDaysToDate(String dateStr, int days, String timeZone) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		// set timezone
		sdf.setCalendar(cal);
		try {
			cal.setTime(sdf.parse(dateStr));
		} catch (Exception e) {
		}
		cal.set(Calendar.DATE, days);
		// set timestamp
		String dateString = sdf.format(cal.getTime());
		return dateString;
	}// method

	/**
	 * Gets the first sunday.
	 * 
	 * @param dateStr
	 *            the date str
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the first sunday
	 */
	public static GregorianCalendar getFirstSunday(String dateStr, String timeZone) {

		GregorianCalendar c = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		// set timezone
		sdf.setCalendar(c);
		try {
			c.setTime(sdf.parse(dateStr));
		} catch (Exception e) {
		}
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		int decrement = 0;
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			decrement = 0;
			break;
		case Calendar.MONDAY:
			decrement = 1;
			break;
		case Calendar.TUESDAY:
			decrement = 2;
			break;
		case Calendar.WEDNESDAY:
			decrement = 3;
			break;
		case Calendar.THURSDAY:
			decrement = 4;
			break;
		case Calendar.FRIDAY:
			decrement = 5;
			break;
		case Calendar.SATURDAY:
			decrement = 6;
			break;
		}
		c.set(Calendar.DATE, c.get(Calendar.DATE) - decrement);
		return c;
	}

	/**
	 * Gets the first saturday.
	 * 
	 * @param dateStr
	 *            the date str
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the first saturday
	 */
	public static GregorianCalendar getFirstSaturday(String dateStr, String timeZone) {

		GregorianCalendar c = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		// set timezone
		sdf.setCalendar(c);
		try {
			c.setTime(sdf.parse(dateStr));
		} catch (Exception e) {
		}
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		int decrement = 0;
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			decrement = 1;
			break;
		case Calendar.MONDAY:
			decrement = 2;
			break;
		case Calendar.TUESDAY:
			decrement = 3;
			break;
		case Calendar.WEDNESDAY:
			decrement = 4;
			break;
		case Calendar.THURSDAY:
			decrement = 5;
			break;
		case Calendar.FRIDAY:
			decrement = 6;
			break;
		case Calendar.SATURDAY:
			decrement = 0;
			break;
		}
		c.set(Calendar.DATE, c.get(Calendar.DATE) - decrement);
		return c;
	}

	/**
	 * Parses the date string.
	 * 
	 * @param datestr
	 *            the datestr
	 * @param format
	 *            the format
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the java.util. date
	 */
	public static java.util.Date parseDateString(String datestr, String format, String timeZone) {
		java.util.Date d = null;
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat df = new SimpleDateFormat(format);
		df.setCalendar(cal);
		try {
			d = df.parse(datestr);
		} catch (Exception e) {
		}
		return d;
	}// method

	/**
	 * Gets the week short hand.
	 * 
	 * @param dateStr
	 *            the date str
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the week short hand
	 */
	public static String getWeekShortHand(String dateStr, String timeZone) {
		StringBuffer week = new StringBuffer();
		GregorianCalendar startGC = AdminUtils.getFirstSunday(dateStr, timeZone);
		GregorianCalendar endGC = new GregorianCalendar(TimeZone.getTimeZone(timeZone));

		endGC.setTime(startGC.getTime());
		endGC.add(Calendar.DATE, 6);

		if (startGC.get(Calendar.MONTH) == endGC.get(Calendar.MONTH)) {
			week.append(AdminUtils.getMonth(startGC) + " " + startGC.get(Calendar.DATE) + " - " + endGC.get(Calendar.DATE) + " , " + startGC.get(Calendar.YEAR));
		} else if (startGC.get(Calendar.YEAR) != endGC.get(Calendar.YEAR)) {
			// (Year1 != Year2) use {Month1 sdate, Year1 - Month2 edate , Year2}
			// format
			week.append(AdminUtils.getMonth(startGC) + " " + startGC.get(Calendar.DATE) + " , " + startGC.get(Calendar.YEAR) + " - " + AdminUtils.getMonth(endGC) + " "
					+ endGC.get(Calendar.DATE) + " , " + endGC.get(Calendar.YEAR));
		} else {
			// use {Month1 sdate - Month2 edate , Year} format
			week.append(AdminUtils.getMonth(startGC) + " " + startGC.get(Calendar.DATE) + " - " + AdminUtils.getMonth(endGC) + " " + endGC.get(Calendar.DATE) + " , "
					+ startGC.get(Calendar.YEAR));
		}

		return week.toString();
	}

	// to find JSP calendar Header day
	/**
	 * Gets the short day.
	 * 
	 * @param dateStr
	 *            the date str
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the short day
	 */
	public static String getShortDay(String dateStr, String timeZone) {
		String day = "";
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
		// set timezone
		sdf.setCalendar(calendar);

		try {
			calendar.setTime(sdf.parse(dateStr));
			day = getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * Format sql string to gc.
	 * 
	 * @param datestr
	 *            the datestr
	 * 
	 * @return the gregorian calendar
	 */
	public static GregorianCalendar formatSqlStringToGC(String datestr) {
		int year = Integer.parseInt(datestr.substring(0, 4));
		int month = Integer.parseInt(datestr.substring(5, 7));
		int date = Integer.parseInt(datestr.substring(8, 10));
		int hour = Integer.parseInt(datestr.substring(11, 13));
		int min = Integer.parseInt(datestr.substring(14, 16));
		int sec = Integer.parseInt(datestr.substring(17, 19));
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}// method

	/**
	 * Compare time hhmmss.
	 * 
	 * @param time1
	 *            the time1
	 * @param time2
	 *            the time2
	 * 
	 * @return the int
	 */
	public static int compareTimeHHMMSS(String time1, String time2) {
		int retval = -2;
		if (time1 == null && time2 == null) {
			retval = 0;
		} else if (time1 != null && time2 != null) {
			// hh:mm:ss
			int hour1 = Integer.valueOf(time1.substring(0, 2)).intValue();
			int min1 = Integer.valueOf(time1.substring(3, 5)).intValue();
			int sec1 = Integer.valueOf(time1.substring(6, 8)).intValue();

			// hh:mm:ss
			int hour2 = Integer.valueOf(time2.substring(0, 2)).intValue();
			int min2 = Integer.valueOf(time2.substring(3, 5)).intValue();
			int sec2 = Integer.valueOf(time2.substring(6, 8)).intValue();

			int timeInSeconds1 = hour1 * 60 * 60 + min1 * 60 + sec1;
			int timeInSeconds2 = hour2 * 60 * 60 + min2 * 60 + sec2;

			if (timeInSeconds1 == timeInSeconds2)
				retval = 0;
			else if (timeInSeconds1 < timeInSeconds2)
				retval = -1;
			else
				retval = 1;
		}
		return retval;
	}

	/**
	 * Format display date.
	 * 
	 * @param date
	 *            the date
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String formatDisplayDate(String date, String timeZone) {
		Date dateObject = parseDateString(date, "MMMMM dd, yyyy", timeZone);
		return formatDateToString(dateObject, "EEEEE, MMM dd, yyyy", timeZone);
	}

	/**
	 * Format display date2.
	 * 
	 * @param date
	 *            the date
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String formatDisplayDate2(String date, String timeZone) {
		Date dateObject = parseDateString(date, "MM/dd/yy", timeZone);
		return formatDateToString(dateObject, "EEEEE, MMMM dd, yyyy", timeZone);
	}

	public static String formatDisplayDate4(String date, String timeZone) {
		System.out.println("input date :" + date);
		String dateFormatString = "";
		// if(iDisplayAdapter.getCountryCode().equals("UK")) dateFormatString =
		// "EEEEE, dd MMM yyyy";
		// else dateFormatString = "EEEEE, MMM dd, yyyy";
		dateFormatString = "MM/dd/yy";

		Date dateObject = parseDateString(date, dateFormatString, timeZone);
		String ordinal = formatDateToString(dateObject, "dd", timeZone);
		System.out.println("ordinal :" + ordinal);
		int ordinalValue = Integer.parseInt(ordinal);
		System.out.println("getOrdinalFor :" + getOrdinalFor(ordinalValue));
		return (formatDateToString(dateObject, "MMMMM d", timeZone) + getOrdinalFor(ordinalValue));
	}

	public static String getOrdinalFor(int value) {
		int hundredRemainder = value % 100;
		if (hundredRemainder >= 10 && hundredRemainder <= 20) {
			return "th";
		}
		int tenRemainder = value % 10;
		switch (tenRemainder) {
		case 1:
			return "st";
		case 2:
			return "nd";
		case 3:
			return "rd";
		default:
			return "th";
		}
	}

	/**
	 * Format display date3.
	 * 
	 * @param date
	 *            the date
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the string
	 */
	public static String formatDisplayDate3(String date, String timeZone) {
		Date dateObject = parseDateString(date, "yyyy-MM-dd", timeZone);
		return formatDateToString(dateObject, "EEEEE, MMMM dd, yyyy", timeZone);
	}

	public static String formatDisplayDate5(String date, String timeZone) {
		Date dateObject = parseDateString(date, "MM/dd/yy", timeZone);
		return formatDateToString(dateObject, "EEEEE, MMMM dd, yyyy", timeZone);
	}

	/**
	 * Gets the time display2.
	 * 
	 * @param hhmmss
	 *            the hhmmss
	 * 
	 * @return the time display2
	 */
	public static String getTimeDisplay2(String hhmmss) {
		String hh = hhmmss.substring(0, 2);
		String min = hhmmss.substring(3, 5);
		int hour = Integer.parseInt(hh);
		String ampm = "AM";
		if (hour >= 12)
			ampm = "PM";
		if (hour > 12)
			hour = hour - 12;
		String display = hour + ":" + min + " " + ampm;
		return display;
	}

	/**
	 * Checks if is alpha numeric hypen period.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return true, if is alpha numeric hypen period
	 */
	public static boolean isAlphaNumericHypenPeriod(String str) {
		if (str == null)
			return false;
		try {
			char x[] = str.toCharArray();
			for (int i = 0; i < x.length; i++) {
				if (!(Character.isLetterOrDigit(x[i]) || x[i] == '-' || x[i] == '.'))
					return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Format hour min.
	 * 
	 * @param time
	 *            the time
	 * @param ampm
	 *            the ampm
	 * 
	 * @return the string
	 */
	public static String formatHourMin(String time, String ampm) {
		time = time.trim();
		if (time.length() == 0)
			return new String("");
		int index = time.indexOf(':');
		int hour = Integer.parseInt(time.substring(0, index));
		if (ampm.equals("PM"))
			hour = hour + 12;
		String hourStr = String.valueOf(hour);
		if (hourStr.length() == 1)
			hourStr = "0" + hourStr;

		return new String("2000-01-01 " + hourStr + time.substring(index));
	}

	/**
	 * Format phone number db.
	 * 
	 * @param phone
	 *            the phone
	 * @param areaCode
	 *            the area code
	 * 
	 * @return the string
	 */
	public static String formatPhoneNumberDB(String phone, String areaCode) {
		phone = phone.trim();
		phone = removeNonDigits(phone);
		if (phone.length() == 7)
			phone = areaCode + phone;
		return phone;
	}

	/**
	 * Format m m_ d d_ yyy y_to_ gc.
	 * 
	 * @param date
	 *            the date
	 * 
	 * @return the gregorian calendar
	 */
	public static GregorianCalendar formatMM_DD_YYYY_to_GC(String date) {
		GregorianCalendar gc = new GregorianCalendar();
		int m = Integer.parseInt(date.substring(0, 2));
		int d = Integer.parseInt(date.substring(3, 5));
		int y = Integer.parseInt(date.substring(6, 10));
		gc.set(Calendar.YEAR, y);
		gc.set(Calendar.MONTH, m - 1);
		gc.set(Calendar.DATE, d);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		return gc;
	}

	/**
	 * Format yyyymmdd to gc.
	 * 
	 * @param datestr
	 *            the datestr
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the gregorian calendar
	 */
	public static GregorianCalendar formatYYYYMMDDToGC(String datestr, String timeZone) {
		int year = Integer.parseInt(datestr.substring(0, 4));
		int month = Integer.parseInt(datestr.substring(5, 7));
		int date = Integer.parseInt(datestr.substring(8, 10));
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}// method

	/**
	 * Checks if is alpha numeric.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return true, if is alpha numeric
	 */
	public static boolean isAlphaNumeric(String str) {
		if (str == null)
			return false;
		try {
			char x[] = str.toCharArray();
			for (int i = 0; i < x.length; i++) {
				if (!(Character.isLetterOrDigit(x[i])))
					return false;
			}
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Gets the resource id csv.
	 * 
	 * @param resourceList
	 *            the resource list
	 * @param resourceId
	 *            the resource id
	 * 
	 * @return the resource id csv
	 */
	public static String getResourceIdCsv(List<Resource> resourceList, int resourceId) {
		StringBuffer sb = new StringBuffer();
		Resource resourceTO = new Resource();
		int i = 0;
		for (i = 0; i < resourceList.size(); i++) {
			if ((resourceList.get(i)).getResourceId() == resourceId) {
				resourceTO = resourceList.get(i);
				break;
			}
		}
		if (i == resourceList.size())
			return sb.toString();

		boolean firstValue = true;
		for (int k = 0; k < resourceList.size(); k++) {
			if ((resourceList.get(k)).getFirstName().equals(resourceTO.getFirstName()) && (resourceList.get(k)).getLastName().equals(resourceTO.getLastName())) {
				if (firstValue) {
					sb.append(String.valueOf((resourceList.get(k)).getResourceId()));
					firstValue = false;
				} else {
					sb.append(",");
					sb.append(String.valueOf((resourceList.get(k)).getResourceId()));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Gets the location id csv.
	 * 
	 * @param resourceList
	 *            the resource list
	 * @param resourceId
	 *            the resource id
	 * 
	 * @return the location id csv
	 */
	public static String getLocationIdCsv(List<Resource> resourceList, int resourceId) {
		StringBuffer sb = new StringBuffer();
		Resource resourceTO = new Resource();
		int i = 0;
		for (i = 0; i < resourceList.size(); i++) {
			if ((resourceList.get(i)).getResourceId() == resourceId) {
				resourceTO = resourceList.get(i);
				break;
			}
		}
		if (i == resourceList.size())
			return sb.toString();

		boolean firstValue = true;
		for (int k = 0; k < resourceList.size(); k++) {
			if ((resourceList.get(k)).getFirstName().equals(resourceTO.getFirstName()) && (resourceList.get(k)).getLastName().equals(resourceTO.getLastName())) {
				if (firstValue) {
					sb.append(String.valueOf((resourceList.get(k)).getLocationId()));
					firstValue = false;
				} else {
					sb.append(",");
					sb.append(String.valueOf((resourceList.get(k)).getLocationId()));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Parses the string list.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return the array list
	 */
	public static List<String> parseStringList(String str) {
		List<String> list = new ArrayList<String>();
		String[] custRecord = str.split("\\|", -2);
		for (int k = 0; k < custRecord.length; k++) {
			list.add(custRecord[k]);
		}
		return list;
	}

	/**
	 * Removes the last pipe.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return the string
	 */
	public static String removeLastPipe(String str) {
		int len = str.length();
		if (len > 0 && str.endsWith("|"))
			str = str.substring(0, len - 1);
		return str;
	}

	public static String formatReason(String reason) {
		StringBuffer buf = new StringBuffer("");
		if (reason != null && reason.length() > 0)
			buf.append("\r\n\r\nYour Appointment was rescheduled for the following reason(s):\r\n").append(reason);
		return buf.toString();
	}

	/**
	 * Checks if is alpha numeric hypen space apostrophe period comma hash slash
	 * bracket etc.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @return true, if is alpha numeric hypen space apostrophe period comma
	 *         hash slash bracket etc
	 */
	public static boolean isAlphaNumericHypenSpaceApostrophePeriodCommaHashSlashBracketEtc(String str) {
		if (str == null)
			return false;
		try {
			char x[] = str.toCharArray();
			for (int i = 0; i < x.length; i++) {
				if (!(Character.isLetterOrDigit(x[i]) || Character.isSpaceChar(x[i]) || x[i] == '-' || x[i] == '\'' || x[i] == '.' || x[i] == ',' || x[i] == '#' || x[i] == '/'
						|| x[i] == '[' || x[i] == ']' || x[i] == '\\' || x[i] == '>' || x[i] == '!'))
					return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Gets the diff in days.
	 * 
	 * @param startGC
	 *            the start gc
	 * @param endGC
	 *            the end gc
	 * @param timeZone
	 *            the time zone
	 * 
	 * @return the diff in days
	 */
	public static int getDiffInDays(GregorianCalendar startGC, GregorianCalendar endGC, String timeZone) {
		int dateDiff = 0;
		long ldate1 = startGC.getTimeInMillis();
		long ldate2 = endGC.getTimeInMillis();
		// Use integer calculation, truncate the decimals
		int hr1 = (int) (ldate1 / 3600000); // 60*60*1000
		int hr2 = (int) (ldate2 / 3600000);
		int days1 = (int) hr1 / 24;
		int days2 = (int) hr2 / 24;
		if (days2 > days1) {
			dateDiff = days2 - days1;
		} else {
			dateDiff = days1 - days2;
		}
		return dateDiff;
	}

	public static String formatDateStringMMDDYYYY(String dt) {
		String formatedDate = "";
		if (dt == null)
			return formatedDate;
		dt = dt.trim();
		if (dt.length() == 0)
			return formatedDate;
		if (dt.length() == 10)
			return dt;// assume it is already in mm/dd/yyyy format

		String mmStr = "";
		String ddStr = "";
		String yyyyStr = "";

		StringTokenizer tok1 = new StringTokenizer(dt, "/");
		if (tok1.hasMoreTokens()) {
			mmStr = tok1.nextToken().trim();
			if (mmStr.length() == 1)
				mmStr = "0" + mmStr;// add leading zero for single digit month
			mmStr = mmStr + "/";
		}
		if (tok1.hasMoreTokens()) {
			ddStr = tok1.nextToken().trim();
			if (ddStr.length() == 1)
				ddStr = "0" + ddStr;// add leading zero for single digit date
			ddStr = ddStr + "/";
		}
		if (tok1.hasMoreTokens()) {
			yyyyStr = tok1.nextToken().trim();
			if (yyyyStr.length() == 2)
				yyyyStr = "20" + yyyyStr;// add century before 2 digit year
		}
		formatedDate = mmStr + ddStr + yyyyStr;
		return formatedDate;
	}

	public static List<String> getDateIntervals(String apptDate, int frequency, int interval, String recurringEndDate, String timeZone) throws Exception {

		System.out.println("apptDate :" + apptDate);
		System.out.println("frequency :" + frequency);
		System.out.println("interval :" + interval);
		System.out.println("recurringEndDate :" + recurringEndDate);
		System.out.println("timeZone :" + timeZone);

		List<String> dateInterval = new ArrayList<String>();
		int dateIncrement = frequency * interval;

		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		// you may need to set with timezone
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setCalendar(cal);
		cal.setTime(formatter.parse(apptDate));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, dateIncrement);

		Calendar calEnddate = new GregorianCalendar(TimeZone.getTimeZone(timeZone));

		SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");
		formatter2.setCalendar(calEnddate);
		calEnddate.setTime(formatter2.parse(recurringEndDate));
		calEnddate.set(Calendar.HOUR, 0);
		calEnddate.set(Calendar.MINUTE, 0);
		calEnddate.set(Calendar.SECOND, 0);
		calEnddate.set(Calendar.MILLISECOND, 0);

		SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm aaa");
		System.out.println("Starting Date :" + sdf.format(cal.getTime()));
		System.out.println("Ending Date :" + sdf.format(calEnddate.getTime()));

		while (cal.compareTo(calEnddate) < 0) {
			dateInterval.add(formatter.format(cal.getTime()));
			cal.add(Calendar.DATE, dateIncrement);
		}

		System.out.println("Date ArrayList :" + dateInterval);

		return dateInterval;

	}

	public static boolean isDateTimeEqual(String oldDateTime, String oldDateFormat, String newDateTime, String newDateFormat, String timeZone) throws Exception {

		Date oldDateObject = parseDateString(oldDateTime, oldDateFormat, timeZone);
		Calendar cal = new GregorianCalendar();// you may need to set with
												// timezone
		cal.setTime(oldDateObject);

		Date newDateObject = parseDateString(newDateTime, newDateFormat, timeZone);
		Calendar calEnddate = new GregorianCalendar();// you may need to set
														// with timezone
		calEnddate.setTime(newDateObject);

		SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm aaa");
		System.out.println("Starting Date :" + sdf.format(cal.getTime()));
		System.out.println("Ending Date :" + sdf.format(calEnddate.getTime()));

		if (cal.compareTo(calEnddate) == 0) {
			return true;
		}

		return false;

	}

	public static String getTodayDateAddDays(int days) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.add(Calendar.DATE, days);

		return formatGCDateToYYYYMMDD(gc);
	}

	public static String getResourceDisplayName(Resource resource, boolean addLastName) {
		StringBuffer displayName = new StringBuffer("");
		if (null != resource) {
			if (StringUtils.isNotEmpty(resource.getPrefix())) {
				displayName.append(resource.getPrefix()).append(" ");
			}
			if (StringUtils.isNotEmpty(resource.getFirstName())) {
				displayName.append(resource.getFirstName());
			}
			if (addLastName) {
				if (StringUtils.isNotEmpty(resource.getLastName())) {
					displayName.append(" ").append(resource.getLastName());
				}
				if (StringUtils.isNotEmpty(resource.getTitle())) {
					displayName.append(" ").append(resource.getTitle());
				}
			}
		}
		return displayName.toString();
	}

	public static String getResourceDisplayName(String prefix, String firstName, String lastName, String title, boolean addLastName) {
		StringBuffer displayName = new StringBuffer("");
		if (StringUtils.isNotEmpty(prefix)) {
			displayName.append(prefix).append(" ");
		}
		if (StringUtils.isNotEmpty(firstName)) {
			displayName.append(firstName);
		}
		if (addLastName) {
			if (StringUtils.isNotEmpty(lastName)) {
				displayName.append(" ").append(lastName);
			}
			if (StringUtils.isNotEmpty(title)) {
				displayName.append(" ").append(title);
			}
		}
		return displayName.toString();
	}

	public static String getBreakTimeWithDuration(String breakTime, String durationStr) {
		if (null != breakTime && !"".equals(breakTime)) {
			try {
				return convert24To12HoursFormat(breakTime) + " for " + durationStr;
			} catch (ParseException e) {
				return "No Break";
			}
		} else {
			return "No Break";
		}
	}

	public static String convert24To12HoursFormat(String twentyFourHourTime) throws ParseException {
		ThreadLocal<DateFormat> time24Format = getSimpleDateFormat(CommonDateContants.TIME_FORMAT_TWENTY_FOUR_HRS.getValue());
		ThreadLocal<DateFormat> time12Format = getSimpleDateFormat(CommonDateContants.TIME_FORMAT_TWELVE_HRS.getValue());
		return time12Format.get().format(time24Format.get().parse(twentyFourHourTime));
	}

	public static ThreadLocal<DateFormat> getSimpleDateFormat(String dateTimeFormatStr) {
		ThreadLocal<DateFormat> tldf = null;
		try {
			tldf = getThreadLocal(dateTimeFormatStr);
			return tldf;
		} catch (Exception e) {
		}
		return tldf;
	}

	public static ThreadLocal<DateFormat> getThreadLocal(final String dateTimeForamtStr) {
		final ThreadLocal<DateFormat> tldf_ = new ThreadLocal<DateFormat>() {
			@Override
			protected DateFormat initialValue() {
				return new SimpleDateFormat(dateTimeForamtStr);
			}
		};
		return tldf_;
	}

	public static String getResourceName(Resource resource12) {
		StringBuilder sb = new StringBuilder();
		String firstName = resource12.getFirstName();
		String lastName = resource12.getLastName();
		if (StringUtils.isNotEmpty(firstName)) {
			sb.append(firstName);
		}
		sb.append(" ");
		if (StringUtils.isNotEmpty(lastName)) {
			sb.append(lastName);
		}
		return sb.toString();
	}

	public static boolean isHighAccessLevelUser(String access_level) {
		if (null != access_level) {
			if (CommonApptDeskConstants.USER_ACCESS_LEVEL_SUPER_USER.getValue().equalsIgnoreCase(access_level)
					|| CommonApptDeskConstants.USER_ACCESS_LEVEL_ADMINISTRATOR.getValue().equalsIgnoreCase(access_level)
					|| CommonApptDeskConstants.USER_ACCESS_LEVEL_MANAGER.getValue().equalsIgnoreCase(access_level)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isReadOnlyAccessLevelUser(String access_level) {
		return isValidAccessLevel(CommonApptDeskConstants.USER_ACCESS_LEVEL_READ_ONLY.getValue(), access_level);
	}

	public static boolean isLocationAccessLevelUser(String access_level) {
		return isValidAccessLevel(CommonApptDeskConstants.USER_ACCESS_LEVEL_LOCATION.getValue(), access_level);
	}

	public static boolean isResourceAccessLevelUser(String access_level) {
		return isValidAccessLevel(CommonApptDeskConstants.USER_ACCESS_LEVEL_PROVIDER.getValue(), access_level);
	}

	public static boolean isSchedulerLevelUser(String access_level) {
		return isValidAccessLevel(CommonApptDeskConstants.USER_ACCESS_LEVEL_SCHEDULER.getValue(), access_level);
	}

	public static boolean isValidAccessLevel(String accessLevel, String userAccessLevel) {
		if (null != userAccessLevel) {
			if (accessLevel.equalsIgnoreCase(userAccessLevel)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static String getStringWithoutStartingAndEndingComma(String data) {
		if (null != data && !"".equals(data)) {
			if (data.startsWith(CommonApptDeskConstants.COMMA.getValue())) {
				data = data.substring(1, data.length());
			}
			if (data.endsWith(CommonApptDeskConstants.COMMA.getValue())) {
				data = data.substring(0, data.length() - 1);
			}
		}
		return data;
	}

}