package net.yapbam.gui.tools.calculator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JTextField;

import net.yapbam.util.BigDecimalEvaluator;

import com.fathzer.soft.javaluator.BracketPair;
import com.fathzer.soft.javaluator.DoubleEvaluator;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
	
	private Map<Character, JButton> map;
	private char decimalSeparator;
	private StringBuilder formula;
	private StringBuilder internalFormula;
	private BigDecimal value;
	private BigDecimalEvaluator evaluator;
	private Color validColor; 
	private Color invalidColor;
	private boolean formulaIsResult;
	private KeyAdapter keyListener;
	
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
		this.addKeyListener(getKeyListener());
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

	private KeyListener getKeyListener() {
		if (keyListener==null) {
			keyListener = new KeyAdapter() {
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
			};
		}
		return keyListener;
	}

	private void doChar(char character) {
		if (character==(char)10) {
			// Return is equivalent to =
			character = EQUAL;
		}
		if ((character>='0') && (character<='9')) {
			// digit
			if (formulaIsResult) {
				erase();
			}
			add(character);
		} else if (character==decimalSeparator) {
			if (formulaIsResult) {
				erase();
			}
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
						if (precision>0) {
							format.setMaximumFractionDigits(precision-1);
						}
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
		} catch (ArithmeticException e) {
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
		GridBagConstraints gbcResult = new GridBagConstraints();
		gbcResult.insets = new Insets(0, 0, 5, 0);
		gbcResult.weightx = 1.0;
		gbcResult.gridwidth = 0;
		gbcResult.fill = GridBagConstraints.HORIZONTAL;
		gbcResult.anchor = GridBagConstraints.NORTH;
		gbcResult.gridx = 0;
		gbcResult.gridy = 0;
		add(getResult(), gbcResult);
		GridBagConstraints gbcOpenBracket = new GridBagConstraints();
		gbcOpenBracket.fill = GridBagConstraints.HORIZONTAL;
		gbcOpenBracket.insets = new Insets(0, 0, 5, 5);
		gbcOpenBracket.weightx = 1.0;
		gbcOpenBracket.gridx = 0;
		gbcOpenBracket.gridy = 1;
		add(getOpenBracket(), gbcOpenBracket);
		GridBagConstraints gbcCloseBracket = new GridBagConstraints();
		gbcCloseBracket.fill = GridBagConstraints.HORIZONTAL;
		gbcCloseBracket.insets = new Insets(0, 0, 5, 5);
		gbcCloseBracket.weightx = 1.0;
		gbcCloseBracket.gridx = 1;
		gbcCloseBracket.gridy = 1;
		add(getCloseBracket(), gbcCloseBracket);
		GridBagConstraints gbcBtnErase = new GridBagConstraints();
		gbcBtnErase.fill = GridBagConstraints.BOTH;
		gbcBtnErase.gridwidth = 2;
		gbcBtnErase.insets = new Insets(0, 0, 5, 5);
		gbcBtnErase.gridx = 2;
		gbcBtnErase.gridy = 1;
		add(getBtnErase(), gbcBtnErase);
		GridBagConstraints gbcBtnClear = new GridBagConstraints();
		gbcBtnClear.gridheight = 2;
		gbcBtnClear.fill = GridBagConstraints.BOTH;
		gbcBtnClear.insets = new Insets(0, 0, 5, 0);
		gbcBtnClear.gridx = 4;
		gbcBtnClear.gridy = 1;
		add(getBtnClear(), gbcBtnClear);
		GridBagConstraints gbcBtnDivide = new GridBagConstraints();
		gbcBtnDivide.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnDivide.insets = new Insets(0, 0, 5, 5);
		gbcBtnDivide.gridx = 3;
		gbcBtnDivide.gridy = 2;
		add(getBtnDivide(), gbcBtnDivide);
		GridBagConstraints gbcBtnMultiply = new GridBagConstraints();
		gbcBtnMultiply.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnMultiply.insets = new Insets(0, 0, 5, 5);
		gbcBtnMultiply.gridx = 3;
		gbcBtnMultiply.gridy = 3;
		add(getBtnMultiply(), gbcBtnMultiply);
		GridBagConstraints gbcBtnMinus = new GridBagConstraints();
		gbcBtnMinus.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnMinus.weightx = 1.0;
		gbcBtnMinus.insets = new Insets(0, 0, 5, 5);
		gbcBtnMinus.gridx = 3;
		gbcBtnMinus.gridy = 4;
		add(getBtnMinus(), gbcBtnMinus);
		GridBagConstraints gbcBtnPlus = new GridBagConstraints();
		gbcBtnPlus.insets = new Insets(0, 0, 0, 5);
		gbcBtnPlus.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnPlus.gridx = 3;
		gbcBtnPlus.gridy = 5;
		add(getBtnPlus(), gbcBtnPlus);
		GridBagConstraints gbcBtn7 = new GridBagConstraints();
		gbcBtn7.fill = GridBagConstraints.HORIZONTAL;
		gbcBtn7.insets = new Insets(0, 0, 5, 5);
		gbcBtn7.gridx = 0;
		gbcBtn7.gridy = 2;
		add(getBtn7(), gbcBtn7);
		GridBagConstraints gbcBtn8 = new GridBagConstraints();
		gbcBtn8.fill = GridBagConstraints.HORIZONTAL;
		gbcBtn8.insets = new Insets(0, 0, 5, 5);
		gbcBtn8.gridx = 1;
		gbcBtn8.gridy = 2;
		add(getBtn8(), gbcBtn8);
		GridBagConstraints gbcBtn9 = new GridBagConstraints();
		gbcBtn9.fill = GridBagConstraints.HORIZONTAL;
		gbcBtn9.insets = new Insets(0, 0, 5, 5);
		gbcBtn9.gridx = 2;
		gbcBtn9.gridy = 2;
		add(getBtn9(), gbcBtn9);
		GridBagConstraints gbcBtn4 = new GridBagConstraints();
		gbcBtn4.fill = GridBagConstraints.HORIZONTAL;
		gbcBtn4.insets = new Insets(0, 0, 5, 5);
		gbcBtn4.gridx = 0;
		gbcBtn4.gridy = 3;
		add(getBtn4(), gbcBtn4);
		GridBagConstraints gbcBtn5 = new GridBagConstraints();
		gbcBtn5.fill = GridBagConstraints.HORIZONTAL;
		gbcBtn5.insets = new Insets(0, 0, 5, 5);
		gbcBtn5.weightx = 1.0;
		gbcBtn5.gridx = 1;
		gbcBtn5.gridy = 3;
		add(getBtn5(), gbcBtn5);
		GridBagConstraints gbcBtn6 = new GridBagConstraints();
		gbcBtn6.fill = GridBagConstraints.HORIZONTAL;
		gbcBtn6.weightx = 1.0;
		gbcBtn6.insets = new Insets(0, 0, 5, 5);
		gbcBtn6.gridx = 2;
		gbcBtn6.gridy = 3;
		add(getBtn6(), gbcBtn6);
		GridBagConstraints gbcBtn1 = new GridBagConstraints();
		gbcBtn1.fill = GridBagConstraints.HORIZONTAL;
		gbcBtn1.insets = new Insets(0, 0, 5, 5);
		gbcBtn1.gridx = 0;
		gbcBtn1.gridy = 4;
		add(getBtn1(), gbcBtn1);
		GridBagConstraints gbcBtn2 = new GridBagConstraints();
		gbcBtn2.fill = GridBagConstraints.HORIZONTAL;
		gbcBtn2.insets = new Insets(0, 0, 5, 5);
		gbcBtn2.gridx = 1;
		gbcBtn2.gridy = 4;
		add(getBtn2(), gbcBtn2);
		GridBagConstraints gbcBtn3 = new GridBagConstraints();
		gbcBtn3.fill = GridBagConstraints.HORIZONTAL;
		gbcBtn3.insets = new Insets(0, 0, 5, 5);
		gbcBtn3.gridx = 2;
		gbcBtn3.gridy = 4;
		add(getBtn3(), gbcBtn3);
		GridBagConstraints gbcBtn0 = new GridBagConstraints();
		gbcBtn0.fill = GridBagConstraints.HORIZONTAL;
		gbcBtn0.gridwidth = 2;
		gbcBtn0.insets = new Insets(0, 0, 0, 5);
		gbcBtn0.gridx = 0;
		gbcBtn0.gridy = 5;
		add(getBtn0(), gbcBtn0);
		GridBagConstraints gbcBtnDecimal = new GridBagConstraints();
		gbcBtnDecimal.insets = new Insets(0, 0, 0, 5);
		gbcBtnDecimal.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnDecimal.gridx = 2;
		gbcBtnDecimal.gridy = 5;
		add(getBtnDecimal(), gbcBtnDecimal);
		GridBagConstraints gbcBtnEquals = new GridBagConstraints();
		gbcBtnEquals.fill = GridBagConstraints.VERTICAL;
		gbcBtnEquals.gridheight = 3;
		gbcBtnEquals.gridx = 4;
		gbcBtnEquals.gridy = 3;
		add(getBtnEquals(), gbcBtnEquals);
	}

	private JTextField getResult() {
		if (result == null) {
			result = new JTextField();
			result.setEditable(false);
			result.setHorizontalAlignment(JTextField.RIGHT);
			result.addKeyListener(getKeyListener());
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
	    URL imgURL = getClass().getResource("backspace.png"); //$NON-NLS-1$
	    if (imgURL != null) {
				btnErase.setText(""); //$NON-NLS-1$
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
			if (text.length()>0) {
				character = text.charAt(0);
			}
			super.setText(text);
		}
	}
}
