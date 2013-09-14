package net.yapbam.gui.graphics.balancehistory;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JCheckBox;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.net.URL;

class BalanceHistoryControlPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel report = null;
	private JButton today = null;
	private JCheckBox isGridVisible = null;
	private JPanel center = null;

	/**
	 * This is the default constructor
	 */
	public BalanceHistoryControlPane() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		report = new JLabel();
		report.setToolTipText(LocalizationData.get("BalanceHistory.report.toolTip")); //$NON-NLS-1$
		this.setLayout(new BorderLayout());
		this.add(getCenter(), BorderLayout.CENTER);
		this.add(getIsGridVisible(), BorderLayout.EAST);
	}

	/**
	 * This method initializes today	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getToday() {
		if (today == null) {
			today = new JButton();
			URL url = getClass().getResource("/com/fathzer/soft/ajlib/swing/widget/date/stop.png"); //$NON-NLS-1$
			today.setIcon(Utils.createIcon(url, (int)(16*Preferences.INSTANCE.getFontSizeRatio())));
			today.setToolTipText(LocalizationData.get("BalanceHistory.toDay.toolTip")); //$NON-NLS-1$
			int size = today.getPreferredSize().height;
			today.setPreferredSize(new Dimension(size, size));
		}
		return today;
	}

	/**
	 * This method initializes isGridVisible	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	JCheckBox getIsGridVisible() {
		if (isGridVisible == null) {
			isGridVisible = new JCheckBox();
			isGridVisible.setText(LocalizationData.get("BalanceHistory.showGrid")); //$NON-NLS-1$
			isGridVisible.setToolTipText(LocalizationData.get("BalanceHistory.showGrid.toolTip")); //$NON-NLS-1$
		}
		return isGridVisible;
	}

	void setReportText(String report) {
		this.report.setText(report);
	}

	/**
	 * This method initializes center	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCenter() {
		if (center == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 0;
			center = new JPanel();
			center.setLayout(new GridBagLayout());
			center.add(report, gridBagConstraints4);
			center.add(getToday(), gridBagConstraints2);
		}
		return center;
	}
}
