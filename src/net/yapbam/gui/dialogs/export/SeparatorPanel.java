package net.yapbam.gui.dialogs.export;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;

import javax.swing.JRadioButton;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.CharWidget;
import com.fathzer.soft.ajlib.utilities.StringUtils;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;

public class SeparatorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String SEPARATOR_PROPERTY = "separator"; //$NON-NLS-1$

	private JRadioButton defaultSeparatorButton;
	private JRadioButton customSeparatorButton;
	private CharWidget customSeparatorValue;
	
	private char defaultSeparator;
	private char separator;
	private JLabel errorLabel;

    public static SeparatorPanel createDecimalSeparatorPanel() {
    	DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        String defaultSeparatorWording = LocalizationData.get("ExportDialog.decimalSeparator.defaultSeparator"); //$NON-NLS-1$
        char decimalSeparator = format.getDecimalFormatSymbols().getDecimalSeparator();
		defaultSeparatorWording = Formatter.format(defaultSeparatorWording, decimalSeparator);
		return new SeparatorPanel(LocalizationData.get("ExportDialog.decimalSeparator"), //$NON-NLS-1$
        		defaultSeparatorWording,
        		LocalizationData.get("ExportDialog.decimalSeparator.defaultSeparator.toolTip"), //$NON-NLS-1$
        		LocalizationData.get("ExportDialog.columnSeparator.customized"), //$NON-NLS-1$
        		LocalizationData.get("ExportDialog.decimalSeparator.customized.toolTip"), //$NON-NLS-1$
        		LocalizationData.get("ExportDialog.decimalSeparator.customizedChar.toolTip"), //$NON-NLS-1$
                decimalSeparator
        );
    }

    public static SeparatorPanel createColumnSeparatorPanel() {
        return new SeparatorPanel(LocalizationData.get("ExportDialog.columnSeparator"), //$NON-NLS-1$
        		LocalizationData.get("ExportDialog.columnSeparator.defaultSeparator"), //$NON-NLS-1$
        		LocalizationData.get("ExportDialog.columnSeparator.defaultSeparator.toolTip"), //$NON-NLS-1$
        		LocalizationData.get("ExportDialog.columnSeparator.customized"), //$NON-NLS-1$
        		LocalizationData.get("ExportDialog.columnSeparator.customized.toolTip"), //$NON-NLS-1$
        		LocalizationData.get("ExportDialog.columnSeparator.customizedChar.toolTip"), //$NON-NLS-1$
                '\t'
        );
    }

	/**
	 * This is the default constructor
     * @param titleKey
     * @param defaultSeparatorKey
     * @param defaultSeparatorToolTipKey
     * @param customizedSeparatorKey
     * @param customizedSeparatorToolTipKey
     * @param customizedCharToolTipKey
     * @param defaultSeparator
     */
    private SeparatorPanel(String title, String defaultSeparatorWording, String defaultSeparatorToolTip,
                           String customizedSeparatorWording, String customizedSeparatorToolTip,
                           String customizedCharToolTip, char defaultSeparator) {
		super();
		this.defaultSeparator = defaultSeparator;
        this.separator = defaultSeparator;
        initialize(title, defaultSeparatorWording, defaultSeparatorToolTip, customizedSeparatorWording,
        		customizedSeparatorToolTip, customizedCharToolTip);
	}

    /**
	 * This method initializes this
     * @param customizedCharToolTip 
     * @param customizedSeparatorToolTip 
     * @param customizedSeparatorWording 
     * @param defaultSeparatorToolTip 
     * @param defaultSeparatorWording 
     * @param title 
	 */
	private void initialize(String title, String defaultSeparatorWording, String defaultSeparatorToolTip, String customizedSeparatorWording, String customizedSeparatorToolTip, String customizedCharToolTip) {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(0, 5, 0, 5);
		gridBagConstraints2.gridx = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.gridwidth = 0;
		gridBagConstraints.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));
		JRadioButton btn = getDefaultSeparatorButton();
		btn.setText(defaultSeparatorWording);
		btn.setToolTipText(defaultSeparatorToolTip);
		this.add(btn, gridBagConstraints);
		btn = getCustomSeparatorButton();
		btn.setText(customizedSeparatorWording);
		btn.setToolTipText(customizedSeparatorToolTip);
		this.add(btn, gridBagConstraints1);
		getCustomSeparatorValue().setToolTipText(customizedCharToolTip);
		this.add(getCustomSeparatorValue(), gridBagConstraints2);
		
		ButtonGroup group = new ButtonGroup();
		group.add(getDefaultSeparatorButton());
		group.add(getCustomSeparatorButton());
		GridBagConstraints gbcErrorLabel = new GridBagConstraints();
		gbcErrorLabel.weightx = 1.0;
		gbcErrorLabel.anchor = GridBagConstraints.WEST;
		gbcErrorLabel.gridx = 2;
		gbcErrorLabel.gridy = 1;
		add(getErrorLabel(), gbcErrorLabel);
	}

	/**
	 * This method initializes defaultSeparatorButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDefaultSeparatorButton() {
		if (defaultSeparatorButton == null) {
			defaultSeparatorButton = new JRadioButton();
			defaultSeparatorButton.setSelected(true);
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
		if (customSeparatorValue == null) {
			customSeparatorValue = new CharWidget();
			// We will not use a document listener to listen the field modifications because we want to change the field (truncate it to its first character)
			// and document listener can't modify the source event (it throws an IllegalStateException).
			customSeparatorValue.addPropertyChangeListener(CharWidget.CHAR_PROPERTY, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getNewValue() == null) {
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
		if (old!=separator) {
			this.firePropertyChange(SEPARATOR_PROPERTY, old, separator);
		}
	}
	private JLabel getErrorLabel() {
		if (errorLabel == null) {
			errorLabel = new JLabel();
		}
		return errorLabel;
	}
	
	public void setError(String message) {
		if (message!=null) {
			getErrorLabel().setText(message);
			getErrorLabel().setIcon(IconManager.get(Name.ALERT));
		} else {
			getErrorLabel().setText(StringUtils.EMPTY);
			getErrorLabel().setIcon(null);
		}
	}

	public String getError() {
		String result = getErrorLabel().getText();
		return result.isEmpty()? null : result;
	}
}
