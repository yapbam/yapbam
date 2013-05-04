package net.yapbam.data;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.yapbam.data.event.*;
import net.yapbam.util.DateUtils;

/** A budget based on the filtered data.
 *  <br>This budget can be built on a "per month" or a "per year" basis.
 *  @see FilteredData
 */
public class BudgetView extends DefaultListenable {
	private FilteredData data;
	private boolean year;
	private HashMap<Key, Double> values;
	private Calendar firstDate;
	private Calendar lastDate;
	private List<Category> categories;
	private HashMap<Category, Double> categoryToSum;
	private HashMap<Date, Double> dateToSum;
	private double sum;
	private boolean groupSubCategories;
	
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
				if (! isNeutral(event)) update();
			}
		});
		this.year = year;
		this.groupSubCategories = false;
		build();
	}
	
	/** Tests whether an event may have any impact on the budget view.
	 * <br>For instance, NeedToBeSavedChangedEvent has no effect on the budgetView, but TransactionsAddedEvent has.
	 * @param event The event to test
	 * @return true if the event has no effect on this view
	 */
	private boolean isNeutral(DataEvent event) {
		return (event instanceof NeedToBeSavedChangedEvent) || (event instanceof PasswordChangedEvent) || (event instanceof URIChangedEvent) 
			|| (event instanceof AccountAddedEvent) || (event instanceof AccountRemovedEvent) || (event instanceof AccountPropertyChangedEvent)
			|| (event instanceof ModeAddedEvent) || (event instanceof ModeRemovedEvent) || (event instanceof ModePropertyChangedEvent)
			|| (event instanceof CheckbookPropertyChangedEvent) || (event instanceof CheckbookAddedEvent) || (event instanceof CheckbookRemovedEvent)
			|| (event instanceof PeriodicalTransactionsAddedEvent) || (event instanceof PeriodicalTransactionsRemovedEvent);
	}
	
	/** Sets the sub-categories grouped attribute.
	 * @param grouped true to merge sub-categories.
	 */
	public void setGroupedSubCategories(boolean grouped) {
		if (grouped!=this.groupSubCategories) {
			this.groupSubCategories = grouped;
			update();
		}
	}
	
	/**
	 * Tests whether the budget is per year
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
	
	/** Gets the number of categories in the budget.
	 * @return an integer. 0 if the budget is empty.
	 */
	public int getCategoriesSize() {
		return this.categories.size();
	}
	
	/** Gets a category in the budget.
	 * The categories are sorted in their names ascending order.
	 * @param index the index of the category we are looking for
	 * @return a category
	 */
	public Category getCategory(int index) {
		return this.categories.get(index);
	}

	/** Gets the number of time periods in the budget.
	 * @return an integer. 0 if the budget is empty.
	 */
	public int getDatesSize() {
		if (this.firstDate==null) return 0;
		return 1+(this.year?this.lastDate.get(Calendar.YEAR)-this.firstDate.get(Calendar.YEAR):DateUtils.getMonthlyDistance(this.firstDate, this.lastDate));
	}
	
	/** Gets the beginning date of a time period in the budget.
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


	/** Gets the end date of a time period in the budget.
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
	 * @return a double, null if there's no amount for this date and category.
	 * @see #getDate(int)
	 * @see #getCategory(int)
	 */
	public Double getAmount(Date date, Category category) {
		return values.get(new Key(date, category));
	}
	
	/** Gets the sum of amounts of all categories for a date.
	 * @param date a date returned by getDate(int) method
	 * @return a double, 0 if there's no amount for this date (or if the sum is 0 !).
	 * @see #getDate(int)
	 */
	public double getSum(Date date) {
		Double result = this.dateToSum.get(date);
		return result==null?0.0:result;
	}
	
	/** Gets the sum of amounts of all dates for a category.
	 * @param category a category
	 * @return a double, 0 if there's no amount for this category (or if the sum is 0 !).
	 * @see #getCategory(int)
	 */
	public double getSum(Category category) {
		Double result = this.categoryToSum.get(category);
		return result==null?0.0:result;
	}
	
	/** Gets the amount average of all dates for a category.
	 * @param category a category
	 * @return a double, 0 if there's no amount for this category (or if the average is 0 !).
	 * @see #getCategory(int)
	 */
	public double getAverage(Category category) {
		if (getDatesSize()==0) return 0.0;
		return getSum(category)/getDatesSize();
	}

	/** Updates the budget and send related events. */
	private void update() {
		build();
		this.fireEvent(new EverythingChangedEvent(this));
	}

	/** Computes the budget. */
	private void build() {
		this.values = new HashMap<Key, Double>();
		this.categoryToSum = new HashMap<Category, Double>();
		this.dateToSum = new HashMap<Date, Double>();
		this.firstDate = null;
		this.lastDate = null;
		this.categories = new LinkedList<Category>();
		this.sum = 0.0;
		
		HashMap<Category, Category> catMap = new HashMap<Category, Category>();
		for (int i = 0; i < data.getGlobalData().getCategoriesNumber(); i++) {
			Category cat = data.getGlobalData().getCategory(i);
			catMap.put(cat, groupSubCategories?cat.getSuperCategory(data.getGlobalData().getSubCategorySeparator()):cat);
		}
		
		for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			Date date = getNormalizedDate(transaction.getDate());
			for (int j = 0; j < transaction.getSubTransactionSize(); j++) {
				SubTransaction subTransaction = transaction.getSubTransaction(j);
				if (this.data.isOk(subTransaction)) {
					add (new Key(date, catMap.get(subTransaction.getCategory())), subTransaction.getAmount());
					this.sum = this.sum + subTransaction.getAmount();
				}
			}
			if (this.data.isComplementOk(transaction)) {
				add (new Key(date, catMap.get(transaction.getCategory())), transaction.getComplement());
				this.sum = this.sum + transaction.getComplement();
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
			int index = Collections.binarySearch(categories, key.category);
			if (index<0) categories.add(-index-1, key.category);
			// Add the amount to that category/date item
			Double value = this.values.get(key);
			if (value==null) value = 0.0;
			value += amount;
			this.values.put (key, value);
			// Add the amount to that category sum
			value = this.categoryToSum.get(key.category);
			if (value==null) value = 0.0;
			value += amount;
			this.categoryToSum.put (key.category, value);
			// Add the amount to that date sum
			value = this.dateToSum.get(key.date);
			if (value==null) value = 0.0;
			value += amount;
			this.dateToSum.put (key.date, value);
		}
	}

	private Date getNormalizedDate(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		if (year) c.set(Calendar.MONTH, 0);
		return c.getTime();
	}
	
	/** Gets the sum of all amounts contained in this view.
	 * @return a Double
	 */
	public Double getSum() {
		return this.sum;
	}
}
