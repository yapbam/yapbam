package net.yapbam.data;

import java.io.Serializable;

/** This class represents a checkbook.
 */
public class Checkbook implements Serializable {
	private static final long serialVersionUID = 1L;

	private String prefix;
	private int firstNumber;
	private int size;
	private int used;
	
	public Checkbook(String prefix, int firstNumber, int size) {
		this.firstNumber = firstNumber;
		this.prefix = prefix;
		this.size = size;
		this.used = 0;
	}
	
	/** Returns the number of the next available check in the checkbook
	 * @return the check number, or null if there is no more check in this checkbook
	 */
	public String getNextCheckNumber() {
		if (isEmpty()) return null;
		return this.prefix + this.firstNumber + used;
	}
	
	/** Detach a check from this checkbook.
	 * All checks before this one are supposed to be detached too.
	 * @param checkNumber
	 */
	void detach (int checkNumber) {
		int newUsed = checkNumber - this.firstNumber;
		if (newUsed>this.used) this.used = newUsed;
	}
	
	/** Tests if the checkbook is empty
	 * @return true if it is empty
	 */
	public boolean isEmpty() {
		return used >= size;
	}

	/** Gets the prefix of checks in this book
	 * @return a String which is the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/** Gets the first check number of this book (not the first still available).
	 * @return an int, the check number, not including any prefix
	 * @see #getNextCheckNumber()
	 *
	public int getFirstNumber() {
		return firstNumber;
	}*/
	
	/** Gets the total number of checks.
	 * @return an integer, the total number of checks in this book including the already used checks.
	 * @see #getRemainingChecksNumber()
	 */
	public int getChecksNumber() {
		return this.size;
	}

	/** Gets the number of remaining checks.
	 * @return a povitive or null integer
	 */
	public int getRemainingCheckNumber() {
		return Math.max(0, size-used);
	}
}
