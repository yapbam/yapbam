package net.yapbam.dropbox;

import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.ProgressListener;

import java.io.IOException;
import java.io.InputStream;

public class CommandLineAuth {
	public static void main(String[] args) throws DropboxException, IOException {
		AccessTokenPair accessToken;
		if (args.length == 3) {
			accessToken = null;
		} else if (args.length == 5) {
			accessToken = new AccessTokenPair(args[3], args[4]);
		} else {
			System.err.println("Usage: COMMAND appKey appSecret [ access-token access-token-secret ]");
			System.exit(1);
			return;
		}
		
		// ----------------------------------------------------
		// Do web-based OAuth
		AppKeyPair appPair = new AppKeyPair(args[1], args[2]);
		WebAuthSession was = new WebAuthSession(appPair, AccessType.APP_FOLDER);
		if (accessToken == null) {
			WebAuthSession.WebAuthInfo info = was.getAuthInfo();
			System.out.println("1. Go to: " + info.url);
			System.out.println("2. Allow access to this app.");
			System.out.println("3. Press ENTER.");
			while (System.in.read() != '\n') {
			}

			String userId = was.retrieveWebAccessToken(info.requestTokenPair);
			System.out.println("User ID: " + userId);
			System.out.println("Access Key: " + was.getAccessTokenPair().key);
			System.out.println("Access Secret " + was.getAccessTokenPair().secret);
		} else {
			was.setAccessTokenPair(accessToken);
		}

		DropboxAPI<WebAuthSession> api = new DropboxAPI<WebAuthSession>(was);

		DropboxAPI.Account account = api.accountInfo();
		System.out.println("You're connected as " + account.displayName);

//		String content = "This is a simple java to Dropbox test at " + new GregorianCalendar().toString();
//		write(api, new ByteArrayInputStream(content.getBytes()), content.length());
	}

	private static void write(DropboxAPI<WebAuthSession> api, InputStream stream, int length) {
		try {
			Entry newEntry = api.putFile("/testing.txt", stream, length, null, new ProgressListener() {
				@Override
				public void onProgress(long bytes, long total) {
					System.out.println("uploading: " + bytes + "/" + total);
				}
			});
			System.out.println("The uploaded file's name is: " + newEntry.fileName());
			System.out.println("The uploaded file's rev is: " + newEntry.rev);
		} catch (DropboxUnlinkedException e) {
			// User has unlinked, ask them to link again here.
			System.err.println("User has unlinked.");
		} catch (DropboxException e) {
			System.err.println("Something went wrong while uploading.");
		} finally {
		}
	}
}