package net.yapbam.gui.dialogs.checkbook;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import net.yapbam.data.Checkbook;
import net.yapbam.gui.LocalizationData;

import javax.swing.JTextField;

import com.fathzer.soft.ajlib.swing.widget.IntegerWidget;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;

public class CheckbookPane extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$

	private IntegerWidget first = null;
	private IntegerWidget number = null;
	
	private String invalidityCause;
	private Checkbook currentBook;
	private JTextField prefix = null;
	private IntegerWidget next = null;
	private PropertyChangeListener changeListener;
	
	/**
	 * This is the default constructor
	 */
	public CheckbookPane() {
		super();
		this.changeListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				parse();
			}
		};
		initialize();
		parse();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.gridy = 3;
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.anchor = GridBagConstraints.EAST;
		gridBagConstraints4.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints4.gridx = 1;
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.anchor = GridBagConstraints.WEST;
		gridBagConstraints31.gridx = 0;
		gridBagConstraints31.gridy = 3;
		JLabel jLabel3 = new JLabel();
		jLabel3.setText(LocalizationData.get("checkbookDialog.next")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.gridy = 0;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.anchor = GridBagConstraints.EAST;
		gridBagConstraints21.gridx = 1;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.gridy = 0;
		JLabel jLabel2 = new JLabel();
		jLabel2.setText(LocalizationData.get("checkbookDialog.prefix")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.anchor = GridBagConstraints.EAST;
		gridBagConstraints3.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 2;
		JLabel jLabel1 = new JLabel();
		jLabel1.setText(LocalizationData.get("checkbookDialog.number")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.anchor = GridBagConstraints.EAST;
		gridBagConstraints2.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints2.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 1;
		JLabel jLabel = new JLabel();
		jLabel.setText(LocalizationData.get("checkbookDialog.first")); //$NON-NLS-1$
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints);
		this.add(getFirst(), gridBagConstraints2);
		this.add(jLabel1, gridBagConstraints1);
		this.add(getNumber(), gridBagConstraints3);
		this.add(jLabel2, gridBagConstraints11);
		this.add(getPrefix(), gridBagConstraints21);
		this.add(jLabel3, gridBagConstraints31);
		this.add(getNext(), gridBagConstraints4);
	}

	public String getInvalidityCause() {
		return this.invalidityCause;
	}

	public Checkbook getCheckbook() {
		return this.currentBook;
	}

	/**
	 * This method initializes first	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private IntegerWidget getFirst() {
		if (first == null) {
			first = new IntegerWidget(BigInteger.ZERO, null);
			first.setColumns(10);
			first.setToolTipText(LocalizationData.get("checkbookDialog.first.tooltip")); //$NON-NLS-1$
			first.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, changeListener);
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
			number = new IntegerWidget(BigInteger.ZERO, IntegerWidget.INTEGER_MAX_VALUE);
			number.setColumns(2);
			number.setToolTipText(LocalizationData.get("checkbookDialog.number.tooltip")); //$NON-NLS-1$
			number.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, changeListener);
		}
		return number;
	}

	private void parse() {
		String old = this.invalidityCause;
		this.invalidityCause = null;
		this.currentBook = null;
				
		if (first.getValue()==null) {
			invalidityCause = LocalizationData.get("checkbookDialog.error.firstIsBlank");  //$NON-NLS-1$
		} else if (number.getValue()==null) {
			if (number.getText().isEmpty()) {
				invalidityCause = LocalizationData.get("checkbookDialog.error.numberIsBlank"); //$NON-NLS-1$
			} else {
				invalidityCause = LocalizationData.get("checkbookDialog.error.numberOutOfRange"); //$NON-NLS-1$
			}
		} else if (next.getValue()==null) {
			invalidityCause = LocalizationData.get("checkbookDialog.error.nextIsBlank");  //$NON-NLS-1$
		} else {
			// All fields are filled.
			// Let's verify that next check is inside the checkbook
			BigInteger diff = next.getValue().subtract(first.getValue());
			if ((diff.signum()<0) || (diff.compareTo(number.getValue())>=0)) {
				invalidityCause = LocalizationData.get("checkbookDialog.error.nextOutsideOfCheckbook"); //$NON-NLS-1$
			} else {
				currentBook = new Checkbook(prefix.getText(), first.getValue(), number.getValue().intValue(), next.getValue());
			}
			
			// Old code able to separate prefix from numerical suffix in a single full check number
			// We will try to separate the prefix of the check number (the part that will remain constant over all the check book)
			// and the number itself
//			String firstNumber = first.getText();
//			int l = firstNumber.length();
//			// First, we will compute how long is the integer at the end of the first check number
//			int suffixLength = 0;
//			for (int i = l-1; i >=0; i--) {
//				if (!Character.isDigit(firstNumber.charAt(i))) break;
//				suffixLength++;
//			}
//			if (suffixLength==0) {
//				this.invalidityCause = LocalizationData.get("checkbookDialog.error.noNumericalSuffix"); //$NON-NLS-1$
//			} else {
//				BigInteger start = new BigInteger(firstNumber.substring(l-suffixLength));
//				BigInteger last = start.add(number.getValue());
//				if ((last.toString().length()>suffixLength) && (suffixLength!=l)) {
//					this.invalidityCause = LocalizationData.get("checkbookDialog.error.numericalSuffixToSmall"); //$NON-NLS-1$
//				} else {
//					String prefix = suffixLength==l?"":firstNumber.substring(0, l-suffixLength); //$NON-NLS-1$
//					currentBook = new Checkbook(prefix, start, l-prefix.length(), number.getValue().intValue(), 0);
//				}
//			}
		}
		if (!NullUtils.areEquals(old, this.invalidityCause)) {
			this.firePropertyChange(INVALIDITY_CAUSE, old, this.invalidityCause);
		}
	}

	public void setContent(Checkbook book) {
		this.prefix.setText(book.getPrefix());
		this.first.setValue(book.getFirst());
		this.number.setValue(book.size());
		this.next.setValue(book.getFirst().add(BigInteger.valueOf(book.getUsed())));
		parse();
	}

	/**
	 * This method initializes prefix	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getPrefix() {
		if (prefix == null) {
			prefix = new JTextField();
			prefix.setColumns(10);
			prefix.setToolTipText(LocalizationData.get("checkbookDialog.prefix.tooltip")); //$NON-NLS-1$
		}
		return prefix;
	}

	/**
	 * This method initializes next	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private IntegerWidget getNext() {
		if (next == null) {
			next = new IntegerWidget(BigInteger.ZERO, null);
			next.setToolTipText(LocalizationData.get("checkbookDialog.next.tooltip")); //$NON-NLS-1$
			next.setColumns(10);
			next.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, changeListener);
		}
		return next;
	}
}
