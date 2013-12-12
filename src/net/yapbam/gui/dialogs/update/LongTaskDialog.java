package net.yapbam.gui.dialogs.update;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingWorker;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
/** An abstract progress dialog, that manages a long task.
 * <br>When the dialog is shown, its task is created, then executed in a SwingWorker thread.
 * <br>As the running computer may be faster that the developer thought, the long task is maybe a small and fast task !
 * So, instead of displaying immediately the dialog, we wait a little. If the long task completes during this time, the dialog is not displayed
 * (of course, the done method of the swingWorker is invoked).
 * <br>Once it is displayed, it remains visible for a minimum time (to prevent a flash effect if the search completes just after the pop up delay).
 */
public abstract class LongTaskDialog<T,V> extends AbstractDialog<T,V> {
//TODO Should be refactored to use A-JLib JobFrame
	private static final long DEFAULT_DELAY = 500;
	private static final int MINIMUM_TIME_VISIBLE = 1000;
	
	private long setVisibleTime;
	private long delay;
	private SwingWorker<?, ?> worker;
	private SwingWorker<Object, Void> showWorker;

	/** Constructor.
	 * @param owner The dialog owner
	 * @param title The dialog title
	 * @param data 
	 */
	public LongTaskDialog(Window owner, String title, T data) {
		super(owner, title, data);
		this.delay = DEFAULT_DELAY;
		getOkButton().setVisible(false);
		getCancelButton().setText(LocalizationData.get("GenericButton.cancel")); //$NON-NLS-1$
		getCancelButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				worker.cancel(true);
			}
		});
	}
	
	/** Sets the delay before the dialog is shown.
	 * <BR>This class allows you to specify a delay in opening the dialog.
	 * @param delay The delay in ms. Long.MAX_VALUE to prevent the dialog to be shown.
	 * The default value is 500 ms.
	 * <BR>Note that this method must be called before calling setVisible with a true argument.
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			// If the dialog is opened
			// Start the "check for update" thread.
			this.worker = getWorker();
			worker.execute();
			// Start a thread that will delay the dialog display 
			this.showWorker = new SwingWorker<Object, Void>() {
				@Override
				protected Object doInBackground() throws Exception {
					Thread.sleep(delay);
					return null;
				}

				@Override
				protected void done() {
					// This method is called when the showWorker end.
					// This means it is time to display the dialog.
					if (!worker.isDone()) {
						// If the task is not finished (please note we task the main task of the dialog, not the "display timer" task)
						// If main task not ended, show the dialog
						doShow();
					}
				}
			};
			this.showWorker.execute();
		} else {
			// If the dialog is closed
			if (this.showWorker!=null) {
				this.showWorker.cancel(true);
			}
			long delay = MINIMUM_TIME_VISIBLE-(System.currentTimeMillis()-this.setVisibleTime);
			try {
				if (delay>0) {
					// If the dialog is displayed for less than 500 ms, wait for the user to see what happens ;-)
					Thread.sleep(delay);
				}
			} catch (InterruptedException e) {
			}
			super.setVisible(visible);
		}
	}

	protected abstract SwingWorker<?, ?> getWorker();
	
	private void doShow() {
		this.setVisibleTime = System.currentTimeMillis(); // Remember when the dialog was displayed
		super.setVisible(true);
	}
}
