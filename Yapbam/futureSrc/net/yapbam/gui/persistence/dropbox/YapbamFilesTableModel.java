package net.yapbam.gui.persistence.dropbox;

import net.astesana.dropbox.FilesTableModel;

@SuppressWarnings("serial")
public class YapbamFilesTableModel extends FilesTableModel {
	/* (non-Javadoc)
	 * @see net.astesana.dropbox.FilesTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String fileName = getEntry(rowIndex).fileName();
		return fileName.substring(0, fileName.length()-DropboxPersistencePlugin.ZIP_ENTENSION.length());
	}
}
