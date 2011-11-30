package net.yapbam.file;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class SequentialByteReader {
	private InputStream stream;
	private ArrayList<Byte> read;

	SequentialByteReader(InputStream stream) {
		this.stream = stream;
		this.read = new ArrayList<Byte>();
	}

	byte getByte(int offset) throws IOException {
		if (offset < 0) throw new IllegalArgumentException();
		if (offset >= this.read.size()) {
			read(offset);
		}
		return this.read.get(offset);
	}

	private void read(int offset) throws IOException {
		while(this.read.size()<=offset) {
			int b = this.stream.read();
			if (b==-1) throw new EOFException();
			this.read.add((byte)b);
		}
	}
}
