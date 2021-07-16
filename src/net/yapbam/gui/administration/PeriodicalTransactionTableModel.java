package net.yapbam.gui.administration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.fathzer.jlocal.Formatter;

import net.yapbam.data.Filter;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.PeriodicalTransactionsAddedEvent;
import net.yapbam.data.event.PeriodicalTransactionsRemovedEvent;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.date.helpers.DayDateStepper;
import net.yapbam.date.helpers.MonthDateStepper;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.transactiontable.GenericTransactionTableModel;
import net.yapbam.gui.transactiontable.SpreadState;
import net.yapbam.gui.transactiontable.TransactionTableUtils;

@SuppressWarnings("serial")
final class PeriodicalTransactionTableModel extends GenericTransactionTableModel<PeriodicalTransaction> {
	private final PeriodicalTransactionListPanel periodicTransactionListPanel;
	private PeriodicalTransactionsTableSettings settings;
	private boolean ignoreFilter;
	private List<PeriodicalTransaction> transactions;

	PeriodicalTransactionTableModel(PeriodicalTransactionListPanel periodicTransactionListPanel) {
		this.periodicTransactionListPanel = periodicTransactionListPanel;
		this.ignoreFilter = true;
		this.settings = new PeriodicalTransactionsTableSettings();
		this.transactions = new ArrayList<PeriodicalTransaction>();
		this.getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (event instanceof PeriodicalTransactionsAddedEvent) {
					updateTransactionList();
				} else if (event instanceof PeriodicalTransactionsRemovedEvent) {
					for (PeriodicalTransaction transaction : ((PeriodicalTransactionsRemovedEvent)event).getRemoved()) {
						setSpread(transaction, false);
					}
					updateTransactionList();
				} else if (event instanceof EverythingChangedEvent) {
					clearSpreadData();
					transactions.clear();
					updateTransactionList();
				}
			}
		});
		this.periodicTransactionListPanel.data.getFilter().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if (!ignoreFilter) {
					updateTransactionList();
				}
			}
		});
		updateTransactionList();
	}
	
	private void updateTransactionList() {
		Filter filter = ignoreFilter?null:this.periodicTransactionListPanel.data.getFilter();
		List<PeriodicalTransaction> oldList = new ArrayList<PeriodicalTransaction>(this.transactions);
		this.transactions.clear();
		for (int i = 0; i<getGlobalData().getPeriodicalTransactionsNumber(); i++) {
			PeriodicalTransaction tr = getGlobalData().getPeriodicalTransaction(i);
			if (filter==null || !filter.isActive() || filter.isOk(tr)) {
				transactions.add(tr);
			}
		}
		if (filter!=null && filter.isActive()) {
			for (PeriodicalTransaction tr : oldList) {
				if (!transactions.contains(tr)) {
					setSpread(tr, false);
				}
			}
		}
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==settings.getSpreadColumn()) {
			return LocalizationData.get("Transaction.0"); //$NON-NLS-1$
		} else if (columnIndex==settings.getAccountColumn()) {
			return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		} else if (columnIndex==settings.getDescriptionColumn()) {
			return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		} else if (columnIndex==settings.getCommentColumn()) {
			return LocalizationData.get("Transaction.comment"); //$NON-NLS-1$
		} else if (columnIndex==settings.getAmountColumn()) {
			return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		} else if (columnIndex==settings.getReceiptColumn()) {
			return LocalizationData.get("StatementView.receipt"); //$NON-NLS-1$
		} else if (columnIndex==settings.getExpenseColumn()) {
			return LocalizationData.get("StatementView.debt"); //$NON-NLS-1$
		} else if (columnIndex==settings.getCategoryColumn()) {
			return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
		} else if (columnIndex==settings.getModeColumn()) {
			return LocalizationData.get("Transaction.mode"); //$NON-NLS-1$
		} else if (columnIndex==settings.getNextDateColumn()) {
			return LocalizationData.get("PeriodicalTransactionManager.nextDate.column"); //$NON-NLS-1$
		} else if (columnIndex==settings.getPeriodColumn()) {
			return LocalizationData.get("PeriodicalTransactionManager.period.column"); //$NON-NLS-1$
		} else if (columnIndex==settings.getActiveColumn()) {
			return LocalizationData.get("PeriodicalTransactionManager.active.column"); //$NON-NLS-1$
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==settings.getSpreadColumn()) {
			return SpreadState.class;
		} else if (columnIndex==settings.getNextDateColumn()) {
			return Date.class;
		} else if (columnIndex==settings.getAmountColumn() || columnIndex==settings.getReceiptColumn() || columnIndex==settings.getExpenseColumn()) {
			return double[].class;
		} else if (columnIndex==settings.getActiveColumn()) {
			return Boolean.class;
		} else {
			return String.class;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		boolean spread = this.isSpread(rowIndex);
		PeriodicalTransaction transaction = this.getTransaction(rowIndex);
		if (columnIndex==settings.getSpreadColumn()) {
			if (transaction.getSubTransactionSize()==0) {
				return SpreadState.NOT_SPREADABLE;
			} else {
				return spread ? SpreadState.SPREAD : SpreadState.NOT_SPREAD;
			}
		} else if (columnIndex==settings.getAccountColumn()) {
			return transaction.getAccount().getName();
		} else if (columnIndex==settings.getDescriptionColumn()) {
			return TransactionTableUtils.getDescription(transaction, spread, !settings.isCommentSeparatedFromDescription(), true);
		} else if (columnIndex==settings.getCommentColumn()) {
			return TransactionTableUtils.getComment(transaction);
		} else if (columnIndex==settings.getAmountColumn()) {
			return TransactionTableUtils.getAmount(transaction, spread);
		} else if (columnIndex==settings.getReceiptColumn()) {
			return TransactionTableUtils.getExpenseReceipt(transaction, spread, false);
		} else if (columnIndex==settings.getExpenseColumn()) {
			return TransactionTableUtils.getExpenseReceipt(transaction, spread, true);
		} else if (columnIndex==settings.getCategoryColumn()) {
			return TransactionTableUtils.getCategory(transaction, spread);
		} else if (columnIndex==settings.getModeColumn()) {
			Mode mode = transaction.getMode();
			return mode.equals(Mode.UNDEFINED) ? "" : mode.getName(); //$NON-NLS-1$
		} else if (columnIndex==settings.getNextDateColumn()) {
			return transaction.getNextDate();
		} else if (columnIndex==settings.getPeriodColumn()) {
			DateStepper period = transaction.getNextDateBuilder();
			String result;
			if (period instanceof DayDateStepper) {
				if (((DayDateStepper) period).getStep()==1) {
					result = LocalizationData.get("PeriodicalTransactionManager.period.daily.singular.content"); //$NON-NLS-1$
				} else {
					result = Formatter.format(LocalizationData.get("PeriodicalTransactionManager.period.daily.content"), ((DayDateStepper)period).getStep()); //$NON-NLS-1$
				}
			} else if (period instanceof MonthDateStepper) {
				MonthDateStepper monthlyPeriod = (MonthDateStepper)period;
				if (monthlyPeriod.getPeriod() % 12 == 0) {
					// Yearly period
					if (monthlyPeriod.getPeriod() == 12) {
						result = Formatter.format(LocalizationData.get("PeriodicalTransactionManager.period.yearly.singular.content"), monthlyPeriod.getDay()); //$NON-NLS-1$
					} else {
						result = Formatter.format(LocalizationData.get("PeriodicalTransactionManager.period.yearly.content"), monthlyPeriod.getPeriod()/12, monthlyPeriod.getDay()); //$NON-NLS-1$
					}
				} else if (monthlyPeriod.getPeriod()==1) {
					result = Formatter.format(LocalizationData.get("PeriodicalTransactionManager.period.monthly.singular.content"), monthlyPeriod.getDay()); //$NON-NLS-1$
				} else {
					result = Formatter.format(LocalizationData.get("PeriodicalTransactionManager.period.monthly.content"), monthlyPeriod.getPeriod(), monthlyPeriod.getDay()); //$NON-NLS-1$
				}
			} else {
				result = ""; //$NON-NLS-1$
			}
			if ((period!=null) && (period.getLastDate()!=null)) {
				result = Formatter.format(LocalizationData.get("PeriodicalTransactionManager.period.until"), result, period.getLastDate()); //$NON-NLS-1$
			}
			return result;
		} else if (columnIndex==settings.getActiveColumn()) {
			return transaction.isEnabled();
		}
		throw new IllegalArgumentException();
	}

	@Override
	public int getRowCount() {
		return transactions.size();
	}

	@Override
	public int getColumnCount() {
		return settings.getColumnCount();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// Cells that allow to click on HTML links should be editable
		return (columnIndex==settings.getDescriptionColumn() || columnIndex==settings.getCommentColumn()) && columnIndex>=0;
	}

	GlobalData getGlobalData() {
		return this.periodicTransactionListPanel.data.getGlobalData();
	}

	@Override
	public PeriodicalTransaction getTransaction(int rowIndex) {
		return transactions.get(rowIndex);
	}

	public PeriodicalTransactionsTableSettings getTableSettings() {
		return settings;
	}

	public void setIgnoreFilter(boolean ignore) {
		if (ignore!=this.ignoreFilter) {
			this.ignoreFilter = ignore;
			updateTransactionList();
		}
	}
}