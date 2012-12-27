package net.astesana.cloud.dropbox;

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
	private static final String URI_DOMAIN = "dropbox.cloud.astesana.net";
	
	private AccessTokenPair tokens;
	private String accountId;
	private String accountName;
	private String path;
	
	public FileId (AccessTokenPair tokens, String accountId, String accountName, String path) {
		this.path = path;
		if	(!this.path.startsWith("/")) this.path = "/"+path;
		this.tokens = tokens;
		this.accountId = accountId;
		this.accountName = accountName;
	}
	
	public String toString() {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append(DropboxService.URI_SCHEME);
			builder.append("://");
			builder.append(URLEncoder.encode(accountId, CharEncoding.UTF_8));
			builder.append(":");
			builder.append(tokens.key);
			builder.append("-");
			builder.append(tokens.secret);
			builder.append('@');
			builder.append(URI_DOMAIN);
			builder.append('/');
			builder.append(URLEncoder.encode(accountName, CharEncoding.UTF_8));
			builder.append('/');
			builder.append(URLEncoder.encode(path.substring(1), CharEncoding.UTF_8));
			return builder.toString();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public URI toURI() {
		try {
			return new URI(toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static FileId fromURI(URI uri) {
		try {
			String path = URLDecoder.decode(uri.getPath().substring(1), CharEncoding.UTF_8);
			int index = path.indexOf('/');
			String accountName = path.substring(0, index-1);
			path = path.substring(index);
			String[] split = StringUtils.split(uri.getUserInfo(), ':');
			String accountId = URLDecoder.decode(split[0], CharEncoding.UTF_8);
			split = StringUtils.split(split[1], '-');
			AccessTokenPair tokens = new AccessTokenPair(split[0], split[1]);
			return new FileId(tokens, accountId, accountName, path);
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
		return accountId;
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
}
