package net.yapbam.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SymLinksTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (String file : args) {
			File f = new File(file);
			System.out.println (file+" (len= "+f.length()+")");
			System.out.println ("  path     : "+f.getPath());
			System.out.println ("  absolute : "+f.getAbsolutePath());
			try {
				System.out.println ("  canonical: "+f.getCanonicalPath());
			} catch (IOException e) {
				System.out.println ("  unable to resolve the canonical name: "+e);				
			}
/*			LnkParse lnkParse = new LnkParse();
			try {
				lnkParse.parse(file);
				System.out.println ("  lnkparse path    : "+(lnkParse.isLink()?lnkParse.getFullPath():file));
			} catch (IOException e) {
				e.printStackTrace();
			}*/

/*			try {
				OriginalLnkParser parser = new OriginalLnkParser(f);
				System.out.println ("  real path   : "+parser.getRealFilename());
				System.out.println ("  size of real file: "+new File(parser.getRealFilename()).length());
			} catch (IOException e) {
				e.printStackTrace();
			}*/

			LnkParser parser = new LnkParser(f);
			String real = parser.isLink()?parser.getRealFilename():file;
			System.out.println ("  real path   : "+real);
			System.out.println ("  size of real file: "+new File(real));
			
			
			Path path = f.toPath();
			try {
				System.out.println (" java7 says: "+(Files.isSymbolicLink(path)?Files.readSymbolicLink(path):path));
			} catch (IOException e) {
				e.printStackTrace();
			}
/**/
		}
	}

}
