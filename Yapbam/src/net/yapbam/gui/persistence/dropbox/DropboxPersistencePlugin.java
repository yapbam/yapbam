package net.yapbam.gui.persistence.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collection;

import org.apache.commons.codec.CharEncoding;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.ChunkedUploader;
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.common.dropbox.FileId;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.Cancellable;
import net.yapbam.gui.persistence.RemotePersistencePlugin;

public class DropboxPersistencePlugin extends RemotePersistencePlugin {
	static final String ZIP_ENTENSION = ".zip"; //$NON-NLS-1$
	private static final String CACHE_PREFIX = "cache"; //$NON-NLS-1$
	private static final String SYNCHRONIZED_CACHE_PREFIX = "sync"; //$NON-NLS-1$
	private static final int WAIT_DELAY = 30;
	private static final boolean SLOW_READING = Boolean.getBoolean("slowDataReading"); //$NON-NLS-1$

	@Override
	public AbstractURIChooserPanel buildChooser() {
		return new YapbamDropboxFileChooser();
	}

	@Override
	public Collection<String> getSchemes() {
		return YapbamDropboxFileChooser.SCHEMES;
	}

	@Override
	public boolean download(URI uri, File file, Cancellable task) throws IOException {
		DropboxInputStream dropboxStream;
		try {
			String path = FileId.fromURI(uri).getPath();
	    long totalSize = -1;
	    if (task!=null) {
	    	totalSize = Dropbox.getAPI().metadata(path, 0, null, false, null).bytes;
	    	task.setPhase(LocalizationData.get("dropbox.downloading"), totalSize>0?100:-1); //$NON-NLS-1$
	    }
			dropboxStream = Dropbox.getAPI().getFileStream(path, null);
			try {
		    // Open the output file
		    OutputStream out = new FileOutputStream(file);
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
		    } finally {
		    	out.close();
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
	public boolean upload(File file, URI uri, final Cancellable task) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		try {
			long length = file.length();
			String path = FileId.fromURI(uri).getPath();

			// This implementation uses ChunkUploader to allow the user to cancel the upload
			// It has a major problem:
			// 1 - It seems that each chunk requires a new connection to Dropbox. On some network configuration (with very slow proxy)
			//   this dramatically slows down the upload. We use a chunck size equals to the file size to prevent having such a problem.
			//   For that reason, the task will never been informed of the upload progress.
	    if (task!=null) task.setPhase(LocalizationData.get("dropbox.uploading"), -1); //$NON-NLS-1$
	    final ChunkedUploader uploader = Dropbox.getAPI().getChunkedUploader(stream, length, (int)length);
	    ProgressListener listener = new ProgressListener() {
				@Override
				public void onProgress(long arg0, long arg1) {
					if (task!=null && task.isCancelled()) uploader.abort();
				}
			};
			try {
				uploader.upload(listener);
			} catch (DropboxPartialFileException e) {
				// Upload was cancelled
				return false;
			}
			uploader.finish(path, Dropbox.getAPI().metadata(path, 1, null, false, null).rev);
			return true;

/**/
/*
	    // Here is an implementation that do not use chunck upload
			// Its major problems are:
			// - It is not possible to cancel the upload before it is completed
			// - When the upload is cancelled, the Dropbox file is reverted to its previous version but it's revision is incremented
			//   This causes the synchronization process to consider the file has been modified after the upload
			if (task!=null) task.setPhase(LocalizationData.get("dropbox.uploading"), -1); //$NON-NLS-1$
			// As this implementation doesn't allow to cancel during the upload, we will remember what was the file state
			Entry previous = null;
			try {
				previous = Dropbox.getAPI().metadata(path, 1, null, false, null);
				if (previous.isDeleted) previous = null;
			} catch (DropboxServerException e) {
				if (e.error!=DropboxServerException._404_NOT_FOUND) {
					throw e;
				}
			}
			Dropbox.getAPI().putFileOverwrite(path, stream, length, null);
			if (task.isCancelled()) {
				// The upload was cancelled
				if (previous==null) {
					// The file do not existed before, delete it
					Dropbox.getAPI().delete(path);
				} else {
					// Revert to the previous version
					// Unfortunately, this not really revert to the previous state as it creates a new revision on Dropbox
					Dropbox.getAPI().restore(path, previous.rev);
				}
			}
/**/
		} catch (DropboxException e) {
			throw new IOException(e);
		} finally {
			stream.close();
		}
	}

	@Override
	public File getLocalFile(URI uri) {
		// Il y a une subtilité dans l'implémentation :
		// On a besoin de mémoriser la révision de base du fichier de cache. Cette révision va être codée dans le nom du fichier.
		// On va stocker ce fichier dans un répertoire de même nom que le path de l'URI. Le nom du fichier, lui, sera la révision
		// sur laquelle il est basé.
		try {
			FileId id = FileId.fromURI(uri);
			String folder = URLEncoder.encode(id.getAccount(), CharEncoding.UTF_8);
			String fileName = URLEncoder.encode(id.getPath().substring(1), CharEncoding.UTF_8);
			fileName = fileName.substring(0, fileName.length()-ZIP_ENTENSION.length());
			File cacheDirectory = new File(getCacheFolder(uri.getScheme()), folder+"/"+fileName); //$NON-NLS-1$
			if (cacheDirectory.isFile()) {
				// hey ... there's a file where it should be a folder !!!
				// Cache is corrupted, try to repair it
				cacheDirectory.delete();
			}
			if (!cacheDirectory.exists()) {
				cacheDirectory.mkdirs();
			}
			String[] files = cacheDirectory.list();
			// If there's no cache file, return the default cache file
			if (files.length==0) return new File(cacheDirectory, CACHE_PREFIX+ZIP_ENTENSION);
			// There's at least one file in the cache, return the most recent (delete others)
			File result = null;
			for (String f : files) {
				File candidate = new File(cacheDirectory, f);
				if (isValidFile(f) && ((result==null) || (candidate.lastModified()>result.lastModified()))) {
					if (result!=null) result.delete();
					result = candidate;
				} else {
					candidate.delete();
				}
			}
			return result!=null?result:new File(cacheDirectory, CACHE_PREFIX+ZIP_ENTENSION);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private boolean isValidFile(String fileName) {
		return (fileName.startsWith(SYNCHRONIZED_CACHE_PREFIX) || fileName.startsWith(CACHE_PREFIX)) && fileName.endsWith(ZIP_ENTENSION);
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.RemotePersistencePlugin#getLocalRevision(java.net.URI)
	 */
	@Override
	protected String getLocalBaseRevision(URI uri) throws IOException {
		File file = getLocalFile(uri);
		if (!file.exists()) return null;
		String name = file.getName();
		String revision = name.substring(name.startsWith(CACHE_PREFIX) ? CACHE_PREFIX.length() : SYNCHRONIZED_CACHE_PREFIX.length());
		revision = revision.substring(0, revision.length()-ZIP_ENTENSION.length());
		return revision.length()==0?null:revision;
	}

	@Override
	protected void setLocalBaseRevision(URI uri, String revision) {
		File file = getLocalFile(uri);
		file.renameTo(new File(file.getParent(), SYNCHRONIZED_CACHE_PREFIX+revision+ZIP_ENTENSION));
	}

	@Override
	protected boolean isLocalSynchronized(URI uri) {
		return getLocalFile(uri).getName().startsWith(SYNCHRONIZED_CACHE_PREFIX);
	}

	@Override
	public File getLocalFileForWriting(URI uri) {
		File file = getLocalFile(uri);
		if (file.getName().startsWith(SYNCHRONIZED_CACHE_PREFIX)) {
			String name = file.getName().substring(SYNCHRONIZED_CACHE_PREFIX.length());
			file = new File(file.getParent(), CACHE_PREFIX+name);
		}
		return file;
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
