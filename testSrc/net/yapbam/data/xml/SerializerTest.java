package net.yapbam.data.xml;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Currency;
import java.util.Locale;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;

import org.junit.Test;

public class SerializerTest {
	private static final double doubleAccuracy = Math.pow(10, -Currency.getInstance(Locale.getDefault()).getDefaultFractionDigits())/2;

	@Test
	public void test() throws Exception {
		GlobalData data = new GlobalData();
		Account account = new Account("toto", 50.24);
		data.add(account);
		account = new Account("titi", -10.0);
		data.add(account);
		data.setComment(account, "Un commentaire avec plusieurs ligne\nEt des caractères accentués.");
		
		testInstance(data);
		
		data.setPassword("this is the big password");
		testInstance(data);
	}

	private void testInstance(GlobalData data) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(); 
		Serializer serializer = new Serializer(data.getPassword(), os);
		serializer.serialize(data, null);
		serializer.closeDocument(data.getPassword());
		os.flush();
		
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		GlobalData other = new GlobalData();
		other = Serializer.read(data.getPassword(), is, null);
		
		assertEquals(data.getCategoriesNumber(), other.getCategoriesNumber());
		for (int i = 0; i < data.getCategoriesNumber(); i++) {
			Category category = data.getCategory(i);
			if (!category.equals(Category.UNDEFINED)) assertNotNull(other.getCategory(category.getName()));
		}
		
		assertEquals(other.getAccountsNumber(), data.getAccountsNumber());
		for (int i = 0; i < data.getAccountsNumber(); i++) {
			Account account = data.getAccount(i);
			Account oAccount = other.getAccount(account.getName());
			assertNotNull(oAccount);
			assertEquals(account.getInitialBalance(), oAccount.getInitialBalance(), doubleAccuracy);
			assertEquals(account.getComment(), oAccount.getComment());

			assertEquals(account.getModesNumber(), oAccount.getModesNumber());
			//TODO Test if modes are the same
			assertEquals(account.getCheckbooksNumber(), oAccount.getCheckbooksNumber());
			//TODO Test if checkbooks are the same
		}
		assertEquals(data.getPassword(), other.getPassword());
		
		assertEquals(data.getTransactionsNumber(), other.getTransactionsNumber());
		//TODO Test if transactions are the same
		assertEquals(data.getPeriodicalTransactionsNumber(), other.getPeriodicalTransactionsNumber());
		//TODO Test if periodical transactions are the same
	}

}
