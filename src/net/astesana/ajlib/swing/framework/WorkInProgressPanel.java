package net.astesana.ajlib.swing.framework;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import java.awt.GridBagLayout;
import javax.swing.JProgressBar;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@SuppressWarnings("serial")
public class WorkInProgressPanel extends JPanel {
	private SwingWorker<?, ?> worker;
	private JLabel message;
	private JProgressBar progressBar;
	private JButton btnCancel;

	/**
	 * Create the panel.
	 */
	public WorkInProgressPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		message = new JLabel(" "); //$NON-NLS-1$
		GridBagConstraints gbc_message = new GridBagConstraints();
		gbc_message.insets = new Insets(0, 0, 5, 0);
		gbc_message.anchor = GridBagConstraints.WEST;
		gbc_message.gridwidth = 0;
		gbc_message.gridx = 0;
		gbc_message.gridy = 0;
		add(message, gbc_message);
		
		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.weightx = 1.0;
		gbc_progressBar.insets = new Insets(0, 0, 5, 0);
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 0;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 1;
		add(progressBar, gbc_progressBar);
		
		btnCancel = new JButton(Application.getString("GenericButton.cancel")); //$NON-NLS-1$
		btnCancel.setEnabled(false);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (worker!=null) {
					worker.cancel(false);
				}
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.gridx = 0;
		gbc_btnCancel.gridy = 2;
		add(btnCancel, gbc_btnCancel);
	}

	public SwingWorker<?, ?> getWorker() {
		return worker;
	}

	public void setSwingWorker(SwingWorker<?, ?> worker) {
		this.worker = worker;
		this.worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("state")) { //$NON-NLS-1$
					btnCancel.setEnabled(evt.getNewValue().equals(SwingWorker.StateValue.STARTED));
				}
			}
		});
	}

	public void setMessage(String message) {
		this.message.setText(message);
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}
}
