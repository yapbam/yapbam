package net.yapbam.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/** This class represents the balance history.
 */
public class BalanceHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	private static final Comparator<Object> COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			if (o1 instanceof BalanceHistoryElement) {
				return -((BalanceHistoryElement) o1).getRelativePosition((Date)o2);
			} else {
				return ((BalanceHistoryElement) o2).getRelativePosition((Date)o1);
			}
		}	
	};
	
	private boolean minMaxAccurate;
	private double minBalance;
	private double maxBalance;
	private ArrayList<BalanceHistoryElement> elements;
	private double precision;
	
	public BalanceHistory(double intialBalance) {
		super();
		this.minMaxAccurate = false;
		this.elements = new ArrayList<BalanceHistoryElement>();
		this.elements.add(new BalanceHistoryElement(intialBalance, null, null));
		this.setCurrency(Currency.getInstance(Locale.getDefault()));
	}
	
	/** Set the currency.
	 * As the balances are represented with doubles (which are not able to represents the exact value of a decimal number),
	 * and as we try to merge  contiguous history elements with the same amount, it's necessary to know what we mean by "same amount".
	 * Same amount means equivalent for a specific currency (as currencies don't have the same number of fraction digits). 
	 * @param currency The currency
	 */
	public void setCurrency(Currency currency) {
		this.precision = Math.pow(10, -currency.getDefaultFractionDigits())/2;		
	}
	
	public double getMinBalance() {
		refreshMinMax();
		return this.minBalance;
	}

	public double getMaxBalance() {
		refreshMinMax();
		return this.maxBalance;
	}
	
	private void refreshMinMax() {
		if (!minMaxAccurate) {
			this.maxBalance = get(0).getBalance();
			this.minBalance = this.maxBalance;
			for (Iterator<BalanceHistoryElement> iterator = elements.iterator(); iterator.hasNext();) {
				double balance = iterator.next().getBalance();
				if (this.maxBalance<balance) this.maxBalance = balance;
				else if (this.minBalance>balance) this.minBalance = balance;
			}
			minMaxAccurate = true;
		}
	}

	/** Returns the number of history elements (period with the same balance)
	 * @return the number of periods.
	 */
	public int size() {
		return this.elements.size();
	}
	
	public BalanceHistoryElement get(int index) {
		return this.elements.get(index);
	}

	private int find(Date date) {
		return Collections.binarySearch(this.elements, date, COMPARATOR);
	}

	public double getBalance(Date date) {
		return get(find(date)).getBalance();
	}

	/** Add an amount to the history at a specified date
	 * @param amount amount to add (may be negative)
	 * @param date date or null if the amount has to be added at the beginning of times
	 *  (ie the initial balance of a newly created account)
	 */
	public void add(double amount, Date date) {
		if (date==null) {
			if (minMaxAccurate) {
				this.minBalance += amount;
				this.maxBalance += amount;
			}
			for (Iterator<BalanceHistoryElement> iterator = elements.iterator(); iterator.hasNext();) {
				iterator.next().add(amount);
			}
		} else {
			int index = find(date);
			BalanceHistoryElement element = get(index);
			if (!date.equals(element.getFrom())) {
				// not at the beginning of the period, need to split the element
				BalanceHistoryElement el2 = new BalanceHistoryElement(element.getBalance(), date, element.getTo());
				element.setTo(date);
				index++;
				this.elements.add(index, el2);
			} else {
				// Maybe the amount is now the same as previous element.
				// In such a case, we have to merge these elements
				BalanceHistoryElement previous = this.elements.get(index-1);
				double future = element.getBalance()+amount;
				// WARNING : a simple test like "previous.getBalance()==future" is wrong because of the loss of precision
				// of double representation. We have to test that the difference is less than the currency precision
				if (Math.abs(previous.getBalance()-future)<precision) {
					this.elements.remove(index);
					previous.setTo(element.getTo());
				}
			}
			for (int i = index; i < this.elements.size(); i++) {
				element = this.elements.get(i);
				element.add(amount);
			}
			minMaxAccurate = false;
		}
	}
}
