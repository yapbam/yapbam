package net.yapbam.gui.dialogs.checkbook;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import net.yapbam.data.Checkbook;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.IntegerWidget;
import net.yapbam.util.NullUtils;

import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigInteger;

public class CheckbookPane extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$  //  @jve:decl-index=0:

	private JLabel jLabel = null;
	private JTextField first = null;
	private JLabel jLabel1 = null;
	private IntegerWidget number = null;
	
	private String invalidityCause;  //  @jve:decl-index=0:
	private Checkbook currentBook;
	
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
		jLabel1.setText(LocalizationData.get("checkbookDialog.number")); //$NON-NLS-1$
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
		jLabel.setText(LocalizationData.get("checkbookDialog.first")); //$NON-NLS-1$
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
		return this.currentBook;
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
			first.setToolTipText(LocalizationData.get("checkbookDialog.first.tooltip")); //$NON-NLS-1$
			first.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
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
			number.setToolTipText(LocalizationData.get("checkbookDialog.number.tooltip")); //$NON-NLS-1$
			number.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					parse();
				}
			});
		}
		return number;
	}

	private void parse() {
		String old = this.invalidityCause;
		this.invalidityCause = null;
		this.currentBook = null;
		if (first.getText().isEmpty()) {
			invalidityCause = LocalizationData.get("checkbookDialog.error.firstIsBlank"); //$NON-NLS-1$
		} else if ((number.getValue()==null) || (number.getValue()<=0)) {
			if (number.getText().length()==0) invalidityCause = LocalizationData.get("checkbookDialog.error.numberIsBlank"); //$NON-NLS-1$
			else invalidityCause = LocalizationData.get("checkbookDialog.error.numberIsNegative"); //$NON-NLS-1$
		} else {
			// All fields are filled.
			// We will try to separate the prefix of the check number (the part that will remain constant over all the check book)
			// and the number itself
			String firstNumber = first.getText();
			int l = firstNumber.length();
			// First, we will compute how long is the integer at the end of the first check number
			int suffixLength = 0;
			for (int i = l-1; i >=0; i--) {
				if (!Character.isDigit(firstNumber.charAt(i))) break;
				suffixLength++;
			}
			if (suffixLength==0) {
				this.invalidityCause = LocalizationData.get("checkbookDialog.error.noNumericalSuffix"); //$NON-NLS-1$
			} else {
				BigInteger start = new BigInteger(firstNumber.substring(l-suffixLength));
				BigInteger last = start.add(BigInteger.valueOf(number.getValue()));
				if ((last.toString().length()>suffixLength) && (suffixLength!=l)) {
					this.invalidityCause = LocalizationData.get("checkbookDialog.error.numericalSuffixToSmall"); //$NON-NLS-1$
				} else {
					String prefix = suffixLength==l?"":firstNumber.substring(0, l-suffixLength); //$NON-NLS-1$
					currentBook = new Checkbook(prefix, start, l-prefix.length(), number.getValue());
				}
			}
		}
		if (!NullUtils.areEquals(old, this.invalidityCause)) {
			this.firePropertyChange(INVALIDITY_CAUSE, old, this.invalidityCause);
		}
	}
}
