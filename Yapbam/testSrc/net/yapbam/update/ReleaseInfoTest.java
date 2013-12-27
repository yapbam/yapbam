package net.yapbam.update;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class ReleaseInfoTest {

	@Test
	public void test() {
		ReleaseInfo v011 = new ReleaseInfo("0.1.1 (30/01/2012)");
		ReleaseInfo v132p = new ReleaseInfo("1.3.2.preview (15/02/2013)");
		ReleaseInfo v132 = new ReleaseInfo("1.3.2 (20/02/2013)");
		assertTrue(v011.compareTo(v132p)<0);
		assertTrue(v132p.compareTo(v132)<0);
		assertEquals(1, v132.getMajorRevision());
		assertEquals(3, v132.getMinorRevision());
		assertEquals(2, v132.getBuildId());
		assertNull(v132.getPreReleaseComment());
		assertNotNull(v132p.getPreReleaseComment());
		Date d = v132.getReleaseDate();
		GregorianCalendar c = new GregorianCalendar(2013, 01, 20);
		assertEquals(c.getTimeInMillis(), d.getTime());
		
		ReleaseInfo bad = new ReleaseInfo("");
		assertTrue(bad.compareTo(new ReleaseInfo("0.0.0 (01/01/1970)"))<0);
		assertTrue(bad.equals(ReleaseInfo.UNKNOWN));
	}
}
