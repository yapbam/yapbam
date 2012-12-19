package net.astesana.cloud.swing;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JList;
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
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.dialog.urichooser.FileChooserPanel;
import net.astesana.ajlib.swing.widget.TextWidget;
import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.astesana.cloud.Account;
import net.astesana.cloud.Entry;
import net.astesana.cloud.Service;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.JTableListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import net.astesana.ajlib.swing.widget.ComboBox;

@SuppressWarnings("serial")
public abstract class FileChooser extends JPanel {
	public static final String SELECTED_URI_PROPERTY = "selectedUri"; //$NON-NLS-1$
	private JPanel centerPanel;
	private JTable fileList;
	private JPanel filePanel;
	private JLabel lblNewLabel;
	private TextWidget fileNameField;
	
	private JLabel lblAccount;
	private JPanel northPanel;
	private JButton refreshButton;
	private JProgressBar progressBar;
	private FilesTableModel filesModel;
	private JScrollPane scrollPane;
	
	private Runnable cancelAction;
	private Runnable confirmAction;
	
	private JPanel panel;
	private ComboBox accountsCombo;
	private JButton btnNewAccount;
	private JButton deleteButton;
	private Service<? extends Account> service;
	
	public FileChooser(Service<? extends Account> service) {
		this.service = service;
		this.filesModel = new FilesTableModel();
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
	
	public Object showOpenDialog(Component parent, String title) {
		setDialogType(false);
		return showDialog(parent, title);
	}
	
	public Object showSaveDialog(Component parent, String title) {
		setDialogType(true);
		return showDialog(parent, title);
	}
	
	public void setDialogType(boolean save) {
		this.getFilePanel().setVisible(save);
	}

	public Object showDialog(Component parent, String title) {
		Window owner = Utils.getOwnerWindow(parent);
		final FileChooserDialog dialog = new FileChooserDialog(owner, title, this);
		final PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println ("Selected changed from "+evt.getOldValue()+" to "+evt.getNewValue()); //TODO
				dialog.updateOkButtonEnabled();
			}
		};
		dialog.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowOpened(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowOpened(WindowEvent e) {
				// FIXME Memory leak (listener is not removed from the panel listeners when the dialog closes).
				FileChooser.this.addPropertyChangeListener(SELECTED_URI_PROPERTY, listener);
				refresh();
				super.windowOpened(e);
			}

			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosed(WindowEvent e) {
				FileChooser.this.removePropertyChangeListener(SELECTED_URI_PROPERTY, listener);
				super.windowClosed(e);
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

	public void refresh() {
//		while (true) {
//			try {
//				//FIXME This could be a long task ... so, it should be wrapped into the background worker.
//				if (isAccessGranted(getDropboxAPI())) break;
//			} catch (DropboxException e) {
//				if (e.getCause() instanceof UnknownHostException) {
//					System.out.println ("IOException: "+(e.getCause() instanceof IOException));
//					JOptionPane.showMessageDialog(owner, "Seems there's no Internet connection", LocalizationData.get("Generic.warning"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-2$
//				} else {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return false;
//			}
//			getDropboxAPI().getSession().unlink();
//			ConnectionDialog connectionDialog = new ConnectionDialog(owner, getDropboxAPI().getSession());
//			connectionDialog.setVisible(true);
//			AccessTokenPair pair = connectionDialog.getResult();
//			if (pair==null) {
//				if (cancelAction!=null) cancelAction.run(); // Calls the cancel action if any is defined
//				return false; // And exit
//			}
//		}
		System.out.println ("refresh");
		
		Account account = (Account) getAccountsCombo().getSelectedItem();
		RemoteFileListWorker worker = new RemoteFileListWorker(account);
		worker.setPhase(getRemoteConnectingWording(), -1); //$NON-NLS-1$
		final Window owner = Utils.getOwnerWindow(this);
		WorkInProgressFrame frame = new WorkInProgressFrame(owner, LocalizationData.get("Generic.wait.title"), ModalityType.APPLICATION_MODAL, worker);
		frame.setSize(300, frame.getSize().height);
		Utils.centerWindow(frame, owner);
		frame.setVisible(true); //$NON-NLS-1$
		try {
			Collection<Entry> entries = worker.get();
			fillTable(entries);
			getFileNameField().setEditable(true);
			setQuota(account);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			//FIXME
//			if (e.getCause() instanceof DropboxIOException) {
//				JOptionPane.showMessageDialog(owner, LocalizationData.get("dropbox.Chooser.error.connectionFailed"), LocalizationData.get("Generic.warning"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
//			} else if (e.getCause() instanceof DropboxUnlinkedException) {
//				System.err.println ("Not linked !!!"); //FIXME
//				throw new RuntimeException(e);
//			} else {
				throw new RuntimeException(e);
//			}
		} catch (CancellationException e) {
			// The task was cancelled
			setQuota(null);
		}
	}

	private void setQuota(Account account) {
		if ((account!=null) && (account.getQuota()>0)) {
			long percentUsed = 100*(account.getUsed()) / account.getQuota(); 
			getProgressBar().setValue((int)percentUsed);
			double remaining = account.getQuota()-account.getUsed();
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
			getProgressBar().setVisible(true);
		} else {
			getProgressBar().setVisible(false);
		}
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
					int pos = fileNameField.getCaretPosition();
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
					firePropertyChange(SELECTED_URI_PROPERTY, evt.getOldValue(), getSelectedURI());
					pos = Math.min(pos, fileNameField.getText().length());
					fileNameField.setCaretPosition(pos);
				}
			});
		}
		return fileNameField;
	}
	private JLabel getLblAccount() {
		if (lblAccount == null) {
			lblAccount = new JLabel(MessageFormat.format(LocalizationData.get("dropbox.Chooser.account"), "")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return lblAccount;
	}
//	private void setAccountName(String name) {
//		getLblAccount().setText(MessageFormat.format(LocalizationData.get("dropbox.Chooser.account"), name)); //$NON-NLS-1$
//	}
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new JPanel();
			GridBagLayout gbl_northPanel = new GridBagLayout();
			northPanel.setLayout(gbl_northPanel);
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.weightx = 1.0;
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.insets = new Insets(0, 0, 0, 5);
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			northPanel.add(getPanel(), gbc_panel);
			GridBagConstraints gbc_refreshButton = new GridBagConstraints();
			gbc_refreshButton.gridheight = 0;
			gbc_refreshButton.gridx = 1;
			gbc_refreshButton.gridy = 0;
			northPanel.add(getRefreshButton(), gbc_refreshButton);
			GridBagConstraints gbc_progressBar = new GridBagConstraints();
			gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
			gbc_progressBar.insets = new Insets(5, 0, 5, 5);
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
			refreshButton.setEnabled(getAccountsCombo().getItemCount()!=0);
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
			progressBar.setVisible(false);
		}
		return progressBar;
	}

	private void fillTable(Collection<Entry> entries) {
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
//	protected boolean isAccessGranted(DropboxAPI<? extends Session> api) throws DropboxException {
//		AccessTokenPair accessTokenPair = api.getSession().getAccessTokenPair();
//		boolean result = accessTokenPair!=null && accessTokenPair.key!=null && accessTokenPair.secret!=null;
//		if (result) {
//			try {
//				api.accountInfo();
//			} catch (DropboxUnlinkedException e) {
//				return false;
//			}
//		}
//		return result;
//	}
	
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

	public URI getSelectedURI() {
		String name = getFileNameField().getText();
		Account account = (Account) getAccountsCombo().getSelectedItem();
		return ((account==null) || (name==null))?null:account.getURI(name);
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
	
//	protected DropboxInfo getInfo() {
//		return info;
//	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			GridBagLayout gbl_panel = new GridBagLayout();
			panel.setLayout(gbl_panel);
			GridBagConstraints gbc_lblAccount = new GridBagConstraints();
			gbc_lblAccount.fill = GridBagConstraints.BOTH;
			gbc_lblAccount.insets = new Insets(0, 0, 0, 5);
			gbc_lblAccount.anchor = GridBagConstraints.EAST;
			gbc_lblAccount.gridx = 0;
			gbc_lblAccount.gridy = 0;
			panel.add(getLblAccount(), gbc_lblAccount);
			GridBagConstraints gbc_accountsCombo = new GridBagConstraints();
			gbc_accountsCombo.weightx = 1.0;
			gbc_accountsCombo.fill = GridBagConstraints.BOTH;
			gbc_accountsCombo.gridx = 1;
			gbc_accountsCombo.gridy = 0;
			panel.add(getAccountsCombo(), gbc_accountsCombo);
			GridBagConstraints gbc_btnNewAccount = new GridBagConstraints();
			gbc_btnNewAccount.gridx = 2;
			gbc_btnNewAccount.gridy = 0;
			panel.add(getBtnNewAccount(), gbc_btnNewAccount);
			GridBagConstraints gbc_deleteButton = new GridBagConstraints();
			gbc_deleteButton.insets = new Insets(0, 0, 0, 5);
			gbc_deleteButton.gridx = 3;
			gbc_deleteButton.gridy = 0;
			panel.add(getDeleteButton(), gbc_deleteButton);
		}
		return panel;
	}
	private ComboBox getAccountsCombo() {
		if (accountsCombo == null) {
			accountsCombo = new ComboBox();
			accountsCombo.setRenderer(new BasicComboBoxRenderer(){
				@Override
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					if (value!=null) value = ((Account)value).getDisplayName();
					return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});
			Collection<? extends Account> accounts = getService().getAccounts();
			for (Account account : accounts) {
				accountsCombo.addItem(account);
			}
			accountsCombo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean oneIsSelected = getAccountsCombo().getSelectedIndex()>=0;
					getDeleteButton().setEnabled(oneIsSelected);
					getRefreshButton().setEnabled(oneIsSelected);
					refresh();
				}
			});
		}
		return accountsCombo;
	}
	private JButton getBtnNewAccount() {
		if (btnNewAccount == null) {
			btnNewAccount = new JButton(IconManager.NEW);
			int height = getAccountsCombo().getPreferredSize().height;
			btnNewAccount.setPreferredSize(new Dimension(height, height));
			btnNewAccount.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Account account = createNewAccount();
					if (account!=null) {
						for (int i = 0; i < getAccountsCombo().getItemCount(); i++) {
							if (((Account)getAccountsCombo().getItemAt(i)).getDisplayName().equals(account.getDisplayName())) {
								getAccountsCombo().setSelectedIndex(i);
								return;
							}
						}
						getAccountsCombo().addItem(account);
					}
				}
			});
		}
		return btnNewAccount;
	}
	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton(IconManager.DELETE);
			deleteButton.setEnabled(getAccountsCombo().getItemCount()!=0);
			deleteButton.setToolTipText("Deletes the current account");
			int height = getAccountsCombo().getPreferredSize().height;
			deleteButton.setPreferredSize(new Dimension(height, height));
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean confirm = JOptionPane.showOptionDialog(Utils.getOwnerWindow(deleteButton), "<html>Are you sure you want to delete this account ?<br><br>It will only delete the copy made on your computer, not the remote account.</html>", "Delete account",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{LocalizationData.get("GenericButton.ok"),LocalizationData.get("GenericButton.cancel")},1)==0;
					if (confirm) {
						Account account = (Account) getAccountsCombo().getSelectedItem();
						getAccountsCombo().removeItemAt(getAccountsCombo().getSelectedIndex());
						account.delete();
						System.out.println ("Delete account "+getAccountsCombo().getSelectedItem()); //TODO
					}
				}
			});
		}
		return deleteButton;
	}

	protected abstract Account createNewAccount();

	protected Service<? extends Account> getService() {
		return service;
	}

	protected String getRemoteConnectingWording() {
		return "Connecting to remote host ...";
	}
}
