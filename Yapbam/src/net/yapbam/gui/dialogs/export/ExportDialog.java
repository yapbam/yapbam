package net.yapbam.gui.dialogs.export;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;

@SuppressWarnings("serial")
public class ExportDialog extends AbstractDialog {

	public ExportDialog(Window owner, Object data) {
		super(owner, LocalizationData.get("ExportDialog.title"), data); //$NON-NLS-1$
	}

	@Override
	protected Object buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		ExportPanel exportPanel = new ExportPanel();
		FilteredData filteredData = (FilteredData)data;
		exportPanel.getFiltered().setEnabled(filteredData.hasFilter());
		return exportPanel;
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setVisible(boolean visible) {
		this.pack(); //TODO Sure it's useful ?
		super.setVisible(visible);
	}

}
