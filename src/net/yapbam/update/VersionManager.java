package net.yapbam.update;

import java.io.IOException;

import net.yapbam.util.ApplicationContext;

public abstract class VersionManager {
	//TODO Use an update address not hosted explicitly by sourceforge but by www.yapbam.net
	//When moving hosting from sourceforge to another location it would be useful to replace http://yapbam.sourceforge.net/updateInfo.php
	//by the following in order to redirect to the new location (example with www.yapbam.net).
	/*
	<?php
			$query = $_SERVER['QUERY_STRING'];
			if (strlen($query)==0) {
				http_response_code(400);
			} else {
				header("Location: http://www.yapbam.net/updateInfo.php?".$query);
			}
	?>
	*/
	private static final String BASE_UPDATE_URL = "http://yapbam.sourceforge.net/updateInfo.php";
	private static final String BETA_BASE_UPDATE_URL = "http://yapbam.sourceforge.net/updateInfoBeta.php";
	
	private VersionManager() {
		// To prevent subclasses from being created
		super();
	}

	public static UpdateInformation getUpdateInformation() throws IOException {
		return new UpdateInformation(ApplicationContext.toURL(Boolean.getBoolean("BetaUpdating")?BETA_BASE_UPDATE_URL:BASE_UPDATE_URL));
	}
}
