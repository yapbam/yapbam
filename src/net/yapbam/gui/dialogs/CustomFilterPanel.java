package net.yapbam.gui.dialogs;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JList;

import net.yapbam.data.GlobalData;

import java.awt.GridBagConstraints;
import javax.swing.AbstractListModel;
import java.lang.Object;

public class CustomFilterPanel extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private JPanel accountPanel = null;
	private JList accountList = null;
	private GlobalData data;

	/**
	 * This is the default constructor
	 */
	public CustomFilterPanel() {
		this(new GlobalData());
	}
	
	public CustomFilterPanel(GlobalData data) {
		super();
		this.data = data;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);

		this.addTab("Comptes", getAccountPanel());
	}

	/**
	 * This method initializes accountPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAccountPanel() {
		if (accountPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			accountPanel = new JPanel();
			accountPanel.setLayout(new GridBagLayout());
			accountPanel.add(getAccountList(), gridBagConstraints);
		}
		return accountPanel;
	}

	/**
	 * This method initializes accountList	
	 * @return javax.swing.JList	
	 */
	@SuppressWarnings("serial")
	private JList getAccountList() {
		if (accountList == null) {
			accountList = new JList();
			accountList.setModel(new AbstractListModel(){
				public Object getElementAt(int index) {
					return data.getAccount(index).getName();
				}
				public int getSize() {
					return data.getAccountsNumber();
				}
			});
		}
		return accountList;
	}

}
