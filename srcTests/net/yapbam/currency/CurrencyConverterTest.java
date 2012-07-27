package net.yapbam.currency;

import java.io.File;
import java.net.Proxy;

public class CurrencyConverterTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new CurrencyConverter(Proxy.NO_PROXY, new FileCache(new File("cache.xml")));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
