package net.yapbam.gui.filter;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.FilteredData;

public final class UserDefinedFilterAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private FilteredData data;
	private int index;

	public UserDefinedFilterAction(FilteredData data, int i) {
		super(data.getGlobalData().getFilter(i).getName());
		this.data = data;
		this.index = i;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		data.getFilter().copy(data.getGlobalData().getFilter(index));
	}
}