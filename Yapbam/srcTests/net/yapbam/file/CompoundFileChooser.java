package net.yapbam.file;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JRadioButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JFileChooser;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

@SuppressWarnings("serial")
public class CompoundFileChooser extends JPanel {

	private static final String FILE = "file";
	private static final String DROPBOX = "dropbox";
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton fileBtn;
	private JRadioButton dropboxBtn;
	private JPanel chooserPanel;

	/**
	 * Create the panel.
	 */
	public CompoundFileChooser() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{109, 0};
		gbl_panel.rowHeights = new int[]{23, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		fileBtn = new JRadioButton("File");
		fileBtn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (fileBtn.isSelected()) ((CardLayout)chooserPanel.getLayout()).show(chooserPanel, FILE);
			}
		});
		buttonGroup.add(fileBtn);
		fileBtn.setSelected(true);
		GridBagConstraints gbc_fileBtn = new GridBagConstraints();
		gbc_fileBtn.insets = new Insets(0, 0, 5, 0);
		gbc_fileBtn.anchor = GridBagConstraints.WEST;
		gbc_fileBtn.gridx = 0;
		gbc_fileBtn.gridy = 0;
		panel.add(fileBtn, gbc_fileBtn);
		
		dropboxBtn = new JRadioButton("Dropbox");
		dropboxBtn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (dropboxBtn.isSelected()) ((CardLayout)chooserPanel.getLayout()).show(chooserPanel, DROPBOX);
			}
		});
		buttonGroup.add(dropboxBtn);
		GridBagConstraints gbc_dropboxBtn = new GridBagConstraints();
		gbc_dropboxBtn.anchor = GridBagConstraints.NORTHWEST;
		gbc_dropboxBtn.gridx = 0;
		gbc_dropboxBtn.gridy = 1;
		panel.add(dropboxBtn, gbc_dropboxBtn);
		
		chooserPanel = new JPanel();
		add(chooserPanel, BorderLayout.CENTER);
		chooserPanel.setLayout(new CardLayout(0, 0));
		
		JFileChooser fileComponent = new JFileChooser();
		chooserPanel.add(fileComponent, FILE);
		
		JPanel dropboxComponent = new JPanel();
		chooserPanel.add(dropboxComponent, DROPBOX);

	}
	
  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event dispatch thread.
   */
  private static void createAndShowGUI() {
      //Create and set up the window.
      JFrame frame = new JFrame("CompoundFileChooser Demo");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //Add content to the window.
      frame.getContentPane().add(new CompoundFileChooser());

      //Display the window.
      frame.pack();
      frame.setVisible(true);
  }

  public static void main(String[] args) {
      //Schedule a job for the event dispatch thread:
      //creating and showing this application's GUI.
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              createAndShowGUI();
          }
      });
  }

}
