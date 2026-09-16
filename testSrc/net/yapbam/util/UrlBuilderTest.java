package net.yapbam.util;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class UrlBuilderTest {
	@Test
	public void firstParameterIsAppendedWithQuestionMark() throws Exception {
		UrlBuilder builder = new UrlBuilder("http://example.com/page");
		builder.appendParameter("name", "value");
		assertEquals("http://example.com/page?name=value", builder.toString());
	}

	@Test
	public void followingParametersAreAppendedWithAmpersand() throws Exception {
		UrlBuilder builder = new UrlBuilder("http://example.com/page?canal=beta");
		builder.appendParameter("version", "1.0");
		assertEquals("http://example.com/page?canal=beta&version=1.0", builder.toString());
	}

	@Test
	public void valueIsUrlEncoded() throws Exception {
		UrlBuilder builder = new UrlBuilder("http://example.com/page");
		builder.appendParameter("q", "a b&c");
		assertEquals("http://example.com/page?q=a+b%26c", builder.toString());
	}

	@Test
	public void appendParametersAddsAllMapEntries() throws Exception {
		UrlBuilder builder = new UrlBuilder("http://example.com/page");
		Map<String, String> params = new LinkedHashMap<>();
		params.put("a", "1");
		params.put("b", "2");
		builder.appendParameters(params);
		assertEquals("http://example.com/page?a=1&b=2", builder.toString());
	}

	@Test
	public void appendParameterIsChainable() throws Exception {
		UrlBuilder builder = new UrlBuilder("http://example.com/page");
		builder.appendParameter("a", "1").appendParameter("b", "2");
		assertEquals("http://example.com/page?a=1&b=2", builder.toString());
	}

	@Test
	public void toURLReturnsEquivalentURL() throws Exception {
		UrlBuilder builder = new UrlBuilder("http://example.com/page");
		builder.appendParameter("a", "1");
		URL url = builder.toURL();
		assertEquals("http://example.com/page?a=1", url.toString());
	}

	@Test(expected = MalformedURLException.class)
	public void toURLThrowsOnMalformedURL() throws Exception {
		UrlBuilder builder = new UrlBuilder("not a valid url");
		builder.toURL();
	}
}
