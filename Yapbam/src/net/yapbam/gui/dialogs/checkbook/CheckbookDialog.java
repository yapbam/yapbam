package net.yapbam.gui.dialogs.checkbook;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import net.yapbam.data.Account;
import net.yapbam.data.Checkbook;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;

public class CheckbookDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	
	private CheckbookPane pane;
		
	public CheckbookDialog(Window owner) {
		super(owner, LocalizationData.get("checkbookDialog.title.new"), null); //$NON-NLS-1$
	}
	
	protected JPanel createCenterPane(Object data) {
		this.pane = new CheckbookPane();
		this.pane.addPropertyChangeListener(CheckbookPane.INVALIDITY_CAUSE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
        return this.pane;
    }
	
	public Checkbook getCheckbook() {
		return (Checkbook) getResult();
	}

	@Override
	protected Object buildResult() {
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
		Checkbook newMode = dialog.getCheckbook();
		if (newMode!=null) {
//TODO			data.add(account, newMode);
		}
		return newMode;
	}

	@Override
	protected String getOkDisabledCause() {
		return this.pane.getInvalidityCause();
	}

	class Listener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			updateOkButtonEnabled();
		}
	}
/*
	public void setContent(Checkbook book) {
		setTitle(LocalizationData.get("checkbookDialog.title.edit")); //$NON-NLS-1$
		this.pane.setContent (book);
	}*/
}