package net.yapbam.popup;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.dialogs.AbstractDialog;

public class TestDialog extends AbstractDialog<GlobalData> {

	public TestDialog(Window owner, String title, GlobalData data) {
		super(owner, title, data);
	}

	@Override
	protected Object buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		TestPanel testPanel = new TestPanel();
		testPanel.setData(data);
		return testPanel;
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}

}
