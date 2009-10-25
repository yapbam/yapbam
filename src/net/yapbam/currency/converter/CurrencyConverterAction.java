package net.yapbam.currency.converter;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
final class CurrencyConverterAction extends AbstractAction {
	
	CurrencyConverterAction() {
		super("Convertisseur de devises"); //LOCAL
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new CurrencyConverterDialog(null, "Convertisseur de devises", null).setVisible(true);
	}
}