package net.yapbam.gui.dropbox;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.dropbox.FileId;
import net.yapbam.gui.persistence.PersistencePlugin;

public class DropboxPersistencePlugin extends PersistencePlugin {
	@Override
	public AbstractURIChooserPanel buildChooser() {
		return new YapbamDropboxFileChooser();
	}
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getScheme() {
		return FileId.SCHEME;
	}
}
