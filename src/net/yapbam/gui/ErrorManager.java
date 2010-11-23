package net.yapbam.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ErrorManager {
	public static final ErrorManager INSTANCE = new ErrorManager();
	
	private ErrorManager() {}
	
	public void display(Component parent, Throwable t) {
		display (parent, t, LocalizationData.get("ErrorManager.message")); //$NON-NLS-1$
	}

	public void display(Component parent, Throwable t, String message) {
		JOptionPane.showMessageDialog(parent, message, LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
	}
	
	public void log(Throwable t) {
		System.err.println("An exception was catched by "+this.getClass().getName());
		t.printStackTrace();
		//TODO Not yet implemented
		//May log the error and probably, try to send an error report to Yapbam.		
//		String message = t.getMessage();
//
//		if (message == null || message.length() == 0) {
//			message = "Fatal: " + t.getClass();
//		}
//
//		JOptionPane.showMessageDialog(null, "General Error", message, JOptionPane.ERROR_MESSAGE);
	}
}
