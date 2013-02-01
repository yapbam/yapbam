package net.yapbam.gui.filter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import java.awt.Insets;

import net.yapbam.data.FilteredData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;

import java.awt.Color;

public class FilterView extends JPanel {
	public static String DEPLOYED_PROPERTY = "deployed";
	private static final long serialVersionUID = 1L;
	private JLabel btnOpen;
	private JPanel elementsPane;
	private JLabel lblFilter;
	private boolean deployed;
	private FilteredData data;

	/**
	 * Create the panel.
	 */
	public FilterView(FilteredData data) {
		this.data = data;
		setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		deployed = false;
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		btnOpen = new JLabel(IconManager.get(Name.DEPLOY));
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
			@Override
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

		setFilterElements(new FilterElementView[]{new FilterElementView("New label"),
				new Toto("New label just a little bit loooooooooooooooooooooooooooooooooooong, for fun !!!")});
		internalSetDeployed(false);
		
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (event instanceof EverythingChangedEvent) {
					refreshFilter();
				} else {
					System.out.println ("event "+event.toString()+" was ignored");
				}
			}
		});
		refreshFilter();
	}
	
	@SuppressWarnings("serial")
	class Toto extends FilterElementView {
		Toto(String label) {
			super(label);
		}
		@Override
		protected Component buildPopupContent() {
			AmountPanel amountPanel = new AmountPanel();
			amountPanel.setAmounts(data.getFilter().getMinAmount(), data.getFilter().getMaxAmount());
			return amountPanel;
		}
	}
	
	private void setFilterElements(FilterElementView[] elements) {
		elementsPane.removeAll();
		for (int i = 0; i < elements.length; i++) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 5, 0);
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.weighty = i==elements.length-1?1.0:0.0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = i;
			elementsPane.add(elements[i], gbc);
		}
	}

	private void internalSetDeployed(boolean deploy) {
		elementsPane.setBorder(deploy?new TitledBorder(null, "Filters", TitledBorder.LEADING, TitledBorder.TOP, null, null):null);
		lblFilter.setVisible(!deploy);
		btnOpen.setIcon(IconManager.get(deploy?Name.UNDEPLOY:Name.DEPLOY));
		elementsPane.setVisible(deploy);
		this.deployed = deploy;
	}
	
	public void setDeployed(boolean deploy) {
		if (deploy != this.deployed) {
			internalSetDeployed(deploy);
			this.firePropertyChange(DEPLOYED_PROPERTY, !deploy, deploy);
		}
	}

	public boolean isDeployed() {
		return this.deployed;
	}
	
	private void refreshFilter() {
		if (data.getFilter().isActive()) {
			lblFilter.setText("A filter is set"); //LOCAL
		} else {
			lblFilter.setText("No filter is set");
		}
	}
}
