package net.yapbam.gui.persistence.file;

import java.io.File;
import java.net.URI;

import com.fathzer.soft.jclop.swing.URIChooser;
import com.fathzer.soft.jclop.swing.FileChooserPanel;

import net.yapbam.gui.persistence.PersistencePlugin;

public class FilePersistencePlugin extends PersistencePlugin {
	@Override
	public String getScheme() {
		return FileChooserPanel.SCHEME;
	}

	@Override
	public URIChooser buildChooser() {
		return new FileChooserPanel();
	}
	
	@Override
	public File getLocalFile(URI uri) {
		return new File(uri);
	}
}
