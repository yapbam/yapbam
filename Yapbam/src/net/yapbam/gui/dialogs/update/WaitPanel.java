package net.yapbam.gui.dialogs.update;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import net.yapbam.gui.LocalizationData;

import java.awt.Insets;

public class WaitPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel waiting = null;
	private JLabel icon = null;
	private JProgressBar progressBar = null;
	/**
	 * This is the default constructor
	 */
	public WaitPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.weighty = 1.0D;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.gridy = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getWaiting(), gridBagConstraints);
	}

	/**
	 * This method initializes waiting	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWaiting() {
		if (waiting == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.anchor = GridBagConstraints.NORTH;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 0;
			icon = new JLabel();
			icon.setIcon(UIManager.getIcon("OptionPane.informationIcon")); //$NON-NLS-1$
			icon.setText(LocalizationData.get("MainMenu.CheckUpdate.Dialog.message")); //$NON-NLS-1$
			waiting = new JPanel();
			waiting.setLayout(new GridBagLayout());
			waiting.add(icon, gridBagConstraints1);
			waiting.add(getProgressBar(), gridBagConstraints3);
		}
		return waiting;
	}

	/**
	 * This method initializes progressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
		}
		return progressBar;
	}

	public void setIndeterminate(boolean b) {
		this.progressBar.setIndeterminate(false);
	}
	
	public void setMaximum(int value) {
		this.progressBar.setMaximum(value);
	}

	public void setValue(int value) {
		this.progressBar.setValue(value);
	}

	public void setMessage(String message) {
		icon.setText(message);
	}
}
