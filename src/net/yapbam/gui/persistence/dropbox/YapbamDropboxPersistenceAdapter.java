package net.yapbam.gui.persistence.dropbox;

import java.io.File;

import net.yapbam.util.Portable;

import com.dropbox.client2.DropboxAPI;
import com.fathzer.soft.jclop.dropbox.DropboxService;

public class YapbamDropboxPersistenceAdapter extends DropboxPersistenceAdapter {
	public YapbamDropboxPersistenceAdapter() {
		super(new DropboxService(getCacheFolder(), new DropboxAPI<YapbamDropboxSession>(new YapbamDropboxSession())));
	}
	
	private static File getCacheFolder() {
		return new File(Portable.getDataDirectory(), "cache");
	}
}
