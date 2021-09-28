package net.yapbam.gui.dialogs.export;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.StringUtils;

import com.alexandriasoftware.swing.JSplitButton;
import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.dialog.FileChooser;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.FriendlyTable.ExportFormat;

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

					JFileChooser chooser = new FileChooser();
					chooser.setAcceptAllFileFilterUsed(false);
					chooser.setFileFilter(new FileNameExtensionFilter( //
							exportType.getDescription(), //
							exportType.getExtension() //
					));
					chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));

					File file = chooser.showSaveDialog(Utils.getOwnerWindow(ExportComponent.this)) == JFileChooser.APPROVE_OPTION
							? chooser.getSelectedFile()
							: null;

					if (file != null) {

						if (!file.getPath().endsWith(exportType.getExtension()))
							file = new File(file.getPath() + "." + exportType.getExtension());

						FileOutputStream outputStream = null;
						try {
							outputStream = new FileOutputStream(file);
							ExportFormat exportFormat = null;
							if (ExportFormatType.CSV.equals(exportType)) {
								exportFormat = new TableCsvExporter(outputStream, ';', StandardCharsets.UTF_8, LocalizationData.getLocale());
							} else if (ExportFormatType.HTML.equals(exportType)) {
								exportFormat = new TableHtmlExporter(outputStream, StandardCharsets.UTF_8, LocalizationData.getLocale());
							} else if(ExportFormatType.JSON.equals(exportType)) {
								exportFormat = new TableJsonExporter(outputStream, StandardCharsets.UTF_8, LocalizationData.getLocale());
							}
							if (exportFormat != null)
								exportFormat.export(ExportComponent.this.table, file);
						} catch (IOException ex) {
							ErrorManager.INSTANCE.display(ExportComponent.this, ex);
						} finally {
							if(outputStream != null) {
								try {
									outputStream.close();
								} catch (IOException ex) {
									ErrorManager.INSTANCE.display(ExportComponent.this, ex);
								}
							}
						}
					}
				}
			}
		};
		
		JPopupMenu exportMenu = new JPopupMenu();
		for (ExportFormatType formatType : ExportFormatType.values()) {
			JMenuItem menuItem = new JMenuItem(Formatter.format(LocalizationData.get("ExportComponent.exportAs"),  //$NON-NLS-1$
					formatType.getDescription(), formatType.getExtension()) //
			);
			menuItem.setActionCommand(formatType.name());
			menuItem.addActionListener(exportActionListener);
			exportMenu.add(menuItem);
		}
		this.setPopupMenu(exportMenu);
	}

}
