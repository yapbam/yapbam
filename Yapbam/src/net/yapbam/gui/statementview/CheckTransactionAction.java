package net.yapbam.gui.statementview;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;

import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.AbstractTransactionAction;
import net.yapbam.gui.actions.TransactionSelector;

public class CheckTransactionAction extends AbstractTransactionAction {
	private static final long serialVersionUID = 1L;
	private StatementViewPanel tPanel;
	
	public CheckTransactionAction (StatementViewPanel statementViewPanel, TransactionSelector selector, boolean check) {
		super(selector, check?LocalizationData.get("MainMenu.Transactions.Check"):LocalizationData.get("MainMenu.Transactions.Uncheck"), //$NON-NLS-1$ //$NON-NLS-2$);
				check?IconManager.CHECK_TRANSACTION:IconManager.UNCHECK_TRANSACTION,
				check?LocalizationData.get("MainMenu.Transactions.Check.ToolTip"):LocalizationData.get("MainMenu.Transactions.Uncheck.ToolTip")); //$NON-NLS-1$ //$NON-NLS-2$
		if (check) putValue(MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Check.Mnemonic")); //$NON-NLS-1$
		this.tPanel = statementViewPanel;
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateEnabled();
			}
		};
		// The test is mandatory to prevent Window builder from crashing when editing StatementViewPanel
		if (statementViewPanel!=null) statementViewPanel.addPropertyChangeListener(StatementViewPanel.CHECK_MODE_READY_PROPERTY, listener);
		updateEnabled();
	}

	@Override
	public void updateEnabled() {
		boolean isEnabled = this.selector.getSelectedTransactions().length>0;
		if (tPanel!=null) isEnabled = isEnabled && tPanel.isCheckModeReady();
		setEnabled(isEnabled);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction[] ts = selector.getSelectedTransactions();
		Transaction[] tChecked = new Transaction[ts.length];
		for (int i = 0; i < ts.length; i++) {
			Transaction t = ts[i];
			ArrayList<SubTransaction> list = new ArrayList<SubTransaction>(t.getSubTransactionSize());
			for (int j = 0; j < t.getSubTransactionSize(); j++) {
				list.add(t.getSubTransaction(j));
			}
			String statementId = null;
			Date date = t.getValueDate();
			if (t.getStatement()==null) {
				statementId = tPanel.getEditedStatement();
			}
			tChecked [i] = new Transaction(t.getDate(), t.getNumber(), t.getDescription(), t.getComment(), t.getAmount(), t.getAccount(), t.getMode(), t.getCategory(),
					date, statementId, list);
		}
		selector.getFilteredData().getGlobalData().add(tChecked);
		selector.getFilteredData().getGlobalData().remove(ts);
	}
}
