package net.yapbam.gui.persistence.dropbox;

import java.io.File;

import net.astesana.ajlib.utilities.FileUtils;
import net.yapbam.util.Portable;

import com.dropbox.client2.DropboxAPI;
import com.fathzer.soft.jclop.dropbox.DropboxService;

public class YapbamDropboxPersistenceAdapter extends DropboxPersistenceAdapter {
	public YapbamDropboxPersistenceAdapter() {
		super(new DropboxService(getCacheFolder(), new DropboxAPI<YapbamDropboxSession>(new YapbamDropboxSession())));
	}
	
	private static File getCacheFolder() {
		File folder = FileUtils.isWritable(Portable.getDataDirectory()) ? Portable.getDataDirectory() : new File(
				System.getProperty("user.home"), ".yapbam"); //$NON-NLS-1$ //$NON-NLS-2$
		return new File(folder, "cache");
	}
}
