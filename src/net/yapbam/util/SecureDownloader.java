package net.yapbam.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/** Utility to download an URL to a file and compute the checksum of the downloaded file.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class SecureDownloader {
	private Proxy proxy;
	private String checkSum;
	private long length;
	private boolean cancelled;
	
	/** Constructor.
	 * @param proxy The proxy to used to connect to the download site.
	 * @see java.net.Proxy
	 */
	public SecureDownloader(Proxy proxy) {
		this.proxy = proxy;
		reset();
	}

	private void reset() {
		this.length = 0;
		this.checkSum = null;
		this.cancelled = false;
	}
	
	/** Download an URL to a file.
	 * @param url The url to download.
	 * @param file The file where to store the URL content. Null if you don't want to save the file (if you just want to compute the MD5 hash).
	 * @throws IOException if there's a problem during the download.
	 */
	public synchronized void download(URL url, File file) throws IOException {
		reset();
		// First, create a digest to verify the checksum
  	MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		InputStream in = url.openConnection(proxy).getInputStream();
		byte[] buffer = new byte[2048];
		BufferedOutputStream bos = file==null?null:new BufferedOutputStream(new FileOutputStream(file), buffer.length);
		try {
			int size;
			while ((size = in.read(buffer, 0, buffer.length)) != -1) {
				if (this.isCancelled()) break;
				if (bos!=null) bos.write(buffer, 0, size);
				if (size>0) {
					digest.update(buffer, 0, size);
					this.length += size;
					progress();
				}
			}
		} finally {
			if (bos!=null) {
				bos.flush();
				bos.close();
			}
		}
		if (!cancelled) this.checkSum = CheckSum.toString(digest.digest());
	}
	
	/** Reports the download progress.
	 * <br>This method is called each time the download method get a block of data.
	 * <br>You can use the getDownloadedSize method to know how much bytes have been downloaded.
	 * <br>The default implementation does nothing.
	 * @see #download(URL, File)
	 * @see #getDownloadedSize()
	 */
	protected void progress() {}

	/** Tests whether the download has been canceled or not.
	 * @return true if the download has been canceled. Please note that each time the download method is call, this value is reset to false.
	 * @see #cancel()
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	/** Cancels the download.
	 */
	public void cancel() {
		this.cancelled = true;
	}

	/** Gets the MD5 checksum of the downloaded file.
	 * @return a String or null if the file is not completely downloaded.
	 * @see #download(URL, File)
	 */
	public String getCheckSum() {
		return checkSum;
	}

	/** Gets the number of bytes downloaded since the last call to download.
	 * @return a long.
	 * @see #download(URL, File)
	 */
	public long getDownloadedSize() {
		return length;
	}
}
