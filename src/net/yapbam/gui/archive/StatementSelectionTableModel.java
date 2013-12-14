package net.yapbam.gui.archive;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.ArrayUtils;

@SuppressWarnings("serial")
class StatementSelectionTableModel extends AbstractTableModel implements TableModel {
	private final int alertColumn;
	private final int accountColumn;
	private final int ignoredColumn;
	private final int statementColumn;
	
	private GlobalData data;
	private boolean[] ignored;
	private String[] selectedStatements;
	private Statement[][] statements;
	private CharSequence[] alerts;

	public StatementSelectionTableModel(GlobalData data, CharSequence[] alerts) {
		this.data = data;
		this.statements = new Statement[data.getAccountsNumber()][];
		this.ignored = new boolean[data.getAccountsNumber()];
		for (int i = 0; i < statements.length; i++) {
			this.statements[i] = Statement.getStatements(data.getAccount(i));
		}
		Arrays.fill(ignored, true);
		this.selectedStatements = new String[this.ignored.length];
		this.alerts = alerts.clone();
		if (ArrayUtils.isAllNull(alerts)) {
			this.alertColumn = -1;
		} else {
			this.alertColumn = 0;
		}
		this.accountColumn = this.alertColumn + 1;
		this.ignoredColumn = this.accountColumn + 1;
		this.statementColumn = this.ignoredColumn +1;
	}

	@Override
	public int getRowCount() {
		return this.data.getAccountsNumber();
	}

	@Override
	public int getColumnCount() {
		return this.statementColumn+1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==alertColumn) {
			return this.alerts[rowIndex]==null ? null : IconManager.get(Name.ALERT);
		} else if (columnIndex==accountColumn) {
			return data.getAccount(rowIndex).getName();
		} else if (columnIndex==ignoredColumn) {
			return ignored[rowIndex];
		} else if (columnIndex==statementColumn) {
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
		if (columnIndex==this.alertColumn) {
			return Icon.class;
		} else if (columnIndex==accountColumn) {
			return String.class;
		} else if (columnIndex==ignoredColumn) {
			return Boolean.class;
		} else {
			return super.getColumnClass(columnIndex);
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return hasStatement(rowIndex) && ((columnIndex==ignoredColumn) || (columnIndex==statementColumn));
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex==ignoredColumn) {
			this.ignored[rowIndex] = ((Boolean)aValue);
		} else if (columnIndex==statementColumn) {
			this.selectedStatements[rowIndex] = (String) aValue;
			this.ignored[rowIndex] = false;
		}
		this.fireTableRowsUpdated(rowIndex, rowIndex);
	}

	@Override
	public String getColumnName(int column) {
		if (column == alertColumn) {
			return ""; //$NON-NLS-1$
		} else if (column == accountColumn) {
			return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		} else if (column == ignoredColumn) {
			return LocalizationData.get("Archive.statementSelection.ignored.title"); //$NON-NLS-1$
		} else if (column == statementColumn) {
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
				if (statements[index][i].getId().equals(selectedStatements[index])) {
					break;
				}
			}
			return result;
		} else {
			return Collections.emptySet();
		}
	}

	public boolean[] isSelectedAccount() {
		boolean[] result = new boolean[data.getAccountsNumber()];
		for (int i = 0; i < result.length; i++) {
			result[i] = this.selectedStatements[i] != null;
		}
		return result;
	}

	int getAlertColumn() {
		return this.alertColumn;
	}

	int getStatementColumn() {
		return this.statementColumn;
	}

	int getAccountColumn() {
		return this.accountColumn;
	}

	boolean hasAlert(int row) {
		return this.alerts[row] != null;
	}
}
