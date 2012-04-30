package net.yapbam.gui.statementview;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.astesana.ajlib.swing.widget.TextWidget;
import net.astesana.ajlib.swing.widget.date.DateWidget;
import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class CheckModePanel extends JPanel {
	public static final String IS_OK_PROPERTY = "isOk"; //$NON-NLS-1$
	public static final String EDITED_STATEMENT_PROPERTY = "editedStatement"; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;

	private JLabel statementLabel;
	private TextWidget statement;
	private JCheckBox valueDateLabel;
	private DateWidget valueDate;
	private JCheckBox checkModeBox;
	private boolean ok;
	private String editedStatement;
	
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
				refreshEditedStatement();
			}
		});
		GridBagConstraints gbc_checkModeBox = new GridBagConstraints();
		gbc_checkModeBox.anchor = GridBagConstraints.WEST;
		gbc_checkModeBox.insets = new Insets(0, 0, 0, 5);
		gbc_checkModeBox.gridx = 0;
		gbc_checkModeBox.gridy = 0;
		add(checkModeBox, gbc_checkModeBox);
		statementLabel = new JLabel(LocalizationData.get("TransactionDialog.statement")); //$NON-NLS-1$
		GridBagConstraints gbc_statementLabel = new GridBagConstraints();
		gbc_statementLabel.insets = new Insets(0, 0, 0, 5);
		gbc_statementLabel.gridx = 1;
		gbc_statementLabel.gridy = 0;
		add(statementLabel, gbc_statementLabel);
		statement = new TextWidget(5);
		GridBagConstraints gbc_statement = new GridBagConstraints();
		gbc_statement.anchor = GridBagConstraints.WEST;
		gbc_statement.gridx = 2;
		gbc_statement.gridy = 0;
		add(statement, gbc_statement);
		statement.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				refreshEditedStatement();
				refreshOk();
			}
		});
		statement.addFocusListener(AutoSelectFocusListener.INSTANCE);
		statement.setToolTipText(LocalizationData.get("CheckModePanel.statement.tooltip")); //$NON-NLS-1$
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 0;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		valueDateLabel = new JCheckBox(LocalizationData.get("CheckModePanel.valueDateEnabled")); //$NON-NLS-1$
		GridBagConstraints gbc_valueDateLabel = new GridBagConstraints();
		gbc_valueDateLabel.anchor = GridBagConstraints.WEST;
		gbc_valueDateLabel.insets = new Insets(0, 0, 0, 5);
		gbc_valueDateLabel.gridx = 0;
		gbc_valueDateLabel.gridy = 0;
		panel.add(valueDateLabel, gbc_valueDateLabel);
		valueDateLabel.setToolTipText(LocalizationData.get("CheckModePanel.valueDateEnabled.toolTip")); //$NON-NLS-1$
		valueDate = new DateWidget();
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
		statementLabel.setVisible(selected);
		statement.setVisible(selected);
		valueDateLabel.setVisible(selected);
		valueDate.setVisible(selected);
		refreshOk();
	}

	private void refreshOk() {
		boolean old = this.ok;
		if (isVisible()) {
			boolean selected = checkModeBox.isSelected();
			boolean dateOk = (!valueDateLabel.isSelected()) || (valueDate.getDate()!=null);
			boolean statementOk = !statement.getText().trim().isEmpty();
			valueDateLabel.setForeground(!selected || dateOk ? Color.black : Color.red);
			statementLabel.setForeground(!selected || statementOk ? Color.black : Color.red);
			this.ok = selected && dateOk && statementOk;
		} else {
			this.ok = false;
		}
		if (old!=this.ok) {
			firePropertyChange(IS_OK_PROPERTY, old, this.ok);
		}
	}

	/** Tests wether the check mode is selected or not. 
	 * @return true is check mode is selected.
	 */
	public boolean isSelected() {
		return checkModeBox.isSelected();
	}
	
	public String getStatement() {
		return editedStatement;
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
	
	public String getEditedStatement() {
		return this.editedStatement;
	}
	
	private void refreshEditedStatement() {
		String old = this.editedStatement;
		String edited = statement.getText().trim();
		this.editedStatement = checkModeBox.isSelected() && (edited.length()!=0)? edited: null;
		if (!NullUtils.areEquals(old, editedStatement)) {
//System.out.println (SELECTED_STATEMENT_PROPERTY+" from "+old+" to "+this.lastSelectedStatement); //TODO
			firePropertyChange(EDITED_STATEMENT_PROPERTY, old, this.editedStatement);
		}
	}
}
