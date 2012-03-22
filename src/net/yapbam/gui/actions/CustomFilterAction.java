package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.CustomFilterDialog;

@SuppressWarnings("serial")
public class CustomFilterAction extends AbstractAction {
	private FilteredData data;

	public CustomFilterAction(FilteredData data) {
		super(LocalizationData.get("MainMenuBar.customizedFilter")); //$NON-NLS-1$
		this.data = data;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Window owner = e.getSource() instanceof Component ?AbstractDialog.getOwnerWindow((Component) e.getSource()):null;
		CustomFilterDialog dialog = new CustomFilterDialog(owner, data);
		dialog.setVisible(true);
		Boolean result = dialog.getResult();
		if ((result==null) || !result) {
			// The menu item check box state is automatically changed, so, if nothing is done, we have to restore it
			((JCheckBoxMenuItem)e.getSource()).setSelected(!((JCheckBoxMenuItem)e.getSource()).isSelected());
		}
	}
}
