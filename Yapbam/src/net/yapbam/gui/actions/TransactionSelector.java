package net.yapbam.gui.actions;

import java.beans.PropertyChangeListener;

import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;

/** A transaction selector : Something able to select a transaction.
 * @see DeleteTransactionAction
 */
public interface TransactionSelector {
	/** When the selection changes, this selector must throw a PropertyChangedEvent.
	 * This constant is the name of that property.
	 */
	public static final String SELECTED_PROPERTY = "selectedTransaction";
	public Transaction[] getSelectedTransactions();
	public void setSelectedTransactions(Transaction[] transactions);
	public FilteredData getFilteredData();
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
