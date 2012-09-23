package net.yapbam.gui.dropbox;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import com.dropbox.client2.DropboxAPI;
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
	public String getScheme() {
		return FileId.SCHEME;
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.PersistencePlugin#synchronizeForOpening(java.net.URI)
	 */
	@Override
	public URI synchronizeForOpening(URI uri) throws ExecutionException {
		FileId id = FileId.fromURI(uri);
		DropboxAPI<? extends WebAuthSession> api = Dropbox.getAPI();
		api.getSession().setAccessTokenPair(id.getAccessTokenPair());
		try {
			Entry metadata = api.metadata(id.getPath(), 1, null, true, null);
			System.out.println (metadata.rev);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return super.synchronizeForOpening(uri);
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
		if (!path.startsWith("/")) builder.append('/');
		builder.append(path);
		return builder.toString();
	}

	public static void main(String[] args) {
		try {
			DropboxPersistencePlugin plugin = new DropboxPersistencePlugin();
			URI uri = new URI("Dropbox://Jean-Marc+Astesana:0vqjj9jznct586f-1mg71myi8q7z65v@dropbox.yapbam.net/Comptes.zip?rev=10");
			plugin.synchronizeForOpening(uri);
			System.out.println (plugin.getDisplayableName(uri));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
