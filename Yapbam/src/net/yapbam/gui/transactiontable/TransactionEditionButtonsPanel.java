package net.yapbam.gui.transactiontable;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AccountSelector;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.actions.NewTransactionAction;

@SuppressWarnings("serial")
public class TransactionEditionButtonsPanel extends JPanel {
	private FilteredData data;
	private TransactionTable table;
	private AccountSelector accountSelector;
	
	private JButton newButton;
	private JButton massNewButton;
	private JButton editButton;
	private JButton duplicateButton;
	private JButton deleteButton;

	/**
	 * Create the panel.
	 */
	public TransactionEditionButtonsPanel(TransactionTable table, FilteredData data, AccountSelector selector) {
		super();
		this.data = data;
		this.table = table;
		this.accountSelector = selector;
		initialize();
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		add(getNewButton(), c);
		c.gridx = 1;
		add(getMassNewButton(), c);
		c.gridx = 2;
		add(getEditButton(), c);
		c.gridx = 3;
		add(getDuplicateButton(), c);
		c.gridx = 4;
		c.anchor = GridBagConstraints.WEST;
		add(getDeleteButton(), c);
		GeneratePeriodicalTransactionsAction action = new GeneratePeriodicalTransactionsAction(data==null?null:data.getGlobalData(), false);
		final JButton periodicalTransactionsButton = new JButton(action);
		c.gridx = 5;
		c.weightx = 1;
		c.anchor = GridBagConstraints.EAST;
		add(periodicalTransactionsButton, c);
	}

	protected JButton getNewButton() {
		if (newButton==null) {
			newButton = new JButton(new NewTransactionAction(data, getTable(),false,accountSelector));
			newButton.setText(LocalizationData.get("GenericButton.new")); //$NON-NLS-1$
		}
		return newButton;
	}

	protected JButton getMassNewButton() {
		if (massNewButton==null) {
			massNewButton = new JButton(new NewTransactionAction(data, getTable(),true, accountSelector));
			massNewButton.setText(LocalizationData.get("MainMenu.Transactions.NewMultiple")); //$NON-NLS-1$
		}
		return massNewButton;
	}

	protected JButton getEditButton() {
		if (editButton==null) {
			editButton = new JButton(new EditTransactionAction(getTable()));
			editButton.setText(LocalizationData.get("GenericButton.edit")); //$NON-NLS-1$
		}
		return editButton;
	}

	protected JButton getDuplicateButton() {
		if (duplicateButton==null) {
			duplicateButton = new JButton(new DuplicateTransactionAction(getTable()));
			duplicateButton.setText(LocalizationData.get("GenericButton.duplicate")); //$NON-NLS-1$
		}
		return duplicateButton;
	}

	protected JButton getDeleteButton() {
		if (deleteButton==null) {
			deleteButton = new JButton(new DeleteTransactionAction(getTable()));
			deleteButton.setText(LocalizationData.get("GenericButton.delete")); //$NON-NLS-1$			
		}
		return deleteButton;
	}

	private TransactionTable getTable() {
		return table;
	}
}
