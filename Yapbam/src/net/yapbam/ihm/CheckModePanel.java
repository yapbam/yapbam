package net.yapbam.ihm;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.ihm.widget.DateWidget;

public class CheckModePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final Cursor CHECK_CURSOR;

	private JLabel statementLabel;
	private JTextField statement;
	private JCheckBox valueDateLabel;
	private DateWidget valueDate;
	private JCheckBox checkModeBox;
	private boolean ok;
	
	private MainFrame frame;

	static {
	    URL imgURL = MainMenuBar.class.getResource("images/checkCursor.png"); //$NON-NLS-1$
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
		CHECK_CURSOR = toolkit.createCustomCursor(toolkit.getImage(imgURL), new Point(5, 13), "checked"); //$NON-NLS-1$
	}

	public CheckModePanel(MainFrame frame) {
		super();
		this.frame = frame;
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
		Cursor cursor = checkModeBox.isSelected() & ok ? CHECK_CURSOR:Cursor.getDefaultCursor();
		frame.getTransactionTable().setCursor(cursor);
	}

	public boolean isSelected() {
		return checkModeBox.isSelected();
	}

	public void check() {
		if (!ok) {
			Toolkit.getDefaultToolkit().beep();
		} else {
			Transaction t = frame.getSelectedTransaction();
			ArrayList<SubTransaction> list = new ArrayList<SubTransaction>(t.getSubTransactionSize());
			for (int i = 0; i < t.getSubTransactionSize(); i++) {
				list.add(t.getSubTransaction(i));
			}
			String statementId = null;
			Date date = t.getValueDate();
			if (t.getStatement()==null) {
				if (valueDateLabel.isSelected()) date = valueDate.getDate();
				statementId = statement.getText();
			}
			Transaction tChecked = new Transaction(t.getDate(), t.getNumber(), t.getDescription(), t.getAmount(), t.getAccount(), t.getMode(), t.getCategory(),
					date, statementId, list);
			frame.getData().remove(t);
			frame.getData().add(tChecked);
		}
	}
}
