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

import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.YapbamDataWrapper;
import net.yapbam.gui.persistence.YapbamPersistenceManager;

import javax.swing.JSeparator;

import com.fathzer.soft.ajlib.swing.Browser;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.widget.HTMLPane;
import com.fathzer.soft.ajlib.swing.widget.IntegerWidget;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class WelcomePanel extends JPanel {
	private static final String BUNDLE_NAME = "net.yapbam.gui.welcome.urls"; //$NON-NLS-1$

	private JCheckBox showAtStartup;
	private IntegerWidget tipNumber;
	private HTMLPane tipPane;
	private TipManager tips;
	private JButton nextTip;
	private JButton lastTip;
	private JButton previousTip;
	private JButton firstTip;
	private int currentTip;
	private ResourceBundle urlsResourceBundle;
	
	private GlobalData data;
		
	private URI getURI(String key) {
		try {
			String uriString = urlsResourceBundle.getString(key);
			if (uriString.contains(":")) {
				return new URI(uriString);
			}
			return new File(uriString).toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Create the panel.
	 */
	public WelcomePanel(GlobalData data) {
		this.data = data;
		
		urlsResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
		
		tips = new TipManager();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		JLabel lblWelcomeToYapbam = new JLabel(LocalizationData.get("Welcome.introduction")); //$NON-NLS-1$
		lblWelcomeToYapbam.setIcon(UIManager.getIcon("OptionPane.informationIcon")); //$NON-NLS-1$
		GridBagConstraints gbcLblWelcomeToYapbam = new GridBagConstraints();
		gbcLblWelcomeToYapbam.anchor = GridBagConstraints.WEST;
		gbcLblWelcomeToYapbam.gridwidth = 2;
		gbcLblWelcomeToYapbam.insets = new Insets(0, 10, 5, 0);
		gbcLblWelcomeToYapbam.gridx = 0;
		gbcLblWelcomeToYapbam.gridy = 0;
		add(lblWelcomeToYapbam, gbcLblWelcomeToYapbam);
		
		JPanel bottomPanel = new JPanel();
		GridBagConstraints gbcBottomPanel = new GridBagConstraints();
		gbcBottomPanel.insets = new Insets(0, 0, 5, 0);
		gbcBottomPanel.gridwidth = 2;
		gbcBottomPanel.fill = GridBagConstraints.BOTH;
		gbcBottomPanel.gridx = 0;
		gbcBottomPanel.gridy = 3;
		add(bottomPanel, gbcBottomPanel);
		GridBagLayout gblBottomPanel = new GridBagLayout();
		bottomPanel.setLayout(gblBottomPanel);
		bottomPanel.setOpaque(false);
		
		showAtStartup = new JCheckBox(LocalizationData.get("Welcome.showAtStartup")); //$NON-NLS-1$
		showAtStartup.setToolTipText(LocalizationData.get("Welcome.showAtStartup.tooltip")); //$NON-NLS-1$
		showAtStartup.setOpaque(false);
		showAtStartup.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbcShowAtStartup = new GridBagConstraints();
		gbcShowAtStartup.anchor = GridBagConstraints.EAST;
		gbcShowAtStartup.weightx = 1.0;
		gbcShowAtStartup.fill = GridBagConstraints.HORIZONTAL;
		gbcShowAtStartup.gridx = 0;
		gbcShowAtStartup.gridy = 0;
		bottomPanel.add(showAtStartup, gbcShowAtStartup);
		
		JPanel shortcutsPanel = new JPanel();
		shortcutsPanel.setOpaque(false);
		shortcutsPanel.setBorder(new TitledBorder(null, LocalizationData.get("Welcome.shorcuts.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		GridBagConstraints gbcShortcutsPanel = new GridBagConstraints();
		gbcShortcutsPanel.anchor = GridBagConstraints.NORTH;
		gbcShortcutsPanel.weighty = 1.0;
		gbcShortcutsPanel.insets = new Insets(0, 0, 5, 5);
		gbcShortcutsPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcShortcutsPanel.gridx = 0;
		gbcShortcutsPanel.gridy = 2;
		add(shortcutsPanel, gbcShortcutsPanel);
		GridBagLayout gblShortcutsPanel = new GridBagLayout();
		shortcutsPanel.setLayout(gblShortcutsPanel);
		
		final URI file = getURI("sampleFile"); //$NON-NLS-1$
		final JButton btnOpenSampleData = new JButton(LocalizationData.get("Welcome.sampleData")); //$NON-NLS-1$
		btnOpenSampleData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (YapbamPersistenceManager.MANAGER.read(Utils.getOwnerWindow(WelcomePanel.this), new YapbamDataWrapper(WelcomePanel.this.data),
						file, null)) {
					WelcomePanel.this.data.setURI(null);
				}
			}
		});
		btnOpenSampleData.setHorizontalAlignment(SwingConstants.LEFT);
		btnOpenSampleData.setToolTipText(LocalizationData.get("Welcome.sampleData.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcBtnOpenSampleData = new GridBagConstraints();
		gbcBtnOpenSampleData.anchor = GridBagConstraints.NORTH;
		gbcBtnOpenSampleData.insets = new Insets(0, 0, 5, 0);
		gbcBtnOpenSampleData.weightx = 1.0;
		gbcBtnOpenSampleData.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnOpenSampleData.gridx = 0;
		gbcBtnOpenSampleData.gridy = 0;
		shortcutsPanel.add(btnOpenSampleData, gbcBtnOpenSampleData);
		
		JButton btnViewTheTutorial = new JButton(LocalizationData.get("Welcome.tutorial")); //$NON-NLS-1$
		btnViewTheTutorial.addActionListener(new URIClienthandler(getURI("tutorial"))); //$NON-NLS-1$
		btnViewTheTutorial.setToolTipText(LocalizationData.get("Welcome.tutorial.tooltip")); //$NON-NLS-1$
		btnViewTheTutorial.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbcBtnViewTheTutorial = new GridBagConstraints();
		gbcBtnViewTheTutorial.insets = new Insets(0, 0, 5, 0);
		gbcBtnViewTheTutorial.anchor = GridBagConstraints.WEST;
		gbcBtnViewTheTutorial.weightx = 1.0;
		gbcBtnViewTheTutorial.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnViewTheTutorial.gridx = 0;
		gbcBtnViewTheTutorial.gridy = 1;
		btnViewTheTutorial.setVisible(true);
		shortcutsPanel.add(btnViewTheTutorial, gbcBtnViewTheTutorial);
		
		JButton faq = new JButton(LocalizationData.get("Welcome.faq")); //$NON-NLS-1$
		faq.addActionListener(new URIClienthandler(getURI("faq"))); //$NON-NLS-1$
		faq.setHorizontalAlignment(SwingConstants.LEFT);
		faq.setToolTipText(LocalizationData.get("Welcome.faq.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcFaq = new GridBagConstraints();
		gbcFaq.anchor = GridBagConstraints.NORTH;
		gbcFaq.weighty = 1.0;
		gbcFaq.fill = GridBagConstraints.HORIZONTAL;
		gbcFaq.gridx = 0;
		gbcFaq.gridy = 2;
		shortcutsPanel.add(faq, gbcFaq);
		
		JPanel tipsPanel = new JPanel();
		tipsPanel.setBorder(new TitledBorder(LocalizationData.get("Welcome.tip.title"))); //$NON-NLS-1$
		GridBagConstraints gbcTipsPanel = new GridBagConstraints();
		gbcTipsPanel.weighty = 1.0;
		gbcTipsPanel.weightx = 1.0;
		gbcTipsPanel.insets = new Insets(0, 0, 5, 0);
		gbcTipsPanel.fill = GridBagConstraints.BOTH;
		gbcTipsPanel.gridx = 1;
		gbcTipsPanel.gridy = 2;
		add(tipsPanel, gbcTipsPanel);
		tipsPanel.setOpaque(false);
		tipsPanel.setLayout(new BorderLayout(0, 0));
		
		tipPane = new HTMLPane();
		tipPane.setPreferredSize(new Dimension(300*getFont().getSize()/12, 200*getFont().getSize()/12));
		tipsPanel.add(tipPane, BorderLayout.CENTER);
		
		JPanel tipSelectionPanel = new JPanel();
		tipsPanel.add(tipSelectionPanel, BorderLayout.SOUTH);
		GridBagLayout gblTipSelectionPanel = new GridBagLayout();
		tipSelectionPanel.setLayout(gblTipSelectionPanel);
		tipSelectionPanel.setOpaque(false);
		
		firstTip = new JButton();
		firstTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTip(0);
			}
		});
		firstTip.setToolTipText(LocalizationData.get("Welcome.firstTip.tooltip")); //$NON-NLS-1$
		firstTip.setIcon(IconManager.get(Name.FIRST));
		GridBagConstraints gbcFirstTip = new GridBagConstraints();
		gbcFirstTip.insets = new Insets(0, 0, 0, 5);
		gbcFirstTip.weighty = 1.0;
		gbcFirstTip.fill = GridBagConstraints.VERTICAL;
		gbcFirstTip.gridx = 1;
		gbcFirstTip.gridy = 0;
		setTipSelectionButtonSize(firstTip);
		tipSelectionPanel.add(firstTip, gbcFirstTip);
		
		tipNumber = new IntegerWidget(BigInteger.ONE, BigInteger.valueOf(tips.size()));
		tipNumber.setToolTipText(LocalizationData.get("Welcome.tipNumber.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcTipNumber = new GridBagConstraints();
		gbcTipNumber.gridx = 3;
		gbcTipNumber.gridy = 0;
		gbcTipNumber.fill = GridBagConstraints.VERTICAL;
		tipSelectionPanel.add(tipNumber, gbcTipNumber);
		tipNumber.setColumns(2);
		tipNumber.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue()!=null) {
					setTip(((BigInteger)evt.getNewValue()).intValue()-1);
				}
			}
		});
		
		JLabel label = new JLabel("/"+tips.size()); //$NON-NLS-1$
		GridBagConstraints gbcLabel = new GridBagConstraints();
		gbcLabel.insets = new Insets(0, 0, 0, 5);
		gbcLabel.gridx = 4;
		gbcLabel.gridy = 0;
		tipSelectionPanel.add(label, gbcLabel);
		
		nextTip = new JButton();
		nextTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTip(currentTip+1);
			}
		});
		nextTip.setToolTipText(LocalizationData.get("Welcome.nextTip.tooltip")); //$NON-NLS-1$
		nextTip.setIcon(IconManager.get(Name.NEXT));
		GridBagConstraints gbcNextTip = new GridBagConstraints();
		gbcNextTip.fill = GridBagConstraints.VERTICAL;
		gbcNextTip.insets = new Insets(0, 0, 0, 5);
		gbcNextTip.gridx = 5;
		gbcNextTip.gridy = 0;
		setTipSelectionButtonSize(nextTip);
		tipSelectionPanel.add(nextTip, gbcNextTip);
		
		lastTip = new JButton();
		lastTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTip(tips.size()-1);
			}
		});
		lastTip.setToolTipText(LocalizationData.get("Welcome.lastTip.tooltip")); //$NON-NLS-1$
		lastTip.setIcon(IconManager.get(Name.LAST));
		GridBagConstraints gbcLastTip = new GridBagConstraints();
		gbcLastTip.fill = GridBagConstraints.VERTICAL;
		gbcLastTip.gridx = 6;
		gbcLastTip.gridy = 0;
		setTipSelectionButtonSize(lastTip);
		tipSelectionPanel.add(lastTip, gbcLastTip);
		
		previousTip = new JButton();
		previousTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTip(currentTip-1);
			}
		});
		previousTip.setToolTipText(LocalizationData.get("Welcome.previousTip.tooltip")); //$NON-NLS-1$
		previousTip.setIcon(IconManager.get(Name.PREVIOUS));
		GridBagConstraints gbcPreviousTip = new GridBagConstraints();
		gbcPreviousTip.fill = GridBagConstraints.VERTICAL;
		gbcPreviousTip.insets = new Insets(0, 0, 0, 5);
		gbcPreviousTip.gridx = 2;
		gbcPreviousTip.gridy = 0;
		setTipSelectionButtonSize(previousTip);
		tipSelectionPanel.add(previousTip, gbcPreviousTip);

		this.setOpaque(false);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbcSeparator = new GridBagConstraints();
		gbcSeparator.weightx = 1.0;
		gbcSeparator.gridwidth = 2;
		gbcSeparator.fill = GridBagConstraints.HORIZONTAL;
		gbcSeparator.anchor = GridBagConstraints.NORTH;
		gbcSeparator.insets = new Insets(0, 10, 20, 10);
		gbcSeparator.gridx = 0;
		gbcSeparator.gridy = 1;
		add(separator, gbcSeparator);
		
		currentTip = -1; // Just to ensure the tip will be displayed (if we omit this line and tip is the first, setTip would think the tip hasn't change)
		setTip(tips.getRandom());
	}
	
	private final class URIClienthandler implements ActionListener {
		private URI uri;

		private URIClienthandler(URI uri) {
			this.uri = uri;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			Browser.show(uri, WelcomePanel.this, null);
		}
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
		}
	}
}
