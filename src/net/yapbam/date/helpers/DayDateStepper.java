package net.yapbam.date.helpers;

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import net.yapbam.util.DateUtils;

public class DayDateStepper implements DateStepper {
	private static final boolean DEBUG = false;

	private int nbDays;
	private int lastDate;

	/** Constructor.
	 *  @param nbDays Number of days between the value date and the operation date.
	 *  If this number is negative, the value dates will be before operation dates.
	 */
	public DayDateStepper(int nbDays, Date lastDate) {
		super();
		this.nbDays = nbDays;
		this.lastDate = DateUtils.dateToInteger(lastDate);
	}
	
	public Date getNextStep(Date date) {
		if (DEBUG) {
			System.out.println("date de l'opération : "+DateFormat.getDateInstance().format(date));
			System.out.println("  nombre de jours : "+this.nbDays);
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(GregorianCalendar.DAY_OF_MONTH, this.nbDays);
		if (DEBUG) {
			System.out.println("Jour du débit : "+DateFormat.getDateInstance().format(gc.getTime()));
			System.out.println("----------------------");
		}
		return gc.getTime();
	}

	public int getStep() {
		return this.nbDays;
	}

	@Override
	public Date getLastDate() {
		return DateUtils.integerToDate(lastDate);
	}
}
