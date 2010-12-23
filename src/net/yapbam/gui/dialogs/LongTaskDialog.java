package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
/** An abstract progress dialog, that manages a long task.
 * <br>When the dialog is shown, its task is created, then executed in a SwingWorker thread.
 * <br>As the running computer may be faster that the developer thought, the long task is maybe a small and fast task !
 * So, instead of displaying immediately the dialog, we wait a little. If the long task completes during this time, the dialog is not displayed
 * (of course, the done method of the swingWorker is invoked).
 * <br>Once it is displayed, it remains visible for a minimum time (to prevent a flash effect if the search completes just after the pop up delay.
 */
public abstract class LongTaskDialog extends AbstractDialog {
	private static final int DELAY = 500;
	private static final int MINIMUM_TIME_VISIBLE = 1000;
	
	private long setVisibleTime;
	private SwingWorker<?, ?> worker;

	/** Constructor.
	 * @param owner The dialog owner
	 * @param title The dialog title
	 * @param data 
	 */
	public LongTaskDialog(Window owner, String title, Object data) {
		super(owner, title, data);
		this.okButton.setVisible(false);
		this.cancelButton.setText(LocalizationData.get("GenericButton.cancel")); //$NON-NLS-1$
		this.cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				worker.cancel(true);
			}
		});
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		return new CheckUpdatePanel();
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) { // If the dialog is opened
			// Start the "check for update" thread.
			this.worker = getWorker();
			worker.execute();
			// Start a thread that will delay the dialog display 
			new SwingWorker<Object, Void>() {
				@Override
				protected Object doInBackground() throws Exception {
					Thread.sleep(DELAY);
					return null;
				}

				@Override
				protected void done() {
					super.done();
					if (!worker.isDone()) {
						doShow();
					}
				}
			}.execute();
		} else { // If the dialog is closed
			long delay = MINIMUM_TIME_VISIBLE-(System.currentTimeMillis()-this.setVisibleTime);
			try {
				if (delay>0) { // If the dialog is display for less than 500 ms, wait for the user to see what happens ;-)
					Thread.sleep(delay);
				}
			} catch (InterruptedException e) {
			}
			super.setVisible(visible);
		}
	}

	protected abstract SwingWorker<?, ?> getWorker();
	
	private void doShow() {
		LongTaskDialog.this.setVisibleTime = System.currentTimeMillis(); // Remember when the dialog was displayed
		super.setVisible(true);
	}
}
