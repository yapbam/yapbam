package net.yapbam.gui.filter;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

import net.yapbam.data.Filter;
import net.yapbam.gui.LocalizationData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;

public class SavePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final String NAME_FIELD_TOOLTIP = LocalizationData.get("CustomFilterPanel.save.nameField.tooltip"); //$NON-NLS-1$
	private static final String SAVE_BUTTON_NAME = LocalizationData.get("CustomFilterPanel.save.saveButton.title"); //$NON-NLS-1$
	private static final String SAVE_BUTTON_ENABLED_TOOLTIP = LocalizationData.get("CustomFilterPanel.save.saveButton.tooltip"); //$NON-NLS-1$
	private static final String SAVE_BUTTON_DISABLED_TOOLTIP = LocalizationData.get("CustomFilterPanel.save.saveButton.disabled.tooltip"); //$NON-NLS-1$
	
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
		
		add(new JLabel(LocalizationData.get("CustomFilterPanel.error.filterNameField.title"))); //$NON-NLS-1$
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
				if (JOptionPane.showConfirmDialog(this, LocalizationData.get("CustomFilterPanel.save.overwrite.question"), null, JOptionPane.YES_NO_OPTION)!=0) {//$NON-NLS-1$
					return;
				}
			}
			filterPanel.apply(editedFilter);
			if (newOne) {
				filterPanel.getData().add(editedFilter);
			} else {
				filterPanel.getData().getFilter(name).copy(editedFilter);
			}
		}
	}
}
