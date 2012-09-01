package net.astesana.dropbox;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JList;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Account;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.widget.TextWidget;
import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;

import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public abstract class DropboxFileChooser extends JPanel {
	private JPanel centerPanel;
	private JList fileList;
	private JPanel filePanel;
	private JLabel lblNewLabel;
	private TextWidget fileNameField;
	
	private JLabel lblAccount;
	private JButton disconnectButton;
	private JPanel northPanel;
	private JButton refreshButton;
	private JProgressBar progressBar;
	private DefaultListModel filesModel;

	/**
	 * Create the panel.
	 */
	public DropboxFileChooser() {
		setLayout(new BorderLayout(0, 0));
		add(getNorthPanel(), BorderLayout.NORTH);
		add(getCenterPanel(), BorderLayout.CENTER);
	}
	
//	public void connect() {
//		if (getDropboxAPI().getSession().getAccessTokenPair()==null) {
//			final ConnectionPanel connectionPanel = new ConnectionPanel(getDropboxAPI().getSession());
//			connectionPanel.addPropertyChangeListener(ConnectionPanel.STATE_PROPERTY, new PropertyChangeListener() {
//				@Override
//				public void propertyChange(PropertyChangeEvent evt) {
//					ConnectionPanel.State state = (State) evt.getNewValue();
//					if (state.equals(ConnectionPanel.State.FAILED)) {
//						JOptionPane.showMessageDialog(DropboxFileChooser.this, "There was something wrong", "Error", JOptionPane.ERROR_MESSAGE);
//					} else if (state.equals(ConnectionPanel.State.GRANTED)) {
//						accessGranted();
//						getNorthPanel().setVisible(true);
//						remove(connectionPanel);
//						add(getCenterPanel(), BorderLayout.CENTER);
//						refresh();
//					}
//				}
//			});
//			getNorthPanel().setVisible(false);
//			remove(getCenterPanel());
//			add(connectionPanel, BorderLayout.CENTER);
//		} else {
//			refresh();
//		}
//	}

	public void showOpenDialog(Component parent) {
		Window owner = Utils.getOwnerWindow(parent);
		if (getDropboxAPI().getSession().getAccessTokenPair()==null) {
			ConnectionDialog connectionDialog = new ConnectionDialog(owner, "Be cool, connect to Dropbox", getDropboxAPI().getSession());
			connectionDialog.setVisible(true);
			if (connectionDialog.getResult()==null) return;
			accessGranted();
System.out.println ("Cool, the access was granted !!!"); //TODO
		}
		DropboxFileChooserDialog dialog = new DropboxFileChooserDialog(owner, "DropboxChooser", this);
		dialog.setVisible(true);
	}
	
	private static class DropboxInfo {
		Account account;
		List<Entry> files;
	}
	
	protected abstract DropboxAPI<? extends WebAuthSession> getDropboxAPI();
	protected abstract void clearAccess();
	
	private void refresh() {
		add(northPanel, BorderLayout.NORTH);
		new WorkInProgressFrame(Utils.getOwnerWindow(this), "Please wait", ModalityType.APPLICATION_MODAL, new Worker<DropboxInfo, Void>() {
			@Override
			protected DropboxInfo doInBackground() throws Exception {
				setPhase("Connecting to Dropbox", -1);
				DropboxInfo info = new DropboxInfo();
				info.account = getDropboxAPI().accountInfo();
				info.files = getDropboxAPI().metadata("", 0, null, true, null).contents;
				return info;
			}

			/* (non-Javadoc)
			 * @see javax.swing.SwingWorker#done()
			 */
			@Override
			protected void done() {
				try {
					DropboxInfo info = get();
					getLblAccount().setText(MessageFormat.format("Account: {0}", info.account.displayName));
					Entry[] entries = info.files.toArray(new Entry[info.files.size()]);
					fillList(entries);
					long percentUsed = 100*(info.account.quotaNormal+info.account.quotaShared) / info.account.quota; 
					getProgressBar().setValue((int)percentUsed);
					double remaining = info.account.quota-info.account.quotaNormal-info.account.quotaShared;
					String unit = "bytes";
					if (remaining>1024) {
						unit = "kB";
						remaining = remaining/1024;
						if (remaining>1024) {
							unit = "MB";
							remaining = remaining/1024;
							if (remaining>1024) {
								unit = "GB";
								remaining = remaining/1024;
							}
						}
					}
					getProgressBar().setString(MessageFormat.format("{1}{0} free", unit, new DecimalFormat("0.0").format(remaining)));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CancellationException e) {
					// The task was cancelled, do nothing
				}
				super.done();
			}
			
		}).setVisible(true);
	}
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			GridBagLayout gbl_centerPanel = new GridBagLayout();
			centerPanel.setLayout(gbl_centerPanel);
			GridBagConstraints gbc_fileList = new GridBagConstraints();
			gbc_fileList.weightx = 1.0;
			gbc_fileList.fill = GridBagConstraints.BOTH;
			gbc_fileList.gridwidth = 0;
			gbc_fileList.weighty = 1.0;
			gbc_fileList.insets = new Insets(5, 5, 5, 5);
			gbc_fileList.gridx = 0;
			gbc_fileList.gridy = 0;
			centerPanel.add(getFileList(), gbc_fileList);
			GridBagConstraints gbc_filePanel = new GridBagConstraints();
			gbc_filePanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_filePanel.gridwidth = 0;
			gbc_filePanel.insets = new Insets(0, 5, 5, 5);
			gbc_filePanel.gridx = 0;
			gbc_filePanel.gridy = 1;
			centerPanel.add(getFilePanel(), gbc_filePanel);
		}
		return centerPanel;
	}
	private JList getFileList() {
		if (fileList == null) {
			filesModel = new DefaultListModel();
			fileList = new JList(filesModel);
			fileList.setBorder(new LineBorder(new Color(0, 0, 0)));
		}
		return fileList;
	}
	private JPanel getFilePanel() {
		if (filePanel == null) {
			filePanel = new JPanel();
			filePanel.setLayout(new BorderLayout(0, 0));
			filePanel.add(getLblNewLabel(), BorderLayout.WEST);
			filePanel.add(getFileNameField(), BorderLayout.CENTER);
		}
		return filePanel;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("File: ");
		}
		return lblNewLabel;
	}
	private TextWidget getFileNameField() {
		if (fileNameField == null) {
			fileNameField = new TextWidget();
		}
		return fileNameField;
	}
	private JLabel getLblAccount() {
		if (lblAccount == null) {
			lblAccount = new JLabel("Account :");
		}
		return lblAccount;
	}
	private JButton getDisconnectButton() {
		if (disconnectButton == null) {
			disconnectButton = new JButton("Disconnect", new ImageIcon(getClass().getResource("/net/astesana/dropbox/brokenLink.png")));
			disconnectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					clearAccess();
//					connect();
				}
			});
		}
		return disconnectButton;
	}
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new JPanel();
			GridBagLayout gbl_northPanel = new GridBagLayout();
			northPanel.setLayout(gbl_northPanel);
			GridBagConstraints gbc_lblAccount = new GridBagConstraints();
			gbc_lblAccount.weightx = 1.0;
			gbc_lblAccount.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblAccount.anchor = GridBagConstraints.WEST;
			gbc_lblAccount.insets = new Insets(0, 5, 5, 5);
			gbc_lblAccount.gridx = 0;
			gbc_lblAccount.gridy = 0;
			northPanel.add(getLblAccount(), gbc_lblAccount);
			GridBagConstraints gbc_refreshButton = new GridBagConstraints();
			gbc_refreshButton.gridheight = 0;
			gbc_refreshButton.insets = new Insets(0, 0, 0, 5);
			gbc_refreshButton.gridx = 1;
			gbc_refreshButton.gridy = 0;
			northPanel.add(getRefreshButton(), gbc_refreshButton);
			GridBagConstraints gbc_disconnectButton = new GridBagConstraints();
			gbc_disconnectButton.gridheight = 0;
			gbc_disconnectButton.gridx = 2;
			gbc_disconnectButton.gridy = 0;
			northPanel.add(getDisconnectButton(), gbc_disconnectButton);
			GridBagConstraints gbc_progressBar = new GridBagConstraints();
			gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
			gbc_progressBar.insets = new Insets(0, 5, 0, 5);
			gbc_progressBar.gridx = 0;
			gbc_progressBar.gridy = 1;
			northPanel.add(getProgressBar(), gbc_progressBar);
		}
		return northPanel;
	}
	private JButton getRefreshButton() {
		if (refreshButton == null) {
			refreshButton = new JButton("Refresh", new ImageIcon(getClass().getResource("/net/astesana/dropbox/synchronize.png")));
			refreshButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refresh();
				}
			});
		}
		return refreshButton;
	}
	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
			progressBar.setStringPainted(true);
			progressBar.setString("");
		}
		return progressBar;
	}

	private void fillList(Entry[] entries) {
		filesModel.clear();
		for (Entry entry : entries) {
			String name = filter(entry);
			if (name!=null) filesModel.addElement(name);
		}
	}

	/** Filters an entry.
	 * <br>By default, this method returns the entry path.
	 * @param entry The entry available in the current Dropbox folder
	 * @return a string that will be displayed in the files list, or null to ignore this entry
	 */
	protected String filter(Entry entry) {
		String fileName = entry.fileName();
		if (fileName.endsWith(".zip")) {
			return fileName.substring(0, fileName.length()-".zip".length());
		} else {
			return null;
		}
	}

	/** This method is called after the user granted access to Dropbox.
	 * <br>By default, this method does nothing, but this is the place where you would store the access key in a key store.
	 * <br>The session could be retrieve by using getDropboxAPI().getSession().
	 * @see #getDropboxAPI()
	 */
	protected void accessGranted() {
	}
}
