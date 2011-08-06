package net.yapbam.gui.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import net.yapbam.gui.LocalizationData;

/** An abstract dialog with a customizable center pane, an Ok and/or a Cancel button.
 * <br>By default, the dialog is not resizable, call this.setResizable(true) to change this behaviour.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 * @param <T> The class of the parameter of the dialog (information that is useful to build the center pane).
 * @param <V> The class of the result of the dialog
 */
public abstract class AbstractDialog<T,V> extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private V result;

	/** The cancel button.
	 * If you want the dialog not to have a cancel button, you may set the visibility of this button to false
	 * (cancelButton.setVisible(false)).
	 */
	protected JButton cancelButton;
	/** The ok button.
	 * If you want the dialog not to have an ok button, you may set the visibility of this button to false
	 * (okButton.setVisible(false)).
	 */
	protected JButton okButton;
	/** The data passed to the dialog's constructor.
	 */
	protected T data;

	/**
	 * Constructor
	 * @param owner Dialog's parent frame
	 * @param title Dialog's title
	 * @param data optional data (will be transfered to createContentPane)
	 */
	public AbstractDialog(Window owner, String title, T data) {
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
		JPanel buttonsPane = createButtonsPane();

		southPane.add(buttonsPane, BorderLayout.EAST);
		contentPane.add(southPane, BorderLayout.SOUTH);

		JPanel centerPane = this.createCenterPane();
		if (centerPane != null) contentPane.add(centerPane, BorderLayout.CENTER);
		
		this.updateOkButtonEnabled();
		
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(okButton)) {
					confirm();
				} else {
					cancel();
				}
			}
		};
		okButton.addActionListener(listener);
		cancelButton.addActionListener(listener);
		return contentPane;
	}

	/** Gets the panel that contains the ok and cancel buttons.
	 * <br>This method creates a new JPanel with flow layout and ok and cancel buttons. Then, it defines ok has the default button.
	 * <br>It can be override to add more buttons.
	 * @return a JPanel
	 */
	protected JPanel createButtonsPane() {
		JPanel buttonsPane = new JPanel();
		buttonsPane.setOpaque(false);
		okButton = new JButton(LocalizationData.get("GenericButton.ok"));
		okButton.setOpaque(false);
		buttonsPane.add(okButton);
		cancelButton = new JButton(LocalizationData.get("GenericButton.cancel"));
		cancelButton.setToolTipText(LocalizationData.get("GenericButton.cancel.toolTip"));
		cancelButton.setOpaque(false);
		buttonsPane.add(cancelButton);
		getRootPane().setDefaultButton(okButton);
		return buttonsPane;
	}
	
	/** Gets the center pane of this dialog.
	 * This method is called once by the constructor of the dialog.
	 * The data attribute is already set to the data parameter passed to the constructor.
	 * @return a panel
	 */
	protected abstract JPanel createCenterPane();
	
	/** This method is called when the user clicks the ok button.
	 * <br>This method should return the object, result of the dialog, that will be returned
	 * by getResult.
	 * <br>Note that it is not a good practice to override this method and set its visibility to public.
	 * You should prefer calling the getResult method as buildResult may instantiate a new object each
	 * time it is called.
	 * @return an object
	 * @see #getResult()
	 */
	protected abstract V buildResult();

	/** This method is called when the user clicks the ok button.
	 * <br>This method should return the object, result of the dialog, that will be returned
	 * by getResult.
	 * <br>Note that it is not a good practice to override this method and set its visibility to public.
	 * You should prefer calling the getResult method as buildResult may instantiate a new object each
	 * time it is called.
	 * @see #getResult()
	 */
	protected void confirm() {
		result = buildResult();
		setVisible(false);
	}

	/** This method is called when the user clicks the cancel button.
	 * This default implementation closes the dialog.
	 */
	protected void cancel() {
		setVisible(false);
	}

	/** Checks if the user input is consistent and return a short explanation of why it is not 
	 * @return a short message explaining why the ok button is disabled or null if the ok button has to be enabled.
	 * This message will be displayed in the ok button toolTip.
	 */
	protected abstract String getOkDisabledCause();
	
	/** Gets the result of this dialog.
	 * @return an object, or null if the dialog was cancelled.
	 */
	public V getResult() {
		return result;
	}
	
	/** Forces the state of the users input to be evaluated and updates the state of the ok button.
	 * @see #getOkDisabledCause()
	 */
	public void updateOkButtonEnabled() {
		String cause = getOkDisabledCause();
		this.okButton.setEnabled(cause==null);
		this.okButton.setToolTipText(cause==null?LocalizationData.get("GenericButton.ok.toolTip"):cause);
	}

	/** Gets the window which contains a component.
	 * @param component the component
	 * @return The window containing the component or null if no window contains the component.
	 */
	public static Window getOwnerWindow(Component component) {
		while ((component!=null) && !(component instanceof Window)) {
			if (component instanceof JPopupMenu) {
				component = ((JPopupMenu)component).getInvoker();
			} else {
				component = component.getParent();
			}
		}
		return (Window) component;
	}
}
