package net.yapbam.gui.statementview;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.util.NullUtils;

public class StatementBuilder {
	private Statement[] statements;
	
	public StatementBuilder(GlobalData data, Account account) {
		HashMap<String, Statement> map = new HashMap<String, Statement>();
		for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			if (transaction.getAccount().getName().equals(account.getName())) {
				String statementId = transaction.getStatement();
				Statement statement = map.get(statementId);
				if (statement==null) {
					statement = new Statement(statementId);
					map.put(statementId, statement);
				}
				statement.add(transaction);
			}
		}
		this.statements = map.values().toArray(new Statement[map.size()]);
		Arrays.sort(statements, new Comparator<Statement>() {
			@Override
			public int compare(Statement o2, Statement o1) {
				if ((o2.getId()==null) || (o1.getId()==null)) return NullUtils.compareTo(o2.getId(),o1.getId(),false);
				int result = o2.getMediumDate()-o1.getMediumDate();
				if (result==0) result = NullUtils.compareTo(o2.getId(),o1.getId(),false);
				return result;
			}
		});
		if (statements.length>0) statements[0].setStartBalance(account.getInitialBalance());
		for (int i = 1; i < statements.length; i++) {
			statements[i].setStartBalance(statements[i-1].getEndBalance());
		}
	}

	public Statement[] getStatements() {
		return this.statements;
	}
}