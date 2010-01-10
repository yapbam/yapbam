package net.yapbam.budget;

import javax.swing.table.AbstractTableModel;

import net.yapbam.data.BudgetView;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;

@SuppressWarnings("serial")
class BudgetTableRowHeaderModel extends AbstractTableModel {
	BudgetView budget;
	
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
		return budget.getCategoriesSize();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return budget.getCategory(rowIndex);
	}
/*
	@Override
	public String getColumnName(int column) {
		return "";
	}
*/
}
