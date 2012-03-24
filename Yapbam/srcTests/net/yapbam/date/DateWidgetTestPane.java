package net.yapbam.date;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.util.Locale;

import javax.swing.JTextField;

import net.astesana.ajlib.swing.widget.date.DateWidget;

public class DateWidgetTestPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField jTextField = null;
	private DateWidget dateWidgetPanel = null;
	/**
	 * This is the default constructor
	 */
	public DateWidgetTestPane() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getJTextField(), gridBagConstraints2);
		this.add(getDateWidgetPanel(), gridBagConstraints1);
	}

	/**
	 * This method initializes dateWidgetPanel	
	 * 	
	 * @return net.yapbam.gui.widget.DateWidgetPanel	
	 */
	private DateWidget getDateWidgetPanel() {
		if (dateWidgetPanel == null) {
			dateWidgetPanel = new DateWidget();
			dateWidgetPanel.setLocale(Locale.ENGLISH);
			dateWidgetPanel.addPropertyChangeListener("date",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							System.out.println("> propertyChange(date) : "+e.getNewValue()); 
						}
					});
			System.out.println ("Initial value = "+dateWidgetPanel.getDate());
			dateWidgetPanel.setDate(null);
			dateWidgetPanel.setIsEmptyNullDateIsValid(false);
			dateWidgetPanel.setColumns(6);
		}
		return dateWidgetPanel;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setColumns(10);
		}
		return jTextField;
	}
}
