package net.yapbam.gui.dialogs;

import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;

abstract class AbstractPredefinedComputer implements PredefinedDescriptionComputer {
	protected GlobalData data;
	/** The instance creation time in the same scale than System.currentTimeMillis. */
	protected long now;
	private Map<String, Double> map;

	protected AbstractPredefinedComputer (GlobalData data) {
		this.data = data;
		this.now = System.currentTimeMillis();
	}

	@Override
	/** Gets the groups sizes.
	 * @return 3 by default.
	 */
	public int getUnsortedSize() {
		return 3;
	}

	@Override
	public String[] getPredefined() {
		this.map = new HashMap<String, Double>();
		for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			process (transaction);
		}
		// Sort the map by ranking
		LinkedList<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(map.entrySet());
		Collections.sort(list, new Comparator<Object>() {
			@Override
			@SuppressWarnings("unchecked")
			public int compare(Object o1, Object o2) {
				return -((Comparable<Double>) ((Map.Entry<String, Double>) (o1)).getValue()).compareTo(((Map.Entry<String, Double>) (o2)).getValue());
			}
		});
		String[] array = new String[list.size()];
		Iterator<Map.Entry<String, Double>> iterator = list.iterator();
		for (int i = 0; i < array.length; i++) {
			array[i] = iterator.next().getKey();
		}
		return array;
	}
	
	protected abstract void process(Transaction transaction);

	protected final void add (String value, double ranking) {
		Double current = map.get(value);
		if (current==null) {
			map.put(value, ranking);
		} else {
			map.put(value, (ranking + current));
		}
	}
}
