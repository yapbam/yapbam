package net.yapbam.gui.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.astesana.ajlib.swing.widget.IntegerWidget;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.date.helpers.DayDateStepper;
import net.yapbam.date.helpers.DeferredValueDateComputer;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;

/** This panel allows to create or modify a valueDateComputer */
class ModePanel extends JPanel {
	static final String IS_SELECTED_PROPERTY = "IS_SELECTED"; //$NON-NLS-1$
	static final String IS_VALID_PROPERTY = "IS_VALID"; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	
	private JComboBox combo;
	private JPanel relativePanel;
	private JPanel deferedPanel;
	private JPanel emptyPanel;
	private JCheckBox checkBook;
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
	 * @param checkBookOption an optional component, displayed at the bottom of the panel (null if none)
	 */
	ModePanel(String title, boolean checkBookOption) {
		super(new GridBagLayout());
		Insets insets = new Insets(5, 5, 5, 5);
		this.setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$

		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = insets;
		c.anchor = GridBagConstraints.WEST;
		isSelectedBox = new JCheckBox(title);
		isSelectedBox.addItemListener(new ItemListener() {
			@Override
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
				if (checkBook!=null) checkBook.setEnabled(ok);
				if (!ok && (checkBook!=null)) checkBook.setSelected(false);
				firePropertyChange(IS_SELECTED_PROPERTY, !ok, ok);
				checkValidity();
			}});
		this.add(isSelectedBox, c);
		
		c = new GridBagConstraints();
		c.gridy=1; c.anchor=GridBagConstraints.WEST; c.insets=insets; c.gridwidth=1;
		comboLabel = new JLabel(LocalizationData.get("TransactionDialog.valueDate")); //$NON-NLS-1$
		comboLabel.setEnabled(false);
		this.add(comboLabel,c);
		combo = new JComboBox(new String[]{LocalizationData.get("ModeDialog.immediate"),LocalizationData.get("ModeDialog.relative"),LocalizationData.get("ModeDialog.deferred")}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		combo.setEditable(false);
		combo.setEnabled(false);
		combo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = combo.getSelectedIndex();
				emptyPanel.setVisible((index==0));
				relativePanel.setVisible((index==1));
				deferedPanel.setVisible(index==2);
				checkValidity();
			}});
		c = new GridBagConstraints(); c.gridy=1; c.gridx=1; c.weightx=1;
		this.add(combo, c);
		
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				checkValidity();
			}
		};

		relativePanel = new JPanel(new GridBagLayout());
		if (DEBUG) relativePanel.setBorder(BorderFactory.createTitledBorder("relativePanel")); //$NON-NLS-1$
		GridBagConstraints c2 = new GridBagConstraints();
		c2.insets = insets;
		c2.anchor = GridBagConstraints.WEST;
		relLabel = new JLabel(LocalizationData.get("ModeDialog.daysNumber")); //$NON-NLS-1$
		relativePanel.add(relLabel, c2);
		c2.gridx = 1;
		c2.weightx = 1.0;
		relField = new IntegerWidget();
		relField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		relField.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, listener);
		relField.setColumns(2);
		relativePanel.add(relField, c2);
		relativePanel.setVisible(false);

		deferedPanel = new JPanel(new GridBagLayout());
		if (DEBUG) deferedPanel.setBorder(BorderFactory.createTitledBorder("defferedPanel")); //$NON-NLS-1$
		c2 = new GridBagConstraints();
		c2.weightx = 1.0;
		c2.insets = insets;
		stopLabel = new JLabel(LocalizationData.get("ModeDialog.stop")); //$NON-NLS-1$
		deferedPanel.add(stopLabel, c2);
		c2.gridx = 1;
		stopField = new IntegerWidget(BigInteger.ONE, BigInteger.valueOf(31));
		stopField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		stopField.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, listener);
		stopField.setColumns(2);
		deferedPanel.add(stopField, c2);
		c2.gridx = 2;
		debtLabel = new JLabel(LocalizationData.get("ModeDialog.debt")); //$NON-NLS-1$
		deferedPanel.add(debtLabel, c2);
		debtField = new IntegerWidget(BigInteger.ONE, BigInteger.valueOf(31));
		debtField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		debtField.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, listener);
		debtField.setColumns(2);
		c2.gridx = 3;
		deferedPanel.add(debtField, c2);
		deferedPanel.setVisible(false);

		emptyPanel = new JPanel();
		if (DEBUG) emptyPanel.setBorder(BorderFactory.createTitledBorder("emptyPanel")); //$NON-NLS-1$

		homogeneizePreferedSize(new JPanel[]{emptyPanel,relativePanel,deferedPanel});
		c.gridy = 2; c.gridx=0; c.gridwidth=GridBagConstraints.REMAINDER; c.fill=GridBagConstraints.HORIZONTAL;
		this.add(relativePanel, c);
		this.add(deferedPanel, c);
		this.add(emptyPanel, c);

		if (checkBookOption) {
			c = new GridBagConstraints();
			c.insets = insets;
			c.gridy = 3;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.anchor = GridBagConstraints.WEST;
			this.checkBook = new JCheckBox(LocalizationData.get("ModeDialog.useCheckBook")); //$NON-NLS-1$
			this.checkBook.setEnabled(false);
			this.add(this.checkBook, c);
		}
	}
	
	DateStepper getValueDateComputer() {
		if (isSelectedBox.isSelected()) {
			int index = combo.getSelectedIndex();
			if (index==0) {
				return DateStepper.IMMEDIATE;
			} else if (index==1) {
				return new DayDateStepper(relField.getValue().intValue(), null);
			} else if (index==2) {
				return new DeferredValueDateComputer(stopField.getValue().intValue(), debtField.getValue().intValue());
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

	boolean lastValidity = true;
	private void checkValidity() {
		boolean isValid = hasValidContent();
		if (lastValidity!=isValid) {
			this.firePropertyChange(IS_VALID_PROPERTY, lastValidity, isValid);
			lastValidity = isValid;
		}
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

	public void setContent(DateStepper vdc) {
		isSelectedBox.setSelected(vdc!=null);
		if (vdc!=null) {
			if (vdc == DateStepper.IMMEDIATE) {
				combo.setSelectedIndex(0);
			} else if (vdc instanceof DayDateStepper) {
				combo.setSelectedIndex(1);
				relField.setValue(((DayDateStepper)vdc).getStep());
			} else if (vdc instanceof DeferredValueDateComputer) {
				combo.setSelectedIndex(2);
				stopField.setValue(((DeferredValueDateComputer)vdc).getStopDay());
				debtField.setValue(((DeferredValueDateComputer)vdc).getDebtDay());
			} else {
				throw new IllegalArgumentException();
			}
		}
	}

	public boolean isCheckBookSelected() {
		return (checkBook!=null) && (checkBook.isSelected());
	}

	public void setCheckBookSelected(boolean selected) {
		checkBook.setSelected(selected);
	}
}
