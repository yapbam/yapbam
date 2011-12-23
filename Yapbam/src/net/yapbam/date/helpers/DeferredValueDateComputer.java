package net.yapbam.date.helpers;

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/** This class allows to compute value date for a deferred operation */ 
public class DeferredValueDateComputer extends DateStepper {
	private static final boolean DEBUG = false;

	private int stopDay;
	private int debtDay;

	public DeferredValueDateComputer(int stopDay, int debtDay) {
		super();
		this.stopDay = stopDay;
		this.debtDay = debtDay;
	}

	public int getStopDay() {
		return stopDay;
	}

	public int getDebtDay() {
		return debtDay;
	}

	@Override
	public Date getNextStep(Date date) {
		if (DEBUG) {
			System.out.println("date de l'opération : "+DateFormat.getDateInstance().format(date)); //$NON-NLS-1$
			System.out.println("  date d'arrêt de compte : "+this.stopDay); //$NON-NLS-1$
			System.out.println("  date de débit : "+this.debtDay); //$NON-NLS-1$
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		int day = gc.get(GregorianCalendar.DATE);
		int month = gc.get(GregorianCalendar.MONTH);
		int year = gc.get(GregorianCalendar.YEAR);
		if (day>this.stopDay) {
			month++;
			if (month>gc.getActualMaximum(GregorianCalendar.MONTH)) {
				year++;
				month = 0;
			}
		}
		if (this.stopDay>this.debtDay){
			month++;
			if (month>gc.getActualMaximum(GregorianCalendar.MONTH)) {
				year++;
				month = 0;
			}
		}
		gc.set(year, month, 1);
		if (DEBUG) System.out.println("  1 du mois du débit : "+DateFormat.getDateInstance().format(gc.getTime())); //$NON-NLS-1$
		gc.add(GregorianCalendar.MONTH, 1);
		if (DEBUG) System.out.println("  1 du mois suivant  : "+DateFormat.getDateInstance().format(gc.getTime())); //$NON-NLS-1$
		gc.add(GregorianCalendar.DAY_OF_MONTH, -1);
		if (DEBUG) System.out.println("  Dernier jour du mois du débit : "+DateFormat.getDateInstance().format(gc.getTime())); //$NON-NLS-1$
		if (debtDay<gc.get(GregorianCalendar.DAY_OF_MONTH)) {
			gc.set(GregorianCalendar.DATE, debtDay);
		} else if (DEBUG) {
			 System.out.println ("  Le mois a moins de "+debtDay+" jours, on prend le dernier jour du mois"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (DEBUG) System.out.println("Jour du débit : "+DateFormat.getDateInstance().format(gc.getTime())); //$NON-NLS-1$
		if (DEBUG) System.out.println("----------------------"); //$NON-NLS-1$
		return gc.getTime();
	}

	@Override
	public Date getLastDate() {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = super.equals(obj);
		if (result) {
			result = (getStopDay()==((DeferredValueDateComputer)obj).getStopDay()) &&
				(getDebtDay()==((DeferredValueDateComputer)obj).getDebtDay());
		}
		return result;
	}
}
