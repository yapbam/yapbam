package net.astesana.cloud.dropbox.swing;

import java.io.IOException;

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
	protected Account<? extends Object> createNewAccount() {
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
}
