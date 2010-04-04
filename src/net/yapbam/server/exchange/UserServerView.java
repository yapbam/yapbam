package net.yapbam.server.exchange;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.Proxy;

public class UserServerView extends AbstractServerView {
	private static final String POST_DATA_COMMAND = "POST_DATA";
	private static final String GET_DATA_COMMAND = "GET_DATA";

	private static final int UNEXPEXTED_ERROR = -1;
	private static final int UNKNOWN_COMMAND = 1;
	
	private static final int UNKNOWN_USER = 3;
	private static final int INVALID_PASSWORD = 4;
	private static final int UNKNOWN_ACCOUNT = 6;
	private static final int ACCOUNT_EXPIRED = 7;	
	
	public UserServerView(Proxy proxy, String eMail, String password) {
		super(proxy, eMail, password);
	}
	
	protected void codeToException(int errCode) {
		if ((errCode==UNEXPEXTED_ERROR) || (errCode==UNKNOWN_COMMAND)) {
			throw new RuntimeException("server error "+errCode);
		} else if (errCode==UNKNOWN_USER) {
			throw new UnknownUserException();
		} else if (errCode==INVALID_PASSWORD) {
			throw new InvalidPasswordException();
		} else if (errCode==UNKNOWN_ACCOUNT) {
			throw new UnknownAccountException();
		} else if (errCode==ACCOUNT_EXPIRED) {
			throw new ExpiredAccountException();
		}
	}

	public void postData (String account, String data) throws IOException, UnknownUserException, InvalidPasswordException, UnknownAccountException, ExpiredAccountException {
		toServer(POST_DATA_COMMAND, new Serializable[]{account, data});
	}
	
	public String getData(String account) throws IOException, UnknownUserException, InvalidPasswordException, UnknownAccountException, ExpiredAccountException {
		return (String) toServer(GET_DATA_COMMAND, new Serializable[]{account});
	}
	
	public static void main (String[] args) {
		try {
			StringBuffer buf = new StringBuffer();
			BufferedReader in = new BufferedReader(new FileReader("C:/Users/Jean-Marc/Documents/Comptes.xml")); //TODO Euh ... what about encoding ?
			try {
				for (String line=in.readLine(); line!=null; line=in.readLine()) {
					buf.append(line);
				}
				UserServerView server = new UserServerView(Proxy.NO_PROXY, "jeanmarc@astesana.net", "gti9220");
				server.postData("main", buf.toString());
			} finally {
				in.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
