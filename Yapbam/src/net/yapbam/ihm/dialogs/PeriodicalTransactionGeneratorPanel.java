package net.yapbam.ihm.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.date.helpers.DateHelper;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.transactiontable.AmountRenderer;
import net.yapbam.ihm.transactiontable.DateRenderer;
import net.yapbam.ihm.transactiontable.GenericTransactionTableModel;
import net.yapbam.ihm.transactiontable.ObjectRenderer;
import net.yapbam.ihm.transactiontable.SpreadState;
import net.yapbam.ihm.transactiontable.SpreadStateRenderer;
import net.yapbam.ihm.widget.DateWidget;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.text.MessageFormat;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

public class PeriodicalTransactionGeneratorPanel extends JPanel { //LOCAL

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
		jLabel.setText("Générer les opérations jusqu'au :");
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
			summary.setText(" ");
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
			dateField = new DateWidget();
			dateField.setColumns(6);
			dateField.setToolTipText("Entrez ici la date jusqu'à laquelle générer les opérations");
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

	@SuppressWarnings("serial")
	class GenerateTableModel extends AbstractTableModel implements GenericTransactionTableModel {
		Transaction[] transactions;
		
		GenerateTableModel() {
			this.transactions = new Transaction[0];
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==0) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
			if (columnIndex==1) return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
			if (columnIndex==2) return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
			if (columnIndex==3) return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
			throw new IllegalArgumentException();
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex==2) return Date.class;
			if (columnIndex==3) return double[].class;
			return String.class;
		}

		@Override
		public int getAlignment(int column) {
			if (column==3) return SwingConstants.RIGHT;
	    	if ((column==0) || (column==1)) return SwingConstants.LEFT;
	    	else return SwingConstants.CENTER;
		}

		@Override
		public int getRowCount() {
			return transactions.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Transaction t = transactions[rowIndex];
			if (columnIndex==0) return t.getAccount().getName();
			if (columnIndex==1) return t.getDescription();
			if (columnIndex==2) return t.getDate();
			if (columnIndex==3) return new double[]{t.getAmount()};
			throw new IllegalArgumentException();
		}

		@Override
		public boolean isChecked(int row) {
			return false;
		}

		@Override
		public boolean isExpense(int row) {
			return transactions[row].getAmount()<0;
		}
	}
	
	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			tableModel = new GenerateTableModel();
			jTable = new JTable(tableModel);
			jTable.setDefaultRenderer(Date.class, new DateRenderer());
			jTable.setDefaultRenderer(double[].class, new AmountRenderer());
			jTable.setDefaultRenderer(Object.class, new ObjectRenderer());
			jTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jTable;
	}

	private void updateTransactions() {
		Date endDate = dateField.getDate();
		boolean change = DateHelper.dateToInteger(endDate)!=DateHelper.dateToInteger(lastDate);
		Transaction[] transactions;
		if (change) {
			System.out.println (lastDate+" -> "+endDate);//TODO
			if (endDate==null) {
				transactions = new Transaction[0];
			} else {
				transactions = data.generateTransactionsFromPeriodicals(endDate);
			}
			String message;
			if (endDate==null) {
				message = " ";
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
				message = MessageFormat.format("{0} opérations. Recettes : {1}, dépenses {2}, total : {3}", transactions.length,
						LocalizationData.getCurrencyInstance().format(receipts), LocalizationData.getCurrencyInstance().format(-debts),
						LocalizationData.getCurrencyInstance().format(receipts+debts));
			}
			summary.setText(message);
			int oldSize = tableModel.transactions.length;
			if (transactions.length!=oldSize) {
				tableModel.transactions = transactions;
				tableModel.fireTableDataChanged();
			}
			Date old = lastDate;
			lastDate=endDate;
			this.firePropertyChange("endDate", old, lastDate);
		}
	}

	Transaction[] getTransactions() {
		return tableModel.transactions.clone();
	}
	
	Date getDate() {
		return getDateField().getDate();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
