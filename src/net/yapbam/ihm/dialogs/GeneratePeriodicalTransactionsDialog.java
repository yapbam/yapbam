package net.yapbam.ihm.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.ihm.LocalizationData;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsDialog extends AbstractDialog {//LOCAL

	public GeneratePeriodicalTransactionsDialog(Window owner, GlobalData data) {
		super(owner, "Génération des opérations périodiques", data);
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		return new PeriodicalTransactionGeneratorPanel((GlobalData) data);
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
