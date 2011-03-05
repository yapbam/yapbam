package net.yapbam.update;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.yapbam.util.CheckSum;

public class SecureDownloader {
	private String checkSum;
	private long length;
	private boolean cancelled;
	
	public SecureDownloader(URL url, File file, Proxy proxy) throws IOException {
		this.cancelled = false;
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
					progress(size);
				}
			}
		} finally {
			if (bos!=null) {
				bos.flush();
				bos.close();
			}
		}
		this.checkSum = CheckSum.toString(digest.digest());
	}

	protected void progress(int blockSize) {
		this.length += blockSize;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public void cancel() {
		this.cancelled = true;
	}

	public String getCheckSum() {
		return checkSum;
	}

	public long getLength() {
		return length;
	}
}
