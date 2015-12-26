package net.yapbam.gui.archive;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.fathzer.jlocal.Formatter;
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
import net.yapbam.util.ArrayUtils;
import net.yapbam.util.HtmlUtils;

@SuppressWarnings("serial")
public class ArchiveAction extends AbstractAction {
	private static final class ReadErrorProcessor implements ErrorProcessor {
		private boolean isNewFile = false;
		
		ReadErrorProcessor() {
			this.isNewFile = false;
		}

		@Override
		public boolean processError(Throwable e) {
			// FileNotFound should simply be ignored (the globalData remains unchanged)
			this.isNewFile = true;
			return e instanceof FileNotFoundException;
		}

		boolean isNewFile() {
			return isNewFile;
		}
	}

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
		final Window owner = Utils.getOwnerWindow((Component)e.getSource());

		// Select archive file
		URI uri = getArchiveURI(owner);
		if (uri==null) {
			return;
		}
		
		// Read the archive file
		final GlobalData archiveData = new GlobalData();
		YapbamDataWrapper wrapper = new YapbamDataWrapper(archiveData);
		ReadErrorProcessor errProcessor = new ReadErrorProcessor();
		boolean readIsOk = YapbamPersistenceManager.MANAGER.read(owner, wrapper, uri, errProcessor);
		if (errProcessor.isNewFile()) {
			archiveData.setArchive(true);
			archiveData.setURI(uri);
		} else if (!readIsOk) {
			return;
		}
		
		// Report the collisions between final archive balance and data initial balance (there should be equals).
		CharSequence[] alerts = getAlerts(archiveData);
		if (!ArrayUtils.isAllNull(alerts) || !archiveData.isArchive()) {
			String cancel = LocalizationData.get("GenericButton.cancel"); //$NON-NLS-1$
			String[] options = new String[] {LocalizationData.get("GenericButton.continue"), cancel}; //$NON-NLS-1$
			int continued = JOptionPane.showOptionDialog(owner, getAlertMessage(archiveData, alerts), LocalizationData.get("Generic.warning"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, cancel); //$NON-NLS-1$
			if (continued!=0) {
				return;
			}
		}

		// Select transactions to move
		StatementSelectionDialog filterDialog = new StatementSelectionDialog(owner, data, archiveData, alerts);
		filterDialog.setVisible(true);
		Collection<Transaction> selectedTransactions = filterDialog.getResult();
		if (selectedTransactions == null || selectedTransactions.isEmpty()) {
			return;
		}
		
		// Move transactions
		final boolean old = archiveData.isArchive();
		Transaction[] transactions = selectedTransactions.toArray(new Transaction[selectedTransactions.size()]);
		Archiver archiver = new Archiver() {
			@Override
			protected boolean save(GlobalData data) {
				if (data==archiveData) {
					archiveData.setArchive(old);
				}
				return YapbamPersistenceManager.MANAGER.save(owner, new YapbamDataWrapper(data));
			}
		};
		// As the user can force to archive data in a "standard" file, we have to ensure the archiveData has the archive type  
		archiveData.setArchive(true);
		if (archiver.move(data, archiveData, transactions, filterDialog.isArchiveMode())) {
			JOptionPane.showMessageDialog(owner, Formatter.format(LocalizationData.get(filterDialog.isArchiveMode()?
					"Archive.report": //$NON-NLS-1$
					"Archive.restore.report"), //$NON-NLS-1$
					transactions.length));
		}
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

	private CharSequence[] getAlerts(GlobalData archiveData) {
		CharSequence[] result = new CharSequence[data.getAccountsNumber()];
		for (int i = 0; i < data.getAccountsNumber(); i++) {
			// If there will be transactions archived in the account number i
			Account account = data.getAccount(i);
			Account archiveAccount = archiveData.getAccount(account.getName());
			if (archiveAccount!=null) {
				double arcFinal = archiveAccount.getBalanceData().getFinalBalance();
				if (GlobalData.AMOUNT_COMPARATOR.compare(arcFinal, account.getInitialBalance())!=0) {
					// If archive final balance in the archived account is not the initial balance of the account
					String strFinal = LocalizationData.getCurrencyInstance().format(arcFinal);
					String strInitial = LocalizationData.getCurrencyInstance().format(account.getInitialBalance());
					result[i] = Formatter.format(LocalizationData.get("Archive.accountBalancesNotMatch"), account.getName(), strFinal, strInitial); //$NON-NLS-1$
				}
			}
		}
		return result;
	}
	
	private CharSequence getAlertMessage(GlobalData archiveData, CharSequence[] alerts) {
		StringBuilder builder = new StringBuilder();
		if (!archiveData.isArchive()) {
			builder.append(HtmlUtils.START_TAG);
			builder.append(LocalizationData.get("Archive.warning.notAnArchiveFile")); //$NON-NLS-1$
			builder.append(HtmlUtils.NEW_LINE_TAG);
		}
		for (int i = 0; i < alerts.length; i++) {
			if (alerts[i]!=null) {
				builder.append(builder.length()==0 ? HtmlUtils.START_TAG:HtmlUtils.NEW_LINE_TAG);
				builder.append(alerts[i]);
			}
		}
		if (builder.length()>0) {
			builder.append(HtmlUtils.NEW_LINE_TAG);
			builder.append(HtmlUtils.NEW_LINE_TAG);
			builder.append(LocalizationData.get("Archive.accountBalancesNotMatchFinalMessage")); //$NON-NLS-1$
			builder.append(HtmlUtils.END_TAG);
		}
		return builder;
	}
}
