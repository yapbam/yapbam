package net.yapbam.gui.persistence.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.ajlib.swing.dialog.urichooser.FileChooserPanel;
import net.yapbam.gui.persistence.PersistencePlugin;

public class FilePersistencePlugin extends PersistencePlugin {

	@Override
	public Collection<String> getSchemes() {
		return FileChooserPanel.SCHEMES;
	}

	@Override
	public AbstractURIChooserPanel buildChooser() {
		return new FileChooserPanel();
	}

	@Override
	public Long getRemoteDate(URI uri) throws IOException {
		return new File(uri).lastModified();
	}

	@Override
	protected File getLocalCacheFile(URI uri) {
		return new File(uri);
	}
}
