package net.yapbam.gui.tools;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker.StateValue;
import javax.swing.UIManager;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.worker.DefaultWorkInProgressPanel;
import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.WorkInProgressPanel;
import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.currency.CurrencyConverter;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.Preferences;

@SuppressWarnings("serial")
final public class CurrencyConverterAction extends AbstractAction {
	
	public CurrencyConverterAction() {
		super(Messages.getString("ToolsPlugIn.currencyConverter.title")); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, Messages.getString("ToolsPlugIn.currencyConverter.toolTip")); //$NON-NLS-1$
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final Window owner = Utils.getOwnerWindow((Component) e.getSource());
		final Worker<CurrencyConverter, Void> worker = new Worker<CurrencyConverter, Void>() {
			@Override
			protected CurrencyConverter doInBackground() throws Exception {
				return new CurrencyConverter(Preferences.INSTANCE.getHttpProxy());
			}
		};
		final WorkInProgressFrame waitFrame = new WorkInProgressFrame(owner, Messages.getString("ToolsPlugIn.currencyConverter.title"), ModalityType.APPLICATION_MODAL, worker) { //$NON-NLS-1$
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
				if (evt.getPropertyName().equals(Worker.STATE_PROPERTY_NAME)) {
					if (evt.getNewValue().equals(StateValue.DONE)) {
						if (!worker.isCancelled()) {
							CurrencyConverter converter = null;
							try {
								converter = worker.get();
							} catch (InterruptedException e) {
							} catch (ExecutionException e) {
								if (e.getCause() instanceof IOException) {
									ErrorManager.INSTANCE.display(owner, null, Messages.getString("ToolsPlugin.currencyConverter.ioErrorMessage")); //$NON-NLS-1$
								} else {
									String message = MessageFormat.format(Messages.getString("CurrencyConverterPanel.errorMessage"), e.getCause()); //$NON-NLS-1$
									ErrorManager.INSTANCE.display(owner, null, message);
								}
							}
							if (converter!=null) {
								new CurrencyConverterDialog(owner, Messages.getString("ToolsPlugIn.currencyConverter.title"), converter).setVisible(true); //$NON-NLS-1$
							}
						}
					}
				}
			}
		});
		waitFrame.setVisible(true);
	}
}