package net.yapbam.data;

import java.util.Arrays;

import net.yapbam.date.helpers.DateStepper;

import org.junit.Test;
import static org.junit.Assert.*;

/** Test of FilteredData.*/
public class FilteredDataTest {
	private static class TestData {
		Account[] accounts;
		Category[] categories;
		Mode[] modes;
		FilteredData fData;
		
		TestData() {
			accounts = new Account[]{new Account("Toto", 0.0), new Account("Titi", 0.0), new Account("Tutu", 0.0)};
			categories = new Category[]{new Category("cat0"), new Category("cat1"), new Category("cat2")};
			modes = new Mode[]{new Mode("mode0", DateStepper.IMMEDIATE, DateStepper.IMMEDIATE, false), new Mode("mode1", DateStepper.IMMEDIATE, DateStepper.IMMEDIATE, false)};
			// Build the global data
			GlobalData gData = new GlobalData();
			fData = new FilteredData(gData);
			for (Account account : accounts) {
				gData.add(account);
			}
			for (Category category : categories) {
				gData.add(category);
			}
			for (Mode mode : modes) {
				gData.getAccount(0).add(mode);
			}
		}
	}
	
	@Test
	public void testFilterUpdateWhenRemoving() {
		TestData data = new TestData();

		// Do the tests
		// Test mode removing
		data.fData.getFilter().setValidModes(Arrays.asList(new String[]{data.modes[0].getName()}));
		data.fData.getGlobalData().remove(data.fData.getGlobalData().getAccount(0), data.modes[0]);
		assertNull(data.fData.getFilter().getValidModes());
		// Test account removing
		data.fData.getFilter().setValidAccounts(Arrays.asList(new Account[]{data.accounts[0], data.accounts[1]}));
		data.fData.getGlobalData().remove(data.accounts[1]);
		assertNotNull(data.fData.getFilter().getValidAccounts());
		assertEquals(1,data.fData.getFilter().getValidAccounts().size());
		data.fData.getGlobalData().setName(data.accounts[0], "Toto_bis");
		data.fData.getGlobalData().remove(data.fData.getGlobalData().getAccount(0));
		assertNull(data.fData.getFilter().getValidAccounts());
		
		// Test category removing
		data.fData.getFilter().setValidCategories(Arrays.asList(new Category[]{data.categories[0], data.categories[1]}));
		data.fData.getGlobalData().remove(data.categories[1]);
		assertNotNull(data.fData.getFilter().getValidCategories());
		assertEquals(1,data.fData.getFilter().getValidCategories().size());
		data.fData.getGlobalData().setName(data.categories[0], "cat0_bis");
		data.fData.getGlobalData().remove(data.fData.getGlobalData().getCategory("cat0_bis"));
		assertNull(data.fData.getFilter().getValidCategories());
	}
	
	@Test
	public void testEmptyFilterUpdateWhenRemoving() {
		TestData data = new TestData();

		// Do the tests
		// Test mode removing
		data.fData.getGlobalData().remove(data.fData.getGlobalData().getAccount(0), data.modes[0]);
		assertNull(data.fData.getFilter().getValidModes());
		// Test account removing
		data.fData.getGlobalData().remove(data.accounts[1]);
		assertNull(data.fData.getFilter().getValidAccounts());
		
		// Test category removing
		data.fData.getGlobalData().remove(data.categories[1]);
		assertNull(data.fData.getFilter().getValidCategories());
	}
	
	@Test
	public void testNotConcernedFilterUpdateWhenRemoving() {
		TestData data = new TestData();

		// Do the tests
		// Test mode removing
		data.fData.getFilter().setValidAccounts(Arrays.asList(new Account[]{data.accounts[0]}));
		data.fData.getFilter().setValidModes(Arrays.asList(new String[]{data.modes[0].getName()}));
		data.fData.getFilter().setValidCategories(Arrays.asList(new Category[]{data.categories[0]}));
		
		data.fData.getGlobalData().remove(data.fData.getGlobalData().getAccount(0), data.modes[1]);
		assertNotNull(data.fData.getFilter().getValidModes());
		// Test account removing
		data.fData.getGlobalData().remove(data.accounts[1]);
		assertNotNull(data.fData.getFilter().getValidAccounts());
		
		// Test category removing
		data.fData.getGlobalData().remove(data.categories[1]);
		assertNotNull(data.fData.getFilter().getValidCategories());
	}
}
