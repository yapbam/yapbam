package net.yapbam.gui.persistence.file;

import java.io.File;
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
	public File getLocalFile(URI uri) {
		return new File(uri);
	}
}
