package net.astesana.cloud.dropbox;

import java.io.File;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.cloud.Service;
import net.yapbam.gui.persistence.dropbox.Dropbox;

public class DropboxService extends Service<DropboxAccount> {
	public DropboxService(File root) {
		super(root);
	}

	@Override
	public DropboxAccount buildAccount(File file) {
		try {
			return new DropboxAccount(this, file);
		} catch (Exception e) {
			return null; //FIXME
		}
	}

	public DropboxAPI<? extends WebAuthSession> getDropboxAPI() {
		return Dropbox.getAPI();
	}
}
