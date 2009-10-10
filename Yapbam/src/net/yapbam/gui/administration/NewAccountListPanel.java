package net.yapbam.gui.administration;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridBagConstraints;

public class NewAccountListPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private AccountListPanel accountListPanel = null;
	/**
	 * This is the default constructor
	 */
	public NewAccountListPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getAccountListPanel(), gridBagConstraints);
	}

	/**
	 * This method initializes accountListPanel	
	 * 	
	 * @return net.yapbam.gui.administration.AccountListPanel	
	 */
	private AccountListPanel getAccountListPanel() {
		if (accountListPanel == null) {
			accountListPanel = new AccountListPanel();
		}
		return accountListPanel;
	}

}
