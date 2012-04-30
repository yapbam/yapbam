package net.yapbam.gui.statementview;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;

public class BalancePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel startBalance;
	private JLabel endBalance;

	/**
	 * Create the panel.
	 */
	public BalancePanel() {
		initialize();
	}
	private void initialize() {
		setLayout(new GridBagLayout());
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 1;
		gridBagConstraints6.anchor = GridBagConstraints.EAST;
		gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.weightx = 1.0D;
		gridBagConstraints6.gridy = 0;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.weightx = 1.0D;
		gridBagConstraints4.gridy = 0;
		startBalance = new JLabel();
		startBalance.setHorizontalTextPosition(SwingConstants.LEADING);
		startBalance.setFont(new Font("Dialog", Font.PLAIN, 14)); //$NON-NLS-1$
		endBalance = new JLabel();
		endBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		endBalance.setFont(new Font("Dialog", Font.PLAIN, 14)); //$NON-NLS-1$
		add(startBalance, gridBagConstraints4);
		add(endBalance, gridBagConstraints6);
	}
	
	public void setStart(String text) {
		startBalance.setText(text);
	}

	
	public void setEnd(String text) {
		endBalance.setText(text);
	}
}
