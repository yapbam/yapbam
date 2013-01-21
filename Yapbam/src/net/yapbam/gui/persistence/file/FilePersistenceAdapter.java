package net.yapbam.gui.persistence.file;

import com.fathzer.soft.jclop.FileSystemService;
import com.fathzer.soft.jclop.swing.URIChooser;
import com.fathzer.soft.jclop.swing.FileChooserPanel;

import net.yapbam.gui.persistence.PersistenceAdapter;

public class FilePersistenceAdapter extends PersistenceAdapter {
	public FilePersistenceAdapter() {
		super(FileSystemService.INSTANCE);
	}

	@Override
	public URIChooser buildChooser() {
		return new FileChooserPanel();
	}
}
