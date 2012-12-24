package net.astesana.cloud.dropbox;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.cloud.Service;

public class DropboxService extends Service<DropboxAccount> {
	static final Collection<String> SCHEMES = Collections.unmodifiableCollection(Arrays.asList(new String[]{FileId.SCHEME}));
	
	private DropboxAPI<? extends WebAuthSession> api;

	public DropboxService(File root, DropboxAPI<? extends WebAuthSession> api) {
		super(root);
		this.api = api;
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
		return this.api;
	}

	@Override
	public Collection<String> getSchemes() {
		return SCHEMES;
	}

	@Override
	public boolean exists(URI uri) {
		// FIXME Auto-generated method stub
		return false;
	}
}
