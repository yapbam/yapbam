package net.yapbam.gui.administration;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.widget.TabbedPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class AdministrationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private GlobalData data;
	private AbstractAdministrationPanel[] panels;

	private TabbedPane tabbedPane;
	
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
		tabbedPane = new TabbedPane();
		this.add(tabbedPane, gridBagConstraints);
		panels = new AbstractAdministrationPanel[]{
				new PeriodicalTransactionListPanel(data),
				new AccountAdministrationPanel(data),
				new CategoryListPanel(data)
		};
		for (int i = 0; i < panels.length; i++) {
			tabbedPane.addTab(panels[i].getPanelTitle(), null, panels[i].getPanel(), panels[i].getPanelToolTip());
		}
	}

	void saveState() {
		YapbamState.INSTANCE.saveState(tabbedPane, this.getClass().getCanonicalName());
		for (int i = 0; i < panels.length; i++) {
			panels[i].saveState();
		}
	}

	void restoreState() {
		for (int i = 0; i < panels.length; i++) {
			panels[i].restoreState();
		}
		YapbamState.INSTANCE.restoreState(tabbedPane, this.getClass().getCanonicalName());
	}
}
