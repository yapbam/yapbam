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
 * <br>Once it is displayed, it remains visible for a minimum time (to prevent a flash effect if the search completes just after the pop up delay).
 * @see #
 */
public abstract class LongTaskDialog<V> extends AbstractDialog<V> {
	private static final long DEFAULT_DELAY = 500;
	private static final int MINIMUM_TIME_VISIBLE = 1000;
	
	private long setVisibleTime;
	private long delay;
	private SwingWorker<?, ?> worker;

	/** Constructor.
	 * @param owner The dialog owner
	 * @param title The dialog title
	 * @param data 
	 */
	public LongTaskDialog(Window owner, String title, V data) {
		super(owner, title, data);
		this.delay = DEFAULT_DELAY;
		this.okButton.setVisible(false);
		this.cancelButton.setText(LocalizationData.get("GenericButton.cancel")); //$NON-NLS-1$
		this.cancelButton.addActionListener(new ActionListener() {
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
		if (visible) { // If the dialog is opened
			// Start the "check for update" thread.
			this.worker = getWorker();
			worker.execute();
			// Start a thread that will delay the dialog display 
			new SwingWorker<Object, Void>() {
				@Override
				protected Object doInBackground() throws Exception {
					//FIXME A big problem here : if you set the delay to MAX_LONG, the thread ... and the application never stops
					Thread.sleep(delay);
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

	@Override
	protected JPanel createCenterPane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		System.out.println ("dispose is called on "+this); //TODO
		worker.cancel(true);
		super.dispose();
	}
}
