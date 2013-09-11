package net.yapbam.gui.dialogs.checkbook;

import java.awt.Window;

import javax.swing.*;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.data.Account;
import net.yapbam.data.Checkbook;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

public class CheckbookDialog extends AbstractDialog<Void, Checkbook> {
	private static final long serialVersionUID = 1L;
	
	private CheckbookPane pane;
		
	public CheckbookDialog(Window owner) {
		super(owner, LocalizationData.get("checkbookDialog.title.new"), null); //$NON-NLS-1$
	}
	
	@Override
	protected JPanel createCenterPane() {
		this.pane = new CheckbookPane();
		this.pane.addPropertyChangeListener(CheckbookPane.INVALIDITY_CAUSE, new AutoUpdateOkButtonPropertyListener(this));
		return this.pane;
	}

	@Override
	protected Checkbook buildResult() {
		return this.pane.getCheckbook();
	}
	
	/** Opens the dialog, and add the newly created checkbook to the data
	 * @param data The account where to add the new checkbook
	 * @param owner The frame upon which the dialog will be displayed
	 * @return The checkbook which was added, null if the operation was canceled
	 */
	public static Checkbook open(GlobalData data, Account account, Window owner) {
		CheckbookDialog dialog = new CheckbookDialog(owner);
		dialog.setVisible(true);
		Checkbook book = dialog.getResult();
		if (book!=null) {
			data.add(account, book);
		}
		return book;
	}

	@Override
	protected String getOkDisabledCause() {
		return this.pane.getInvalidityCause();
	}

	public void setContent(Checkbook book) {
		setTitle(LocalizationData.get("checkbookDialog.title.edit")); //$NON-NLS-1$
		this.pane.setContent (book);
	}
}