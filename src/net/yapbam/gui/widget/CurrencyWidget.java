package net.yapbam.gui.widget;

import java.util.Locale;

import net.astesana.ajlib.utilities.LocalizationData;
import net.astesana.javaluator.BracketPair;
import net.astesana.javaluator.DoubleEvaluator;
import net.astesana.javaluator.Parameters;

@SuppressWarnings("serial")
public class CurrencyWidget extends net.astesana.ajlib.swing.widget.CurrencyWidget {
	private static final DoubleEvaluator EVALUATOR = new Evaluator();
	
	private static class Evaluator extends DoubleEvaluator {
		Evaluator() {
			super(buildParameters());
		}

		private static Parameters buildParameters() {
			Parameters params = new Parameters();
			params.addExpressionBracket(BracketPair.PARENTHESES);
			params.add(DoubleEvaluator.MULTIPLY);
			params.add(DoubleEvaluator.DIVIDE);
			params.add(DoubleEvaluator.PLUS);
			params.add(DoubleEvaluator.MINUS);
			params.add(DoubleEvaluator.NEGATE);
			return params;
		}

		@Override
		protected Double toValue(String literal, Object evaluationContext) {
			//TODO
			return super.toValue(literal, evaluationContext);
		}
	}

	public CurrencyWidget(Locale locale) {
		super(locale);
	}

	public CurrencyWidget() {
		this(LocalizationData.DEFAULT.getLocale());
	}

	@Override
	protected Number parseValue(String text) {
		Number result = super.parseValue(text);
		if (result==null) {
			// Maybe its a formula
			try {
				result = EVALUATOR.evaluate(text);
			} catch (IllegalArgumentException e) {
				// The expression is invalid
			}
		}
		return result;
	}
}
