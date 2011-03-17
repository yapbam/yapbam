package net.yapbam.gui.administration;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.yapbam.data.FilteredData;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class AdministrationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private FilteredData data;
	private AbstractAdministrationPanel[] panels;
	
	/**
	 * This is the constructor
	 */
	public AdministrationPanel(FilteredData data) {
		super();
		this.data = data;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		JTabbedPane jTabbedPane = new JTabbedPane();
//		jTabbedPane.setTabPlacement(JTabbedPane.LEFT);
		this.add(jTabbedPane, gridBagConstraints);
		panels = new AbstractAdministrationPanel[]{
				new PeriodicalTransactionListPanel(data),
				new AccountAdministrationPanel(data.getGlobalData()),
				new CategoryListPanel(data.getGlobalData())
		};
		for (int i = 0; i < panels.length; i++) {
			jTabbedPane.addTab(panels[i].getPanelTitle(), null, panels[i].getPanel(), panels[i].getPanelToolTip());
		}
	}

	void saveState() {
		for (int i = 0; i < panels.length; i++) {
			panels[i].saveState();
		}
	}

	void restoreState() {
		for (int i = 0; i < panels.length; i++) {
			panels[i].restoreState();
		}
	}
}
