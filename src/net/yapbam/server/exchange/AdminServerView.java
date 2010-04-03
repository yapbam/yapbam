package net.yapbam.server.exchange;

import java.io.IOException;
import java.io.Serializable;
import java.net.Proxy;

public class AdminServerView extends UserServerView {
	private static final String CREATE_USER_COMMAND = "CREATE_USER";
	private static final String CREATE_ACCOUNT_COMMAND = "CREATE_ACCOUNT";
	
	public AdminServerView(Proxy proxy, String eMail, String password) {
		super(proxy, eMail, password);
	}

	public void createUser(String userEMail, String userPassword, boolean admin) throws IOException {
		toServer(CREATE_USER_COMMAND, new Serializable[]{userEMail, userPassword, Boolean.valueOf(admin)});
	}
	
	public void createAccount(String userEMail, String account) throws IOException {
		toServer(CREATE_ACCOUNT_COMMAND, new Serializable[]{userEMail, account});
	}
	
	public static void main (String[] args) {
		try {
			AdminServerView server = new AdminServerView(Proxy.NO_PROXY, "jeanmarc@astesana.net", "gti9220");
			server.createUser("jeanmarc@astesana.net", "gti9220", true);
			server.createUser("laurence.bot@numericable.fr", "bot", false);
			String account = "main";
			server.createAccount("jeanmarc@astesana.net", account);
			server.postData(account, "sqfhjhlzuehsjkljqsmkll !!!");
			System.out.println (server.getData(account));

			server = new AdminServerView(Proxy.NO_PROXY, "laurence.bot@numericable.fr", "bot");
			server.createUser("toto@astesana.net", "xxxx", true);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
