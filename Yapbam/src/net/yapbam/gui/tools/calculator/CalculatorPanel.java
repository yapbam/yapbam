package net.yapbam.gui.tools.calculator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import javax.swing.JTextField;

import net.astesana.javaluator.BracketPair;
import net.astesana.javaluator.DoubleEvaluator;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

@SuppressWarnings("serial")
public class CalculatorPanel extends JPanel {
	private static final char CLEAR = 'C';
	private static final char EQUAL = '=';
	private static final Character PLUS = DoubleEvaluator.PLUS.getSymbol().charAt(0);
	private static final Character MINUS = DoubleEvaluator.MINUS.getSymbol().charAt(0);
	private static final Character MULTIPLY = DoubleEvaluator.MULTIPLY.getSymbol().charAt(0);
	private static final Character DIVIDE = DoubleEvaluator.DIVIDE.getSymbol().charAt(0);
	private static final Character OPEN = BracketPair.PARENTHESES.getOpen().charAt(0);
	private static final Character CLOSE = BracketPair.PARENTHESES.getClose().charAt(0);
	private static final char POINT = '.';

	private static final int PRECISION = 20;

	private JTextField result;
	private CalculatorButton openBracket;
	private CalculatorButton closeBracket;
	private CalculatorButton btn7;
	private CalculatorButton btn6;
	private CalculatorButton btn5;
	private CalculatorButton btn4;
	private CalculatorButton btn3;
	private CalculatorButton btn2;
	private CalculatorButton btn8;
	private CalculatorButton btn9;
	private CalculatorButton btn1;
	private CalculatorButton btn0;
	private CalculatorButton btnDecimal;
	private CalculatorButton btnPlus;
	private CalculatorButton btnMinus;
	private CalculatorButton btnMultiply;
	private CalculatorButton btnDivide;
	private CalculatorButton btnErase;
	private CalculatorButton btnEquals;
	private CalculatorButton btnClear;
	
	private HashMap<Character, JButton> map;
	private char decimalSeparator;
	private StringBuilder formula;
	private StringBuilder internalFormula;
	private BigDecimal value;
	private BigDecimalEvaluator evaluator;
	private Color validColor; 
	private Color invalidColor;
	private boolean formulaIsResult;
	
	/**
	 * Creates the panel with the default locale.
	 */
	public CalculatorPanel() {
		this(Locale.getDefault());
	}

	/**
	 * Creates the panel.
	 * @param locale the locale
	 */
	public CalculatorPanel(Locale locale) {
		this.evaluator = new BigDecimalEvaluator(new MathContext(PRECISION+3));
		this.formula = new StringBuilder();
		this.internalFormula = new StringBuilder();
		this.validColor = getResult().getForeground();
		this.invalidColor = halfContrast(validColor, getResult().getBackground());
		this.formulaIsResult = false;
		
		// Initialize stuff to be able to simulate click on button while pressing keys
		initCharToButton();
		// Init GUI
		this.initialize();
		// Set the locale
		setLocale(locale);
		// Set the point as an equivalent to the decimal separator
		setPointIsADecimalSeparator(true);
	}
	
	private Color halfContrast(Color c1, Color c2) {
		return new Color((c1.getRed()+c2.getRed())/2, (c1.getGreen()+c2.getGreen())/2, (c1.getBlue()+c2.getBlue())/2);
	}
	
	/* Sets the calculator locale.
	 * <br>The decimal character depends on the locale.
	 * @see java.awt.Component#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale locale) {
		this.map.remove(decimalSeparator);
		this.decimalSeparator = ((DecimalFormat)NumberFormat.getNumberInstance(locale)).getDecimalFormatSymbols().getDecimalSeparator();
		this.map.put(decimalSeparator, getBtnDecimal());
		getBtnDecimal().setText(new String(new char[]{decimalSeparator}));
		super.setLocale(locale);
	}
	
	/** Sets the point as always equivalent to a decimal separator. 
	 * <br>Some countries do not have the point as decimal separator, but their numerical keyboard has.
	 * By default, this panel considers the point as a decimal separator. For instance, in France, this
	 * results in having the ',' and the '.' keys outputting a decimal separator ','.
	 * <br>You may set always to false in order to change this behavior
	 * @param always false to only accept the locale decimal separator.
	 * @see #setLocale(Locale)
	 */
	public void setPointIsADecimalSeparator(boolean always) {
		if (decimalSeparator!=POINT) {
			if (always) {
				this.map.put(POINT, getBtnDecimal());
			} else {
				this.map.remove(POINT);
			}
		}
	}

