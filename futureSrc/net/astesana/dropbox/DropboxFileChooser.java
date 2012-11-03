package net.astesana.dropbox;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
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
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Account;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.widget.TextWidget;
import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.astesana.common.dropbox.FileId;
import net.yapbam.gui.LocalizationData;
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
public abstract class DropboxFileChooser extends JPanel {
	public static final String SELECTED_FILEID_PROPERTY = "selectedIds"; //$NON-NLS-1$
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
	
	public DropboxFileChooser(FilesTableModel model) {
		this.filesModel = model;
		setLayout(new BorderLayout(0, 0));
		add(getNorthPanel(), BorderLayout.NORTH);
		add(getCenterPanel(), BorderLayout.CENTER);
	}
	
	/**
	 * Creates the panel.
	 */
	public DropboxFileChooser() {
		this(new FilesTableModel());
	}
	
	public void setCancelAction(Runnable action) {
		this.cancelAction = action;
	}
	
	public void setConfirmAction(Runnable action) {
		this.confirmAction = action;
	}
	
	public FileId showOpenDialog(Component parent, String title) {
		setDialogType(false);
		return showDialog(parent, title);
	}
	
	public FileId showSaveDialog(Component parent, String title) {
		setDialogType(true);
		return showDialog(parent, title);
	}
	
	public void setDialogType(boolean save) {
		this.getFilePanel().setVisible(save);
	}

