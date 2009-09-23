package net.yapbam.date.helpers;

import java.util.Date;
import java.util.GregorianCalendar;

public abstract class DateHelper {

	public static Date integerToDate(int date) {
		if (date<0) return null;
		int year = date/10000;
		int day = date - 10000*year;
		int month = day/100;
		day = day - 100*month;
		return new GregorianCalendar(1900+year, month-1, day).getTime();
	}

	@SuppressWarnings("deprecation")
	public static int dateToInteger(Date date) {
		if (date==null) return -1;
		return date.getYear()*10000+(date.getMonth()+1)*100+date.getDate();
	}

}
