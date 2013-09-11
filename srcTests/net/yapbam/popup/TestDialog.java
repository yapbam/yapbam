package net.yapbam.popup;

import java.awt.Window;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.data.GlobalData;

public class TestDialog extends AbstractDialog<GlobalData, Void> {

	public TestDialog(Window owner, String title, GlobalData data) {
		super(owner, title, data);
	}

	@Override
	protected Void buildResult() {
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
