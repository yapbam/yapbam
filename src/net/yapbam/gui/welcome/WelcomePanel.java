package net.yapbam.gui.welcome;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.UIManager;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.widget.HTMLPane;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;

import net.yapbam.gui.widget.IntegerWidget;

@SuppressWarnings("serial")
public class WelcomePanel extends JPanel {//LOCAL

	private JCheckBox showAtStartup;
	private IntegerWidget tipNumber;
	private HTMLPane tipPane;
	private TipManager tips;
	private JButton nextTip;
	private JButton lastTip;
	private JButton previousTip;
	private JButton firstTip;
	private int currentTip;

	/**
	 * Create the panel.
	 */
	public WelcomePanel() {
		tips = new TipManager();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		JLabel lblWelcomeToYapbam = new JLabel("<html>Welcome to <b>Yapbam</b></html>");
		lblWelcomeToYapbam.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
		GridBagConstraints gbc_lblWelcomeToYapbam = new GridBagConstraints();
		gbc_lblWelcomeToYapbam.anchor = GridBagConstraints.WEST;
		gbc_lblWelcomeToYapbam.gridwidth = 2;
		gbc_lblWelcomeToYapbam.insets = new Insets(0, 10, 5, 0);
		gbc_lblWelcomeToYapbam.gridx = 0;
		gbc_lblWelcomeToYapbam.gridy = 0;
		add(lblWelcomeToYapbam, gbc_lblWelcomeToYapbam);
		
		JPanel bottomPanel = new JPanel();
		GridBagConstraints gbc_bottomPanel = new GridBagConstraints();
		gbc_bottomPanel.insets = new Insets(0, 0, 5, 0);
		gbc_bottomPanel.gridwidth = 2;
		gbc_bottomPanel.fill = GridBagConstraints.BOTH;
		gbc_bottomPanel.gridx = 0;
		gbc_bottomPanel.gridy = 3;
		add(bottomPanel, gbc_bottomPanel);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[]{0, 0};
		gbl_bottomPanel.rowHeights = new int[]{0, 0};
		gbl_bottomPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_bottomPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		bottomPanel.setLayout(gbl_bottomPanel);
		bottomPanel.setOpaque(false);
		
		showAtStartup = new JCheckBox("Show at startup");
		showAtStartup.setToolTipText("Check this box to show this dialog at every yapbam startup");
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
		gbc_shortcutsPanel.gridy = 2;
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
		btnViewTheTutorial.setToolTipText("Click here to open the tutorial web page");
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
		gbc_tipsPanel.gridy = 2;
		add(tipsPanel, gbc_tipsPanel);
		GridBagLayout gbl_tipsPanel = new GridBagLayout();
		gbl_tipsPanel.columnWidths = new int[]{68, 0};
		gbl_tipsPanel.rowHeights = new int[]{16, 0, 0};
		gbl_tipsPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_tipsPanel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		tipsPanel.setLayout(gbl_tipsPanel);
		tipsPanel.setOpaque(false);
		
		tipPane = new HTMLPane();
		tipPane.setPreferredSize(new Dimension(300,200));
		GridBagConstraints gbc_tipPane = new GridBagConstraints();
		gbc_tipPane.weightx = 1.0;
		gbc_tipPane.insets = new Insets(0, 0, 5, 0);
		gbc_tipPane.fill = GridBagConstraints.BOTH;
		gbc_tipPane.gridx = 0;
		gbc_tipPane.gridy = 0;
		tipsPanel.add(tipPane, gbc_tipPane);
		
		JPanel tipSelectionPanel = new JPanel();
		GridBagConstraints gbc_tipSelectionPanel = new GridBagConstraints();
		gbc_tipSelectionPanel.fill = GridBagConstraints.VERTICAL;
		gbc_tipSelectionPanel.gridx = 0;
		gbc_tipSelectionPanel.gridy = 1;
		tipsPanel.add(tipSelectionPanel, gbc_tipSelectionPanel);
		GridBagLayout gbl_tipSelectionPanel = new GridBagLayout();
		gbl_tipSelectionPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_tipSelectionPanel.rowHeights = new int[]{0, 0};
		gbl_tipSelectionPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_tipSelectionPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		tipSelectionPanel.setLayout(gbl_tipSelectionPanel);
		tipSelectionPanel.setOpaque(false);
		
		firstTip = new JButton();
		firstTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTip(0);
			}
		});
		firstTip.setToolTipText("Displays the first tip");
		firstTip.setIcon(IconManager.FIRST);
		GridBagConstraints gbc_firstTip = new GridBagConstraints();
		gbc_firstTip.insets = new Insets(0, 0, 0, 5);
		gbc_firstTip.weighty = 1.0;
		gbc_firstTip.fill = GridBagConstraints.VERTICAL;
		gbc_firstTip.gridx = 1;
		gbc_firstTip.gridy = 0;
		setTipSelectionButtonSize(firstTip);
		tipSelectionPanel.add(firstTip, gbc_firstTip);
		
		tipNumber = new IntegerWidget(BigInteger.ONE, BigInteger.valueOf(tips.size()));
		tipNumber.setToolTipText("Type a tip number here to display it");
		GridBagConstraints gbc_tipNumber = new GridBagConstraints();
		gbc_tipNumber.gridx = 3;
		gbc_tipNumber.gridy = 0;
		tipSelectionPanel.add(tipNumber, gbc_tipNumber);
		tipNumber.setColumns(2);
		tipNumber.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue()!=null) setTip(((BigInteger)evt.getNewValue()).intValue()-1);
			}
		});
		
		JLabel label = new JLabel("/"+tips.size());
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 4;
		gbc_label.gridy = 0;
		tipSelectionPanel.add(label, gbc_label);
		
		nextTip = new JButton();
		nextTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTip(currentTip+1);
			}
		});
		nextTip.setToolTipText("Displays next tip");
		nextTip.setIcon(IconManager.NEXT);
		GridBagConstraints gbc_nextTip = new GridBagConstraints();
		gbc_nextTip.insets = new Insets(0, 0, 0, 5);
		gbc_nextTip.gridx = 5;
		gbc_nextTip.gridy = 0;
		setTipSelectionButtonSize(nextTip);
		tipSelectionPanel.add(nextTip, gbc_nextTip);
		
		lastTip = new JButton();
		lastTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTip(tips.size()-1);
			}
		});
		lastTip.setToolTipText("Displays last tip");
		lastTip.setIcon(IconManager.LAST);
		GridBagConstraints gbc_lastTip = new GridBagConstraints();
		gbc_lastTip.gridx = 6;
		gbc_lastTip.gridy = 0;
		setTipSelectionButtonSize(lastTip);
		tipSelectionPanel.add(lastTip, gbc_lastTip);
		
		previousTip = new JButton();
		previousTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTip(currentTip-1);
			}
		});
		previousTip.setToolTipText("Displays the previous tip");
		previousTip.setIcon(IconManager.PREVIOUS);
		GridBagConstraints gbc_previousTip = new GridBagConstraints();
		gbc_previousTip.insets = new Insets(0, 0, 0, 5);
		gbc_previousTip.gridx = 2;
		gbc_previousTip.gridy = 0;
		setTipSelectionButtonSize(previousTip);
		tipSelectionPanel.add(previousTip, gbc_previousTip);

		this.setOpaque(false);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.weightx = 1.0;
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.anchor = GridBagConstraints.NORTH;
		gbc_separator.insets = new Insets(0, 10, 20, 10);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		add(separator, gbc_separator);
		
		currentTip = -1; // Just to ensure the tip will be displayed (if we omit this line and tip is the first, setTip would think the tip hasn't change)
		setTip(tips.getRandom());
	}
	
	private void setTipSelectionButtonSize(JButton button) {
		Dimension preferredSize = button.getPreferredSize();
		preferredSize.width = preferredSize.height;
		button.setPreferredSize(preferredSize);
	}

	public boolean isShowAtStartup() {
		return this.showAtStartup.isSelected();
	}
	
	public void setShowAtStartup(boolean show) {
		this.showAtStartup.setSelected(show);
	}
	
	private void setTip(int index) {
		if (index!=currentTip) {
			currentTip = index;
			tipPane.setContent(tips.get(index));
			firstTip.setEnabled(index!=0);
			previousTip.setEnabled(index!=0);
			nextTip.setEnabled(index!=tips.size()-1);
			lastTip.setEnabled(index!=tips.size()-1);
			tipNumber.setText(Integer.toString(index+1));
//		AbstractDialog.getOwnerWindow(this).pack();
		}
	}
}
