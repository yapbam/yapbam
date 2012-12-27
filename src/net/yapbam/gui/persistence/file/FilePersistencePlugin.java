package net.yapbam.gui.persistence.file;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.ajlib.swing.dialog.urichooser.FileChooserPanel;
import net.yapbam.gui.persistence.PersistencePlugin;

public class FilePersistencePlugin extends PersistencePlugin {
	private static final Collection<String> SCHEMES = Collections.unmodifiableCollection(Arrays.asList(new String[]{FileChooserPanel.SCHEME}));

	@Override
	public Collection<String> getSchemes() {
		return SCHEMES;
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
