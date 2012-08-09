package net.yapbam.evaluate;

public class Token {
	private enum Kind {
		/** Something like white space */
		IGNORED,
		OPEN_BRACKET,
		CLOSE_BRACKET,
		FUNCTION_SEPARATOR,
		FUNCTION,
		OPERATOR,
		LITERAL
	}
	public static final Token OPEN_BRACKET = new Token(Kind.OPEN_BRACKET, null);
	public static final Token CLOSE_BRACKET = new Token(Kind.CLOSE_BRACKET, null);
	public static final Token FUNCTION_ARG_SEPARATOR = new Token(Kind.FUNCTION_SEPARATOR, null);
	public static final Token IGNORED = new Token(Kind.IGNORED, null);
	
	private Kind kind;
	private Object content;
	
	public static Token buildLiteral(String literal) {
		return new Token(Kind.LITERAL, literal);
	}

	public static Token buildOperator(Operator ope) {
		return new Token(Kind.OPERATOR, ope);
	}

	public static Token buildFunction(Function function) {
		return new Token(Kind.FUNCTION, function);
	}

	private Token(Kind kind, Object content) {
		super();
		if ((kind.equals(Kind.OPERATOR) && !(content instanceof Operator)) ||
				(kind.equals(Kind.FUNCTION) && !(content instanceof Function)) ||
				(kind.equals(Kind.LITERAL) && !(content instanceof String))) throw new IllegalArgumentException();
		this.kind = kind;
		this.content = content;
	}
	
	public Operator getOperator() {
		return (Operator) this.content;
	}

	public Function getFunction() {
		return (Function) this.content;
	}

	public Kind getKind() {
		return kind;
	}

	public boolean isOperator() {
		return kind.equals(Kind.OPERATOR);
	}

	public boolean isFunction() {
		return kind.equals(Kind.FUNCTION);
	}

	public boolean isIgnored() {
		return kind.equals(Kind.IGNORED);
	}

	public boolean isOpenBracket() {
		return kind.equals(Kind.OPEN_BRACKET);
	}

	public boolean isCloseBracket() {
		return kind.equals(Kind.CLOSE_BRACKET);
	}

	public boolean isFunctionArgumentSeparator() {
		return kind.equals(Kind.FUNCTION_SEPARATOR);
	}
	
	public boolean isLiteral() {
		return kind.equals(Kind.LITERAL);
	}

	public boolean isLeftToRight() {
		return getOperator().isLeftToRight();
	}

	public int getPrecedence() {
		return getOperator().getPrecedence();
	}
}
