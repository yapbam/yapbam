package net.yapbam.ihm.actions;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.GlobalData;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.dialogs.AbstractDialog;
import net.yapbam.ihm.dialogs.AccountDialog;

@SuppressWarnings("serial")
public class DeleteModeAction extends AbstractAction {
	
	public DeleteModeAction() { //LOCAL
		super("Supprimer");
        putValue(SHORT_DESCRIPTION, "tooltip");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO
	}
}