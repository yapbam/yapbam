package net.yapbam.gui.dialogs.periodicaltransaction;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.dialogs.TransactionDialog;
import net.yapbam.gui.transactiontable.AmountRenderer;
import net.yapbam.gui.transactiontable.BooleanRenderer;
import net.yapbam.gui.transactiontable.DateRenderer;
import net.yapbam.gui.transactiontable.ObjectRenderer;
import net.yapbam.util.DateUtils;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.table.JTableListener;
import com.fathzer.soft.ajlib.swing.table.RowSorter;
import com.fathzer.soft.ajlib.swing.widget.date.DateWidget;

public class PeriodicalTransactionGeneratorPanel extends JPanel {
	static final String HAS_IMPACT_PROPERTY = "hasImpact"; //$NON-NLS-1$
	
	@SuppressWarnings("serial")
	private final class EditTransactionAction extends AbstractAction {
		public EditTransactionAction() {
			super(LocalizationData.get("MainMenu.Transactions.Edit"), IconManager.get(Name.EDIT_TRANSACTION)); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Edit.ToolTip")); //$NON-NLS-1$
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (jTable.getSelectedRowCount()==0) {
				return; 
			}
			int row = jTable.convertRowIndexToModel(jTable.getSelectedRow());
			Transaction transaction = (Transaction) tableModel.getTransaction(row);
			transaction = TransactionDialog.open(data, Utils.getOwnerWindow(jTable), transaction, true, false, false);
			if (transaction!=null) {
				tableModel.setTransaction(row, transaction);
			}
		}
	}

	private static final String STATE_PROPERTIES_PREFIX = "net.yapbam.ihm.dialogs.PeriodicalGeneratorPanel.table."; //$NON-NLS-1$
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private DateWidget dateField = null;
	private JLabel summary = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private GenerateTableModel tableModel;

	private GlobalData data;
	private boolean hasImpact;

	private EditTransactionAction editAction;
	private JPanel panel;
	private JButton editButton;
	private JPanel panel1;

	/**
	 * This is the default constructor
	 */
	public PeriodicalTransactionGeneratorPanel(GlobalData data) {
		super();
		this.data = data;
		this.hasImpact = false;
		initialize();
		getDateField().setDate(DateUtils.getMidnight(new Date()));
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getJPanel(), BorderLayout.NORTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);
		add(getPanel(), BorderLayout.SOUTH);
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
			jPanel.setLayout(new BorderLayout(0, 0));
			jPanel.add(getPanel1(), BorderLayout.NORTH);
			summary = new JLabel();
			jPanel.add(summary);
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
			dateField.setDate(null);
			dateField.setToolTipText(LocalizationData.get("GeneratePeriodicalTransactionsDialog.lastDate.toolTip")); //$NON-NLS-1$
			dateField.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					tableModel.setDate(dateField.getDate());
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
	private JTable getJTable() {
		if (jTable == null) {
			tableModel = new GenerateTableModel(data);
			jTable = new GeneratedTransactionsTable(tableModel);
			jTable.setDefaultRenderer(Date.class, new DateRenderer());
			jTable.setDefaultRenderer(double[].class, new AmountRenderer());
			jTable.setDefaultRenderer(Boolean.class, new BooleanRenderer());
			jTable.setDefaultRenderer(Object.class, new ObjectRenderer());
			jTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.setRowSorter(new RowSorter<GenerateTableModel>(tableModel));
			restoreState();
			
			getEditAction().setEnabled(false);
			jTable.addMouseListener(new JTableListener(new Action[]{getEditAction()}, getEditAction()));
			jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						editAction.setEnabled(jTable.getSelectedRowCount()>0);
					}
				}
			});
			tableModel.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					updateTransactions();
				}
			});
		}
		return jTable;
	}
	
	private Action getEditAction() {
		if (editAction==null) {
			editAction = new EditTransactionAction();
		}
		return editAction;
	}
	
	void saveState() {
		YapbamState.INSTANCE.saveState(getJTable(), STATE_PROPERTIES_PREFIX);
	}
	
	void restoreState() {
		YapbamState.INSTANCE.restoreState(getJTable(), STATE_PROPERTIES_PREFIX);
	}
	
	private void updateTransactions() {
		String message;
		if (getDateField().getDate()==null) {
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
			message = Formatter.format(LocalizationData.get("GeneratePeriodicalTransactionsDialog.summary"), this.tableModel.getRowCount(), //$NON-NLS-1$
					LocalizationData.getCurrencyInstance().format(receipts), LocalizationData.getCurrencyInstance().format(-debts),
					LocalizationData.getCurrencyInstance().format(receipts+debts));
		}
		summary.setText(message);
		updateHasImpact();
	}
	
	private void updateHasImpact() {
		if (tableModel.hasImpact()!=hasImpact) {
			hasImpact = !hasImpact;
			firePropertyChange(HAS_IMPACT_PROPERTY, !hasImpact, hasImpact);
		}
	}
	
	/** Tests whether the actual user selection has an impact on the data.
	 * <br>An impact means there is some periodical transactions's "next date" that will be changed.  
	 * @return true if there is an impact
	 */
	boolean hasImpact() {
		return hasImpact;
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
			if (isValid(i)) {
				result.add((Transaction) tableModel.getTransaction(i));
			}
		}
		return (Transaction[]) result.toArray(new Transaction[result.size()]);
	}
	
	/** Gets the postponed date of a periodical transaction.
	 * @param indexPeriodical The index of the periodical transaction in the global data.
	 * @return The postponed date, or null if the transaction is not postponed
	 */
	public Date getPostponedDate(int indexPeriodical) {
		return tableModel.getPostponedDate(indexPeriodical);
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
			panel.add(getEditButton(), BorderLayout.WEST);
		}
		return panel;
	}
	private JButton getEditButton() {
		if (editButton == null) {
			editButton = new JButton(getEditAction());
		}
		return editButton;
	}
	private JPanel getPanel1() {
		if (panel1 == null) {
			panel1 = new JPanel();
			GridBagLayout gblPanel1 = new GridBagLayout();
			gblPanel1.columnWidths = new int[]{0, 0, 0};
			gblPanel1.rowHeights = new int[]{0, 0};
			gblPanel1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gblPanel1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel1.setLayout(gblPanel1);
			jLabel = new JLabel();
			GridBagConstraints gbcJLabel = new GridBagConstraints();
			gbcJLabel.anchor = GridBagConstraints.WEST;
			gbcJLabel.insets = new Insets(0, 0, 0, 5);
			gbcJLabel.gridx = 0;
			gbcJLabel.gridy = 0;
			panel1.add(jLabel, gbcJLabel);
			jLabel.setText(LocalizationData.get("GeneratePeriodicalTransactionsDialog.lastDate")); //$NON-NLS-1$
			GridBagConstraints gbcDateField = new GridBagConstraints();
			gbcDateField.anchor = GridBagConstraints.WEST;
			gbcDateField.gridx = 1;
			gbcDateField.gridy = 0;
			panel1.add(getDateField(), gbcDateField);
		}
		return panel1;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
