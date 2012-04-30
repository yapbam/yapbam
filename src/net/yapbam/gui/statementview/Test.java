package net.yapbam.gui.statementview;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.framework.Application;

public class Test extends Application {

	@Override
	protected JPanel buildMainPanel() {
		return new CheckModePanel();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Test().launch();
	}

}
