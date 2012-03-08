package net.yapbam.gui.transfer;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import net.yapbam.gui.IconManager;

public class AbstractSelector<T> extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JComboBox<T> combo;
	private JButton newButton;

	/**
	 * Create the panel.
	 */
	public AbstractSelector(String label, String tipCombo, String tipButton) {
		initialize(label, tipCombo, tipButton);
	}
	
	private void initialize(String label, String tipCombo, String tipButton) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		add(getLabel(), gbc_label);
		GridBagConstraints gbc_combo = new GridBagConstraints();
		gbc_combo.weightx = 1.0;
		gbc_combo.fill = GridBagConstraints.HORIZONTAL;
		gbc_combo.insets = new Insets(0, 0, 0, 5);
		gbc_combo.gridx = 1;
		gbc_combo.gridy = 0;
		add(getCombo(), gbc_combo);
		GridBagConstraints gbc_newButton = new GridBagConstraints();
		gbc_newButton.gridx = 2;
		gbc_newButton.gridy = 0;
		add(getNewButton(), gbc_newButton);
		
		if (label!=null) getLabel().setText(label);
		if (tipCombo!=null) getCombo().setToolTipText(tipCombo);
		if (tipButton!=null) getNewButton().setToolTipText(tipButton);
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel();
		}
		return label;
	}
	
	protected JComboBox<T> getCombo() {
		if (combo == null) {
			combo = new JComboBox<T>();
		}
		return combo;
	}
	
	private JButton getNewButton() {
		if (newButton == null) {
			newButton = new JButton();
			newButton = new JButton(IconManager.NEW_ACCOUNT);
			newButton.setFocusable(false);
			newButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					createNew();
				}
			});
		}
		return newButton;
	}
	
	protected void createNew() {
	}
}
