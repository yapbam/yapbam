package net.yapbam.gui.widget;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.fathzer.soft.javaluator.BracketPair;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Parameters;

import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class CurrencyWidget extends com.fathzer.soft.ajlib.swing.widget.CurrencyWidget {
	private static final DoubleEvaluator EVALUATOR = new Evaluator();
	private static final DecimalFormat US_FORMAT = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
	
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
			params.setFunctionArgumentSeparator(';');
			return params;
		}

		@Override
		protected Double toValue(String literal, Object evaluationContext) {
			CurrencyWidget widget = (CurrencyWidget)evaluationContext;
			Number result = widget.parseLiteral(literal);
			if (result==null) {
				throw new IllegalArgumentException();
			}
			return result.doubleValue();
		}
	}

	public CurrencyWidget(Locale locale) {
		super(locale);
	}

	public CurrencyWidget() {
		this(LocalizationData.getLocale());
	}

	@Override
	protected Number parseValue(String text) {
		// Try to parse the string as a literal
		Number result = parseLiteral(text);
		if (result==null) {
			// Maybe its a formula
			try {
				result = EVALUATOR.evaluate(text, this);
			} catch (IllegalArgumentException e) {
				// The expression is invalid
			}
		}
		return result;
	}

	private Number parseLiteral(String text) {
		Number result = super.parseValue(text);
		if (result==null) {
			// Maybe its an us formatted number
			result = safeParse(US_FORMAT, text);
		}
		return result;
	}
}
