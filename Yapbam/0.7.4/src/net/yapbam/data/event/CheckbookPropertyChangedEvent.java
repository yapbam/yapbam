package net.yapbam.data.event;

import net.yapbam.data.Account;
import net.yapbam.data.Checkbook;
import net.yapbam.data.GlobalData;
import net.yapbam.util.NullUtils;

/** This event is sent when a checkbook is updated. */
public class CheckbookPropertyChangedEvent extends DataEvent {
	/** Constant that signifies "the prefix was changed"
	 * @see #getChanges()
	 */
	public static final int PREFIX = 1;

	/** Constant that signifies "the first check number was changed"
	 * @see #getChanges()
	 */
	public static final int FIRST = 2;
	
	/** Constant that signifies "the next check number was changed"
	 * @see #getChanges()
	 */
	public static final int NEXT = 4;
	
	/** Constant that signifies "the number of checks in the checkbook was changed"
	 * @see #getChanges()
	 */
	public static final int SIZE = 8;


	private int changes;
	private Account account;
	private Checkbook oldBook;
	private Checkbook newBook;

	/** Constructor.
	 * @param globalData The dataset on which the event occurs (it will be returned by getSource() method. 
	 * @param account The account that owned the checkbook
	 * @param oldBook The checkbook before the update
	 * @param newBook The updated checkbook
	 */
	public CheckbookPropertyChangedEvent(GlobalData globalData, Account account, Checkbook oldBook, Checkbook newBook) {
		super(globalData);
		this.account = account;
		this.oldBook = oldBook;
		this.newBook = newBook;
		changes = 0;
		if (!oldBook.getPrefix().equals(newBook.getPrefix())) changes += PREFIX;
		if (!oldBook.getFirst().equals(newBook.getFirst())) changes += FIRST;
		if (!NullUtils.areEquals(oldBook.getNext(),newBook.getNext())) changes += NEXT;
		if (oldBook.size()!=newBook.size()) changes += SIZE;
	}

	/** Returns the changes on the updated checkbook.
	 * @return an integer, sum of the constant defined in this class. 0, if no changes were made.
	 * @see #PREFIX
	 * @see #FIRST
	 * @see #NEXT
	 * @see #SIZE
	 */
	public int getChanges() {
		return changes;
	}

	/** Returns the account owning the updated mode.
	 * @return an account
	 */
	public Account getAccount() {
		return account;
	}

	/** Returns the checkbook as it was before the update.
	 * @return a checkbook. Please note that the instance is not the same as getNewCheckbook()
	 */
	public Checkbook getOldCheckbook() {
		return oldBook;
	}

	/** Returns the checkbook as it is after the update.
	 * @return a checkbook. Please note that the instance is not the same as getOldCheckbook()
	 */
	public Checkbook getNewCheckbook() {
		return newBook;
	}
}
