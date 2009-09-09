package net.yapbam.ihm.actions;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.GlobalData;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.dialogs.AbstractDialog;
import net.yapbam.ihm.dialogs.BankAccountDialog;

@SuppressWarnings("serial")
public class EditAccountAction extends AbstractAction {
	private GlobalData data;
	
	public EditAccountAction(GlobalData Data) {
		super(LocalizationData.get("MainMenu.Accounts.New"));
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Accounts.New.ToolTip"));
        this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Window owner = e.getSource() instanceof Component ?AbstractDialog.getOwnerWindow((Component) e.getSource()):null;
		BankAccountDialog.open(data, owner, null);
	}
}