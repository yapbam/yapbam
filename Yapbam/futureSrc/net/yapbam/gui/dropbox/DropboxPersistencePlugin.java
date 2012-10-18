package net.yapbam.gui.dropbox;

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
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.dropbox.FileId;
import net.yapbam.gui.persistence.Cancellable;
import net.yapbam.gui.persistence.RemotePersistencePlugin;

public class DropboxPersistencePlugin extends RemotePersistencePlugin {
	private static final String CACHE_SUFFIX = ".zip";
	private static final String CACHE_PREFIX = "cache";
	private static final String SYNCHRONIZED_CACHE_PREFIX = "sync";
	private static final int WAIT_DELAY = 30;
	private static final boolean SLOW_WRITING = Boolean.getBoolean("slowDataWriting"); //$NON-NLS-1$
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
	    	task.setPhase("Downloading from Dropbox ...", totalSize>0?100:-1);
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
	public boolean upload(File file, URI uri, Cancellable task) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		try {
			//FIXME For now ... I don't know how cancel the upload
			//Maybe by using ChunkUpload
			long length = file.length();
	    if (task!=null) task.setPhase("Uploading to Dropbox ...", -1);
			Dropbox.getAPI().putFileOverwrite(FileId.fromURI(uri).getPath(), stream, length, null);
		} catch (DropboxException e) {
			throw new IOException(e);
		} finally {
			stream.close();
		}
		return task==null || !task.isCancelled();
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
			fileName = fileName.substring(0, fileName.length()-".zip".length());
			File cacheDirectory = new File(getCacheFolder(uri.getScheme()), folder+"/"+fileName);
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
			if (files.length==0) return new File(cacheDirectory, CACHE_PREFIX+CACHE_SUFFIX);
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
			return result!=null?result:new File(cacheDirectory, CACHE_PREFIX+CACHE_SUFFIX);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private boolean isValidFile(String fileName) {
		return (fileName.startsWith(SYNCHRONIZED_CACHE_PREFIX) || fileName.startsWith(CACHE_PREFIX)) && fileName.endsWith(CACHE_SUFFIX);
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
		revision = revision.substring(0, revision.length()-CACHE_SUFFIX.length());
		return revision.length()==0?null:revision;
	}

	@Override
	protected void setLocalBaseRevision(URI uri, String revision) {
		File file = getLocalFile(uri);
		file.renameTo(new File(file.getParent(), SYNCHRONIZED_CACHE_PREFIX+revision+CACHE_SUFFIX));
	}

	@Override
	protected boolean isLocalSynchronized(URI uri) {
		return getLocalFile(uri).getName().startsWith(SYNCHRONIZED_CACHE_PREFIX);
	}

	@Override
	protected File getLocalFileForWriting(URI uri) {
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
		builder.append("Dropbox://").append(id.getAccount());
		String path = id.getPath();
		if (path.endsWith(".zip")) path = path.substring(0, path.length()-".zip".length());
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
}
