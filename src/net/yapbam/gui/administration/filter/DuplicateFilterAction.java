package net.yapbam.gui.administration.filter;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.Filter;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.dialogs.CustomFilterDialog;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class DuplicateFilterAction extends AbstractAction {
	private FilterListPanel panel;
	
	public DuplicateFilterAction(FilterListPanel panel) {
		super(LocalizationData.get("GenericButton.duplicate"), IconManager.get(Name.DUPLICATE_TRANSACTION)); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Duplicates the selected filter");
    this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int index = panel.getJTable().getSelectedRow();
		final Filter filter = new Filter();
		filter.copy(panel.getData().getFilter(index));
		filter.setName(null);
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
		FilterDialog dialog = new FilterDialog(owner, filterData);
		dialog.setFilterName(getCopyName(panel.getData(), panel.getData().getFilter(index).getName()));
		dialog.setVisible(true);
		Boolean result = dialog.getResult();
		if (result!=null && result) {
			filterData.getGlobalData().add(filter);
		}
	}

	private String getCopyName(GlobalData data, String name) {
		String candidate = Formatter.format("{0} - copy", name);
		if (data.getFilter(candidate)==null) {
			return candidate;
		}
		name = candidate;
		for (long i=1; i<Long.MAX_VALUE; i++) {
			candidate = name + " - "+i;
			if (data.getFilter(candidate)==null) {
				return candidate;
			}
		}
		throw new IllegalArgumentException("Unable to find an available copy name");
	}
}