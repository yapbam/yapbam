package net.yapbam.date;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import net.yapbam.gui.widget.DateWidget;
import java.awt.GridBagConstraints;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DateWidgetTestPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private DateWidget dateWidget = null;
	private JTextField jTextField = null;
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
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridx = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getDateWidget(), gridBagConstraints);
		this.add(getJTextField(), gridBagConstraints2);
	}

	/**
	 * This method initializes dateWidget	
	 * 	
	 * @return net.yapbam.gui.widget.DateWidget	
	 */
	private DateWidget getDateWidget() {
		if (dateWidget == null) {
			dateWidget = new DateWidget();
			dateWidget.addPropertyChangeListener("date",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							System.out.println("propertyChange(date) : "+e.getNewValue()); 
						}
					});
			System.out.println ("Initial value = "+dateWidget.getDate());
			dateWidget.setDate(null);
//			dateWidget.setIsEmptyNullDateIsValid(false);
			dateWidget.setColumns(6);
		}
		return dateWidget;
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
