package net.yapbam.gui.dialogs.transaction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;

public abstract class AbstractEditionWizard<T> {
	private static final int MILLIS_PER_DAY = 60000 * 24;
	private GlobalData data;
	private String description;
	private T value;
	private boolean inited;
	
	protected AbstractEditionWizard(GlobalData data, String description) {
		this.data = data;
		this.description = description;
		this.value = null;
		this.inited = false;
	}

	protected abstract T getValue(Transaction transaction);
	
	/** Gets the ranking of a transaction, based on its date.
	 * This method is used by the wizard to determine which descriptions are the most probable.
	 * @param transaction The transaction
	 * @return a double
	 */
	public static double getRankingBasedOnDate(long now, Transaction transaction) {
		// we will use a function between 0 (for very, very old ones) and 1 for recent one.
		// Probably this function could be improved ...
		long time = Math.abs(transaction.getDate().getTime() - now) / MILLIS_PER_DAY;
		return 2 / Math.sqrt(time + 4);
	}
	
	public static <V> V getHeaviest(Map<V, Double> map) {
		V ct = null;
		double max = 0.0;
		for (Iterator<Entry<V, Double>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<V, Double> next = iterator.next();
			if (next.getValue() > max) {
				ct = next.getKey();
				max = next.getValue();
			}
		}
		return ct;
	}
	
	public T get() {
		if (!inited) {
			long now = System.currentTimeMillis();
			HashMap<T, Double> toProbability = new HashMap<T, Double>();
			for (int i = 0; i < data.getTransactionsNumber(); i++) {
				Transaction transaction = data.getTransaction(i);
				if (transaction.getDescription().equalsIgnoreCase(description)) {
					T transactionValue = getValue(transaction);
					if (transactionValue != null) {
						Double weight = toProbability.get(transactionValue);
						double transactionWeight = getRankingBasedOnDate(now, transaction);
						toProbability.put(transactionValue, transactionWeight + (weight == null ? 0 : weight));
					}
				}
			}
			value = getHeaviest(toProbability);
		}
		return value;
	}
}
