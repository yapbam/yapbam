package net.yapbam.gui.dialogs.export;

import java.awt.GridBagLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.JCheckBox;
import javax.swing.JButton;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.HelpManager;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.JTableUtils;
import net.yapbam.util.NullUtils;
import net.yapbam.util.StringUtils;

import javax.swing.JLabel;
import java.awt.Insets;

public class ImportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$

	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JCheckBox ignoreFirstLine = null;
	private JButton first = null;
	private JButton previous = null;
	private JButton next = null;
	private JButton last = null;
	private JCheckBox addToCurrentFile = null;
	private JComboBox accounts = null;
	private JPanel addToAccountPanel = null;
	private JLabel jLabel1 = null;
	private SeparatorPanel separatorPanel = null;
	private JPanel jPanel2 = null;
	GlobalData data = null;  //  @jve:decl-index=0:
	private File file;  //  @jve:decl-index=0:
	private int numberOfLines;
	private int currentLine;
	private JComboBox fieldsCombo;
	private String invalidityCause;  //  @jve:decl-index=0:
	private JLabel jLabel = null;
	
	/**
	 * This is the default constructor
	 */
	public ImportPanel() {
		super();
		initialize();
	}
	
	/**
	 * This method initializes this
	 */
	private void initialize() {
		jLabel = new JLabel();
		jLabel.setText(LocalizationData.get("ImportDialog.help")); //$NON-NLS-1$
		jLabel.setIcon(IconManager.HELP);
		jLabel.setToolTipText(LocalizationData.get("ImportDialog.help.toolTip")); //$NON-NLS-1$
		jLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HelpManager.show(ImportPanel.this, HelpManager.IMPORT);
				super.mouseClicked(e);
			}
		});
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints22.gridx = 0;
		gridBagConstraints22.gridy = 0;
		gridBagConstraints22.weightx = 1.0D;
		gridBagConstraints22.gridwidth = 0;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 4;
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.gridy = 2;
		GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
		gridBagConstraints91.anchor = GridBagConstraints.WEST;
		gridBagConstraints91.gridy = 7;
		gridBagConstraints91.gridwidth = 0;
		gridBagConstraints91.gridx = 0;
		GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
		gridBagConstraints81.gridx = 0;
		gridBagConstraints81.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints81.gridwidth = 0;
		gridBagConstraints81.gridy = 8;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridwidth = 0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getSeparatorPanel(), gridBagConstraints22);
		this.add(getJScrollPane(), gridBagConstraints);
		this.add(getAddToCurrentFile(), gridBagConstraints91);
		this.add(getAddToAccountPanel(), gridBagConstraints81);
		this.add(getJPanel2(), gridBagConstraints11);
		updateAddToAccountPanel();
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	@SuppressWarnings("serial")
	private JTable getJTable() {
		if (jTable == null) {
			ImportTableModel model = new ImportTableModel();
			jTable = new JTable(model) {
				// Implement table header tool tips.
				@Override
				protected JTableHeader createDefaultTableHeader() {
					return new JTableHeader(columnModel) {
						@Override
						public String getToolTipText(MouseEvent e) {
							String tip = null;
							java.awt.Point p = e.getPoint();
							int index = columnModel.getColumnIndexAtX(p.x);
							int realIndex = columnModel.getColumn(index).getModelIndex();
							if (realIndex == 1) {
								tip = LocalizationData.get("ImportDialog.linkedTo.toolTip"); //$NON-NLS-1$
							} else if (realIndex == 2) {
								tip = LocalizationData.get("ImportDialog.importedFields.toolTip"); //$NON-NLS-1$
							}
							return tip;
						}
					};
				}
			};

			jTable.getTableHeader().setReorderingAllowed(false); // Disallow columns reordering
			JTableUtils.initColumnSizes(jTable, Integer.MAX_VALUE);
			jTable.setPreferredScrollableViewportSize(getJTable().getPreferredSize());

			fieldsCombo = new JComboBox();
			TableColumn importedColumns = jTable.getColumnModel().getColumn(2);
			importedColumns.setCellEditor(new DefaultCellEditor(fieldsCombo));

			jTable.setFillsViewportHeight(true);
			model.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					updateAddToAccountPanel();
					updateIsValid();
				}
			});
		}
		return jTable;
	}

	/**
	 * This method initializes ignoreFirstLine	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getIgnoreFirstLine() {
		if (ignoreFirstLine == null) {
			ignoreFirstLine = new JCheckBox();
			ignoreFirstLine.setText(LocalizationData.get("ImportDialog.ignoreFirstLine")); //$NON-NLS-1$
			ignoreFirstLine.setSelected(true);
			ignoreFirstLine.setToolTipText(LocalizationData.get("ImportDialog.ignoreFirstLine.toolTip")); //$NON-NLS-1$
		}
		return ignoreFirstLine;
	}

	/**
	 * This method initializes first	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getFirst() {
		if (first == null) {
			first = new JButton(IconManager.TOP);
			first.setToolTipText(LocalizationData.get("ImportDialog.first.toolTip")); //$NON-NLS-1$
			first.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setLine(0);
				}
			});
			first.setFocusable(false);
		}
		return first;
	}

	/**
	 * This method initializes previous	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPrevious() {
		if (previous == null) {
			previous = new JButton(IconManager.UP);
			previous.setToolTipText(LocalizationData.get("ImportDialog.previous.toolTip")); //$NON-NLS-1$
			previous.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setLine(currentLine-1);
				}
			});
			previous.setFocusable(false);
		}
		return previous;
	}

	/**
	 * This method initializes next	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNext() {
		if (next == null) {
			next = new JButton(IconManager.DOWN);
			next.setToolTipText(LocalizationData.get("ImportDialog.next.toolTip")); //$NON-NLS-1$
			next.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setLine(currentLine+1);
				}
			});
			next.setFocusable(false);
		}
		return next;
	}

	/**
	 * This method initializes last	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLast() {
		if (last == null) {
			last = new JButton(IconManager.BOTTOM);
			last.setToolTipText(LocalizationData.get("ImportDialog.last.toolTip")); //$NON-NLS-1$
			last.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setLine(numberOfLines-1);
				}
			});
			last.setFocusable(false);
		}
		return last;
	}

	/**
	 * This method initializes addToCurrentFile	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getAddToCurrentFile() {
		if (addToCurrentFile == null) {
			addToCurrentFile = new JCheckBox();
			addToCurrentFile.setText(LocalizationData.get("ImportDialog.addToCurrentFile")); //$NON-NLS-1$
			addToCurrentFile.setToolTipText(LocalizationData.get("ImportDialog.addToCurrentFile.toolTip")); //$NON-NLS-1$
			addToCurrentFile.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					updateAddToAccountPanel();
				}
			});
		}
		return addToCurrentFile;
	}

	private void updateAddToAccountPanel() {
		boolean ok = (addToCurrentFile.isSelected() && !(Boolean)getJTable().getValueAt(0, 1)) && !data.isEmpty();
		jLabel1.setEnabled(ok);
		accounts.setEnabled(ok);
		if (!ok) {
			accounts.setSelectedIndex(-1);
		} else {
			if (accounts.getSelectedIndex()<1) accounts.setSelectedIndex(0);
		}
	}

	/**
	 * This method initializes accounts	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getAccounts() {
		if (accounts == null) {
			accounts = new JComboBox();
			accounts.setToolTipText(LocalizationData.get("ImportDialog.addToAccount.toolTip")); //$NON-NLS-1$
		}
		return accounts;
	}

	/**
	 * This method initializes addToAccountPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAddToAccountPanel() {
		if (addToAccountPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText(LocalizationData.get("ImportDialog.addToAccount")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.BOTH;
			gridBagConstraints9.gridy = -1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 0;
			addToAccountPanel = new JPanel();
			addToAccountPanel.setLayout(new GridBagLayout());
			addToAccountPanel.add(getAccounts(), gridBagConstraints9);
			addToAccountPanel.add(jLabel1, gridBagConstraints6);
		}
		return addToAccountPanel;
	}

	/**
	 * This method initializes separatorPanel	
	 * 	
	 * @return net.yapbam.gui.dialogs.export.SeparatorPanel	
	 */
	private SeparatorPanel getSeparatorPanel() {
		if (separatorPanel == null) {
			separatorPanel = new SeparatorPanel();
			separatorPanel.addPropertyChangeListener(SeparatorPanel.SEPARATOR_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					setLine(currentLine);
				}
			});
		}
		return separatorPanel;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridwidth = 1;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 0.0D;
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.fill = GridBagConstraints.NONE;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.anchor = GridBagConstraints.EAST;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 3;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.EAST;
			gridBagConstraints5.gridx = 4;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.fill = GridBagConstraints.NONE;
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.add(getLast(), gridBagConstraints5);
			jPanel2.add(getNext(), gridBagConstraints4);
			jPanel2.add(getPrevious(), gridBagConstraints3);
			jPanel2.add(getFirst(), gridBagConstraints2);
			jPanel2.add(getIgnoreFirstLine(), gridBagConstraints1);
			jPanel2.add(jLabel, gridBagConstraints7);
		}
		return jPanel2;
	}

	public void setFile(File file) {
		this.file = file;
		this.numberOfLines = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				for (String line = reader.readLine(); line!=null; line=reader.readLine()) {
					numberOfLines++;
				}
				if (this.numberOfLines==0) {
					doError(LocalizationData.get("ImportDialog.error.emptyFile"));
				} else {
					setLine(0);
				}
			} catch (IOException e) {
				doError(e);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {}
			}
		} catch (FileNotFoundException e) {
			doError(e);
		}
	}
	
	public void setLine (int i) {
		if (i>=this.numberOfLines) throw new IllegalArgumentException("line ("+i+") must be less than "+this.numberOfLines);
		this.currentLine = i;
		boolean first = this.currentLine==0;
		getFirst().setSelected(first);
		getFirst().setEnabled(!first);
		getPrevious().setEnabled(!first);
		boolean last = this.currentLine==this.numberOfLines-1;
		getLast().setSelected(last);
		getLast().setEnabled(!last);
		getNext().setEnabled(!last);
		String[] fields = getFields();
		setFieldsCombo(fields);
		ImportTableModel model = (ImportTableModel) getJTable().getModel();
		model.setFields(fields);
	}
	
	private void setFieldsCombo(String[] fields) {
		fieldsCombo.removeAllItems();
		for (int i = 0; i < fields.length; i++) {
			fieldsCombo.addItem(fields[i]);
		}
	}
	
	private String[] getFields() {
		String[] result = new String[0];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				for (int i = 0; i < this.currentLine; i++) {
					reader.readLine();
				}
				String line = reader.readLine();
				result = StringUtils.split(line, separatorPanel.getSeparator());
			} catch (IOException e) {
				doError (e);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {}
			}
		} catch (FileNotFoundException e) {
			doError(e);
		}
		return result;
	}
		
	private void doError(Throwable e) {
		doError(MessageFormat.format(LocalizationData.get("ImportDialog.error.exception"), e.getMessage()));
	}
	
	private void doError(String message) {
		throw new BadImportFileException(message);
	}

	public void setData(GlobalData data) {
		this.data = data;
		accounts.removeAllItems();
		for (int i = 0; i < data.getAccountsNumber(); i++) {
			accounts.addItem(data.getAccount(i).getName());
		}
		addToCurrentFile.setSelected(data.isEmpty());
		addToCurrentFile.setEnabled(!data.isEmpty());
		updateAddToAccountPanel();
	}
	
	public Importer getImporter() {
		boolean addToCurrentData = addToCurrentFile.isSelected();
		int index = accounts.getSelectedIndex();
		Account defaultAccount = index>=0?data.getAccount(index):null;
		return new Importer(file, new ImporterParameters(separatorPanel.getSeparator(), ignoreFirstLine.isSelected()?1:0,
				((ImportTableModel)getJTable().getModel()).getRelations()),
				addToCurrentData?data:null, defaultAccount);
	}

	public boolean getAddToCurrentData() {
		return addToCurrentFile.isSelected();
	}
	
	public String getInvalidityCause() {
		return invalidityCause;
	}
	
	private void updateIsValid() {
		String old = invalidityCause;
		invalidityCause = null;
		// Date, amount are mandatory
		int[] relations = ((ImportTableModel)getJTable().getModel()).getRelations();
		if (relations[ExportTableModel.AMOUNT_INDEX]<0) {
			invalidityCause = LocalizationData.get("ImportDialog.noAmountSelected"); //$NON-NLS-1$
		} else if (relations[ExportTableModel.DATE_INDEX]<0) {
			invalidityCause = LocalizationData.get("ImportDialog.noDateSelected"); //$NON-NLS-1$
		}
		if (!NullUtils.areEquals(invalidityCause, old)) {
			this.firePropertyChange(INVALIDITY_CAUSE, old, invalidityCause);
		}
	}

	public void setParameters(ImporterParameters parameters) {
		separatorPanel.setSeparator(parameters.getSeparator());
		ignoreFirstLine.setSelected(parameters.getIgnoredLeadingLines()!=0);
		((ImportTableModel)getJTable().getModel()).setRelations(parameters.getImportedFileColumns());
	}
}
