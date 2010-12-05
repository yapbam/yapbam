package net.yapbam.gui.welcome;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;
import java.awt.Insets;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import net.yapbam.gui.widget.HTMLPane;

@SuppressWarnings("serial")
public class WelcomePanel extends JPanel {

	private JCheckBox showAtStartup;

	/**
	 * Create the panel.
	 */
	public WelcomePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblWelcomeToYapbam = new JLabel("Welcome to Yapbam");
		GridBagConstraints gbc_lblWelcomeToYapbam = new GridBagConstraints();
		gbc_lblWelcomeToYapbam.gridwidth = 2;
		gbc_lblWelcomeToYapbam.insets = new Insets(0, 0, 5, 0);
		gbc_lblWelcomeToYapbam.gridx = 0;
		gbc_lblWelcomeToYapbam.gridy = 0;
		add(lblWelcomeToYapbam, gbc_lblWelcomeToYapbam);
		
		JPanel bottomPanel = new JPanel();
		GridBagConstraints gbc_bottomPanel = new GridBagConstraints();
		gbc_bottomPanel.gridwidth = 2;
		gbc_bottomPanel.fill = GridBagConstraints.BOTH;
		gbc_bottomPanel.gridx = 0;
		gbc_bottomPanel.gridy = 2;
		add(bottomPanel, gbc_bottomPanel);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[]{0, 0};
		gbl_bottomPanel.rowHeights = new int[]{0, 0};
		gbl_bottomPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_bottomPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		bottomPanel.setLayout(gbl_bottomPanel);
		bottomPanel.setOpaque(false);
		
		showAtStartup = new JCheckBox("Show at startup");
		showAtStartup.setOpaque(false);
		showAtStartup.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_showAtStartup = new GridBagConstraints();
		gbc_showAtStartup.anchor = GridBagConstraints.EAST;
		gbc_showAtStartup.weightx = 1.0;
		gbc_showAtStartup.fill = GridBagConstraints.HORIZONTAL;
		gbc_showAtStartup.gridx = 0;
		gbc_showAtStartup.gridy = 0;
		bottomPanel.add(showAtStartup, gbc_showAtStartup);
		
