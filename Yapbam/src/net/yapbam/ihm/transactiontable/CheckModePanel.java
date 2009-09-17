package net.yapbam.ihm.transactiontable;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.widget.DateWidget;

public class CheckModePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel statementLabel;
	private JTextField statement;
	private JCheckBox valueDateLabel;
	private DateWidget valueDate;
	private JCheckBox checkModeBox;
	private boolean ok;
	
	private TransactionTable table;

	public CheckModePanel(TransactionTable table) {
		super();
		this.table = table;
		this.ok = false;
		
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
		add(valueDateLabel);
		valueDate = new DateWidget();
		valueDate.addKeyListener(listener);
        valueDate.setToolTipText(LocalizationData.get("CheckModePanel.valueDate.tooltip")); //$NON-NLS-1$
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
		boolean dateOk = (valueDate.getDate()!=null);
		boolean statementOk = !statement.getText().trim().isEmpty();
		valueDateLabel.setForeground(!selected || dateOk ? Color.black : Color.red);
		statementLabel.setForeground(!selected || statementOk ? Color.black : Color.red);
		this.ok = selected && dateOk && statementOk;
		table.setCheckMode ((isSelected()&&ok));
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
}
