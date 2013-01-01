package net.yapbam.gui.persistence.dropbox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import com.dropbox.client2.DropboxAPI;
import com.fathzer.soft.jclop.Cancellable;
import com.fathzer.soft.jclop.dropbox.DropboxService;
import com.fathzer.soft.jclop.dropbox.swing.DropboxURIChooser;
import com.fathzer.soft.jclop.swing.URIChooser;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.RemotePersistencePlugin;

public class DropboxPersistencePlugin extends RemotePersistencePlugin {
	private DropboxService service;
	
	public DropboxPersistencePlugin() {
		File root = getCacheFolder("Dropbox"); 
		this.service = new DropboxService(root, new DropboxAPI<YapbamDropboxSession>(new YapbamDropboxSession()));
	}

	@Override
	public URIChooser buildChooser() {
		return new DropboxURIChooser(this.service);
	}

	@Override
	public String getScheme() {
		return DropboxService.URI_SCHEME;
	}

	@Override
	public boolean download(URI uri, OutputStream out, Cancellable task) throws IOException {
		return this.service.download(this.service.getEntry(uri), out, task, LocalizationData.getLocale());
	}
	
	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.PersistencePlugin#upload(java.io.File, java.net.URI)
	 */
	@Override
	public boolean upload(InputStream stream, long length, URI uri, final Cancellable task) throws IOException {
		return this.service.upload(stream, length, service.getEntry(uri), task, LocalizationData.getLocale());
	}

	@Override
	public String getLocalPath(URI uri) {
		return service.getLocalPath(service.getEntry(uri));
	}
	
	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.PersistencePlugin#getDisplayableName(java.net.URI)
	 */
	@Override
	public String getDisplayableName(URI uri) {
		return service.getDisplayable(uri);
	}
	
	@Override
	public String getRemoteRevision(URI uri) throws IOException {
		return this.service.getRemoteRevision(service.getEntry(uri));
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
