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
import java.util.zip.ZipInputStream;

import org.apache.commons.codec.CharEncoding;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.RESTUtility;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.dropbox.FileId;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.persistence.PersistenceManager;
import net.yapbam.gui.persistence.RemotePersistencePlugin;

public class DropboxPersistencePlugin extends RemotePersistencePlugin {
	@Override
	public AbstractURIChooserPanel buildChooser() {
		return new YapbamDropboxFileChooser();
	}

	@Override
	public Collection<String> getSchemes() {
		return YapbamDropboxFileChooser.SCHEMES;
	}

	@Override
	public void download(URI uri, File file) throws IOException {
		DropboxInputStream dropboxStream;
		try {
			dropboxStream = Dropbox.getAPI().getFileStream(FileId.fromURI(uri).getPath(), null);
			try {
				extractFromZip(dropboxStream, file);
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
	public void upload(File file, URI uri) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		try {
			Dropbox.getAPI().putFileOverwrite(FileId.fromURI(uri).getPath(), stream, file.length(), null);
		} catch (DropboxException e) {
			throw new IOException(e);
		} finally {
			stream.close();
		}
	}

	@Override
	public Long getRemoteDate(URI uri) throws IOException {
		FileId id = FileId.fromURI(uri);
		DropboxAPI<? extends WebAuthSession> api = Dropbox.getAPI();
		api.getSession().setAccessTokenPair(id.getAccessTokenPair());
		try {
			Entry metadata = api.metadata(id.getPath(), 1, null, true, null);
			if (metadata.isDeleted) return null;
			return RESTUtility.parseDate(metadata.modified).getTime();
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
	
	private void extractFromZip(InputStream zipStream, File destFile) throws IOException {
    // Open the zip file
    ZipInputStream in = new ZipInputStream(zipStream);

    // Get the first entry
    in.getNextEntry();
    // Open the output file
    OutputStream out = new FileOutputStream(destFile);
    try {
	    // Transfer bytes from the ZIP file to the output file
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
    } finally {
    	out.close();
    }
	}
	
	@Override
	public File getLocalFile(URI uri) {
		try {
			FileId id = FileId.fromURI(uri);
			String folder = URLEncoder.encode(id.getAccount(), CharEncoding.UTF_8);
			String fileName = URLEncoder.encode(id.getPath().substring(1), CharEncoding.UTF_8);
			fileName = fileName.substring(0, fileName.length()-".zip".length());
			File cacheDirectory = getCacheFolder();
			return new File(cacheDirectory, folder+"/"+fileName);
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
		builder.append("Dropbox://").append(id.getAccount());
		String path = id.getPath();
		if (path.endsWith(".zip")) path = path.substring(0, path.length()-".zip".length());
		builder.append(path);
		return builder.toString();
	}
	
	@Override
	public String getRevision(URI uri) throws IOException {
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
	 * @see net.yapbam.gui.persistence.RemotePersistencePlugin#getLocalRevision(java.net.URI)
	 */
	@Override
	protected String getLocalRevision(URI uri) throws IOException {
		if (!getLocalFile(uri).exists()) return null;
		//FIXME
		return null;
	}

	public static void main(String[] args) {
		try {
			URI uri = new URI("Dropbox://Jean-Marc+Astesana:0vqjj9jznct586f-1mg71myi8q7z65v@dropbox.yapbam.net/Comptes.zip");
			System.out.println ("URI: "+uri);
			PersistenceManager.MANAGER.read(null, new GlobalData(), uri, new PersistenceManager.ErrorProcessor() {
				@Override
				public boolean processError(Throwable e) {
					e.printStackTrace();
					return false;
				}
			});
			System.out.println ("done");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
