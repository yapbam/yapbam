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
		return this.budget.getDatesSize();
	}

	@Override
	public int getRowCount() {
		return this.budget.getCategoriesSize();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Double value;
		if (rowIndex==this.budget.getCategoriesSize()) { // If this is the date sums line
			if (columnIndex<this.budget.getDatesSize()) { // If this is a date column
				value = this.budget.getSum(this.budget.getDate(columnIndex));
			} else { // If this is another column
				value = 0.0; //TODO
			}
		} else if (columnIndex>=this.budget.getDatesSize()) {
			Category category = this.budget.getCategory(rowIndex);
			value = isSumColumn(columnIndex)?budget.getSum(category):budget.getAverage(category);
		} else {
			Date date = this.budget.getDate(columnIndex);
			Category category = this.budget.getCategory(rowIndex);
			value = this.budget.getAmount(date, category);
		}
		return ((value==null)||(value==0.0))?"":LocalizationData.getCurrencyInstance().format(value);
	}
	
	private boolean isSumColumn(int columnIndex) {
		//TODO
		return columnIndex==budget.getDatesSize();
	}

//	private boolean isAverageColumn(int columnIndex) {
//		//TODO
//		return columnIndex==budget.getDatesSize()+1;
//	}

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
		} else if (isSumColumn(column)) {
			return LocalizationData.get("BudgetPanel.sum");
		} else {
			return LocalizationData.get("BudgetPanel.average");
		}
	}
}
