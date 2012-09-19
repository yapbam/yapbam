package net.yapbam.data.persistence;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FilePersistencePlugin extends PersistencePlugin {
	private AbstractURIChooserPanel panel;

	@Override
	public String getName() {
		return "Computer";
	}

	@Override
	public String getScheme() {
		return "file";
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
	public AbstractURIChooserPanel getChooser() {
		if (panel==null) {
			panel = new FileChooserPanel();
		}
		return panel;
	}

	@Override
	public String getTooltip() {
		return "Select this tab to save/read data to/from a local storage";
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(getClass().getResource("computer.png"));
	}
}
