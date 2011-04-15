package net.yapbam.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/** Utilities about dates. 
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public final class DateUtils {
	// Be sure nobody will instantiate this class
	private DateUtils(){};
	
	/** Computes the number of months between two dates.
	 * @param first The first date
	 * @param last The last date
	 * @return the number of months between the dates. The day of month is ignored
	 * (2010/01/01 is one month after 2009/12/31). If last is before first, the integer returned
	 * is negative.
	 */
	public static int getMonthlyDistance (Calendar first, Calendar last) {
		return  last.get(Calendar.YEAR)*12+last.get(Calendar.MONTH) - (first.get(Calendar.YEAR)*12+first.get(Calendar.MONTH));
	}

	/** Converts an integer into a date
	 * @param date the integer representation of a date as it is returned by dateToInteger
	 * @return a date or null if date is negative
	 * @see #dateToInteger(Date)
	 */
	public static Date integerToDate(int date) {
		if (date<0) return null;
		int year = date/10000;
		int day = date - 10000*year;
		int month = day/100;
		day = day - 100*month;
		return new GregorianCalendar(year, month-1, day).getTime();
	}

	/** Converts a date into an integer.
	 * @param date the date to be converted or null
	 * @return the date in its integer format (null is coded by a negative number).
	 * It is guaranteed that if two dates are separated by x days the difference between their integer representation is x.
	 * Please note that hour, minutes, second and milliseconds are ignored. 
	 */
	@SuppressWarnings("deprecation")
	public static int dateToInteger(Date date) {
		if (date==null) return -1;
		return (date.getYear()+1900)*10000+(date.getMonth()+1)*100+date.getDate();
	}
	
	/** Gets the date with the hour, minutes and seconds fields set to zero.
	 * @param date a Date
	 * @return a new Date
	 */
	public static Date getMidnight(Date date) {
		return integerToDate(dateToInteger(date));
	}
}
