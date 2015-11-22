package net.yapbam.gui.dialogs;

import javax.swing.JPanel;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.util.HtmlUtils;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Matcher;

import javax.swing.JLabel;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

public class CommentWidget extends JPanel {
	private static final String BUTTON_TOOLTIP_DISABLED_KEY = "LinkEditor.button.tooltip.disabled"; //$NON-NLS-1$
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
			textField.setToolTipText(LocalizationData.get("TransactionDialog.comment.tooltip")); //$NON-NLS-1$
			textField.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					getLinkButton().setEnabled(false);
					linkButton.setToolTipText(LocalizationData.get(BUTTON_TOOLTIP_DISABLED_KEY));
				}
				
				@Override
				public void focusGained(FocusEvent e) {
					getLinkButton().setEnabled(true);
					getLinkButton().setToolTipText(LocalizationData.get("LinkEditor.button.tooltip")); //$NON-NLS-1$
				}
			});
		}
		return textField;
	}
	private JLabel getLinkButton() {
		if (linkButton == null) {
			linkButton = new JLabel(IconManager.get(Name.LINK));
			linkButton.setToolTipText(LocalizationData.get(BUTTON_TOOLTIP_DISABLED_KEY));
			linkButton.setEnabled(false);
			linkButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (linkButton.isEnabled()) {
						int start = getTextField().getSelectionStart();
						int end = getTextField().getSelectionEnd();
						String text = getTextField().getSelectedText();
						String url = null;
						if (end!=0) {
							Matcher link = HtmlUtils.getLink(getTextField().getText(), start, end);
							if (link!=null) {
								text = link.group(1);
								url = link.group(2);
								start = link.start(1)-1;
								end = link.end(2)+2;
							}
						}
						LinkEditDialog dialog = new LinkEditDialog(Utils.getOwnerWindow(getLinkButton()), new String[]{text, url});
						dialog.setVisible(true);
						String result = dialog.getResult();
						if (result!=null) {
							getTextField().setSelectionStart(start);
							getTextField().setSelectionEnd(end);
							getTextField().replaceSelection(result);
							getTextField().setSelectionStart(start);
							getTextField().setSelectionEnd(start+result.length());
						}
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