	private void initCharToButton() {
		this.addKeyListener(new KeyAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				int modifiers = e.getModifiers();
				if ((modifiers&~KeyEvent.SHIFT_MASK)==0) {
					JButton btn = map.get(Character.toUpperCase(e.getKeyChar()));
					if (btn!=null) {
						btn.doClick();
//					} else {
//						System.out.println ((int)e.getKeyChar()+" ("+e.getModifiers()+")");
					}
				}
				super.keyTyped(e);
			}
		});
		this.setFocusable(true);
		// Initialize the character to button map 		
		this.map = new HashMap<Character, JButton>();
		map.put('0', getBtn0());
		map.put('1', getBtn1());
		map.put('2', getBtn2());
		map.put('3', getBtn3());
		map.put('4', getBtn4());
		map.put('5', getBtn5());
		map.put('6', getBtn6());
		map.put('7', getBtn7());
		map.put('8', getBtn8());
		map.put('9', getBtn9());
		map.put(OPEN, getOpenBracket());
		map.put(CLOSE, getCloseBracket());
		map.put(CLEAR, getBtnClear());
		map.put((char) 8, getBtnErase());
		map.put((char) 127, getBtnErase());
		map.put(EQUAL, getBtnEquals());
		map.put((char) 10, getBtnEquals());
		map.put(PLUS, getBtnPlus());
		map.put(MINUS, getBtnMinus());
		map.put(DIVIDE, getBtnDivide());
		map.put(MULTIPLY, getBtnMultiply());
		// The decimal point is added in the map by setLocale
	}

	private void doChar(char character) {
		if (character==(char)10) character = EQUAL; // Return is equivalent to =
		if ((character>='0') && (character<='9')) {
			// digit
			if (formulaIsResult) erase();
			add(character);
		} else if (character==decimalSeparator) {
			if (formulaIsResult) erase();
			add(character);
		} else if ((character==PLUS) || (character==MINUS) || (character==MULTIPLY) || (character==DIVIDE) || (character==OPEN) || (character==CLOSE)) {
			add(character);
		} else if ((character==(char)8) || (character==(char)127)) {
			// Delete last char
			if (formula.length()>0) {
				formula.delete(formula.length()-1, formula.length());
				// The internal formula should be replaced by a strict copy of the displayed formula.
				// If we do not do that, the sequence 1 / 3 = <- will result in having 0.3333 displayed and 0.333333333 internally
				internalFormula.delete(0, internalFormula.length());
				internalFormula.append(formula);
			}
		} else if (Character.toUpperCase(character)==CLEAR) {
			// Clear
			erase();
			throw new IllegalArgumentException(); //TODO
		} else if (character==EQUAL) {
			// Evaluate the expression
			if (this.value!=null) {
				//The calculator try to evaluate the expression each time a key is pressed
				//So, this.value contains the result of the evaluation (or null if the expression is wrong)
				internalFormula.delete(0, internalFormula.length());
				internalFormula.append(this.value.toString());
				formula.delete(0, formula.length());
				DecimalFormat format = (DecimalFormat) NumberFormat.getInstance();
				format.setMinimumFractionDigits(0);
				// Searching for the right precision in order to not have the result bigger than the field
				int availableWidth = getResult().getSize().width - getResult().getInsets().left - getResult().getInsets().right;
				FontMetrics fontMetrics = getResult().getFontMetrics(getResult().getFont());
				for (int precision=0; precision<PRECISION; precision++) {
					format.setMaximumFractionDigits(precision);
					if (fontMetrics.stringWidth(format.format(this.value))>availableWidth) {
						if (precision>0) format.setMaximumFractionDigits(precision-1);
						break;
					}
				}
				formula.append(format.format(this.value).replace(POINT, decimalSeparator));
			}
		}
		try {
			// Evaluator expects point as decimal separator
			String evaluatedString = internalFormula.toString().replace(decimalSeparator, POINT); 
			value = evaluator.evaluate(evaluatedString);
		} catch (IllegalArgumentException e) {
			value = null;
		}
		formulaIsResult = (this.value!=null) && (character==EQUAL);
		getResult().setForeground(value==null?this.invalidColor:this.validColor);
		getBtnEquals().setEnabled(value!=null);
		getResult().setText(formula.toString());
	}

	private void erase() {
		internalFormula.delete(0, internalFormula.length());
		formula.delete(0, formula.length());
	}

	private void add(char character) {
		formula.append(character);
		internalFormula.append(character);
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_result = new GridBagConstraints();
		gbc_result.insets = new Insets(0, 0, 5, 0);
		gbc_result.weightx = 1.0;
		gbc_result.gridwidth = 0;
		gbc_result.fill = GridBagConstraints.HORIZONTAL;
		gbc_result.anchor = GridBagConstraints.NORTH;
		gbc_result.gridx = 0;
		gbc_result.gridy = 0;
		add(getResult(), gbc_result);
		GridBagConstraints gbc_openBracket = new GridBagConstraints();
		gbc_openBracket.fill = GridBagConstraints.HORIZONTAL;
		gbc_openBracket.insets = new Insets(0, 0, 5, 5);
		gbc_openBracket.weightx = 1.0;
		gbc_openBracket.gridx = 0;
		gbc_openBracket.gridy = 1;
		add(getOpenBracket(), gbc_openBracket);
		GridBagConstraints gbc_closeBracket = new GridBagConstraints();
		gbc_closeBracket.fill = GridBagConstraints.HORIZONTAL;
		gbc_closeBracket.insets = new Insets(0, 0, 5, 5);
		gbc_closeBracket.weightx = 1.0;
		gbc_closeBracket.gridx = 1;
		gbc_closeBracket.gridy = 1;
		add(getCloseBracket(), gbc_closeBracket);
		GridBagConstraints gbc_btnErase = new GridBagConstraints();
		gbc_btnErase.fill = GridBagConstraints.BOTH;
		gbc_btnErase.gridwidth = 2;
		gbc_btnErase.insets = new Insets(0, 0, 5, 5);
		gbc_btnErase.gridx = 2;
		gbc_btnErase.gridy = 1;
		add(getBtnErase(), gbc_btnErase);
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.gridheight = 2;
		gbc_btnClear.fill = GridBagConstraints.BOTH;
		gbc_btnClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnClear.gridx = 4;
		gbc_btnClear.gridy = 1;
		add(getBtnClear(), gbc_btnClear);
		GridBagConstraints gbc_btnDivide = new GridBagConstraints();
		gbc_btnDivide.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDivide.insets = new Insets(0, 0, 5, 5);
		gbc_btnDivide.gridx = 3;
		gbc_btnDivide.gridy = 2;
		add(getBtnDivide(), gbc_btnDivide);
		GridBagConstraints gbc_btnMultiply = new GridBagConstraints();
		gbc_btnMultiply.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnMultiply.insets = new Insets(0, 0, 5, 5);
		gbc_btnMultiply.gridx = 3;
		gbc_btnMultiply.gridy = 3;
		add(getBtnMultiply(), gbc_btnMultiply);
		GridBagConstraints gbc_btnMinus = new GridBagConstraints();
		gbc_btnMinus.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnMinus.weightx = 1.0;
		gbc_btnMinus.insets = new Insets(0, 0, 5, 5);
		gbc_btnMinus.gridx = 3;
		gbc_btnMinus.gridy = 4;
		add(getBtnMinus(), gbc_btnMinus);
		GridBagConstraints gbc_btnPlus = new GridBagConstraints();
		gbc_btnPlus.insets = new Insets(0, 0, 0, 5);
		gbc_btnPlus.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPlus.gridx = 3;
		gbc_btnPlus.gridy = 5;
		add(getBtnPlus(), gbc_btnPlus);
		GridBagConstraints gbc_btn7 = new GridBagConstraints();
		gbc_btn7.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn7.insets = new Insets(0, 0, 5, 5);
		gbc_btn7.gridx = 0;
		gbc_btn7.gridy = 2;
		add(getBtn7(), gbc_btn7);
		GridBagConstraints gbc_btn8 = new GridBagConstraints();
		gbc_btn8.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn8.insets = new Insets(0, 0, 5, 5);
		gbc_btn8.gridx = 1;
		gbc_btn8.gridy = 2;
		add(getBtn8(), gbc_btn8);
		GridBagConstraints gbc_btn9 = new GridBagConstraints();
		gbc_btn9.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn9.insets = new Insets(0, 0, 5, 5);
		gbc_btn9.gridx = 2;
		gbc_btn9.gridy = 2;
		add(getBtn9(), gbc_btn9);
		GridBagConstraints gbc_btn4 = new GridBagConstraints();
		gbc_btn4.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn4.insets = new Insets(0, 0, 5, 5);
		gbc_btn4.gridx = 0;
		gbc_btn4.gridy = 3;
		add(getBtn4(), gbc_btn4);
		GridBagConstraints gbc_btn5 = new GridBagConstraints();
		gbc_btn5.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn5.insets = new Insets(0, 0, 5, 5);
		gbc_btn5.weightx = 1.0;
		gbc_btn5.gridx = 1;
		gbc_btn5.gridy = 3;
		add(getBtn5(), gbc_btn5);
		GridBagConstraints gbc_btn6 = new GridBagConstraints();
		gbc_btn6.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn6.weightx = 1.0;
		gbc_btn6.insets = new Insets(0, 0, 5, 5);
		gbc_btn6.gridx = 2;
		gbc_btn6.gridy = 3;
		add(getBtn6(), gbc_btn6);
		GridBagConstraints gbc_btn1 = new GridBagConstraints();
		gbc_btn1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn1.insets = new Insets(0, 0, 5, 5);
		gbc_btn1.gridx = 0;
		gbc_btn1.gridy = 4;
		add(getBtn1(), gbc_btn1);
		GridBagConstraints gbc_btn2 = new GridBagConstraints();
		gbc_btn2.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn2.insets = new Insets(0, 0, 5, 5);
		gbc_btn2.gridx = 1;
		gbc_btn2.gridy = 4;
		add(getBtn2(), gbc_btn2);
		GridBagConstraints gbc_btn3 = new GridBagConstraints();
		gbc_btn3.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn3.insets = new Insets(0, 0, 5, 5);
		gbc_btn3.gridx = 2;
		gbc_btn3.gridy = 4;
		add(getBtn3(), gbc_btn3);
		GridBagConstraints gbc_btn0 = new GridBagConstraints();
		gbc_btn0.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn0.gridwidth = 2;
		gbc_btn0.insets = new Insets(0, 0, 0, 5);
		gbc_btn0.gridx = 0;
		gbc_btn0.gridy = 5;
		add(getBtn0(), gbc_btn0);
		GridBagConstraints gbc_btnDecimal = new GridBagConstraints();
		gbc_btnDecimal.insets = new Insets(0, 0, 0, 5);
		gbc_btnDecimal.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDecimal.gridx = 2;
		gbc_btnDecimal.gridy = 5;
		add(getBtnDecimal(), gbc_btnDecimal);
		GridBagConstraints gbc_btnEquals = new GridBagConstraints();
		gbc_btnEquals.fill = GridBagConstraints.VERTICAL;
		gbc_btnEquals.gridheight = 3;
		gbc_btnEquals.gridx = 4;
		gbc_btnEquals.gridy = 3;
		add(getBtnEquals(), gbc_btnEquals);
	}

	private JTextField getResult() {
		if (result == null) {
			result = new JTextField();
			result.setEditable(false);
			result.setFocusable(false);
			result.setHorizontalAlignment(JTextField.RIGHT);
		}
		return result;
	}
	private CalculatorButton getOpenBracket() {
		if (openBracket == null) {
			openBracket = new CalculatorButton(OPEN);
		}
		return openBracket;
	}
	private CalculatorButton getCloseBracket() {
		if (closeBracket == null) {
			closeBracket = new CalculatorButton(CLOSE);
		}
		return closeBracket;
	}
	private CalculatorButton getBtn7() {
		if (btn7 == null) {
			btn7 = new CalculatorButton('7');
			btn7.setFocusable(false);
		}
		return btn7;
	}
	private CalculatorButton getBtn6() {
		if (btn6 == null) {
			btn6 = new CalculatorButton('6');
		}
		return btn6;
	}
	private CalculatorButton getBtn5() {
		if (btn5 == null) {
			btn5 = new CalculatorButton('5');
		}
		return btn5;
	}
	private CalculatorButton getBtn4() {
		if (btn4 == null) {
			btn4 = new CalculatorButton('4');
		}
		return btn4;
	}
	private CalculatorButton getBtn3() {
		if (btn3 == null) {
			btn3 = new CalculatorButton('3');
		}
		return btn3;
	}
	private CalculatorButton getBtn2() {
		if (btn2 == null) {
			btn2 = new CalculatorButton('2');
		}
		return btn2;
	}
	private CalculatorButton getBtn8() {
		if (btn8 == null) {
			btn8 = new CalculatorButton('8');
		}
		return btn8;
	}
	private CalculatorButton getBtn9() {
		if (btn9 == null) {
			btn9 = new CalculatorButton('9');
		}
		return btn9;
	}
	private CalculatorButton getBtn1() {
		if (btn1 == null) {
			btn1 = new CalculatorButton('1');
		}
		return btn1;
	}
	private CalculatorButton getBtn0() {
		if (btn0 == null) {
			btn0 = new CalculatorButton('0');
		}
		return btn0;
	}
	private CalculatorButton getBtnDecimal() {
		if (btnDecimal == null) {
			btnDecimal = new CalculatorButton(POINT);
		}
		return btnDecimal;
	}
	private CalculatorButton getBtnPlus() {
		if (btnPlus == null) {
			btnPlus = new CalculatorButton(PLUS);
		}
		return btnPlus;
	}
	private CalculatorButton getBtnMinus() {
		if (btnMinus == null) {
			btnMinus = new CalculatorButton(MINUS);
		}
		return btnMinus;
	}
	private CalculatorButton getBtnMultiply() {
		if (btnMultiply == null) {
			btnMultiply = new CalculatorButton(MULTIPLY);
		}
		return btnMultiply;
	}
	private CalculatorButton getBtnDivide() {
		if (btnDivide == null) {
			btnDivide = new CalculatorButton(DIVIDE);
		}
		return btnDivide;
	}
	private CalculatorButton getBtnErase() {
		if (btnErase == null) {
			btnErase = new CalculatorButton('<');
	    URL imgURL = getClass().getResource("backspace.png");
	    if (imgURL != null) {
				btnErase.setText("");
				btnErase.setIcon(new ImageIcon(imgURL));
	    }
			btnErase.character=(char)8;
		}
		return btnErase;
	}
	private CalculatorButton getBtnEquals() {
		if (btnEquals == null) {
			btnEquals = new CalculatorButton(EQUAL);
		}
		return btnEquals;
	}
	private CalculatorButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new CalculatorButton(CLEAR);
		}
		return btnClear;
	}
	
	private class CalculatorButton extends JButton {
		private char character;
		
		public CalculatorButton(char name) {
			super();
			setText(new String(new char[]{name}));
			setFocusable(false);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doChar(character);
				}
			});
		}

		/* (non-Javadoc)
		 * @see javax.swing.AbstractButton#setText(java.lang.String)
		 */
		@Override
		public void setText(String text) {
			if (text.length()>0) character = text.charAt(0);
			super.setText(text);
		}
	}
}
