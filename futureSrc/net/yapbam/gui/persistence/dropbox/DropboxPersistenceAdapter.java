package net.yapbam.gui.persistence.dropbox;

import com.fathzer.soft.jclop.dropbox.DropboxService;
import com.fathzer.soft.jclop.dropbox.swing.DropboxURIChooser;
import com.fathzer.soft.jclop.swing.URIChooser;

import net.yapbam.gui.persistence.RemotePersistenceAdapter;

public class DropboxPersistenceAdapter extends RemotePersistenceAdapter {

	protected DropboxPersistenceAdapter(DropboxService service) {
		super(service);
	}

	@Override
	public URIChooser buildChooser() {
		return new DropboxURIChooser((DropboxService) getService());
	}
}
