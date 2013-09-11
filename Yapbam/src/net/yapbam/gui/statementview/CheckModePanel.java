package net.yapbam.gui.statementview;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fathzer.soft.ajlib.swing.widget.date.DateWidget;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class CheckModePanel extends JPanel {
	public static final String IS_OK_PROPERTY = "isOk"; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;
	private JCheckBox valueDateLabel;
	private DateWidget valueDate;
	private JCheckBox checkModeBox;
	private boolean ok;
	
	private JPanel panel;

	public CheckModePanel() {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		checkModeBox = new JCheckBox(LocalizationData.get("CheckModePanel.title")); //$NON-NLS-1$
		checkModeBox.setToolTipText(LocalizationData.get("CheckModePanel.title.tooltip")); //$NON-NLS-1$
		checkModeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				refresh();
			}
		});
		GridBagConstraints gbc_checkModeBox = new GridBagConstraints();
		gbc_checkModeBox.anchor = GridBagConstraints.WEST;
		gbc_checkModeBox.insets = new Insets(0, 0, 0, 5);
		gbc_checkModeBox.gridx = 0;
		gbc_checkModeBox.gridy = 0;
		add(checkModeBox, gbc_checkModeBox);
		
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 0;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		valueDateLabel = new JCheckBox(LocalizationData.get("CheckModePanel.valueDateEnabled")); //$NON-NLS-1$
		GridBagConstraints gbc_valueDateLabel = new GridBagConstraints();
		gbc_valueDateLabel.anchor = GridBagConstraints.WEST;
		gbc_valueDateLabel.insets = new Insets(0, 0, 0, 5);
		gbc_valueDateLabel.gridx = 0;
		gbc_valueDateLabel.gridy = 0;
		panel.add(valueDateLabel, gbc_valueDateLabel);
		valueDateLabel.setToolTipText(LocalizationData.get("CheckModePanel.valueDateEnabled.toolTip")); //$NON-NLS-1$
		valueDate = new DateWidget();
		valueDate.getDateField().setMinimumSize(valueDate.getDateField().getPreferredSize());
		GridBagConstraints gbc_valueDate = new GridBagConstraints();
		gbc_valueDate.gridx = 1;
		gbc_valueDate.gridy = 0;
		panel.add(valueDate, gbc_valueDate);
		valueDate.setDate(null);
		valueDate.setLocale(LocalizationData.getLocale());
		valueDate.setToolTipText(LocalizationData.get("CheckModePanel.valueDate.tooltip")); //$NON-NLS-1$
		valueDate.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				valueDateLabel.setSelected(true);
				refreshOk();
			}
		});
		valueDate.getDateField().addFocusListener(AutoSelectFocusListener.INSTANCE);
		valueDateLabel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				refreshOk();
			}
		});
		refresh();
	}
	
	private void refresh() {
		boolean selected = checkModeBox.isSelected();
		valueDateLabel.setVisible(selected);
		valueDate.setVisible(selected);
		refreshOk();
	}

	private void refreshOk() {
		boolean old = this.ok;
		if (isVisible()) {
			boolean selected = checkModeBox.isSelected();
			boolean dateOk = (!valueDateLabel.isSelected()) || (valueDate.getDate()!=null);
			valueDateLabel.setForeground(!selected || dateOk ? Color.black : Color.red);
			this.ok = selected && dateOk;
		} else {
			this.ok = false;
		}
		if (old!=this.ok) {
			firePropertyChange(IS_OK_PROPERTY, old, this.ok);
		}
	}

	/** Tests whether the check mode is selected or not. 
	 * @return true is check mode is selected.
	 */
	public boolean isSelected() {
		return checkModeBox.isSelected();
	}
	
	public void setSelected(boolean selected) {
		checkModeBox.setSelected(selected);
	}
	
	public Date getValueDate() {
		return valueDateLabel.isSelected()?valueDate.getDate():null;
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		refreshOk();
	}
	
	public boolean isOk() {
		return this.ok;
	}
}
