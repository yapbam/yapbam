package net.yapbam.data;

/** An alert threshold when balance reach an amount.
 */
public class AlertThreshold {
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

	/** Gets whether the alert is for balance lower than balance threshold or not.
	 * @return true if alert is set for balance lower than the result of {@link #getBalance()}, false for balance greater than {@link #getBalance()}
	 */
	public boolean isLessThan() {
		return less;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AlertThreshold) {
			AlertThreshold al = (AlertThreshold) obj;
			return (al.less==this.less) && (al.balance==this.balance);
		} else {
			return super.equals(obj);
		}
	}

	/** Gets whether this threshlod is able to generate an alert.
	 * @return true if it can generete alerts, false otherwise (example: alert is set for less than negative infinity)
	 * @see AlertThreshold#NO
	 */
	public boolean isLifeless() {
		return ((balance==Double.NEGATIVE_INFINITY) && less) || ((balance==Double.POSITIVE_INFINITY) && !less);
	}
	
	@Override
	public int hashCode() {
		if (isLifeless()) return 1;
		return (int) this.balance;
	}
}