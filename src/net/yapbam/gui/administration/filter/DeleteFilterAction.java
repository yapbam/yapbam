package net.yapbam.gui.administration.filter;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class DeleteFilterAction extends AbstractAction {
	private FilterListPanel panel;
	
	public DeleteFilterAction(FilterListPanel panel) {
		super(LocalizationData.get("GenericButton.delete"), IconManager.get(Name.DELETE_TRANSACTION)); //$NON-NLS-1$
    putValue(SHORT_DESCRIPTION, LocalizationData.get("FilterManager.delete.tooltip")); //$NON-NLS-1$
    this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final JTable table = panel.getJTable();
		final int index = table.convertRowIndexToModel(table.getSelectedRow());
		panel.getData().remove(panel.getData().getFilter(index));
	}
}