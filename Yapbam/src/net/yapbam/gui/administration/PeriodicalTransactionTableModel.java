package net.yapbam.gui.administration;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.MessageFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.Category;
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

@SuppressWarnings("serial")
final class PeriodicalTransactionTableModel extends GenericTransactionTableModel {
	private final PeriodicalTransactionListPanel periodicTransactionListPanel;

	PeriodicalTransactionTableModel(PeriodicalTransactionListPanel periodicTransactionListPanel) {
		this.periodicTransactionListPanel = periodicTransactionListPanel;
		this.getFilteredData().addListener(new DataListener() {
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
		if (columnIndex==0) return LocalizationData.get("Transaction.0"); //$NON-NLS-1$
		if (columnIndex==1) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		if (columnIndex==2) return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		if (columnIndex==3) return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		if (columnIndex==4) return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
		if (columnIndex==5) return LocalizationData.get("Transaction.mode"); //$NON-NLS-1$
		if (columnIndex==6) return LocalizationData.get("PeriodicalTransactionManager.nextDate.column"); //$NON-NLS-1$
		if (columnIndex==7) return LocalizationData.get("PeriodicalTransactionManager.period.column"); //$NON-NLS-1$
		if (columnIndex==8) return LocalizationData.get("PeriodicalTransactionManager.active.column"); //$NON-NLS-1$
		return "?"; //$NON-NLS-1$
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==0) return SpreadState.class;
		if (columnIndex==6) return Date.class;
		if (columnIndex==3) return double[].class;
		if (columnIndex==8) return Boolean.class;
		return String.class;
	}

	//TODO à fusionner avec TransactionsTableModel
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		boolean spread = this.isSpread(rowIndex);
		PeriodicalTransaction transaction = (PeriodicalTransaction) this.getTransaction(rowIndex);
		if (columnIndex==0) return new SpreadState(transaction.getSubTransactionSize()!=0, spread);
		else if (columnIndex==1) return transaction.getAccount().getName();
		else if (columnIndex==2) {
			if (spread) {
				StringBuilder buf = new StringBuilder("<html><body>").append(transaction.getDescription()); //$NON-NLS-1$
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					buf.append("<BR>&nbsp;&nbsp;").append(transaction.getSubTransaction(i).getDescription()); //$NON-NLS-1$
				}
				if (transaction.getComplement()!=0) {
					buf.append("<BR>&nbsp;&nbsp;").append(LocalizationData.get("Transaction.14")); //$NON-NLS-1$ //$NON-NLS-2$
				}
				buf.append("</body></html>"); //$NON-NLS-1$
				return buf.toString();
			} else {
				return transaction.getDescription();
			}
		} else if (columnIndex==3) {
			if (spread) {
				double complement = transaction.getComplement();
				int numberOfLines = transaction.getSubTransactionSize()+1;
				if (complement!=0) numberOfLines++;
				double[] result = new double[numberOfLines];
				result[0] = transaction.getAmount();
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					result[i+1] = transaction.getSubTransaction(i).getAmount();
				}
				if (complement!=0) result[result.length-1] = complement;
				return result;
			} else {
				return new double[]{transaction.getAmount()};
			}
		} else if (columnIndex==4) {
			if (spread) {
				StringBuilder buf = new StringBuilder("<html><body>").append(getName(transaction.getCategory())); //$NON-NLS-1$
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					buf.append("<BR>&nbsp;&nbsp;").append(getName(transaction.getSubTransaction(i).getCategory())); //$NON-NLS-1$
				}
				if (transaction.getComplement()!=0) {
					buf.append("<BR>&nbsp;&nbsp;").append(getName(transaction.getCategory())); //$NON-NLS-1$
				}
				buf.append("</body></html>"); //$NON-NLS-1$
				return buf.toString();
			} else {
				return getName(transaction.getCategory());
			}
		} else if (columnIndex==5) {
			Mode mode = transaction.getMode();
			return mode.equals(Mode.UNDEFINED) ? "" : mode.getName(); //$NON-NLS-1$
		} else if (columnIndex==6) {
			return transaction.getNextDate();
		} else if (columnIndex==7) {
			DateStepper period = transaction.getNextDateBuilder();
			String result;
			if (period instanceof DayDateStepper) {
				result = MessageFormat.format(LocalizationData.get("PeriodicalTransactionManager.period.daily.content"), ((DayDateStepper)period).getStep()); //$NON-NLS-1$
			} else if (period instanceof MonthDateStepper) {
				result = MessageFormat.format(LocalizationData.get("PeriodicalTransactionManager.period.monthly.content"), ((MonthDateStepper)period).getPeriod(), ((MonthDateStepper)period).getDay()); //$NON-NLS-1$
			} else {
				result = ""; //$NON-NLS-1$
			}
			if ((period!=null) && (period.getLastDate()!=null)) {
				result = MessageFormat.format(LocalizationData.get("PeriodicalTransactionManager.period.until"), result, period.getLastDate()); //$NON-NLS-1$
			}
			return result;
		} else if (columnIndex==8) return transaction.isEnabled();
		throw new IllegalArgumentException();
	}

	@Override
	public int getRowCount() {
		return getFilteredData().getGlobalData().getPeriodicalTransactionsNumber();
	}

	@Override
	public int getColumnCount() {
		return 9;
	}

	private Object getName(Category category) {
		return category.equals(Category.UNDEFINED) ? "" : category.getName(); //$NON-NLS-1$
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public int getAlignment(int column) {
    	if ((column==1) || (column==2)) return SwingConstants.LEFT;
    	if (column==3) return SwingConstants.RIGHT;
    	else return SwingConstants.CENTER;
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
}