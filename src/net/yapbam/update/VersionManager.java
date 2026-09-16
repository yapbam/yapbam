package net.yapbam.update;

import java.io.IOException;

import net.yapbam.util.ApplicationContext;

public abstract class VersionManager {
	//Originally, this URL pointed to SourceForge for durability reasons: the assumption was that SourceForge would outlive
	//the yapbam.net domain once the maintainers went away. This assumption proved wrong: SourceForge dropped support for
	//hosting dynamic web sites, making the update search service impossible to maintain there. The URL now points to yapbam.net.
	static final String BASE_UPDATE_URL = "http://www.yapbam.net/updateInfo"; //$NON-NLS-1$
	private static final String BETA_CHANNEL_QUERY_PARAM = "canal=beta"; //$NON-NLS-1$
	
	private VersionManager() {
		// To prevent subclasses from being created
		super();
	}

	/** Builds the update info URL for the specified channel.
	 * @param beta true to check the beta channel, false to check the release channel.
	 * @return The URL to query for update information.
	 */
	static String getUpdateURL(boolean beta) {
		return BASE_UPDATE_URL + (beta ? "?" + BETA_CHANNEL_QUERY_PARAM : ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Gets the update information from the specified channel.
	 * @param beta true to check the beta channel, false to check the release channel.
	 * @return The update information.
	 * @throws IOException If an I/O error occurs.
	 */
	public static UpdateInformation getUpdateInformation(boolean beta) throws IOException {
		return new UpdateInformation(ApplicationContext.toURL(getUpdateURL(beta)));
	}
}
