package net.yapbam.gui.dialogs.export;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.HelpManager;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

@SuppressWarnings("serial")
public class ImportDialog extends AbstractDialog<ImportDialog.Container, Importer> {
	private ImportPanel importPanel;
	private static File lastFile;
	private transient IOException instantiateException;

	static final class Container {
		File file;
		GlobalData data;
		
		public Container(File file, GlobalData data) {
			super();
			this.file = file;
			this.data = data;
		}
	}
	
	public ImportDialog(Window owner, GlobalData data, File file) throws IOException {
		super(owner, LocalizationData.get("ImportDialog.title"), new Container(file, data)); //$NON-NLS-1$
		if (instantiateException!=null) {
			throw instantiateException;
		}
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
		return this.getClass().getCanonicalName()+"."+saved.getName(); //$NON-NLS-1$
	}

	private String getStateKey() {
		return this.getClass().getCanonicalName()+".params"; //$NON-NLS-1$
	}

	@Override
	protected JPanel createCenterPane() {
		importPanel = new ImportPanel();
		importPanel.setData(data.data);
		try {
			importPanel.setFile(data.file);
			importPanel.setLine(0);
		} catch (IOException e) {
			instantiateException = e;
		}
		if (instantiateException==null) {
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
			if (parameters!=null) {
				importPanel.setParameters(parameters);
			}
		}
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

	public static void doError(Component parent, IOException e) {
		String title = LocalizationData.get("ImportDialog.errorMessage.title"); //$NON-NLS-1$
		if (e instanceof EmptyImportFileException) {
			JOptionPane.showMessageDialog(parent, LocalizationData.get("ImportDialog.error.emptyFile"), title, JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		} else if (e instanceof FileNotFoundException) {
			JOptionPane.showMessageDialog(parent, LocalizationData.get("ImportDialog.error.unknownFile"), title, JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		} else {
			ErrorManager.INSTANCE.display(parent, e);
		}
	}

	@Override
	protected JComponent createExtraComponent() {
		JLabel jLabel = new JLabel();
		jLabel.setText(LocalizationData.get("ImportDialog.help")); //$NON-NLS-1$
		jLabel.setIcon(IconManager.get(Name.HELP));
		jLabel.setToolTipText(LocalizationData.get("ImportDialog.help.toolTip")); //$NON-NLS-1$
		jLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HelpManager.show(importPanel, HelpManager.IMPORT);
				super.mouseClicked(e);
			}
		});
		return jLabel;
	}
	
	public static File getLastFile() {
		return lastFile;
	}
}