		JPanel shortcutsPanel = new JPanel();
		shortcutsPanel.setOpaque(false);
		shortcutsPanel.setBorder(new TitledBorder(null, "Useful shortcuts", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_shortcutsPanel = new GridBagConstraints();
		gbc_shortcutsPanel.insets = new Insets(0, 0, 5, 5);
		gbc_shortcutsPanel.fill = GridBagConstraints.BOTH;
		gbc_shortcutsPanel.gridx = 0;
		gbc_shortcutsPanel.gridy = 1;
		add(shortcutsPanel, gbc_shortcutsPanel);
		GridBagLayout gbl_shortcutsPanel = new GridBagLayout();
		gbl_shortcutsPanel.columnWidths = new int[]{0, 0};
		gbl_shortcutsPanel.rowHeights = new int[]{0, 0, 0};
		gbl_shortcutsPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_shortcutsPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		shortcutsPanel.setLayout(gbl_shortcutsPanel);
		
		JButton btnOpenSampleData = new JButton("Open sample data file");
		btnOpenSampleData.setHorizontalAlignment(SwingConstants.LEFT);
		btnOpenSampleData.setToolTipText("Click here to open a sample file");
		GridBagConstraints gbc_btnOpenSampleData = new GridBagConstraints();
		gbc_btnOpenSampleData.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpenSampleData.weightx = 1.0;
		gbc_btnOpenSampleData.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOpenSampleData.gridx = 0;
		gbc_btnOpenSampleData.gridy = 0;
		shortcutsPanel.add(btnOpenSampleData, gbc_btnOpenSampleData);
		
		JButton btnViewTheTutorial = new JButton("<html>View the tutorial<BR>(Internet connection needed)</html>");
		btnViewTheTutorial.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_btnViewTheTutorial = new GridBagConstraints();
		gbc_btnViewTheTutorial.anchor = GridBagConstraints.WEST;
		gbc_btnViewTheTutorial.weightx = 1.0;
		gbc_btnViewTheTutorial.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnViewTheTutorial.gridx = 0;
		gbc_btnViewTheTutorial.gridy = 1;
		shortcutsPanel.add(btnViewTheTutorial, gbc_btnViewTheTutorial);
		
		JPanel tipsPanel = new JPanel();
		tipsPanel.setBorder(new TitledBorder(null, "Tip of the day ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_tipsPanel = new GridBagConstraints();
		gbc_tipsPanel.weighty = 1.0;
		gbc_tipsPanel.weightx = 1.0;
		gbc_tipsPanel.insets = new Insets(0, 0, 5, 0);
		gbc_tipsPanel.fill = GridBagConstraints.BOTH;
		gbc_tipsPanel.gridx = 1;
		gbc_tipsPanel.gridy = 1;
		add(tipsPanel, gbc_tipsPanel);
		GridBagLayout gbl_tipsPanel = new GridBagLayout();
		gbl_tipsPanel.columnWidths = new int[]{68, 0};
		gbl_tipsPanel.rowHeights = new int[]{16, 0, 0};
		gbl_tipsPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_tipsPanel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		tipsPanel.setLayout(gbl_tipsPanel);
		tipsPanel.setOpaque(false);
		
		HTMLPane textPane = new HTMLPane();
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.insets = new Insets(0, 0, 5, 0);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 0;
		tipsPanel.add(textPane, gbc_textPane);
		
		JPanel tipSelectionPanel = new JPanel();
		GridBagConstraints gbc_tipSelectionPanel = new GridBagConstraints();
		gbc_tipSelectionPanel.fill = GridBagConstraints.BOTH;
		gbc_tipSelectionPanel.gridx = 0;
		gbc_tipSelectionPanel.gridy = 1;
		tipsPanel.add(tipSelectionPanel, gbc_tipSelectionPanel);
		GridBagLayout gbl_tipSelectionPanel = new GridBagLayout();
		gbl_tipSelectionPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_tipSelectionPanel.rowHeights = new int[]{0, 0};
		gbl_tipSelectionPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_tipSelectionPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		tipSelectionPanel.setLayout(gbl_tipSelectionPanel);
		tipSelectionPanel.setOpaque(false);
		
		JButton firstTip = new JButton("");
		firstTip.setIcon(new ImageIcon(WelcomePanel.class.getResource("/net/yapbam/gui/images/bottom.png")));
		GridBagConstraints gbc_firstTip = new GridBagConstraints();
		gbc_firstTip.insets = new Insets(0, 0, 0, 5);
		gbc_firstTip.weighty = 1.0;
		gbc_firstTip.fill = GridBagConstraints.VERTICAL;
		gbc_firstTip.gridx = 1;
		gbc_firstTip.gridy = 0;
		tipSelectionPanel.add(firstTip, gbc_firstTip);
		
		JButton previousTip = new JButton("");
		previousTip.setIcon(new ImageIcon(WelcomePanel.class.getResource("/net/yapbam/gui/images/down.png")));
		GridBagConstraints gbc_previousTip = new GridBagConstraints();
		gbc_previousTip.insets = new Insets(0, 0, 0, 5);
		gbc_previousTip.gridx = 2;
		gbc_previousTip.gridy = 0;
		tipSelectionPanel.add(previousTip, gbc_previousTip);
		
		JButton button = new JButton("");
		button.setIcon(new ImageIcon(WelcomePanel.class.getResource("/net/yapbam/gui/images/up.png")));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 0, 5);
		gbc_button.gridx = 3;
		gbc_button.gridy = 0;
		tipSelectionPanel.add(button, gbc_button);
		
		JButton button_1 = new JButton("");
		button_1.setIcon(new ImageIcon(WelcomePanel.class.getResource("/net/yapbam/gui/images/top.png")));
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.gridx = 4;
		gbc_button_1.gridy = 0;
		tipSelectionPanel.add(button_1, gbc_button_1);

		this.setOpaque(false);
	}
	
	public boolean isShowAtStartup() {
		return this.showAtStartup.isSelected();
	}
	
	public void setShowAtStartup(boolean show) {
		this.showAtStartup.setSelected(show);
	}
}
