package net.yapbam.gui.dialogs;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import net.yapbam.data.Account;
import net.yapbam.data.Checkbook;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.checkbook.CheckbookDialog;

import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.utilities.NullUtils;

public class CheckNumberWidget extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String NUMBER_PROPERTY = "Number"; //$NON-NLS-1$

	private JComboBox numbers = null;
	private JButton newButton = null;
	private Account account; // @jve:decl-index=0:
	private GlobalData data;

	private String number = null;

	/**
	 * This is the default constructor
	 */
	public CheckNumberWidget() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		this.add(getNumbers(), gridBagConstraints);
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		this.add(getNewButton(), gridBagConstraints1);
		Dimension dimension = getNumbers().getPreferredSize();
		getNewButton().setPreferredSize(new Dimension(dimension.height, dimension.height));
		// Setting the editable property before would lead to wrong dimension
		// (at most with Nimbus LAF)
		getNumbers().setEditable(true);
	}

	/**
	 * This method initializes numbers
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getNumbers() {
		if (numbers == null) {
			numbers = new JComboBox(new String[]{});
			numbers.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						String old = number;
						number = (String) numbers.getSelectedItem();
						if (!NullUtils.areEquals(old, number)) {
							firePropertyChange(NUMBER_PROPERTY, old, number);
						}
					}
				}
			});
		}
		return numbers;
	}

	/**
	 * This method initializes newButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getNewButton() {
		if (newButton == null) {
			newButton = new JButton();
			newButton.setEnabled(false);
			newButton.setIcon(IconManager.get(Name.NEW));
			newButton.setFocusable(false);
			newButton.setToolTipText(LocalizationData.get("TransactionDialog.checkbook.new.tooltip")); //$NON-NLS-1$
			newButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Checkbook book = CheckbookDialog.open(data, account, Utils.getOwnerWindow(newButton));
					if (book != null) {
						numbers.addItem(book.getFullNumber(book.getNext()));
						numbers.setSelectedIndex(numbers.getItemCount() - 1);
					}
				}
			});
		}
		return newButton;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(GlobalData data, Account account) {
		this.data = data;
		this.account = account;
		this.getNewButton().setEnabled((data != null) && (account != null));
		numbers.removeAllItems();
		for (int i = 0; i < account.getCheckbooksNumber(); i++) {
			Checkbook checkbook = account.getCheckbook(i);
			if (!checkbook.isEmpty()) {
				numbers.addItem(checkbook.getFullNumber(checkbook.getNext()));
			}
		}
	}

	public void setText(String text) {
		numbers.setSelectedItem((text==null) ?"":text); //$NON-NLS-1$
	}

	public String getNumber() {
		return this.number;
	}
}
