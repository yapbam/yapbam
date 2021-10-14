package net.yapbam.gui.dialogs.checkbook;

import java.awt.Window;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

public class CheckbookAlertsPreferencesDialog extends AbstractDialog<Void, Integer> {

	private static final long serialVersionUID = 1L;

	private CheckbookAlertsPreferencesPane pane;

	public CheckbookAlertsPreferencesDialog(Window owner) {
		super(owner, LocalizationData.get("checkbookAlertsPreferencesDialog.Title"), null);
	}

	public void setContent(Integer threshold) {
		this.pane.setContent(threshold);
	}

	@Override
	protected JPanel createCenterPane() {
		this.pane = new CheckbookAlertsPreferencesPane();
		this.pane.addPropertyChangeListener(CheckbookPane.INVALIDITY_CAUSE,
				new AutoUpdateOkButtonPropertyListener(this));
		return this.pane;
	}

	@Override
	protected Integer buildResult() {
		return this.pane.getAlertThreshold();
	}

	@Override
	protected String getOkDisabledCause() {
		return this.pane.getInvalidityCause();
	}

	public static Integer open(GlobalData data, Account account, Window owner) {
		CheckbookAlertsPreferencesDialog dialog = new CheckbookAlertsPreferencesDialog(owner);
		dialog.setContent(account.getCheckNumberAlertThreshold());
		dialog.setVisible(true);
		Integer threshold = dialog.getResult();
		if (threshold != null) {
			data.setCheckNumberAlertThreshold(account, threshold);
		}
		return threshold;
	}

}