package net.yapbam.gui.archive;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.swing.JPanel;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

@SuppressWarnings("serial")
public class StatementSelectionDialog extends AbstractDialog<Object[], Collection<Transaction>> {
	private StatementSelectionPanel panel;

	public StatementSelectionDialog(Window owner, GlobalData data, CharSequence[] alerts) {
		super(owner, LocalizationData.get("Archive.menu.name"), new Object[]{data, alerts}); //$NON-NLS-1$
	}
	
	private GlobalData getCurrentData() {
		return (GlobalData) data[0];
	}
	
	private CharSequence[] getAlerts() {
		return (CharSequence[]) data[1];
	}

	@Override
	protected JPanel createCenterPane() {
		panel = new StatementSelectionPanel(this.getCurrentData(), this.getAlerts());
		panel.addPropertyChangeListener(StatementSelectionPanel.INVALIDITY_CAUSE, new AutoUpdateOkButtonPropertyListener(this));
		return panel;
	}
	
	@Override
	protected String getOkDisabledCause() {
		return panel.getInvalidityCause();
	}

	@Override
	protected Collection<Transaction> buildResult() {
		StatementSelectionTableModel model = (StatementSelectionTableModel)panel.getTable().getModel();
		Collection<Transaction> result = new ArrayList<Transaction>();
		for (int i = 0; i < getCurrentData().getAccountsNumber(); i++) {
			result.addAll(getTransactions(getCurrentData().getAccount(i), model.getSelectedStatements(i)));
		}
		return result;
	}

	private Collection<Transaction> getTransactions(Account account, Set<String> statementIds) {
		Collection<Transaction> transactions = new ArrayList<Transaction>();
		for (int i = 0; i < getCurrentData().getTransactionsNumber(); i++) {
			Transaction transaction = getCurrentData().getTransaction(i);
			Account tAccount = transaction.getAccount();
			String tStatement = transaction.getStatement();
			if (tAccount.equals(account) && statementIds.contains(tStatement)) {
				transactions.add(transaction);
			}
		}
		return transactions;
	}

	public boolean[] isAccountSelected() {
		StatementSelectionTableModel model = (StatementSelectionTableModel)panel.getTable().getModel();
		return model.isSelectedAccount();
	}
}
