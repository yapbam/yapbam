package net.yapbam.gui.recent;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.YapbamDataWrapper;
import net.yapbam.gui.persistence.YapbamPersistenceManager;
import net.yapbam.gui.persistence.PersistenceManager.ErrorProcessor;;

@SuppressWarnings("serial")
public class RecentFileAction extends AbstractAction {
	private URI uri;
	private GlobalData data;
	private RecentFilesPlugin plugin;

	public RecentFileAction(RecentFilesPlugin plugin, URI uri, GlobalData data) {
		super(YapbamPersistenceManager.MANAGER.getAdapter(uri).getService().getDisplayable(uri));
		this.plugin = plugin;
		this.uri = uri;
		this.data = data;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Window ownerWindow = Utils.getOwnerWindow((Component) e.getSource());
		YapbamPersistenceManager.MANAGER.read(ownerWindow, new YapbamDataWrapper(data), uri, new ErrorProcessor() {
			@Override
			public boolean processError(Throwable e) {
				if (e instanceof FileNotFoundException) {
					// The file is unreachable
					String cancel = LocalizationData.get("GenericButton.cancel"); //$NON-NLS-1$
					int result = JOptionPane.showOptionDialog(ownerWindow, LocalizationData.get("RecentFiles.unreachable.message"), LocalizationData.get("RecentFiles.unreachable.title"), JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{LocalizationData.get("GenericButton.yes"),cancel}, cancel); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					if (result==0) {
						plugin.remove(uri);
					}
					return true;
				} else {
					return false;
				}
			}
		});
	}
}
