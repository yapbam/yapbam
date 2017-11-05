package net.yapbam.gui.persistence.dropbox;

import java.io.File;
import java.io.IOException;

import net.yapbam.util.Portable;

import com.fathzer.soft.jclop.dropbox.DropboxService;

public class YapbamDropboxPersistenceAdapter extends DropboxPersistenceAdapter {
	public YapbamDropboxPersistenceAdapter() throws IOException {
		super(new DropboxService(getCacheFolder(), new YapbamDropboxSession()));
	}
	
	private static File getCacheFolder() {
		return new File(Portable.getDataDirectory(), "cache"); //$NON-NLS-1$
	}
}
