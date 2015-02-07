package net.yapbam.gui.info;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import com.fathzer.soft.ajlib.swing.widget.HTMLPane;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;
import java.util.List;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.widget.PageSelector;
import net.yapbam.gui.LocalizationData;

public class InfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JButton closeBtn;
	private HTMLPane textPane;
	private News news;
	private JButton displayButton;
	private InfoCommandPanel panel;

	/**
	 * Create the panel.
	 */
	public InfoPanel() {
		initialize();
	}

	private void initialize() {
		System.out.println (getBackground());
		System.out.println (getBackground().darker());
		setBackground(getBackground().darker());
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbcCloseBtn = new GridBagConstraints();
		gbcCloseBtn.insets = new Insets(5, 0, 0, 5);
		gbcCloseBtn.anchor = GridBagConstraints.NORTHEAST;
		gbcCloseBtn.gridx = 1;
		gbcCloseBtn.gridy = 0;
		add(getCloseBtn(), gbcCloseBtn);
		GridBagConstraints gbcTextPane = new GridBagConstraints();
		gbcTextPane.weighty = 1.0;
		gbcTextPane.weightx = 1.0;
		gbcTextPane.fill = GridBagConstraints.BOTH;
		gbcTextPane.gridx = 0;
		gbcTextPane.gridy = 0;
		add(getTextPane(), gbcTextPane);
		setVisible(false);
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.weightx = 1.0;
		gbcPanel.insets = new Insets(0, 5, 0, 0);
		gbcPanel.fill = GridBagConstraints.BOTH;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 1;
		add(getPanel(), gbcPanel);
	}
	
	private JButton getCloseBtn() {
		if (closeBtn == null) {
			closeBtn = new JButton(IconManager.get(Name.CLOSE));
			closeBtn.setBorder(BorderFactory.createEmptyBorder());
			closeBtn.setBorderPainted(false);
			closeBtn.setFocusPainted(false);
			closeBtn.setContentAreaFilled(false);
			closeBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			closeBtn.setToolTipText(LocalizationData.get("info.close.tooltip")); //$NON-NLS-1$
		}
		return closeBtn;
	}
	
	private HTMLPane getTextPane() {
		if (textPane == null) {
			textPane = new HTMLPane();
			textPane.addPropertyChangeListener(HTMLPane.CONTENT_CHANGED_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					revalidate();
				}
			});
		}
		return textPane;
	}

	public void setInfo(List<Info> newsList) {
		for (Info info : newsList) {
			System.out.println (info.getId()+" -> "+info.isRead()); //TODO
		}
		
		this.news = new News(newsList);
		setVisible(!news.isEmpty());
		if (!news.isEmpty()) {
			getPanel().getPageSelector().setPageCount(news.size());
			getPanel().getPageSelector().setPage(0);
		}
	}

	private void setNews(int index) {
		Info info = news.get(index);
		// Update the command panel
		getPanel().getMarkAsReadButton().setEnabled(!info.isRead());
		getPanel().getPageSelector().getPageNumber().setValue((BigInteger)null);
		getPanel().getPageSelector().setPageCount(news.size());
		getPanel().getPageSelector().setPage(index);
		setVisible(true);
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (displayButton!=null) {
			displayButton.setVisible(!visible);
		}
	}

	public void setDisplayButton(JButton displayButton) {
		this.displayButton = displayButton;
		if (displayButton!=null) {
			this.displayButton.setToolTipText(LocalizationData.get("info.open.tooltip"));
			this.displayButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(true);
				}
			});
		}
	}
	private InfoCommandPanel getPanel() {
		if (panel == null) {
			panel = new InfoCommandPanel();
			panel.getMarkAsReadButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int index = panel.getPageSelector().getPageNumber().getValue().intValue()-1;
					Info info = news.get(index);
					info.markRead();
					if (news.isOnlyUnread()) {
						// Only unread are displayed
						if (news.isEmpty()) {
							InfoPanel.this.setVisible(false);
						} else {
							if (index<=news.size()-1) {
								setNews(index);
							} else {
								setNews(0);
							}
						}
					} else {
						if (index<news.size()-1) {
							setNews(index+1);
						} else {
							setNews(0);
						}
					}
				}
			});
			panel.getShowReadCheckBox().addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					System.out.println ("Changed to "+(e.getStateChange()==ItemEvent.SELECTED));
				}
			});
			panel.getPageSelector().addPropertyChangeListener(PageSelector.PAGE_SELECTED_PROPERTY_NAME, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					int index = (Integer) evt.getNewValue();
					System.out.println (news.isOnlyUnread()+", selected info: "+index+" -> "+news.get(index).getId()+(news.get(index).isRead()?" (read)":" (unread)")); //TODO
					getTextPane().setContent(news.get(index).getContent());
				}
			});
		}
		return panel;
	}
}
