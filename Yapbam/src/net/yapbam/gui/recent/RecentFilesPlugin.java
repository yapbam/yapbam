package net.yapbam.gui.recent;

import java.net.URI;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.URIChangedEvent;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.persistence.classpath.ClasspathPersistenceAdapter;

/** This plugin remembers the latest opened files and displays them in a menu Item.
 */
public class RecentFilesPlugin extends AbstractPlugIn {
	private URI lastURI;
	private GlobalData data;
	private ArrayList<URI> latest;
	private JMenuItem menu;
	
	public RecentFilesPlugin(FilteredData data, Object restart) {
		this.lastURI = null;
		this.data = data.getGlobalData();
		this.latest = new ArrayList<URI>();
		menu = new JMenu(LocalizationData.get("RecentFiles.title")); //$NON-NLS-1$
		menu.setToolTipText(LocalizationData.get("RecentFiles.tooltip")); //$NON-NLS-1$
		menu.setEnabled(false);
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (event instanceof URIChangedEvent || event instanceof EverythingChangedEvent) {
					updateMenu();
				}
			}
		});
	}

	private void updateMenu() {
		URI currentUri = data.getURI();
		if ((currentUri!=null) && !currentUri.equals(lastURI) && isValid(currentUri)) {
			// Put the uri at the top of the list
			latest.remove(currentUri);
			latest.add(0, currentUri);
			// Limit the list size
			while (latest.size()>getSizeLimit()+1) {
				latest.remove(latest.size()-1);
			}
		}
		// Update the menu
		menu.removeAll();
		boolean empty = true;
		for (URI uri : latest) {
			if (!uri.equals(currentUri)) {
				menu.add(new JMenuItem(new RecentFileAction(this, uri, data)));
				empty = false;
			}
		}
		menu.setEnabled(!empty);
	}

	private boolean isValid(URI uri) {
		return !uri.getScheme().equals(ClasspathPersistenceAdapter.SCHEME);
	}

	private int getSizeLimit() {
		return 5;
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.AbstractPlugIn#getMenuItem(int)
	 */
	@Override
	public JMenuItem[] getMenuItem(int part) {
		if (part==AbstractPlugIn.FILE_MANIPULATION_PART) {
			return new JMenuItem[]{menu};
		} else {
			return super.getMenuItem(part);
		}
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.AbstractPlugIn#saveState()
	 */
	@Override
	public void saveState() {
		YapbamState.INSTANCE.save(getStateKey(), latest);
	}

	private String getStateKey() {
		return getClass().getCanonicalName()+".data"; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see net.yapbam.gui.AbstractPlugIn#restoreState()
	 */
	@Override
	public void restoreState() {
		ArrayList<URI> obj = (ArrayList<URI>) YapbamState.INSTANCE.restore(getStateKey());
		if (obj!=null) {
			latest = obj;
			updateMenu();
		}
	}

	public void remove(URI uri) {
		latest.remove(uri);
		updateMenu();
	}
}
