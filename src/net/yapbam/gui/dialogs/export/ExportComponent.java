package net.yapbam.gui.dialogs.export;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.StringUtils;

import com.alexandriasoftware.swing.JSplitButton;
import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.dialog.FileChooser;
import com.fathzer.soft.ajlib.utilities.FileUtils;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.FriendlyTable;

public class ExportComponent extends JSplitButton {
	
	private static final long serialVersionUID = 1L;

	private FriendlyTable table;
	
	public ExportComponent(FriendlyTable table) {
		super(LocalizationData.get("ExportComponent.export"));
		
		this.table = table;
		
		this.setPreferredSize(new Dimension(120, this.getMinimumSize().height));
		this.setToolTipText(LocalizationData.get("ExportComponent.export.toolTip")); //$NON-NLS-1$
		
		ActionListener exportActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtils.isNotBlank(e.getActionCommand())) {

					ExportFormatType exportType = ExportFormatType.valueOf(e.getActionCommand());

					//TODO Commonalize with MainMenuBar dialog
					// A main difference to verify is the fact the file filter is not choosable here which seems better
					JFileChooser chooser = new FileChooser();
					chooser.setLocale(LocalizationData.getLocale());
					chooser.setAcceptAllFileFilterUsed(false);
					chooser.setFileFilter(new FileNameExtensionFilter( //
							exportType.getDescription(), //
							exportType.getExtension() //
					));

					File file = chooser.showSaveDialog(Utils.getOwnerWindow(ExportComponent.this)) == JFileChooser.APPROVE_OPTION
							? chooser.getSelectedFile()
							: null;

					if (file != null) {
						if (!file.getPath().endsWith(exportType.getExtension())) {
							file = new File(file.getPath() + "." + exportType.getExtension());
						}
						final Exporter<FriendlyTable> exporter = new DefaultTableExporter(LocalizationData.getLocale());
						export(ExportComponent.this.table, exporter, file, exportType, new ExporterParameters(), Utils.getOwnerWindow(ExportComponent.this));
					}
				}
			}
		};
		
		JPopupMenu exportMenu = new JPopupMenu();
		for (ExportFormatType formatType : ExportFormatType.values()) {
			JMenuItem menuItem = new JMenuItem(Formatter.format(LocalizationData.get("ExportComponent.exportAs"),  //$NON-NLS-1$
					formatType.getDescription(), formatType.getExtension())
			);
			menuItem.setActionCommand(formatType.name());
			menuItem.addActionListener(exportActionListener);
			exportMenu.add(menuItem);
		}
		this.setPopupMenu(exportMenu);
	}

	public static <T> void export(T data, Exporter<T> exporter, File file, ExportFormatType exportType, ExporterParameters params, Window parent) {
		try {
			export(data, exporter, file, exportType, params);
			JOptionPane.showMessageDialog(parent, LocalizationData.get("ExportDialog.done"), LocalizationData.get("ExportDialog.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IOException ex) {
			ErrorManager.INSTANCE.display(parent, ex);
		}
	}

	public static <T> void export(T data, Exporter<T> exporter, File file, ExportFormatType exportType, ExporterParameters params) throws IOException {
		file = FileUtils.getCanonical(file);
		FileOutputStream outputStream = new FileOutputStream(file);
		try {
			final IExportableFormat formatter = exportType.getTableExporter(outputStream, params);
			exporter.export(data, formatter);
			formatter.close();
		} finally {
			outputStream.close();
		}
	}
}
