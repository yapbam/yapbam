package net.yapbam.gui.dialogs;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.astesana.ajlib.swing.widget.TextWidget;
import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.data.Account;
import net.yapbam.data.AlertThreshold;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import net.yapbam.gui.widget.CurrencyWidget;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class EditAccountPanel extends JPanel {
	public static final String OK_DISABLED_CAUSE_PROPERTY = "okDisabled"; //$NON-NLS-1$
	
	private JLabel lblName;
	private TextWidget nameField;
	private JLabel lblInitialBalance;
	private CurrencyWidget initBalanceField;
	private JLabel lblLowThreshold;
	private CurrencyWidget lowThresholdField;
	private JLabel lblHighThreshold;
	private CurrencyWidget hightThresholdField;
	
	private GlobalData data;
	private String okDisabledCause;
	private int accountIndex;
	private JLabel message;

	private PropertyChangeListener listener;
	private JScrollPane notePane;
	private JTextArea noteField;
	private JLabel lblNotes;

	/**
	 * Create the panel.
	 */
	public EditAccountPanel(GlobalData data) {
		this.data = data;
		this.accountIndex = -1;
		this.listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkDisabledCause();
			}
		};
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_message = new GridBagConstraints();
		gbc_message.insets = new Insets(0, 0, 15, 0);
		gbc_message.anchor = GridBagConstraints.WEST;
		gbc_message.gridwidth = 0;
		gbc_message.gridx = 0;
		gbc_message.gridy = 0;
		add(getMessage(), gbc_message);
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 1;
		add(getLblName(), gbc_lblName);
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.insets = new Insets(0, 0, 5, 0);
		gbc_nameField.weightx = 1.0;
		gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameField.gridx = 1;
		gbc_nameField.gridy = 1;
		add(getNameField(), gbc_nameField);
		GridBagConstraints gbc_lblInitialBalance = new GridBagConstraints();
		gbc_lblInitialBalance.anchor = GridBagConstraints.WEST;
		gbc_lblInitialBalance.insets = new Insets(0, 0, 5, 5);
		gbc_lblInitialBalance.gridx = 0;
		gbc_lblInitialBalance.gridy = 2;
		add(getLblInitialBalance(), gbc_lblInitialBalance);
		GridBagConstraints gbc_initBalanceField = new GridBagConstraints();
		gbc_initBalanceField.insets = new Insets(0, 0, 5, 0);
		gbc_initBalanceField.fill = GridBagConstraints.HORIZONTAL;
		gbc_initBalanceField.gridx = 1;
		gbc_initBalanceField.gridy = 2;
		add(getInitBalanceField(), gbc_initBalanceField);
		GridBagConstraints gbc_lblLowThreshold = new GridBagConstraints();
		gbc_lblLowThreshold.anchor = GridBagConstraints.WEST;
		gbc_lblLowThreshold.insets = new Insets(0, 0, 5, 5);
		gbc_lblLowThreshold.gridx = 0;
		gbc_lblLowThreshold.gridy = 3;
		add(getLblLowThreshold(), gbc_lblLowThreshold);
		GridBagConstraints gbc_lowThresholdField = new GridBagConstraints();
		gbc_lowThresholdField.insets = new Insets(0, 0, 5, 0);
		gbc_lowThresholdField.fill = GridBagConstraints.HORIZONTAL;
		gbc_lowThresholdField.gridx = 1;
		gbc_lowThresholdField.gridy = 3;
		add(getLowThresholdField(), gbc_lowThresholdField);
		GridBagConstraints gbc_lblHighThreshold = new GridBagConstraints();
		gbc_lblHighThreshold.anchor = GridBagConstraints.WEST;
		gbc_lblHighThreshold.insets = new Insets(0, 0, 5, 5);
		gbc_lblHighThreshold.gridx = 0;
		gbc_lblHighThreshold.gridy = 4;
		add(getLblHighThreshold(), gbc_lblHighThreshold);
		GridBagConstraints gbc_hightThresholdField = new GridBagConstraints();
		gbc_hightThresholdField.insets = new Insets(0, 0, 5, 0);
		gbc_hightThresholdField.fill = GridBagConstraints.HORIZONTAL;
		gbc_hightThresholdField.gridx = 1;
		gbc_hightThresholdField.gridy = 4;
		add(getHightThresholdField(), gbc_hightThresholdField);
		GridBagConstraints gbc_lblNotes = new GridBagConstraints();
		gbc_lblNotes.gridwidth = 0;
		gbc_lblNotes.anchor = GridBagConstraints.WEST;
		gbc_lblNotes.gridx = 0;
		gbc_lblNotes.gridy = 5;
		add(getLblNotes(), gbc_lblNotes);
		GridBagConstraints gbc_notePane = new GridBagConstraints();
		gbc_notePane.gridwidth = 0;
		gbc_notePane.fill = GridBagConstraints.BOTH;
		gbc_notePane.gridx = 0;
		gbc_notePane.gridy = 6;
		add(getNotePane(), gbc_notePane);
	}
	
	void setAccount(int accountIndex) {
		this.accountIndex = accountIndex;
		Account account = data.getAccount(accountIndex);
		getNameField().setText(account.getName());
		getInitBalanceField().setValue(account.getInitialBalance());
		getLblLowThreshold().setVisible(true);
		double low = account.getAlertThreshold().getLessThreshold();
		getLowThresholdField().setValue(low==AlertThreshold.NO.getLessThreshold()?null:low);
		getLowThresholdField().setVisible(true);
		getLblHighThreshold().setVisible(true);
		double high = account.getAlertThreshold().getMoreThreshold();
		getHightThresholdField().setValue(high==AlertThreshold.NO.getMoreThreshold()?null:high);
		getHightThresholdField().setVisible(true);
		getLblNotes().setVisible(true);
		getNotePane().setVisible(true);
		getNoteField().setText(account.getComment()==null?"":account.getComment());
		updateOkDisabledCause();
	}
	
	void setMessage(String message) {
		this.getMessage().setText(message);
		this.getMessage().setVisible(true);
	}

	private JLabel getLblName() {
		if (lblName == null) {
			lblName = new JLabel(LocalizationData.get("AccountDialog.account")); //$NON-NLS-1$
		}
		return lblName;
	}
	private TextWidget getNameField() {
		if (nameField == null) {
			nameField = new TextWidget();
			nameField.setToolTipText(LocalizationData.get("AccountDialog.account.tooltip")); //$NON-NLS-1$
			nameField.setColumns(20);
			nameField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, listener);
		}
		return nameField;
	}
	private JLabel getLblInitialBalance() {
		if (lblInitialBalance == null) {
			lblInitialBalance = new JLabel(LocalizationData.get("AccountDialog.balance")); //$NON-NLS-1$
		}
		return lblInitialBalance;
	}
	private CurrencyWidget getInitBalanceField() {
		if (initBalanceField == null) {
			initBalanceField = new CurrencyWidget();
			initBalanceField.addFocusListener(AutoSelectFocusListener.INSTANCE);
			initBalanceField.setToolTipText(LocalizationData.get("AccountDialog.balance.tooltip")); //$NON-NLS-1$
			initBalanceField.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, listener);
			initBalanceField.setValue(new Double(0));
		}
		return initBalanceField;
	}
	private JLabel getLblLowThreshold() {
		if (lblLowThreshold == null) {
			lblLowThreshold = new JLabel(LocalizationData.get("AccountDialog.alertThresholdLess")); //$NON-NLS-1$
			lblLowThreshold.setVisible(false);
		}
		return lblLowThreshold;
	}
	private CurrencyWidget getLowThresholdField() {
		if (lowThresholdField == null) {
			lowThresholdField = new CurrencyWidget();
			lowThresholdField.addFocusListener(AutoSelectFocusListener.INSTANCE);
			lowThresholdField.setEmptyAllowed(true);
			lowThresholdField.setToolTipText(LocalizationData.get("AccountDialog.alertThresholdLess.tooltip")); //$NON-NLS-1$
			lowThresholdField.addPropertyChangeListener(CurrencyWidget.CONTENT_VALID_PROPERTY, listener);
			lowThresholdField.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, listener);
			lowThresholdField.setVisible(false);
		}
		return lowThresholdField;
	}
	private JLabel getLblHighThreshold() {
		if (lblHighThreshold == null) {
			lblHighThreshold = new JLabel(LocalizationData.get("AccountDialog.alertThresholdMore")); //$NON-NLS-1$
			lblHighThreshold.setVisible(false);
		}
		return lblHighThreshold;
	}
	private CurrencyWidget getHightThresholdField() {
		if (hightThresholdField == null) {
			hightThresholdField = new CurrencyWidget();
			hightThresholdField.addFocusListener(AutoSelectFocusListener.INSTANCE);
			hightThresholdField.setEmptyAllowed(true);
			hightThresholdField.setToolTipText(LocalizationData.get("AccountDialog.alertThresholdMore.tooltip")); //$NON-NLS-1$
			hightThresholdField.addPropertyChangeListener(CurrencyWidget.CONTENT_VALID_PROPERTY, listener);
			hightThresholdField.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, listener);
			hightThresholdField.setVisible(false);
		}
		return hightThresholdField;
	}
	
	private void updateOkDisabledCause() {
		String old = this.okDisabledCause;
		String name = getAccountName();
		if (name.length()==0) {
			this.okDisabledCause = LocalizationData.get("AccountDialog.err1"); //$NON-NLS-1$
		} else if (!isNameOk(name)) {
			this.okDisabledCause = LocalizationData.get("AccountDialog.err2"); //$NON-NLS-1$
		} else if (getInitBalanceField().getValue()==null) {
			this.okDisabledCause = LocalizationData.get("AccountDialog.err3"); //$NON-NLS-1$
		} else if (!getLowThresholdField().isContentValid()) {
			this.okDisabledCause = LocalizationData.get("AccountDialog.err4"); //$NON-NLS-1$
		} else if (!getHightThresholdField().isContentValid()) {
			this.okDisabledCause = LocalizationData.get("AccountDialog.err5"); //$NON-NLS-1$
		} else {
			AlertThreshold alerts = getAlerts();
			if (alerts.getLessThreshold()>=alerts.getMoreThreshold()) {
				this.okDisabledCause = LocalizationData.get("AccountDialog.err6"); //$NON-NLS-1$
			} else {
				this.okDisabledCause = null;
			}
		}

		if (!NullUtils.areEquals(old, this.okDisabledCause)) {
			firePropertyChange(OK_DISABLED_CAUSE_PROPERTY, old, this.okDisabledCause);
		}
	}
	
	private String getAccountName() {
		return this.getNameField().getText().trim();
	}
	
	private AlertThreshold getAlerts() {
		if (getLowThresholdField().isContentValid() && getHightThresholdField().isContentValid()) {
			Double low = getLowThresholdField().getValue();
			if (low==null) low = AlertThreshold.NO.getLessThreshold();
			Double high = getHightThresholdField().getValue();
			if (high==null) high = AlertThreshold.NO.getMoreThreshold();
			AlertThreshold result = new AlertThreshold(low, high);
			if (result.equals(AlertThreshold.NO)) return AlertThreshold.NO;
			if (result.equals(AlertThreshold.DEFAULT)) return AlertThreshold.DEFAULT;
			return result;
		} else {
			return null;
		}
	}

	String getOkDisabledCause() {
		return this.okDisabledCause;
	}
	private JLabel getMessage() {
		if (message == null) {
			message = new JLabel();
			message.setVisible(false);
		}
		return message;
	}
	
	Account getAccount() {
		if (this.okDisabledCause!=null) return null;
		return new Account(this.getNameField().getText().trim(), getInitBalanceField().getValue(), getAlerts(), getNoteField().getText());
	}
	
	private boolean isNameOk(String name) {
		// Unfortunately, before Yapbam version 0.9.8, it was possible to define account names starting or ending with a space
		// We chose not to trim the old account names because it was very hard to merge accounts with the same name except the spaces
		// (because modes could not be the same).
		// So, we have to test if the name is equivalent to a trimmed previously entered name
		for (int i = 0; i < data.getAccountsNumber(); i++) {
			if ((i!=accountIndex) && name.equalsIgnoreCase(data.getAccount(i).getName().trim())) return false;
		}
		return true;
	}
	private JScrollPane getNotePane() {
		if (notePane == null) {
			notePane = new JScrollPane(getNoteField());
			notePane.setPreferredSize(new Dimension(100, 200));
			notePane.setVisible(false);
		}
		return notePane;
	}
	private JTextArea getNoteField() {
		if (noteField == null) {
			noteField = new JTextArea();
			noteField.setToolTipText(LocalizationData.get("AccountDialog.notes.tooltip")); //$NON-NLS-1$
			noteField.setLineWrap(true);
			noteField.setWrapStyleWord(true);
		}
		return noteField;
	}
	private JLabel getLblNotes() {
		if (lblNotes == null) {
			lblNotes = new JLabel(LocalizationData.get("AccountDialog.notes")); //$NON-NLS-1$
			lblNotes.setVisible(false);
		}
		return lblNotes;
	}
}
