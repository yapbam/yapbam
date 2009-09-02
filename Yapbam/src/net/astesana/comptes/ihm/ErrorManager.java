package net.astesana.comptes.ihm;

import java.awt.Component;

import javax.swing.JOptionPane;

class ErrorManager {
	static final ErrorManager INSTANCE = new ErrorManager();

	void display(Component parent, Throwable e) {
		display (parent, e, "An error occured !\n Current operation is aborted."); //LOCAL
	}

	void display(Component parent, Throwable e, String message) {
		e.printStackTrace(); //TODO Maybe better to log the exception ?
		JOptionPane.showMessageDialog(parent, message, "ALERT !!", JOptionPane.WARNING_MESSAGE);
	}
}
