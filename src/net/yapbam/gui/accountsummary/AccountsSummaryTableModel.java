package net.yapbam.gui.accountsummary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.CategoryAddedEvent;
import net.yapbam.data.event.CategoryPropertyChangedEvent;
import net.yapbam.data.event.CategoryRemovedEvent;
import net.yapbam.data.event.CheckbookAddedEvent;
import net.yapbam.data.event.CheckbookPropertyChangedEvent;
import net.yapbam.data.event.CheckbookRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.IsArchivedChangedEvent;
import net.yapbam.data.event.IsLockedChangedEvent;
import net.yapbam.data.event.ModeAddedEvent;
import net.yapbam.data.event.ModePropertyChangedEvent;
import net.yapbam.data.event.ModeRemovedEvent;
import net.yapbam.data.event.NeedToBeSavedChangedEvent;
import net.yapbam.data.event.PasswordChangedEvent;
import net.yapbam.data.event.PeriodicalTransactionsAddedEvent;
import net.yapbam.data.event.PeriodicalTransactionsRemovedEvent;
import net.yapbam.data.event.URIChangedEvent;
import net.yapbam.gui.LocalizationData;

class AccountsSummaryTableModel extends AbstractTableModel {
	static final int SELECT_COLUMN = 0;
	static final int ACCOUNT_COLUMN = 1;
	static final int CURRENT_BALANCE_COLUMN = 2;
	static final int FINAL_BALANCE_COLUMN = 3;
	static final int CHECKED_BALANCE_COLUMN = 4;
	static final int NB_TRANSACTIONS_COLUMN = 5;
	static final int NB_UNCHECKED_TRANSACTIONS_COLUMN = 6;
	
	private static final long serialVersionUID = 1L;
	private static final Set<Class<? extends DataEvent>> IGNORED_EVENTS;
	private GlobalData data;
	private List<Boolean> accountSelected;
	
	static {
		IGNORED_EVENTS = new HashSet<Class<? extends DataEvent>>();
		IGNORED_EVENTS.add(CategoryAddedEvent.class);
		IGNORED_EVENTS.add(CategoryRemovedEvent.class);
		IGNORED_EVENTS.add(CategoryPropertyChangedEvent.class);
		IGNORED_EVENTS.add(CheckbookAddedEvent.class);
		IGNORED_EVENTS.add(CheckbookRemovedEvent.class);
		IGNORED_EVENTS.add(CheckbookPropertyChangedEvent.class);
		IGNORED_EVENTS.add(ModeAddedEvent.class);
		IGNORED_EVENTS.add(ModeRemovedEvent.class);
		IGNORED_EVENTS.add(ModePropertyChangedEvent.class);
		IGNORED_EVENTS.add(NeedToBeSavedChangedEvent.class);
		IGNORED_EVENTS.add(PasswordChangedEvent.class);
		IGNORED_EVENTS.add(PeriodicalTransactionsAddedEvent.class);
		IGNORED_EVENTS.add(PeriodicalTransactionsRemovedEvent.class);
		IGNORED_EVENTS.add(URIChangedEvent.class);
		IGNORED_EVENTS.add(IsLockedChangedEvent.class);
		IGNORED_EVENTS.add(IsArchivedChangedEvent.class);
	}
	
	AccountsSummaryTableModel(JTable table, GlobalData data) {
		super();
		this.data = data;
		this.accountSelected = new ArrayList<Boolean>();
		this.setAllAccountSelected();
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (!IGNORED_EVENTS.contains(event.getClass())) {
					if (event instanceof EverythingChangedEvent) {
						setAllAccountSelected();
					} else if (event instanceof AccountAddedEvent) {
						accountSelected.add(Boolean.TRUE);
					} else if (event instanceof AccountRemovedEvent) {
						int index = ((AccountRemovedEvent)event).getIndex();
						accountSelected.remove(index);
					}
					fireTableDataChanged();
				}
			}
		});
	}

	private void setAllAccountSelected() {
		accountSelected.clear();
		for (int i = 0; i < AccountsSummaryTableModel.this.data.getAccountsNumber(); i++) {
			accountSelected.add(Boolean.TRUE);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==SELECT_COLUMN) {
			return Boolean.class;
		} else if (columnIndex==ACCOUNT_COLUMN) {
			return Object.class;
		} else if (columnIndex==NB_TRANSACTIONS_COLUMN || columnIndex==NB_UNCHECKED_TRANSACTIONS_COLUMN) {
			return Long.class;
		} else {
			return Double.class;
		}
	}

	public int getColumnCount() {
		return 7;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==SELECT_COLUMN) {
			return ""; //$NON-NLS-1$
		} else if (columnIndex==ACCOUNT_COLUMN) {
			return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		} else if (columnIndex==CURRENT_BALANCE_COLUMN) {
			return LocalizationData.get("AccountsSummary.CurrentBalance"); //$NON-NLS-1$
		} else if (columnIndex==FINAL_BALANCE_COLUMN) {
			return LocalizationData.get("AccountsSummary.FinalBalance"); //$NON-NLS-1$
		} else if (columnIndex==CHECKED_BALANCE_COLUMN) {
			return LocalizationData.get("AccountsSummary.CheckedBalance"); //$NON-NLS-1$
		} else if (columnIndex==NB_TRANSACTIONS_COLUMN) {
			return LocalizationData.get("AccountsSummary.TransactionsNumber"); //$NON-NLS-1$
		} else if (columnIndex==NB_UNCHECKED_TRANSACTIONS_COLUMN) {
			return LocalizationData.get("MainMenuBar.notChecked"); //$NON-NLS-1$
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	public int getRowCount() {
		return data==null ? 0 : data.getAccountsNumber();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==SELECT_COLUMN) {
			return this.accountSelected.get(rowIndex);
		} else if (columnIndex==ACCOUNT_COLUMN) {
			return data.getAccount(rowIndex).getName();
		} else if (columnIndex==CURRENT_BALANCE_COLUMN) {
			return data.getAccount(rowIndex).getBalanceData().getCurrentBalance();
		} else if (columnIndex==FINAL_BALANCE_COLUMN) {
			return data.getAccount(rowIndex).getBalanceData().getFinalBalance();
		} else if (columnIndex==CHECKED_BALANCE_COLUMN) {
			return data.getAccount(rowIndex).getBalanceData().getCheckedBalance();
		} else if (columnIndex==NB_TRANSACTIONS_COLUMN) {
			return data.getAccount(rowIndex).getTransactionsNumber();
		} else if (columnIndex==NB_UNCHECKED_TRANSACTIONS_COLUMN) {
			return data.getAccount(rowIndex).getUncheckedTransactionsNumber();
		} else {
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return rowIndex<getRowCount() && columnIndex==SELECT_COLUMN;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		this.accountSelected.set(rowIndex, (Boolean) aValue);
//		fireTableCellUpdated(rowIndex, columnIndex);
//		fireTableRowsUpdated(getRowCount(), getRowCount()); //This line Hangs
		fireTableDataChanged(); //TODO Remove (this clears the selection and makes the screen flicky)
	}
}
