package net.astesana.cloud;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/** This class represents a cloud service, with accounts synchronized with a local cache.
 * <br>Limitation: Account can't contain folder.
 * @author Jean-Marc Astesana
 * Licence GPL v3
 */
public abstract class Service <T extends Account>{
	private File root;
	private Collection<T> accounts;

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
	 * <br>You should override this method in order to create an account. Most of the time, this method
	 * only have to call the account's constructor with a File argument and catch possible exceptions. 
	 * @param folder The folder where the account is cached
	 * @return An account, or null if the folder doesn't not contain a valid account.
	 * @see Account#Account(Service, File)
	 */
	protected abstract T buildAccount(File folder);
	
	/** Gets the available accounts.
	 * @return A collection of available accounts
	 */
	public Collection<T> getAccounts() {
		return accounts;
	}
	
	/** Forces the account list to be rebuild from the file cache content.
	 */
	public void refreshAccounts() {
		File[] files = root.listFiles();
		accounts = new ArrayList<T>();
		for (File file : files) {
			if (file.isDirectory()) {
				T candidate = buildAccount(file);
				if (candidate!=null) accounts.add(candidate);
			}
		}
	}
	
	File getCacheRoot() {
		return root;
	}
}
