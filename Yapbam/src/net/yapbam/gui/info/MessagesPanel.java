package net.yapbam.gui.info;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.UIManager;

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

import java.awt.BorderLayout;

public class MessagesPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JButton closeBtn;
	private HTMLPane textPane;
	private Messages messages;
	private JButton displayButton;
	private MessagesCommandPanel panel;
	private JLabel titleLabel;
	private JPanel panel_1;
	private JLabel icon;

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
		gbcTitleLabel.insets = new Insets(2, 5, 5, 5);
		gbcTitleLabel.gridx = 0;
		gbcTitleLabel.gridy = 0;
		add(getTitleLabel(), gbcTitleLabel);
		GridBagConstraints gbcCloseBtn = new GridBagConstraints();
		gbcCloseBtn.insets = new Insets(2, 0, 5, 2);
		gbcCloseBtn.weightx = 1.0;
		gbcCloseBtn.anchor = GridBagConstraints.NORTHEAST;
		gbcCloseBtn.gridx = 1;
		gbcCloseBtn.gridy = 0;
		add(getCloseBtn(), gbcCloseBtn);
		GridBagConstraints gbcPanel_1 = new GridBagConstraints();
		gbcPanel_1.weighty = 1.0;
		gbcPanel_1.weightx = 1.0;
		gbcPanel_1.fill = GridBagConstraints.BOTH;
		gbcPanel_1.gridwidth = 0;
		gbcPanel_1.insets = new Insets(0, 5, 0, 0);
		gbcPanel_1.gridx = 0;
		gbcPanel_1.gridy = 1;
		add(getPanel_1(), gbcPanel_1);
		setVisible(false);
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.gridwidth = 0;
		gbcPanel.weightx = 1.0;
		gbcPanel.insets = new Insets(0, 5, 0, 0);
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
		getIcon().setIcon(getIcon(message));
	}
	
	private Icon getIcon(Message message) {
		if (message==null) {
			return null;
		} else if (Message.Kind.WARNING.equals(message.getKind())) {
			return UIManager.getIcon("OptionPane.warningIcon"); //$NON-NLS-1$
		}
		return UIManager.getIcon("OptionPane.informationIcon"); //$NON-NLS-1$
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (displayButton!=null) {
			displayButton.setVisible(!visible && messages.getPhysicalSize()!=0);
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
					if (messages.isEmpty()) {
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
		return Formatter.format("<html>{0}</html>",LocalizationData.get("messagesnoMessageAvailable")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setLayout(new BorderLayout(0, 0));
			panel_1.add(getTextPane());
			panel_1.add(getIcon(), BorderLayout.WEST);
		}
		return panel_1;
	}
	private JLabel getIcon() {
		if (icon == null) {
			icon = new JLabel();
		}
		return icon;
	}
}
