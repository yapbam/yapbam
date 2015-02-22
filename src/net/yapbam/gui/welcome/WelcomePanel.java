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
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.YapbamDataWrapper;
import net.yapbam.gui.persistence.YapbamPersistenceManager;

import javax.swing.JSeparator;

import com.fathzer.soft.ajlib.swing.Browser;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.widget.HTMLPane;
import com.fathzer.soft.ajlib.swing.widget.PageSelector;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@SuppressWarnings("serial")
public class WelcomePanel extends JPanel {
	private static final String BUNDLE_NAME = "net.yapbam.gui.welcome.urls"; //$NON-NLS-1$

	private JCheckBox showAtStartup;
	private HTMLPane tipPane;
	private TipManager tips;
	private ResourceBundle urlsResourceBundle;
	
	private GlobalData data;

	private URI getURI(String key) {
		try {
			String uriString = urlsResourceBundle.getString(key);
			if (uriString.contains(":")) { //$NON-NLS-1$
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
		
		PageSelector tipSelectionPanel = new PageSelector();
		tipSelectionPanel.addPropertyChangeListener(PageSelector.PAGE_SELECTED_PROPERTY_NAME, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Integer index = (Integer) evt.getNewValue();
				if (index!=null) {
					tipPane.setContent(tips.get(index));
				}
			}
		});
		tipsPanel.add(tipSelectionPanel, BorderLayout.SOUTH);
		
		tipSelectionPanel.getPageNumber().setToolTipText(LocalizationData.get("Welcome.tipNumber.tooltip")); //$NON-NLS-1$
		tipSelectionPanel.getFirstPage().setToolTipText(LocalizationData.get("Welcome.firstTip.tooltip")); //$NON-NLS-1$
		tipSelectionPanel.getNextPage().setToolTipText(LocalizationData.get("Welcome.nextTip.tooltip")); //$NON-NLS-1$
		tipSelectionPanel.getLastPage().setToolTipText(LocalizationData.get("Welcome.lastTip.tooltip")); //$NON-NLS-1$
		tipSelectionPanel.getPreviousPage().setToolTipText(LocalizationData.get("Welcome.previousTip.tooltip")); //$NON-NLS-1$

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
		
		tipSelectionPanel.setPageCount(tips.size());
		tipSelectionPanel.setPage(tips.getRandom());
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

	public boolean isShowAtStartup() {
		return this.showAtStartup.isSelected();
	}
	
	public void setShowAtStartup(boolean show) {
		this.showAtStartup.setSelected(show);
	}
}
