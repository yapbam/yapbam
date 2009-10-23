package net.yapbam.date;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jfree.ui.DateChooserPanel;

public class JFreeTest extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private DateChooserPanel dateChooserPanel = null;

	/**
	 * This is the default constructor
	 */
	public JFreeTest() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		jLabel = new JLabel();
		jLabel.setText("This is a JFree date panel test");
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(jLabel, BorderLayout.NORTH);
		this.add(getDateChooserPanel(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes dateChooserPanel	
	 * 	
	 * @return org.jfree.ui.DateChooserPanel	
	 */
	private DateChooserPanel getDateChooserPanel() {
		if (dateChooserPanel == null) {
			dateChooserPanel = new DateChooserPanel(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					super.actionPerformed(arg0);
					System.out.println (arg0);
				}
				
			};
			dateChooserPanel.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					System.out.println (evt);					
				}
			});
			dateChooserPanel.setChosenDateButtonColor(Color.GRAY);
			dateChooserPanel.setChosenOtherButtonColor(Color.WHITE);
		}
		return dateChooserPanel;
	}

}
