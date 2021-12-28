package net.yapbam.gui.statementview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.dialog.FileChooser;
import com.fathzer.soft.ajlib.utilities.StringUtils;

import net.yapbam.gui.HelpManager;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.dialogs.export.ExportComponent.ExtraFileSelectionPanel;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;

public class ExportAccessoryPanel extends ExtraFileSelectionPanel<StatementExporterParameters> {
	//TODO Split this into 2 classes (1 for the css, one, specific to statements view to select included fields).

	private static final long serialVersionUID = 1L;

	private enum CssTypeEnum {

		NONE(LocalizationData.get("ExportAccessoryPanel.style.css.type.none")),
		LOCAL(LocalizationData.get("ExportAccessoryPanel.style.css.type.local")), 
		REMOTE(LocalizationData.get("ExportAccessoryPanel.style.css.type.remote"));

		final String label;

		CssTypeEnum(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return this.label;
		}

	}

	private JCheckBox includeTitle;
	private JCheckBox includeStartBalance;
	private JCheckBox includeEndBalance;
	private JLabel selectCssLabel;
	private JComboBox<CssTypeEnum> selectCssTypeComboBox;
	private JLabel selectCssHelpLabel;
	private JTextField selectCssTextField;
	private JButton selectCssButton;
	private JLabel selectCssInvalidityCauseLabel;
	
	public ExportAccessoryPanel() {
		initialize();
	}

