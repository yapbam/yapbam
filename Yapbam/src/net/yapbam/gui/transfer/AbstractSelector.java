package net.yapbam.gui.transfer;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.widget.CoolJComboBox;

/** An abstract widget composed of a label, a combo box and a new button.
 * <br>It is typically used to select a value in a list of possible values.
 */
public abstract class AbstractSelector<T> extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel labelField;
	private CoolJComboBox combo;
	private JButton newButton;

	/**
	 * Constructor.
	 */
	public AbstractSelector() {
		initialize();
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_labelField = new GridBagConstraints();
		gbc_labelField.insets = new Insets(0, 0, 0, 5);
		gbc_labelField.anchor = GridBagConstraints.WEST;
		gbc_labelField.gridx = 0;
		gbc_labelField.gridy = 0;
		add(getLabelField(), gbc_labelField);
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

		Dimension dimension = getCombo().getPreferredSize();
		getNewButton().setPreferredSize(new Dimension(dimension.height, dimension.height));

		if (getLabel()!=null) getLabelField().setText(getLabel());
		if (getComboTip()!=null) getCombo().setToolTipText(getComboTip());
		if (getNewButtonTip()!=null) getNewButton().setToolTipText(getNewButtonTip());
	}

	private JLabel getLabelField() {
		if (labelField == null) {
			labelField = new JLabel();
		}
		return labelField;
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
	
	int selectedIndex = -1; //TODO
	protected CoolJComboBox getCombo() {
		if (combo == null) {
			combo = new CoolJComboBox();
			combo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = combo.getSelectedIndex();
					if (index != selectedIndex) {
						Object old = combo.getItemAt(selectedIndex);
						selectedIndex = index;
						firePropertyChange(getPropertyName(), old, combo.getItemAt(index));
					} else {
						System.out.println ("We selected the same index");
					}
				}
			});
		}
		return combo;
	}
	
	protected void createNew() {
	}
	
	protected String getLabel() {
		return null;
	}
	
	protected String getComboTip() {
		return null;
	}
	
	protected String getNewButtonTip() {
		return null;
	}
	
	protected abstract String getPropertyName(); 
	
	@SuppressWarnings("unchecked")
	public T get() {
		return (T)getCombo().getSelectedItem();
	}
	
	public void set(T value) {
		Object oldValue = this.get();
		if (!value.equals(oldValue)) {
			getCombo().setSelectedItem(value);
			this.firePropertyChange(getPropertyName(), oldValue, value);
		}
	}
}
