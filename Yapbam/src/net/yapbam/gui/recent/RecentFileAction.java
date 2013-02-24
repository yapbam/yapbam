package net.yapbam.gui.recent;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.net.URI;

import javax.swing.AbstractAction;

import net.astesana.ajlib.swing.Utils;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.persistence.YapbamDataWrapper;
import net.yapbam.gui.persistence.YapbamPersistenceManager;

@SuppressWarnings("serial")
public class RecentFileAction extends AbstractAction {
	private URI uri;
	private GlobalData data;

	public RecentFileAction(URI uri, GlobalData data) {
		super(YapbamPersistenceManager.MANAGER.getAdapter(uri).getService().getDisplayable(uri));
		this.uri = uri;
		this.data = data;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		YapbamPersistenceManager.MANAGER.read(Utils.getOwnerWindow((Component) e.getSource()), new YapbamDataWrapper(data), uri, null);
	}
}
