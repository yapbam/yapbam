package net.yapbam.evaluate;

import java.util.HashMap;
import java.util.StringTokenizer;

public class ShuntingYard {
	private Operator[] operators;
	private Function[] functions;
	private String tokenDelimiters;
	private HashMap<String, Object> map;
	
	public ShuntingYard(Operator[] operators, Function[] functions) {
		this.operators = operators;
		this.functions = functions;
		this.map = new HashMap<String, Object>();
		StringBuilder tokenDelimiters = new StringBuilder();
		tokenDelimiters.append(" (),");
		if (operators!=null) {
			for (Operator ope : operators) {
				this.map.put(new String(new char[]{ope.getChar()}), ope);
				tokenDelimiters.append(ope.getChar());
			}
		}
		this.tokenDelimiters = tokenDelimiters.toString();
		if (functions!=null) {
			for (Function function : functions) {
				//TODO if function name contains operators => error
				this.map.put(function.getName(), function);
			}			
		}
	}
	
	int op_preced(char c) {
		return getOperator(c).getPrecedence();
	}

	boolean op_left_assoc(char c) {
		return getOperator(c).isLeftToRight();
	}

	int op_arg_count(char c) {
		Operator ope = getOperator(c);
		if (ope!=null) {
			return ope.getArgumentCount();
		} else {
			return c - 'A';
		}
	}

	private Operator getOperator(char c) {
		for (Operator operator : operators) {
			if (operator.getChar()==c) return operator;
		}
		return null;
	}

	private boolean isOperator(char c) {
		return getOperator(c)!=null;
	}

	private boolean isFunction(char c) {
		return (c >= 'A' && c <= 'Z');
	}

	private boolean isIdent(char c) {
		return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z'));
	}

	public String toRPN(String input) {
		String[] stack = new String[32]; // operator stack
		int sl = 0; // stack length
		String sc; // used for record stack element

		StringBuilder output = new StringBuilder();
		StringTokenizer tokens = new StringTokenizer(input, tokenDelimiters, true);
		while (tokens.hasMoreTokens()) {
			// read one token from the input stream
			String token = tokens.nextToken();
			if (token.equals(" ")) {
				// If the token is a space ... do nothing
			} else if (isIdent(token)) {
				// If the token is a number (identifier), then add it to the output
				// queue.
				if (output.length() != 0) output.append(' ');
				output.append(token);
			} else if (isFunction(token)) {
				// If the token is a function token, then push it onto the stack.
				stack[sl] = token;
				++sl;
			} else if (token.equals(",")) {
				// If the token is a function argument separator (e.g., a comma):
				boolean pe = false;
				while (sl > 0) {
					sc = stack[sl - 1];
					if (sc == "(") {
						pe = true;
						break;
					} else {
						// Until the token at the top of the stack is a left parenthesis,
						// pop operators off the stack onto the output queue.
						if (output.length() != 0) output.append(' ');
						output.append(sc);
						sl--;
					}
				}
				// If no left parentheses are encountered, either the separator was
				// misplaced
				// or parentheses were mismatched.
				if (!pe) {
					throw new IllegalArgumentException("Separator or parentheses mismatched");
				}
			} else if (isOperator(token)) {
				// If the token is an operator, op1, then:
				while (sl > 0) {
					sc = stack[sl - 1];
					// While there is an operator token, o2, at the top of the stack
					// op1 is left-associative and its precedence is less than or equal
					// to that of op2,
					// or op1 has precedence less than that of op2,
					// Let + and ^ be right associative.
					// Correct transformation from 1^2+3 is 12^3+
					// The differing operator priority decides pop / push
					// If 2 operators have equal priority then associativity decides.
					if (isOperator(token)
							&& ((op_left_assoc(c) && (op_preced(c) <= op_preced(sc))) || (op_preced(c) < op_preced(sc)))) {
						// Pop o2 off the stack, onto the output queue;
						if (output.length() != 0) output.append(' ');
						output.append(sc);
						sl--;
					} else {
						break;
					}
				}
				// push op1 onto the stack.
				stack[sl] = c;
				++sl;
			} else if (token.equals("(")) {
				// If the token is a left parenthesis, then push it onto the stack.
				stack[sl] = token;
				++sl;
			} else if (token.equals(")")) {
				// If the token is a right parenthesis:
				boolean pe = false;
				// Until the token at the top of the stack is a left parenthesis,
				// pop operators off the stack onto the output queue
				while (sl > 0) {
					sc = stack[sl - 1];
					if (sc.equals("(")) {
						pe = true;
						break;
					} else {
						if (output.length() != 0) output.append(' ');
						output.append(sc);
						sl--;
					}
				}
				// If the stack runs out without finding a left parenthesis, then
				// there are mismatched parentheses.
				if (!pe) {
					throw new IllegalArgumentException("Parentheses mismatched");
				}
				// Pop the left parenthesis from the stack, but not onto the output
				// queue.
				sl--;
				// If the token at the top of the stack is a function token, pop it
				// onto the output queue.
				if (sl > 0) {
					sc = stack[sl - 1];
					if (isFunction(sc)) {
						if (output.length() != 0) output.append(' ');
						output.append(sc);
						sl--;
					}
				}
			}
		}
		// When there are no more tokens to read:
		// While there are still operator tokens in the stack:
		while (sl > 0) {
			sc = stack[sl - 1];
			if (sc.equals("(") || sc.equals(")")) {
				throw new IllegalArgumentException("Parentheses mismatched");
			}
			if (output.length() != 0) output.append(' ');
			output.append(sc);
			--sl;
		}
		return output.toString();
	}

	public String evaluate(String rpnExpression) {
		System.out.println("order of "+rpnExpression+":");
		String res;
		int sl = 0, resultNumber = 0;
		//TODO implement with a java.util.Stack
		String[] stack = new String[32];
		String sc;
		
		StringTokenizer tokens = new StringTokenizer(rpnExpression, " ", false);
		// While there are input tokens left
		while (tokens.hasMoreTokens()) {
			// Read the next token from input.
			Object token = getNext(tokens.nextToken());
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
					System.out.print(function.getName()+"(");
					while (nargs > 0) {
						sc = stack[sl - nargs]; // to remove reverse order of arguments
						if (nargs > 1) {
							System.out.print(sc + ", ");
						} else {
							System.out.println(sc + ")");
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

	private Object getNext(String token) {
		Object result = map.get(token);
		return (result!=null) ? result : new Literal(token);
	}

	public static void main(String[] args) {
		// functions: A() B(a) C(a, b), D(a, b, c) ...
		// identifiers: 0 1 2 3 ... and a b c d e ...
		// operators: = - + / * % !
//		String input = "a = D(f - b * c + d, !e, g)";
		ShuntingYard sy = new ShuntingYard(Operators.SET, Functions.SET);
//		String input = "1+!4*B(x)+C(y,z)";
//		System.out.println("input: " + input);
//		String rpn = sy.toRPN(input);
//		System.out.println("RPN: " + rpn);
//		try {
//			System.out.println("result=" + sy.evaluate(rpn));
//		} catch (Throwable e) {
//			e.printStackTrace();
//			System.out.println("Invalid input");
//		}
		
		try {
			String rpn = "45 43 min 14 12 ! + *";
			System.out.println("result=" + sy.evaluate(rpn));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
