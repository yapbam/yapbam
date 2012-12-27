package net.astesana.cloud.dropbox;

import java.io.File;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.utilities.StringUtils;
import net.astesana.cloud.Account;
import net.astesana.cloud.Entry;
import net.astesana.cloud.Service;
import net.astesana.cloud.exceptions.UnreachableHostException;
import net.yapbam.gui.persistence.Cancellable;

public class DropboxService extends Service {
	public static final String URI_SCHEME = "Dropbox";
	
	private DropboxAPI<? extends WebAuthSession> api;

	public DropboxService(File root, DropboxAPI<? extends WebAuthSession> api) {
		super(root);
		this.api = api;
	}

	public DropboxAPI<? extends WebAuthSession> getDropboxAPI() {
		return this.api;
	}

	@Override
	public String getScheme() {
		return URI_SCHEME;
	}
	
	@Override
	public Collection<Entry> getRemoteFiles(Account account, Cancellable task) throws UnreachableHostException {
		DropboxAPI<? extends WebAuthSession> api = getDropboxAPI();
		try {
			// Refresh the quota data
			com.dropbox.client2.DropboxAPI.Account accountInfo = api.accountInfo();
			long quota = accountInfo.quota;
			long used = accountInfo.quotaNormal+accountInfo.quotaShared;
			//FIXME update the account data
			
			if (task.isCancelled()) return null;
			// Get the remote files list //FIXME The following line will hang if content has more than 2500 entries
			List<com.dropbox.client2.DropboxAPI.Entry> contents = api.metadata("", 0, null, true, null).contents; //$NON-NLS-1$
			Collection<Entry> result = new ArrayList<Entry>();
			for (com.dropbox.client2.DropboxAPI.Entry entry : contents) {
				if (!entry.isDeleted) {
					Entry localEntry = filterRemote(account, entry.fileName());
					if (localEntry!=null) result.add(localEntry);
				}
			}
			return result;
		} catch (DropboxException e) {
			Throwable cause = e.getCause();
			if (cause instanceof UnknownHostException) {
				throw new UnreachableHostException();
			} else {
				//FIXME
				e.printStackTrace();
				throw new RuntimeException(cause);
			}
		}
	}

	@Override
	public String getConnectionDataURIFragment(Serializable connectionData) {
		AccessTokenPair tokens = (AccessTokenPair) connectionData;
		return tokens.key + "-" + tokens.secret;
	}

	@Override
	public Serializable getConnectionData(String uriFragment) {
		String[] split = StringUtils.split(uriFragment, '-');
		return new AccessTokenPair(split[0], split[1]);
	}
}
