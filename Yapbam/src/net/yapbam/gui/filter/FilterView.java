package net.yapbam.gui.filter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import java.awt.Insets;

import net.yapbam.gui.IconManager;
import java.awt.Color;

public class FilterView extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel btnOpen;
	private Component filterElement;
	private JPanel elementsPane;
	private JLabel lblFilter;
	private boolean deployed;
	private FilterElementView label;

	/**
	 * Create the panel.
	 */
	public FilterView() {
		setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		deployed = false;
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		btnOpen = new JLabel(IconManager.DEPLOY);
		btnOpen.setToolTipText("Click here to display the current filter");
		GridBagConstraints gbc_btnOpen = new GridBagConstraints();
		gbc_btnOpen.gridheight = 0;
		gbc_btnOpen.insets = new Insets(0, 0, 0, 0);
		gbc_btnOpen.weighty = 1.0;
		gbc_btnOpen.gridx = 1;
		gbc_btnOpen.gridy = 0;
		add(btnOpen, gbc_btnOpen);
		
		lblFilter = new JLabel("Filter");
		lblFilter.setUI(new VerticalLabelUI(false));
		GridBagConstraints gbc_lblFilter = new GridBagConstraints();
		gbc_lblFilter.gridheight = 0;
		gbc_lblFilter.insets = new Insets(0, 0, 0, 0);
		gbc_lblFilter.gridx = 0;
		gbc_lblFilter.gridy = 0;
		add(lblFilter, gbc_lblFilter);
		java.awt.event.MouseAdapter listener = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				setDeployed(!deployed);
			}
		};
		btnOpen.addMouseListener(listener);
		
		elementsPane = new JPanel();
		GridBagConstraints gbc_elementsPane = new GridBagConstraints();
		gbc_elementsPane.weighty = 1.0;
		gbc_elementsPane.fill = GridBagConstraints.BOTH;
		gbc_elementsPane.gridx = 2;
		gbc_elementsPane.gridy = 0;
		add(elementsPane, gbc_elementsPane);
		GridBagLayout gbl_elementsPane = new GridBagLayout();
		elementsPane.setLayout(gbl_elementsPane);

		filterElement = new FilterElementView("New label just a little bit loooooooooooooooooooooooooooooooooooong, for fun !!!");
		GridBagConstraints gbc_filterElement = new GridBagConstraints();
		gbc_filterElement.insets = new Insets(0, 0, 5, 0);
		gbc_filterElement.anchor = GridBagConstraints.NORTHWEST;
		gbc_filterElement.weighty = 1.0;
		gbc_filterElement.fill = GridBagConstraints.HORIZONTAL;
		gbc_filterElement.gridx = 0;
		gbc_filterElement.gridy = 1;
		elementsPane.add(filterElement, gbc_filterElement);
		
		label = new FilterElementView("New label");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.NORTHWEST;
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		elementsPane.add(label, gbc_label);
		
		setDeployed(false);
	}

	private void setDeployed(boolean deploy) {
		elementsPane.setBorder(deploy?new TitledBorder(null, "Filters", TitledBorder.LEADING, TitledBorder.TOP, null, null):null);
		lblFilter.setVisible(!deploy);
		btnOpen.setIcon(deploy?IconManager.UNDEPLOY:IconManager.DEPLOY);
		filterElement.setVisible(deploy);
		this.deployed = deploy;
	}
}
