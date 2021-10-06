package net.yapbam.gui.dialogs.export;

import java.awt.Window;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

@SuppressWarnings("serial")
public class ExportDialog extends AbstractDialog<FilteredData, DataExporter> {

	private ExportPanel exportPanel;

	public ExportDialog(Window owner, FilteredData data) {
		super(owner, LocalizationData.get("ExportDialog.title"), data); //$NON-NLS-1$
	}

	@Override
	protected DataExporter buildResult() {
		DataExporterParameters parameters = exportPanel.getExporterParameters();
		YapbamState.INSTANCE.save(getStateKey(), parameters);
		return new DataExporter(parameters);
	}

	private String getStateKey() {
		return this.getClass().getCanonicalName()+".params"; //$NON-NLS-1$
	}

	private String getOldStateKey() {
		return this.getClass().getCanonicalName()+"."+ExporterParameters.class.getName(); //$NON-NLS-1$
	}

	@Override
	protected JPanel createCenterPane() {
		exportPanel = new ExportPanel();
		boolean hasFilter = data.getFilter().isActive();
		exportPanel.getFiltered().setEnabled(hasFilter);
		exportPanel.addPropertyChangeListener(ExportPanel.INVALIDITY_CAUSE, new AutoUpdateOkButtonPropertyListener(this));
		DataExporterParameters parameters = (DataExporterParameters) YapbamState.INSTANCE.restore(getStateKey());
		// The key name has changed after 0.11.7 (the old key was too long to be saved by java.utils.Preferences)
		// Try with the old name if the new one can't be found
		if (parameters==null) {
			parameters = (DataExporterParameters) YapbamState.INSTANCE.restore(getOldStateKey());
			if (parameters != null) {
				YapbamState.INSTANCE.remove(getOldStateKey());
				YapbamState.INSTANCE.save(getStateKey(), parameters);
			}
		}
		if (parameters!=null) {
			if (!hasFilter) {
				parameters.setExportFilteredData(false);
			}
			if (!exportPanel.setParameters(parameters)) {
				JOptionPane.showMessageDialog(this.getOwner(), LocalizationData.get("ExportDialog.columnsChangedMessage"), //$NON-NLS-1$
						LocalizationData.get("Generic.warning"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
			}
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
