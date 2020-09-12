package net.yapbam.gui.filter;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.FilteredData;

public final class UserDefinedFilterAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private FilteredData data;
	private String filterName;

	public UserDefinedFilterAction(FilteredData data, String filterName) {
		super(data.getGlobalData().getFilter(filterName).getName());
		this.data = data;
		this.filterName = filterName;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		data.getFilter().copy(data.getGlobalData().getFilter(filterName));
	}
}