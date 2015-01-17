package net.yapbam.gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.ImageIcon;

import java.awt.Insets;

import com.fathzer.soft.ajlib.swing.widget.HTMLPane;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JLabel;

public class InfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final ImageIcon OPEN = new ImageIcon(InfoPanel.class.getResource("/net/yapbam/gui/images/spread.png"));
	private static final ImageIcon CLOSED = new ImageIcon(InfoPanel.class.getResource("/net/yapbam/gui/images/undeploy.png"));

	private JButton closeBtn;
	private HTMLPane textPane;
	private JLabel alertIcon;
	private List<Info> news;

	/**
	 * Create the panel.
	 */
	public InfoPanel() {
		initialize();
	}

	private void initialize() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbcCloseBtn = new GridBagConstraints();
		gbcCloseBtn.insets = new Insets(0, 0, 5, 0);
		gbcCloseBtn.anchor = GridBagConstraints.EAST;
		gbcCloseBtn.gridx = 1;
		gbcCloseBtn.gridy = 0;
		add(getCloseBtn(), gbcCloseBtn);
		GridBagConstraints gbcTextPane = new GridBagConstraints();
		gbcTextPane.insets = new Insets(0, 0, 5, 0);
		gbcTextPane.fill = GridBagConstraints.BOTH;
		gbcTextPane.gridx = 0;
		gbcTextPane.gridy = 1;
		add(getTextPane(), gbcTextPane);
		GridBagConstraints gbcAlertIcon = new GridBagConstraints();
		gbcAlertIcon.weightx = 1.0;
		gbcAlertIcon.anchor = GridBagConstraints.WEST;
		gbcAlertIcon.gridx = 0;
		gbcAlertIcon.gridy = 0;
		add(getAlertIcon(), gbcAlertIcon);
		setVisible(false);
	}
	
	private JButton getCloseBtn() {
		if (closeBtn == null) {
			closeBtn = new JButton(OPEN);
			closeBtn.setBorder(BorderFactory.createEmptyBorder());
			closeBtn.setBorderPainted(false);
			closeBtn.setFocusPainted(false);
			closeBtn.setContentAreaFilled(false);
			closeBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setInfoVisible(!getTextPane().isVisible());
				}
			});
		}
		return closeBtn;
	}
	
	public void setInfoVisible(boolean visible) {
		getTextPane().setVisible(visible);
		getCloseBtn().setIcon(visible?OPEN:CLOSED);
		revalidate();
	}
	
	private HTMLPane getTextPane() {
		if (textPane == null) {
			textPane = new HTMLPane();
		}
		return textPane;
	}

	private JLabel getAlertIcon() {
		if (alertIcon == null) {
			alertIcon = new JLabel("");
			alertIcon.setIcon(new ImageIcon(InfoPanel.class.getResource("/net/yapbam/gui/images/alert.png")));
		}
		return alertIcon;
	}

	public void setInfo(List<Info> news) {
		//TODO Display all news
		//TODO Check if id is ok
		this.news = news;
		setVisible(!news.isEmpty());
		if (!news.isEmpty()) {
			setNews(0);
		}
	}

	private void setNews(int index) {
		setInfoVisible(true);
		getTextPane().setContent(news.get(index).content);
	}

	static class Info {
		private String id;
		private String content;
		
		public Info(String id, String content) {
			super();
			this.id = id;
			this.content = content;
		}
	}
}
