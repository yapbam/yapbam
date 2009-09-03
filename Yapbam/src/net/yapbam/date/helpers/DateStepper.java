package net.yapbam.date.helpers;

import java.util.Date;

/** This interface describe a class able perform a time increment over a date.
 * It could be used, for instance, to convert a transaction date in a value date */
public interface DateStepper {
	/** A trivial value date computer, that returns the transaction date as value date.*/
	public static final DateStepper IMMEDIATE = new DateStepper() {
		public Date getNextStep(Date date) {
			return date;
		}
		@Override
		public boolean equals(Object obj) {
			return this.getClass().equals(obj.getClass());
		}
		@Override
		public int hashCode() {
			return 0;
		}
	};
	
	/** Compute the next date
	 * @param date date for which we want a successor (which could be before date, for instance for a backward stepper)
	 * @return the next date or null if the cycle has to stop (if the stepper manage this concept)
	 */
	public abstract Date getNextStep (Date date);
}
