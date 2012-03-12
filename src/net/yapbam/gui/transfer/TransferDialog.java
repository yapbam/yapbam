package net.yapbam.gui.transfer;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.util.AbstractDialog;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

public class TransferDialog extends AbstractDialog<GlobalData, Boolean> {
	private static final long serialVersionUID = 1L;
	
	private TransferPanel transferPanel;

	public TransferDialog(Window owner, String title, GlobalData data) {
		super(owner, title, data);
	}

	@Override
	protected JPanel createCenterPane() {
		transferPanel = new TransferPanel(data);
		transferPanel.addPropertyChangeListener(TransferPanel.OK_DISABLED_CAUSE_PROPERTY, new AutoUpdateOkButtonPropertyListener(this));
		return transferPanel;
	}

	@Override
	protected Boolean buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		return transferPanel.getOkDisabledCause();
	}

}
