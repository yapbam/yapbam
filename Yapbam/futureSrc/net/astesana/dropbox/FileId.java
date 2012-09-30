package net.astesana.dropbox;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.CharEncoding;

import net.astesana.ajlib.utilities.StringUtils;

import com.dropbox.client2.session.AccessTokenPair;

/** A Dropbox fileId.*/
public class FileId {
	public static final String SCHEME = "Dropbox";

	private AccessTokenPair tokens;
	private String account;
	private String path;
//	private String rev;
	
	FileId (AccessTokenPair tokens, String account, String path) {
		this.path = path;
		if	(!this.path.startsWith("/")) this.path = "/"+path;
		this.tokens = tokens;
		this.account = account;
	}
	
	public String toString() {
		//FIXME the strings may contains delimiters !!!
		StringBuilder builder = new StringBuilder();
		builder.append(SCHEME);
		builder.append("://");
		builder.append(URLEncoder.encode(account));
		builder.append(":");
		builder.append(tokens.key);
		builder.append("-");
		builder.append(tokens.secret);
		builder.append('@');
		builder.append("dropbox.yapbam.net");
		builder.append('/');
		builder.append(URLEncoder.encode(path.substring(1)));
//		if (rev!=null) {
//			builder.append('?');
//			builder.append("rev=");
//			builder.append(rev);
//		}
		return builder.toString();
	}
	
	public URI toURI() {
		try {
			return new URI(toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static FileId fromURI(URI uri) {
//		System.out.println ("uri: "+uri);
		String path;
		try {
			path = "/"+URLDecoder.decode(uri.getPath().substring(1), CharEncoding.UTF_8);
			String[] split = StringUtils.split(uri.getUserInfo(), ':');
			String account = URLDecoder.decode(split[0], CharEncoding.UTF_8);
			split = StringUtils.split(split[1], '-');
			AccessTokenPair tokens = new AccessTokenPair(split[0], split[1]);
//			String query = uri.getQuery();
//			String rev = (query!=null && query.startsWith("rev=")) ? query.substring("rev=".length()): null;
//			System.out.println ("account: "+account);
//			System.out.println ("tokens pair: "+tokens);
//			System.out.println ("path: "+path);
//			System.out.println ("Revision: "+rev);
			return new FileId(tokens, account, path);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the tokens
	 */
	public AccessTokenPair getAccessTokenPair() {
		return tokens;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/** Sets the path. */
	public void setPath(String path) {
		this.path = path;
	}

//	/**
//	 * @return the rev
//	 */
//	public String getRev() {
//		return rev;
//	}
}
