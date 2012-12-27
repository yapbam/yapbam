package net.astesana.cloud;

public class Entry implements Comparable<Entry> {
	private String displayName;
	private Account account;
	
	public Entry(Account account, String displayName) {
		this.displayName = displayName;
		this.account = account;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public int compareTo(Entry o) {
		return this.displayName.compareTo(o.displayName);
	}
}
