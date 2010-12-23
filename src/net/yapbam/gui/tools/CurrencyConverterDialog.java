package net.yapbam.gui.tools;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;

@SuppressWarnings("serial")
public class CurrencyConverterDialog extends AbstractDialog<Void> {

	public CurrencyConverterDialog(Window owner, String title) {
		super(owner, title, null);
		this.cancelButton.setVisible(false);
		this.okButton.setText(LocalizationData.get("GenericButton.close"));
		this.okButton.setToolTipText(LocalizationData.get("GenericButton.close.ToolTip"));
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		return new CurrencyConverterPanel();
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}

}
