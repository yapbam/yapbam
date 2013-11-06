package net.yapbam.gui.archive;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.jclop.swing.URIChooserDialog;
import com.fathzer.soft.jclop.swing.URIChooserDialog.ConfirmButtonUpdater;

import net.yapbam.data.GlobalData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.TransactionsAddedEvent;
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
						(event instanceof TransactionsAddedEvent)) {
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
		URIChooserDialog dialog = YapbamPersistenceManager.MANAGER.getChooserDialog(owner);
		dialog.setSaveDialog(true);
		dialog.setConfirmIfExisting(false);
		dialog.setTitle(LocalizationData.get("Archive.selectionDialog.title")); //$NON-NLS-1$
		dialog.setConfirmButtonUpdater(new ConfirmButtonUpdater() {
			@Override
			public boolean update(JButton button, URI selectedURI, boolean existing) {
				if (selectedURI==null) return false;
				button.setText(existing?LocalizationData.get("Archive.selectionDialog.select"):LocalizationData.get("Archive.selectionDialog.create")); //$NON-NLS-1$ //$NON-NLS-2$
				return true;
			}
		});
		URI uri = dialog.showDialog();
		if (uri!=null) {
			GlobalData archiveData = new GlobalData();
			YapbamPersistenceManager.MANAGER.read(owner, new YapbamDataWrapper(archiveData), uri, new ErrorProcessor() {
				@Override
				public boolean processError(Throwable e) {
					// FileNotFound should simply be ignored (the globalData remains unchanged)
					return e instanceof FileNotFoundException;
				}
			});
			FilterDialog filterDialog = new FilterDialog(owner, "What transaction do you want to archive ?", data);
			filterDialog.setVisible(true);
			System.out.println(archiveData.getTransactionsNumber()+" transactions in archive");
			JOptionPane.showMessageDialog(owner, "<html>Not finished<br>Go next with "+uri);
		}
	}

}
