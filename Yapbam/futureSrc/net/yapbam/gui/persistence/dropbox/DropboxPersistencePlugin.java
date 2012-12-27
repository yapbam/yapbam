package net.yapbam.gui.persistence.dropbox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.commons.codec.CharEncoding;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.ChunkedUploader;
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.cloud.Account;
import net.astesana.cloud.dropbox.DropboxService;
import net.astesana.cloud.dropbox.FileId;
import net.astesana.cloud.dropbox.swing.DropboxURIChooser;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.Cancellable;
import net.yapbam.gui.persistence.RemotePersistencePlugin;

public class DropboxPersistencePlugin extends RemotePersistencePlugin {
	private static final int WAIT_DELAY = 30;
	private static final boolean SLOW_READING = Boolean.getBoolean("slowDataReading"); //$NON-NLS-1$

	private DropboxService service;

	@Override
	public AbstractURIChooserPanel buildChooser() {
		File root = getCacheFolder("Dropbox"); 
		this.service = new DropboxService(root, Dropbox.getAPI()) {
			@Override
			public net.astesana.cloud.Entry filterRemote(Account account, String path) {
				if (!path.endsWith(".zip")) return null;
				path = path.substring(0, path.length()-".zip".length());
				return super.filterRemote(account, path);
			}
			@Override
			public URI getURI(Account account, String displayName) {
				return super.getURI(account, displayName+".zip");
			}
		};
		return new DropboxURIChooser(this.service);
	}

	@Override
	public String getScheme() {
		return DropboxService.URI_SCHEME;
	}

	@Override
	public boolean download(URI uri, OutputStream out, Cancellable task) throws IOException {
		try {
			String path = FileId.fromURI(uri).getPath();
	    long totalSize = -1;
	    if (task!=null) {
	    	totalSize = Dropbox.getAPI().metadata(path, 0, null, false, null).bytes;
	    	task.setPhase(LocalizationData.get("dropbox.downloading"), totalSize>0?100:-1); //$NON-NLS-1$
	    }
	    DropboxInputStream dropboxStream = Dropbox.getAPI().getFileStream(path, null);
			try {
		    // Transfer bytes from the file to the output file
		    byte[] buf = new byte[1024];
		    int len;
		    long red = 0;
				while ((len = dropboxStream.read(buf)) > 0) {
					out.write(buf, 0, len);
					if (SLOW_READING) {
						try {
							Thread.sleep(WAIT_DELAY);
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
					if (task != null) {
						if (task.isCancelled()) return false;
						if (totalSize > 0) {
							red += len;
							int progress = (int) (red * 100 / totalSize);
							task.reportProgress(progress);
						}
					}
				}
		    return true;
			} finally {
				dropboxStream.close();
			}
		} catch (DropboxException e) {
			throw new IOException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.PersistencePlugin#upload(java.io.File, java.net.URI)
	 */
	@Override
	public boolean upload(InputStream stream, long length, URI uri, final Cancellable task) throws IOException {
		try {
			String path = FileId.fromURI(uri).getPath();
	    if (task!=null) task.setPhase(LocalizationData.get("dropbox.uploading"), -1); //$NON-NLS-1$

			// This implementation uses ChunkedUploader to allow the user to cancel the upload
			// It has a major trap:
			// It seems that each chunk requires a new connection to Dropbox. On some network configuration (with very slow proxy)
			//   this dramatically slows down the upload. We use a chunck size equals to the file size to prevent having such a problem.
			//   For that reason, the task will never been informed of the upload progress.
	    final ChunkedUploader uploader = Dropbox.getAPI().getChunkedUploader(stream, length);
	    if (task!=null) {
		    task.setCancelAction(new Runnable() {
					@Override
					public void run() {
						uploader.abort();
					}
		    });
	    }
			try {
				int retryCounter = 0;
				while (!uploader.isComplete()) {
					try {
						uploader.upload();
					} catch (DropboxException e) {
						if (retryCounter > 5) throw e; // Give up after a while.
						retryCounter++;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
						}
					}
				}
			} catch (DropboxPartialFileException e) {
				// Upload was cancelled
				return false;
	    } finally {
	    	if (task!=null) task.setCancelAction(null);
	    }
			String parentRev = task!=null && task.isCancelled()?null:getRemoteRevision(uri);
			boolean result = task==null || !task.isCancelled();
			if (result) {
				uploader.finish(path, parentRev);
			} else {
				uploader.abort();
			}
			return result;

/*
	    // Here is an implementation that do not use chunckedUploader
			// Its major problems are:
			// - It is not possible to cancel the upload before it is completed
			// - When the upload is cancelled, the Dropbox file is reverted to its previous version but it's revision is incremented
			//   This causes the synchronization process to consider the file has been modified after the upload
			if (task!=null) task.setPhase(LocalizationData.get("dropbox.uploading"), -1); //$NON-NLS-1$
			// As this implementation doesn't allow to cancel during the upload, we will remember what was the file state
			String previous = getRemoteRevision(uri);
			Dropbox.getAPI().putFileOverwrite(path, stream, length, null);
			if (task!=null && task.isCancelled()) {
				// The upload was cancelled
				if (previous==null) {
					// The file do not existed before, delete it
					Dropbox.getAPI().delete(path);
				} else {
					// Revert to the previous version
					// Unfortunately, this not really revert to the previous state as it creates a new revision on Dropbox
					Dropbox.getAPI().restore(path, previous);
				}
				return false;
			}
			return true;
/**/
		} catch (DropboxException e) {
			System.err.println ("Dropbox Exception !!!");//TODO
			throw new IOException(e);
		}
	}

	@Override
	public String getLocalPath(URI uri) {
		FileId id = FileId.fromURI(uri);
		String folder;
		try {
			folder = URLEncoder.encode(id.getAccount(), CharEncoding.UTF_8);
			String fileName = URLEncoder.encode(id.getPath().substring(1), CharEncoding.UTF_8);
			return folder+"/"+fileName;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.PersistencePlugin#getDisplayableName(java.net.URI)
	 */
	@Override
	public String getDisplayableName(URI uri) {
		FileId id = FileId.fromURI(uri);
		StringBuilder builder = new StringBuilder();
		builder.append("Dropbox://").append(id.getAccount()); //$NON-NLS-1$
		String path = id.getPath();
		if (path.endsWith(ZIP_ENTENSION)) path = path.substring(0, path.length()-ZIP_ENTENSION.length());
		builder.append(path);
		return builder.toString();
	}
	
	@Override
	public String getRemoteRevision(URI uri) throws IOException {
		FileId id = FileId.fromURI(uri);
		DropboxAPI<? extends WebAuthSession> api = Dropbox.getAPI();
		api.getSession().setAccessTokenPair(id.getAccessTokenPair());
		try {
			Entry metadata = api.metadata(id.getPath(), 1, null, true, null);
			if (metadata.isDeleted) return null;
			return metadata.rev;
		} catch (DropboxServerException e) {
			if (e.error==DropboxServerException._404_NOT_FOUND) {
				return null;
			} else {
				throw new IOException(e);
			}
		} catch (DropboxException e) {
			throw new IOException(e);
		}
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
