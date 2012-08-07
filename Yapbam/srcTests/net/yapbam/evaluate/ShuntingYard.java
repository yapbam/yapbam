package net.yapbam.evaluate;

public class ShuntingYard {
	private Operator[] operators;
	
	public ShuntingYard(Operator[] operators) {
		this.operators = operators;
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
		char[] stack = new char[32]; // operator stack
		int sl = 0; // stack length
		char sc; // used for record stack element

		StringBuilder output = new StringBuilder();

		for (int i = 0; i < input.length(); i++) {
			// read one token from the input stream
			char c = input.charAt(i);
			if (c != ' ') {
				// If the token is a number (identifier), then add it to the output
				// queue.
				if (isIdent(c)) {
					if (output.length()!=0) output.append(' ');
					output.append(c);
				}
				// If the token is a function token, then push it onto the stack.
				else if (isFunction(c)) {
					stack[sl] = c;
					++sl;
				}
				// If the token is a function argument separator (e.g., a comma):
				else if (c == ',') {
					boolean pe = false;
					while (sl > 0) {
						sc = stack[sl - 1];
						if (sc == '(') {
							pe = true;
							break;
						} else {
							// Until the token at the top of the stack is a left parenthesis,
							// pop operators off the stack onto the output queue.
							if (output.length()!=0) output.append(' ');
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
				}
				// If the token is an operator, op1, then:
				else if (isOperator(c)) {
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
						if (isOperator(sc) && ((op_left_assoc(c) && (op_preced(c) <= op_preced(sc))) || (op_preced(c) < op_preced(sc)))) {
							// Pop o2 off the stack, onto the output queue;
							if (output.length()!=0) output.append(' ');
							output.append(sc);
							sl--;
						} else {
							break;
						}
					}
					// push op1 onto the stack.
					stack[sl] = c;
					++sl;
				}
				// If the token is a left parenthesis, then push it onto the stack.
				else if (c == '(') {
					stack[sl] = c;
					++sl;
				}
				// If the token is a right parenthesis:
				else if (c == ')') {
					boolean pe = false;
					// Until the token at the top of the stack is a left parenthesis,
					// pop operators off the stack onto the output queue
					while (sl > 0) {
						sc = stack[sl - 1];
						if (sc == '(') {
							pe = true;
							break;
						} else {
							if (output.length()!=0) output.append(' ');
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
							if (output.length()!=0) output.append(' ');
							output.append(sc);
							sl--;
						}
					}
				} else {
					throw new IllegalArgumentException("Unknown token " + c);
				}
			}
		}
		// When there are no more tokens to read:
		// While there are still operator tokens in the stack:
		while (sl > 0) {
			sc = stack[sl - 1];
			if (sc == '(' || sc == ')') {
				throw new IllegalArgumentException("Parentheses mismatched");
			}
			if (output.length()!=0) output.append(' ');
			output.append(sc);
			--sl;
		}
		return output.toString();
	}

	public String evaluate(String rpnExpression) {
		System.out.println("order:");
		String res;
		int sl = 0, rn = 0;
		String[] stack = new String[32];
		String sc;
		// While there are input tokens left
		for (int i = 0; i < rpnExpression.length(); i++) {
			// Read the next token from input.
			char c = rpnExpression.charAt(i);
			// If the token is a value or identifier
			if (isIdent(c)) {
				// Push it onto the stack.
				stack[sl] = new String(new char[] { c });
				++sl;
			}
			// Otherwise, the token is an operator (operator here includes both
			// operators, and functions).
			else if (isOperator(c) || isFunction(c)) {
				res = "result_" + rn;
				System.out.print(res + " = ");
				++rn;
				// It is known a priori that the operator takes n arguments.
				int nargs = op_arg_count(c);
				// If there are fewer than n values on the stack
				if (sl < nargs) {
					// (Error) The user has not input sufficient values in the expression.
					throw new IllegalArgumentException("Not enough args for " + c);
				}
				// Else, Pop the top n values from the stack.
				// Evaluate the operator, with the values as arguments.
				if (isFunction(c)) {
					System.out.print(c+"(");
					while (nargs > 0) {
						sc = stack[sl - nargs]; // to remove reverse order of arguments
						if (nargs > 1) {
							System.out.print(sc + ", ");
						} else {
							System.out.println(sc + ")");
						}
						--nargs;
					}
					sl -= op_arg_count(c);
				} else {
					if (nargs == 1) {
						sc = stack[sl - 1];
						sl--;
						System.out.println(c + " " + sc);
					} else {
						sc = stack[sl - 2];
						System.out.print(sc + " " + c + " ");
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

	public static void main(String[] args) {
		// functions: A() B(a) C(a, b), D(a, b, c) ...
		// identifiers: 0 1 2 3 ... and a b c d e ...
		// operators: = - + / * % !
//		String input = "a = D(f - b * c + d, !e, g)";
		String input = "1+4*B(x)+C(y,z)";
		ShuntingYard sy = new ShuntingYard(Operators.SET);
		System.out.println("input: " + input);
		String rpn = sy.toRPN(input);
		System.out.println("RPN: " + rpn);
		try {
			System.out.println("result=" + sy.evaluate(rpn));
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("Invalid input");
		}
		
		try {
			rpn = "14 12 +";
			System.out.println("result=" + sy.evaluate(rpn));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
