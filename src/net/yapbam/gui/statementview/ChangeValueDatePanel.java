package net.yapbam.gui.statementview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JPanel;

import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.widget.AutoSelectFocusListener;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;

import com.fathzer.soft.ajlib.swing.widget.date.DateWidget;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChangeValueDatePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton valueDateLabel;
	private DateWidget valueDate;
	private StatementTable table;
	private TransactionsUpdater updater = new TransactionsUpdater() {
		@Override
		protected Transaction update(Transaction t) {
			return new Transaction(t.getDate(), t.getNumber(), t.getDescription(), t.getComment(), t.getAmount(), t.getAccount(), t.getMode(), t.getCategory(),
					valueDate.getDate(), t.getStatement(), Arrays.asList(t.getSubTransactions()));
		}
	};

	public ChangeValueDatePanel(StatementTable transactionTable) {
		super();
		this.table = transactionTable;
		setLayout(new GridBagLayout());
		
		valueDateLabel = new JButton(LocalizationData.get("CheckModePanel.valueDateEnabled")); //$NON-NLS-1$
		valueDateLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.setSelectedTransactions(updater.update(table));
			}
		});
		GridBagConstraints gbcValueDateLabel = new GridBagConstraints();
		gbcValueDateLabel.anchor = GridBagConstraints.WEST;
		gbcValueDateLabel.insets = new Insets(0, 0, 0, 5);
		gbcValueDateLabel.gridx = 0;
		gbcValueDateLabel.gridy = 0;
		add(valueDateLabel, gbcValueDateLabel);
		valueDateLabel.setToolTipText(LocalizationData.get("CheckModePanel.valueDateEnabled.toolTip")); //$NON-NLS-1$
		valueDate = new DateWidget();
		valueDate.getDateField().setMinimumSize(valueDate.getDateField().getPreferredSize());
		GridBagConstraints gbcValueDate = new GridBagConstraints();
		gbcValueDate.gridx = 1;
		gbcValueDate.gridy = 0;
		add(valueDate, gbcValueDate);
		valueDate.setDate(null);
		valueDate.setLocale(LocalizationData.getLocale());
		valueDate.setToolTipText(LocalizationData.get("CheckModePanel.valueDate.tooltip")); //$NON-NLS-1$
		
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				refreshOk();
			}
		};
		if (transactionTable!=null) {
			transactionTable.addPropertyChangeListener(TransactionSelector.SELECTED_PROPERTY, listener);
		}
		valueDate.addPropertyChangeListener(DateWidget.DATE_PROPERTY, listener);
		
		valueDate.getDateField().addFocusListener(AutoSelectFocusListener.INSTANCE);
		refreshOk();
	}
	
	private void refreshOk() {
		boolean ok = valueDate.getDate()!=null;
		ok = ok && (table!=null) && (table.getSelectedTransactions().length>0);
		valueDateLabel.setEnabled(ok);
	}

	public Date getValueDate() {
		return valueDate.getDate();
	}
}
