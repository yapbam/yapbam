package net.yapbam.gui.dialogs.checkbook;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.utilities.NullUtils;
import com.fathzer.soft.ajlib.utilities.StringUtils;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;

public class CheckbookAlertsPreferencesPane extends JPanel {

	private static final long serialVersionUID = 1L;

	static final String INVALIDITY_CAUSE = "invalidityCause";

	private String invalidityCause;
	private JCheckBox alertState;
	private SpinnerNumberModel alertValueModel;
	private JSpinner alertValue;

	/**
	 * This is the default constructor
	 */
	public CheckbookAlertsPreferencesPane() {
		super();
		initialize();
		parse();
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

	public Integer getAlertThreshold() {
		return this.alertState.isSelected() ? getAlertValueModel().getNumber().intValue() : Integer.MIN_VALUE;
	}

	public void setContent(Integer threshold) {
		if (threshold != null && threshold > 0) {
			getAlertState().setSelected(Boolean.TRUE);
			getAlertValue().setEnabled(Boolean.TRUE);
			getAlertValue().setValue(threshold);
		} else {
			getAlertState().setSelected(Boolean.FALSE);
			getAlertValue().setEnabled(Boolean.FALSE);
			getAlertValue().setValue(getAlertValueModel().getMinimum());
		}
		parse();
	}

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
	}

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
	private SpinnerNumberModel getAlertValueModel() {
		if (alertValueModel == null) {
			alertValueModel = new SpinnerNumberModel(1, 1, 100, 1);
		}
		return alertValueModel;
	}

	/**
	 * This method initializes AlertValue
	 * 
	 * @return javax.swing.JSpinner
	 */
	private JSpinner getAlertValue() {
		if (alertValue == null) {
			alertValue = new JSpinner(getAlertValueModel());
			alertValue.setToolTipText(LocalizationData.get("checkbookAlertsPreferencesDialog.Value.tooltip"));
			alertValue.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					parse();
				}
			});
		}
		return alertValue;
	}
}