	private void initialize() {
		JPanel checkPanel = new JPanel();
		checkPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("ExportAccessoryPanel.formatting")));
		checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
		checkPanel.add(getIncludeTitle());
		checkPanel.add(getIncludeStartBalance());
		checkPanel.add(getIncludeEndBalance());

		JPanel cssPanel = new JPanel();
		cssPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("ExportAccessoryPanel.style.css.title")));
		cssPanel.setLayout(new BorderLayout());
		cssPanel.add(getSelectCssTextField(), BorderLayout.CENTER);
		cssPanel.add(getSelectCssButton(), BorderLayout.EAST);
		cssPanel.add(getSelectCssInvalidityCauseLabel(), BorderLayout.SOUTH);
		
		JPanel stylePanel = new JPanel();
		stylePanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("ExportAccessoryPanel.style.title")));
		stylePanel.setLayout(new BorderLayout());
		stylePanel.add(getSelectCssLabel(), BorderLayout.WEST);
		stylePanel.add(getSelectCssTypeComboBox(), BorderLayout.CENTER);
		stylePanel.add(getSelectCssHelpLabel(), BorderLayout.EAST);
		stylePanel.add(cssPanel, BorderLayout.SOUTH);
		
		this.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("ExportAccessoryPanel.title")));
		this.setLayout(new BorderLayout());
		this.add(checkPanel, BorderLayout.NORTH);
		this.add(stylePanel, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(400, this.getMinimumSize().height));
	}

	private JLabel getSelectCssLabel() {
		if (selectCssLabel == null) {
			selectCssLabel = new JLabel(LocalizationData.get("ExportAccessoryPanel.style.css"));
		}
		return selectCssLabel;
	}

	private JComboBox<CssTypeEnum> getSelectCssTypeComboBox() {
		if (selectCssTypeComboBox == null) {
			selectCssTypeComboBox = new JComboBox<CssTypeEnum>();
			for (CssTypeEnum type : CssTypeEnum.values()) {
				selectCssTypeComboBox.addItem(type);
			}
			selectCssTypeComboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							CssTypeEnum type = ((CssTypeEnum) getSelectCssTypeComboBox().getSelectedItem());
							boolean isLocal = CssTypeEnum.LOCAL.equals(type);
							boolean isNone = CssTypeEnum.NONE.equals(type);
							getSelectCssTextField().setEnabled(!isNone);
							getSelectCssTextField().setEditable(!isLocal);
							getSelectCssTextField().setText(YapbamState.INSTANCE.get(getSelectCssTextFieldStateKey(type), StringUtils.EMPTY));
							getSelectCssButton().setEnabled(isLocal && !isNone);
						}
					});
				}
			});
		}
		return selectCssTypeComboBox;
	}
	
	private String getSelectCssTypeComboBoxStateKey() {
		return this.getClass().getCanonicalName() + ".selectCssTypeComboBox";
	}

	private JLabel getSelectCssHelpLabel() {
		if (selectCssHelpLabel == null) {
			selectCssHelpLabel = new JLabel();
			selectCssHelpLabel.setToolTipText(LocalizationData.get("ExportAccessoryPanel.style.css.helpButton.toolTip"));
			selectCssHelpLabel.setIcon(IconManager.get(Name.HELP));
			selectCssHelpLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HelpManager.show(e.getComponent(), HelpManager.EXPORT_CSS);
				}
			});
		}
		return selectCssHelpLabel;
	}

	private JTextField getSelectCssTextField() {
		if (selectCssTextField == null) {
			selectCssTextField = new JTextField();
			selectCssTextField.setEditable(false);
			selectCssTextField.setEnabled(false);
			selectCssTextField.setInputVerifier(new InputVerifier() {
				@Override
				public boolean verify(JComponent input) {
					String url = org.apache.commons.lang3.StringUtils.trimToNull(getSelectCssTextField().getText());
					if (url != null) {
						CssTypeEnum type = ((CssTypeEnum) getSelectCssTypeComboBox().getSelectedItem());
						if (CssTypeEnum.LOCAL.equals(type)) {
							File selectedFile = new File(url);
							return selectedFile.exists() && selectedFile.canRead();
						} else if (CssTypeEnum.REMOTE.equals(type)) {
							// https://regex101.com/library/zB1sS9
							return url.matches("^(((ftp|http|https):\\/\\/)|(\\/)|(..\\/))(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/]))?$");
						}
					}
					return true;
				}
			});
			selectCssTextField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					update(e);
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					update(e);
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					update(e);
				}

				public void update(DocumentEvent e) {
					updateInvalidCause();
				}

			});
		}
		return selectCssTextField;
	}
	
	private String getSelectCssTextFieldStateKey(CssTypeEnum type) {
		return this.getClass().getCanonicalName() + "." + type.name() + ".selectCssTextField";
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
							getSelectCssTextField().setText(file != null ? file.getPath() : StringUtils.EMPTY);
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
	
	private JLabel getSelectCssInvalidityCauseLabel() {
		if (selectCssInvalidityCauseLabel == null) {
			selectCssInvalidityCauseLabel = new JLabel(LocalizationData.get("ExportAccessoryPanel.errorMessage"));
			selectCssInvalidityCauseLabel.setToolTipText(LocalizationData.get("ExportAccessoryPanel.errorMessage.tooltip"));
			selectCssInvalidityCauseLabel.setForeground(Color.RED);
			updateInvalidCause();
		}
		return selectCssInvalidityCauseLabel;
	}
	
	private void updateInvalidCause() {
		getSelectCssInvalidityCauseLabel().setEnabled(!getSelectCssTextField().getInputVerifier().verify(getSelectCssTextField()));
	}

	private JCheckBox getIncludeTitle() {
		if (includeTitle == null) {
			includeTitle = new JCheckBox(LocalizationData.get("ExportAccessoryPanel.formatting.title"), true);
		}
		return includeTitle;
	}

	private String getIncludeTitleStateKey() {
		return this.getClass().getCanonicalName() + ".includeTitle";
	}

	private JCheckBox getIncludeStartBalance() {
		if (includeStartBalance == null) {
			includeStartBalance = new JCheckBox(LocalizationData.get("ExportAccessoryPanel.formatting.startBalance"), false);
		}
		return includeStartBalance;
	}

	private String getIncludeStartBalanceStateKey() {
		return this.getClass().getCanonicalName() + ".includeStartBalance";
	}

	private JCheckBox getIncludeEndBalance() {
		if (includeEndBalance == null) {
			includeEndBalance = new JCheckBox(LocalizationData.get("ExportAccessoryPanel.formatting.endBalance"), false);
		}
		return includeEndBalance;
	}

	private String getIncludeEndBalanceStateKey() {
		return this.getClass().getCanonicalName() + ".includeEndBalance";
	}

	@Override
	public void saveState() {
		YapbamState.INSTANCE.put(getIncludeTitleStateKey(), Boolean.toString(getIncludeTitle().isSelected()));
		YapbamState.INSTANCE.put(getIncludeStartBalanceStateKey(), Boolean.toString(getIncludeStartBalance().isSelected()));
		YapbamState.INSTANCE.put(getIncludeEndBalanceStateKey(), Boolean.toString(getIncludeEndBalance().isSelected()));
		final CssTypeEnum type = ((CssTypeEnum) getSelectCssTypeComboBox().getSelectedItem());
		YapbamState.INSTANCE.put(getSelectCssTypeComboBoxStateKey(), type.name());
		YapbamState.INSTANCE.put(getSelectCssTextFieldStateKey(type), getSelectCssTextField().getText());
	}

	@Override
	public void restoreState() {
		getIncludeTitle().setSelected(Boolean.parseBoolean(YapbamState.INSTANCE.get(getIncludeTitleStateKey())));
		getIncludeStartBalance().setSelected(Boolean.parseBoolean(YapbamState.INSTANCE.get(getIncludeStartBalanceStateKey())));
		getIncludeEndBalance().setSelected(Boolean.parseBoolean(YapbamState.INSTANCE.get(getIncludeEndBalanceStateKey())));
		getSelectCssTypeComboBox().setSelectedItem(CssTypeEnum.valueOf(YapbamState.INSTANCE.get(getSelectCssTypeComboBoxStateKey(), CssTypeEnum.LOCAL.name())));
	}

	@Override
	public void setExtraParameters(StatementExporterParameters params) {
		params.setWithStatementId(getIncludeTitle().isSelected());
		params.setWithStartBalance(getIncludeStartBalance().isSelected());
		params.setWithEndBalance(getIncludeEndBalance().isSelected());
		if (getSelectCssTextField().getInputVerifier().verify(getSelectCssTextField())) {
			String url = org.apache.commons.lang3.StringUtils.trimToNull(getSelectCssTextField().getText());
			if (url != null) {
				try {
					CssTypeEnum cssType = ((CssTypeEnum) getSelectCssTypeComboBox().getSelectedItem());
					if (CssTypeEnum.LOCAL.equals(cssType)) {
						url = "file://" + url;
					}
					params.setCss(new URL(url));
				} catch (MalformedURLException e) {
					params.setCss(null);
				}
			}
		}
	}

}
