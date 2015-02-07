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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.widget.PageSelector;
import net.yapbam.gui.LocalizationData;

public class InfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JButton closeBtn;
	private HTMLPane textPane;
	private List<Info> news;
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

	public void setInfo(List<Info> news) {
		//TODO Display all news
		//TODO Check if id is ok
		this.news = news;
		setVisible(!news.isEmpty());
		if (!news.isEmpty()) {
			getPanel().getPageSelector().setPageCount(news.size());
			getPanel().getPageSelector().setPage(0);
		}
	}

	private void setNews(int index) {
		getTextPane().setContent(news.get(index).getContent());
		setVisible(true);
	}

//	public void setInfoVisible(boolean visible) {
//		getTextPane().setVisible(visible);
//		getCloseBtn().setVisible(visible);
//		if (displayButton!=null) {
//			displayButton.setVisible(!visible);
//		}
//		getTextPane().getTextPane().revalidate();
//		revalidate();
//	}

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
					int index = panel.getPageSelector().getPageNumber().getValue().intValue();
					Info info = news.get(index);
					info.markRead();
					//FIXME Go to next, or prev, or close the panel
				}
			});
			panel.getPageSelector().addPropertyChangeListener(PageSelector.PAGE_SELECTED_PROPERTY_NAME, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					setNews((Integer) evt.getNewValue());
				}
			});
		}
		return panel;
	}
}
