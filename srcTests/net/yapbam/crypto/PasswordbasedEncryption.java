package net.yapbam.crypto;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.yapbam.util.Crypto;

public class PasswordbasedEncryption {
	private static final String PASSWORD = "blougiboulga";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File file = new File("cryptoTest.txt");
			OutputStream out = new FileOutputStream(file);
			out = Crypto.getPasswordProtectedOutputStream(PASSWORD, out);
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < 100; i++) {
				buf.append("This is just a very simple test. Let see if the end of the message is correct or not\n");
			}
			byte[] bytes = buf.toString().getBytes();
			out.write(bytes);
			out.close();
			InputStream in = new FileInputStream(file);
			in = Crypto.getPasswordProtectedInputStream(PASSWORD, in);
			byte[] result = new byte[bytes.length];
			for (int index=0;index<bytes.length;) {
				int read = in.read(result, index, bytes.length-index);
				if (read<0) throw new EOFException();
				index += read;
			}
			in.close();
			System.out.println (new String(result));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
