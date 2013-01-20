package net.yapbam.gui.persistence.dropbox;

import net.yapbam.gui.persistence.PersistenceAdapter;

import com.fathzer.soft.jclop.dropbox.DropboxService;
import com.fathzer.soft.jclop.dropbox.swing.DropboxURIChooser;
import com.fathzer.soft.jclop.swing.URIChooser;

public class DropboxPersistenceAdapter extends PersistenceAdapter {

	protected DropboxPersistenceAdapter(DropboxService service) {
		super(service);
	}

	@Override
	public URIChooser buildChooser() {
		return new DropboxURIChooser((DropboxService) getService());
	}
}
