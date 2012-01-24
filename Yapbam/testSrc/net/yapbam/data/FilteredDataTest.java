package net.yapbam.data;

import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

public class FilteredDataTest {
	@Test
	public void test() {
		Account[] accounts = new Account[]{new Account("Toto", 0.0), new Account("Titi", 0.0), new Account("Tutu", 0.0)};
		Category[] categories = new Category[]{new Category("cat0"), new Category("cat1"), new Category("cat2")};

		// Build the global data
		GlobalData gData = new GlobalData();
		FilteredData fData = new FilteredData(gData);
		for (Account account : accounts) {
			gData.add(account);
		}
		for (Category category : categories) {
			gData.add(category);
		}

		// Do the tests
		// Test account removing
		fData.getFilter().setValidAccounts(Arrays.asList(new Account[]{accounts[0], accounts[1]}));
		gData.remove(accounts[1]);
		assertNotNull(fData.getFilter().getValidAccounts());
		assertEquals(1,fData.getFilter().getValidAccounts().size());
		gData.setName(accounts[0], "Toto_bis");
		gData.remove(gData.getAccount(0));
		assertNull(fData.getFilter().getValidAccounts());
		
		// Test category removing
		fData.getFilter().setValidCategories(Arrays.asList(new Category[]{categories[0], categories[1]}));
		gData.remove(categories[1]);
		assertNotNull(fData.getFilter().getValidCategories());
		assertEquals(1,fData.getFilter().getValidCategories().size());
		gData.setName(categories[0], "cat0_bis");
		gData.remove(gData.getCategory("cat0_bis"));
		assertNull(fData.getFilter().getValidCategories());
	}
}
