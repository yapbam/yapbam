package net.astesana.dropbox;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import com.dropbox.client2.session.AccessTokenPair;

/** A Dropbox fileId.*/
public class FileId {
	public static final String SCHEME = "Dropbox";

	private AccessTokenPair tokens;
	private String account;
	private String path;
	private String rev;
	
	FileId (AccessTokenPair tokens, String account, String path) {
		this.rev = null;
		this.path = path;
		this.tokens = tokens;
		this.account = account;
	}
	
	private String toString(boolean fullData) {
		//FIXME the strings may contains delimiters !!!
		StringBuilder builder = new StringBuilder();
		builder.append(SCHEME);
		builder.append("://");
		builder.append(URLEncoder.encode(account));
		if (fullData) {
			builder.append(":");
			builder.append(tokens.key);
			builder.append("-");
			builder.append(tokens.secret);
		}
		builder.append('@');
		builder.append("dropbox.yapbam.net/");
		builder.append(path);
		if (fullData && rev!=null) {
			builder.append('-');
			builder.append(rev);
		}
		return builder.toString();
	}
	
	public String toString() {
		return toString(false);
	}
	
	public URI getURI() {
		try {
			return new URI(toString(true));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
