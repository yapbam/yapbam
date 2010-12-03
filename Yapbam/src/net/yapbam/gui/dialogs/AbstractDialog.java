package net.yapbam.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import net.yapbam.gui.LocalizationData;

public abstract class AbstractDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private Object result;

	protected JButton cancelButton;
	protected JButton okButton;
	protected Object data;

	/**
	 * Constructor
	 * @param owner Dialog's parent frame
	 * @param title Dialog's title
	 * @param data optional data (will be transfered to createContentPane)
	 */
	public AbstractDialog(Window owner, String title, Object data) {
		super(owner, title, ModalityType.APPLICATION_MODAL);
		this.data = data;
		this.result = null;
		this.setContentPane(this.createContentPane());
		this.pack();
		this.setLocationRelativeTo(owner);
		this.setResizable(false);
	}
	
	private Container createContentPane() {
		//Create the content pane.
		JPanel contentPane = new JPanel(new BorderLayout(5,5));
		contentPane.setOpaque(true);
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel southPane = new JPanel(new BorderLayout());
		southPane.setOpaque(false);
		JPanel buttonsPane = new JPanel();
		buttonsPane.setOpaque(false);
		southPane.add(buttonsPane, BorderLayout.EAST);
		okButton = new JButton(LocalizationData.get("GenericButton.ok"));
		okButton.setOpaque(false);
		buttonsPane.add(okButton);
		cancelButton = new JButton(LocalizationData.get("GenericButton.cancel"));
		cancelButton.setToolTipText(LocalizationData.get("GenericButton.cancel.toolTip"));
		cancelButton.setOpaque(false);
		buttonsPane.add(cancelButton);

		contentPane.add(southPane, BorderLayout.SOUTH);

		JPanel centerPane = this.createCenterPane(data);
		if (centerPane != null) contentPane.add(centerPane, BorderLayout.CENTER);
		
		this.updateOkButtonEnabled();
		getRootPane().setDefaultButton(okButton);
		
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(okButton)) {
					result = buildResult();
				}
				setVisible(false);
			}
		};
		okButton.addActionListener(listener);
		cancelButton.addActionListener(listener);
		return contentPane;
	}
	
	protected abstract JPanel createCenterPane(Object data);
	
	public void actionPerformed(ActionEvent e) {
	}
	
	protected abstract Object buildResult();

	/** Returns a message explaining why the ok button is disabled
	 * @return the message or null if the ok button has to be enabled. This message will be displayed in the button toolTip.
	 */
	protected abstract String getOkDisabledCause();
	
	public Object getResult() {
		return result;
	}
	
	protected void updateOkButtonEnabled() {
		String cause = getOkDisabledCause();
		this.okButton.setEnabled(cause==null);
		this.okButton.setToolTipText(cause==null?LocalizationData.get("GenericButton.ok.toolTip"):cause);
	}

	/** Return the window which contains the component */
	public static Window getOwnerWindow(Component component) {
		while (!(component instanceof Window)) {
			if (component instanceof JPopupMenu) {
				component = ((JPopupMenu)component).getInvoker();
			} else {
				component = component.getParent();
			}
		}
		return (Window) component;
	}
}
