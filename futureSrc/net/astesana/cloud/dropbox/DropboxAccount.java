package net.astesana.cloud.dropbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.dropbox.client2.session.AccessTokenPair;

import net.astesana.cloud.Account;

public class DropboxAccount extends Account<AccessTokenPair> {

	public DropboxAccount(File file) throws IOException {
		super(file);
	}
	
	public DropboxAccount(DropboxService service, String name, AccessTokenPair connectionData) throws IOException {
		super(service, name, connectionData);
	}

	@Override
	protected void serializeConnectionData(OutputStream stream) throws IOException {
		AccessTokenPair pair = getConnectionData();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(stream));
		out.write(pair.key);
		out.write(pair.secret);
	}

	@Override
	protected void deserializeConnectionData(InputStream stream) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		this.connectionData = new AccessTokenPair(in.readLine(), in.readLine());
	}
}
