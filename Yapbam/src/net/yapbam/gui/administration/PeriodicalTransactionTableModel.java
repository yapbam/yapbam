package net.yapbam.gui.administration;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.MessageFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.fathzer.jlocal.Formatter;

import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.FilteredData;
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
final class PeriodicalTransactionTableModel extends GenericTransactionTableModel {
	private final PeriodicalTransactionListPanel periodicTransactionListPanel;
	private PeriodicalTransactionsTableSettings settings;

	PeriodicalTransactionTableModel(PeriodicalTransactionListPanel periodicTransactionListPanel) {
		this.periodicTransactionListPanel = periodicTransactionListPanel;
		this.settings = new PeriodicalTransactionsTableSettings();
		this.getFilteredData().getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (event instanceof PeriodicalTransactionsAddedEvent) {
					fireTableDataChanged();
				} else if (event instanceof PeriodicalTransactionsRemovedEvent) {
					for (PeriodicalTransaction transaction : ((PeriodicalTransactionsRemovedEvent)event).getRemoved()) {
						setSpread(transaction, false);
					}
					fireTableDataChanged();
				} else if (event instanceof EverythingChangedEvent) {
					clearSpreadData();
					fireTableDataChanged();
				}
			}
		});
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
		PeriodicalTransaction transaction = (PeriodicalTransaction) this.getTransaction(rowIndex);
		if (columnIndex==settings.getSpreadColumn()) {
			if (transaction.getSubTransactionSize()==0) {
				return SpreadState.NOT_SPREADABLE;
			} else {
				return spread ? SpreadState.SPREAD : SpreadState.NOT_SPREAD;
			}
		} else if (columnIndex==settings.getAccountColumn()) {
			return transaction.getAccount().getName();
		} else if (columnIndex==settings.getDescriptionColumn()) {
			return TransactionTableUtils.getDescription(transaction, spread, !settings.isCommentSeparatedFromDescription());
		} else if (columnIndex==settings.getCommentColumn()) {
			return transaction.getComment();
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
					result = LocalizationData.get("PeriodicalTransactionManager.period.daily.singular.content");
				} else {
					result = MessageFormat.format(LocalizationData.get("PeriodicalTransactionManager.period.daily.content"), ((DayDateStepper)period).getStep()); //$NON-NLS-1$
				}
			} else if (period instanceof MonthDateStepper) {
				MonthDateStepper monthlyPeriod = (MonthDateStepper)period;
				if (monthlyPeriod.getPeriod() % 12 == 0) {
					// Yearly period
					if (monthlyPeriod.getPeriod() == 12) {
						result = MessageFormat.format(LocalizationData.get("PeriodicalTransactionManager.period.yearly.singular.content"), monthlyPeriod.getDay()); //$NON-NLS-1$
					} else {
						result = MessageFormat.format(LocalizationData.get("PeriodicalTransactionManager.period.yearly.content"), monthlyPeriod.getPeriod()/12, monthlyPeriod.getDay()); //$NON-NLS-1$
					}
				} else if (monthlyPeriod.getPeriod()==1) {
					result = MessageFormat.format(LocalizationData.get("PeriodicalTransactionManager.period.monthly.singular.content"), monthlyPeriod.getDay()); //$NON-NLS-1$
				} else {
					result = MessageFormat.format(LocalizationData.get("PeriodicalTransactionManager.period.monthly.content"), monthlyPeriod.getPeriod(), monthlyPeriod.getDay()); //$NON-NLS-1$
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
		return getFilteredData().getGlobalData().getPeriodicalTransactionsNumber();
	}

	@Override
	public int getColumnCount() {
		return settings.getColumnCount();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public int getAlignment(int column) {
    	if ((column==settings.getDescriptionColumn()) || (column==settings.getCommentColumn()) || (column==settings.getAccountColumn())) {
    		return SwingConstants.LEFT;
    	} else if (column==settings.getAmountColumn() || column==settings.getReceiptColumn() || column==settings.getExpenseColumn()) {
    		return SwingConstants.RIGHT;
    	} else {
    		return SwingConstants.CENTER;
    	}
	}

	FilteredData getFilteredData() {
		return this.periodicTransactionListPanel.data;
	}

	@Override
	protected AbstractTransaction getTransaction(int rowIndex) {
		return this.periodicTransactionListPanel.data.getGlobalData().getPeriodicalTransaction(rowIndex);
	}

	@Override
	public void setRowLook(Component renderer, JTable table, int row, boolean isSelected) {
		super.setRowLook(renderer, table, row, isSelected);
		boolean ended = ((PeriodicalTransaction) this.getTransaction(row)).getNextDate() == null;
		Font font = renderer.getFont().deriveFont(ended ? Font.ITALIC : Font.PLAIN);
		if (ended) {
			renderer.setForeground(Color.GRAY);
		}
		renderer.setFont(font);
	}

	public PeriodicalTransactionsTableSettings getTableSettings() {
		return settings;
	}
}