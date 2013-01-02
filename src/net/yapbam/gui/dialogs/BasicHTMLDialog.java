package net.yapbam.gui.dialogs;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.astesana.ajlib.swing.widget.AbstractTitledPanel;
import net.astesana.ajlib.swing.widget.HTMLPane;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class BasicHTMLDialog extends AbstractDialog<Object[], Void> {
	private static final Dimension PREFERED_HTML_PANE_SIZE = new Dimension(480,240);

	public static enum Type {
		INFO, WARNING, ERROR, QUESTION;

		public Icon getIcon() {
			if (this.equals(INFO)) {
				return UIManager.getIcon("OptionPane.informationIcon");
			} else if (this.equals(ERROR)) {
				return UIManager.getIcon("OptionPane.errorIcon");
			} else if (this.equals(QUESTION)) {
				return UIManager.getIcon("OptionPane.questionIcon");
			} else if (this.equals(WARNING)) {
				return UIManager.getIcon("OptionPane.warningIcon");
			}
			return null;
		}
	}
	
	private AbstractTitledPanel<String> panel;
	
	public BasicHTMLDialog(Window owner, String title, String header, Type type, String content) {
		super(owner, title, new Object[]{header, type==null?null:type.getIcon(), content});
		getCancelButton().setVisible(false);
		getOkButton().setText(LocalizationData.get("GenericButton.close")); //$NON-NLS-1$
		getOkButton().setToolTipText(LocalizationData.get("GenericButton.close.ToolTip")); //$NON-NLS-1$
		getOkButton().requestFocus();
	}

	@Override
	protected Void buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		if (panel==null) {
			panel = new AbstractTitledPanel<String>((String)data[0], (Icon)data[1], (String)data[2]) {
				@Override
				protected JComponent getCenterComponent() {
					HTMLPane htmlPane = new HTMLPane(this.data);
					htmlPane.setPreferredSize(PREFERED_HTML_PANE_SIZE);
					htmlPane.setFocusable(false);
					return htmlPane;
				}
			};
		}
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
