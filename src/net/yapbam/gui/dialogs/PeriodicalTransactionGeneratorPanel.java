package net.yapbam.gui.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.date.helpers.DateHelper;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.transactiontable.AmountRenderer;
import net.yapbam.gui.transactiontable.BooleanRenderer;
import net.yapbam.gui.transactiontable.DateRenderer;
import net.yapbam.gui.transactiontable.ObjectRenderer;
import net.yapbam.gui.util.JTableListener;
import net.yapbam.gui.widget.DateWidget;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.text.MessageFormat;
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
	private Date lastDate;
	private JLabel summary = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private GenerateTableModel tableModel;

	private GlobalData data;

	/**
	 * This is the default constructor
	 */
	public PeriodicalTransactionGeneratorPanel(GlobalData data) {
		super();
		this.data = data;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		jLabel = new JLabel();
		jLabel.setText(LocalizationData.get("GeneratePeriodicalTransactionsDialog.lastDate")); //$NON-NLS-1$
		this.setSize(360, 200);
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
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.gridwidth=GridBagConstraints.REMAINDER;
			summary = new JLabel();
			summary.setText(" "); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.gridx = 1;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(jLabel, gridBagConstraints1);
			jPanel.add(getDateField(), gridBagConstraints);
			jPanel.add(summary, gridBagConstraints3);
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
			dateField = new DateWidget(new Date());
			dateField.setColumns(6);
			dateField.setToolTipText(LocalizationData.get("GeneratePeriodicalTransactionsDialog.lastDate.toolTip")); //$NON-NLS-1$
			dateField.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
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
			tableModel = new GenerateTableModel();
			jTable = new JTable(tableModel);
			jTable.setDefaultRenderer(Date.class, new DateRenderer());
			jTable.setDefaultRenderer(double[].class, new AmountRenderer());
			jTable.setDefaultRenderer(Boolean.class, new BooleanRenderer());
			jTable.setDefaultRenderer(Object.class, new ObjectRenderer());
			jTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			YapbamState.restoreState(jTable, STATE_PROPERTIES_PREFIX);
			new JTableListener(jTable, null, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = jTable.getSelectedRow();
					Transaction transaction = tableModel.getTransactions()[row];
					transaction = TransactionDialog.open(data, AbstractDialog.getOwnerWindow(jTable), transaction, true, false);
					if (transaction==null) {
						tableModel.setTransaction(row, transaction);
					}
				}
			});
		}
		return jTable;
	}
	
	void saveState() {
		YapbamState.saveState(getJTable(), STATE_PROPERTIES_PREFIX);
	}

	private void updateTransactions() {
		Date endDate = dateField.getDate();
		boolean change = DateHelper.dateToInteger(endDate)!=DateHelper.dateToInteger(lastDate);
		Transaction[] transactions;
		if (change) {
			if (endDate==null) {
				transactions = new Transaction[0];
			} else {
				transactions = data.generateTransactionsFromPeriodicals(endDate);
			}
			String message;
			if (endDate==null) {
				message = " "; //$NON-NLS-1$
			} else {
				double debts = 0;
				double receipts = 0;
				for (int i=0; i<transactions.length; i++) {
					if (transactions[i].getAmount()>0) {
						receipts += transactions[i].getAmount();
					} else {
						debts += transactions[i].getAmount();
					}
				}
				message = MessageFormat.format(LocalizationData.get("GeneratePeriodicalTransactionsDialog.summary"), transactions.length, //$NON-NLS-1$
						LocalizationData.getCurrencyInstance().format(receipts), LocalizationData.getCurrencyInstance().format(-debts),
						LocalizationData.getCurrencyInstance().format(receipts+debts));
			}
			summary.setText(message);
			if (transactions.length!=tableModel.getTransactions().length) {
				tableModel.setTransactions(transactions);
			}
			Date old = lastDate;
			lastDate=endDate;
			this.firePropertyChange("endDate", old, lastDate); //$NON-NLS-1$
		}
	}

	Transaction[] getTransactions() {
		return tableModel.getTransactions().clone();
	}

	public boolean isValid(int i) {
		return tableModel.isValid(i);
	}
	
	Date getDate() {
		return getDateField().getDate();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
