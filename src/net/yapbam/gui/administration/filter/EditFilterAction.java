package net.yapbam.gui.administration.filter;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;

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
		putValue(SHORT_DESCRIPTION, LocalizationData.get("FilterManager.edit.tooltip")); //$NON-NLS-1$
		this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final JTable table = panel.getJTable();
		final int index = table.convertRowIndexToModel(table.getSelectedRow());
		final Filter filter = panel.getData().getFilter(index);
		final Window owner = e.getSource() instanceof Component ?Utils.getOwnerWindow((Component) e.getSource()):null;
		final CustomFilterDialog.FilterData filterData = new CustomFilterDialog.FilterData() {
			@Override
			public GlobalData getGlobalData() {
				return panel.getData();
			}
			@Override
			public Filter getFilter() {
				return filter;
			}
		};
		final FilterDialog dialog = new FilterDialog(owner, filterData);
		dialog.setVisible(true);
		final Boolean result = dialog.getResult();
		if (result!=null && result) {
			((FiltersTableModel)panel.getJTable().getModel()).update(index);
		}
	}
}