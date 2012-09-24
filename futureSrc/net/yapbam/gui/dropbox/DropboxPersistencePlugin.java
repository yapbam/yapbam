package net.yapbam.gui.dropbox;

import java.io.File;
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
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.dropbox.FileId;
import net.yapbam.gui.persistence.PersistencePlugin;

public class DropboxPersistencePlugin extends PersistencePlugin {
	@Override
	public AbstractURIChooserPanel buildChooser() {
		return new YapbamDropboxFileChooser();
	}

	@Override
	public Collection<String> getSchemes() {
		return YapbamDropboxFileChooser.SCHEMES;
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.PersistencePlugin#synchronizeForOpening(java.net.URI)
	 */
	@Override
	public URI synchronizeForOpening(URI uri) throws IOException {
		FileId id = FileId.fromURI(uri);
		DropboxAPI<? extends WebAuthSession> api = Dropbox.getAPI();
		api.getSession().setAccessTokenPair(id.getAccessTokenPair());
		try {
			Entry metadata = api.metadata(id.getPath(), 1, null, true, null);
			long dateDropbox = RESTUtility.parseDate(metadata.modified).getTime();
			File file = getLocalCacheFile(uri);
			long dateFile = file.exists()?file.lastModified():0;
			if (dateFile==dateDropbox) {
				// The file date is identical to Dropbox version
				System.out.println ("uptodate");
				return file.toURI();
			} else if (dateFile<dateDropbox) {
				// The file is older than Dropbox version
				// Download the Dropbox version
				System.out.println ("download required");
				file.getParentFile().mkdirs();
				DropboxInputStream dropboxStream = api.getFileStream(id.getPath(), null);
				try {
					extractFromZip(dropboxStream, file);
					file.setLastModified(dateDropbox);
				} finally {
					dropboxStream.close();
				}
				return file.toURI();
			} else {
				// The file is newer than Dropbox version
				return null;
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
	
	public File getLocalCacheFile(URI uri) {
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

	public static void main(String[] args) {
		try {
			DropboxPersistencePlugin plugin = new DropboxPersistencePlugin();
			URI uri = new URI("Dropbox://Jean-Marc+Astesana:0vqjj9jznct586f-1mg71myi8q7z65v@dropbox.yapbam.net/Comptes.zip");
			plugin.synchronizeForOpening(uri);
			System.out.println ("URI: "+uri);
			System.out.println ("Display name: "+plugin.getDisplayableName(uri));
			System.out.println ("Local cache: "+plugin.getLocalCacheFile(uri));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
