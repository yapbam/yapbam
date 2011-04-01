package net.yapbam.gui.dialogs.export;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;

@SuppressWarnings("serial")
public class ExportDialog extends AbstractDialog<FilteredData> {

	private ExportPanel exportPanel;

	public ExportDialog(Window owner, FilteredData data) {
		super(owner, LocalizationData.get("ExportDialog.title"), data); //$NON-NLS-1$
	}

	@Override
	protected Object buildResult() {
		return exportPanel.getExporter();
	}

	@Override
	protected JPanel createCenterPane() {
		exportPanel = new ExportPanel();
		exportPanel.getFiltered().setEnabled(data.hasFilter());
		exportPanel.addPropertyChangeListener(ExportPanel.INVALIDITY_CAUSE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
		return exportPanel;
	}

	@Override
	protected String getOkDisabledCause() {
		return exportPanel.getInvalidityCause();
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.pack();
		super.setVisible(visible);
	}

	public Exporter getExporter() {
		return (Exporter) this.getResult();
	}
}
