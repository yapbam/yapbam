package net.yapbam.gui.archive;

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
class AccountTableModel extends AbstractTableModel implements TableModel {
	private static final int ACCOUNT_COLUMN = 0;
	private static final int EXPORT_COLUMN = 1;
	public static final int STATEMENT_COLUMN = 2;
	
	private GlobalData data;
	private boolean[] selected;
	private String[] selectedStatements;
	private Statement[][] statements;

	public AccountTableModel(GlobalData data) {
		this.data = data;
		this.statements = new Statement[data.getAccountsNumber()][];
		this.selected = new boolean[data.getAccountsNumber()];
		for (int i = 0; i < statements.length; i++) {
			this.statements[i] = Statement.getStatements(data.getAccount(i));
		}
		Arrays.fill(selected, false);
		this.selectedStatements = new String[this.selected.length];
	}

	@Override
	public int getRowCount() {
		return this.data.getAccountsNumber();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==ACCOUNT_COLUMN) return data.getAccount(rowIndex).getName();
		if (columnIndex==EXPORT_COLUMN) return selected[rowIndex];
		if (columnIndex==STATEMENT_COLUMN) {
			if (!hasStatement(rowIndex)) return "There is no statement in this account"; 
			return (!selected[rowIndex] || (this.selectedStatements[rowIndex]==null))?"-":this.selectedStatements[rowIndex];
		}
		return null;
	}

	boolean hasStatement(int index) {
		if (getStatements(index).length>=2) return true;
		return getStatements(index)[0].getId()!=null;
	}

	Statement[] getStatements(int index) {
		return statements[index];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==EXPORT_COLUMN) return Boolean.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return hasStatement(rowIndex) && ((columnIndex==EXPORT_COLUMN) || (columnIndex==STATEMENT_COLUMN));
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex==EXPORT_COLUMN) {
			this.selected[rowIndex] = ((Boolean)aValue);
		} else if (columnIndex==STATEMENT_COLUMN) {
			this.selectedStatements[rowIndex] = (String) aValue;
			this.selected[rowIndex] = true;
		}
		this.fireTableRowsUpdated(rowIndex, rowIndex);
	}

	@Override
	public String getColumnName(int column) {
		if (column == ACCOUNT_COLUMN) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		if (column == EXPORT_COLUMN) return "Export";
		if (column == STATEMENT_COLUMN) return "Statement";
		return super.getColumnName(column);
	}
	
	void setAllExported(boolean exported) {
		if (!exported) {
			Arrays.fill(this.selected, exported);
		} else {
			for (int i = 0; i < this.selected.length; i++) {
				this.selected[i] = this.hasStatement(i);
			}
		}
		fireTableRowsUpdated(0, getRowCount()-1);
	}
}
