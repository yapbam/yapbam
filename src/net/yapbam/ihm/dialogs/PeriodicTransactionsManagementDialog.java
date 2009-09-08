package net.yapbam.ihm.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.administration.PeriodicTransactionListPanel;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class PeriodicTransactionsManagementDialog extends AbstractDialog {

	public PeriodicTransactionsManagementDialog(Window owner) {
		super(owner, LocalizationData.get("PeriodicManagementDialog.title"), null);
		this.cancelButton.setVisible(false);
	}

	@Override
	protected Object buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		return new PeriodicTransactionListPanel();
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
