package net.yapbam.currency.converter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

import etudes.CurrencyConverter;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println (Arrays.asList(CurrencyConverter.getInstance().getCurrencies()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
