package net.astesana.cloud.dropbox.swing;

import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.dropbox.client2.session.AccessTokenPair;

import net.astesana.ajlib.swing.Utils;
import net.astesana.cloud.Account;
import net.astesana.cloud.dropbox.DropboxService;
import net.astesana.cloud.swing.URIChooser;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class DropboxURIChooser extends URIChooser {

	public DropboxURIChooser(DropboxService service) {
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
			return new Account(getService(), Long.toString(accountInfo.uid), accountInfo.displayName, pair, accountInfo.quota, accountInfo.quotaNormal+accountInfo.quotaShared);
		} catch (IOException e) {
			//FIXME Alert the user something went wrong
			return null;
		}
	}
	
	protected String getRemoteConnectingWording() {
		return "Connecting to Dropbox...";
	}

	@Override
	public String getTooltip(boolean save) { //FIXME
		return save?LocalizationData.get("dropbox.Chooser.save.tabTooltip"):LocalizationData.get("dropbox.Chooser.read.tabTooltip"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(DropboxURIChooser.class.getResource("dropbox.png"));
	}
	
	@Override
	public String getName() {
		return "Dropbox";
	}
}
