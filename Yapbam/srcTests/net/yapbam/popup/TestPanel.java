package net.yapbam.popup;

import java.awt.GridBagLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

import javax.swing.JTextField;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.widget.PopupTextFieldList;

public class TestPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private PopupTextFieldList field1 = null;
	private JLabel jLabel1 = null;
	private JTextField jTextField = null;
	private JLabel jLabel2 = null;

	/**
	 * This is the default constructor
	 */
	public TestPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.gridy = 2;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.gridx = 1;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 2;
		jLabel2 = new JLabel();
		jLabel2.setText("Libell� bis");
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		jLabel1 = new JLabel();
		jLabel1.setText("x :");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText("Libell� :");
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints);
		this.add(getField1(), gridBagConstraints1);
		this.add(jLabel1, gridBagConstraints2);
		this.add(getJTextField(), gridBagConstraints3);
		this.add(jLabel2, gridBagConstraints11);
		getField1().addPropertyChangeListener(PopupTextFieldList.PREDEFINED_VALUE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println (evt.getPropertyName()+" : "+evt.getOldValue()+" -> "+evt.getNewValue());
			}
		});
	}

	/**
	 * This method initializes field1	
	 * 	
	 * @return net.yapbam.popup.PopupTextFieldList	
	 */
	private PopupTextFieldList getField1() {
		if (field1 == null) {
			field1 = new PopupTextFieldList();
		}
		return field1;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
		}
		return jTextField;
	}

	public void setData(GlobalData data) {
		HashSet<String> set = new HashSet<String>();
		for (int i=0;i<data.getTransactionsNumber();i++) {
			set.add(data.getTransaction(i).getDescription());
		}
		String[] array = set.toArray(new String[set.size()]);
		field1.setPredefined(array);
	}
}
