package net.yapbam.gui.transactiontable;

import static org.junit.Assert.assertNotEquals;

import java.util.Comparator;

import javax.swing.table.TableRowSorter;

import org.junit.Test;

import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;

public class TransactionTableTest {

	@Test
	public void column4ComparatorComparesAllArrayElementsNotJustTheFirstOne() {
		GlobalData data = new GlobalData();
		data.add(new Account("account", 0.0)); //$NON-NLS-1$
		FilteredData filteredData = new FilteredData(data);
		TransactionTable table = new TransactionTable(filteredData);

		// Column 4 is always either the amount or the receipt column (see TableSettings):
		// the constructor must not silently overwrite the comparator registered for it.
		@SuppressWarnings("unchecked")
		Comparator<double[]> comparator = (Comparator<double[]>) ((TableRowSorter<?>) table.getRowSorter()).getComparator(4);

		double[] o1 = {1.0, 2.0};
		double[] o2 = {1.0, 3.0};
		assertNotEquals("Column 4 comparator must compare beyond the first array element", 0, comparator.compare(o1, o2));
	}
}
