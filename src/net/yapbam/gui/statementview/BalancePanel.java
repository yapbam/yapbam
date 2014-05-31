package net.yapbam.gui.statementview;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.CurrencyWidget;

import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JTextField;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

public class BalancePanel extends JPanel {
	public static final String EDITED_STATEMENT_PROPERTY = "editedStatement"; //$NON-NLS-1$

	private static final long serialVersionUID = 1L;
	private JLabel startBalance;
	private JLabel endBalance;
	private JLabel labelStatement;
	private JPanel panel;
	private JTextField statementField;
	private JLabel warningLabel;
	private String editedStatement;
	private Font bigFont;
	private CurrencyWidget targetAmount;
	private JLabel gapLabel;
	
	private Statement statement;

	/**
	 * Creates the panel.
	 */
	public BalancePanel() {
		this.statement = null;
		initialize();
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		setBackground(Color.white);
		setBorder(BorderFactory.createMatteBorder(0,0,3,0,Color.gray));
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 1;
		gridBagConstraints6.anchor = GridBagConstraints.EAST;
		gridBagConstraints6.insets = new Insets(0, 5, 0, 5);
		gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.weightx = 1.0D;
		gridBagConstraints6.gridy = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.insets = new Insets(0, 5, 0, 5);
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.weightx = 1.0D;
		gridBagConstraints4.gridy = 1;
		startBalance = new JLabel();
		startBalance.setHorizontalTextPosition(SwingConstants.LEADING);
		this.bigFont = startBalance.getFont().deriveFont((float)14*startBalance.getFont().getSize()/12);
		startBalance.setFont(this.bigFont);
		endBalance = new JLabel();
		endBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		endBalance.setFont(this.bigFont);
		
		panel = new JPanel();
		panel.setOpaque(false);
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.anchor = GridBagConstraints.EAST;
		gbcPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcPanel.gridwidth = 0;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 0;
		add(panel, gbcPanel);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbcLabelStatement = new GridBagConstraints();
		gbcLabelStatement.insets = new Insets(0, 5, 0, 0);
		gbcLabelStatement.anchor = GridBagConstraints.WEST;
		gbcLabelStatement.gridx = 0;
		gbcLabelStatement.gridy = 0;
		panel.add(getLabelStatement(), gbcLabelStatement);
		
		statementField = new TextWidget(8);
		statementField.setFont(this.bigFont);
		statementField.setMinimumSize(statementField.getPreferredSize());
		statementField.setToolTipText(LocalizationData.get("CheckModePanel.statement.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcStatementField = new GridBagConstraints();
		gbcStatementField.insets = new Insets(0, 5, 0, 0);
		gbcStatementField.fill = GridBagConstraints.HORIZONTAL;
		gbcStatementField.gridx = 1;
		gbcStatementField.gridy = 0;
		panel.add(statementField, gbcStatementField);
		statementField.setColumns(10);
		statementField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String old = editedStatement;
				editedStatement = statementField.getText().trim();
				if (editedStatement.isEmpty()) {
					editedStatement = null;
				}
				getLabelStatement().setForeground(editedStatement!=null ? Color.black : Color.red);
				firePropertyChange(EDITED_STATEMENT_PROPERTY, old, editedStatement);
			}
		});
		
		warningLabel = new JLabel(IconManager.get(Name.ALERT));
		warningLabel.setText(LocalizationData.get("CheckModePanel.notLast.message")); //$NON-NLS-1$
		warningLabel.setForeground(Color.RED);
		GridBagConstraints gbcWarningLabel = new GridBagConstraints();
		gbcWarningLabel.anchor = GridBagConstraints.EAST;
		gbcWarningLabel.insets = new Insets(0, 10, 0, 5);
		gbcWarningLabel.gridx = 2;
		gbcWarningLabel.gridy = 0;
		panel.add(warningLabel, gbcWarningLabel);
		
		JLabel lblTargetBalance = new JLabel(LocalizationData.get("CheckModePanel.target")); //$NON-NLS-1$
		GridBagConstraints gbcLblTargetBalance = new GridBagConstraints();
		gbcLblTargetBalance.weightx = 1.0;
		gbcLblTargetBalance.anchor = GridBagConstraints.EAST;
		gbcLblTargetBalance.insets = new Insets(0, 5, 0, 5);
		gbcLblTargetBalance.gridx = 3;
		gbcLblTargetBalance.gridy = 0;
		panel.add(lblTargetBalance, gbcLblTargetBalance);
		
		targetAmount = new CurrencyWidget();
		targetAmount.setColumns(8);
		targetAmount.setToolTipText(LocalizationData.get("CheckModePanel.target.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcTargetAmount = new GridBagConstraints();
		gbcTargetAmount.anchor = GridBagConstraints.EAST;
		gbcTargetAmount.insets = new Insets(0, 0, 0, 5);
		gbcTargetAmount.fill = GridBagConstraints.HORIZONTAL;
		gbcTargetAmount.gridx = 4;
		gbcTargetAmount.gridy = 0;
		panel.add(targetAmount, gbcTargetAmount);
		targetAmount.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateBalances();
			}
		});
		add(startBalance, gridBagConstraints4);
		add(endBalance, gridBagConstraints6);
		
		gapLabel = new JLabel();
		gapLabel.setForeground(Color.RED);
		GridBagConstraints gbcGapLabel = new GridBagConstraints();
		gbcGapLabel.insets = new Insets(0, 0, 0, 5);
		gbcGapLabel.anchor = GridBagConstraints.EAST;
		gbcGapLabel.gridx = 2;
		gbcGapLabel.gridy = 1;
		add(gapLabel, gbcGapLabel);
	}
	
	private void setStart(String text) {
		startBalance.setText(text);
	}

	private void setEnd(String text) {
		endBalance.setText(text);
	}
	
	public void setCheckMode(boolean checkMode) {
		panel.setVisible(checkMode);
		if (!checkMode) {
			targetAmount.setValue(null);
		}
	}
	
	public void setAlertVisible(boolean visible) {
		warningLabel.setVisible(visible);
	}
	
	private JLabel getLabelStatement() {
		if (labelStatement == null) {
			labelStatement = new JLabel(LocalizationData.get("TransactionDialog.statement")); //$NON-NLS-1$
			labelStatement.setForeground(Color.RED);
			labelStatement.setFont(this.bigFont);
		}
		return labelStatement;
	}
	
	public String getEditedStatement() {
		editedStatement = statementField.getText().trim();
		if (editedStatement.isEmpty()) {
			editedStatement = null;
		}
		return editedStatement;
	}
	
	public void setStatement(Statement statement) {
		this.statement = statement;
		updateBalances();
	}

	private void updateBalances() {
		DecimalFormat ci = LocalizationData.getCurrencyInstance();
		setStart(Formatter.format(LocalizationData.get("StatementView.startBalance"), statement!=null?ci.format(statement.getStartBalance()):"")); //$NON-NLS-1$ //$NON-NLS-2$
		setEnd(Formatter.format(LocalizationData.get("StatementView.endBalance"), statement!=null?ci.format(statement.getEndBalance()):"")); //$NON-NLS-1$ //$NON-NLS-2$
		
		Double target = targetAmount.getValue();
		if ((statement==null) || (target==null) || (GlobalData.AMOUNT_COMPARATOR.compare(target,statement.getEndBalance())==0)) {
			gapLabel.setVisible(false);
		} else {
			gapLabel.setText(Formatter.format(LocalizationData.get("CheckModePanel.gap"),ci.format(target-statement.getEndBalance()))); //$NON-NLS-1$
			gapLabel.setVisible(true);
		}
	}
}
