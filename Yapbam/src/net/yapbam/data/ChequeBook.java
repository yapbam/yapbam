package net.yapbam.data;

import java.io.Serializable;

/** This class represents a cheque book.
 * Planned for future use (when Yapbam will manage checkBooks, maybe).
 */
public class ChequeBook implements Serializable {
	private static final long serialVersionUID = 1L;

	private String prefix;
	private int firstNumber;
	private int size;
	private int used;
	
	public ChequeBook(String prefix, int firstNumber, int size) {
		this.firstNumber = firstNumber;
		this.size = size;
		this.used = 0;
	}
	
	/** Returns the number of the next available cheque in the cheque book
	 * @return the cheque number, or null if there is no more cheque in this cheque book
	 */
	public String getNextChequeNumber() {
		if (isEmpty()) return null;
		return this.prefix + this.firstNumber + used;
	}
	
	/** Detach a cheque from the cheque book.
	 * All cheques before this one are supposed to be detached too.
	 * @param chequeNumber
	 */
	void detach (int chequeNumber) {
		int newUsed = chequeNumber - this.firstNumber;
		if (newUsed>this.used) this.used = newUsed;
	}
	
	/** Tests if the cheque book is empty
	 * @return true if it is empty
	 */
	public boolean isEmpty() {
		return used >= size;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getFirstNumber() {
		return firstNumber;
	}
	
	/** Gets the total number of cheques.
	 * @return an integer, the total number of cheques in this book. It includes the already used cheques.
	 */
	public int getChequesNumber() {
		return this.size;
	}
}
