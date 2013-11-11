package net.yapbam.gui.archive;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

@SuppressWarnings("serial")
public class FilterDialog extends AbstractDialog<GlobalData, String[]> {
	private FilterPanel panel;

	public FilterDialog(Window owner, GlobalData data) {
		super(owner, "Archive", data);
	}

	@Override
	protected JPanel createCenterPane() {
		panel = new FilterPanel(this.data);
		panel.addPropertyChangeListener(FilterPanel.INVALIDITY_CAUSE, new AutoUpdateOkButtonPropertyListener(this));
		return panel;
	}
	
	@Override
	protected String getOkDisabledCause() {
		return panel.getInvalidityCause();
	}

	@Override
	protected String[] buildResult() {
		AccountTableModel model = ((AccountTableModel)panel.getTable().getModel());
		String[] result = new String[model.getRowCount()];
		for (int i = 0; i < result.length; i++) {
			result[i] = model.getSelectedStatement(i);
		}
		return result;
	}

}
