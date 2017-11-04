package net.yapbam.gui.administration;

import javax.swing.JPanel;

import net.yapbam.gui.filter.CustomFilterPanel;
import javax.swing.JLabel;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

public class FilterNamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private TextWidget nameField;
	private CustomFilterPanel filterPanel;
	/**
	 * Create the panel.
	 */
	public FilterNamePanel() {
		this(null);
	}

	public FilterNamePanel(CustomFilterPanel filterPanel) {
		this.filterPanel = filterPanel;
		add(new JLabel("Name:"));
		add(getNameField());
	}

	TextWidget getNameField() {
		if (nameField == null) {
			nameField = new TextWidget();
			nameField.setColumns(10);
			if (filterPanel!=null && filterPanel.getFilter().getName()!=null) {
				nameField.setName(filterPanel.getFilter().getName());
			}
		}
		return nameField;
	}
}
