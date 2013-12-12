package net.yapbam.gui.statementview;

import java.awt.Window;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.BasicInputDialog;

public class StatementRenameDialog extends BasicInputDialog<GlobalData, String> {
	private static final long serialVersionUID = 1L;

	StatementRenameDialog(Window owner, GlobalData data) {
		super(owner, LocalizationData.get("StatementDialog.title"), data); //$NON-NLS-1$
	}
	
	@Override
	protected String getLabel() {
		return LocalizationData.get("TransactionDialog.statement"); //$NON-NLS-1$
	}

	@Override
	protected String getTooltip() {
		return LocalizationData.get("StatementDialog.tooltip"); //$NON-NLS-1$
	}

	@Override
	protected String buildResult() {
		return this.getField().getText().trim();
	}

	@Override
	protected String getOkDisabledCause() {
		String name = this.getField().getText().trim();
		if (name.length()==0) {
			return LocalizationData.get("StatementDialog.err"); //$NON-NLS-1$
		}
		return null;
	}
}
