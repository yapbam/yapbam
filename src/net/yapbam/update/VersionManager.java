package net.yapbam.update;

import java.io.IOException;

import net.yapbam.util.ApplicationContext;

public abstract class VersionManager {
	//WARNING: It is important to stay on an address hosted explicitly by sourceforge and not by www.yapbam.net
	//Probably, in the future, when all the maintainers went away, the domain net yapbam.net will not remain active. Sourceforge will probably do
	private static final String BASE_UPDATE_URL = "http://yapbam.sourceforge.net/updateInfo.php"; //$NON-NLS-1$
	private static final String BETA_BASE_UPDATE_URL = "http://yapbam.sourceforge.net/updateInfoBeta.php"; //$NON-NLS-1$
	
	private VersionManager() {
		// To prevent subclasses from being created
		super();
	}

	public static UpdateInformation getUpdateInformation() throws IOException {
		return new UpdateInformation(ApplicationContext.toURL(Boolean.getBoolean("BetaUpdating")?BETA_BASE_UPDATE_URL:BASE_UPDATE_URL)); //$NON-NLS-1$
	}
}
