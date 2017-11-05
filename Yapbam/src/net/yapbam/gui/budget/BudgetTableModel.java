package net.yapbam.gui.budget;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import com.fathzer.soft.ajlib.swing.table.TitledRowsTableModel;

import net.yapbam.data.BudgetView;
import net.yapbam.data.Category;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
class BudgetTableModel extends AbstractTableModel implements TitledRowsTableModel {
	private BudgetView budget;
	private boolean hasExtraLine;
	private boolean hasExtraColumn;

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
		int count = this.budget.getDatesSize();
		if (hasExtraColumn) {
			count++;
		}
		return count;
	}

	@Override
	public int getRowCount() {
		int count = this.budget.getCategoriesSize();
		if (hasExtraLine) {
			count++;
		}
		return count;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Double value;
		boolean isSumColumn = (columnIndex==budget.getDatesSize());
		if (rowIndex==this.budget.getCategoriesSize()) { // If this is the date sums line
			if (!isSumColumn) { // If this is a date column
				value = this.budget.getSum(this.budget.getDate(columnIndex));
			} else { // If this is sum of sums cell
				value = budget.getSum();
			}
		} else if (isSumColumn) { // If this is the category sum column
			Category category = this.budget.getCategory(rowIndex);
			value = budget.getSum(category);
		} else {
			Date date = this.budget.getDate(columnIndex);
			Category category = this.budget.getCategory(rowIndex);
			value = this.budget.getAmount(date, category);
		}
		return ((value==null)||(value==0.0))?"":LocalizationData.getCurrencyInstance().format(value); //$NON-NLS-1$
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getColumnName(int column) {
		int datesSize = this.budget.getDatesSize();
		if (column<datesSize) {
			Date date = this.budget.getDate(column);
			if (this.budget.isYear()) {
				return ""+(date.getYear()+1900); //$NON-NLS-1$
			}
			//TODO It would be better to have a localized version for this formatter ...
			// but I can't find how to do that (simple with the day, but not documented without)
			return new SimpleDateFormat("yyyy/MM").format(date); //$NON-NLS-1$
		} else {
			return LocalizationData.get("BudgetPanel.sum"); //$NON-NLS-1$
		}
	}
	
	public void setHasSumLine(boolean hasExtraLine) {
		if (hasExtraLine!=this.hasExtraLine) {
			this.hasExtraLine = hasExtraLine;
			fireTableStructureChanged();
		}
	}
	
	public void setHasSumColumn(boolean hasExtraColumn) {
		if (hasExtraColumn!=this.hasExtraColumn) {
			this.hasExtraColumn = hasExtraColumn;
			fireTableStructureChanged();
		}
	}

	@Override
	public int getTitlesColumnCount() {
		return 1;
	}

	@Override
	public String getRowTitle(int rowIndex, int columnIndex) {
		if (rowIndex==budget.getCategoriesSize()) {
			return LocalizationData.get("BudgetPanel.sum"); //$NON-NLS-1$
		} else {
			Category category = budget.getCategory(rowIndex);
			return category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName(); //$NON-NLS-1$
		}
	}
}
