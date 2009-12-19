package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;
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
		new CustomFilterDialog(owner, data).setVisible(true);
	}
}
