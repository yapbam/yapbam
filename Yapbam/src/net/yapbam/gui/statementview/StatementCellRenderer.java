package net.yapbam.gui.statementview;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import net.yapbam.gui.util.CellRenderer;

@SuppressWarnings("serial")
public class StatementCellRenderer extends CellRenderer {
	
	public StatementCellRenderer() {
		super();
	}
	
	@Override
	protected int getAlignment(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowModel, int columnModel) {
		if ((columnModel == 6) || (columnModel == 7)) {
			return SwingConstants.RIGHT;
		} else if (columnModel == 1) {
			return SwingConstants.LEFT;
		} else {
			return SwingConstants.CENTER;
		}
	}
	
}
