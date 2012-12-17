package net.astesana.cloud;

public class Entry implements Comparable<Entry> {
	private String displayName;
	
	public Entry(String name) {
		this.displayName = name;
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
