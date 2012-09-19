package net.astesana.dropbox;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.yapbam.data.persistence.AbstractURIChooserPanel;
import net.yapbam.data.persistence.PersistencePlugin;
import net.yapbam.gui.dropbox.YapbamDropboxFileChooser;

public class DropboxPersistencePlugin extends PersistencePlugin {

	@Override
	public String getName() {
		return "Dropbox";
	}

	@Override
	public String getTooltip() {
		return "Select this tab to save/read data to/from Dropbox";
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(getClass().getResource("dropbox.png"));
	}
	
	@Override
	public AbstractURIChooserPanel buildChooser() {
		return new YapbamDropboxFileChooser();
	}
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}
}
