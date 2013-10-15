package net.yapbam.gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;

import com.fathzer.soft.ajlib.swing.table.CustomCellRenderer;

import net.yapbam.gui.LocalizationData;

/** Statement view cell renderer.
 * @author Jean-Marc Astesana (Licence GPL v3)
 */
public class CellRenderer extends CustomCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	protected Object getValue(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowModel, int columnModel) {
		if (value instanceof Date) {
			value=SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, LocalizationData.getLocale()).format(value);
		} else if (value instanceof Double) {
			value = LocalizationData.getCurrencyInstance().format(value);
		}
		return value;
	}
}
