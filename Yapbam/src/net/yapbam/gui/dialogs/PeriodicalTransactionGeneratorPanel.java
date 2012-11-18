package net.yapbam.gui.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.astesana.ajlib.swing.widget.date.DateWidget;
import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.transactiontable.AmountRenderer;
import net.yapbam.gui.transactiontable.BooleanRenderer;
import net.yapbam.gui.transactiontable.DateRenderer;
import net.yapbam.gui.transactiontable.ObjectRenderer;
import net.yapbam.gui.util.JTableListener;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class PeriodicalTransactionGeneratorPanel extends JPanel {
	private static final String STATE_PROPERTIES_PREFIX = "net.yapbam.ihm.dialogs.PeriodicalGeneratorPanel.table."; //$NON-NLS-1$
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private DateWidget dateField = null;
	private JLabel summary = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private GenerateTableModel tableModel;

	private FilteredData data;

	/**
	 * This is the default constructor
	 */
	public PeriodicalTransactionGeneratorPanel(FilteredData data) {
		super();
		this.data = data;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getJPanel(), BorderLayout.NORTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);
		this.updateTransactions();
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jLabel = new JLabel();
			GridBagConstraints gbc_jLabel = new GridBagConstraints();
			gbc_jLabel.anchor = GridBagConstraints.WEST;
			gbc_jLabel.insets = new Insets(0, 0, 0, 5);
			gbc_jLabel.gridx = 0;
			gbc_jLabel.gridy = 0;
			jPanel.add(jLabel, gbc_jLabel);
			jLabel.setText(LocalizationData.get("GeneratePeriodicalTransactionsDialog.lastDate")); //$NON-NLS-1$
			GridBagConstraints gbc_dateField = new GridBagConstraints();
			gbc_dateField.anchor = GridBagConstraints.WEST;
			gbc_dateField.insets = new Insets(0, 0, 0, 5);
			gbc_dateField.gridx = 1;
			gbc_dateField.gridy = 0;
			jPanel.add(getDateField(), gbc_dateField);
			summary = new JLabel();
			GridBagConstraints gbc_summary = new GridBagConstraints();
			gbc_summary.weightx = 1.0;
			gbc_summary.fill = GridBagConstraints.HORIZONTAL;
			gbc_summary.anchor = GridBagConstraints.WEST;
			gbc_summary.gridwidth = 0;
			gbc_summary.gridx = 0;
			gbc_summary.gridy = 1;
			jPanel.add(summary, gbc_summary);
			summary.setText(" ");
		}
		return jPanel;
	}

	/**
	 * This method initializes dateField	
	 * 	
	 * @return net.yapbam.ihm.widget.DateWidget	
	 */
	private DateWidget getDateField() {
		if (dateField == null) {
			dateField = new DateWidget();
			dateField.setColumns(6);
			dateField.setToolTipText(LocalizationData.get("GeneratePeriodicalTransactionsDialog.lastDate.toolTip")); //$NON-NLS-1$
			dateField.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					updateTransactions();
				}
			});
		}
		return dateField;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	
	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	@SuppressWarnings("serial")
	private JTable getJTable() {
		if (jTable == null) {
			tableModel = new GenerateTableModel(data.getGlobalData());
			jTable = new JTable(tableModel);
			jTable.setDefaultRenderer(Date.class, new DateRenderer());
			jTable.setDefaultRenderer(double[].class, new AmountRenderer());
			jTable.setDefaultRenderer(Boolean.class, new BooleanRenderer());
			jTable.setDefaultRenderer(Object.class, new ObjectRenderer());
			jTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			YapbamState.INSTANCE.restoreState(jTable, STATE_PROPERTIES_PREFIX);
			//TODO It would be better to have a popup indicating that the transactions listed can be edited
			new JTableListener(jTable, null, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = jTable.convertRowIndexToModel(jTable.getSelectedRow());
					Transaction transaction = (Transaction) tableModel.getTransaction(row);
					transaction = TransactionDialog.open(data, AbstractDialog.getOwnerWindow(jTable), transaction, true, false, false);
					if (transaction!=null) {
						tableModel.setTransaction(row, transaction);
					}
				}
			});
		}
		return jTable;
	}
	
	void saveState() {
		YapbamState.INSTANCE.saveState(getJTable(), STATE_PROPERTIES_PREFIX);
	}
	
	void restoreState() {
		YapbamState.INSTANCE.restoreState(getJTable(), STATE_PROPERTIES_PREFIX);
	}
	
	private void updateTransactions() {
		Date endDate = dateField.getDate();
		Date lastDate = this.tableModel.getDate();
		boolean change = !NullUtils.areEquals(endDate, lastDate);
		if (change) {
			change = this.tableModel.setDate(endDate);
			if (change) {
				String message;
				if (endDate==null) {
					message = " "; //$NON-NLS-1$
				} else {
					double debts = 0;
					double receipts = 0;
					for (int i=0; i<this.tableModel.getRowCount(); i++) {
						AbstractTransaction transaction = this.tableModel.getTransaction(i);
						if (transaction.getAmount()>0) {
							receipts += transaction.getAmount();
						} else {
							debts += transaction.getAmount();
						}
					}
					message = MessageFormat.format(LocalizationData.get("GeneratePeriodicalTransactionsDialog.summary"), this.tableModel.getRowCount(), //$NON-NLS-1$
							LocalizationData.getCurrencyInstance().format(receipts), LocalizationData.getCurrencyInstance().format(-debts),
							LocalizationData.getCurrencyInstance().format(receipts+debts));
				}
				summary.setText(message);
			}
			Date old = lastDate;
			lastDate=endDate;
			this.firePropertyChange("endDate", old, lastDate); //$NON-NLS-1$
		}
	}

	Transaction[] getTransactions() {
		Transaction[] result = new Transaction[this.tableModel.getRowCount()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (Transaction) this.tableModel.getTransaction(i);
		}
		return result;
	}

	public boolean isValid(int i) {
		return tableModel.isValid(i);
	}
	
	Date getDate() {
		return getDateField().getDate();
	}

	public Transaction[] getValidTransactions() {
		ArrayList<Transaction> result = new ArrayList<Transaction>(tableModel.getRowCount());
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			if (isValid(i)) result.add((Transaction) tableModel.getTransaction(i));
		}
		return (Transaction[]) result.toArray(new Transaction[result.size()]);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
