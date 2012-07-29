package net.yapbam.file;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JRadioButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.astesana.ajlib.swing.dialog.FileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

@SuppressWarnings("serial")
public class CompoundFileChooser extends JPanel {
	public enum Type {
    OPEN, SAVE 
	}
	
	public enum Source {
		FILE, DROPBOX
	}
	
	private static final String FILE = "file";
	private static final String DROPBOX = "dropbox";
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton fileBtn;
	private JRadioButton dropboxBtn;
	private JPanel chooserPanel;
	private JFileChooser fileChooser;
	private DropboxFileChooser dropboxChooser;

	/**
	 * Create the panel.
	 */
	public CompoundFileChooser() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		GridBagLayout gbl_panel = new GridBagLayout();
		panel.setLayout(gbl_panel);
		
		fileBtn = new JRadioButton("File");
		fileBtn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (fileBtn.isSelected()) ((CardLayout)getChooserPanel().getLayout()).show(getChooserPanel(), FILE);
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
				if (dropboxBtn.isSelected()) ((CardLayout)getChooserPanel().getLayout()).show(getChooserPanel(), DROPBOX);
			}
		});
		buttonGroup.add(dropboxBtn);
		GridBagConstraints gbc_dropboxBtn = new GridBagConstraints();
		gbc_dropboxBtn.weighty = 1.0;
		gbc_dropboxBtn.anchor = GridBagConstraints.NORTHWEST;
		gbc_dropboxBtn.gridx = 0;
		gbc_dropboxBtn.gridy = 1;
		panel.add(dropboxBtn, gbc_dropboxBtn);
		
		add(getChooserPanel(), BorderLayout.CENTER);
	}

	private JPanel getChooserPanel() {
		if (chooserPanel==null) {
			chooserPanel = new JPanel();
			chooserPanel.setLayout(new CardLayout(0, 0));
			
			chooserPanel.add(getFileChooser(), FILE);
			chooserPanel.add(getDropboxChooser(), DROPBOX);
		}
		return chooserPanel;
	}

	private DropboxFileChooser getDropboxChooser() {
		if (dropboxChooser==null) {
			dropboxChooser = new DropboxFileChooser();
		}
		return dropboxChooser;
	}

	private JFileChooser getFileChooser() {
		if (fileChooser==null) {
			fileChooser = new FileChooser();
			fileChooser.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
			     if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
			        System.out.println("File selected: " + fileChooser.getSelectedFile());
			     } else if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
			       System.out.println("Cancel was called");	 
			     } else {
			    	 System.out.println ("Something else: "+e.getActionCommand());
			     }
			  }
			});
		}
		return fileChooser;
	}
	
	public void setDialogType(Type type) {
		getFileChooser().setDialogType(type.equals(Type.OPEN)?JFileChooser.OPEN_DIALOG:JFileChooser.SAVE_DIALOG);
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
