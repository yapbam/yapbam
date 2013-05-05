package net.yapbam.data.event;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.util.NullUtils;

/** This event is sent when a mode is updated. */
public class ModePropertyChangedEvent extends DataEvent {
	/** Constant that signifies "the name was changed"
	 * @see #getChanges()
	 */
	public static final int NAME = 1;
	/** Constant that signifies "the expenses value date computer was changed"
	 * @see #getChanges()
	 */
	public static final int EXPENSE_VDC = 2;
	/** Constant that signifies "the receipts value date computer was changed"
	 * @see #getChanges()
	 */
	public static final int RECEIPT_VDC = 4;
	/** Constant that signifies "the use checkbook was changed"
	 * @see #getChanges()
	 */
	public static final int CHECKBOOK = 8;

	private int changes;
	private Account account;
	private Mode oldMode;
	private Mode newMode;

	/** Constructor.
	 * @param globalData The dataset on which the event occurs (it will be returned by getSource() method. 
	 * @param account The account that owned the mode
	 * @param oldMode The mode before the update
	 * @param newMode The updated mode
	 */
	public ModePropertyChangedEvent(GlobalData globalData, Account account, Mode oldMode, Mode newMode) {
		super(globalData);
		this.account = account;
		this.oldMode = oldMode;
		this.newMode = newMode;
		changes = 0;
		if (!NullUtils.areEquals(oldMode.getName(),newMode.getName())) changes += NAME;
		if (!NullUtils.areEquals(oldMode.getExpenseVdc(),newMode.getExpenseVdc())) changes += EXPENSE_VDC;
		if (!NullUtils.areEquals(oldMode.getReceiptVdc(),newMode.getReceiptVdc())) changes += RECEIPT_VDC;
		if (oldMode.isUseCheckBook()!=newMode.isUseCheckBook()) changes += CHECKBOOK;
	}

	/** Returns the changes on the updated mode.
	 * @return an integer, sum of the constant defined in this class. 0, if no changes were made.
	 * @see #CHECKBOOK
	 * @see #NAME
	 * @see #EXPENSE_VDC
	 * @see #RECEIPT_VDC
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

	/** Returns the mode as it was before the update.
	 * @return a mode. Please note that the instance is not the same as getNewMode()
	 */
	public Mode getOldMode() {
		return oldMode;
	}

	/** Returns the mode as it is after the update.
	 * @return a mode. Please note that the instance is not the same as getOldMode()
	 */
	public Mode getNewMode() {
		return newMode;
	}
}
