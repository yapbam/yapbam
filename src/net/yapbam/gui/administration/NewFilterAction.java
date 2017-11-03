package net.yapbam.gui.administration;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class NewFilterAction extends AbstractAction {
	private GlobalData data;
	
	public NewFilterAction(GlobalData data) {
		super(LocalizationData.get("GenericButton.new"), IconManager.get(Name.NEW_TRANSACTION)); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Create a new pre-defined filter");
		this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("new was pressed");
	}
}