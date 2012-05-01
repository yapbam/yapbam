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
	private StatementViewPanel tPanel;
	private TransactionSelector selector;
	
	public CheckTransactionAction (StatementViewPanel statementViewPanel, TransactionSelector selector, boolean check) {
		super(check?LocalizationData.get("MainMenu.Transactions.Check"):LocalizationData.get("MainMenu.Transactions.Uncheck"), //$NON-NLS-1$
				check?IconManager.CHECK_TRANSACTION:IconManager.UNCHECK_TRANSACTION);
		putValue(SHORT_DESCRIPTION, check?LocalizationData.get("MainMenu.Transactions.Check.ToolTip"):LocalizationData.get("MainMenu.Transactions.Uncheck.ToolTip")); //$NON-NLS-1$
		if (check) putValue(MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Check.Mnemonic")); //$NON-NLS-1$
		this.tPanel = statementViewPanel;
		this.selector = selector;
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateEnabled();
			}
		};
		// The test is mandatory to prevent Window builder from crashing when editing StatementViewPanel
		if (statementViewPanel!=null) statementViewPanel.addPropertyChangeListener(StatementViewPanel.CHECK_MODE_READY_PROPERTY, listener);
		this.selector.addPropertyChangeListener(TransactionSelector.SELECTED_PROPERTY, listener);
		updateEnabled();
	}

	public void updateEnabled() {
		boolean isEnabled = this.selector.getSelectedTransaction()!=null;
		isEnabled = isEnabled && tPanel.isCheckModeReady();
		setEnabled(isEnabled);
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
			statementId = tPanel.getEditedStatement();
		}
		Transaction tChecked = new Transaction(t.getDate(), t.getNumber(), t.getDescription(), t.getComment(), t.getAmount(), t.getAccount(), t.getMode(), t.getCategory(),
				date, statementId, list);
		selector.getFilteredData().getGlobalData().add(tChecked);
		selector.getFilteredData().getGlobalData().remove(t);
	}
}
