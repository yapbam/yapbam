package net.yapbam.gui.transactiontable;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.DateWidgetPanel;

public class CheckModePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel statementLabel;
	private JTextField statement;
	private JCheckBox valueDateLabel;
	private DateWidgetPanel valueDate;
	private JCheckBox checkModeBox;
	private boolean ok;
	
	private TransactionTable table;

	public CheckModePanel(TransactionTable table) {
		super();
		this.table = table;
		
		KeyListener listener = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				refreshOk();
			}
		};
        checkModeBox = new JCheckBox(LocalizationData.get("CheckModePanel.title")); //$NON-NLS-1$
        checkModeBox.setToolTipText(LocalizationData.get("CheckModePanel.title.tooltip")); //$NON-NLS-1$
        checkModeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				refresh();
			}
		});
        add(checkModeBox);
        statementLabel = new JLabel(LocalizationData.get("TransactionDialog.statement")); //$NON-NLS-1$
		add(statementLabel);
        statement = new JTextField(5);
        statement.addKeyListener(listener);
        statement.setToolTipText(LocalizationData.get("CheckModePanel.statement.tooltip")); //$NON-NLS-1$
        add(statement);
        valueDateLabel = new JCheckBox(LocalizationData.get("CheckModePanel.valueDateEnabled")); //$NON-NLS-1$
        valueDateLabel.setToolTipText(LocalizationData.get("CheckModePanel.valueDateEnabled.toolTip")); //$NON-NLS-1$
        valueDateLabel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				refreshOk();
			}
		});
		add(valueDateLabel);
		valueDate = new DateWidgetPanel();
		valueDate.setLocale(LocalizationData.getLocale());
        valueDate.setToolTipText(LocalizationData.get("CheckModePanel.valueDate.tooltip")); //$NON-NLS-1$
        valueDate.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				refreshOk();
			}
		});
        add(valueDate);
        
        setSelected(false);
	}
	
	public void setSelected(boolean selected) {
		checkModeBox.setSelected(selected);
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
		boolean selected = checkModeBox.isSelected();
		boolean dateOk = (!valueDateLabel.isSelected()) || (valueDate.getDate()!=null);
		boolean statementOk = !statement.getText().trim().isEmpty();
		valueDateLabel.setForeground(!selected || dateOk ? Color.black : Color.red);
		statementLabel.setForeground(!selected || statementOk ? Color.black : Color.red);
		this.ok = selected && dateOk && statementOk;
		table.setCheckMode (ok);
	}

	public boolean isSelected() {
		return checkModeBox.isSelected();
	}
	
	public String getStatement() {
		return statement.getText();
	}

	public Date getValueDate() {
		return valueDateLabel.isSelected()?valueDate.getDate():null;
	}
	
	public boolean isOk() {
		return this.ok;
	}
}
