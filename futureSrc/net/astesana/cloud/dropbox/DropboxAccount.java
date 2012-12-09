package net.astesana.cloud.dropbox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dropbox.client2.session.AccessTokenPair;

import net.astesana.cloud.Account;

public class DropboxAccount extends Account<AccessTokenPair> {

	public DropboxAccount(File file) throws IOException {
		super(file);
	}

	@Override
	protected void serializeConnectionData(OutputStream stream) throws IOException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deserializeConnectionData(InputStream stream) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
