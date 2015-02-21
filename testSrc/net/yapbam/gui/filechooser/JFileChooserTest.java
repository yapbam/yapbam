package net.yapbam.gui.filechooser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

import net.yapbam.gui.persistence.YapbamPersistenceManager;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.dialog.FileChooser;
import com.fathzer.soft.jclop.swing.URIChooserDialog;
import com.fathzer.soft.jclop.swing.URIChooserDialog.ConfirmButtonUpdater;

public class JFileChooserTest {
	public static void main (String[] args) {
		setLookAndFeel("CDE/Motif");
//		setLookAndFeel("Nimbus");
//		setLookAndFeel("GTK+");
		
//		JFileChooser fc = new FileChooser() {
//			@Override
//			public void approveSelection() {
//				System.out.println ("Approve selection is called with "+super.getSelectedFile());
//			}
//		};
//		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
//		fc.setMultiSelectionEnabled(false);
//		fc.addPropertyChangeListener(new PropertyChangeListener() {
//			@Override
//			public void propertyChange(PropertyChangeEvent evt) {
//				System.out.println (evt.getPropertyName()+" -> "+evt.getNewValue());
//			}
//		});
//		int result = fc.showSaveDialog(null);
//		if (result==JFileChooser.APPROVE_OPTION) {
//			System.out.println ("Approved is "+fc.getSelectedFile());
//		}
		
		URIChooserDialog dialog = YapbamPersistenceManager.MANAGER.getChooserDialog(null);
		dialog.setSaveDialog(true);
//		dialog.setConfirmIfExisting(false);
		dialog.setTitle("Title"); //$NON-NLS-1$
		dialog.setConfirmButtonUpdater(new ConfirmButtonUpdater() {
			@Override
			public boolean update(JButton button, URI selectedURI, boolean existing) {
System.out.println ("ArchiveAction update was called with URI "+selectedURI+" with existing arg set to "+existing); //TODO
				if (selectedURI==null) {
					return false;
				}
				button.setText(existing?"select":"Create");
				return true;
			}
		});
		System.out.println (dialog.showDialog());
	}
	
	public static void setLookAndFeel(String LFName) {
		String lookAndFeelClass = Utils.getLFClassFromName(LFName);
		if (lookAndFeelClass!=null) {
			try {
				UIManager.setLookAndFeel(lookAndFeelClass);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}


}
