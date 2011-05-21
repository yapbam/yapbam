package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AccountDialog;
import net.yapbam.gui.util.AbstractDialog;

@SuppressWarnings("serial")
public class NewAccountAction extends AbstractAction {
	private GlobalData data;
	
	public NewAccountAction(GlobalData data) {
		super(LocalizationData.get("MainMenu.Accounts.New"), IconManager.NEW_ACCOUNT);
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Accounts.New.ToolTip"));
        this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Window owner = e.getSource() instanceof Component ?AbstractDialog.getOwnerWindow((Component) e.getSource()):null;
		AccountDialog.open(data, owner, null);
	}
}