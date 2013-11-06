package net.yapbam.gui.archive;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.Filter;
import net.yapbam.data.GlobalData;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

@SuppressWarnings("serial")
public class FilterDialog extends AbstractDialog<GlobalData, Filter> {

	public FilterDialog(Window owner, String title, GlobalData data) {
		super(owner, title, data);
	}

	@Override
	protected JPanel createCenterPane() {
		return new FilterPanel(this.data);
	}

	@Override
	protected Filter buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
