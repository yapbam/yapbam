package net.yapbam.file;

import java.io.IOException;
import java.io.InputStream;

class SequentialByteReader {
	private long current;
	private byte last;
	private InputStream stream;
	
	SequentialByteReader(InputStream stream) {
		this.current = -1;
		this.stream = stream;
	}
	
	byte getByte(long offset) throws IOException {
		if (offset<0) throw new IllegalArgumentException();
		if (offset<current) throw new IllegalArgumentException("Can't go back");
		if (offset!=current) {
			long toSkip = offset-current-1;
			while (toSkip>0) {
				toSkip = toSkip - stream.skip(toSkip);
			}
			last = (byte) stream.read();
		}
		return last;
	}
}
