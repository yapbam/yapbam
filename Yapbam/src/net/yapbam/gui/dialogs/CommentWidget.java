package net.yapbam.gui.dialogs;

import javax.swing.JPanel;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

public class CommentWidget extends JPanel {
	private static final long serialVersionUID = 1L;
	private TextWidget textField;
	private JLabel linkButton;

	/**
	 * Create the panel.
	 */
	public CommentWidget() {
		initialize();
	}
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getTextField());
		add(getLinkButton(), BorderLayout.EAST);
	}

	private TextWidget getTextField() {
		if (textField == null) {
			textField = new TextWidget();
			textField.setColumns(10);
		}
		return textField;
	}
	private JLabel getLinkButton() {
		if (linkButton == null) {
			linkButton = new JLabel(IconManager.get(Name.LINK));
			linkButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (linkButton.isEnabled()) {
						System.out.println("link clicked on "+getTextField().getSelectedText());
					}
				}
			});
		}
		return linkButton;
	}
	public void setText(String comment) {
		getTextField().setText(comment);
	}
	public String getText() {
		return getTextField().getText();
	}
}
