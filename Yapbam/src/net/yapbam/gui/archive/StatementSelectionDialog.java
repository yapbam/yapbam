package net.yapbam.gui.archive;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.swing.JPanel;

import net.yapbam.data.Account;
import net.yapbam.data.Filter;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;
import net.yapbam.util.TextMatcher;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

@SuppressWarnings("serial")
public class StatementSelectionDialog extends AbstractDialog<GlobalData, Collection<Transaction>> {
	private StatementSelectionPanel panel;

	public StatementSelectionDialog(Window owner, GlobalData data) {
		super(owner, LocalizationData.get("Archive.menu.name"), data); //$NON-NLS-1$
	}

	@Override
	protected JPanel createCenterPane() {
		panel = new StatementSelectionPanel(this.data);
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
		for (int i = 0; i < data.getAccountsNumber(); i++) {
			Set<String> selectedStatements = model.getSelectedStatements(i);
			if (selectedStatements!=null) {
				result.addAll(getTransactions(data.getAccount(i), selectedStatements));
			}
		}
		return result;
	}

	private Collection<Transaction> getTransactions(Account account, Set<String> statementIds) {
		Collection<Transaction> transactions = new ArrayList<Transaction>();
		for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			Account tAccount = transaction.getAccount();
			String tStatement = transaction.getStatement();
			if (tAccount.equals(account) && statementIds.contains(tStatement)) {
				transactions.add(transaction);
			}
		}
		return transactions;
	}

}
