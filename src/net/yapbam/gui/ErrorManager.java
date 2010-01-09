package net.yapbam.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ErrorManager {
	public static final ErrorManager INSTANCE = new ErrorManager();

	public void display(Component parent, Throwable e) {
		display (parent, e, LocalizationData.get("ErrorManager.message")); //$NON-NLS-1$
	}

	public void display(Component parent, Throwable e, String message) {
		e.printStackTrace(); //TODO Maybe better to log the exception ?
		JOptionPane.showMessageDialog(parent, message, LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
	}
}
