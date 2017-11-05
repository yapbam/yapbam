package net.yapbam.gui.administration.filter;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.Filter;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.dialogs.CustomFilterDialog;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class EditFilterAction extends AbstractAction {
	private FilterListPanel panel;
	
	public EditFilterAction(FilterListPanel panel) {
		super(LocalizationData.get("GenericButton.edit"), IconManager.get(Name.EDIT_TRANSACTION)); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Edit the selected pre-defined filter");
    this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int index = panel.getJTable().getSelectedRow();
		final Filter filter = panel.getData().getFilter(index);
		Window owner = e.getSource() instanceof Component ?Utils.getOwnerWindow((Component) e.getSource()):null;
		CustomFilterDialog.FilterData filterData = new CustomFilterDialog.FilterData() {
			@Override
			public GlobalData getGlobalData() {
				return panel.getData();
			}
			@Override
			public Filter getFilter() {
				return filter;
			}
		};
		CustomFilterDialog dialog = new FilterDialog(owner, filterData);
		dialog.setVisible(true);
		Boolean result = dialog.getResult();
		if (result!=null && result) {
			((FiltersTableModel)panel.getJTable().getModel()).update(index);
		}
	}
}