package net.yapbam.update;

import static org.junit.Assert.assertEquals;

import static net.yapbam.update.VersionManager.BASE_UPDATE_URL;

import org.junit.Test;

public class VersionManagerTest {

	@Test
	public void releaseChannelUrlHasNoQueryParam() {
		assertEquals(BASE_UPDATE_URL, VersionManager.getUpdateURL(false));
	}

	@Test
	public void betaChannelUrlHasBetaQueryParam() {
		assertEquals(BASE_UPDATE_URL + "?canal=beta", VersionManager.getUpdateURL(true));
	}
}
