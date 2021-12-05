package net.yapbam.gui.dialogs.export;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.dialog.FileChooser;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.statementview.StatementExporterParameters;

public class ExportAccessoryPanel<P extends ExporterParameters> extends JPanel {

	private static final long serialVersionUID = 1L;

	private transient P parameters;
	private JCheckBox includeTitle;
	private JCheckBox includeStartBalance;
	private JCheckBox includeEndBalance;
	private JLabel selectCssLabel;
	private JTextField selectCssTextField;
	private JButton selectCssButton;

	public ExportAccessoryPanel(P parameters) {
		this.parameters = parameters;
		initialize();
	}

	private void initialize() {
		JPanel checkPanel = new JPanel();
		checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
		checkPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		checkPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("ExportAccessoryPanel.formatting")));
		checkPanel.add(getIncludeTitle());
		checkPanel.add(getIncludeStartBalance());
		checkPanel.add(getIncludeEndBalance());

		JPanel cssPanel = new JPanel();
		cssPanel.setLayout(new BorderLayout());
		cssPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("ExportAccessoryPanel.style")));
		cssPanel.add(getSelectCssLabel(), BorderLayout.WEST);
		cssPanel.add(getSelectCssTextField(), BorderLayout.CENTER);
		cssPanel.add(getSelectCssButton(), BorderLayout.EAST);
		cssPanel.add(Box.createVerticalStrut(200), BorderLayout.SOUTH);

		
		this.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("ExportAccessoryPanel.title")));
		this.setLayout(new BorderLayout());
		if (parameters instanceof StatementExporterParameters) {
			this.add(checkPanel, BorderLayout.NORTH);
		}
		this.add(cssPanel, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(400, this.getMinimumSize().height));
	}

	private JLabel getSelectCssLabel() {
		if (selectCssLabel == null) {
			selectCssLabel = new JLabel(LocalizationData.get("ExportAccessoryPanel.style.css"));
		}
		return selectCssLabel;
	}

	private JTextField getSelectCssTextField() {
		if (selectCssTextField == null) {
			selectCssTextField = new JTextField();
			selectCssTextField.setEditable(false);

		}
		return selectCssTextField;
	}

	private JButton getSelectCssButton() {
		if (selectCssButton == null) {
			final Window ownerWindow = Utils.getOwnerWindow(ExportAccessoryPanel.this);

			final JFileChooser cssChooser = new FileChooser();
			cssChooser.setLocale(LocalizationData.getLocale());
			cssChooser.setAcceptAllFileFilterUsed(false);
			cssChooser.setFileFilter(new FileNameExtensionFilter("Cascading Style Sheet", "css"));

			AbstractAction selectCssAction = new AbstractAction() {

				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							File file = cssChooser.showOpenDialog(ownerWindow) == JFileChooser.APPROVE_OPTION
									? cssChooser.getSelectedFile()
									: null;
							getSelectCssTextField().setText(file != null ? file.getPath() : "");
						}
					});
				}
			};
			selectCssAction.putValue(Action.SMALL_ICON, IconManager.get(Name.LINK));

			selectCssButton = new JButton();
			selectCssButton.setAction(selectCssAction);
			selectCssButton.setPreferredSize(new Dimension(40, 20));
		}
		return selectCssButton;
	}

	private JCheckBox getIncludeTitle() {
		if (includeTitle == null) {
			includeTitle = new JCheckBox(LocalizationData.get("ExportAccessoryPanel.formatting.title"), true);
		}
		return includeTitle;
	}

	private JCheckBox getIncludeStartBalance() {
		if (includeStartBalance == null) {
			includeStartBalance = new JCheckBox(LocalizationData.get("ExportAccessoryPanel.formatting.startBalance"), false);
		}
		return includeStartBalance;
	}

	private JCheckBox getIncludeEndBalance() {
		if (includeEndBalance == null) {
			includeEndBalance = new JCheckBox(LocalizationData.get("ExportAccessoryPanel.formatting.endBalance"), false);
		}
		return includeEndBalance;
	}

	public ExporterExtendedParameters getExporterExtendedParameters() {
		ExporterExtendedParameters extendedParameters = new ExporterExtendedParameters();
		if (parameters instanceof StatementExporterParameters) {
			StatementExporterParameters statementExporterParameters = (StatementExporterParameters) parameters;
			if (getIncludeTitle().isSelected()) {
				extendedParameters.setStatementId(statementExporterParameters.getStatementId());
			}
			if (getIncludeStartBalance().isSelected()) {
				extendedParameters.setStartBalance(statementExporterParameters.getStartBalance());
			}
			if (getIncludeEndBalance().isSelected()) {
				extendedParameters.setEndBalance(statementExporterParameters.getEndBalance());
			}
		}
		extendedParameters.setCss(new File(getSelectCssTextField().getText()));
		return extendedParameters;
	}

}
