package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AccountDialog;

@SuppressWarnings("serial")
public class NewAccountAction extends AbstractAction {
	private GlobalData data;
	
	public NewAccountAction(GlobalData data) {
		super(LocalizationData.get("MainMenu.Accounts.New"), IconManager.NEW_ACCOUNT); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Accounts.New.ToolTip")); //$NON-NLS-1$
        this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Window owner = e.getSource() instanceof Component ?AbstractDialog.getOwnerWindow((Component) e.getSource()):null;
		AccountDialog.open(data, owner, null);
	}
}
