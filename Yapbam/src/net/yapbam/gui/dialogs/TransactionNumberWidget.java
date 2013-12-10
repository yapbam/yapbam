package net.yapbam.gui.dialogs;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TransactionNumberWidget extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextField transactionNumber;
	private CheckNumberWidget checkNumber;
	private boolean checkNumberIsVisible;
	
	/**
	 * Create the panel.
	 */
	public TransactionNumberWidget() {
		CardLayout layout = new CardLayout(0, 0);
		setLayout(layout);
		
		add(getCheckNumberWidget(), "1"); //$NON-NLS-1$
		add(getTransactionNumberField(), "2"); //$NON-NLS-1$
		layout.last(this);
		this.checkNumberIsVisible = false;
	}

	private JTextField getTransactionNumberField() {
		if (transactionNumber==null) {
			transactionNumber = new JTextField(15);
			transactionNumber.setToolTipText(LocalizationData.get("TransactionDialog.number.tooltip")); //$NON-NLS-1$
			transactionNumber.addFocusListener(AutoSelectFocusListener.INSTANCE);
		}
		return transactionNumber;
	}

	private CheckNumberWidget getCheckNumberWidget() {
		if (checkNumber==null) {
			checkNumber = new CheckNumberWidget();
			checkNumber.addPropertyChangeListener(CheckNumberWidget.NUMBER_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					transactionNumber.setText(checkNumber.getNumber());
				}
			});
		}
		return checkNumber;
	}
	
	public void set(GlobalData data, Account account, Mode mode, boolean expense) {
		boolean checkNumberRequired = (expense) && (mode.isUseCheckBook());
		if (checkNumberRequired != checkNumberIsVisible) {
			if (!checkNumberRequired) {
				transactionNumber.setText(""); //$NON-NLS-1$
			} else {
				checkNumber.setAccount(data, account);
			}
			// If we need to switch from text field to check numbers popup
			CardLayout layout = (CardLayout) getLayout();
			if (checkNumberRequired) {
				layout.first(this);
			} else {
				layout.last(this);
			}
			checkNumberIsVisible = !checkNumberIsVisible;
		} else if (checkNumberRequired && !NullUtils.areEquals(checkNumber.getAccount(), account)) {
			checkNumber.setAccount(data, account);
		}
	}
	
	public String getNumber() {
		return checkNumberIsVisible?getCheckNumberWidget().getNumber():getTransactionNumberField().getText();
	}
}
