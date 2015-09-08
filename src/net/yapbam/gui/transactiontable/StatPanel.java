package net.yapbam.gui.transactiontable;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fathzer.jlocal.Formatter;

import net.yapbam.data.FilteredData;
import net.yapbam.data.StatData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class StatPanel extends JPanel {
	private StatData data;
	private boolean needRefresh;

	private JLabel content;
	private JLabel showButton;

	public StatPanel(FilteredData data) {
		super();
		this.initialize();
		this.data = new StatData(data);
		this.data.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if (isDeployed()) {
					doUpdate();;
				}
			}
		});
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints cContent = new GridBagConstraints();
		add(getContent(), cContent);
		GridBagConstraints cShow = new GridBagConstraints();
		cShow.gridy = 1;
		add(getShowButton(), cShow);
	}
	
	private void doUpdate() {
		String message = Formatter.format("<html>Nombre de dépenses : <b>{0}</b>, montant : <b>{1}</b><br><html>Nombre de recettes : <b>{2}</b>, montant : <b>{3}</b><br><html>Nombre de d'opérations : <b>{4}</b>, solde : <b>{5}</b></html>",
				data.getNbExpenses(),
				LocalizationData.getCurrencyInstance().format(-data.getExpenses()),
				data.getNbReceipts(),
				LocalizationData.getCurrencyInstance().format(data.getReceipts()),
				data.getNbExpenses()+data.getNbReceipts(),
				LocalizationData.getCurrencyInstance().format(data.getReceipts()+data.getExpenses())
				);
		getContent().setText(message);
		//TODO
		System.out.println ("StatPanel.doUpdate done");
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
			content = new JLabel();
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