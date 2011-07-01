package net.yapbam.gui.dialogs.export;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.util.AbstractDialog;

@SuppressWarnings("serial")
public class ExportDialog extends AbstractDialog<FilteredData, Exporter> {

	private ExportPanel exportPanel;

	public ExportDialog(Window owner, FilteredData data) {
		super(owner, LocalizationData.get("ExportDialog.title"), data); //$NON-NLS-1$
	}

	@Override
	protected Exporter buildResult() {
		ExporterParameters parameters = exportPanel.getExporterParameters();
		YapbamState.INSTANCE.save(getStateKey(), parameters);
		return new Exporter(parameters);
	}

	private String getStateKey() {
		return this.getClass().getCanonicalName()+"."+ExporterParameters.class.getName();
	}

	@Override
	protected JPanel createCenterPane() {
		exportPanel = new ExportPanel();
		boolean hasFilter = data.getFilter().isActive();
		exportPanel.getFiltered().setEnabled(hasFilter);
		exportPanel.addPropertyChangeListener(ExportPanel.INVALIDITY_CAUSE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
		ExporterParameters parameters = (ExporterParameters) YapbamState.INSTANCE.restore(getStateKey());
		if (parameters!=null) {
			if (!hasFilter) parameters.setExportFilteredData(false);
			exportPanel.setParameters(parameters);
		}
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
}
