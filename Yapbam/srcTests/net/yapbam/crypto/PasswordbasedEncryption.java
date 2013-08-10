package net.yapbam.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.AccessControlException;
import java.security.NoSuchAlgorithmException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import net.yapbam.util.Crypto;

public class PasswordbasedEncryption {
	private static final String PASSWORD = "blougiboulga";
	private static final String HEADER = "Password encoded file";
	private static final String ENCODING = "UTF-8";
	private static final String GLOBAL_DATA_TAG = "DATA";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File file = new File("cryptoTest.txt");
			
			write(file);
			
			read(file);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	private static void read(File file) throws IOException, NoSuchAlgorithmException {
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		in.mark(1024);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		if (!reader.readLine().equals(HEADER)) {
			System.err.println ("Not an encrypted file");
		} else {
			in.reset();
			in.skip(HEADER.getBytes().length+1);
			reader = new BufferedReader(new InputStreamReader(Crypto.getPasswordProtectedInputStream(PASSWORD, in)));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				System.out.println (line);
			}
			in.close();
		}
	}

	private static void write(File file) throws IOException, TransformerConfigurationException, SAXException {
		OutputStream out = new FileOutputStream(file);
		out.write(HEADER.getBytes());
		out.write('\n');
		out = Crypto.getPasswordProtectedOutputStream(PASSWORD, out);
//		StringBuilder buf = new StringBuilder();
//		for (int i = 0; i < 100; i++) {
//			buf.append("This is just a very simple test. Let see if the end of the message is correct or not\n");
//		}
//		out.write(buf.toString().getBytes());
		StreamResult streamResult = new StreamResult(out);
		SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
		TransformerHandler hd = tf.newTransformerHandler();
		Transformer serializer = hd.getTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, ENCODING);
		serializer.setOutputProperty(OutputKeys.INDENT,"yes");
		hd.setResult(streamResult);
		hd.startDocument();
		
		AttributesImpl atts = new AttributesImpl();
		hd.startElement("","",GLOBAL_DATA_TAG,atts);
		
		hd.endElement("","",GLOBAL_DATA_TAG);
		
		hd.endDocument();
		out.close();
	}
}
