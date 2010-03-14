package net.yapbam.gui.dialogs.checkbook;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import net.yapbam.gui.widget.IntegerWidget;
import net.yapbam.util.NullUtils;

import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CheckbookPane extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$  //  @jve:decl-index=0:

	private JLabel jLabel = null;
	private JTextField first = null;
	private JLabel jLabel1 = null;
	private IntegerWidget number = null;
	private String invalidityCause;
	/**
	 * This is the default constructor
	 */
	public CheckbookPane() {
		super();
		initialize();
		parse();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.anchor = GridBagConstraints.EAST;
		gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 1;
		jLabel1 = new JLabel();
		jLabel1.setText("Number of checks:");
		jLabel1.setToolTipText("Enter here the number of the first check (without the prefix)");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.anchor = GridBagConstraints.EAST;
		gridBagConstraints2.insets = new Insets(0, 0, 5, 0);
		gridBagConstraints2.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText("First check's number:");
		jLabel.setToolTipText("");
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints);
		this.add(getFirst(), gridBagConstraints2);
		this.add(jLabel1, gridBagConstraints1);
		this.add(getNumber(), gridBagConstraints3);
	}

	public String getInvalidityCause() {
		return this.invalidityCause;
	}

	public Object getCheckbook() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method initializes first	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFirst() {
		if (first == null) {
			first = new JTextField();
			first.setColumns(10);
			first.setToolTipText("Enter here the checks number prefix");
			first.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					parse();
				}
			});
		}
		return first;
	}

	/**
	 * This method initializes number	
	 * 	
	 * @return net.yapbam.gui.widget.IntegerWidget	
	 */
	private IntegerWidget getNumber() {
		if (number == null) {
			number = new IntegerWidget();
			number.setColumns(2);
			number.setToolTipText("Enter here the number of checks in the checkbook");
			number.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					parse();
				}
			});
		}
		return number;
	}

	private void parse() {
		System.out.println("First:"+first.getText());//TODO
		System.out.println("Number:"+number.getText()+"->"+number.getValue()); //TODO
		String old = this.invalidityCause;
		this.invalidityCause = null;
		if ((number.getValue()==null) || (number.getValue()<=0)) {
			if (number.getText().length()==0) invalidityCause = "Disabled because the number of checks in the checkbook is empty";
			invalidityCause = "Disabled because the number of checks you entered is not a positive integer";
		} else {
			
		}
		if (!NullUtils.areEquals(old, this.invalidityCause)) {
			System.out.println ("change fired -> "+this.invalidityCause);
			this.firePropertyChange(INVALIDITY_CAUSE, old, this.invalidityCause);
		}
	}
}
