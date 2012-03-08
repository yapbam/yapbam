package net.yapbam.gui.transfer;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.util.AbstractDialog;

public class TransferDialog extends AbstractDialog<GlobalData, Boolean> {
	private static final long serialVersionUID = 1L;

	public TransferDialog(Window owner, String title, GlobalData data) {
		super(owner, title, data);
	}

	@Override
	protected JPanel createCenterPane() {
		return new TransferPanel(data);
	}

	@Override
	protected Boolean buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}

}
