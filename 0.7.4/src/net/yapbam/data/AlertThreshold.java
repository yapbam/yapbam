package net.yapbam.data;

/** An alert threshold when balance is less or more than specified amounts.
 */
public class AlertThreshold {
	/** The default threshold: Alert if balance is less than 0. No high limit. */
	public static final AlertThreshold DEFAULT = new AlertThreshold(0, Double.POSITIVE_INFINITY);
	/** A threshold that never generates any alert.
	 * Useful to ask for no alerts on an account.
	 */
	public static AlertThreshold NO = new AlertThreshold(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	
	private double lessThreshold;
	private double moreThreshold;

	/** Constructor.
	 * @param lessThreshold if balance is lower than lessThreshold, an alert is generated. Double.NEGATIVE_INFINITY to have no "less than amount" alert
	 * @param moreThreshold if balance is higher than moreThreshold, an alert is generated. Double.POSITIVE_INFINITY to have no "more then amount" alert
	 */
	public AlertThreshold(double lessThreshold, double moreThreshold) {
		this.lessThreshold = lessThreshold;
		this.moreThreshold = moreThreshold;
	}

	/** Gets the threshold for "balance more than threshold" alert.
	 * @return a double. Double.POSITIVE_INFINITY if no alert is set.
	 */
	public double getMoreThreshold() {
		return moreThreshold;
	}

	/** Gets the threshold for "balance less than threshold" alert.
	 * @return a double. Double.NEGATIVE_INFINITY is no alert is set.
	 */
	public double getLessThreshold() {
		return lessThreshold;
	}


	/** Tests whether an alert is equal to this.
	 * Two alerts are equals if and only if their thresholds are equals.
	 * Note : As thresholds are coded with doubles, the equality is tested according to the currency precision.
	 * @return a boolean
	 * @see GlobalData#AMOUNT_COMPARATOR
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AlertThreshold) {
			AlertThreshold al = (AlertThreshold) obj;
			return (GlobalData.AMOUNT_COMPARATOR.compare(al.lessThreshold,this.lessThreshold)==0) && (GlobalData.AMOUNT_COMPARATOR.compare(al.moreThreshold,this.moreThreshold)==0);
		} else {
			return super.equals(obj);
		}
	}

	/** Tests whether this threshold is able to generate an alert.
	 * @return true if it can generate alerts, false otherwise (example: alert is set for less than negative infinity)
	 * @see AlertThreshold#NO
	 */
	public boolean isLifeless() {
		return (lessThreshold==Double.NEGATIVE_INFINITY) && (moreThreshold==Double.POSITIVE_INFINITY);
	}
	
	/** Tests whether an amount triggers the alert. 
	 * @param amount The amount to test
	 * @return an integer. <0 if the tested amount is less than the "less threshold", >0 if the tested amount is more than the "more threshold", 0 if no trigger is raised.
	 */
	public int getTrigger(double amount) {
		if (GlobalData.AMOUNT_COMPARATOR.compare(amount,this.lessThreshold)<0) return -1;
		else if (GlobalData.AMOUNT_COMPARATOR.compare(amount,this.moreThreshold)>0) return 1;
		return  0;
	}
	
	@Override
	public int hashCode() {
		if (isLifeless()) return 1;
		return (int) Math.min(this.lessThreshold,this.moreThreshold);
	}
}