package net.yapbam.gui;

import net.yapbam.gui.actions.TransactionSelector;

/** A plugin execution context.
 * @author Jean-Marc Astesana
 */
public class Context {
	private MainFrame frame;

	Context (MainFrame frame) {
		this.frame = frame;
	}
	
	public TransactionSelector getCurrentTransactionSelector() {
		return this.frame.mainMenu.getTransactionSelector();
	}
	
	public MainFrame getMainFrame() {
		return this.frame;
	}
}
