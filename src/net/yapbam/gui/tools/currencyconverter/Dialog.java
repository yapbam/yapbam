package net.yapbam.gui.tools.currencyconverter;

import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker.StateValue;
import javax.swing.UIManager;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.ToolsFrame;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.worker.DefaultWorkInProgressPanel;
import com.fathzer.soft.ajlib.swing.worker.WorkInProgressFrame;
import com.fathzer.soft.ajlib.swing.worker.WorkInProgressPanel;
import com.fathzer.soft.ajlib.swing.worker.Worker;

import net.yapbam.currency.AbstractCurrencyConverter;
import net.yapbam.currency.ECBCurrencyConverter;
import net.yapbam.currency.YahooCurrencyConverter;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.tools.Messages;
import net.yapbam.util.Portable;

@SuppressWarnings("serial")
public class Dialog extends ToolsFrame {
	private DialogMainPanel panel;
	
	public Dialog(Window owner) {
		super(owner, new DialogMainPanel());
		this.panel = (DialogMainPanel) getContentPane();
		this.panel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (DialogButtons.SOURCE_PROPERTY.equals(evt.getPropertyName())) {
					setSource((Source) evt.getNewValue());
				}
			}
		});
		setTitle(Messages.getString("ToolsPlugIn.currencyConverter.title")); //$NON-NLS-1$
	}


	private void setSource(Source source) {
		final AbstractCurrencyConverter converter = getConverter(source);
		SourceManager.setSource(source);
		final Worker<AbstractCurrencyConverter, Void> worker = new Worker<AbstractCurrencyConverter, Void>() {
			@Override
			protected AbstractCurrencyConverter doProcessing() throws Exception {
				converter.update();
				return converter;
			}
		};
		final Window owner = Utils.getOwnerWindow(this);
		final WorkInProgressFrame waitFrame = new WorkInProgressFrame(owner, Messages.getString("ToolsPlugIn.currencyConverter.title"), ModalityType.APPLICATION_MODAL, worker) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected WorkInProgressPanel buildProgressPanel() {
				DefaultWorkInProgressPanel panel = (DefaultWorkInProgressPanel) super.buildProgressPanel();
				panel.setMessage(Messages.getString("ToolsPlugin.currencyConverter.wait.message")); //$NON-NLS-1$
				panel.setIcon(UIManager.getIcon("OptionPane.informationIcon")); //$NON-NLS-1$
				return panel;
			}
		};
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(Worker.STATE_PROPERTY_NAME)
						&& evt.getNewValue().equals(StateValue.DONE) && !worker.isCancelled()) {
					AbstractCurrencyConverter converter = null;
					try {
						converter = worker.get();
					} catch (InterruptedException e) {
					} catch (ExecutionException e) {
						if (e.getCause() instanceof IOException) {
							ErrorManager.INSTANCE.display(owner, null, Messages.getString("ToolsPlugin.currencyConverter.ioErrorMessage")); //$NON-NLS-1$
						} else {
							String message = Formatter.format(Messages.getString("CurrencyConverterPanel.errorMessage"), e.getCause()); //$NON-NLS-1$
							ErrorManager.INSTANCE.display(owner, null, message);
						}
					}
					if (converter!=null) {
						setConverter(converter);
					}
				}
			}
		});
		waitFrame.setVisible(true);
	}
	
	private AbstractCurrencyConverter getConverter(Source source) {
		Proxy proxy;
		try {
			proxy = Preferences.INSTANCE.getHttpProxy();
			if (Source.ECB.equals(source)) {
				return new ECBCurrencyConverter(proxy, new FileCache(new File(Portable.getDataDirectory(), "ExchangeRates.xml"))); //$NON-NLS-1$
			} else {
				return new YahooCurrencyConverter(proxy, new FileCache(new File(Portable.getDataDirectory(), "YahooExchangeRates.xml"))); //$NON-NLS-1$
			}
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setConverter(AbstractCurrencyConverter converter) {
		this.panel.setConverter(converter);
		this.setSize(this.getPreferredSize());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Dialog.this.setLocationRelativeTo(Dialog.this.getOwner());
			}
		});
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			setSource(SourceManager.getSource());
		}
		super.setVisible(visible);
	}
}
