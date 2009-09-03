package net.yapbam.ihm;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MainFrameListener extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent event) {
		MainFrame frame = (MainFrame) event.getWindow();
		if (SaveManager.MANAGER.verify(frame)) {
			frame.saveState();
			super.windowClosing(event);
			System.exit(0);
		}
	}
/*
	@Override
	public void windowActivated(WindowEvent e) {
		System.out.println ("windowActivated");
		super.windowActivated(e);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.out.println ("windowClosed");
		super.windowClosed(e);
	}
/*
	@Override
	public void windowDeactivated(WindowEvent e) {
		System.out.println ("windowDeactivated");
		super.windowDeactivated(e);
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		System.out.println ("windowDeiconified");
		super.windowDeiconified(e);
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		System.out.println ("windowGainedFocus");
		super.windowGainedFocus(e);
	}

	@Override
	public void windowIconified(WindowEvent e) {
		System.out.println ("windowIconified "+e);
		super.windowIconified(e);
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		System.out.println ("windowLostFocus");
		super.windowLostFocus(e);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		System.out.println ("windowOpened");
		super.windowOpened(e);
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		System.out.println ("windowStateChanged "+e);
		super.windowStateChanged(e);
	}*/
}
