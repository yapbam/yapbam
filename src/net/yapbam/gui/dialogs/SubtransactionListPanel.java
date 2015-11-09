package net.yapbam.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.table.RowSorter;

import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.GlobalData;
import net.yapbam.data.SubTransaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.transactiontable.AmountRenderer;
import net.yapbam.gui.transactiontable.ObjectRenderer;

public class SubtransactionListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String SUM_PROPERTY = "sum"; //$NON-NLS-1$
	
	private SubTransactionsTableModel tableModel;
	private JButton delete;
	private JButton edit;
	private JTable table;
	private JCheckBox addToTransaction;
	private double sum;
	private JLabel sumLabel;
	private GlobalData data;

	public SubtransactionListPanel(GlobalData data) {
		super(new BorderLayout());
		this.data = data;
		this.sum = 0;
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), LocalizationData.get("TransactionDialog.SubPanel.title"))); //$NON-NLS-1$
		
		JPanel pane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx=1;
		c.anchor=GridBagConstraints.WEST;
		addToTransaction = new JCheckBox(LocalizationData.get("TransactionDialog.SubPanel.add")); //$NON-NLS-1$
		addToTransaction.setSelected(true);
		addToTransaction.setToolTipText(LocalizationData.get("TransactionDialog.SubPanel.add.tooltip")); //$NON-NLS-1$
		pane.add(addToTransaction, c);
		
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 1;
		c2.weightx=1;
		c2.anchor=GridBagConstraints.EAST;
		sumLabel = new JLabel();
		pane.add(sumLabel, c2);
		this.add(pane, BorderLayout.NORTH);
		
		tableModel = new SubTransactionsTableModel();
		table = new SubTransactionsTable(tableModel);
		table.setRowSorter(new RowSorter<TableModel>(tableModel));
		ListSelectionModel selModel = table.getSelectionModel();
		selModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
//System.out.println(e);
				ListSelectionModel m = (javax.swing.ListSelectionModel) e.getSource();
				if (!e.getValueIsAdjusting()) {
					boolean ok = m.getMinSelectionIndex()>=0;
					delete.setEnabled(ok);
					edit.setEnabled(ok);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
//System.out.println(e);
				if (e.getClickCount() == 2) {
					Point p = e.getPoint();
					int row = table.rowAtPoint(p);
					if (row >= 0) {
						editSelected(SubtransactionListPanel.this.data);
					}
				}
			}
		});
		//table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "none");//This should prevent the enter key to change the selected row ... but it doesn't
		//The tutorial is there : http://java.sun.com/docs/books/tutorial/uiswing/misc/keybinding.html
