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
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;

import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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

	/**
	 * Create the panel.
	 */
	public BalancePanel() {
		initialize();
	}
	private void initialize() {
		setLayout(new GridBagLayout());
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
		startBalance.setFont(new Font("Dialog", Font.PLAIN, 14)); //$NON-NLS-1$
		endBalance = new JLabel();
		endBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		endBalance.setFont(new Font("Dialog", Font.PLAIN, 14)); //$NON-NLS-1$
		
		panel = new JPanel();
		panel.setOpaque(false);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.gridwidth = 2;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc_labelStatement = new GridBagConstraints();
		gbc_labelStatement.insets = new Insets(0, 5, 0, 5);
		gbc_labelStatement.anchor = GridBagConstraints.WEST;
		gbc_labelStatement.gridx = 0;
		gbc_labelStatement.gridy = 0;
		panel.add(getLabelStatement(), gbc_labelStatement);
		
		statementField = new TextWidget(8);
		statementField.setFont(new Font("Dialog", Font.BOLD, 14)); //$NON-NLS-1$
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
		
		warningLabel = new JLabel(IconManager.ALERT);
		warningLabel.setText(LocalizationData.get("CheckModePanel.notLast.message")); //$NON-NLS-1$
		warningLabel.setForeground(Color.RED);
		GridBagConstraints gbc_warningLabel = new GridBagConstraints();
		gbc_warningLabel.anchor = GridBagConstraints.EAST;
		gbc_warningLabel.insets = new Insets(0, 10, 0, 5);
		gbc_warningLabel.gridx = 2;
		gbc_warningLabel.gridy = 0;
		panel.add(warningLabel, gbc_warningLabel);
		add(startBalance, gridBagConstraints4);
		add(endBalance, gridBagConstraints6);
	}
	
	public void setStart(String text) {
		startBalance.setText(text);
	}

	public void setEnd(String text) {
		endBalance.setText(text);
	}
	
	public void setStatementId(String statementId) {
		if (statementId!=null) getLabelStatement().setText("<html>"+LocalizationData.get("TransactionDialog.statement").trim()+" <b>"+statementId+"</b></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		setStatementVisible(statementId!=null);
	}
	
	public void setStatementVisible(boolean visible) {
		panel.setVisible(visible);
	}
	
	public void setAlertVisible(boolean visible) {
		warningLabel.setVisible(visible);
	}
	
	private JLabel getLabelStatement() {
		if (labelStatement == null) {
			labelStatement = new JLabel(LocalizationData.get("TransactionDialog.statement")); //$NON-NLS-1$
			labelStatement.setForeground(Color.RED);
			labelStatement.setFont(new Font("Dialog", Font.PLAIN, 14)); //$NON-NLS-1$
		}
		return labelStatement;
	}
	
	public String getEditedStatement() {
		editedStatement = statementField.getText().trim();
		if (editedStatement.isEmpty()) editedStatement = null;
		return editedStatement;
	}
}
