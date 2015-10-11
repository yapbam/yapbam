package net.yapbam.gui.widget;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import java.awt.Insets;

@SuppressWarnings("serial")
public abstract class AbstractStatPanel<T extends Observable, V> extends JPanel {
	private T data;

	private JComponent details;
	private JLabel showButton;
	private JLabel summaryLabel;

	public AbstractStatPanel(V data) {
		super();
		this.data = buildData(data);
		this.initialize();
		this.data.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				doUpdate();
			}
		});
		doUpdate();
		setDeployed(false);
	}
	
	protected T getData() {
		return data;
	}
	
	protected abstract T buildData(V data);
	
	protected abstract JComponent buildDetails();

	protected void initialize() {
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
		add(getSummary(), gbcSummaryLabel);
		GridBagConstraints cContent = new GridBagConstraints();
		cContent.anchor = GridBagConstraints.WEST;
		cContent.insets = new Insets(0, 0, 5, 0);
		cContent.gridwidth = 0;
		cContent.gridx = 1;
		cContent.weightx = 1.0;
		cContent.gridy = 1;
		add(getDetails(), cContent);
	}
	
	protected abstract void doUpdate();
	
	protected void setDeployed(boolean deployed) {
		getDetails().setVisible(deployed);
		getShowButton().setIcon(IconManager.get(deployed?Name.SPREAD:Name.SPREADABLE));
		getShowButton().setToolTipText(deployed?LocalizationData.get("MainFrame.stat.hide.tip"):LocalizationData.get("MainFrame.stat.show.tip")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	private boolean isDeployed() {
		return getDetails().isVisible();
	}
	
	protected JComponent getDetails() {
		if (details==null) {
			details = buildDetails();
		}
		return details;
	}
	
	protected JLabel getShowButton() {
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
	protected JLabel getSummary() {
		if (summaryLabel == null) {
			summaryLabel = new JLabel();
		}
		return summaryLabel;
	}
}