package net.yapbam.file;

import java.io.File;
import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;

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
			try {
				LnkParser parser = new LnkParser(f);
				System.out.println ("  real path   : "+parser.getRealFilename());
				System.out.println ("  size of real file: "+new File(parser.getRealFilename()).length());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
/**			
			System.out.println (" java7 says: "+Files.isSymbolicLink(Paths.get(f.getPath())));
			try {
				System.out.println (" java7 says: "+Files.readSymbolicLink(Paths.get(f.getPath())));
			} catch (IOException e) {
				e.printStackTrace();
			}
/**/
		}
	}

}
