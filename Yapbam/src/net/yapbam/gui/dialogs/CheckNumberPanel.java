package net.yapbam.gui.dialogs;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import net.yapbam.data.Account;
import net.yapbam.data.Checkbook;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.checkbook.CheckbookDialog;

import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class CheckNumberPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox numbers = null;
	private JButton newButton = null;
	private Account account;  //  @jve:decl-index=0:
	private GlobalData data;
	
	/**
	 * This is the default constructor
	 */
	public CheckNumberPanel() {
		super();
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getNumbers(), gridBagConstraints);
		this.add(getNewButton(), gridBagConstraints1);
	}

	/**
	 * This method initializes numbers	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getNumbers() {
		if (numbers == null) {
			numbers = new JComboBox();
			numbers.setEditable(true);
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
	        Dimension dimension = getNumbers().getPreferredSize();
	        newButton.setPreferredSize(new Dimension(dimension.height, dimension.height));
	        newButton.setEnabled(false);
			newButton.setIcon(IconManager.NEW_CATEGORY);
	        newButton.setFocusable(false);
			newButton.setToolTipText(LocalizationData.get("TransactionDialog.checkbook.new.tooltip")); //$NON-NLS-1$
			newButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Checkbook book = CheckbookDialog.open(data, account, AbstractDialog.getOwnerWindow(newButton));
					if (book!=null) {
						numbers.addItem(book.getNextCheckNumber());
						numbers.setSelectedIndex(numbers.getItemCount()-1);
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
		this.getNewButton().setEnabled((data!=null)&&(account!=null));
		numbers.removeAllItems();
		for (int i = 0; i < account.getCheckbooksNumber(); i++) {
			numbers.addItem(account.getCheckbook(i).getNextCheckNumber());
		}
	}
}
