package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.periodicaltransaction.PeriodicalTransactionDialog;

@SuppressWarnings("serial")
public class NewPeriodicalTransactionAction extends AbstractAction {
	private GlobalData data;
	public NewPeriodicalTransactionAction(GlobalData data) {
		super(LocalizationData.get("GenericButton.new"), IconManager.get(Name.NEW_TRANSACTION)); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("PeriodicalTransactionManager.new.toolTip")); //$NON-NLS-1$
		this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PeriodicalTransactionDialog.open(data, Utils.getOwnerWindow((Component) e.getSource()), null, false);
	}
}