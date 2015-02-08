package net.yapbam.gui.info;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import com.fathzer.jlocal.Formatter;
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

import javax.swing.JLabel;

public class InfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JButton closeBtn;
	private HTMLPane textPane;
	private News news;
	private JButton displayButton;
	private InfoCommandPanel panel;
	private JLabel titleLabel;

	/**
	 * Create the panel.
	 */
	public InfoPanel() {
		initialize();
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbcTitleLabel = new GridBagConstraints();
		gbcTitleLabel.insets = new Insets(2, 5, 0, 5);
		gbcTitleLabel.gridx = 0;
		gbcTitleLabel.gridy = 0;
		add(getTitleLabel(), gbcTitleLabel);
		GridBagConstraints gbcCloseBtn = new GridBagConstraints();
		gbcCloseBtn.insets = new Insets(2, 0, 0, 2);
		gbcCloseBtn.weightx = 1.0;
		gbcCloseBtn.anchor = GridBagConstraints.NORTHEAST;
		gbcCloseBtn.gridx = 1;
		gbcCloseBtn.gridy = 0;
		add(getCloseBtn(), gbcCloseBtn);
		GridBagConstraints gbcTextPane = new GridBagConstraints();
		gbcTextPane.gridwidth = 0;
		gbcTextPane.weighty = 1.0;
		gbcTextPane.weightx = 1.0;
		gbcTextPane.fill = GridBagConstraints.BOTH;
		gbcTextPane.gridx = 0;
		gbcTextPane.gridy = 1;
		add(getTextPane(), gbcTextPane);
		setVisible(false);
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.gridwidth = 0;
		gbcPanel.weightx = 1.0;
		gbcPanel.insets = new Insets(0, 5, 5, 0);
		gbcPanel.fill = GridBagConstraints.BOTH;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 2;
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
		this.news = new News(newsList);
		setVisible(!news.isEmpty());
		setNews(news.isEmpty()?-1:0);
	}

	private void setNews(int index) {
System.out.println("Setting message "+index);//TODO
		Info info = index<0?null:news.get(index);
		int size = news.size();
		// Update the command panel
		getPanel().getMarkAsReadButton().setEnabled(size!=0);
		getPanel().getMarkAsReadButton().setEnabled(info!=null && !info.isRead());
		getPanel().getPageSelector().setVisible(size!=0);
		getPanel().getPageSelector().getPageNumber().setValue((BigInteger)null);
		getPanel().getPageSelector().setPageCount(size);
		getPanel().getPageSelector().setPage(index);
		int read = news.getNbRead();
		getPanel().getShowReadCheckBox().setEnabled(read!=0);
		getPanel().getShowReadCheckBox().setText(Formatter.format("Show read messages ({0})", read));
		getTitleLabel().setIcon(IconManager.get(read==news.getPhysicalSize()?Name.MESSAGE:Name.NEW_MESSAGE));
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (displayButton!=null) {
			displayButton.setVisible(!visible);
			displayButton.setIcon(IconManager.get(news.isEmpty()?Name.MESSAGE:Name.NEW_MESSAGE));
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
							setNews(-1);
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
	private JLabel getTitleLabel() {
		if (titleLabel == null) {
			titleLabel = new JLabel(Formatter.format("<HTML><font size=\"4\"><U><B>{0}</B></U></font></HTML>", "Messages"));
		}
		return titleLabel;
	}
}
