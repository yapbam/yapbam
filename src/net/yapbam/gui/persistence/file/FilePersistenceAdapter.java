package net.yapbam.gui.persistence.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.fathzer.soft.jclop.swing.URIChooser;
import com.fathzer.soft.jclop.swing.FileChooserPanel;

import net.astesana.ajlib.utilities.FileUtils;
import net.yapbam.gui.persistence.PersistenceAdapter;

public class FilePersistenceAdapter extends PersistenceAdapter {
	public FilePersistenceAdapter() {
		super(null);
	}

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
		File file = new File(uri);
		try {
			return FileUtils.getCanonical(file);
		} catch (IOException e) {
			return file;
		}
	}
}
