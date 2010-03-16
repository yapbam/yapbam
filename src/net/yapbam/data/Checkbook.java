package net.yapbam.data;

import java.io.Serializable;
import java.math.BigInteger;

/** This class represents a checkbook.
 */
public class Checkbook implements Serializable {
	private static final long serialVersionUID = 1L;

	private String prefix;
	private BigInteger firstNumber;
	private int size;
	private int used;
	private int numberLength;
	
	/** Constructor.
	 * @param prefix The check number prefix; the non numerical part preceding the number itself
	 * @param start The first available check number, not including the prefix
	 * @param numberLength The number of characters used to represent the number (this is mandatory to have the right number of leading zeros in the number)
	 * @param size The number of checks still available
	 */
	public Checkbook(String prefix, BigInteger start, int numberLength, int size, int nextIndex) {
		this.firstNumber = start;
		this.prefix = prefix;
		this.size = size;
		this.used = nextIndex;
		this.numberLength = numberLength;
	}
	
	/** Returns the number of the next available check in the checkbook
	 * @return the check number, or null if there is no more check in this checkbook
	 */
	public String getNextCheckNumber() {
		if (isEmpty()) return null;
		return this.prefix + getFormatedNumber(used);
	}

	private String getFormatedNumber(int index) {
		String number = this.firstNumber.add(BigInteger.valueOf(index)).toString();
		StringBuffer leadingZeros = new StringBuffer();
		for (int i = number.length(); i < this.numberLength; i++) {
			leadingZeros.append('0');
		}
		number = leadingZeros + number;
		return number;
	}
	
	/** Detach a check from this checkbook.
	 * All checks before this one are supposed to be detached too.
	 * @param checkNumber
	 */
/*	void detach (int checkNumber) {
		int newUsed = checkNumber - this.firstNumber;
		if (newUsed>this.used) this.used = newUsed;
	}*/
	
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
	 * @return an BigInteger, the check number, not including any prefix
	 * @see #getNextCheckNumber()
	 */
	public BigInteger getFirstNumber() {
		return firstNumber;
	}

	/** Gets the total number of checks.
	 * @return an integer, the total number of checks in this book including the already used checks.
	 * @see #getRemainingChecksNumber()
	 */
	public int getChecksNumber() {
		return this.size;
	}
	
	public int getNumberLength() {
		return this.numberLength;
	}
	
	public int getUsed() {
		return this.used;
	}

	/** Gets the number of remaining checks.
	 * @return a povitive or null integer
	 */
	public int getRemainingChecksNumber() {
		return Math.max(0, size-used);
	}

	@Override
	public String toString() {
		return prefix+"["+getFormatedNumber(0)+"-"+getFormatedNumber(size)+"]->"+getNextCheckNumber();
	}
}
