package net.yapbam.gui.archive;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.jclop.swing.URIChooserDialog;
import com.fathzer.soft.jclop.swing.URIChooserDialog.ConfirmButtonUpdater;

import net.yapbam.data.Account;
import net.yapbam.data.Archiver;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.TransactionsAddedEvent;
import net.yapbam.data.event.TransactionsRemovedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.YapbamDataWrapper;
import net.yapbam.gui.persistence.YapbamPersistenceManager;
import net.yapbam.gui.persistence.PersistenceManager.ErrorProcessor;

@SuppressWarnings("serial")
public class ArchiveAction extends AbstractAction {
	private GlobalData data;

	public ArchiveAction(GlobalData data) {
		super(LocalizationData.get("Archive.menu.name"), IconManager.get(Name.ARCHIVE)); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("Archive.menu.tooltip")); //$NON-NLS-1$
		this.data = data;
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof EverythingChangedEvent) || (event instanceof TransactionsAddedEvent) ||
						(event instanceof TransactionsRemovedEvent)) {
					refresh();
				}
			}
		});
		refresh();
	}
	
	private void refresh() {
		setEnabled(data.getTransactionsNumber()>0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Window owner = Utils.getOwnerWindow((Component)e.getSource());

		// Select transactions to archive
		StatementSelectionDialog filterDialog = new StatementSelectionDialog(owner, data);
		filterDialog.setVisible(true);
		Collection<Transaction> selectedTransactions = filterDialog.getResult();
		if (selectedTransactions.isEmpty()) {
			return;
		}
		
		// Select archive file
		URI uri = getArchiveURI(owner);
		if (uri==null) {
			return;
		}
		
		// Read the archive file
		GlobalData archiveData = new GlobalData();
		YapbamDataWrapper wrapper = new YapbamDataWrapper(archiveData);
		boolean readIsOk = YapbamPersistenceManager.MANAGER.read(owner, wrapper, uri, new ErrorProcessor() {
			@Override
			public boolean processError(Throwable e) {
				// FileNotFound should simply be ignored (the globalData remains unchanged)
				return e instanceof FileNotFoundException;
			}
		});
		if (!readIsOk) {
			return;
		}
		
		// Report the collisions between final archive balance and data initial balance (there should be equals).
		CharSequence alerts = getAlerts(filterDialog.isAccountSelected(), archiveData);
		if (alerts.length()>0) {
			int continued = JOptionPane.showOptionDialog(owner, alerts, LocalizationData.get("Generic.warning"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
			if (continued!=0) {
				return;
			}
		}
		
		// Copy archived transactions into archive
		Transaction[] transactions = selectedTransactions.toArray(new Transaction[selectedTransactions.size()]);
		Archiver.archive(archiveData, transactions);
		
		// Save the archive
		if (!YapbamPersistenceManager.MANAGER.save(owner, wrapper)) {
			return;
		}
		
		// Remove transactions from the data
		Archiver.remove(data, transactions);
		JOptionPane.showMessageDialog(owner, MessageFormat.format(LocalizationData.get("Archive.report"),transactions.length));
	}

	private URI getArchiveURI(Window owner) {
		URIChooserDialog dialog = YapbamPersistenceManager.MANAGER.getChooserDialog(owner);
		dialog.setSaveDialog(true);
		dialog.setConfirmIfExisting(false);
		dialog.setTitle(LocalizationData.get("Archive.selectionDialog.title")); //$NON-NLS-1$
		dialog.setConfirmButtonUpdater(new ConfirmButtonUpdater() {
			@Override
			public boolean update(JButton button, URI selectedURI, boolean existing) {
				if (selectedURI==null) {
					return false;
				}
				button.setText(existing?LocalizationData.get("GenericButton.select"):LocalizationData.get("GenericButton.new")); //$NON-NLS-1$ //$NON-NLS-2$
				return true;
			}
		});
		return dialog.showDialog();
	}

	private CharSequence getAlerts(boolean[] isAccountSelected, GlobalData archiveData) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < data.getAccountsNumber(); i++) {
			if (isAccountSelected[i]) {
				// If there will be transactions archived in the account number i
				Account account = data.getAccount(i);
				Account archiveAccount = archiveData.getAccount(account.getName());
				if (archiveAccount!=null) {
					double arcFinal = archiveAccount.getBalanceData().getFinalBalance();
					if (GlobalData.AMOUNT_COMPARATOR.compare(arcFinal, account.getInitialBalance())!=0) {
						// If archive final balance in the archived account is not the initial balance of the account
						builder.append(builder.length()==0 ? "<html>":"<br>");
						String strFinal = LocalizationData.getCurrencyInstance().format(arcFinal);
						String strInitial = LocalizationData.getCurrencyInstance().format(account.getInitialBalance());
						builder.append(MessageFormat.format(LocalizationData.get("Archive.accountBalancesNotMatch"), account.getName(), strFinal, strInitial));
					}
				}
			}
		}
		if (builder.length()>0) {
			builder.append("<br><br>");
			builder.append(LocalizationData.get("Archive.accountBalancesNotMatchFinalMessage"));
			builder.append("</html>");
		}
		return builder;
	}
}
