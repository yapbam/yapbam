package net.yapbam.gui.persistence;

import java.net.URI;

/** Denotes that the uri has a scheme that no adapter supports.
 */
public class UnsupportedSchemeException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;

	private URI uri;
	public UnsupportedSchemeException(URI uri) {
		super(uri.getScheme());
		this.uri = uri;
	}
	
	/** Gets the not supported uri.*/
	public URI getURI() {
		return this.uri;
	}
}