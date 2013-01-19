package net.yapbam.gui.persistence.dropbox;

import java.io.File;

import com.dropbox.client2.DropboxAPI;
import com.fathzer.soft.jclop.dropbox.DropboxService;
import com.fathzer.soft.jclop.dropbox.swing.DropboxURIChooser;
import com.fathzer.soft.jclop.swing.URIChooser;

import net.astesana.ajlib.utilities.FileUtils;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.RemotePersistencePlugin;
import net.yapbam.util.Portable;

public class DropboxPersistencePlugin extends RemotePersistencePlugin {
	private DropboxService service;
	
	public DropboxPersistencePlugin() {
		super (new DropboxService(getCacheFolder(), new DropboxAPI<YapbamDropboxSession>(new YapbamDropboxSession())));
	}
	
	private static File getCacheFolder() {
		//FIXME
		File folder = FileUtils.isWritable(Portable.getDataDirectory()) ? Portable.getDataDirectory() : new File(
				System.getProperty("java.io.tmpdir"), "yapbam"); //$NON-NLS-1$ //$NON-NLS-2$
		return folder;
	}

	@Override
	public URIChooser buildChooser() {
		return new DropboxURIChooser(this.service);
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.RemotePersistencePlugin#getConflictMessage()
	 */
	@Override
	public String getConflictMessage() {
		return LocalizationData.get("dropbox.conflict"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.RemotePersistencePlugin#getDeletedMessage()
	 */
	@Override
	public String getDeletedMessage() {
		return LocalizationData.get("dropbox.dropbox.remoteDeleted"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.RemotePersistencePlugin#getUploadActionMessage()
	 */
	@Override
	public String getUploadActionMessage() {
		return LocalizationData.get("dropbox.upload.action"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.RemotePersistencePlugin#getDownloadActionMessage()
	 */
	@Override
	public String getDownloadActionMessage() {
		return LocalizationData.get("dropbox.download.action"); //$NON-NLS-1$
	}
}
