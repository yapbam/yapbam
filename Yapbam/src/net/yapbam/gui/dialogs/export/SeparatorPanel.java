package net.yapbam.gui.dialogs.export;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.CharWidget;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SeparatorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String SEPARATOR_PROPERTY = "separator"; //$NON-NLS-1$
	
	private JRadioButton defaultSeparatorButton = null;
	private JRadioButton customSeparatorButton = null;
	private CharWidget customSeparatorValue = null;
	
	private char defaultSeparator = '\t';
	private char separator = defaultSeparator;

	/**
	 * This is the default constructor
	 */
	public SeparatorPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints2.gridx = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.gridwidth = 0;
		gridBagConstraints.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("ExportDialog.columnSeparator") //$NON-NLS-1$
				, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$
		this.setSize(new Dimension(196, 86));
		this.add(getDefaultSeparatorButton(), gridBagConstraints);
		this.add(getCustomSeparatorButton(), gridBagConstraints1);
		this.add(getCustomSeparatorValue(), gridBagConstraints2);
		
		ButtonGroup group = new ButtonGroup();
		group.add(getDefaultSeparatorButton());
		group.add(getCustomSeparatorButton());
	}

	/**
	 * This method initializes defaultSeparatorButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDefaultSeparatorButton() {
		if (defaultSeparatorButton == null) {
			defaultSeparatorButton = new JRadioButton();
			defaultSeparatorButton.setText(LocalizationData.get("ExportDialog.columnSeparator.defaultSeparator")); //$NON-NLS-1$
			defaultSeparatorButton.setSelected(true);
			defaultSeparatorButton.setToolTipText(LocalizationData.get("ExportDialog.columnSeparator.defaultSeparator.toolTip")); //$NON-NLS-1$
		}
		return defaultSeparatorButton;
	}

	/**
	 * This method initializes customSeparatorButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCustomSeparatorButton() {
		if (customSeparatorButton == null) {
			customSeparatorButton = new JRadioButton();
			customSeparatorButton.setText(LocalizationData.get("ExportDialog.columnSeparator.customized")); //$NON-NLS-1$
			customSeparatorButton.setToolTipText(LocalizationData.get("ExportDialog.columnSeparator.customized.toolTip")); //$NON-NLS-1$
			customSeparatorButton.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (customSeparatorButton.isSelected()) {
						if (customSeparatorValue.getText().length()==0) {
							setSeparator(',');
						} else {
							setSeparator(customSeparatorValue.getText().charAt(0));
						}
					} else {
						setSeparator(defaultSeparator);
					}
				}
			});
		}
		return customSeparatorButton;
	}

	/**
	 * This method initializes customSeparatorValue	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private CharWidget getCustomSeparatorValue() {
		//TODO Use CharWidget
		if (customSeparatorValue == null) {
			customSeparatorValue = new CharWidget();
			customSeparatorValue.setToolTipText(LocalizationData.get("ExportDialog.columnSeparator.customizedChar.toolTip")); //$NON-NLS-1$
			// We will not use a document listener to listen the field modifications because we want to change the field (truncate it to its fisrt character)
			// and document listener can't modify the source event (it throws an IllegalStateException).
			customSeparatorValue.addPropertyChangeListener(CharWidget.CHAR_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getNewValue()==null) {
						defaultSeparatorButton.setSelected(true);
					} else {
						setSeparator(customSeparatorValue.getChar());
					}
				}
			});
		}
		return customSeparatorValue;
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		char old = this.separator;
		this.separator = separator;
		if (this.separator==defaultSeparator) {
			defaultSeparatorButton.setSelected(true);
		} else {
			customSeparatorValue.setText(new String(new char[]{separator}));
			customSeparatorButton.setSelected(true);
		}
		if (old!=separator) this.firePropertyChange(SEPARATOR_PROPERTY, old, separator);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
