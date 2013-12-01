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
		GridBagConstraints gbcCheckModeBox = new GridBagConstraints();
		gbcCheckModeBox.anchor = GridBagConstraints.WEST;
		gbcCheckModeBox.insets = new Insets(0, 0, 0, 5);
		gbcCheckModeBox.gridx = 0;
		gbcCheckModeBox.gridy = 0;
		add(checkModeBox, gbcCheckModeBox);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.gridwidth = 0;
		gbcPanel.insets = new Insets(0, 0, 0, 5);
		gbcPanel.anchor = GridBagConstraints.NORTH;
		gbcPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 1;
		add(panel, gbcPanel);
		valueDateLabel = new JCheckBox(LocalizationData.get("CheckModePanel.valueDateEnabled")); //$NON-NLS-1$
		GridBagConstraints gbcValueDateLabel = new GridBagConstraints();
		gbcValueDateLabel.anchor = GridBagConstraints.WEST;
		gbcValueDateLabel.insets = new Insets(0, 0, 0, 5);
		gbcValueDateLabel.gridx = 0;
		gbcValueDateLabel.gridy = 0;
		panel.add(valueDateLabel, gbcValueDateLabel);
		valueDateLabel.setToolTipText(LocalizationData.get("CheckModePanel.valueDateEnabled.toolTip")); //$NON-NLS-1$
		valueDate = new DateWidget();
		valueDate.getDateField().setMinimumSize(valueDate.getDateField().getPreferredSize());
		GridBagConstraints gbcValueDate = new GridBagConstraints();
		gbcValueDate.gridx = 1;
		gbcValueDate.gridy = 0;
		panel.add(valueDate, gbcValueDate);
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
