package net.yapbam.gui.persistence.dropbox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.common.dropbox.FileId;
import net.astesana.dropbox.DropboxFileChooser;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class YapbamDropboxFileChooser extends DropboxFileChooser implements AbstractURIChooserPanel {
	static final List<String> SCHEMES = Arrays.asList(new String[]{FileId.SCHEME});

	private URI selectedURI;

	/**
	 * Creates the panel.
	 */
	public YapbamDropboxFileChooser() {
		super(new YapbamFilesTableModel());
		this.selectedURI = null;
		this.addPropertyChangeListener(DropboxFileChooser.SELECTED_FILEID_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				URI old = selectedURI;
				FileId selectedFile = getSelectedFile();
				selectedURI = selectedFile==null?null:selectedFile.toURI();
				firePropertyChange(SELECTED_URI_PROPERTY, old, selectedURI);
			}
		});
		setConfirmAction(new Runnable() {
			@Override
			public void run() {
				firePropertyChange(URI_APPROVED_PROPERTY, false, true);
			}
		});
	}
	
	@Override
	public String getName() {
		return FileId.SCHEME;
	}

	@Override
	public String getTooltip(boolean save) {
		return save?LocalizationData.get("dropbox.Chooser.save.tabTooltip"):LocalizationData.get("dropbox.Chooser.read.tabTooltip"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(DropboxFileChooser.class.getResource("dropbox.png")); //$NON-NLS-1$
	}

	@Override
	protected Entry filter(Entry entry) {
		String fileName = entry.fileName();
		if (fileName.endsWith(DropboxPersistencePlugin.ZIP_ENTENSION)) {
			return entry;
		} else {
			return null;
		}
	}
	
	@Override
	protected DropboxAPI<? extends WebAuthSession> getDropboxAPI() {
		return Dropbox.getAPI();
	}

	@Override
	protected boolean isAccessGranted(DropboxAPI<? extends Session> api) throws DropboxException {
		boolean result = super.isAccessGranted(api);
		if (api.getSession().getAccessTokenPair()!=null) {
			Dropbox.storeKeys(api.getSession().getAccessTokenPair());
		}
		return result;
	}

	@Override
	protected void clearAccess() {
		Dropbox.storeKeys(null);
	}
	
	@Override
	public URI getSelectedURI() {
		return this.selectedURI;
	}

	@Override
	public void setUp() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				refresh();
			}
		});
	}

	@Override
	public Collection<String> getSchemes() {
		return SCHEMES;
	}
	
	/* (non-Javadoc)
	 * @see net.astesana.dropbox.DropboxFileChooser#getSelectedFile()
	 */
	@Override
	public FileId getSelectedFile() {
		FileId selectedFile = super.getSelectedFile();
		if (selectedFile!=null) selectedFile.setPath(selectedFile.getPath()+DropboxPersistencePlugin.ZIP_ENTENSION);
		return selectedFile;
	}

	@Override
	public boolean exist(URI uri) {
		//TODO Verify that the local cache doesn't exists ?
		FileId id = FileId.fromURI(uri);
		if (!getInfo().getAccount().displayName.equals(id.getAccount())) throw new IllegalArgumentException("invalid account"); //$NON-NLS-1$
		String path = id.getPath();
		List<Entry> files = getInfo().getFiles();
		for (Entry entry : files) {
			if (entry.path.equals(path)) return true;
		}
		return false;
	}
}
