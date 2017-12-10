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
public class NewFilterAction extends AbstractAction {
	private GlobalData data;
	
	public NewFilterAction(GlobalData data) {
		super(LocalizationData.get("GenericButton.new"), IconManager.get(Name.NEW_TRANSACTION)); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("FilterManager.new.tooltip")); //$NON-NLS-1$
		this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Window owner = e.getSource() instanceof Component ?Utils.getOwnerWindow((Component) e.getSource()):null;
		final Filter filter = new Filter();
		CustomFilterDialog.FilterData filterData = new CustomFilterDialog.FilterData() {
			@Override
			public GlobalData getGlobalData() {
				return data;
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
			data.add(filterData.getFilter());
		}
	}
}