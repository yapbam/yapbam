package net.yapbam.currency;

import java.io.File;
import java.net.Proxy;

import net.yapbam.gui.tools.FileCache;

public class CurrencyConverterTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CurrencyConverter converter = new CurrencyConverter(Proxy.NO_PROXY, new FileCache(new File("toto.txt")));
			System.out.println ("Converter was created ("+(converter.isNetworkSynchronized()?"synchro":"cache data")+")");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
