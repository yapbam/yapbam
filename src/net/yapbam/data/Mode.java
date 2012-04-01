package net.yapbam.data;

import java.io.Serializable;

import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.gui.LocalizationData;

/** A payment mode (Blue card, check ...).
 */
public class Mode implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** Undefined mode (useable for receipts and expenses, the date value is the operation date). */ 
	public static final Mode UNDEFINED = new Mode(null,DateStepper.IMMEDIATE, DateStepper.IMMEDIATE, false);
	
	private String name;
	private DateStepper receiptVDC;
	private DateStepper expenseVDC;
	private boolean useCheckBook;

	/** Constructor
	 * @param name The name
	 * @param receiptVDC A ValueDateComputer used to compute value date for receipts, or null if this mode
	 *  can't be used for receipts
	 * @param expenseVDC A ValueDateComputer used to compute value date for expenditures, or null if this mode
	 *  can't be used for receipts
	 * @param useCheckbook true if this mode use a checkbook
	 * @throws IllegalArgumentException if useCheckbook is true and vdcForExpenditure is false;
	 */
	public Mode(String name, DateStepper receiptVDC,
			DateStepper expenseVDC, boolean useCheckbook) {
		super();
		if (useCheckbook && (expenseVDC==null)) throw new IllegalArgumentException();
		this.name = name;
		this.receiptVDC = receiptVDC;
		this.expenseVDC = expenseVDC;
		this.useCheckBook = useCheckbook;
	}

	public String getName() {
		return name == null?LocalizationData.get("Mode.undefined"):name; //$NON-NLS-1$
	}

	public DateStepper getReceiptVdc() {
		return receiptVDC;
	}

	public DateStepper getExpenseVdc() {
		return expenseVDC;
	}

	public boolean isUseCheckBook() {
		return useCheckBook;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) return false;
		return NullUtils.areEquals(this.getName(),((Mode)obj).getName());
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}
	
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return this.getName() + "[" + this.getExpenseVdc()+"/"+this.getReceiptVdc()+"-"+(this.isUseCheckBook()?"check":"no check")+"]";
	}

	void updateTo(Mode newMode) {
		this.name = newMode.name;
		this.expenseVDC = newMode.expenseVDC;
		this.receiptVDC = newMode.receiptVDC;
		this.useCheckBook = newMode.useCheckBook;
	}
}
