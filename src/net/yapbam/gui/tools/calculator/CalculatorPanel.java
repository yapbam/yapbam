package net.yapbam.gui.tools.calculator;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JTextField;

import net.astesana.javaluator.DoubleEvaluator;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class CalculatorPanel extends JPanel {
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
	
	private StringBuilder formula;
	private Double value;
	private DoubleEvaluator evaluator;
	private Color validColor; 
	private Color invalidColor; 
	
	private void doChar(char character) {
		System.out.println("doChar('"+character+"') : "+(int)character);
		if ((character>='0') && (character<='9')) {
			// digit
			formula.append(character);
		} else if (character==getBtnDecimal().character) {
			// decimal point
			formula.append(character);
		} else if ((character=='+') || (character=='-') || (character=='*') || (character=='/') || (character=='(') || (character==')')) {
			// Operator
			formula.append(character);
		} else if ((character==(char)8) || (character==(char)127)) {
			// Delete last char
			if (formula.length()>0) formula.delete(formula.length()-1, formula.length());
		} else if (Character.toUpperCase(character)=='C') {
			// Clear
			formula.delete(0, formula.length());
		} else if ((character=='=') || (character==(char)10)) {
			// Evaluate the expression
			if (this.value!=null) {
				formula.delete(0, formula.length());
				formula.append(Double.toString(this.value));
			}
		}
		try {
			value = evaluator.evaluate(formula.toString());
		} catch (IllegalArgumentException e) {
			value = null;
		}
		getResult().setForeground(value==null?this.invalidColor:this.validColor);
		getResult().setText(formula.toString());
	}

	/**
	 * Create the panel.
	 */
	public CalculatorPanel() {
		this.evaluator = new DoubleEvaluator();
		this.formula = new StringBuilder();
		this.validColor = getResult().getForeground();
		this.invalidColor = validColor.brighter();
		
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
		gbc_btnErase.fill = GridBagConstraints.HORIZONTAL;
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

		this.addKeyListener(new KeyAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				doChar(e.getKeyChar());
				super.keyTyped(e);
			}
		});
		this.setFocusable(true);
		this.requestFocus();
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
			openBracket = new CalculatorButton("(");
		}
		return openBracket;
	}
	private CalculatorButton getCloseBracket() {
		if (closeBracket == null) {
			closeBracket = new CalculatorButton(")");
		}
		return closeBracket;
	}
	private CalculatorButton getBtn7() {
		if (btn7 == null) {
			btn7 = new CalculatorButton("7");
			btn7.setFocusable(false);
		}
		return btn7;
	}
	private CalculatorButton getBtn6() {
		if (btn6 == null) {
			btn6 = new CalculatorButton("6");
		}
		return btn6;
	}
	private CalculatorButton getBtn5() {
		if (btn5 == null) {
			btn5 = new CalculatorButton("5");
		}
		return btn5;
	}
	private CalculatorButton getBtn4() {
		if (btn4 == null) {
			btn4 = new CalculatorButton("4");
		}
		return btn4;
	}
	private CalculatorButton getBtn3() {
		if (btn3 == null) {
			btn3 = new CalculatorButton("3");
		}
		return btn3;
	}
	private CalculatorButton getBtn2() {
		if (btn2 == null) {
			btn2 = new CalculatorButton("2");
		}
		return btn2;
	}
	private CalculatorButton getBtn8() {
		if (btn8 == null) {
			btn8 = new CalculatorButton("8");
		}
		return btn8;
	}
	private CalculatorButton getBtn9() {
		if (btn9 == null) {
			btn9 = new CalculatorButton("9");
		}
		return btn9;
	}
	private CalculatorButton getBtn1() {
		if (btn1 == null) {
			btn1 = new CalculatorButton("1");
		}
		return btn1;
	}
	private CalculatorButton getBtn0() {
		if (btn0 == null) {
			btn0 = new CalculatorButton("0");
		}
		return btn0;
	}
	private CalculatorButton getBtnDecimal() {
		if (btnDecimal == null) {
			btnDecimal = new CalculatorButton(".");
		}
		return btnDecimal;
	}
	private CalculatorButton getBtnPlus() {
		if (btnPlus == null) {
			btnPlus = new CalculatorButton("+");
		}
		return btnPlus;
	}
	private CalculatorButton getBtnMinus() {
		if (btnMinus == null) {
			btnMinus = new CalculatorButton("-");
		}
		return btnMinus;
	}
	private CalculatorButton getBtnMultiply() {
		if (btnMultiply == null) {
			btnMultiply = new CalculatorButton("*");
		}
		return btnMultiply;
	}
	private CalculatorButton getBtnDivide() {
		if (btnDivide == null) {
			btnDivide = new CalculatorButton("/");
		}
		return btnDivide;
	}
	private CalculatorButton getBtnErase() {
		if (btnErase == null) {
			btnErase = new CalculatorButton("<-");
			btnErase.character=(char)8;
		}
		return btnErase;
	}
	private CalculatorButton getBtnEquals() {
		if (btnEquals == null) {
			btnEquals = new CalculatorButton("=");
		}
		return btnEquals;
	}
	private CalculatorButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new CalculatorButton("C");
		}
		return btnClear;
	}
	
	private class CalculatorButton extends JButton {
		private char character;
		
		public CalculatorButton(String name) {
			super(name);
			if (name.length()>0) character = name.charAt(0);
			setFocusable(false);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doChar(character);
				}
			});
		}
	}
}
