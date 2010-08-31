package net.yapbam.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.yapbam.util.Crypto;

public class PasswordbasedEncryption {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File file = new File("cryptoTest.txt");
			OutputStream out = new FileOutputStream(file);
			out = Crypto.getPasswordProtectedOutputStream("!gti", out);
			byte[] bytes = "This is just a very simple test. Let see if the end of the message is correct or not".getBytes();
			out.write(bytes);
			out.close();
			InputStream in = new FileInputStream(file);
			in = Crypto.getPasswordProtectedInputStream("!gti", in);
			byte[] result = new byte[bytes.length];
			in.read(result);
			in.close();
			System.out.println (new String(result));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
