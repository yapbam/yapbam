package net.yapbam.data;

import java.io.Serializable;

import net.yapbam.date.helpers.DateStepper;
import net.yapbam.gui.LocalizationData;

/** This class represents a paiement mode (Blue card, cheque ...) */
public class Mode implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** Undefined mode (useable for receipts and expenses, the date value is the operation date). */ 
	public static final Mode UNDEFINED = new Mode(null,DateStepper.IMMEDIATE, DateStepper.IMMEDIATE, false);
	
	private String name;
	private DateStepper receiptVDC;
	private DateStepper expenseVDC;
	private boolean useChequeBook;

	/** Construtor
	 * @param name The name
	 * @param receiptVDC A ValueDateComputer used to compute value date for receipts, or null this mode
	 *  can't be used for receipts
	 * @param expenseVDC A ValueDateComputer used to compute value date for expenditures, or null this mode
	 *  can't be used for receipts
	 * @param useChequeBook true if this mode use a cheque book
	 * @throws IllegalArgumentException if useChequeBook is true and vdcForExpenditure is false;
	 */
	public Mode(String name, DateStepper receiptVDC,
			DateStepper expenseVDC, boolean useChequeBook) {
		super();
		if (useChequeBook && (expenseVDC==null)) throw new IllegalArgumentException();
		this.name = name;
		this.receiptVDC = receiptVDC;
		this.expenseVDC = expenseVDC;
		this.useChequeBook = useChequeBook;
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

	public boolean isUseChequeBook() {
		return useChequeBook;
	}

	@Override
	public boolean equals(Object obj) {
		return this.getName().equals(((Mode)obj).getName());
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}
	
	@Override
	public String toString() {
		return this.getName() + "[" + this.getExpenseVdc()+"/"+this.getReceiptVdc()+"-"+(this.isUseChequeBook()?"check":"no check")+"]";
	}

	void updateTo(Mode newMode) {
		this.name = newMode.name;
		this.expenseVDC = newMode.expenseVDC;
		this.receiptVDC = newMode.receiptVDC;
		this.useChequeBook = newMode.useChequeBook;
	}
}
