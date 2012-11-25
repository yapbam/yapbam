package net.yapbam.gui.dialogs.periodicaltransaction;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.date.helpers.MonthDateStepper;

import org.junit.Before;
import org.junit.Test;

public class GeneratorTest {
	GlobalData data;
	private PeriodicalTransaction p1;
	private PeriodicalTransaction p2;

	@Before
	public void setUp() throws Exception {
		data = new GlobalData();
		Account account = new Account("account", 0.0);
		data.add(account);
		ArrayList<SubTransaction> subTransactions = new ArrayList<SubTransaction>();
		p1 = new PeriodicalTransaction("p1", null, 1., account, Mode.UNDEFINED, Category.UNDEFINED, subTransactions, new Date(112,1,1), true, new MonthDateStepper(1, 1));
		data.add(p1);
		p2 = new PeriodicalTransaction("p2", null, 2., account, Mode.UNDEFINED, Category.UNDEFINED, subTransactions, new Date(112,1,1), true, new MonthDateStepper(2, 1));
		data.add(p2);
	}

	@Test
	public void test() {
		Generator generator = new Generator(data);
		// No transactions while date is not set
		assertEquals(0,generator.getNbTransactions());
		// Right number of transactions while setting the date
		Date futureDate = new Date(112,5,2);
		generator.setDate(futureDate);
		assertEquals(8,generator.getNbTransactions());
		// No more transaction when removing date
		generator.setDate(null);
		assertEquals(0,generator.getNbTransactions());
//		// Verify the modified
//		generator.setDate(futureDate);
//		assertEquals(8,generator.getNbTransactions());
	}
	
	@Test
	public void edit() {
		Generator generator = new Generator(data);
		Date futureDate = new Date(112,5,2);
		generator.setDate(futureDate);
		// Edit the description of last transaction 
		int tIndex = generator.getNbTransactions()-1;
		Transaction t = generator.getTransaction(tIndex);
		t = new Transaction(t.getDate(), t.getNumber(), "Test", t.getComment(), t.getAmount(), t.getAccount(), t.getMode(), t.getCategory(),
				t.getValueDate(), t.getStatement(), Arrays.asList(t.getSubTransactions()));
		generator.setTransaction(tIndex, t);
		// Verify the description is changed
		assertEquals("Test", generator.getTransaction(tIndex).getDescription());
		// Verify the description is not erased by setting the date to null to reverting it back to its initial value
		generator.setDate(null);
		generator.setDate(futureDate);
		assertEquals("Test", generator.getTransaction(tIndex).getDescription());
	}

}
