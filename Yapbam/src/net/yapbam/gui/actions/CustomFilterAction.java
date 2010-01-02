package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainMenuBar;
import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.gui.dialogs.CustomFilterDialog;

@SuppressWarnings("serial")
public class CustomFilterAction extends AbstractAction {
	private FilteredData data;
	private MainMenuBar bar;

	public CustomFilterAction(FilteredData data, MainMenuBar bar) {
		super(LocalizationData.get("MainMenuBar.customizedFilter")); //$NON-NLS-1$
		this.data = data;
		this.bar = bar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Window owner = e.getSource() instanceof Component ?AbstractDialog.getOwnerWindow((Component) e.getSource()):null;
		CustomFilterDialog dialog = new CustomFilterDialog(owner, data);
		dialog.setVisible(true);
		if (dialog.getResult()!=null) {
			bar.updateFilterMenu();
		} else {
			((JCheckBoxMenuItem)e.getSource()).setSelected(!((JCheckBoxMenuItem)e.getSource()).isSelected());
		}
	}
}
