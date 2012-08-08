package net.yapbam.evaluate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;

public class ShuntingYard {
	private static final String FUNCTION_ARGUMENT_SEPARATOR = ",";
	private static final String CLOSE_BRACKET = ")";
	private static final String OPEN_BRACKET = "(";
	
	private String tokenDelimiters;
	private HashMap<String, Object> map;
	
	public ShuntingYard(Operator[] operators, Function[] functions) {
		this.map = new HashMap<String, Object>();
		if (operators!=null) {
			for (Operator ope : operators) {
				this.map.put(new String(new char[]{ope.getChar()}), ope);
			}
		}
		if (functions!=null && functions.length>0) {
			for (Function function : functions) {
				//TODO if function name contains operators or reserved chars => error
				this.map.put(function.getName(), function);
			}			
		}
	}
	
	private synchronized String getDelimiters() {
		if (tokenDelimiters==null) {
			boolean needFunctionSeparator = false;
			StringBuilder builder = new StringBuilder();
			builder.append(" ").append(OPEN_BRACKET).append(CLOSE_BRACKET);
			for (Object obj:map.values()) {
				if (obj instanceof Operator) {
					builder.append(((Operator)obj).getChar());
				} else {
					if (((Function)obj).getArgumentCount()>1) needFunctionSeparator = true;
				}
			}
			if (needFunctionSeparator) builder.append(FUNCTION_ARGUMENT_SEPARATOR);
			tokenDelimiters = builder.toString();
		}
		return tokenDelimiters;
	}
	
	private int getPrecedence(String token) {
		return ((Operator) toObject(token)).getPrecedence();
	}

	private boolean isLeftToRight(String token) {
		return ((Operator) toObject(token)).isLeftToRight();
	}

	private boolean isOperator(String token) {
		return toObject(token) instanceof Operator;
	}

	private boolean isFunction(String token) {
		return toObject(token) instanceof Function;
	}

	public String[] toRPN(String input) {
		Stack<String> stack = new Stack<String>(); // operator stack
		LinkedList<String> output = new LinkedList<String>();
		StringTokenizer tokens = new StringTokenizer(input, getDelimiters(), true);
		while (tokens.hasMoreTokens()) {
			// read one token from the input stream
			String token = tokens.nextToken();
			if (token.equals(" ")) {
				// If the token is a space ... do nothing
			} else if (token.equals(OPEN_BRACKET)) {
				// If the token is a left parenthesis, then push it onto the stack.
				stack.push(token);
			} else if (token.equals(CLOSE_BRACKET)) {
				// If the token is a right parenthesis:
				boolean openBracketFound = false;
				// Until the token at the top of the stack is a left parenthesis,
				// pop operators off the stack onto the output queue
				while (!stack.isEmpty()) {
					String sc = stack.pop();
					if (sc.equals(OPEN_BRACKET)) {
						openBracketFound = true;
						break;
					} else {
						output.add(sc);
					}
				}
				if (!openBracketFound) {
					// If the stack runs out without finding a left parenthesis, then
					// there are mismatched parentheses.
					throw new IllegalArgumentException("Parentheses mismatched");
				}
				// If the token at the top of the stack is a function token, pop it
				// onto the output queue.
				if (!stack.isEmpty()) {
					if (isFunction(stack.peek())) {
						output.add(stack.pop());
					}
				}
			} else if (token.equals(FUNCTION_ARGUMENT_SEPARATOR)) {
				// If the token is a function argument separator (e.g., a comma):
				boolean pe = false;
				while (!stack.isEmpty()) {
					if (stack.peek().equals(OPEN_BRACKET)) {
						pe = true;
						break;
					} else {
						// Until the token at the top of the stack is a left parenthesis,
						// pop operators off the stack onto the output queue.
						output.add(stack.pop());
					}
				}
				if (!pe) {
					// If no left parentheses are encountered, either the separator was misplaced
					// or parentheses were mismatched.
					throw new IllegalArgumentException("Separator or parentheses mismatched");
				}
			} else if (isFunction(token)) {
				// If the token is a function token, then push it onto the stack.
				stack.push(token);
			} else if (isOperator(token)) {
				// If the token is an operator, op1, then:
				while (!stack.isEmpty()) {
					String sc = stack.peek();
					// While there is an operator token, o2, at the top of the stack
					// op1 is left-associative and its precedence is less than or equal
					// to that of op2,
					// or op1 has precedence less than that of op2,
					// Let + and ^ be right associative.
					// Correct transformation from 1^2+3 is 12^3+
					// The differing operator priority decides pop / push
					// If 2 operators have equal priority then associativity decides.
					if (isOperator(sc)
							&& ((isLeftToRight(token) && (getPrecedence(token) <= getPrecedence(sc))) || (getPrecedence(token) < getPrecedence(sc)))) {
						// Pop o2 off the stack, onto the output queue;
						output.add(stack.pop());
					} else {
						break;
					}
				}
				// push op1 onto the stack.
				stack.push(token);
			} else {
				// If the token is a number (identifier), then add it to the output queue.
				output.add(token);
			}
		}
		// When there are no more tokens to read:
		// While there are still operator tokens in the stack:
		while (!stack.isEmpty()) {
			String sc = stack.pop();
			if (sc.equals(OPEN_BRACKET) || sc.equals(CLOSE_BRACKET)) {
				throw new IllegalArgumentException("Parentheses mismatched");
			}
			output.add(sc);
		}
		return output.toArray(new String[output.size()]);
	}

