package net.yapbam.gui.dialogs.export;

import java.awt.Window;
import java.io.File;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

@SuppressWarnings("serial")
public class ImportDialog extends AbstractDialog<ImportDialog.Container, Importer> {
	private ImportPanel importPanel;
	public static File lastFile;

	static final class Container {
		File file;
		GlobalData data;
		
		public Container(File file, GlobalData data) {
			super();
			this.file = file;
			this.data = data;
		}
	}
	
	public ImportDialog(Window owner, GlobalData data, File file) {
		super(owner, LocalizationData.get("ImportDialog.title"), new Container(file, data)); //$NON-NLS-1$
	}

	@Override
	protected Importer buildResult() {
		Importer importer = importPanel.getImporter();
		ImporterParameters parameters = importer.getParameters();
		YapbamState.INSTANCE.save(getStateKey(), parameters);
		lastFile = importer.getFile();
		return importer;
	}

	private String getOldStateKey(Class<?> saved) {
		return this.getClass().getCanonicalName()+"."+saved.getName();
	}

	private String getStateKey() {
		return this.getClass().getCanonicalName()+".params";
	}

	@Override
	protected JPanel createCenterPane() {
		importPanel = new ImportPanel();
		importPanel.setData(data.data);
		importPanel.setFile(data.file);
		importPanel.addPropertyChangeListener(ImportPanel.INVALIDITY_CAUSE, new AutoUpdateOkButtonPropertyListener(this));
		ImporterParameters parameters = (ImporterParameters) YapbamState.INSTANCE.restore(getStateKey());
		// The key name has changed after 0.11.7 (the old key was too long to be saved by java.utils.Preferences)
		// Try with the old name if the new one can't be found
		if (parameters==null) {
			parameters = (ImporterParameters) YapbamState.INSTANCE.restore(getOldStateKey(ImporterParameters.class));
			if (parameters != null) {
				YapbamState.INSTANCE.remove(getOldStateKey(ImporterParameters.class));
				YapbamState.INSTANCE.save(getStateKey(), parameters);
			}
		}
		if (parameters!=null) importPanel.setParameters(parameters);
		return importPanel;
	}

	@Override
	protected String getOkDisabledCause() {
		return importPanel.getInvalidityCause();
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.pack();
		super.setVisible(visible);
	}

	public boolean getAddToCurrentData() {
		return importPanel.getAddToCurrentData();
	}
}
