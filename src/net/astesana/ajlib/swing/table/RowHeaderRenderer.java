/** This class is copied from http://www.chka.de/swing/table/row-headers/JTable.html.
 * Seems to not be copyrighted ? Hope it isn't ...
 */
package net.astesana.ajlib.swing.table;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;

@SuppressWarnings("serial")
public class RowHeaderRenderer extends DefaultTableCellRenderer {
	protected Border noFocusBorder, focusBorder;
	private boolean hideSelection;

	public RowHeaderRenderer(boolean hideSelection) {
		setOpaque(true);
		setBorder(noFocusBorder);
		this.hideSelection = true;
	}

	@Override
	public void updateUI() {
		super.updateUI();
		Border cell = UIManager.getBorder("TableHeader.cellBorder");
		Border focus = UIManager.getBorder("Table.focusCellHighlightBorder");

		focusBorder = new BorderUIResource.CompoundBorderUIResource(cell, focus);

		Insets i = focus.getBorderInsets(this);

		noFocusBorder = new BorderUIResource.CompoundBorderUIResource(cell, BorderFactory.createEmptyBorder(i.top, i.left, i.bottom, i.right));

		/*
		 * Alternatively, if focus shouldn't be supported: focusBorder =
		 * noFocusBorder = cell;
		 */
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
		if (table != null) {
			if (hideSelection) selected = false;
			if (selected) {
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			} else {
				setBackground(table.getBackground());
				setForeground(table.getForeground());
			}
			setFont(table.getFont());
			setEnabled(table.isEnabled());
		} else {
			setBackground(UIManager.getColor("TableHeader.background"));
			setForeground(UIManager.getColor("TableHeader.foreground"));
			setFont(UIManager.getFont("TableHeader.font"));
			setEnabled(true);
		}

		if (focused) {
			setBorder(focusBorder);
		}	else {
			setBorder(noFocusBorder);
		}
		setValue(value);
		return this;
	}
}