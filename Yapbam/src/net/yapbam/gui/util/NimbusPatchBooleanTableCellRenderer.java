package net.yapbam.gui.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/** This class is a workaround for a very weird bug in Swing and Nimbus L&F :
 * <br>Default boolean renderer is broken. Its background desperately remains blank instead of using the alternate Nimbus background.
 * @see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6723524">bug id 6723524</a>
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class NimbusPatchBooleanTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	private JCheckBox renderer;

	/** Constructor.
	 */
	public NimbusPatchBooleanTableCellRenderer() {
		renderer = new JCheckBox();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Boolean b = (Boolean) value;
		if (b != null) {
			renderer.setSelected(b);
		}
		if (isSelected) {
			renderer.setForeground(table.getSelectionForeground());
			renderer.setBackground(table.getSelectionBackground());
		} else {
			// Call super in order to have background color initialized
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			Color bg = getBackground();
			renderer.setForeground(getForeground());
			// We have to create a new color object because Nimbus returns
			// a color of type DerivedColor, which behaves strange, not sure why.
			renderer.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
			renderer.setOpaque(true);
		}
		return renderer;
	}
}
