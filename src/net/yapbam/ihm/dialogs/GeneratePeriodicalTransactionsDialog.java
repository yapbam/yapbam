package net.yapbam.ihm.dialogs;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.ihm.LocalizationData;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsDialog extends AbstractDialog {
	private PeriodicalTransactionGeneratorPanel panel;

	public GeneratePeriodicalTransactionsDialog(Window owner, GlobalData data) {
		super(owner, "Génération des opérations périodiques", data);//LOCAL
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		panel = new PeriodicalTransactionGeneratorPanel((GlobalData) data);
		panel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		if (panel.getDate()==null) return "La date saisie est incorrecte";
		if (panel.getTransactions().length==0) return "Aucune opération n'est à générer";
		return null;
	}
}
