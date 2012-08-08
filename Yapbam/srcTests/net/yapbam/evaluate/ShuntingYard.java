package net.yapbam.evaluate;

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
	
	Stack<Token> stack;
	LinkedList<Token> output = new LinkedList<Token>();
	private void output(Token token) {
		output.add(token);
	}

	public Token[] toRPN(String input) {
		//TODO stack and output should be local variables (for multithreading considerations)
		stack = new Stack<Token>(); // operator stack
		output = new LinkedList<Token>();
		StringTokenizer tokens = new StringTokenizer(input, getDelimiters(), true);
		while (tokens.hasMoreTokens()) {
			// read one token from the input stream
			Token token = toToken(tokens.nextToken());
			if (token.isIgnored()) {
				// If the token is a space ... do nothing
			} else if (token.isOpenBracket()) {
				// If the token is a left parenthesis, then push it onto the stack.
				stack.push(token);
			} else if (token.isCloseBracket()) {
				// If the token is a right parenthesis:
				boolean openBracketFound = false;
				// Until the token at the top of the stack is a left parenthesis,
				// pop operators off the stack onto the output queue
				while (!stack.isEmpty()) {
					Token sc = stack.pop();
					if (sc.isOpenBracket()) {
						openBracketFound = true;
						break;
					} else {
						output(sc);
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
					if (stack.peek().isFunction()) {
						output(stack.pop());
					}
				}
			} else if (token.isFunctionArgumentSeparator()) {
				// If the token is a function argument separator (e.g., a comma):
				boolean pe = false;
				while (!stack.isEmpty()) {
					if (stack.peek().isOpenBracket()) {
						pe = true;
						break;
					} else {
						// Until the token at the top of the stack is a left parenthesis,
						// pop operators off the stack onto the output queue.
						output(stack.pop());
					}
				}
				if (!pe) {
					// If no left parentheses are encountered, either the separator was misplaced
					// or parentheses were mismatched.
					throw new IllegalArgumentException("Separator or parentheses mismatched");
				}
			} else if (token.isFunction()) {
				// If the token is a function token, then push it onto the stack.
				stack.push(token);
			} else if (token.isOperator()) {
				// If the token is an operator, op1, then:
				while (!stack.isEmpty()) {
					Token sc = stack.peek();
					// While there is an operator token, o2, at the top of the stack
					// op1 is left-associative and its precedence is less than or equal
					// to that of op2,
					// or op1 has precedence less than that of op2,
					// Let + and ^ be right associative.
					// Correct transformation from 1^2+3 is 12^3+
					// The differing operator priority decides pop / push
					// If 2 operators have equal priority then associativity decides.
					if (sc.isOperator()
							&& ((token.isLeftToRight() && (token.getPrecedence() <= sc.getPrecedence())) || (token.getPrecedence() < sc.getPrecedence()))) {
						// Pop o2 off the stack, onto the output queue;
						output(stack.pop());
					} else {
						break;
					}
				}
				// push op1 onto the stack.
				stack.push(token);
			} else {
				// If the token is a number (identifier), then add it to the output queue.
				output(token);
			}
		}
		// When there are no more tokens to read:
		// While there are still operator tokens in the stack:
		while (!stack.isEmpty()) {
			Token sc = stack.pop();
			if (sc.isOpenBracket() || sc.isCloseBracket()) {
				throw new IllegalArgumentException("Parentheses mismatched");
			}
			output(sc);
		}
		return output.toArray(new Token[output.size()]);
	}

	private Token toToken(String token) {
		if (token.equals(" ")) {
			return Token.IGNORED;
		} else if (token.equals(OPEN_BRACKET)) {
			return Token.OPEN_BRACKET;
		} else if (token.equals(CLOSE_BRACKET)) {
			return Token.CLOSE_BRACKET;
		} else if (token.equals(FUNCTION_ARGUMENT_SEPARATOR)) {
			return Token.FUNCTION_ARG_SEPARATOR;
		}
		Object result = map.get(token);
		if (result==null) return Token.buildLiteral(token);
		if (result instanceof Function) return Token.buildFunction((Function) result);
		return Token.buildOperator((Operator) result);
	}

//	public String evaluate(Token[] rpnTokens) {
//		System.out.println("evaluation of "+Arrays.asList(rpnTokens)+":");
//		String res;
//		int sl = 0, resultNumber = 0;
//		//TODO implement with a java.util.Stack
//		String[] stack = new String[32];
//		String sc;
//		
//		// While there are input tokens left
//		for (Token token : rpnTokens) {
//			// If the token is a value or identifier
//			if (token.isLiteral()) {
//				// Push it onto the stack.
//				stack[sl] = ((Literal) token).getLiteral();
//				++sl;
//			} else {
//				// Otherwise, the token is an operator (operator here includes both
//				// operators, and functions).
//				res = "result_" + resultNumber;
//				System.out.print(res + " = ");
//				++resultNumber;
//				// It is known a priori that the operator takes n arguments.
//				int nargs = token instanceof Function ? ((Function)token).getArgumentCount() : ((Operator)token).getArgumentCount();
//				// If there are fewer than n values on the stack
//				if (sl < nargs) {
//					// (Error) The user has not input sufficient values in the expression.
//					throw new IllegalArgumentException("Not enough args for " + (token instanceof Function ? ((Function)token).getName() : ((Operator)token).getChar()));
//				}
//				// Else, Pop the top n values from the stack.
//				// Evaluate the operator, with the values as arguments.
//				if (token instanceof Function) {
//					Function function = (Function) token;
//					System.out.print(function.getName()+OPEN_BRACKET);
//					while (nargs > 0) {
//						sc = stack[sl - nargs]; // to remove reverse order of arguments
//						if (nargs > 1) {
//							System.out.print(sc + ", ");
//						} else {
//							System.out.println(sc + CLOSE_BRACKET);
//						}
//						--nargs;
//					}
//					sl -= function.getArgumentCount();
//				} else {
//					Operator ope = (Operator) token;
//					if (nargs == 1) {
//						sc = stack[sl - 1];
//						sl--;
//						System.out.println(ope.getChar() + " " + sc);
//					} else {
//						sc = stack[sl - 2];
//						System.out.print(sc + " " + ope.getChar() + " ");
//						sc = stack[sl - 1];
//						sl--;
//						sl--;
//						System.out.println(sc);
//					}
//				}
//				// Push the returned results, if any, back onto the stack.
//				stack[sl] = res;
//				++sl;
//			}
//		}
//		// If there is only one value in the stack
//		// That value is the result of the calculation.
//		if (sl == 1) {
//			sc = stack[sl - 1];
//			sl--;
//			System.out.println(sc + " is a result");
//			return sc;
//		}
//		// If there are more values in the stack
//		// (Error) The user input has too many values.
//		throw new IllegalArgumentException("Too many values on the stack");
//	}

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
		Token[] rpn = sy.toRPN(input);
		System.out.println("RPN: " + rpn);
//		try {
//			System.out.println("result=" + sy.evaluate(rpn));
//		} catch (Throwable e) {
//			e.printStackTrace();
//			System.out.println("Invalid input");
//		}
		
//		try {
//			String rpn = "45 43 min 14 12 ! + *";
//			System.out.println("result=" + sy.evaluate(rpn));
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
	}
}
