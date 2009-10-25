package net.yapbam.currency.converter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Currency;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String[] currencies = CurrencyConverter.getInstance().getCurrencies();
			System.out.println (Arrays.asList(currencies));
			for (int i = 0; i < currencies.length; i++) {
				System.out.println(Currency.getInstance(currencies[i]).getSymbol());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
