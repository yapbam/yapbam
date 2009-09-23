package net.yapbam.date.helpers;

import java.util.Date;
import java.util.GregorianCalendar;

/** This utiliy class allows to convert Date into integer in order to reduce the memory footprint of such data.
 * Be careful, other fields than the day, month and year are ignored 
 * @author fathzer
 * license GPL v3
 */
public abstract class DateHelper {

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
		return new GregorianCalendar(1900+year, month-1, day).getTime();
	}

	@SuppressWarnings("deprecation")
	/** Converts a date into an integer
	 * @param date the date to be converted or null
	 * @return the date in its integer format (null is coded by a negative number)
	 */
	public static int dateToInteger(Date date) {
		if (date==null) return -1;
		return date.getYear()*10000+(date.getMonth()+1)*100+date.getDate();
	}

}