/*		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int code = e.getKeyCode();
				if (code==KeyEvent.VK_ENTER) {
					e.consume();
					//editSelected(data); //TODO Strange, the enterKey go to next line
				} else if ((code==KeyEvent.VK_DELETE) || (code==KeyEvent.VK_BACK_SPACE)) {
					int ok = JOptionPane.showOptionDialog(SubtransactionListPanel.this,
							"Voulez-vous réellement supprimer la sous-opération sélectionnée ?",
							"Confirmez la suppression",
						    JOptionPane.YES_NO_OPTION,
						    JOptionPane.WARNING_MESSAGE,
						    null,     //do not use a custom Icon
						    new String[]{"Oui","Non"},  //the titles of buttons
						    "Non");
					if (ok==0) deleteSelected();
				}
			}
		});*/
		table.setDefaultRenderer(double[].class, new AmountRenderer());
		table.setDefaultRenderer(Object.class, new ObjectRenderer());
		table.setPreferredScrollableViewportSize(new Dimension(1, table.getRowHeight() * 6));
		JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
		JPanel buttonsPanel = new JPanel();
		JButton newSubTransactionButton = new JButton(LocalizationData.get("TransactionDialog.SubPanel.new")); //$NON-NLS-1$
		newSubTransactionButton.setToolTipText(LocalizationData.get("TransactionDialog.SubPanel.new.tooltip")); //$NON-NLS-1$
		newSubTransactionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				create(SubtransactionListPanel.this.data);
			}
		});
		delete = new JButton(LocalizationData.get("TransactionDialog.SubPanel.delete")); //$NON-NLS-1$
		delete.setToolTipText(LocalizationData.get("TransactionDialog.SubPanel.delete.tooltip")); //$NON-NLS-1$
		delete.setEnabled(false);
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSelected();
			}
		});
		edit = new JButton(LocalizationData.get("TransactionDialog.SubPanel.modify")); //$NON-NLS-1$
		edit.setToolTipText(LocalizationData.get("TransactionDialog.SubPanel.modify.tooltip")); //$NON-NLS-1$
		edit.setEnabled(false);
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editSelected(SubtransactionListPanel.this.data);
			}
		});
		buttonsPanel.add(newSubTransactionButton);
		buttonsPanel.add(edit);
		buttonsPanel.add(delete);
		add(buttonsPanel, BorderLayout.SOUTH);
		this.addPropertyChangeListener(SUM_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (getSubtransactionsCount()==0) {
					sumLabel.setText(""); //$NON-NLS-1$
				} else {
					sumLabel.setText(Formatter.format(LocalizationData.get("TransactionDialog.SubPanel.total"), evt.getNewValue())); //$NON-NLS-1$
				}
			}
		});
	}
	
	public void fill(AbstractTransaction transaction) {
		double oldValue = this.sum;
		tableModel.fill(transaction);
		this.sum = 0;
		for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
			this.sum += transaction.getSubTransaction(i).getAmount();
		}
		firePropertyChange(SUM_PROPERTY, oldValue, this.sum);
	}

	public int getSubtransactionsCount() {
		return tableModel.getRowCount();
	}

	public SubTransaction getSubtransaction(int i) {
		return tableModel.get(i);
	}
	
	public double getSum() {
		return sum;
	}

	public void setAddToTransaction(boolean selected) {
		this.addToTransaction.setSelected(selected);
	}
	
	public boolean isAddToTransactionSelected() {
		return this.addToTransaction.isSelected();
	}
	
	private void editSelected(final GlobalData data) {
		int index = table.getSelectedRow();
		if (index>=0) {
			// One can think this method is only called when a row is selected, it's wrong
			// It is called when the user double-click on a row. If the ctrl key is down while clicking,
			// the first click selects the row, the second erases the selection it !
			SubTransaction old = tableModel.get(index);
			SubTransactionDialog dialog = new SubTransactionDialog(Utils.getOwnerWindow(this), data, old);
			setPredefinedDescriptions(dialog);
			if (this.updater!=null) {
				dialog.setPredefinedUpdater(updater);
			}
			dialog.setVisible(true);
			SubTransaction sub = dialog.getSubTransaction();
			if (sub!=null) {
				tableModel.replace(index, sub);
				double oldSum = this.sum;
				this.sum = this.sum - old.getAmount() + sub.getAmount();
				this.firePropertyChange(SUM_PROPERTY, oldSum, this.sum);
			}
		}
	}
	
	private void deleteSelected() {
		int index = table.getSelectedRow();
		if (index>=0) {
			SubTransaction old = tableModel.get(index);
			tableModel.remove(index);
			double oldSum = this.sum;
			this.sum = this.sum - old.getAmount();
			this.firePropertyChange(SUM_PROPERTY, oldSum, this.sum);
		}
	}

	private void create(final GlobalData data) {
		SubTransactionDialog dialog = new SubTransactionDialog(Utils.getOwnerWindow(this), data, null);
		setPredefinedDescriptions(dialog);
		if (this.updater!=null) {
			dialog.setPredefinedUpdater(updater);
		}
		dialog.setVisible(true);
		SubTransaction sub = dialog.getSubTransaction();
		if (sub!=null) {
			tableModel.add(sub);
			double oldSum = this.sum;
			this.sum = this.sum + sub.getAmount();
			this.firePropertyChange(SUM_PROPERTY, oldSum, this.sum);
		}
	}
	
	private PredefinedDescriptionComputer predefinedDescriptionComputer;
	private void setPredefinedDescriptions(SubTransactionDialog dialog) {
		if (predefinedDescriptionComputer!=null) {
			dialog.setPredefined(predefinedDescriptionComputer.getPredefined(), predefinedDescriptionComputer.getUnsortedSize());
		}
	}
	public void setPredefinedDescriptionComputer(PredefinedDescriptionComputer predefinedDescriptionComputer) {
		this.predefinedDescriptionComputer = predefinedDescriptionComputer;
	}
	private SubTransactionPanel.PredefinedDescriptionUpdater updater;	
	public void setPredefinedDescriptionUpdater (SubTransactionPanel.PredefinedDescriptionUpdater updater) {
		this.updater = updater;
	}

	void saveState(String prefix) {
		YapbamState.INSTANCE.saveState(table, prefix);
	}
	
	void restoreState(String prefix) {
		YapbamState.INSTANCE.restoreState(table, prefix);
	}
}