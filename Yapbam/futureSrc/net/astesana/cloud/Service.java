package net.astesana.cloud;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

import net.astesana.ajlib.utilities.StringUtils;
import net.astesana.cloud.exceptions.UnreachableHostException;
import net.yapbam.gui.persistence.Cancellable;

import org.apache.commons.codec.CharEncoding;

/** This class represents a cloud service, with accounts synchronized with a local cache.
 * <br>Limitation: Account can't contain folder.
 * @author Jean-Marc Astesana
 * Licence GPL v3
 */
public abstract class Service {
	public static final String URI_DOMAIN = "cloud.astesana.net";

	private File root;
	private Collection<Account> accounts;

	/** Constructor.
	 * @param root The root folder of the service (the place where all accounts are cached).<br>
	 * If the folder doesn't exists, it is created.
	 * @throws IllegalArgumentException if it is not possible to create the folder (or root is a file, not a folder)  
	 */
	protected Service(File root) {
		if (!root.exists()) root.mkdirs();
		if (!root.isDirectory()) throw new IllegalArgumentException();
		this.root = root;
		refreshAccounts();
	}
	
	/** Builds an account instance that is cached in a folder passed in argument.
	 * @param folder The folder where the account is cached
	 * @return An account, or null if the folder doesn't not contain a valid account.
	 * @see Account#Account(Service, File)
	 */
	private Account buildAccount(File folder) {
		try {
			return new Account(this, folder);
		} catch (Exception e) {
			return null; //FIXME
		}
	}
	
	/** Gets the available accounts.
	 * @return A collection of available accounts
	 */
	public Collection<Account> getAccounts() {
		return accounts;
	}
	
	/** Forces the account list to be rebuild from the file cache content.
	 */
	public void refreshAccounts() {
		File[] files = root.listFiles();
		accounts = new ArrayList<Account>();
		for (File file : files) {
			if (file.isDirectory()) {
				Account candidate = buildAccount(file);
				if (candidate!=null) accounts.add(candidate);
			}
		}
	}
	
	File getCacheRoot() {
		return root;
	}

	public abstract String getScheme();

	/** Gets the entry related to a remote path.
	 * <br>This method can be used by the account instances in their getRemoteFiles method in order to filter files.
	 * <br>By default, this method returns an entry with a display name equals to path.
	 * @param path The remote path
	 * @return an entry or null if the entry should be ignored
	 */
	public Entry filterRemote(Account account, String path) {
		return new Entry(account, path);
	}
	
	public URI getURI(Account account, String path) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append(getScheme());
			builder.append("://");
			builder.append(URLEncoder.encode(account.getId(), CharEncoding.UTF_8));
			builder.append(":");
			builder.append(getConnectionDataURIFragment(account.getConnectionData()));
			builder.append('@');
			builder.append(URI_DOMAIN);
			builder.append('/');
			builder.append(URLEncoder.encode(account.getDisplayName(), CharEncoding.UTF_8));
			builder.append('/');
			builder.append(URLEncoder.encode(path.substring(1), CharEncoding.UTF_8));
			return new URI(builder.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public Entry getEntry(URI uri) {
		try {
			String path = URLDecoder.decode(uri.getPath().substring(1), CharEncoding.UTF_8);
			int index = path.indexOf('/');
			String accountName = path.substring(0, index);
			path = path.substring(index);
			String[] split = StringUtils.split(uri.getUserInfo(), ':');
			String accountId = URLDecoder.decode(split[0], CharEncoding.UTF_8);
			for (Account account : getAccounts()) {
				if (account.getId().equals(accountId)) {
					return filterRemote(account, path);
				}
			}
			Serializable connectionData = getConnectionData(split[1]);
			return null; //FIXME Unknown account
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public abstract Collection<Entry> getRemoteFiles(Account account, Cancellable task) throws UnreachableHostException;

	public abstract String getConnectionDataURIFragment(Serializable connectionData);
	public abstract Serializable getConnectionData(String uriFragment);
}
