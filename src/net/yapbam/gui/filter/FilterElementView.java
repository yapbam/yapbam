package net.yapbam.gui.filter;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

import net.yapbam.gui.IconManager;

import org.jfree.ui.DateChooserPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class FilterElementView extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel label;

	/**
	 * Create the panel.
	 */
	public FilterElementView() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{100};
		setLayout(gridBagLayout);
		
		label = new JLabel(" ");
		label.setOpaque(true);
		label.setMaximumSize(new Dimension(150, 40));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.BOTH;
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		add(label, gbc_label);
		
		label.setText("New label just a little bit loooooooooooooooooooooooooooooooooooong, for fun !!!");
		Dimension d = label.getPreferredSize();  
    label.setPreferredSize(new Dimension(150,d.height));		
		
		final JPopupMenu popup = new JPopupMenu();

		final JLabel button = new JLabel(IconManager.SPREADABLE);
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.anchor = GridBagConstraints.WEST;
		gbc_button.fill = GridBagConstraints.VERTICAL;
		gbc_button.gridx = 1;
		gbc_button.gridy = 0;
		add(button, gbc_button);
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (button.isEnabled() && !popup.isVisible()) {
					popup.removeAll();
					DateChooserPanel widget = new DateChooserPanel();
					widget.setChosenDateButtonColor(Color.RED);
					widget.setChosenOtherButtonColor(Color.GRAY);
					widget.setChosenMonthButtonColor(Color.WHITE);
					popup.add(widget);
					popup.show(button, button.getSize().width, widget.getHeight());
				}
			}
		});
	}

}
