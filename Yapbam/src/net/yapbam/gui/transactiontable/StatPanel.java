package net.yapbam.gui.transactiontable;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.text.DecimalFormat;
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
import java.awt.Insets;

@SuppressWarnings("serial")
public class StatPanel extends JPanel {
	private StatData data;

	private JLabel content;
	private JLabel showButton;
	private JLabel summaryLabel;

	public StatPanel(FilteredData data) {
		super();
		this.initialize();
		this.data = new StatData(data);
		this.data.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				doUpdate();
			}
		});
		doUpdate();
		setDeployed(false);
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints cShow = new GridBagConstraints();
		cShow.insets = new Insets(0, 0, 5, 0);
		cShow.gridx = 0;
		cShow.anchor = GridBagConstraints.WEST;
		cShow.gridy = 0;
		add(getShowButton(), cShow);
		GridBagConstraints gbcSummaryLabel = new GridBagConstraints();
		gbcSummaryLabel.weightx = 1.0;
		gbcSummaryLabel.anchor = GridBagConstraints.WEST;
		gbcSummaryLabel.gridx = 1;
		gbcSummaryLabel.gridy = 0;
		add(getSummaryLabel(), gbcSummaryLabel);
		GridBagConstraints cContent = new GridBagConstraints();
		cContent.anchor = GridBagConstraints.WEST;
		cContent.insets = new Insets(0, 0, 5, 0);
		cContent.gridwidth = 0;
		cContent.gridx = 1;
		cContent.weightx = 1.0;
		cContent.gridy = 1;
		add(getContent(), cContent);
	}
	
	private void doUpdate() {
		if (data.getNbExpenses()+data.getNbReceipts()==0) {
			this.setVisible(false);
		} else {
			DecimalFormat format = LocalizationData.getCurrencyInstance();
			String message = Formatter.format(LocalizationData.get("MainFrame.stat.summary"), //$NON-NLS-1$
					data.getNbExpenses(),
					data.getNbReceipts(),
					format.format(data.getReceipts()+data.getExpenses())
					);
			getContent().setText(message);
			String summary = Formatter.format(LocalizationData.get("StatementView.statementSummary"), data.getNbExpenses()+data.getNbReceipts(), //$NON-NLS-1$
					format.format(data.getExpenses()), format.format(data.getReceipts()));
			getSummaryLabel().setText(summary);
			this.setVisible(true);
		}
	}
	
	private void setDeployed(boolean deployed) {
		getContent().setVisible(deployed);
		getShowButton().setIcon(IconManager.get(deployed?Name.SPREAD:Name.SPREADABLE));
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
			showButton = new JLabel(IconManager.get(IconManager.Name.SPREADABLE));
			showButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					setDeployed(!isDeployed());
				}
			});
		}
		return showButton;
	}
	private JLabel getSummaryLabel() {
		if (summaryLabel == null) {
			summaryLabel = new JLabel();
		}
		return summaryLabel;
	}
}