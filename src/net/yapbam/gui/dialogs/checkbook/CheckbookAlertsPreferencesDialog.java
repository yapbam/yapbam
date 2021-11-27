package net.yapbam.gui.dialogs.checkbook;

import java.awt.Window;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

public class CheckbookAlertsPreferencesDialog extends AbstractDialog<Void, Integer> {

	private static final long serialVersionUID = 1L;

	private CheckbookAlertsPreferencesPane pane;

	public CheckbookAlertsPreferencesDialog(Window owner) {
		super(owner, LocalizationData.get("checkbookAlertsPreferencesDialog.Title"), null);
	}

	@Override
	protected JPanel createCenterPane() {
		this.pane = new CheckbookAlertsPreferencesPane();
		this.pane.addPropertyChangeListener(CheckbookAlertsPreferencesPane.INVALIDITY_CAUSE,
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

	public static Integer open(int current, Window owner) {
		CheckbookAlertsPreferencesDialog dialog = new CheckbookAlertsPreferencesDialog(owner);
		dialog.pane.setContent(current);
		dialog.setVisible(true);
		return dialog.getResult();
	}
}