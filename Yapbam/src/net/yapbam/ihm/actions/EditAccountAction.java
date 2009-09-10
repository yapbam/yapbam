package net.yapbam.ihm.actions;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import net.yapbam.data.GlobalData;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.dialogs.AbstractDialog;
import net.yapbam.ihm.dialogs.AccountDialog;

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
		JTable table = (JTable)e.getSource();
		Window owner = e.getSource() instanceof Component ?AbstractDialog.getOwnerWindow(table):null;
		AccountDialog dialog = new AccountDialog(owner, null, data);
		int selectedRow = table.getSelectedRow();
		dialog.setContent(data.getAccount(selectedRow));
		dialog.setVisible(true);
		if (dialog.getAccount()!=null) {
			System.out.println("Account modified");//TODO
		}
	}
}