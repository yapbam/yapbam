package net.yapbam.ihm;

import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JPanel;

public abstract class AbstractPlugIn {
	
	public AbstractPlugIn() { //What about the arguments (GlobalData, ...)
		super();
	}
	
	public JMenu[] getPlugInMenu() {
        return null;
	}

	public void restoreState(Properties properties) {
	}

	public void saveState(Properties properties) {
	}

	public String getPanelTitle() {
		return null;
	}

	public JPanel getPanel() {
		return null;
	}

	public void setDisplayed(boolean displayed) {
	}
	
	public Object getRestartData() {
		return null;
	}
}
