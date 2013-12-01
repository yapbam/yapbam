package net.yapbam.gui.transfer;

import javax.swing.JPanel;

import net.yapbam.data.Account;
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

import com.fathzer.soft.ajlib.swing.widget.TextWidget;
import com.fathzer.soft.ajlib.swing.widget.date.DateWidget;

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
		GridBagConstraints gbcLblAccount = new GridBagConstraints();
		gbcLblAccount.anchor = GridBagConstraints.WEST;
		gbcLblAccount.insets = new Insets(0, 0, 5, 5);
		gbcLblAccount.gridx = 0;
		gbcLblAccount.gridy = 0;
		add(getLblAccount(), gbcLblAccount);
		GridBagConstraints gbcAccountWidget = new GridBagConstraints();
		gbcAccountWidget.weightx = 1.0;
		gbcAccountWidget.fill = GridBagConstraints.HORIZONTAL;
		gbcAccountWidget.insets = new Insets(0, 0, 5, 0);
		gbcAccountWidget.gridx = 1;
		gbcAccountWidget.gridy = 0;
		add(getAccountWidget(), gbcAccountWidget);
		GridBagConstraints gbcLblMode = new GridBagConstraints();
		gbcLblMode.anchor = GridBagConstraints.WEST;
		gbcLblMode.insets = new Insets(0, 0, 5, 5);
		gbcLblMode.gridx = 0;
		gbcLblMode.gridy = 1;
		add(getLblMode(), gbcLblMode);
		GridBagConstraints gbcModeWidget = new GridBagConstraints();
		gbcModeWidget.insets = new Insets(0, 0, 5, 0);
		gbcModeWidget.fill = GridBagConstraints.HORIZONTAL;
		gbcModeWidget.gridx = 1;
		gbcModeWidget.gridy = 1;
		add(getModeWidget(), gbcModeWidget);
		GridBagConstraints gbcLblNumber = new GridBagConstraints();
		gbcLblNumber.anchor = GridBagConstraints.WEST;
		gbcLblNumber.insets = new Insets(0, 0, 5, 5);
		gbcLblNumber.gridx = 0;
		gbcLblNumber.gridy = 2;
		add(getLblNumber(), gbcLblNumber);
		GridBagConstraints gbcNumberField = new GridBagConstraints();
		gbcNumberField.insets = new Insets(0, 0, 5, 0);
		gbcNumberField.fill = GridBagConstraints.HORIZONTAL;
		gbcNumberField.gridx = 1;
		gbcNumberField.gridy = 2;
		add(getNumberField(), gbcNumberField);
		GridBagConstraints gbcLblDateVal = new GridBagConstraints();
		gbcLblDateVal.anchor = GridBagConstraints.WEST;
		gbcLblDateVal.insets = new Insets(0, 0, 5, 5);
		gbcLblDateVal.gridx = 0;
		gbcLblDateVal.gridy = 3;
		add(getLblDateVal(), gbcLblDateVal);
		GridBagConstraints gbcValueDateField = new GridBagConstraints();
		gbcValueDateField.insets = new Insets(0, 0, 5, 0);
		gbcValueDateField.anchor = GridBagConstraints.WEST;
		gbcValueDateField.gridx = 1;
		gbcValueDateField.gridy = 3;
		add(getValueDateField(), gbcValueDateField);
		GridBagConstraints gbcLblStatement = new GridBagConstraints();
		gbcLblStatement.anchor = GridBagConstraints.WEST;
		gbcLblStatement.insets = new Insets(0, 0, 0, 5);
		gbcLblStatement.gridx = 0;
		gbcLblStatement.gridy = 4;
		add(getLblStatement(), gbcLblStatement);
		GridBagConstraints gbcStatementField = new GridBagConstraints();
		gbcStatementField.anchor = GridBagConstraints.WEST;
		gbcStatementField.gridx = 1;
		gbcStatementField.gridy = 4;
		add(getStatementField(), gbcStatementField);
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
				@Override
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
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					firePropertyChange(VALUE_DATE_PROPERTY, evt.getOldValue(), evt.getNewValue());
				}
			});
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
			statementField.setToolTipText(LocalizationData.get("TransactionDialog.statement.tooltip")); //$NON-NLS-1$
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
		if ((vdc!=null) && (date!=null)) {
			getValueDateField().setDate(vdc.getNextStep(date));
		}
	}

	public void setAccount(Account account) {
		getAccountWidget().set(account);
	}
}
