package net.yapbam.gui.statementview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.widget.date.DateWidget;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;

public class ChangeValueDatePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton valueDateLabel;
	private DateWidget valueDate;

	public ChangeValueDatePanel() {
		super();
		setLayout(new GridBagLayout());
		
		valueDateLabel = new JButton(LocalizationData.get("CheckModePanel.valueDateEnabled")); //$NON-NLS-1$
		GridBagConstraints gbc_valueDateLabel = new GridBagConstraints();
		gbc_valueDateLabel.anchor = GridBagConstraints.WEST;
		gbc_valueDateLabel.insets = new Insets(0, 0, 0, 5);
		gbc_valueDateLabel.gridx = 0;
		gbc_valueDateLabel.gridy = 0;
		add(valueDateLabel, gbc_valueDateLabel);
		valueDateLabel.setToolTipText("Cliquez ici pour changer la date de valeur des opérations sélectionnées");
		valueDate = new DateWidget();
		valueDate.getDateField().setMinimumSize(valueDate.getDateField().getPreferredSize());
		GridBagConstraints gbc_valueDate = new GridBagConstraints();
		gbc_valueDate.gridx = 1;
		gbc_valueDate.gridy = 0;
		add(valueDate, gbc_valueDate);
		valueDate.setDate(null);
		valueDate.setLocale(LocalizationData.getLocale());
		valueDate.setToolTipText(LocalizationData.get("CheckModePanel.valueDate.tooltip")); //$NON-NLS-1$
		valueDate.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				refreshOk();
			}
		});
		valueDate.getDateField().addFocusListener(AutoSelectFocusListener.INSTANCE);
		refreshOk();
	}
	
	private void refreshOk() {
		boolean dateOk = valueDate.getDate()!=null;
		valueDateLabel.setEnabled(dateOk);
	}

	public Date getValueDate() {
		return valueDate.getDate();
	}
}
