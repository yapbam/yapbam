package net.yapbam.gui.transfer;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.widget.TextWidget;
import net.astesana.ajlib.swing.widget.date.DateWidget;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AccountWidget;
import net.yapbam.gui.dialogs.ModeWidgetParams;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.awt.Insets;
import net.yapbam.gui.dialogs.ModeWidget;
import javax.swing.JLabel;
import net.yapbam.gui.dialogs.TransactionNumberWidget;
import net.yapbam.gui.widget.AutoSelectFocusListener;

public class FromOrToPane extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String ACCOUNT_PROPERTY = AccountWidget.ACCOUNT_PROPERTY;
	public static final String VALUE_DATE_PROPERTY = "ValueDate"; //$NON-NLS-1$
	
	private AccountWidget accountWidget;
	private GlobalData data;
	private ModeWidget modeWidget;
	private JLabel lblAccount;
	private JLabel lblMode;
	private JLabel lblNumber;
	private TransactionNumberWidget numberField;
	private JLabel lblDateVal;
	private DateWidget valueDateField;
	private JLabel lblStatement;
	private TextWidget statementField;
	private boolean from;
	private Date date;

	/**
	 * Create the panel.
	 */
	public FromOrToPane(GlobalData data, boolean from) {
		this.from = from;
		this.data = data;
		initialize();
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblAccount = new GridBagConstraints();
		gbc_lblAccount.anchor = GridBagConstraints.WEST;
		gbc_lblAccount.insets = new Insets(0, 0, 5, 5);
		gbc_lblAccount.gridx = 0;
		gbc_lblAccount.gridy = 0;
		add(getLblAccount(), gbc_lblAccount);
		GridBagConstraints gbc_accountWidget = new GridBagConstraints();
		gbc_accountWidget.weightx = 1.0;
		gbc_accountWidget.fill = GridBagConstraints.HORIZONTAL;
		gbc_accountWidget.insets = new Insets(0, 0, 5, 0);
		gbc_accountWidget.gridx = 1;
		gbc_accountWidget.gridy = 0;
		add(getAccountWidget(), gbc_accountWidget);
		GridBagConstraints gbc_lblMode = new GridBagConstraints();
		gbc_lblMode.anchor = GridBagConstraints.WEST;
		gbc_lblMode.insets = new Insets(0, 0, 5, 5);
		gbc_lblMode.gridx = 0;
		gbc_lblMode.gridy = 1;
		add(getLblMode(), gbc_lblMode);
		GridBagConstraints gbc_modeWidget = new GridBagConstraints();
		gbc_modeWidget.insets = new Insets(0, 0, 5, 0);
		gbc_modeWidget.fill = GridBagConstraints.HORIZONTAL;
		gbc_modeWidget.gridx = 1;
		gbc_modeWidget.gridy = 1;
		add(getModeWidget(), gbc_modeWidget);
		GridBagConstraints gbc_lblNumber = new GridBagConstraints();
		gbc_lblNumber.anchor = GridBagConstraints.WEST;
		gbc_lblNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumber.gridx = 0;
		gbc_lblNumber.gridy = 2;
		add(getLblNumber(), gbc_lblNumber);
		GridBagConstraints gbc_numberField = new GridBagConstraints();
		gbc_numberField.insets = new Insets(0, 0, 5, 0);
		gbc_numberField.fill = GridBagConstraints.HORIZONTAL;
		gbc_numberField.gridx = 1;
		gbc_numberField.gridy = 2;
		add(getNumberField(), gbc_numberField);
		GridBagConstraints gbc_lblDateVal = new GridBagConstraints();
		gbc_lblDateVal.anchor = GridBagConstraints.WEST;
		gbc_lblDateVal.insets = new Insets(0, 0, 5, 5);
		gbc_lblDateVal.gridx = 0;
		gbc_lblDateVal.gridy = 3;
		add(getLblDateVal(), gbc_lblDateVal);
		GridBagConstraints gbc_valueDateField = new GridBagConstraints();
		gbc_valueDateField.insets = new Insets(0, 0, 5, 0);
		gbc_valueDateField.anchor = GridBagConstraints.WEST;
		gbc_valueDateField.gridx = 1;
		gbc_valueDateField.gridy = 3;
		add(getValueDateField(), gbc_valueDateField);
		GridBagConstraints gbc_lblStatement = new GridBagConstraints();
		gbc_lblStatement.anchor = GridBagConstraints.WEST;
		gbc_lblStatement.insets = new Insets(0, 0, 0, 5);
		gbc_lblStatement.gridx = 0;
		gbc_lblStatement.gridy = 4;
		add(getLblStatement(), gbc_lblStatement);
		GridBagConstraints gbc_statementField = new GridBagConstraints();
		gbc_statementField.anchor = GridBagConstraints.WEST;
		gbc_statementField.gridx = 1;
		gbc_statementField.gridy = 4;
		add(getStatementField(), gbc_statementField);
	}

	private JLabel getLblAccount() {
		if (lblAccount == null) {
			lblAccount = new JLabel(LocalizationData.get("AccountDialog.account")); //$NON-NLS-1$
		}
		return lblAccount;
	}

	public AccountWidget getAccountWidget() {
		if (accountWidget == null) {
			accountWidget = new AccountWidget(data);
			accountWidget.getJLabel().setVisible(false);
			accountWidget.addPropertyChangeListener(AccountWidget.ACCOUNT_PROPERTY, new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					firePropertyChange(ACCOUNT_PROPERTY, evt.getOldValue(), evt.getNewValue());
					getModeWidget().getParameters().setAccount(accountWidget.get());
					getModeWidget().refresh();
					doModeChanges();
				}
			});
		}
		return accountWidget;
	}
	
	private JLabel getLblMode() {
		if (lblMode == null) {
			lblMode = new JLabel(LocalizationData.get("TransactionDialog.mode")); //$NON-NLS-1$
		}
		return lblMode;
	}
	
	public ModeWidget getModeWidget() {
		if (modeWidget == null) {
			modeWidget = new ModeWidget(new ModeWidgetParams(data, getAccountWidget().get(), from));
			modeWidget.getJLabel().setVisible(false);
			modeWidget.addPropertyChangeListener(ModeWidget.MODE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					doModeChanges();
				}

			});
		}
		return modeWidget;
	}

	private JLabel getLblNumber() {
		if (lblNumber == null) {
			lblNumber = new JLabel(LocalizationData.get("TransactionDialog.number")); //$NON-NLS-1$
		}
		return lblNumber;
	}
	public TransactionNumberWidget getNumberField() {
		if (numberField == null) {
			numberField = new TransactionNumberWidget();
		}
		return numberField;
	}
	private JLabel getLblDateVal() {
		if (lblDateVal == null) {
			lblDateVal = new JLabel(LocalizationData.get("TransactionDialog.valueDate")); //$NON-NLS-1$
		}
		return lblDateVal;
	}
	public DateWidget getValueDateField() {
		if (valueDateField == null) {
			valueDateField = new DateWidget();
			valueDateField.getDateField().addFocusListener(AutoSelectFocusListener.INSTANCE);
			valueDateField.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					firePropertyChange(VALUE_DATE_PROPERTY, evt.getOldValue(), evt.getNewValue());
				}
			});
			valueDateField.setToolTipText("blabla todo");
		}
		return valueDateField;
	}
	private JLabel getLblStatement() {
		if (lblStatement == null) {
			lblStatement = new JLabel(LocalizationData.get("TransactionDialog.statement")); //$NON-NLS-1$
		}
		return lblStatement;
	}
	public TextWidget getStatementField() {
		if (statementField == null) {
			statementField = new TextWidget();
			statementField.setColumns(10);
			statementField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		}
		return statementField;
	}

	private void doModeChanges() {
		getNumberField().set(data, getAccountWidget().get(), getModeWidget().get(), from);
		setValueDate();
	}
	
	public void setDate(Date date) {
		this.date = date;
		setValueDate();
	}

	private void setValueDate() {
		Mode mode = getModeWidget().get();
		DateStepper vdc = from ? mode.getExpenseVdc() : mode.getReceiptVdc();
		if ((vdc!=null) && (date!=null)) getValueDateField().setDate(vdc.getNextStep(date));
	}
}
