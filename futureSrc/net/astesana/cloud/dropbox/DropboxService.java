package net.astesana.cloud.dropbox;

import java.io.File;

import net.astesana.cloud.Service;

public class DropboxService extends Service<DropboxAccount> {
	protected DropboxService(File root) {
		super(root);
	}

	@Override
	public DropboxAccount buildAccount(File file) {
		try {
			return new DropboxAccount(file);
		} catch (Exception e) {
			return null;
		}
	}

}
