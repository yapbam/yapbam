package net.yapbam.gui.transactiontable;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fathzer.jlocal.Formatter;

import net.yapbam.data.FilteredData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.NeedToBeSavedChangedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class StatPanel extends JPanel {
	private FilteredData data;
	private boolean needRefresh;

	private JLabel content;
	private JLabel showButton;

	public StatPanel(FilteredData data) {
		super();
		this.data = data;
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (!(event instanceof NeedToBeSavedChangedEvent)) {
					update();
				}
			}
		});
		this.initialize();
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints cContent = new GridBagConstraints();
		add(getContent(), cContent);
		GridBagConstraints cShow = new GridBagConstraints();
		cShow.gridy = 1;
		add(getShowButton(), cShow);
	}
	
	private void update() {
		if (isVisible()) {
			doUpdate();
		} else {
			needRefresh = true;
		}
	}
	
	private void doUpdate() {
		int nbReceipts = 0;
		double receipts = 0.0;
		int nbExpenses = 0;
		double expenses = 0.0;
		for (int i = 0; i < data.getTransactionsNumber(); i++) {
			double amount = data.getTransaction(i).getAmount();
			if (amount<0) {
				nbExpenses++;
				expenses += amount;
			} else {
				nbReceipts++;
				receipts += amount;
			}
		}
		String message = Formatter.format("<html>Nombre de dépenses : <b>{0}</b>, montant : <b>{1}</b><br><html>Nombre de recettes : <b>{2}</b>, montant : <b>{3}</b><br><html>Nombre de d'opérations : <b>{4}</b>, solde : <b>{5}</b></html>",
				nbExpenses,
				LocalizationData.getCurrencyInstance().format(-expenses),
				nbReceipts,
				LocalizationData.getCurrencyInstance().format(receipts),
				nbExpenses+nbReceipts,
				LocalizationData.getCurrencyInstance().format(receipts+expenses)
				);
		getContent().setText(message);
		//TODO
		System.out.println ("Update done");
		needRefresh = false;
	}
	
	private void setDeployed(boolean deployed) {
		if (deployed) {
			if (!isDeployed() && needRefresh) {
				doUpdate();
			}
		}
		getContent().setVisible(deployed);
		getShowButton().setIcon(IconManager.get(deployed?Name.SPREAD_UP:Name.SPREAD));
	}
	
	private boolean isDeployed() {
		return getContent().isVisible();
	}
	
	private JLabel getContent() {
		if (content==null) {
			content = new JLabel("this is a test");
		}
		return content;
	}
	
	private JLabel getShowButton() {
		if (showButton==null) {
			showButton = new JLabel(IconManager.get(IconManager.Name.SPREAD_UP));
			showButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					setDeployed(!isDeployed());
				}
			});
		}
		return showButton;
	}
}