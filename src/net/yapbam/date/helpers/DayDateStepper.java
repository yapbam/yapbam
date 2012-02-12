package net.yapbam.date.helpers;

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import net.yapbam.util.DateUtils;

public class DayDateStepper extends DateStepper {
	private static final boolean DEBUG = false;

	private int nbDays;
	private int lastDate;

	/** Constructor.
	 *  @param nbDays Number of days between the value date and the operation date.
	 *  If this number is negative, the value dates will be before operation dates.
	 *  @param lastDate lastDate or null if there's no time limit.
	 */
	public DayDateStepper(int nbDays, Date lastDate) {
		super();
		this.nbDays = nbDays;
		this.lastDate = lastDate==null?Integer.MAX_VALUE:DateUtils.dateToInteger(lastDate);
	}
	
	@Override
	public Date getNextStep(Date date) {
		if (DEBUG) {
			System.out.println("date de l'opération : "+DateFormat.getDateInstance().format(date)); //$NON-NLS-1$
			System.out.println("  nombre de jours : "+this.nbDays); //$NON-NLS-1$
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(GregorianCalendar.DAY_OF_MONTH, this.nbDays);
		Date result = gc.getTime();
		if (DEBUG) {
			System.out.println("Jour du débit : "+DateFormat.getDateInstance().format(result)); //$NON-NLS-1$
			System.out.println("----------------------"); //$NON-NLS-1$
		}
		if (DateUtils.dateToInteger(result)>this.lastDate) result = null;
		return result;
	}

	public int getStep() {
		return this.nbDays;
	}

	@Override
	public Date getLastDate() {
		return this.lastDate==Integer.MAX_VALUE?null:DateUtils.integerToDate(this.lastDate);
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = super.equals(obj);
		if (result) result = (getStep()==((DayDateStepper)obj).getStep()) && (lastDate==((DayDateStepper)obj).lastDate);
		return result;
	}
}
