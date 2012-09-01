package net.yapbam.gui.tools;

import java.awt.Window;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.currency.CurrencyConverter;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class CurrencyConverterDialog extends AbstractDialog<CurrencyConverter, Void> {

	public CurrencyConverterDialog(Window owner, String title, CurrencyConverter converter) {
		super(owner, title, converter);
		getCancelButton().setVisible(false);
		getOkButton().setText(LocalizationData.get("GenericButton.close"));
		getOkButton().setToolTipText(LocalizationData.get("GenericButton.close.ToolTip"));
	}

	@Override
	protected Void buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		return new CurrencyConverterPanel(data);
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
