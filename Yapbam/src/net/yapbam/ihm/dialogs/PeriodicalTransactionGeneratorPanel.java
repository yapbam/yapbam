package net.yapbam.ihm.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.widget.DateWidget;
import java.awt.Insets;
import javax.swing.JButton;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class PeriodicalTransactionGeneratorPanel extends JPanel { //LOCAL

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private DateWidget date = null;
	private JButton simulate = null;
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
		jLabel.setText("Générer les opération jusqu'au :");
		this.setSize(359, 199);
		this.setLayout(new BorderLayout());
		this.add(getJPanel(), BorderLayout.NORTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);
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
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.gridy = 0;
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
			jPanel.add(getDate(), gridBagConstraints);
			jPanel.add(getSimulate(), gridBagConstraints2);
			jPanel.add(summary, gridBagConstraints3);
		}
		return jPanel;
	}

	/**
	 * This method initializes date	
	 * 	
	 * @return net.yapbam.ihm.widget.DateWidget	
	 */
	private DateWidget getDate() {
		if (date == null) {
			date = new DateWidget();
			date.setColumns(6);
			date.setToolTipText("Entrez ici la date jusqu'à laquelle générer les opérations");
			date.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					getSimulate().setEnabled(date.getDate()!=null);
					summary.setText(" ");
					//TODO Clear JTable content
				}
			});
		}
		return date;
	}

	/**
	 * This method initializes simulate	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSimulate() {
		if (simulate == null) {
			simulate = new JButton();
			simulate.setText("Simuler");
			simulate.setToolTipText("Cliquer sur ce bouton pour simuler la génération des opérations");
			simulate.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Transaction[] transactions = data.generateTransactionsFromPeriodicals(date.getDate());
					double debts = 0;
					double receipts = 0;
					for (int i=0; i<transactions.length; i++) {
						if (transactions[i].getAmount()>0) {
							receipts += transactions[i].getAmount();
						} else {
							debts += transactions[i].getAmount();
						}
					}
					String message = MessageFormat.format("{0} opérations. Recettes : {1}, dépenses {2}, total : {3}", transactions.length,
							LocalizationData.getCurrencyInstance().format(receipts), LocalizationData.getCurrencyInstance().format(-debts),
							LocalizationData.getCurrencyInstance().format(receipts+debts));
					summary.setText(message);
					tableModel.transactions = transactions;
					tableModel.fireTableDataChanged();
				}
			});
		}
		return simulate;
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

	class GenerateTableModel extends AbstractTableModel {
		Transaction[] transactions;
		
		GenerateTableModel() {
			this.transactions = new Transaction[0];
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public int getRowCount() {
			return transactions.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return transactions[rowIndex].getDescription();
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
		}
		return jTable;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
