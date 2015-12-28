package net.yapbam.gui.archive;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

	public StatementSelectionDialog(Window owner, GlobalData data, GlobalData archiveData, CharSequence[] alerts) {
		super(owner, LocalizationData.get("Archive.menu.name"), new Object[]{data, archiveData, alerts}); //$NON-NLS-1$
	}

	@Override
	protected JPanel createCenterPane() {
		panel = new StatementSelectionPanel((GlobalData) data[0], (GlobalData) data[1], (CharSequence[]) data[2]);
		panel.addPropertyChangeListener(StatementSelectionPanel.INVALIDITY_CAUSE, new AutoUpdateOkButtonPropertyListener(this));
		panel.addPropertyChangeListener(StatementSelectionPanel.ARCHIVE_MODE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setTitle(LocalizationData.get((Boolean)evt.getNewValue()?"Archive.menu.name": //$NON-NLS-1$
					"Archive.restore.title")); //$NON-NLS-1$
			}
		});
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
		for (int i = 0; i < panel.getSource().getAccountsNumber(); i++) {
			result.addAll(getTransactions(panel.getSource().getAccount(i), model.getSelectedStatements(i)));
		}
		return result;
	}

	private Collection<Transaction> getTransactions(Account account, Set<String> statementIds) {
		Collection<Transaction> transactions = new ArrayList<Transaction>();
		for (int i = 0; i < panel.getSource().getTransactionsNumber(); i++) {
			Transaction transaction = panel.getSource().getTransaction(i);
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
	
	public boolean isArchiveMode() {
		return panel.isArchiveMode();
	}
}
