package net.yapbam.gui.archive;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
class StatementSelectionTableModel extends AbstractTableModel implements TableModel {
	private static final int ACCOUNT_COLUMN = 0;
	private static final int IGNORED_COLUMN = 1;
	public static final int STATEMENT_COLUMN = 2;
	
	private GlobalData data;
	private boolean[] ignored;
	private String[] selectedStatements;
	private Statement[][] statements;

	public StatementSelectionTableModel(GlobalData data) {
		this.data = data;
		this.statements = new Statement[data.getAccountsNumber()][];
		this.ignored = new boolean[data.getAccountsNumber()];
		for (int i = 0; i < statements.length; i++) {
			this.statements[i] = Statement.getStatements(data.getAccount(i));
		}
		Arrays.fill(ignored, true);
		this.selectedStatements = new String[this.ignored.length];
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
		if (columnIndex==ACCOUNT_COLUMN) {
			return data.getAccount(rowIndex).getName();
		} else if (columnIndex==IGNORED_COLUMN) {
			return ignored[rowIndex];
		} else if (columnIndex==STATEMENT_COLUMN) {
			if (!hasStatement(rowIndex)) {
				return LocalizationData.get("Archive.statementSelection.nothingSelected");  //$NON-NLS-1$
			} else {
				return (ignored[rowIndex] || (this.selectedStatements[rowIndex]==null))?"-":this.selectedStatements[rowIndex]; //$NON-NLS-1$
			}
		} else {
			return null;
		}
	}

	boolean hasStatement(int index) {
		return (getStatements(index).length>=2) || (getStatements(index)[0].getId()!=null);
	}

	Statement[] getStatements(int index) {
		return statements[index];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return (columnIndex==IGNORED_COLUMN) ? Boolean.class : super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return hasStatement(rowIndex) && ((columnIndex==IGNORED_COLUMN) || (columnIndex==STATEMENT_COLUMN));
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex==IGNORED_COLUMN) {
			this.ignored[rowIndex] = ((Boolean)aValue);
		} else if (columnIndex==STATEMENT_COLUMN) {
			this.selectedStatements[rowIndex] = (String) aValue;
			this.ignored[rowIndex] = false;
		}
		this.fireTableRowsUpdated(rowIndex, rowIndex);
	}

	@Override
	public String getColumnName(int column) {
		if (column == ACCOUNT_COLUMN) {
			return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		} else if (column == IGNORED_COLUMN) {
			return LocalizationData.get("Archive.statementSelection.ignored.title"); //$NON-NLS-1$
		} else if (column == STATEMENT_COLUMN) {
			return LocalizationData.get("Archive.statementSelection.until.title"); //$NON-NLS-1$
		} else {
			return super.getColumnName(column);
		}
	}
	
	void setAllExported(boolean exported) {
		if (!exported) {
			Arrays.fill(this.ignored, true);
		} else {
			for (int i = 0; i < this.ignored.length; i++) {
				this.ignored[i] = !this.hasStatement(i);
			}
		}
		fireTableRowsUpdated(0, getRowCount()-1);
	}

	Set<String> getSelectedStatements(int index) {
		if (!this.ignored[index] && (this.selectedStatements[index]!=null)) {
			HashSet<String> result = new HashSet<String>();
			for (int i = 0; i < statements[index].length; i++) {
				result.add(statements[index][i].getId());
				if (statements[index][i].getId().equals(selectedStatements[index])) break;
			}
			return result;
		} else {
			return null;
		}
	}
}
