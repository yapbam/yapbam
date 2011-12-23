package net.yapbam.data;

import java.io.Serializable;
import java.math.BigInteger;

/** A checkbook.
 * A check number is composed of a prefix, which could be not numeric and a number.
 * All the check in a checkbook share the same prefix and have consecutive numbers.
 * The concatenation of prefix and number is the check "Full number".
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
		this.numberLength = start.add(BigInteger.valueOf(size)).toString().length();
		if (next!=null) {
			this.used = next.subtract(start).intValue();
			if (used>=size) throw new IllegalArgumentException();
		} else {
			used = size;
		}
	}
	
	/** Gets the number of the next available check in the checkbook
	 * @return the check number, or null if there is no more check in this checkbook
	 */
	public BigInteger getNext() {
		if (isEmpty()) return null;
		return getFirst().add(BigInteger.valueOf(used));
	}

	/** Gets the number of a check by its index (position) in the checkbook.
	 * @param index the check's index (0 is the index of the first check).
	 * @return the check number, or null if the checkbook has less than index-1 checks
	 */
	public BigInteger get(int index) {
		if (index>=this.size) return null;
		return getFirst().add(BigInteger.valueOf(index));
	}

	/** Converts a short check number (without prefix) to a full check number (with the prefix).
	 * @param shortNumber the short check number
	 * @return the full check number or null if the argument is null.
	 * @see #getNumber(String)
	 */
	public String getFullNumber(BigInteger shortNumber) {
		if (shortNumber==null) return null;
		String number = prefix + shortNumber.toString();
		StringBuilder leadingZeros = new StringBuilder();
		for (int i = number.length(); i < this.numberLength; i++) {
			leadingZeros.append('0');
		}
		number = leadingZeros + number;
		return number;
	}
		
	/** Tests whether the checkbook is empty or not
	 * @return true if this is empty
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

	/** Gets the first check short number of this book (not the first still available).
	 * @return an BigInteger, the check number, not including any prefix
	 * @see #getNext()
	 */
	public BigInteger getFirst() {
		return firstNumber;
	}
	
	/** Gets the last check short number of this book.
	 * @return an BigInteger, the check number, not including any prefix
	 * @see #getFirst()
	 */
	public BigInteger getLast() {
		return this.firstNumber.add(BigInteger.valueOf(size-1));
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
		return prefix+"["+getFullNumber(getNext())+"-"+getFullNumber(getLast())+"]->"+getFullNumber(getNext()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	void copy(Checkbook checkbook) {
		this.firstNumber = checkbook.firstNumber;
		this.numberLength = checkbook.numberLength;
		this.prefix = checkbook.prefix;
		this.size = checkbook.size;
		this.used = checkbook.used;
	}

	/** Returns the short number (without prefix) of a full check number (including its prefix).
	 * @param fullNumber The full number of the check.
	 * @return a BigInteger or null if the check number is not is this checkbook.
	 * @see #getFullNumber(BigInteger)
	 */
	public BigInteger getNumber(String fullNumber) {
		if (!fullNumber.startsWith(this.prefix)) return null;
		String numberString = fullNumber.substring(this.prefix.length());
		try {
			BigInteger result = new BigInteger(numberString);
			if ((result.compareTo(this.firstNumber)<0) || (result.compareTo(this.getFirst().add(BigInteger.valueOf(this.size)))>=0)) return null;
			return result;
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
