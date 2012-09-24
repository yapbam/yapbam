package net.yapbam.gui.dropbox;

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
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.dropbox.DropboxFileChooser;
import net.astesana.dropbox.FileId;

@SuppressWarnings("serial")
public class YapbamDropboxFileChooser extends DropboxFileChooser implements AbstractURIChooserPanel {
	static final List<String> SCHEMES = Arrays.asList(new String[]{FileId.SCHEME});

	private URI selectedURI;
	private boolean setUp;

	/**
	 * Creates the panel.
	 */
	public YapbamDropboxFileChooser() {
		super();
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
		return save?"Select this tab to save to your Dropbox account":"Select this tab to read data from your Dropbox account";
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(DropboxFileChooser.class.getResource("dropbox.png"));
	}

	@Override
	protected String filter(Entry entry) {
		String fileName = entry.fileName();
		if (fileName.endsWith(".zip")) { //$NON-NLS-1$
			return fileName.substring(0, fileName.length()-".zip".length()); //$NON-NLS-1$
		} else {
			return null;
		}
	}
	
	@Override
	protected DropboxAPI<? extends WebAuthSession> getDropboxAPI() {
		return Dropbox.getAPI();
	}

	@Override
	protected boolean accessGranted(AccessTokenPair pair) {
		if (pair!=null) {
			Dropbox.storeKeys(pair);
		}
		return super.accessGranted(pair);
	}

	@Override
	protected void clearAccess() {
		Dropbox.storeKeys(null);
		this.setUp = false;
	}
	
	@Override
	public URI getSelectedURI() {
		return this.selectedURI;
	}

	@Override
	public void setUp() {
		if (!setUp) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					setUp = refresh();
				}
			});
		}
	}

	@Override
	public Collection<String> getSchemes() {
		return SCHEMES;
	}
	
	@Override
	public void setSelectedURI(URI uri) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean exist(URI selectedURI) {
		return false; //TODO
	}
}
