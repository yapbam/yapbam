package net.yapbam.gui.tools.currencyconverter;

import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

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
import net.yapbam.currency.FrankfurterCurrencyConverter;
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
		this.panel.addPropertyChangeListener(evt -> {
			if (SourceSelectionButtons.SOURCE_PROPERTY.equals(evt.getPropertyName())) {
				setSource((Source) evt.getNewValue(), (Source) evt.getOldValue());
			}
		});
		setTitle(Messages.getString("ToolsPlugIn.currencyConverter.title")); //$NON-NLS-1$
	}


	void setSource(Source source, Source oldSource) {
		final AbstractCurrencyConverter converter = getConverter(source);
		SourceManager.setSource(source);
		final Worker<AbstractCurrencyConverter, Void> worker = new Worker<AbstractCurrencyConverter, Void>() {
			@Override
			protected AbstractCurrencyConverter doProcessing() throws Exception {
				converter.update();
				return converter;
			}
		};
		final Window owner = this.isVisible() ? this : Utils.getOwnerWindow(this);
		final WorkInProgressFrame waitFrame = new WorkInProgressFrame(owner, Messages.getString("ToolsPlugIn.currencyConverter.title"), ModalityType.APPLICATION_MODAL, worker) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected WorkInProgressPanel buildProgressPanel() {
				DefaultWorkInProgressPanel pane = (DefaultWorkInProgressPanel) super.buildProgressPanel();
				pane.setMessage(Messages.getString("ToolsPlugin.currencyConverter.wait.message")); //$NON-NLS-1$
				pane.setIcon(UIManager.getIcon("OptionPane.informationIcon")); //$NON-NLS-1$
				return pane;
			}
		};
		SourceSelectionButtons buttons = panel.getSourceSelectionButtons();
		worker.addPropertyChangeListener(evt -> {
			if (evt.getPropertyName().equals(Worker.STATE_PROPERTY_NAME) && evt.getNewValue().equals(StateValue.DONE) && !worker.isCancelled()) {
				onSourceLoaded(worker, waitFrame, buttons);
			}
		});
		buttons.setEnabled(false);
		waitFrame.setVisible(true);
	}


	private void onSourceLoaded(final Worker<AbstractCurrencyConverter, Void> worker,
			final WorkInProgressFrame waitFrame, SourceSelectionButtons buttons) {
		AbstractCurrencyConverter newConverter = null;
		try {
			newConverter = worker.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			waitFrame.disposeNow();
			if (e.getCause() instanceof IOException) {
				ErrorManager.INSTANCE.display(Dialog.this, null, Messages.getString("ToolsPlugin.currencyConverter.ioErrorMessage")); //$NON-NLS-1$
			} else {
				String message = Formatter.format(Messages.getString("CurrencyConverterPanel.errorMessage"), e.getCause()); //$NON-NLS-1$
				ErrorManager.INSTANCE.display(Dialog.this, null, message);
			}
		} finally {
			buttons.setEnabled(true);
		}
		if (newConverter!=null) {
			setConverter(newConverter);
		}
	}
	
	private AbstractCurrencyConverter getConverter(Source source) {
		Proxy proxy;
		try {
			proxy = Preferences.INSTANCE.getHttpProxy();
			if (Source.ECB.equals(source)) {
				return new ECBCurrencyConverter(proxy, new FileCache(new File(Portable.getDataDirectory(), "ExchangeRates.xml"))); //$NON-NLS-1$
			} else {
				return new FrankfurterCurrencyConverter(proxy, new FileCache(new File(Portable.getDataDirectory(), "FrankfurterExchangeRates.json"))); //$NON-NLS-1$
			}
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setConverter(AbstractCurrencyConverter converter) {
		this.panel.setConverter(converter);
		this.setSize(this.getPreferredSize());
	}
}
