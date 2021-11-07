package net.yapbam.gui.dialogs.checkbook;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.IntegerWidget;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;
import com.fathzer.soft.ajlib.utilities.NullUtils;
import com.fathzer.soft.ajlib.utilities.StringUtils;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;

public class CheckbookAlertsPreferencesPane extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$

	private String invalidityCause;
	private JCheckBox alertState;
	private IntegerWidget alertValue;

	/**
	 * This is the default constructor
	 */
	public CheckbookAlertsPreferencesPane() {
		super();
		initialize();
//TODO		parse();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Map<String, JComponent> components = new LinkedHashMap<String, JComponent>();
		components.put((Formatter.format(LocalizationData.get("checkbookAlertsPreferencesDialog.Help"),
				LocalizationData.get("AdministrationPlugIn.title"))), new JLabel(IconManager.get(Name.ALERT)));
		components.put(LocalizationData.get("checkbookAlertsPreferencesDialog.State"), getAlertState());
		components.put(LocalizationData.get("checkbookAlertsPreferencesDialog.Value"), getAlertValue());
		for (Map.Entry<String, JComponent> entry : components.entrySet()) {
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(new JLabel(entry.getKey()), BorderLayout.WEST);
			panel.add(Box.createHorizontalStrut(50), BorderLayout.CENTER);
			panel.add(entry.getValue(), BorderLayout.EAST);
			this.add(panel);
			this.add(Box.createVerticalStrut(10));
		}
	}

	public String getInvalidityCause() {
		return this.invalidityCause;
	}

	public int getAlertThreshold() {
		return this.alertState.isSelected() ? getAlertValue().getValue().intValue() : -1;
	}

	public void setContent(int threshold) {
		if (threshold > 0) {
			getAlertState().setSelected(Boolean.TRUE);
			getAlertValue().setEnabled(Boolean.TRUE);
			getAlertValue().setValue(threshold);
		} else {
			getAlertState().setSelected(Boolean.FALSE);
			getAlertValue().setEnabled(Boolean.FALSE);
			getAlertValue().setValue(1);
		}
//		parse();
	}
/*
	private void parse() {
		String old = this.invalidityCause;
		this.invalidityCause = null;
		if (getAlertState().isSelected()) {
			if (getAlertValueModel().getNumber() == null) {
				this.invalidityCause = LocalizationData.get("checkbookAlertsPreferencesDialog.error.valueIsBlank");
			}
		}
		if (!NullUtils.areEquals(old, this.invalidityCause)) {
			this.firePropertyChange(INVALIDITY_CAUSE, old, this.invalidityCause);
		}
	}*/

	/**
	 * This method initializes AlertState
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getAlertState() {
		if (alertState == null) {
			alertState = new JCheckBox(StringUtils.EMPTY);
			alertState.setToolTipText(LocalizationData.get("checkbookAlertsPreferencesDialog.State.tooltip"));
			alertState.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getAlertValue().setEnabled(alertState.isSelected());
				}
			});
		}
		return alertState;
	}

	/**
	 * This method initializes AlertValueModel
	 * 
	 * @return javax.swing.SpinnerNumberModel
	 */
	private IntegerWidget getAlertValue() {
		if (alertValue == null) {
			alertValue = new IntegerWidget(BigInteger.ONE, null);
			alertValue.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					updateOkDisableCause();
				}
			});
		}
		return alertValue;
	}

	private void updateOkDisableCause() {
		final String old = this.invalidityCause;
		if (getAlertState().isSelected() && getAlertValue().getValue()==null) {
			this.invalidityCause = LocalizationData.get("checkbookAlertsPreferencesDialog.error.valueIsBlank");
		} else {
			this.invalidityCause = null;
		}
		if (!NullUtils.areEquals(old, this.invalidityCause)) {
			firePropertyChange(INVALIDITY_CAUSE, old, this.invalidityCause);
		}
	}
}
