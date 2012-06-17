package net.yapbam.gui;

import java.awt.Container;
import java.awt.Window;

import net.astesana.ajlib.swing.framework.Application;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.actions.TransactionSelector;

public class Yapbam extends Application implements YapbamInstance {
	private GlobalData data;
	private FilteredData filteredData;
	private AbstractPlugIn[] plugins;
	private Object[] restartData;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Remove obsolete files from previous installations
		FolderCleaner.clean();
		new Yapbam(null, null).launch();
	}

	private Yapbam(FilteredData filteredData, Object[] restartData) {
		if (filteredData == null) {
			// Create the data structures if they are not provided as argument
			this.data = new GlobalData();
			this.filteredData = new FilteredData(this.data);
		} else {
			this.data = filteredData.getGlobalData();
			this.filteredData = filteredData;
		}
		this.restartData = restartData;
	}

	
	@Override
	protected Container buildMainPanel() {
		PlugInContainer[] pluginContainers = Preferences.getPlugins();
		if (restartData == null) restartData = new Object[pluginContainers.length];
		this.plugins = new AbstractPlugIn[pluginContainers.length];
		for (int i = 0; i < plugins.length; i++) {
			if (pluginContainers[i].isActivated()) {
				this.plugins[i] = (AbstractPlugIn) pluginContainers[i].build(this.filteredData, restartData[i]);
				this.plugins[i].setContext(this);
			}
			if (pluginContainers[i].getInstanciationException()!=null) { // An error occurs during plugin instantiation
				ErrorManager.INSTANCE.display(null, pluginContainers[i].getInstanciationException(), "Une erreur est survenue durant l'instanciation du plugin "+"?"); //LOCAL //TODO
				ErrorManager.INSTANCE.log(null, pluginContainers[i].getInstanciationException());
			}
		}
		return new MainPanel(plugins);
	}

	/* (non-Javadoc)
	 * @see net.astesana.ajlib.swing.framework.Application#getDefaultLookAndFeelName()
	 */
	@Override
	protected String getDefaultLookAndFeelName() {
		return Preferences.INSTANCE.getLookAndFeel();
	}

	@Override
	public TransactionSelector getCurrentTransactionSelector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Window getApplicationWindow() {
		return super.getJFrame();
	}
}
