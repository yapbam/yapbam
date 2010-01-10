package net.yapbam.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.DefaultListenable;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.util.DateUtils;

/** This class represents a budget based on the filtered transactions.
 *  This budget can be built on a "per month" or a "par year" basis.
 */
public class BudgetView extends DefaultListenable {
	private FilteredData data;
	private boolean year;
	private HashMap<Key, Double> values;
	private Calendar firstDate;
	private Calendar lastDate;
	private List<Category> categories;
	
	private static final class Key {
		Date date;
		Category category;
		Key(Date date, Category category) {
			super();
			this.date = date;
			this.category = category;
		}
		@Override
		public boolean equals(Object obj) {
			Key key = (Key) obj;
			return this.date.equals(key.date) && this.category.equals(key.category);
		}
		@Override
		public int hashCode() {
			return category.hashCode()+date.hashCode();
		}
	}
	
	/** Constructor.
	 * @param data The filtered data on which to build the budget
	 * @param year true to construct a budget per year, false to have it per month
	 */
	public BudgetView(FilteredData data, boolean year) {
		this.data = data;
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				update();
			}
		});
		this.year = year;
		build();
	}
	
	/**
	 * Returns whether the budget is per year
	 * @return true if the budget is per year
	 */
	public boolean isYear() {
		return year;
	}

	/** Sets the "per year" or "per month" state of this budget.
	 * Budget is automatically computed when this attribute change.
	 * @param year true to set this budget on a per year basis.
	 */
	public void setYear(boolean year) {
		if (year!=this.year) {
			this.year = year;
			update();
		}
	}
	
	/** Returns the number of categories in the budget.
	 * @return an integer. 0 if the budget is empty.
	 */
	public int getCategoriesSize() {
		return this.categories.size();
	}
	
	/** Returns a category in the budget.
	 * The categories are sorted in their names ascending order.
	 * @param index the index of the category we are looking for
	 * @return a category
	 */
	public Category getCategory(int index) {
		return this.categories.get(index);
	}

	/** Returns the number of time periods in the budget.
	 * @return an integer. 0 if the budget is empty.
	 */
	public int getDatesSize() {
		if (this.firstDate==null) return 0;
		return 1+(this.year?this.lastDate.get(Calendar.YEAR)-this.firstDate.get(Calendar.YEAR):DateUtils.getMonthlyDistance(this.firstDate, this.lastDate));
	}
	
	/** Returns the beginning date of a time period in the budget.
	 * The time periods are sorted in the ascending order.
	 * @param index the index of the date we are looking for
	 * @return a date. The date is guaranteed to be the start of the time interval
	 * (example: January the first for a year)
	 * @see #getLastDate(int)
	 */
	@SuppressWarnings("deprecation")
	public Date getDate(int index) {
		if (index>=getDatesSize()) throw new ArrayIndexOutOfBoundsException(index);
		if (year) {
			return new Date(firstDate.get(Calendar.YEAR)-1900+index, 0, 1);
		} else {
			Calendar c = (Calendar) this.firstDate.clone();
			c.add(Calendar.MONTH, index);
			return c.getTime();
		}
	}


	/** Returns the end date of a time period in the budget.
	 * The time periods are sorted in the ascending order.
	 * @param index the index of the date we are looking for
	 * @return a date. The date is guaranteed to be the end of the time interval
	 * (example: December 31 for a year)
	 * @see #getDate(int)
	 */
	@SuppressWarnings("deprecation")
	public Date getLastDate(int index) {
		if (index>=getDatesSize()) throw new ArrayIndexOutOfBoundsException(index);
		if (year) {
			return new Date(firstDate.get(Calendar.YEAR)+index, 11, 31);
		} else {
			Calendar c = (Calendar) this.firstDate.clone();
			c.add(Calendar.MONTH, index);
			c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
			return c.getTime();
		}
	}
	
	/** Gets the amount for a date and a category.
	 * @param date a date returned by getDate(int) method
	 * @param category a category
	 * @return a double, 0 if there's no amount for this date and category.
	 * @see #getDate(int)
	 * @see #getCategory(int)
	 */
	public double getAmount(Date date, Category category) {
		Double result = values.get(new Key(date, category));
		return result==null?0:result;
	}
	
	/** Exports this budget to a text file.
	 * @param file that will receive the content.
	 * @param columnSeparator the character to use to separate columns
	 * @param The locale to use to export the dates and numbers
	 * @throws IOException
	 */
	public void export(File file, char columnSeparator, Locale locale) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		try {
			// Output header line
			DateFormat DateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
			for (int i = 0; i < getDatesSize(); i++) {
				out.append(columnSeparator);
				out.append(DateFormater.format(getDate(i)));
			}
			// Output category lines
			NumberFormat currencyFormatter = NumberFormat.getInstance(locale);
			if (currencyFormatter instanceof DecimalFormat) {
				// We don't use the currency instance, because it would have outputed some currency prefix or suffix, not very easy
				// to manipulate with an excel like application
				currencyFormatter.setMaximumFractionDigits(NumberFormat.getCurrencyInstance(locale).getMaximumFractionDigits());
			}
			for (int i = 0; i < categories.size(); i++) {
				out.newLine();
				out.append(categories.get(i).getName());
				for (int j = 0; j < getDatesSize(); j++) {
					out.append(columnSeparator);
					Double value = values.get(new Key(getDate(j), categories.get(i)));
					if (value!=null) {
						out.append(currencyFormatter.format(value));
					}
				}
			}
		} finally {
			out.close();
		}
	}

	/** Updates the budget and send related events. */
	private void update() {
		build();
		this.fireEvent(new EverythingChangedEvent(this));
	}

	/** Computes the budget. */
	private void build() {
		this.values = new HashMap<Key, Double>();
		this.firstDate = null;
		this.lastDate = null;
		this.categories = new LinkedList<Category>();
		
		for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			Date date = getNormalizedDate(transaction.getDate());
			for (int j = 0; j < transaction.getSubTransactionSize(); j++) {
				SubTransaction subTransaction = transaction.getSubTransaction(j);
				if (this.data.isOk(subTransaction)) {
					add (new Key(date, subTransaction.getCategory()), subTransaction.getAmount());
				}
			}
			if (this.data.isComplementOk(transaction)) {
				add (new Key(date, transaction.getCategory()), transaction.getComplement());
			}
		}
	}

	private void add(Key key, double amount) {
		if (amount!=0) {
			// Insert the date in the budget (refresh first and last date)
			Calendar c = new GregorianCalendar();
			c.setTime(key.date);
			if (firstDate==null) { // There's currently no date in the budget
				this.firstDate = c;
				this.lastDate = this.firstDate;
			} else {
				if (DateUtils.getMonthlyDistance(firstDate, c)<0) this.firstDate = c;
				else if (DateUtils.getMonthlyDistance(lastDate, c)>0) this.lastDate = c;
			}
			// Insert the category in the budget
			addToSortedList(categories, key.category);
			// Add the amount to that category/date item
			Double value = this.values.get(key);
			if (value==null) value = 0.0;
			value += amount;
			this.values.put (key, value);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void addToSortedList(List list, Object element) {
		int index = Collections.binarySearch(list, element);
		if (index<0) list.add(-index-1, element);
	}

	private Date getNormalizedDate(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		if (year) c.set(Calendar.MONTH, 0);
		return c.getTime();
	}
}
