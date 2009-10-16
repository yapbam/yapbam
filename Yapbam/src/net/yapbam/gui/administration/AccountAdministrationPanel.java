package net.yapbam.gui.administration;

import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;

import java.awt.GridBagConstraints;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.ModeListPanel;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Font;
import java.awt.Color;

public class AccountAdministrationPanel extends JPanel implements AbstractAdministrationPanel {
	private static final long serialVersionUID = 1L;
	private AccountListPanel accountListPanel = null;
	private ModeListPanel modeListPanel = null;
	private GlobalData data;
	
	public AccountAdministrationPanel(GlobalData data) {
		super();
		this.data = data;
		initialize();
		final JTable jTable = this.getAccountListPanel().getJTable();
		jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int row = jTable.getSelectedRow();
					if (row>=0) {
						getModeListPanel().setContent(AccountAdministrationPanel.this.data.getAccount(row));
					}
				}
			}
		});
	}
	
	/**
	 * This is the default constructor
	 */
	public AccountAdministrationPanel() {
		this(new GlobalData());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.weightx = 0.0D;
		gridBagConstraints1.weighty = 0.75D;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.weighty = 1.0D;
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1.0D;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints2.weightx = 1.0D;
		gridBagConstraints.gridy = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getAccountListPanel(), gridBagConstraints);
		this.add(getModeListPanel(), gridBagConstraints1);
	}

	/**
	 * This method initializes accountListPanel	
	 * 	
	 * @return net.yapbam.gui.administration.AccountListPanel	
	 */
	private AccountListPanel getAccountListPanel() {
		if (accountListPanel == null) {
			accountListPanel = new AccountListPanel(data);
		}
		return accountListPanel;
	}

	/**
	 * This method initializes modeListPanel	
	 * 	
	 * @return net.yapbam.gui.dialogs.ModeListPanel	
	 */
	private ModeListPanel getModeListPanel() {
		if (modeListPanel == null) {
			modeListPanel = new ModeListPanel();
			modeListPanel.setBorder(BorderFactory.createTitledBorder(null, "Modes de paiement", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
		}
		return modeListPanel;
	}

	@Override
	public JComponent getPanel() {
		return this;
	}

	@Override
	public void restoreState() {
	}

	@Override
	public void saveState() {
	}
	
	@Override
	public String getPanelTitle() {
		return LocalizationData.get("AccountManager.title"); //$NON-NLS-1$
	}

	public String getPanelToolTip() {
		return LocalizationData.get("AccountManager.toolTip"); //$NON-NLS-1$
	}
}
