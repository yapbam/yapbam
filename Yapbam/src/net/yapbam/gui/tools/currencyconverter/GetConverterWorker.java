package net.yapbam.gui.tools.currencyconverter;

import java.io.File;
import java.net.Proxy;

import net.yapbam.currency.AbstractCurrencyConverter;
import net.yapbam.currency.ECBCurrencyConverter;
import net.yapbam.currency.YahooCurrencyConverter;
import net.yapbam.gui.Preferences;
import net.yapbam.util.Portable;

import com.fathzer.soft.ajlib.swing.worker.Worker;

final class GetConverterWorker extends Worker<AbstractCurrencyConverter, Void> {
	private Source source;
	
	public GetConverterWorker(Source source) {
		this.source = source;
	}
	
	@Override
	protected AbstractCurrencyConverter doProcessing() throws Exception {
		Proxy proxy = Preferences.INSTANCE.getHttpProxy();
		if (Source.ECB.equals(source)) {
			return new ECBCurrencyConverter(proxy, new FileCache(new File(Portable.getDataDirectory(), "ExchangeRates.xml"))); //$NON-NLS-1$
		} else {
			return new YahooCurrencyConverter(proxy, new FileCache(new File(Portable.getDataDirectory(), "YahooExchangeRates.xml"))); //$NON-NLS-1$
		}
	}
}