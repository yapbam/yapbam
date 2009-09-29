package net.yapbam.ihm.administration;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.yapbam.data.GlobalData;

import java.awt.GridBagConstraints;

public class AdministrationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private GlobalData data;
	private AbstractListAdministrationPanel[] panels;
	
	/**
	 * This is the constructor
	 */
	public AdministrationPanel(GlobalData data) {
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
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		JTabbedPane jTabbedPane = new JTabbedPane();
		jTabbedPane.setTabPlacement(JTabbedPane.LEFT);
		panels = new AbstractListAdministrationPanel[]{
/*				new AccountListPanel(data),
				new CategoryListPanel(data),*/
				new PeriodicalTransactionListPanel(data)
		};
		for (int i = 0; i < panels.length; i++) {
			jTabbedPane.addTab(panels[i].getTitle(), null, panels[i], panels[i].getToolTipText());
		}
		this.add(jTabbedPane, gridBagConstraints);
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
