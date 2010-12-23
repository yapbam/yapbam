package net.yapbam.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import net.yapbam.gui.dialogs.ErrorDialog;

public class ErrorManager {
	public static final ErrorManager INSTANCE = new ErrorManager();
	
	private ErrorManager() {}

	public void display(Component parent, Throwable t) {
		//TODO Let see if this method remains useful
		display (parent, t, LocalizationData.get("ErrorManager.message")); //$NON-NLS-1$
	}

	public void display(Component parent, Throwable t, String message) {
		//TODO Let see if this method remains useful
		JOptionPane.showMessageDialog(parent, message, LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
	}
	
	public void log(Throwable t) {
		System.err.println("An exception was catched by "+this.getClass().getName());		
		t.printStackTrace();
		//TODO Probably need to ensure that only one dialog could be opened at a time
		ErrorDialog errorDialog = new ErrorDialog(null, t);
		errorDialog.setVisible(true);
		if (errorDialog.getResult()!=null) {
			System.out.println ("TODO : The report is not sent");
			//TODO send message to Yapbam
		}
		errorDialog.dispose(); //Don't remove this line, it would prevent Yapbam from quit !!!
	}
}
