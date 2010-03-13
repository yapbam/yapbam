package net.yapbam.gui.dialogs.checkbook;

import java.awt.GridBagLayout;
import javax.swing.JPanel;

public class CheckbookPane extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public CheckbookPane() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
	}

	public String getOkDisableCause() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getCheckbook() {
		// TODO Auto-generated method stub
		return null;
	}

}
