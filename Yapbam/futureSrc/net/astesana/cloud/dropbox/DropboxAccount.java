package net.astesana.cloud.dropbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.cloud.Account;
import net.astesana.cloud.Entry;
import net.astesana.cloud.exceptions.UnreachableHostException;
import net.yapbam.gui.persistence.Cancellable;

public class DropboxAccount extends Account<AccessTokenPair> {
	private long quota;
	private long used;

	public DropboxAccount(DropboxService service, File file) throws IOException {
		super(service, file);
		this.quota = -1;
		this.used = -1;
	}
	
	public DropboxAccount(DropboxService service, String id, String name, AccessTokenPair connectionData) throws IOException {
		super(service, id, name, connectionData);
		this.quota = -1;
		this.used = -1;
	}

	@Override
	protected void serializeConnectionData(OutputStream stream) throws IOException {
		AccessTokenPair pair = getConnectionData();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(stream));
		out.write(pair.key);
		out.newLine();
		out.write(pair.secret);
		out.flush();
	}

	@Override
	protected void deserializeConnectionData(InputStream stream) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		String key = in.readLine();
		String secret = in.readLine();
		this.connectionData = new AccessTokenPair(key, secret);
	}
	
	/* (non-Javadoc)
	 * @see net.astesana.cloud.Account#getQuota()
	 */
	@Override
	public long getQuota() {
		return quota;
	}

	/* (non-Javadoc)
	 * @see net.astesana.cloud.Account#getUsed()
	 */
	@Override
	public long getUsed() {
		return used;
	}

	@Override
	public Collection<Entry> getRemoteFiles(Cancellable task) throws UnreachableHostException {
		DropboxAPI<? extends WebAuthSession> api = ((DropboxService)getService()).getDropboxAPI();
		try {
			// Refresh the quota data
			com.dropbox.client2.DropboxAPI.Account accountInfo = api.accountInfo();
			quota = accountInfo.quota;
			used = accountInfo.quotaNormal+accountInfo.quotaShared;
			
			if (task.isCancelled()) return null;
			// Get the remote files list //FIXME The following line will hang if content has more than 2500 entries
			List<com.dropbox.client2.DropboxAPI.Entry> contents = api.metadata("", 0, null, true, null).contents; //$NON-NLS-1$
			Collection<Entry> result = new ArrayList<Entry>();
			for (com.dropbox.client2.DropboxAPI.Entry entry : contents) {
				if (!entry.isDeleted) result.add(new Entry(entry.fileName()));
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
}
