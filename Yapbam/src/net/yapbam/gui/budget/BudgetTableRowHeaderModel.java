package net.yapbam.gui.budget;

import javax.swing.table.AbstractTableModel;

import net.yapbam.data.BudgetView;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
class BudgetTableRowHeaderModel extends AbstractTableModel {
	private BudgetView budget;
	private boolean hasExtraLine;
	
	BudgetTableRowHeaderModel(BudgetView budget) {
		super();
		this.budget = budget;
		this.budget.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				fireTableStructureChanged();
			}
		});
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		int count = budget.getCategoriesSize();
		if (hasExtraLine) count++;
		return count;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex==budget.getCategoriesSize()) {
			return LocalizationData.get("BudgetPanel.sum");
		} else {
			return budget.getCategory(rowIndex).getName();
		}
	}
	
	public void setHasExtraLine(boolean hasExtraLine) {
		if (this.hasExtraLine!=hasExtraLine) {
			this.hasExtraLine = hasExtraLine;
			fireTableStructureChanged();
		}
	}
}
