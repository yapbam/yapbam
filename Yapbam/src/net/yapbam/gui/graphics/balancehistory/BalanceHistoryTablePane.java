package net.yapbam.gui.graphics.balancehistory;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import net.yapbam.data.BalanceData;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.statementview.CellRenderer;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.widget.JLabelMenu;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

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
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
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
		lblSortBy.setToolTipText("Ce menu permet de trier les opérations par date ou date de valeur");
		GridBagConstraints gbc_lblSortBy = new GridBagConstraints();
		gbc_lblSortBy.insets = new Insets(0, 5, 5, 5);
		gbc_lblSortBy.gridx = 0;
		gbc_lblSortBy.gridy = 1;
		add(lblSortBy, gbc_lblSortBy);
		
		JLabel label = table.getShowHideColumnsMenu(LocalizationData.get("MainFrame.showColumns"));
		label.setToolTipText(LocalizationData.get("MainFrame.showColumns.ToolTip"));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.gridx = 1;
		gbc_label.gridy = 1;
		add(label, gbc_label);
		
		JButton btnExport = new JButton("Export");
		btnExport.setToolTipText("Ce bouton exporte le contenu du tableau ci-dessus");
		btnExport.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.anchor = GridBagConstraints.EAST;
		gbc_btnExport.weightx = 1.0;
		gbc_btnExport.insets = new Insets(0, 0, 5, 0);
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

	private final class MyModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private BalanceData data;

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
			Transaction transaction = data.getBalanceHistory().getTransaction(rowIndex);
			if (columnIndex==0) return transaction.getAccount().getName();
			if (columnIndex==1) return transaction.getDate();
			if (columnIndex==2) return transaction.getDescription();
			if (columnIndex==3) return transaction.getAmount();
			if (columnIndex==4) return transaction.getCategory().getName();
			if (columnIndex==5) return transaction.getMode().getName();
			if (columnIndex==6) return transaction.getNumber();
			if (columnIndex==7) return transaction.getValueDate();
			if (columnIndex==8) return transaction.getStatement();
			if (columnIndex==9) return "?"; //TODO
			return "?";
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
			if (columnIndex==9) return "Solde restant";
			return "?"; //$NON-NLS-1$
		}
	}
}
