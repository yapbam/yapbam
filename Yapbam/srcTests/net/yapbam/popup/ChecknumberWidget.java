package net.yapbam.popup;

import java.awt.GridBagLayout;
import java.math.BigInteger;

import javax.swing.JPanel;

import net.yapbam.data.Checkbook;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;

public class ChecknumberWidget extends JPanel {

	private static final long serialVersionUID = 1L;
	private Checkbook[] books;
	private JComboBox numbers = null;
	
	/**
	 * This is the default constructor
	 */
	public ChecknumberWidget() {
		super();
		initialize();
		setBooks(new Checkbook[]{new Checkbook("", new BigInteger("12345"),50, new BigInteger("12347")),
				new Checkbook("xx", BigInteger.ONE, 10, BigInteger.valueOf(8))});
	}
	
	public void setBooks(Checkbook[] books) {
		this.books = books;
		numbers.removeAllItems();
		for (int i = 0; i < books.length; i++) {
			numbers.addItem(books[i]);
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getNumbers(), gridBagConstraints);
	}

	/**
	 * This method initializes numbers	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getNumbers() {
		if (numbers == null) {
			numbers = new JComboBox();
			numbers.setEditable(true);
		}
		return numbers;
	}
}
