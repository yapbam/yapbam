package net.yapbam.DatePickers;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import com.michaelbaranov.microba.calendar.DatePicker;
import java.awt.GridBagConstraints;

public class Microba extends JPanel {

	private static final long serialVersionUID = 1L;
	private DatePicker datePicker = null;

	/**
	 * This is the default constructor
	 */
	public Microba() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getDatePicker(), gridBagConstraints);
	}

	/**
	 * This method initializes datePicker	
	 * 	
	 * @return com.michaelbaranov.microba.calendar.DatePicker	
	 */
	private DatePicker getDatePicker() {
		if (datePicker == null) {
			datePicker = new DatePicker();
		}
		return datePicker;
	}

}
