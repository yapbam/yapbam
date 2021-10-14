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

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.dialog.FileChooser;
import com.fathzer.soft.ajlib.utilities.FileUtils;

import net.yapbam.export.Exporter;
import net.yapbam.export.ExportFormatType;
import net.yapbam.export.ExportWriter;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.JSplitButton;

public abstract class ExportComponent<P extends ExporterParameters, C> extends JSplitButton {
	private static final long serialVersionUID = 1L;
	
	private transient C content;
	
	protected ExportComponent() {
		super(LocalizationData.get("ExportComponent.export"));
		
		this.setPreferredSize(new Dimension(120, this.getMinimumSize().height));
		this.setToolTipText(LocalizationData.get("ExportComponent.export.toolTip")); //$NON-NLS-1$
		
		ActionListener exportActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtils.isNotBlank(e.getActionCommand())) {
					ExportFormatType format = ExportFormatType.valueOf(e.getActionCommand());
					final Window ownerWindow = Utils.getOwnerWindow(ExportComponent.this);
					final Exporter<P, C> exporter = buildExporter();
					chooseFileAndExport(ExportComponent.this.content, format, ownerWindow, exporter);
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
	
	public void setContent(C content) {
		this.content = content;
	}
	
	public abstract Exporter<P,C> buildExporter();
	
	public static <P extends ExporterParameters,T> void chooseFileAndExport(T data, ExportFormatType format, final Window ownerWindow,
			final Exporter<P,T> exporter) {
		JFileChooser chooser = new FileChooser();
		chooser.setLocale(LocalizationData.getLocale());
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileNameExtensionFilter(format.getDescription(),format.getExtension()));
		File file = chooser.showSaveDialog(ownerWindow) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;

		if (file != null) {
			String extension = FileUtils.getExtension(file);
			if (extension == null || !extension.endsWith(format.getExtension())) {
				file = new File(file.getPath() + "." + format.getExtension());
			}
			export(data, exporter, file, format, ownerWindow);
		}
	}
	
	private static <P extends ExporterParameters,T> void export(T data, Exporter<P,T> exporter, File file, ExportFormatType exportType, Window parent) {
		try {
			export(data, exporter, file, exportType);
			JOptionPane.showMessageDialog(parent, LocalizationData.get("ExportDialog.done"), LocalizationData.get("ExportDialog.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IOException ex) {
			ErrorManager.INSTANCE.display(parent, ex);
		}
	}

	public static <P extends ExporterParameters,T> void export(T data, Exporter<P,T> exporter, File file, ExportFormatType exportType) throws IOException {
		file = FileUtils.getCanonical(file);
		final ExportWriter formatter = exportType.getTableExporter(new FileOutputStream(file), exporter.getParameters());
		try {
			exporter.export(data, formatter);
		} finally {
			formatter.close();
		}
	}
}
