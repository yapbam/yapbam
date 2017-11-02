package net.yapbam.gui.filter;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

import net.yapbam.data.Filter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;

public class SavePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final String NAME_FIELD_TOOLTIP = "If you wish to save this filter for later use, enter a name here and click the {0} button.";
	private static final String SAVE_BUTTON_NAME = "Save";
	private static final String SAVE_BUTTON_ENABLED_TOOLTIP = "Click this button to save this filter";
	private static final String SAVE_BUTTON_DISABLED_TOOLTIP = "This button is disabled because name is empty";
	
	private CustomFilterPanel filterPanel;
	
	private TextWidget textField;
	private JButton btnSave;

	/**
	 * Create the panel.
	 */
	public SavePanel() {
		this(null);
	}

	public SavePanel(CustomFilterPanel filterPanel) {
		this.filterPanel = filterPanel;
		
		add(new JLabel("Save as"));
		add(getNameField());
		add(getSaveBtn());
	}

	private TextWidget getNameField() {
		if (textField == null) {
			textField = new TextWidget();
			textField.setColumns(10);
			textField.setToolTipText(Formatter.format(NAME_FIELD_TOOLTIP, SAVE_BUTTON_NAME));
			textField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					boolean isOk = isValidName(textField.getText());
					getSaveBtn().setEnabled(isOk);
					getSaveBtn().setToolTipText(isOk ? SAVE_BUTTON_ENABLED_TOOLTIP : SAVE_BUTTON_DISABLED_TOOLTIP);
				}

				private boolean isValidName(String text) {
					return !text.isEmpty();
				}
			});
		}
		return textField;
	}
	
	private JButton getSaveBtn() {
		if (btnSave == null) {
			btnSave = new JButton(SAVE_BUTTON_NAME);
			btnSave.setToolTipText(SAVE_BUTTON_DISABLED_TOOLTIP);
			btnSave.setEnabled(false);
			btnSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doSave();
				}
			});
		}
		return btnSave;
	}

	private void doSave() {
		String name = getNameField().getText();
		if (!name.equals(filterPanel.getFilter().getName())) {
			// We're not updating the original filter
			Filter editedFilter = filterPanel.getData().getFilter(name);
			boolean newOne = editedFilter==null;
			if (newOne) {
				editedFilter = new Filter();
				editedFilter.setName(name);
			} else {
				// Ask the user if it wants to overwrite existing filter
				JOptionPane.showConfirmDialog(this, "There's already a saved filter with this name. Do you want to overwrite it?");
			}
			filterPanel.apply(editedFilter);
			if (newOne) {
				filterPanel.getData().add(editedFilter);
			} else {
				//TODO
			}
		}
	}
}
