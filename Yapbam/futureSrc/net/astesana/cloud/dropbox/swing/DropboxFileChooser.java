package net.astesana.cloud.dropbox.swing;

import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.dropbox.client2.session.AccessTokenPair;

import net.astesana.ajlib.swing.Utils;
import net.astesana.cloud.Account;
import net.astesana.cloud.dropbox.DropboxAccount;
import net.astesana.cloud.dropbox.DropboxService;
import net.astesana.cloud.swing.FileChooser;

@SuppressWarnings("serial")
public class DropboxFileChooser extends FileChooser {

	public DropboxFileChooser(DropboxService service) {
		super(service);
	}

	@Override
	protected Account createNewAccount() {
		ConnectionDialog connectionDialog = new ConnectionDialog(Utils.getOwnerWindow(this), ((DropboxService)getService()).getDropboxAPI());
		connectionDialog.setVisible(true);
		AccessTokenPair pair = connectionDialog.getResult();
		if (pair==null) return null;
		try {
			com.dropbox.client2.DropboxAPI.Account accountInfo = connectionDialog.getAccountInfo();
			return new DropboxAccount((DropboxService) getService(), Long.toString(accountInfo.uid), accountInfo.displayName, pair);
		} catch (IOException e) {
			//FIXME Alert the user something went wrong
			return null;
		}
	}
	
	protected String getRemoteConnectingWording() {
		return "Connecting to Dropbox...";
	}

	@Override
	public String getTooltip(boolean save) {
		return save?"Select this tab to save to your Dropbox account":"Select this tab to read data from your Dropbox account";
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(DropboxFileChooser.class.getResource("dropbox.png"));
	}
	
	@Override
	public String getName() {
		return "Dropbox";
	}
}
