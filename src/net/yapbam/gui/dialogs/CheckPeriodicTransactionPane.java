package net.yapbam.gui.dialogs;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class CheckPeriodicTransactionPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	/**
	 * This is the default constructor
	 */
	public CheckPeriodicTransactionPane() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		jLabel = new JLabel();
		jLabel.setText("JLabel");
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(jLabel, BorderLayout.NORTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
		}
		return jTable;
	}

}
