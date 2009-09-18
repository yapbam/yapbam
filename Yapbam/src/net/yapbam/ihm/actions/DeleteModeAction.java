package net.yapbam.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.ihm.LocalizationData;

@SuppressWarnings("serial")
public class DeleteModeAction extends AbstractAction {
	
	public DeleteModeAction() {
		super(LocalizationData.get("GenericButton.delete"));
        putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.Delete.tooltip"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO
	}
}