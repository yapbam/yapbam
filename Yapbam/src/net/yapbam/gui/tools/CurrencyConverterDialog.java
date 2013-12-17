package net.yapbam.gui.tools;

import java.awt.Window;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.currency.AbstractCurrencyConverter;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class CurrencyConverterDialog extends AbstractDialog<AbstractCurrencyConverter, Void> {

	public CurrencyConverterDialog(Window owner, String title, AbstractCurrencyConverter converter) {
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
