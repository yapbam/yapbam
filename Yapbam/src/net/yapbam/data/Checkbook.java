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
	 * @param size The number of checks still available
	 * @param next The next check number
	 */
	public Checkbook(String prefix, BigInteger start, int size, BigInteger next) {
		this.firstNumber = start;
		this.prefix = prefix;
		this.size = size;
		this.used = next.subtract(start).intValue();
		this.numberLength = start.add(BigInteger.valueOf(size)).toString().length();
	}
	
	/** Returns the number of the next available check in the checkbook
	 * @return the check number, or null if there is no more check in this checkbook
	 */
	public BigInteger getNext() {
		if (isEmpty()) return null;
		return getFirst().add(BigInteger.valueOf(used));
	}

	private String getFormatedNumber(int index) {
		String number = prefix + this.firstNumber.add(BigInteger.valueOf(index)).toString();
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
	 * @see #getNext()
	 */
	public BigInteger getFirst() {
		return firstNumber;
	}

	/** Gets the total number of checks.
	 * @return an integer, the total number of checks in this book including the already used checks.
	 * @see #getRemaining()
	 */
	public int size() {
		return this.size;
	}
	
	/** Gets the number of checks already used in this checkbook.
	 * @return a positive or null integer
	 */
	public int getUsed() {
		return this.used;
	}

	/** Gets the number of remaining checks.
	 * @return a positive or null integer
	 */
	public int getRemaining() {
		return Math.max(0, size-used);
	}

	@Override
	public String toString() {
		return prefix+"["+getFormatedNumber(0)+"-"+getFormatedNumber(size-1)+"]->"+getNextCheckNumber();
	}

	void copy(Checkbook checkbook) {
		this.firstNumber = checkbook.firstNumber;
		this.numberLength = checkbook.numberLength;
		this.prefix = checkbook.prefix;
		this.size = checkbook.size;
		this.used = checkbook.used;
	}

	public String getNextCheckNumber() {
		return getFormatedNumber(used);
	}

	/** Returns the number of a full check number (including its prefix).
	 * @param number The full number of the check.
	 * @return a BigInteger or null if the check number is not is this checkbook.
	 */
	public BigInteger getNumber(String number) {
		if (!number.startsWith(this.prefix)) return null;
		String numberString = number.substring(this.prefix.length());
		try {
			BigInteger result = new BigInteger(numberString);
			if ((result.compareTo(this.firstNumber)<0) || (result.compareTo(this.getFirst().add(BigInteger.valueOf(this.size)))>=0)) return null;
			return result;
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
