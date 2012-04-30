package net.yapbam.gui.statementview;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import net.yapbam.gui.LocalizationData;

import java.awt.GridBagConstraints;

public class BalancePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel startBalance;
	private JLabel endBalance;
	private JLabel labelStatement;

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
		GridBagConstraints gbc_labelStatement = new GridBagConstraints();
		gbc_labelStatement.anchor = GridBagConstraints.WEST;
		gbc_labelStatement.gridwidth = 0;
		gbc_labelStatement.insets = new Insets(0, 5, 0, 5);
		gbc_labelStatement.gridx = 0;
		gbc_labelStatement.gridy = 0;
		add(getLabelStatement(), gbc_labelStatement);
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
		getLabelStatement().setVisible(statementId!=null);
	}
	
	private JLabel getLabelStatement() {
		if (labelStatement == null) {
			labelStatement = new JLabel();
			labelStatement.setFont(new Font("Dialog", Font.PLAIN, 14)); //$NON-NLS-1$
		}
		return labelStatement;
	}
}
