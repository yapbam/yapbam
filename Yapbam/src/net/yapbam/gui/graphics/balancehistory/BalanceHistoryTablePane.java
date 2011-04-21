package net.yapbam.gui.graphics.balancehistory;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable.PrintMode;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import net.yapbam.data.BalanceData;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.statementview.CellRenderer;
import net.yapbam.gui.util.AbstractDialog;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.SafeJFileChooser;
import net.yapbam.gui.widget.JLabelMenu;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;

public class BalanceHistoryTablePane extends JPanel {
	private static final long serialVersionUID = 1L;
	private FriendlyTable table;
	private BalanceData data;

	/**
	 * Creates the panel.
	 * @param data the data to be displayed
	 */
	public BalanceHistoryTablePane(FilteredData data) {
		this.data = data.getBalanceData();
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 0);
		gbc_scrollPane.weighty = 1.0;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);
		
		table = new FriendlyTable();
		table.setDefaultRenderer(Object.class, new CellRenderer());
		table.setModel(new MyModel(this.data));
		scrollPane.setViewportView(table);
		
		JLabel lblSortBy = new JLabelMenu("Sort by:") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void fillPopUp(JPopupMenu popup) {
				boolean valueDateSelected = true; //TODO
				JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem("Value date", valueDateSelected);
				popup.add(menuItem);
				menuItem = new JRadioButtonMenuItem("Transaction's date", !valueDateSelected);
				popup.add(menuItem);
			}
		};
		lblSortBy.setToolTipText("Ce menu permet de trier les opérations par date ou date de valeur"); //LOCAL
		GridBagConstraints gbc_lblSortBy = new GridBagConstraints();
		gbc_lblSortBy.insets = new Insets(0, 5, 0, 5);
		gbc_lblSortBy.gridx = 0;
		gbc_lblSortBy.gridy = 1;
		add(lblSortBy, gbc_lblSortBy);
		lblSortBy.setVisible(false); //TODO ... maybe
		
		JLabel label = table.getShowHideColumnsMenu(LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
		label.setToolTipText(LocalizationData.get("MainFrame.showColumns.ToolTip")); //$NON-NLS-1$
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 5, 0, 5);
		gbc_label.anchor = GridBagConstraints.NORTHWEST;
		gbc_label.gridx = 1;
		gbc_label.gridy = 1;
		add(label, gbc_label);
		
		final JButton btnExport = new JButton(LocalizationData.get("BudgetPanel.export")); //$NON-NLS-1$
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new SafeJFileChooser(btnExport.getText());
				chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
				chooser.updateUI();
				File file = chooser.showSaveDialog(AbstractDialog.getOwnerWindow(btnExport))==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null; //$NON-NLS-1$
				if (file!=null) {
					try {
						table.export(file, new DefaultExporter(LocalizationData.getLocale()));
					} catch (IOException e1) {
						ErrorManager.INSTANCE.display(btnExport, e1);
					}
				}
			}
		});
		btnExport.setToolTipText("BudgetPanel.export.toolTip"); //$NON-NLS-1$
		btnExport.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.anchor = GridBagConstraints.EAST;
		gbc_btnExport.weightx = 1.0;
		gbc_btnExport.insets = new Insets(0, 0, 5, 5);
		gbc_btnExport.gridx = 2;
		gbc_btnExport.gridy = 1;
		add(btnExport, gbc_btnExport);
	}

	public void saveState() {
		YapbamState.INSTANCE.saveState(table, this.getClass().getCanonicalName());
	}

	public void restoreState() {
		YapbamState.INSTANCE.restoreState(table, this.getClass().getCanonicalName());
	}

	private final class DefaultExporter implements FriendlyTable.ExportFormat {
		private DateFormat dateFormater;
		private NumberFormat currencyFormat;

		private DefaultExporter (Locale locale) {
			dateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
			currencyFormat = NumberFormat.getInstance(locale);
			if (currencyFormat instanceof DecimalFormat) {
				// We don't use the currency instance, because it would have outputed some currency prefix or suffix, not very easy
				// to manipulate with an excel like application
				currencyFormat.setMaximumFractionDigits(NumberFormat.getCurrencyInstance(locale).getMaximumFractionDigits());
			}
		}
		
		@Override
		public boolean hasHeader() {
			return true;
		}

		@Override
		public char getSeparator() {
			return '\t';
		}

		@Override
		public String format(Object obj) {
			if (obj instanceof Date) return dateFormater.format(obj);
			if (obj instanceof Double) return currencyFormat.format(obj);
			return obj.toString();
		}
	}

	/** The transaction's table model. */
	private final class MyModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private BalanceData data;

		/** Constructor. */
		public MyModel(BalanceData data) {
			super();
			this.data = data;
			data.addListener(new DataListener() {
				@Override
				public void processEvent(DataEvent event) {
					fireTableDataChanged();
				}
			});
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Transaction transaction = getTransaction(rowIndex);
			if (columnIndex==0) return transaction.getAccount().getName();
			if (columnIndex==1) return transaction.getDate();
			if (columnIndex==2) return transaction.getDescription();
			if (columnIndex==3) return transaction.getAmount();
			if (columnIndex==4) return transaction.getCategory().getName();
			if (columnIndex==5) return transaction.getMode().getName();
			if (columnIndex==6) return transaction.getNumber();
			if (columnIndex==7) return transaction.getValueDate();
			if (columnIndex==8) return transaction.getStatement();
			if (columnIndex==9) return getRemaining(rowIndex);
			return "?"; //$NON-NLS-1$
		}

		/** Gets the remaining amount after a transaction.
		 * @param rowIndex The transaction's row index
		 * @return a Double
		 */
		private Double getRemaining(int rowIndex) {
			Date valueDate = getTransaction(rowIndex).getValueDate();
			double balance = data.getBalanceHistory().getBalance(valueDate);
			for (int i=rowIndex+1;i<getRowCount();i++) {
				Transaction transaction = getTransaction(i);
				if (transaction.getValueDate().equals(valueDate)) {
					balance = balance - transaction.getAmount();
				} else break;
			}
			return balance;
		}

		/** Gets the transaction corresponding to a table row.
		 * @param rowIndex The transaction's row index
		 * @return a Transaction
		 */
		private Transaction getTransaction(int rowIndex) {
			return data.getBalanceHistory().getTransaction(rowIndex);
		}

		@Override
		public int getRowCount() {
			return data.getBalanceHistory().getTransactionsNumber();
		}

		@Override
		public int getColumnCount() {
			return 10;
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==0) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
			if (columnIndex==1) return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
			if (columnIndex==2) return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
			if (columnIndex==3) return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
			if (columnIndex==4) return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
			if (columnIndex==5) return LocalizationData.get("Transaction.mode"); //$NON-NLS-1$
			if (columnIndex==6) return LocalizationData.get("Transaction.number"); //$NON-NLS-1$
			if (columnIndex==7) return LocalizationData.get("Transaction.valueDate"); //$NON-NLS-1$
			if (columnIndex==8) return LocalizationData.get("Transaction.statement"); //$NON-NLS-1$
			if (columnIndex==9) return LocalizationData.get("BalanceHistory.transaction.remainingBalance"); //$NON-NLS-1$
			return "?"; //$NON-NLS-1$
		}
	}

	public Printable getPrintable() {
		return table.getPrintable(PrintMode.FIT_WIDTH, null, null);
	}
}
