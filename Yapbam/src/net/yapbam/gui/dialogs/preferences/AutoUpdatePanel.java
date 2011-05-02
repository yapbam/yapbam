package net.yapbam.gui.dialogs.preferences;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigInteger;

import javax.swing.JLabel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.widget.IntegerWidget;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public class AutoUpdatePanel extends PreferencePanel {

	private static final long serialVersionUID = 1L;
	private JRadioButton auto = null;
	private JRadioButton manual = null;
	private JLabel jLabel = null;
	private IntegerWidget days = null;
	private JLabel jLabel1 = null;
	private JPanel jPanel = null;
	private JRadioButton askMe = null;
	private JRadioButton autoInstall = null;
	private JLabel jLabel2 = null;

	/**
	 * This is the default constructor
	 */
	public AutoUpdatePanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
		gridBagConstraints51.gridx = 0;
		gridBagConstraints51.weighty = 1.0D;
		gridBagConstraints51.gridy = 3;
		jLabel2 = new JLabel();
		jLabel2.setText(""); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridwidth = 4;
		gridBagConstraints4.weighty = 0.0D;
		gridBagConstraints4.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints4.insets = new Insets(0, 20, 5, 5);
		gridBagConstraints4.gridy = 2;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 3;
		gridBagConstraints3.insets = new Insets(5, 5, 0, 5);
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.weightx = 1.0D;
		gridBagConstraints3.gridy = 1;
		jLabel1 = new JLabel();
		jLabel1.setText(LocalizationData.get("PreferencesDialog.AutoUpdate.interval.part2")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 0.0D;
		gridBagConstraints2.insets = new Insets(5, 5, 0, 5);
		gridBagConstraints2.gridx = 2;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints11.gridx = 1;
		gridBagConstraints11.insets = new Insets(5, 5, 0, 5);
		gridBagConstraints11.gridy = 1;
		jLabel = new JLabel();
		jLabel.setText(LocalizationData.get("PreferencesDialog.AutoUpdate.interval.part1")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.fill = GridBagConstraints.NONE;
		gridBagConstraints1.weighty = 0.0D;
		gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 5, 0, 5);
		gridBagConstraints.gridy = 1;
		this.setSize(378, 200);
		this.setLayout(new GridBagLayout());
		ButtonGroup group = new ButtonGroup();
		this.add(getAuto(), gridBagConstraints);
		this.add(getManual(), gridBagConstraints1);
		this.add(jLabel, gridBagConstraints11);
		this.add(getDays(), gridBagConstraints2);
		this.add(jLabel1, gridBagConstraints3);
		this.add(getJPanel(), gridBagConstraints4);
		this.add(jLabel2, gridBagConstraints51);
		group.add(getAuto());
		group.add(getManual());
		group = new ButtonGroup();
		group.add(getAutoInstall());
		group.add(getAskMe());
		int period = Preferences.INSTANCE.getAutoUpdatePeriod();
		days.setValue(Math.max(0, period));
		(Preferences.INSTANCE.getAutoUpdateInstall()?getAutoInstall():getAskMe()).setSelected(true);
		if (period>=0) getAuto().setSelected(true);		
	}
	
	private void refresh() {
		boolean ok = !getManual().isSelected();
		getAutoInstall().setEnabled(ok);
		getAskMe().setEnabled(ok);
		getJPanel().setVisible(ok);
	}

	/**
	 * This method initializes auto	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getAuto() {
		if (auto == null) {
			auto = new JRadioButton();
			auto.setText(LocalizationData.get("PreferencesDialog.AutoUpdate.atStartup")); //$NON-NLS-1$
			auto.setToolTipText(LocalizationData.get("PreferencesDialog.AutoUpdate.atStartup.toolTip")); //$NON-NLS-1$
		}
		return auto;
	}

	/**
	 * This method initializes manual	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getManual() {
		if (manual == null) {
			manual = new JRadioButton();
			manual.setText(LocalizationData.get("PreferencesDialog.AutoUpdate.manually")); //$NON-NLS-1$
			manual.setSelected(true);
			manual.setToolTipText(LocalizationData.get("PreferencesDialog.AutoUpdate.manually.toolTip")); //$NON-NLS-1$
			manual.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					refresh();
				}
			});
		}
		return manual;
	}

	/**
	 * This method initializes days	
	 * 	
	 * @return net.yapbam.ihm.widget.IntegerWidget	
	 */
	private IntegerWidget getDays() {
		if (days == null) {
			days = new IntegerWidget(BigInteger.ZERO, IntegerWidget.INTEGER_MAX_VALUE);
			days.setColumns(2);
			days.setToolTipText(LocalizationData.get("PreferencesDialog.AutoUpdate.days.toolTip")); //$NON-NLS-1$
		}
		return days;
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("PreferencesDialog.AutoUpdate.title"); //$NON-NLS-1$
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("PreferencesDialog.AutoUpdate.toolTip"); //$NON-NLS-1$
	}

	@Override
	public boolean updatePreferences() {
		int step = -1;
		if (getAuto().isSelected()) {
			if (days.getValue()==null) {
				days.setValue(0);
			}
			step = days.getValue().intValue();
		}
		Preferences.INSTANCE.setAutoUpdate(step, getAutoInstall().isSelected());
		return false;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gbc_autoInstall = new GridBagConstraints();
			gbc_autoInstall.fill = GridBagConstraints.HORIZONTAL;
			gbc_autoInstall.gridx = 0;
			gbc_autoInstall.insets = new Insets(5, 5, 0, 5);
			gbc_autoInstall.anchor = GridBagConstraints.WEST;
			gbc_autoInstall.gridy = 1;
			GridBagConstraints gbc_askMe = new GridBagConstraints();
			gbc_askMe.fill = GridBagConstraints.HORIZONTAL;
			gbc_askMe.anchor = GridBagConstraints.WEST;
			gbc_askMe.gridy = 0;
			gbc_askMe.insets = new Insets(0, 5, 0, 5);
			gbc_askMe.gridx = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			TitledBorder border = BorderFactory.createTitledBorder(null, LocalizationData.get("PreferencesDialog.AutoUpdate.ifAvailable.title")); //$NON-NLS-1$
			jPanel.setBorder(border);
			jPanel.setEnabled(true);
			JRadioButton btn = getAskMe();
			jPanel.add(btn, gbc_askMe);
			jPanel.add(getAutoInstall(), gbc_autoInstall);
			jPanel.setVisible(false);
			// Here is a workaround on bug http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4201045
			Dimension d = btn.getPreferredSize();
			if (d.width<border.getMinimumSize(jPanel).width) {
				btn.setPreferredSize(new Dimension(border.getMinimumSize(jPanel).width, d.height));
			}
		}
		return jPanel;
	}

	/**
	 * This method initializes silentFail	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getAskMe() {
		if (askMe == null) {
			askMe = new JRadioButton();
			askMe.setText(LocalizationData.get("PreferencesDialog.AutoUpdate.ifAvailable.askMe")); //$NON-NLS-1$
			askMe.setEnabled(false);
		}
		return askMe;
	}

	/**
	 * This method initializes shoutingFail	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getAutoInstall() {
		if (autoInstall == null) {
			autoInstall = new JRadioButton();
			autoInstall.setText(LocalizationData.get("PreferencesDialog.AutoUpdate.ifAvailable.autoInstall")); //$NON-NLS-1$
			autoInstall.setEnabled(false);
		}
		return autoInstall;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
