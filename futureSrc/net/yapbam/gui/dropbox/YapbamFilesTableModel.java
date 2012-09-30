package net.yapbam.gui.dropbox;

import net.astesana.dropbox.FilesTableModel;

public class YapbamFilesTableModel extends FilesTableModel {
	/* (non-Javadoc)
	 * @see net.astesana.dropbox.FilesTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String fileName = getEntry(rowIndex).fileName();
		return fileName.substring(0, fileName.length()-YapbamDropboxFileChooser.ZIP_ENTENSION.length());
	}
}
