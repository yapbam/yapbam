package net.yapbam.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/** A builder for constructing URLs with query parameters.
 * <br>It takes care of the separator between the URL and the parameters (? for the first parameter, & for the following ones)
 * and URL-encodes the parameter values using UTF-8.
 */
public class UrlBuilder {
	private static final String UTF_8 = "UTF-8";

	private final StringBuilder url;

	/** Creates a new UrlBuilder from a base URL.
	 * @param baseURL The base URL (eventually already containing query parameters).
	 */
	public UrlBuilder(String baseURL) {
		this.url = new StringBuilder(baseURL);
	}

	/** Appends a query parameter to the URL.
	 * @param name The parameter name (not encoded).
	 * @param value The parameter value (it will be URL-encoded using UTF-8).
	 * @return This builder, for chaining.
	 * @throws UnsupportedEncodingException If UTF-8 encoding is not supported.
	 */
	public UrlBuilder appendParameter(String name, String value) throws UnsupportedEncodingException {
		url.append(url.indexOf("?") >= 0 ? "&" : "?"); //$NON-NLS-1$ //$NON-NLS-2$
		url.append(name).append("=").append(URLEncoder.encode(value, UTF_8)); //$NON-NLS-1$
		return this;
	}

	/** Appends multiple query parameters to the URL.
	 * @param parameters A map of parameter names to values (values will be URL-encoded using UTF-8).
	 * @return This builder, for chaining.
	 * @throws UnsupportedEncodingException If UTF-8 encoding is not supported.
	 */
	public UrlBuilder appendParameters(Map<String, String> parameters) throws UnsupportedEncodingException {
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			appendParameter(entry.getKey(), entry.getValue());
		}
		return this;
	}

	@Override
	public String toString() {
		return url.toString();
	}

	/** Converts the built URL to a {@link URL} instance.
	 * @return The URL.
	 * @throws MalformedURLException If the built URL is malformed.
	 */
	public URL toURL() throws MalformedURLException {
		try {
			return new URI(url.toString()).toURL();
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		}
	}
}
