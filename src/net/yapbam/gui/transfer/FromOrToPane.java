package net.yapbam.gui.transfer;

import javax.swing.JPanel;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.dialogs.AccountWidget;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FromOrToPane extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String ACCOUNT_PROPERTY = AccountWidget.ACCOUNT_PROPERTY;
	
	private AccountWidget accountWidget;
	private GlobalData data;

	/**
	 * Create the panel.
	 */
	public FromOrToPane(GlobalData data) {
		this.data = data;
		initialize();
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_accountWidget = new GridBagConstraints();
		gbc_accountWidget.gridx = 0;
		gbc_accountWidget.gridy = 0;
		add(getAccountWidget(), gbc_accountWidget);
	}

	private AccountWidget getAccountWidget() {
		if (accountWidget == null) {
			accountWidget = new AccountWidget(data);
			accountWidget.addPropertyChangeListener(AccountWidget.ACCOUNT_PROPERTY, new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					firePropertyChange(ACCOUNT_PROPERTY, evt.getOldValue(), evt.getNewValue());
				}
			});
		}
		return accountWidget;
	}
	
	public Account getAccount() {
		return accountWidget.get();
	}
}
