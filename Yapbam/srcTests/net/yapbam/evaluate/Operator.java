package net.yapbam.evaluate;

public class Operator implements Comparable<Operator> {
	private char car;
	private int precedence;
	private int argumentCount;
	private boolean isLeftToRight;

	public Operator(char car, int argumentCount, boolean isLeftToRight, int precedence) {
		if (car=='(' || car==',' || car==')' || car==' ') throw new IllegalArgumentException(); 
		this.car = car;
		this.argumentCount = argumentCount;
		this.isLeftToRight = isLeftToRight;
		this.precedence = precedence;
	}

	public char getChar() {
		return this.car;
	}

	public int getArgumentCount() {
		return this.argumentCount;
	}

	public boolean isLeftToRight() {
		return this.isLeftToRight;
	}
	
	public int getPrecedence() {
		return this.precedence;
	}

	@Override
	public int compareTo(Operator ope) {
		return this.precedence - ope.precedence;
	}
}