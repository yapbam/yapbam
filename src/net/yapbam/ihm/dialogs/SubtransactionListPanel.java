package net.yapbam.ihm.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

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

import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.GlobalData;
import net.yapbam.data.SubTransaction;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.transactiontable.AmountRenderer;
import net.yapbam.ihm.transactiontable.ObjectRenderer;
import net.yapbam.ihm.transactiontable.SubTransactionsTableModel;

class SubtransactionListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String SUM_PROPERTY = "sum"; //$NON-NLS-1$
	
	private SubTransactionsTableModel tableModel;
	private JButton delete;
	private JButton edit;
	private JTable table;
	private JCheckBox addToTransaction;
	private double sum;
	private JLabel sumLabel;

	public SubtransactionListPanel(final GlobalData data) {
		super(new BorderLayout());
		this.sum = 0;
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), LocalizationData.get("TransactionDialog.SubPanel.title"))); //$NON-NLS-1$
		
		JPanel pane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(); c.weightx=1; c.anchor=GridBagConstraints.WEST;
		addToTransaction = new JCheckBox(LocalizationData.get("TransactionDialog.SubPanel.add")); //$NON-NLS-1$
		addToTransaction.setSelected(true);
		addToTransaction.setToolTipText(LocalizationData.get("TransactionDialog.SubPanel.add.tooltip")); //$NON-NLS-1$
		pane.add(addToTransaction, c);
		sumLabel = new JLabel(""); //$NON-NLS-1$
		c.anchor=GridBagConstraints.EAST; c.gridx = 1;
		pane.add(sumLabel, c);
		this.add(pane, BorderLayout.NORTH);
		
		tableModel = new SubTransactionsTableModel();
		table = new JTable(tableModel);
		ListSelectionModel selModel = table.getSelectionModel();
		selModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel m = (javax.swing.ListSelectionModel) e.getSource();
				if (!e.getValueIsAdjusting()) {
					boolean ok = m.getMinSelectionIndex()>=0;
					delete.setEnabled(ok);
					edit.setEnabled(ok);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                  Point p = e.getPoint();
                  int row = table.rowAtPoint(p);
                  if (row >= 0) {
               		  editSelected(data);
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
        table.setPreferredScrollableViewportSize(new Dimension(1,table.getRowHeight()*6));
        JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
		JPanel buttonsPanel = new JPanel();
		JButton newSubTransactionButton = new JButton(LocalizationData.get("TransactionDialog.SubPanel.new")); //$NON-NLS-1$
		newSubTransactionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				create(data);
			}
		});
		delete = new JButton(LocalizationData.get("TransactionDialog.SubPanel.delete")); //$NON-NLS-1$
		delete.setEnabled(false);
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSelected();
			}
		});
		edit = new JButton(LocalizationData.get("TransactionDialog.SubPanel.modify")); //$NON-NLS-1$
		edit.setEnabled(false);
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editSelected(data);
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
					sumLabel.setText(MessageFormat.format(LocalizationData.get("TransactionDialog.SubPanel.total"), evt.getNewValue())); //$NON-NLS-1$
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
	
	private Window getWindow() {
		return AbstractDialog.getOwnerWindow(SubtransactionListPanel.this);
	}

	private void editSelected(final GlobalData data) {
		int index = table.getSelectedRow();
		SubTransaction old = tableModel.get(index);
		SubTransactionDialog dialog = new SubTransactionDialog(getWindow(), data, old);
		dialog.setVisible(true);
		SubTransaction sub = dialog.getSubTransaction();
		if (sub!=null) {
			tableModel.replace(index, sub);
			double oldSum = this.sum;
			this.sum = this.sum - old.getAmount() + sub.getAmount();
			this.firePropertyChange(SUM_PROPERTY, oldSum, this.sum);
		}
	}
	
	private void deleteSelected() {
		int index = table.getSelectedRow();
		SubTransaction old = tableModel.get(index);
		tableModel.remove(index);
		double oldSum = this.sum;
		this.sum = this.sum - old.getAmount();
		this.firePropertyChange(SUM_PROPERTY, oldSum, this.sum);
	}

	private void create(final GlobalData data) {
		SubTransactionDialog dialog = new SubTransactionDialog(getWindow(), data, null);
		dialog.setVisible(true);
		SubTransaction sub = dialog.getSubTransaction();
		if (sub!=null) {
			tableModel.add(sub);
			double oldSum = this.sum;
			this.sum = this.sum + sub.getAmount();
			this.firePropertyChange(SUM_PROPERTY, oldSum, this.sum);
		}
	}
}