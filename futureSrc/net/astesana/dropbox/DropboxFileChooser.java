package net.astesana.dropbox;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Account;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.widget.TextWidget;
import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.data.persistence.AbstractURIChooserPanel;
import net.yapbam.gui.util.JTableListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public abstract class DropboxFileChooser extends AbstractURIChooserPanel {
	public static final String SELECTED_FILE_PROPERTY = "selectedFile";
	
	private JPanel centerPanel;
	private JTable fileList;
	private JPanel filePanel;
	private JLabel lblNewLabel;
	private TextWidget fileNameField;
	
	private JLabel lblAccount;
	private JButton disconnectButton;
	private JPanel northPanel;
	private JButton refreshButton;
	private JProgressBar progressBar;
	private FilesTableModel filesModel;
	private JScrollPane scrollPane;
	
	private Runnable cancelAction;
	private Runnable confirmAction;
	
	private DropboxInfo info;
	
	/**
	 * Create the panel.
	 */
	public DropboxFileChooser() {
		setLayout(new BorderLayout(0, 0));
		add(getNorthPanel(), BorderLayout.NORTH);
		add(getCenterPanel(), BorderLayout.CENTER);
	}
	
	public void setCancelAction(Runnable action) {
		this.cancelAction = action;
	}
	
	public void setConfirmAction(Runnable action) {
		this.confirmAction = action;
	}
	
	public FileId showOpenDialog(Component parent) {
		return showDialog(parent, false);
	}
	
	public FileId showSaveDialog(Component parent) {
		return showDialog(parent, true);
	}
	
	public FileId showDialog(Component parent, boolean save) {
		this.getFilePanel().setVisible(save);
		Window owner = Utils.getOwnerWindow(parent);
		final DropboxFileChooserDialog dialog = new DropboxFileChooserDialog(owner, "DropboxChooser", this);
		dialog.addWindowListener(new WindowAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowOpened(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowOpened(WindowEvent e) {
				refresh();
			}
		});
		this.setCancelAction(new Runnable() {
			@Override
			public void run() {
				dialog.cancel();
			}
		});
		this.setConfirmAction(new Runnable() {
			@Override
			public void run() {
				dialog.confirm();
			}
		});
		dialog.setVisible(true);
		this.setCancelAction(null);
		this.setConfirmAction(null);
		return dialog.getResult();
	}


	private static class DropboxInfo {
		Account account;
		List<Entry> files;
	}
	
	protected abstract DropboxAPI<? extends WebAuthSession> getDropboxAPI();
	protected abstract void clearAccess();
	
	public void refresh() {
		Window owner = Utils.getOwnerWindow(this);
		AccessTokenPair pair = getDropboxAPI().getSession().getAccessTokenPair();
		while (!accessGranted(pair)) {
			ConnectionDialog connectionDialog = new ConnectionDialog(owner, "Be cool, connect to Dropbox", getDropboxAPI().getSession());
			connectionDialog.setVisible(true);
			pair = connectionDialog.getResult();
			if (pair==null && cancelAction!=null) {
				cancelAction.run(); // Calls the cancel action if any is defined
				return; // And exit
			}
		}
		new WorkInProgressFrame(owner, "Please wait", ModalityType.APPLICATION_MODAL, new Worker<DropboxInfo, Void>() {
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
					info = get();
					setAccountName(info.account.displayName);
					Entry[] entries = info.files.toArray(new Entry[info.files.size()]);
					fillTable(entries);
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
					getFileNameField().setEditable(true);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					throw new RuntimeException(e);
				} catch (CancellationException e) {
					// The task was cancelled
				}
				super.done();
			}
			
		}).setVisible(true);
	}
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout(0, 0));
			centerPanel.add(getScrollPane(), BorderLayout.CENTER);
			centerPanel.add(getFilePanel(), BorderLayout.SOUTH);
		}
		return centerPanel;
	}
	private JTable getFileList() {
		if (fileList == null) {
			filesModel = new FilesTableModel();
			fileList = new JTable(filesModel);
			fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			new JTableListener(getFileList(), null, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (confirmAction!=null) confirmAction.run();
				}
			});
			fileList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						if (getFileList().getSelectedRow()!=-1) {
							getFileNameField().setText((String) filesModel.getValueAt(getFileList().getSelectedRow(), 0));
						}
					}
				}
			});
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
			fileNameField.setEditable(false);
			fileNameField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {	
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					int index = -1;
					for (int rowIndex=0;rowIndex<filesModel.getRowCount();rowIndex++) {
						if (filesModel.getValueAt(rowIndex, 0).equals(evt.getNewValue())) {
							index = rowIndex;
							break;
						}
					}
					ListSelectionModel selectionModel = getFileList().getSelectionModel();
					if (index<0) {
						selectionModel.clearSelection();
					} else {
						selectionModel.setSelectionInterval(index, index);
					}
					firePropertyChange(SELECTED_FILE_PROPERTY, evt.getOldValue(), evt.getNewValue());
				}
			});
		}
		return fileNameField;
	}
	private JLabel getLblAccount() {
		if (lblAccount == null) {
			lblAccount = new JLabel("Account :");
			lblAccount.setPreferredSize(new Dimension(300, lblAccount.getPreferredSize().height));
		}
		return lblAccount;
	}
	private JButton getDisconnectButton() {
		if (disconnectButton == null) {
			disconnectButton = new JButton("Disconnect", new ImageIcon(getClass().getResource("/net/astesana/dropbox/brokenLink.png")));
			disconnectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setAccountName("?");
					fillTable(new Entry[0]);
					getProgressBar().setValue(0);
					getProgressBar().setString("");
					getFileNameField().setEditable(false);
					clearAccess();
					refresh();
				}
			});
		}
		return disconnectButton;
	}
	private void setAccountName(String name) {
		getLblAccount().setText(MessageFormat.format("Account: {0}", name));
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

	private void fillTable(Entry[] entries) {
		filesModel.clear();
		for (Entry entry : entries) {
			String name = filter(entry);
			if (name!=null) filesModel.add(entry);
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
	 * <br>By default, this method does nothing except returns true if the access token pair is not null and token are not null.
	 * <br>By overriding this method, you could store the access keys in a key store. Be sure to return
	 * true if the access was granted.
	 * <br>The session could be retrieve by using getDropboxAPI().getSession().
	 * @param accessTokenPair The access token pair or null 
	 * @return true if the access is granted.
	 * @see #getDropboxAPI()
	 */
	protected boolean accessGranted(AccessTokenPair accessTokenPair) {
		return accessTokenPair!=null && accessTokenPair.key!=null && accessTokenPair.secret!=null;
	}
	
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getFileList());
			// Do not diplay column names
			getFileList().setTableHeader(null);
			scrollPane.setColumnHeaderView(null);
		}
		return scrollPane;
	}

	public FileId getSelectedFile() {
		String name = getFileNameField().getText();
		return name.length()==0?null:new FileId(getDropboxAPI().getSession().getAccessTokenPair(), info.account.displayName, name);
	}
}
