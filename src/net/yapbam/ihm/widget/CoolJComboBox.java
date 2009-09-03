package net.yapbam.ihm.widget;

import javax.swing.JComboBox;

/** This subclass of JCombo is cool because you can prevent instance from sending action event.
 * I have made this class because I didn't found a way to refresh a JComboBox content list without
 * having some action events send. 
 * @author Jean-Marc
 */
public class CoolJComboBox extends JComboBox {
	private static final long serialVersionUID = 1L;

	private boolean isActionEnabled = true;
	
	public void setActionEnabled(boolean isActionEnabled) {
		this.isActionEnabled = isActionEnabled;
	}

	@Override
	protected void fireActionEvent() {
		if (isActionEnabled) super.fireActionEvent();
	}
}
