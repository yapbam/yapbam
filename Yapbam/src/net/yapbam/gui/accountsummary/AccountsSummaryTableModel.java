package net.yapbam.gui.accountsummary;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.event.CategoryAddedEvent;
import net.yapbam.data.event.CategoryPropertyChangedEvent;
import net.yapbam.data.event.CategoryRemovedEvent;
import net.yapbam.data.event.CheckbookAddedEvent;
import net.yapbam.data.event.CheckbookPropertyChangedEvent;
import net.yapbam.data.event.CheckbookRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
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
	private static final long serialVersionUID = 1L;
	private static final Set<Class<? extends DataEvent>> IGNORED_EVENTS;
	private GlobalData data;
	
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
	}
	
	AccountsSummaryTableModel(JTable table, GlobalData data) {
		super();
		this.data = data;
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (!IGNORED_EVENTS.contains(event.getClass())) {
					fireTableDataChanged();
				}
			}
		});
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnIndex==0 ? Object.class : Double.class;
	}

	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) {
			return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		} else if (columnIndex==1) {
			return LocalizationData.get("AccountsSummary.CurrentBalance"); //$NON-NLS-1$
		} else if (columnIndex==2) {
			return LocalizationData.get("AccountsSummary.FinalBalance"); //$NON-NLS-1$
		} else if (columnIndex==3) {
			return LocalizationData.get("AccountsSummary.CheckedBalance"); //$NON-NLS-1$
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	public int getRowCount() {
		return data==null ? 0 : data.getAccountsNumber();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0) {
			return data.getAccount(rowIndex).getName();
		} else if (columnIndex==1) {
			return data.getAccount(rowIndex).getBalanceData().getCurrentBalance();
		} else if (columnIndex==2) {
			return data.getAccount(rowIndex).getBalanceData().getFinalBalance();
		} else if (columnIndex==3) {
			return data.getAccount(rowIndex).getBalanceData().getCheckedBalance();
		} else {
			return null;
		}
	}
}
