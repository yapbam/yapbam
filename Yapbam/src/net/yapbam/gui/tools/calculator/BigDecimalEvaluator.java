package net.yapbam.gui.tools.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Iterator;

import net.astesana.javaluator.AbstractEvaluator;
import net.astesana.javaluator.BracketPair;
import net.astesana.javaluator.DoubleEvaluator;
import net.astesana.javaluator.Operator;
import net.astesana.javaluator.Parameters;

public class BigDecimalEvaluator extends AbstractEvaluator<BigDecimal> {
	private static final Parameters PARAMS;
	
	static {
		PARAMS = new Parameters();
		PARAMS.addExpressionBracket(BracketPair.PARENTHESES);
		PARAMS.add(DoubleEvaluator.NEGATE);
		PARAMS.add(DoubleEvaluator.MINUS);
		PARAMS.add(DoubleEvaluator.PLUS);
		PARAMS.add(DoubleEvaluator.MULTIPLY);
		PARAMS.add(DoubleEvaluator.DIVIDE);
	}

	private MathContext mathContext;

	public BigDecimalEvaluator(MathContext context) {
		super(PARAMS);
		this.mathContext = context;
	}

	@Override
	protected BigDecimal toValue(String literal, Object evaluationContext) {
		try {
			return new BigDecimal(literal);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
	}

	/* (non-Javadoc)
	 * @see net.astesana.javaluator.AbstractEvaluator#evaluate(net.astesana.javaluator.Operator, java.util.Iterator, java.lang.Object)
	 */
	@Override
	protected BigDecimal evaluate(Operator operator, Iterator<BigDecimal> operands, Object evaluationContext) {
		if (operator.equals(DoubleEvaluator.NEGATE)) {
			return operands.next().negate();
		} else {
			BigDecimal ope1 = operands.next();
			BigDecimal ope2 = operands.next();
			if (operator.equals(DoubleEvaluator.MINUS)) {
				return ope1.subtract(ope2);
			} else if (operator.equals(DoubleEvaluator.PLUS)) {
				return ope1.add(ope2);
			} else if (operator.equals(DoubleEvaluator.MULTIPLY)) {
				return ope1.multiply(ope2);
			} else if (operator.equals(DoubleEvaluator.DIVIDE)) {
				return ope1.divide(ope2, mathContext);
			}
		}
		return super.evaluate(operator, operands, evaluationContext);
	}
}