	public String evaluate(String[] rpnTokens) {
		System.out.println("evaluation of "+Arrays.asList(rpnTokens)+":");
		String res;
		int sl = 0, resultNumber = 0;
		//TODO implement with a java.util.Stack
		String[] stack = new String[32];
		String sc;
		
		// While there are input tokens left
		for (String rpnToken : rpnTokens) {
			// Read the next token from input.
			Object token = toObject(rpnToken);
			// If the token is a value or identifier
			if (token instanceof Literal) {
				// Push it onto the stack.
				stack[sl] = ((Literal) token).getLiteral();
				++sl;
			} else {
				// Otherwise, the token is an operator (operator here includes both
				// operators, and functions).
				res = "result_" + resultNumber;
				System.out.print(res + " = ");
				++resultNumber;
				// It is known a priori that the operator takes n arguments.
				int nargs = token instanceof Function ? ((Function)token).getArgumentCount() : ((Operator)token).getArgumentCount();
				// If there are fewer than n values on the stack
				if (sl < nargs) {
					// (Error) The user has not input sufficient values in the expression.
					throw new IllegalArgumentException("Not enough args for " + (token instanceof Function ? ((Function)token).getName() : ((Operator)token).getChar()));
				}
				// Else, Pop the top n values from the stack.
				// Evaluate the operator, with the values as arguments.
				if (token instanceof Function) {
					Function function = (Function) token;
					System.out.print(function.getName()+OPEN_BRACKET);
					while (nargs > 0) {
						sc = stack[sl - nargs]; // to remove reverse order of arguments
						if (nargs > 1) {
							System.out.print(sc + ", ");
						} else {
							System.out.println(sc + CLOSE_BRACKET);
						}
						--nargs;
					}
					sl -= function.getArgumentCount();
				} else {
					Operator ope = (Operator) token;
					if (nargs == 1) {
						sc = stack[sl - 1];
						sl--;
						System.out.println(ope.getChar() + " " + sc);
					} else {
						sc = stack[sl - 2];
						System.out.print(sc + " " + ope.getChar() + " ");
						sc = stack[sl - 1];
						sl--;
						sl--;
						System.out.println(sc);
					}
				}
				// Push the returned results, if any, back onto the stack.
				stack[sl] = res;
				++sl;
			}
		}
		// If there is only one value in the stack
		// That value is the result of the calculation.
		if (sl == 1) {
			sc = stack[sl - 1];
			sl--;
			System.out.println(sc + " is a result");
			return sc;
		}
		// If there are more values in the stack
		// (Error) The user input has too many values.
		throw new IllegalArgumentException("Too many values on the stack");
	}

	private Object toObject(String token) {
		Object result = map.get(token);
		return (result!=null) ? result : new Literal(token);
	}

	public static void main(String[] args) {
		// functions: A() B(a) C(a, b), D(a, b, c) ...
		// identifiers: 0 1 2 3 ... and a b c d e ...
		// operators: = - + / * % !
		ShuntingYard sy = new ShuntingYard(Operators.SET, Functions.SET);
		String input = "1+!4*min(sin(45*(2+!3)),0.4)";
		System.out.println("input: " + input);
		String[] rpn = sy.toRPN(input);
		System.out.println("RPN: " + rpn);
		try {
			System.out.println("result=" + sy.evaluate(rpn));
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("Invalid input");
		}
		
//		try {
//			String rpn = "45 43 min 14 12 ! + *";
//			System.out.println("result=" + sy.evaluate(rpn));
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
	}
}
