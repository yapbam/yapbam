package net.yapbam.server.exchange;

import java.io.IOException;
import java.io.Serializable;
import java.net.Proxy;

public class AdminServerView extends UserServerView {
	private static final String CREATE_USER_COMMAND = "CREATE_USER"; //$NON-NLS-1$
	private static final String CREATE_ACCOUNT_COMMAND = "CREATE_ACCOUNT"; //$NON-NLS-1$
	
	private static final int INVALID_ADMIN_ACCOUNT = 8;
	private static final int USER_EXISTS = 2;
	private static final int ACCOUNT_EXISTS = 5;

	public AdminServerView(Proxy proxy, String eMail, String password) {
		super(proxy, eMail, password);
	}

	@Override
	protected void codeToException(int errCode) {
		if (errCode==INVALID_ADMIN_ACCOUNT) {
			throw new InvalidAdminAccountException();
		} else if (errCode==USER_EXISTS) {
			throw new UserExistsException();
		} else if (errCode==ACCOUNT_EXISTS) {
			throw new AccountExistsException();
		} else {
			super.codeToException(errCode);
		}
	}
	
	public void createUser(String userEMail, String userPassword, boolean admin) throws IOException, InvalidAdminAccountException, UserExistsException {
		toServer(CREATE_USER_COMMAND, new Serializable[]{userEMail, userPassword, Boolean.valueOf(admin)});
	}
	
	public void createAccount(String userEMail, String account) throws IOException, InvalidAdminAccountException, AccountExistsException {
		toServer(CREATE_ACCOUNT_COMMAND, new Serializable[]{userEMail, account});
	}
}
