package net.astesana.comptes.ihm.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.astesana.comptes.date.helpers.DateStepper;
import net.astesana.comptes.date.helpers.DeferredValueDateComputer;
import net.astesana.comptes.date.helpers.DayDateStepper;
import net.astesana.comptes.ihm.widget.AutoSelectFocusListener;
import net.astesana.comptes.ihm.widget.IntegerWidget;

/** This panel allows to create or modify a valueDateComputer */
class ModePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	
	private JComboBox combo;
	private JPanel relativePanel;
	private JPanel deferedPanel;
	private JPanel emptyPanel;
	private Component optionalComponent;
	private IntegerWidget relField;
	private IntegerWidget stopField;
	private IntegerWidget debtField;
	private JLabel comboLabel;
	private JLabel stopLabel;
	private JLabel debtLabel;
	private JLabel relLabel;
	private JCheckBox isSelectedBox;

	/** Constructor
	 * @param title the panel title, displayed in the border
	 * @param option an optional component, displayed at the bottom of the panel (null if none)
	 */
	ModePanel(String title, Component option, final AbstractDialog dialog) {
        super(new GridBagLayout());
        
        FocusListener focusListener = new AutoSelectFocusListener();
        KeyListener keyListener = new AutoUpdateOkButtonKeyListener(dialog);
        Insets insets = new Insets(5,5,5,5);
        this.setBorder(BorderFactory.createTitledBorder(""));

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER; c.insets=insets; c.anchor=GridBagConstraints.WEST;
        isSelectedBox = new JCheckBox(title);
        isSelectedBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				boolean ok = (e.getStateChange() == ItemEvent.SELECTED);
				combo.setEnabled(ok);
				relField.setEnabled(ok);
				stopField.setEnabled(ok);
				debtField.setEnabled(ok);
				comboLabel.setEnabled(ok);
				stopLabel.setEnabled(ok);
				debtLabel.setEnabled(ok);
				relLabel.setEnabled(ok);
				if (optionalComponent!=null) optionalComponent.setEnabled(ok);
				firePropertyChange("IS_VALID", !ok, ok);
			}});
		this.add(isSelectedBox, c);

		c = new GridBagConstraints();
		c.gridy=1; c.anchor=GridBagConstraints.WEST; c.insets=insets; c.gridwidth=1;
		comboLabel = new JLabel("Date de valeur :");
		comboLabel.setEnabled(false);
		this.add(comboLabel,c);
		combo = new JComboBox(new String[]{"Imm�diate","Relative","Diff�r�e"});
		combo.setEditable(false);
		combo.setEnabled(false);
		combo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int index = combo.getSelectedIndex();
				emptyPanel.setVisible((index==0));
				relativePanel.setVisible((index==1));
				deferedPanel.setVisible(index==2);
				dialog.updateOkButtonEnabled();
			}});
		c = new GridBagConstraints(); c.gridy=1; c.gridx=1; c.weightx=1;
		this.add(combo, c);
		
		relativePanel = new JPanel(new GridBagLayout());
        if (DEBUG) relativePanel.setBorder(BorderFactory.createTitledBorder("relativePanel"));
        GridBagConstraints c2 = new GridBagConstraints(); c2.insets=insets; c2.anchor=GridBagConstraints.WEST;
        relLabel = new JLabel("Nombre de jours :");
		relativePanel.add(relLabel, c2);
        c2.gridx=1; c2.weightx=1.0;
        relField = new IntegerWidget();
        relField.addFocusListener(focusListener);
        relField.addKeyListener(keyListener);
        relField.setColumns(2);
		relativePanel.add(relField, c2);
        relativePanel.setVisible(false);
        
		deferedPanel = new JPanel(new GridBagLayout());
        if (DEBUG) deferedPanel.setBorder(BorderFactory.createTitledBorder("defferedPanel"));
        c2 = new GridBagConstraints(); c2.weightx=1.0; c2.insets=insets;
        stopLabel = new JLabel("Arr�t le :");
		deferedPanel.add(stopLabel, c2);
        c2.gridx=1;
        stopField = new IntegerWidget(1, 31);
        stopField.addFocusListener(focusListener);
        stopField.addKeyListener(keyListener);
        stopField.setColumns(2);
		deferedPanel.add(stopField, c2);
        c2.gridx=2;
        debtLabel = new JLabel("D�bit le :");
		deferedPanel.add(debtLabel, c2);
		debtField = new IntegerWidget(1, 31);
		debtField.addFocusListener(focusListener);
		debtField.addKeyListener(keyListener);
        debtField.setColumns(2);
        c2.gridx=3;
        deferedPanel.add(debtField, c2);
        deferedPanel.setVisible(false);
        
		emptyPanel = new JPanel();
        if (DEBUG) emptyPanel.setBorder(BorderFactory.createTitledBorder("emptyPanel"));

		homogeneizePreferedSize(new JPanel[]{emptyPanel,relativePanel,deferedPanel});
		c.gridy = 2; c.gridx=0; c.gridwidth=GridBagConstraints.REMAINDER; c.fill=GridBagConstraints.HORIZONTAL;
		this.add(relativePanel, c);
		this.add(deferedPanel, c);
        this.add(emptyPanel, c);

        if (option!=null) {
        	c=new GridBagConstraints(); c.insets = insets; c.gridy=3; c.gridwidth=GridBagConstraints.REMAINDER; c.anchor=GridBagConstraints.WEST;
        	this.add(option,c);
        	this.optionalComponent = option;
        	option.setEnabled(false);
        }	        
	}
	
	DateStepper getValueDateComputer() {
		if (isSelectedBox.isSelected()) {
			int index = combo.getSelectedIndex();
			if (index==0) {
				return DateStepper.IMMEDIATE;
			} else if (index==1) {
				return new DayDateStepper(relField.getValue());
			} else if (index==2) {
				return new DeferredValueDateComputer(stopField.getValue(), debtField.getValue());
			} else {
				throw new RuntimeException();
			}
		} else {
			return null;
		}	
	}

	private void homogeneizePreferedSize(JPanel[] panels) {
		int width = 0;
		int height = 0;
		for (int i = 0; i < panels.length; i++) {
			Dimension d = panels[i].getPreferredSize();
			width = Math.max(width, d.width);
			height = Math.max(height, d.height);
		}
		Dimension d = new Dimension(width,height);
		for (int i = 0; i < panels.length; i++) {
			panels[i].setPreferredSize(d);
		}
	}

	public boolean isSelected() {
		return this.isSelectedBox.isSelected();
	}

	public boolean hasValidContent() {
		if (!this.isSelected()) return true;
		int index = combo.getSelectedIndex();
		if (index==1) {
			return relField.getValue()!=null;
		} else if (index==2) {
			return (stopField.getValue()!=null) && (debtField.getValue()!=null);
		}
		return true;
	}
}
