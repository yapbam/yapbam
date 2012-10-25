package net.yapbam.util;

import java.io.IOException;
import java.io.OutputStream;

public class EchoOutputStream extends OutputStream {
	private OutputStream stream;

	public EchoOutputStream(OutputStream stream) {
		this.stream = stream;
	}
	
	@Override
	public void write(int arg0) throws IOException {
		System.out.print ((char)arg0);
		this.stream.write(arg0);
	}

	@Override
	public void close() throws IOException {
		System.out.println ("Stream closed !");
		this.stream.close();
	}

	@Override
	public void flush() throws IOException {
		System.out.println ("Stream flushed !");
		this.stream.flush();
	}
}
