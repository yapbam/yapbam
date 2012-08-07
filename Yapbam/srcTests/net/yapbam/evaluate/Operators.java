package net.yapbam.evaluate;

public abstract class Operators {
	private Operators(){};
	
	public final static Operator PLUS = new Operator('+', 2, true, 1);
	public final static Operator MINUS = new Operator('-', 2, true, 1);
	public final static Operator MULTIPLY = new Operator('*', 2, true, 2);
	public final static Operator DIVIDE = new Operator('/', 2, true, 2);
	public final static Operator NEGATE = new Operator('!', 1, false, 3);

	public final static Operator[] SET = new Operator[]{PLUS, MINUS, MULTIPLY, DIVIDE, NEGATE};
}
