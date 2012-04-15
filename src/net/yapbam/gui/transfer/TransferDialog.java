package net.yapbam.gui.transfer;

import java.awt.Window;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.GlobalData;
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
		data.add(transferPanel.getTransactions());
		return Boolean.TRUE;
	}

	@Override
	protected String getOkDisabledCause() {
		return transferPanel.getOkDisabledCause();
	}

}
