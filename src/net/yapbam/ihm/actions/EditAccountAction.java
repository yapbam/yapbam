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
	
	public EditAccountAction(GlobalData data) {
		super("Editer");
        putValue(SHORT_DESCRIPTION, "bulle d'aide");
        this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Window owner = e.getSource() instanceof Component ?AbstractDialog.getOwnerWindow((Component) e.getSource()):null;
		BankAccountDialog dialog = new BankAccountDialog(owner, null, data);
		dialog.setContent(data.getAccount(0)); //TODO
		dialog.setVisible(true);
		if (dialog.getAccount()!=null) {
			System.out.println("Account modified");
		}
	}
}