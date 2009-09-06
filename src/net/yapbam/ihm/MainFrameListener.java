package net.yapbam.ihm;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MainFrameListener extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent event) {
		MainFrame frame = (MainFrame) event.getWindow();
		if (SaveManager.MANAGER.verify(frame)) {
			YapbamState.save(frame);
			Preferences.INSTANCE.save();
			super.windowClosing(event);
			System.exit(0);
		}
	}
}
