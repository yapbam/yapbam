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
		//TODO
	}
}