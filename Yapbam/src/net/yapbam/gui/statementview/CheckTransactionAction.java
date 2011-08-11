package net.yapbam.gui.statementview;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractAction;

import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.TransactionSelector;

public class CheckTransactionAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private CheckModePanel tPanel;
	private TransactionSelector selector;
	
	public CheckTransactionAction (CheckModePanel checkModePanel, TransactionSelector selector) {
		super(LocalizationData.get("MainMenu.Transactions.Check"), IconManager.CHECK_TRANSACTION); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Check.ToolTip")); //$NON-NLS-1$
		putValue(MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Check.Mnemonic")); //$NON-NLS-1$
		this.tPanel = checkModePanel;
		this.selector = selector;
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateEnabled();
			}
		};
		this.tPanel.addPropertyChangeListener(CheckModePanel.IS_OK_PROPERTY, listener);
		this.selector.addPropertyChangeListener(TransactionSelector.SELECTED_PROPERTY, listener);
		updateEnabled();
	}

	public void updateEnabled() {
		setEnabled(tPanel.isOk() && (this.selector.getSelectedTransaction()!=null));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction t = selector.getSelectedTransaction();
		ArrayList<SubTransaction> list = new ArrayList<SubTransaction>(t.getSubTransactionSize());
		for (int i = 0; i < t.getSubTransactionSize(); i++) {
			list.add(t.getSubTransaction(i));
		}
		String statementId = null;
		Date date = t.getValueDate();
		if (t.getStatement()==null) {
			Date ckDate = tPanel.getValueDate();
			if (ckDate!=null) date = ckDate;
			statementId = tPanel.getStatement();
		}
		Transaction tChecked = new Transaction(t.getDate(), t.getNumber(), t.getDescription(), t.getComment(), t.getAmount(), t.getAccount(), t.getMode(), t.getCategory(),
				date, statementId, list);
		selector.getFilteredData().getGlobalData().add(tChecked);
		selector.getFilteredData().getGlobalData().remove(t);
	}
}
