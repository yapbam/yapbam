package net.yapbam.data;

/** An alert threshold when balance reach an amount.
 */
public class AlertThreshold {
	/** The default threshold: Balance less than 0. */
	public static final AlertThreshold DEFAULT = new AlertThreshold(0, true);
	/** A threshold that never generates any alert.
	 * Useful to ask for no alerts on an account.
	 */
	public static AlertThreshold NO = new AlertThreshold(Double.NEGATIVE_INFINITY, true);
	
	private double balance;
	private boolean less;

	/** Constructor.
	 * @param amount the new threshold amount.
	 * @param lower true if alert has to be set if balance is lower than threshold, false if balance is greater than threshold
	 */
	public AlertThreshold(double amount, boolean lower) {
		this.balance = amount;
		this.less = lower;
	}

	/** Gets the balance threshold.
	 * @return a double.
	 */
	public double getBalance() {
		return balance;
	}

	/** Tests whether the alert is for balance lower than balance threshold or not.
	 * @return true if alert is set for balance lower than the result of {@link #getBalance()}, false for balance greater than {@link #getBalance()}
	 */
	public boolean isLessThan() {
		return less;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AlertThreshold) {
			AlertThreshold al = (AlertThreshold) obj;
			return (al.less==this.less) && (GlobalData.AMOUNT_COMPARATOR.compare(al.balance,this.balance)==0);
		} else {
			return super.equals(obj);
		}
	}

	/** Tests whether this threshlod is able to generate an alert.
	 * @return true if it can generete alerts, false otherwise (example: alert is set for less than negative infinity)
	 * @see AlertThreshold#NO
	 */
	public boolean isLifeless() {
		return ((balance==Double.NEGATIVE_INFINITY) && less) || ((balance==Double.POSITIVE_INFINITY) && !less);
	}
	
	/** Tests whether an amount triggers the alert. 
	 * @param amount The amount to test
	 * @return true if the alert is triggered.
	 */
	public boolean isTriggered(double amount) {
		int comparison = GlobalData.AMOUNT_COMPARATOR.compare(balance-amount, 0.0);
		return (less && (comparison>0)) || (!less && (comparison<0));
	}
	
	@Override
	public int hashCode() {
		if (isLifeless()) return 1;
		return (int) this.balance;
	}
}