	public FileId showDialog(Component parent, String title) {
		Window owner = Utils.getOwnerWindow(parent);
		final DropboxFileChooserDialog dialog = new DropboxFileChooserDialog(owner, title, this);
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


	public static class DropboxInfo {
		Account account;
		List<Entry> files;
		
		/**
		 * @return the account
		 */
		public Account getAccount() {
			return account;
		}
		/**
		 * @return the files
		 */
		public List<Entry> getFiles() {
			return files;
		}
	}
	
	protected abstract DropboxAPI<? extends WebAuthSession> getDropboxAPI();
	protected abstract void clearAccess();
	
	public boolean refresh() {
		final Window owner = Utils.getOwnerWindow(this);
		while (true) {
			try {
				//FIXME This could be a long task ... so, it should be wrapped into the background worker.
				if (isAccessGranted(getDropboxAPI())) break;
			} catch (DropboxException e) {
				if (e.getCause() instanceof UnknownHostException) {
					System.out.println ("IOException: "+(e.getCause() instanceof IOException));
					JOptionPane.showMessageDialog(owner, "Seems there's no Internet connection", LocalizationData.get("Generic.warning"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-2$
				} else {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			getDropboxAPI().getSession().unlink();
			ConnectionDialog connectionDialog = new ConnectionDialog(owner, getDropboxAPI().getSession());
			connectionDialog.setVisible(true);
			AccessTokenPair pair = connectionDialog.getResult();
			if (pair==null) {
				if (cancelAction!=null) cancelAction.run(); // Calls the cancel action if any is defined
				return false; // And exit
			}
		}
		Worker<DropboxInfo, Void> worker = new Worker<DropboxInfo, Void>() {
			@Override
			protected DropboxInfo doProcessing() throws Exception {
				setPhase(LocalizationData.get("dropbox.Chooser.connecting"), -1); //$NON-NLS-1$
				DropboxInfo info = new DropboxInfo();
				info.account = getDropboxAPI().accountInfo();
				info.files = getDropboxAPI().metadata("", 0, null, true, null).contents; //$NON-NLS-1$
				return info;
			}
		};
		WorkInProgressFrame frame = new WorkInProgressFrame(owner, LocalizationData.get("Generic.wait.title"), ModalityType.APPLICATION_MODAL, worker);
		frame.setSize(300, frame.getSize().height);
		frame.setVisible(true); //$NON-NLS-1$
		try {
			info = worker.get();
			setAccountName(info.account.displayName);
			Entry[] entries = info.files.toArray(new Entry[info.files.size()]);
			fillTable(entries);
			long percentUsed = 100*(info.account.quotaNormal+info.account.quotaShared) / info.account.quota; 
			getProgressBar().setValue((int)percentUsed);
			double remaining = info.account.quota-info.account.quotaNormal-info.account.quotaShared;
			String unit = LocalizationData.get("Generic.data.unit.bytes"); //$NON-NLS-1$
			if (remaining>1024) {
				unit = LocalizationData.get("Generic.data.unit.kBytes"); //$NON-NLS-1$
				remaining = remaining/1024;
				if (remaining>1024) {
					unit = LocalizationData.get("Generic.data.unit.MBytes"); //$NON-NLS-1$
					remaining = remaining/1024;
					if (remaining>1024) {
						unit = LocalizationData.get("Generic.data.unit.GBytes"); //$NON-NLS-1$
						remaining = remaining/1024;
					}
				}
			}
			getProgressBar().setString(MessageFormat.format(LocalizationData.get("dropbox.Chooser.freeSpace"), new DecimalFormat("0.0").format(remaining), unit));  //$NON-NLS-1$//$NON-NLS-2$
			getFileNameField().setEditable(true);
			return true;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof DropboxIOException) {
				JOptionPane.showMessageDialog(owner, LocalizationData.get("dropbox.Chooser.error.connectionFailed"), LocalizationData.get("Generic.warning"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			} else if (e.getCause() instanceof DropboxUnlinkedException) {
				System.err.println ("Not linked !!!"); //FIXME
				throw new RuntimeException(e);
			} else {
				throw new RuntimeException(e);
			}
		} catch (CancellationException e) {
			// The task was cancelled
		}
		return false;
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
			lblNewLabel = new JLabel(LocalizationData.get("dropbox.Chooser.selectedFile")); //$NON-NLS-1$
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
					firePropertyChange(SELECTED_FILEID_PROPERTY, evt.getOldValue(), evt.getNewValue());
				}
			});
		}
		return fileNameField;
	}
	private JLabel getLblAccount() {
		if (lblAccount == null) {
			lblAccount = new JLabel(MessageFormat.format(LocalizationData.get("dropbox.Chooser.account"), "")); //$NON-NLS-1$ //$NON-NLS-2$
			lblAccount.setPreferredSize(new Dimension(300, lblAccount.getPreferredSize().height));
		}
		return lblAccount;
	}
	private JButton getDisconnectButton() {
		if (disconnectButton == null) {
			disconnectButton = new JButton(LocalizationData.get("dropbox.Chooser.disconnect"), new ImageIcon(getClass().getResource("/net/astesana/dropbox/brokenLink.png")));  //$NON-NLS-1$//$NON-NLS-2$
			disconnectButton.setToolTipText(LocalizationData.get("dropbox.Chooser.disconnect.tooltip")); //$NON-NLS-1$
			disconnectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setAccountName("?"); //$NON-NLS-1$
					fillTable(new Entry[0]);
					getProgressBar().setValue(0);
					getProgressBar().setString(""); //$NON-NLS-1$
					getFileNameField().setEditable(false);
					clearAccess();
					refresh();
				}
			});
		}
		return disconnectButton;
	}
	private void setAccountName(String name) {
		getLblAccount().setText(MessageFormat.format(LocalizationData.get("dropbox.Chooser.account"), name)); //$NON-NLS-1$
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
			refreshButton = new JButton(LocalizationData.get("dropbox.Chooser.refresh"), new ImageIcon(getClass().getResource("/net/astesana/dropbox/synchronize.png")));  //$NON-NLS-1$//$NON-NLS-2$
			refreshButton.setToolTipText(LocalizationData.get("dropbox.Chooser.refresh.tooltip")); //$NON-NLS-1$
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
			progressBar.setString(""); //$NON-NLS-1$
		}
		return progressBar;
	}

	private void fillTable(Entry[] entries) {
		filesModel.clear();
		for (Entry entry : entries) {
			Entry filtered = filter(entry);
			if (filtered!=null) filesModel.add(entry);
		}
	}

	/** Filters an entry.
	 * <br>By default, this method returns the entry path.
	 * @param entry The entry available in the current Dropbox folder
	 * @return The entry that will be displayed in the files list, or null to ignore this entry
	 */
	protected Entry filter(Entry entry) {
		return entry;
	}

	/** This method is called after the user granted access to Dropbox.
	 * <br>By default, this method does nothing except returns true if the access was granted.
	 * <br>By overriding this method, you could store the access keys in a key store. Be sure to return
	 * true if the access was granted.
	 * <br>The access tokens could be retrieved by using getDropboxAPI().getSession().getAccessTokenPair()
	 * @param api The api we want to test 
	 * @return true if the access is granted.
	 * @throws DropboxException 
	 * @see #getDropboxAPI()
	 */
	protected boolean isAccessGranted(DropboxAPI<? extends Session> api) throws DropboxException {
		AccessTokenPair accessTokenPair = api.getSession().getAccessTokenPair();
		boolean result = accessTokenPair!=null && accessTokenPair.key!=null && accessTokenPair.secret!=null;
		if (result) {
			try {
				api.accountInfo();
			} catch (DropboxUnlinkedException e) {
				return false;
			}
		}
		return result;
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
	
	public void setSelectedURI(URI uri) {
		if (uri==null) {
			getFileNameField().setText("");
		} else {
			//FIXME Be aware that getInfo may not have been set before this method is called, so, the following lines, if not commented, result in an error 
//			FileId id = FileId.fromURI(uri);
//			if (!getInfo().getAccount().displayName.equals(id.getAccount())) throw new IllegalArgumentException("invalid account"); //$NON-NLS-1$
//			getFileNameField().setText(uri.getPath().substring(1));
		}
	}
	
	protected DropboxInfo getInfo() {
		return info;
	}
}
