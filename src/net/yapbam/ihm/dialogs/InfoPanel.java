package net.yapbam.ihm.dialogs;

import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public abstract class InfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel northPanel = null;
	private JLabel iconLabel = null;
	private JLabel textLabel = null;

	/**
	 * This is the default constructor
	 */
	public InfoPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(309, 281);
		this.setLayout(new BorderLayout());
		this.add(getNorthPanel(), BorderLayout.NORTH);
		this.add(getCenterComponent(), BorderLayout.EAST);
	}

	/**
	 * This method initializes northPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 0;
			textLabel = new JLabel();
			textLabel.setText(getNorthMessage());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 0;
			northPanel = new JPanel();
			northPanel.setLayout(new GridBagLayout());
			northPanel.add(getIconLabel(), gridBagConstraints);
			northPanel.add(textLabel, gridBagConstraints1);
		}
		return northPanel;
	}

	protected abstract String getNorthMessage();

	/**
	 * This method initializes iconLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getIconLabel() {
		if (iconLabel == null) {
			iconLabel = new JLabel();
			iconLabel.setText("");
			iconLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
		}
		return iconLabel;
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	protected abstract JComponent getCenterComponent();
}  //  @jve:decl-index=0:visual-constraint="10,10"
