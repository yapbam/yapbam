package net.yapbam.gui.dialogs;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.astesana.ajlib.swing.dialog.FileChooser;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.dropbox.DropboxFileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

@SuppressWarnings("serial")
public class MultiSourceFileChooser extends JTabbedPane {
	public enum Type {
    OPEN, SAVE 
	}
	
	public enum Source {
		FILE, DROPBOX
	}
	
	private JFileChooser fileChooser;
	private DropboxFileChooser dropboxChooser;

	/**
	 * Create the panel.
	 */
	public MultiSourceFileChooser() {
		setTabPlacement(JTabbedPane.LEFT);
		JPanel dummy = new JPanel();
		dummy.add(getFileChooser());
		addTab("Computer", new ImageIcon(getClass().getResource("computer.png")), dummy, "Select this tab to save/read data to/from a local storage");
		addTab("Dropbox", new ImageIcon(getClass().getResource("dropbox.png")), getDropboxChooser(), "Select this tab to save/read data to/from Dropbox");
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(getSelectedComponent() instanceof DropboxFileChooser) {
					((DropboxFileChooser)getSelectedComponent()).connect();
				}
			}
		});
	}

	private DropboxFileChooser getDropboxChooser() {
		if (dropboxChooser==null) {
			dropboxChooser = new DropboxFileChooser();
		}
		return dropboxChooser;
	}

	private JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new FileChooser();
			fileChooser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
						System.out.println("File selected: " + fileChooser.getSelectedFile());
					} else if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
						System.out.println("Cancel was called");
					} else {
						System.out.println("Something else: " + e.getActionCommand());
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
    JFrame frame = new JFrame("MultiSourceFileChooser Demo");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				try {
					Preferences.INSTANCE.save();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

    //Add content to the window.
    frame.getContentPane().add(new MultiSourceFileChooser());

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
