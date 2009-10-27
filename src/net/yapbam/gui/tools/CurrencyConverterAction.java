package net.yapbam.gui.tools;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


@SuppressWarnings("serial")
final public class CurrencyConverterAction extends AbstractAction {
	
	public CurrencyConverterAction() {
		super(Messages.getString("ToolsPlugIn.currencyConverter.title")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, Messages.getString("ToolsPlugIn.currencyConverter.toolTip")); //$NON-NLS-1$
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new CurrencyConverterDialog(null, Messages.getString("ToolsPlugIn.currencyConverter.title"), null).setVisible(true); //$NON-NLS-1$
	}
}