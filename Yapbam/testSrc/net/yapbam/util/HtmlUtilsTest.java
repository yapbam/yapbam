package net.yapbam.util;

import org.junit.Test;
import static org.junit.Assert.*;


public class HtmlUtilsTest {
	@Test
	public void test() {
		assertEquals ("Just a test", HtmlUtils.removeHtmlTags("<html>Just a test</html>"));
	}
}
