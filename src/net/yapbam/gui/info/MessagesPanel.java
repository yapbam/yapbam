package net.yapbam.gui.info;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.HTMLPane;
import com.fathzer.soft.ajlib.swing.widget.PageSelector;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;

import javax.swing.JLabel;

public class MessagesPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JButton closeBtn;
	private HTMLPane textPane;
	private Messages messages;
	private JButton displayButton;
	private MessagesCommandPanel panel;
	private JLabel titleLabel;

	/**
	 * Create the panel.
	 */
	public MessagesPanel() {
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
			closeBtn.setToolTipText(LocalizationData.get("messages.close.tooltip")); //$NON-NLS-1$
		}
		return closeBtn;
	}
	
	private HTMLPane getTextPane() {
		if (textPane == null) {
			textPane = new HTMLPane();
			textPane.setContent(getNoMessageWording());
			textPane.addPropertyChangeListener(HTMLPane.CONTENT_CHANGED_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					revalidate();
				}
			});
		}
		return textPane;
	}

	public void setMessages(List<Message> newsList) {
		this.messages = new Messages(newsList);
		boolean isEmpty = messages.isEmpty();
		setVisible(!isEmpty);
		setMessage(isEmpty?-1:0);
	}

	private void setMessage(int index) {
		int size = messages.size();
		getPanel().getPageSelector().setVisible(size!=0);
		getPanel().getPageSelector().setPage(-1);
		getPanel().getPageSelector().setPageCount(size);
		if (index>=0) {
			getPanel().getPageSelector().setPage(index);
		}
		refreshButtons(index);
	}

	private void refreshButtons(int index) {
		Message message = index<0?null:messages.get(index);
		// Update the command panel
		getPanel().getMarkAsReadButton().setEnabled(message!=null && !message.isRead());
		int read = messages.getNbRead();
		getPanel().getShowReadCheckBox().setEnabled(read!=0);
		getPanel().getShowReadCheckBox().setText(Formatter.format(LocalizationData.get("messages.showReadMessages"), read, messages.getPhysicalSize())); //$NON-NLS-1$
		getTitleLabel().setIcon(IconManager.get(read==messages.getPhysicalSize()?Name.MESSAGE:Name.NEW_MESSAGE));
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (displayButton!=null) {
			displayButton.setVisible(!visible);
			displayButton.setIcon(IconManager.get(messages.isAllRead()?Name.MESSAGE:Name.NEW_MESSAGE));
		}
		if (visible && !getPanel().getShowReadCheckBox().isSelected() && messages.isEmpty()) {
			getPanel().getShowReadCheckBox().setSelected(true);
		}
	}

	public void setDisplayButton(JButton displayButton) {
		this.displayButton = displayButton;
		if (displayButton!=null) {
			this.displayButton.setToolTipText(LocalizationData.get("messages.open.tooltip")); //$NON-NLS-1$
			this.displayButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(true);
				}
			});
		}
	}
	private MessagesCommandPanel getPanel() {
		if (panel == null) {
			panel = new MessagesCommandPanel();
			panel.getMarkAsReadButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int index = panel.getPageSelector().getPageNumber().getValue().intValue()-1;
					Message message = messages.get(index);
					message.markRead();
					if (messages.isOnlyUnread()) {
						// Only unread are displayed
						if (messages.isEmpty()) {
							MessagesPanel.this.setVisible(false);
							setMessage(-1);
						} else {
							if (index<=messages.size()-1) {
								setMessage(index);
							} else {
								setMessage(0);
							}
						}
					} else {
						if (index<messages.size()-1) {
							setMessage(index+1);
						} else {
							setMessage(0);
						}
					}
				}
			});
			panel.getShowReadCheckBox().addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					boolean onlyUnread = e.getStateChange()==ItemEvent.DESELECTED;
					Message message = getMessage();
					messages.setOnlyUnread(onlyUnread);
					if (messages.size()==0) {
						setMessage(-1);
					} else if (message==null) {
						setMessage(0);
					} else {
						setMessage(messages.getNearest(message));
					}
				}
			});
			panel.getPageSelector().addPropertyChangeListener(PageSelector.PAGE_SELECTED_PROPERTY_NAME, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					int index = (Integer) evt.getNewValue();
					getTextPane().setContent(index<0?getNoMessageWording():messages.get(index).getContent()); //$NON-NLS-1$
					refreshButtons(index);
				}
			});
		}
		return panel;
	}
	private JLabel getTitleLabel() {
		if (titleLabel == null) {
			titleLabel = new JLabel(Formatter.format("<HTML><U><B>{0}</B></U></HTML>", LocalizationData.get("messages.title"))); //$NON-NLS-1$ //$NON-NLS-2$
			titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize()*1.2f));
		}
		return titleLabel;
	}
	
	private Message getMessage() {
		int index = getPanel().getPageSelector().getCurrentPage();
		return index<0?null:messages.get(index);
	}

	private String getNoMessageWording() {
		return Formatter.format("<html>{0}</html>",LocalizationData.get("messagesnoMessageAvailable")); //$NON-NLS-1$
	}
}
