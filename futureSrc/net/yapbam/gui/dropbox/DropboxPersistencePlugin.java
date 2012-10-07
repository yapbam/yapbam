package net.yapbam.gui.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
	private static final String CACHE_SUFFIX = ".xml";
	private static final String CACHE_PREFIX = "cache";
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
			if (task!=null) task.setPhase("Connecting to Dropbox ...", -1);
			dropboxStream = Dropbox.getAPI().getFileStream(FileId.fromURI(uri).getPath(), null);
			try {
				return extractFromZip(dropboxStream, file, task);
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
		// Create the zip file
		task.setPhase("Preparing upload ...", 100);
		File zipFile = File.createTempFile(getClass().getName(), null);
		try {
	    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
	    try {
        FileInputStream in = new FileInputStream(file);
        try {
	        // Add ZIP entry to output stream.
	        ZipEntry zipEntry = new ZipEntry(file.getName());
	  	    long totalSize = file.length();
	        zipEntry.setSize(totalSize);
					out.putNextEntry(zipEntry);
	        // Transfer bytes from the file to the ZIP file
	  	    byte[] buf = new byte[1024];
	  	    int len;
	  	    long current = 0;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	    				if (SLOW_WRITING) {
	    					try {
	    						Thread.sleep(WAIT_DELAY);
	    					} catch (InterruptedException e) {}
	    				}
	    				if (task.isCancelled()) {
	    					return false;
	    				}
	    				current += len;
	    				task.reportProgress((int)(100*current/totalSize));
	        }
	        // Complete the entry
	        out.closeEntry();
        } finally {
	        in.close();
        }
	    } finally {
		    // Complete the ZIP file
		    out.close();
	    }
	    if (task.isCancelled()) return false;
			FileInputStream stream = new FileInputStream(zipFile);
			try {
				//FIXME For now ... I don't know how cancel the upload
				//Maybe by using ChunkUpload
		    task.setPhase("Uploading to Dropbox ...", -1);
				Dropbox.getAPI().putFileOverwrite(FileId.fromURI(uri).getPath(), stream, zipFile.length(), null);
			} catch (DropboxException e) {
				throw new IOException(e);
			} finally {
				stream.close();
			}
		} finally {
			zipFile.delete();
		}
		return !task.isCancelled();
	}

//	@Override
//	public Long getRemoteDate(URI uri) throws IOException {
//		FileId id = FileId.fromURI(uri);
//		DropboxAPI<? extends WebAuthSession> api = Dropbox.getAPI();
//		api.getSession().setAccessTokenPair(id.getAccessTokenPair());
//		try {
//			Entry metadata = api.metadata(id.getPath(), 1, null, true, null);
//			if (metadata.isDeleted) return null;
//			return RESTUtility.parseDate(metadata.modified).getTime();
//		} catch (DropboxServerException e) {
//			if (e.error==DropboxServerException._404_NOT_FOUND) {
//				return null;
//			} else {
//				throw new IOException(e);
//			}
//		} catch (DropboxException e) {
//			throw new IOException(e);
//		}
//	}
	
	private boolean extractFromZip(InputStream zipStream, File destFile, Cancellable task) throws IOException {
    // Open the zip file
    ZipInputStream in = new ZipInputStream(zipStream);

    // Get the first entry
    ZipEntry nextEntry = in.getNextEntry();
    long totalSize = nextEntry.getSize();
    if (task!=null) task.setPhase("Transferring data from Dropbox ...", 100);
    
    // Open the output file
    OutputStream out = new FileOutputStream(destFile);
    try {
	    // Transfer bytes from the ZIP file to the output file
	    byte[] buf = new byte[1024];
	    int len;
	    long red = 0;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
  				if (SLOW_READING) {
  					try {
  						Thread.sleep(WAIT_DELAY);
  					} catch (InterruptedException e) {}
  				}
  				if (task!=null) {
  					if (task.isCancelled()) return false;
    				red += len;
    				int progress = (int)(red*100/totalSize);
  					task.reportProgress(progress);
  				}
	    }
    } finally {
    	out.close();
    }
    return true;
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
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private boolean isValidFile(String fileName) {
		return fileName.startsWith(CACHE_PREFIX) && fileName.endsWith(CACHE_SUFFIX);
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.RemotePersistencePlugin#getLocalRevision(java.net.URI)
	 */
	@Override
	protected String getLocalBaseRevision(URI uri) throws IOException {
		File file = getLocalFile(uri);
		if (!file.exists()) return null;
		String revision = file.getName().substring(CACHE_PREFIX.length());
		revision = revision.substring(0, revision.length()-CACHE_SUFFIX.length());
		return revision.length()==0?null:revision;
	}

	@Override
	protected void setLocalBaseRevision(URI uri, String revision) {
		File file = getLocalFile(uri);
		file.renameTo(new File(file.getParent(), CACHE_PREFIX+revision+CACHE_SUFFIX));
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
	
	@Override
	protected boolean isLocalSynchronized(URI uri) {
		// TODO Auto-generated method stub
		return false;
	}
}
