package net.yapbam.evaluate;

public class Function {
	private String name;
	private int argumentCount;

	public Function(String name, int argumentCount) {
		if (name.contains("(") || name.contains(",") || name.contains(")") || name.contains(" ")) throw new IllegalArgumentException(); 
		this.name = name;
		this.argumentCount = argumentCount;
	}

	public String getName() {
		return this.name;
	}

	public int getArgumentCount() {
		return this.argumentCount;
	}
}