package net.yapbam.junit;

import static org.junit.Assert.*;

import net.yapbam.util.TextMatcher;

import org.junit.Test;

public class TextMatcherTest {
	@Test
	public void testEquals() {
		TextMatcher matcher = new TextMatcher(TextMatcher.Kind.EQUALS, "été".toUpperCase(), true, true);
		assertFalse(matcher.matches("Eté"));
		assertFalse(matcher.matches("été"));
		assertFalse(matcher.matches("ETE"));
		assertTrue(matcher.matches("été".toUpperCase()));
		assertFalse(matcher.matches("été".toUpperCase()+"s"));

		matcher = new TextMatcher(TextMatcher.Kind.EQUALS, "été".toUpperCase(), true, false);
		assertFalse(matcher.matches("Eté"));
		assertFalse(matcher.matches("été"));
		assertTrue(matcher.matches("ETE"));
		assertTrue(matcher.matches("été".toUpperCase()));
		assertFalse(matcher.matches("été".toUpperCase()+"s"));

		matcher = new TextMatcher(TextMatcher.Kind.EQUALS, "été".toUpperCase(), false, false);
		assertTrue(matcher.matches("Eté"));
		assertTrue(matcher.matches("été"));
		assertTrue(matcher.matches("ETE"));
		assertTrue(matcher.matches("été".toUpperCase()));
		assertFalse(matcher.matches("été".toUpperCase()+"s"));

		matcher = new TextMatcher(TextMatcher.Kind.EQUALS, "été".toUpperCase(), false, true);
		assertFalse(matcher.matches("Eté"));
		assertTrue(matcher.matches("été"));
		assertFalse(matcher.matches("ETE"));
		assertTrue(matcher.matches("été".toUpperCase()));
		assertFalse(matcher.matches("été".toUpperCase()+"s"));
	}

	@Test
	public void testContains() {
		TextMatcher matcher = new TextMatcher(TextMatcher.Kind.CONTAINS, "été".toUpperCase(), true, true);
		assertFalse(matcher.matches("Eté"));
		assertFalse(matcher.matches("été"));
		assertFalse(matcher.matches("ETE"));
		assertTrue(matcher.matches("xété".toUpperCase()));
		assertTrue(matcher.matches("été".toUpperCase()+"s"));

		matcher = new TextMatcher(TextMatcher.Kind.CONTAINS, "été".toUpperCase(), true, false);
		assertFalse(matcher.matches("Eté"));
		assertFalse(matcher.matches("été"));
		assertTrue(matcher.matches("ETE"));
		assertTrue(matcher.matches("été".toUpperCase()));
		assertTrue(matcher.matches("x"+"été".toUpperCase()+"s"));

		matcher = new TextMatcher(TextMatcher.Kind.CONTAINS, "été".toUpperCase(), false, false);
		assertTrue(matcher.matches("Eté"));
		assertTrue(matcher.matches("été"));
		assertTrue(matcher.matches("ETE"));
		assertTrue(matcher.matches("été".toUpperCase()));
		assertTrue(matcher.matches("été".toUpperCase()+"s"));

		matcher = new TextMatcher(TextMatcher.Kind.CONTAINS, "été".toUpperCase(), false, true);
		assertFalse(matcher.matches("Eté"));
		assertTrue(matcher.matches("été"));
		assertFalse(matcher.matches("ETE"));
		assertTrue(matcher.matches("xété".toUpperCase()));
		assertTrue(matcher.matches("été".toUpperCase()+"s"));
	}

	@Test
	public void testRegular() {
		TextMatcher matcher = new TextMatcher(TextMatcher.Kind.REGULAR, "^ét.*$", true, true);
		assertFalse(matcher.matches("Eté"));
		assertTrue(matcher.matches("été"));
		assertFalse(matcher.matches("ETE"));
		assertFalse(matcher.matches("été".toUpperCase()));
		assertFalse(matcher.matches("être"));

		matcher = new TextMatcher(TextMatcher.Kind.REGULAR, "^ét.*$", true, false);
		assertFalse(matcher.matches("Eté"));
		assertTrue(matcher.matches("été"));
		assertFalse(matcher.matches("ETE"));
		assertFalse(matcher.matches("été".toUpperCase()));
		assertTrue(matcher.matches("être"));

		matcher = new TextMatcher(TextMatcher.Kind.REGULAR, "^ét.*$", false, false);
		assertTrue(matcher.matches("Eté"));
		assertTrue(matcher.matches("été"));
		assertTrue(matcher.matches("ETE"));
		assertTrue(matcher.matches("été".toUpperCase()));
		assertTrue(matcher.matches("être"));
		assertFalse(matcher.matches("hêtre"));

		matcher = new TextMatcher(TextMatcher.Kind.REGULAR, "^ét.*$", false, true);
		assertFalse(matcher.matches("Eté"));
		assertTrue(matcher.matches("été"));
		assertFalse(matcher.matches("ETE"));
		assertTrue(matcher.matches("été".toUpperCase()));
		assertFalse(matcher.matches("être"));
	}

}
