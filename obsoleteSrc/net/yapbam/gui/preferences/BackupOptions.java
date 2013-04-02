package net.yapbam.gui.dialogs.preferences;

import java.math.BigInteger;
import java.net.URI;

/** The automatic backup preferences.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class BackupOptions {
	private boolean enabled;
	private URI uri;
	private boolean compressed;
	private BigInteger spaceLimit;
	
	/** Constructor.
	 * @param enabled true if automatic backup is enabled
	 * @param uri the uri where to perform the backup
	 * @param compressed true if the backup files are compressed
	 * @param spaceLimit the space limit allocated for backup (null means no limit)
	 */
	public BackupOptions(boolean enabled, URI uri, boolean compressed, BigInteger spaceLimit) {
		super();
		this.enabled = enabled;
		this.uri = uri;
		this.compressed = compressed;
		this.spaceLimit = spaceLimit;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public URI getUri() {
		return uri;
	}

	public boolean isCompressed() {
		return compressed;
	}

	/** Gets the maximum space allocated to backup.
	 * @return a BigInteger or null if there's no limit
	 */
	public BigInteger getSpaceLimit() {
		return spaceLimit;
	}
}
