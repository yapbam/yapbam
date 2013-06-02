package net.yapbam.gui.statementview;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import net.astesana.ajlib.swing.widget.TextWidget;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.CurrencyWidget;

import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.MessageFormat;

import javax.swing.JTextField;

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
	private JLabel lblTargetBalance;
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
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.EAST;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridwidth = 0;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc_labelStatement = new GridBagConstraints();
		gbc_labelStatement.insets = new Insets(0, 5, 0, 0);
		gbc_labelStatement.anchor = GridBagConstraints.WEST;
		gbc_labelStatement.gridx = 0;
		gbc_labelStatement.gridy = 0;
		panel.add(getLabelStatement(), gbc_labelStatement);
		
		statementField = new TextWidget(8);
		statementField.setFont(this.bigFont);
		statementField.setMinimumSize(statementField.getPreferredSize());
		statementField.setToolTipText(LocalizationData.get("CheckModePanel.statement.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_statementField = new GridBagConstraints();
		gbc_statementField.insets = new Insets(0, 5, 0, 0);
		gbc_statementField.fill = GridBagConstraints.HORIZONTAL;
		gbc_statementField.gridx = 1;
		gbc_statementField.gridy = 0;
		panel.add(statementField, gbc_statementField);
		statementField.setColumns(10);
		statementField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String old = editedStatement;
				editedStatement = statementField.getText().trim();
				if (editedStatement.isEmpty()) editedStatement = null;
				getLabelStatement().setForeground(editedStatement!=null ? Color.black : Color.red);
				firePropertyChange(EDITED_STATEMENT_PROPERTY, old, editedStatement);
			}
		});
		
		warningLabel = new JLabel(IconManager.get(Name.ALERT));
		warningLabel.setText(LocalizationData.get("CheckModePanel.notLast.message")); //$NON-NLS-1$
		warningLabel.setForeground(Color.RED);
		GridBagConstraints gbc_warningLabel = new GridBagConstraints();
		gbc_warningLabel.anchor = GridBagConstraints.EAST;
		gbc_warningLabel.insets = new Insets(0, 10, 0, 5);
		gbc_warningLabel.gridx = 2;
		gbc_warningLabel.gridy = 0;
		panel.add(warningLabel, gbc_warningLabel);
		
		lblTargetBalance = new JLabel(LocalizationData.get("CheckModePanel.target")); //$NON-NLS-1$
		GridBagConstraints gbc_lblTargetBalance = new GridBagConstraints();
		gbc_lblTargetBalance.weightx = 1.0;
		gbc_lblTargetBalance.anchor = GridBagConstraints.EAST;
		gbc_lblTargetBalance.insets = new Insets(0, 5, 0, 5);
		gbc_lblTargetBalance.gridx = 3;
		gbc_lblTargetBalance.gridy = 0;
		panel.add(lblTargetBalance, gbc_lblTargetBalance);
		
		targetAmount = new CurrencyWidget();
		targetAmount.setColumns(8);
		targetAmount.setToolTipText(LocalizationData.get("CheckModePanel.target.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_targetAmount = new GridBagConstraints();
		gbc_targetAmount.anchor = GridBagConstraints.EAST;
		gbc_targetAmount.insets = new Insets(0, 0, 0, 5);
		gbc_targetAmount.fill = GridBagConstraints.HORIZONTAL;
		gbc_targetAmount.gridx = 4;
		gbc_targetAmount.gridy = 0;
		panel.add(targetAmount, gbc_targetAmount);
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
		GridBagConstraints gbc_gapLabel = new GridBagConstraints();
		gbc_gapLabel.insets = new Insets(0, 0, 0, 5);
		gbc_gapLabel.anchor = GridBagConstraints.EAST;
		gbc_gapLabel.gridx = 2;
		gbc_gapLabel.gridy = 1;
		add(gapLabel, gbc_gapLabel);
	}
	
	private void setStart(String text) {
		startBalance.setText(text);
	}

	private void setEnd(String text) {
		endBalance.setText(text);
	}
	
	public void setCheckMode(boolean checkMode) {
		panel.setVisible(checkMode);
		if (!checkMode) targetAmount.setValue(null);
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
		if (editedStatement.isEmpty()) editedStatement = null;
		return editedStatement;
	}
	
	public void setStatement(Statement statement) {
		this.statement = statement;
		updateBalances();
	}

	private void updateBalances() {
		DecimalFormat ci = LocalizationData.getCurrencyInstance();
		setStart(MessageFormat.format(LocalizationData.get("StatementView.startBalance"), statement!=null?ci.format(statement.getStartBalance()):"")); //$NON-NLS-1$ //$NON-NLS-2$
		setEnd(MessageFormat.format(LocalizationData.get("StatementView.endBalance"), statement!=null?ci.format(statement.getEndBalance()):"")); //$NON-NLS-1$ //$NON-NLS-2$
		
		Double target = targetAmount.getValue();
		if ((statement==null) || (target==null) || (GlobalData.AMOUNT_COMPARATOR.compare(target,statement.getEndBalance())==0)) {
			gapLabel.setVisible(false);
		} else {
			gapLabel.setText(MessageFormat.format(LocalizationData.get("CheckModePanel.gap"),ci.format(target-statement.getEndBalance()))); //$NON-NLS-1$
			gapLabel.setVisible(true);
		}
	}
}
