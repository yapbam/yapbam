package net.yapbam.evaluate;

public abstract class Functions {
	//TODO Add more functions
	//TODO implements functions with variable args
	private Functions(){};
	
	public final static Function SINUS = new Function("sin", 1);
	public final static Function MIN = new Function("min", 2);

	public final static Function[] SET = new Function[]{SINUS, MIN};
}
