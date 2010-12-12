package net.yapbam.gui.tools;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;

@SuppressWarnings("serial")
public class CurrencyConverterDialog extends AbstractDialog {

	public CurrencyConverterDialog(Window owner, String title, Object data) {
		super(owner, title, data);
		this.cancelButton.setVisible(false);
		this.okButton.setText(LocalizationData.get("GenericButton.close"));
		this.okButton.setToolTipText(LocalizationData.get("GenericButton.close.ToolTip"));
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		return new CurrencyConverterPanel();
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}

}
