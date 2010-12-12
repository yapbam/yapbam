package net.yapbam.popup;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.dialogs.AbstractDialog;

public class TestDialog extends AbstractDialog {

	public TestDialog(Window owner, String title, Object data) {
		super(owner, title, data);
	}

	@Override
	protected Object buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		TestPanel testPanel = new TestPanel();
		testPanel.setData((GlobalData)data);
		return testPanel;
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}

}
