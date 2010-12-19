package net.yapbam.gui.budget;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import net.yapbam.data.BudgetView;
import net.yapbam.data.Category;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
class BudgetTableModel extends AbstractTableModel {
	private BudgetView budget;

	BudgetTableModel (BudgetView budget) {
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
		return this.budget.getDatesSize()+2; //TODO (depends on check boxes set up)
	}

	@Override
	public int getRowCount() {
		return this.budget.getCategoriesSize()+1; //TODO (depends on check boxes set up)
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if ((columnIndex>=this.budget.getDatesSize()) || (rowIndex==this.budget.getCategoriesSize())) {
			return 0; //TODO (average and sum)
		} else {
			Date date = this.budget.getDate(columnIndex);
			Category category = this.budget.getCategory(rowIndex);
			Double value = this.budget.getAmount(date, category);
			return ((value==null)||(value==0.0))?"":LocalizationData.getCurrencyInstance().format(value);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getColumnName(int column) {
		int datesSize = this.budget.getDatesSize();
		if (column<datesSize) {
			Date date = this.budget.getDate(column);
			if (this.budget.isYear()) return ""+(date.getYear()+1900);
			//TODO It would be better to have a localized version for this formatter ...
			// but I can't find how to do that (simple with the day, but not documented without)
			return new SimpleDateFormat("yyyy/MM").format(date);
		} else if (column==datesSize) {
			return LocalizationData.get("BudgetPanel.sum"); //TODO
		} else {
			return LocalizationData.get("BudgetPanel.average"); //TODO
		}
	}
}
