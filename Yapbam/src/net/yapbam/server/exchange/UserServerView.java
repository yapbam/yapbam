package net.yapbam.server.exchange;

import java.io.IOException;
import java.io.Serializable;
import java.net.Proxy;

public class UserServerView extends AbstractServerView {
	private static final String POST_DATA_COMMAND = "POST_DATA";
	private static final String GET_DATA_COMMAND = "GET_DATA";

	public UserServerView(Proxy proxy, String eMail, String password) {
		super(proxy, eMail, password);
	}
	
	protected void codeToException(int errCode) {
		// TODO Auto-generated method stub
	}

	public void postData (String account, String data) throws IOException {
		toServer(POST_DATA_COMMAND, new Serializable[]{account, data});
	}
	
	public String getData(String account) throws IOException {
		return (String) toServer(GET_DATA_COMMAND, new Serializable[]{account});
	}
}
