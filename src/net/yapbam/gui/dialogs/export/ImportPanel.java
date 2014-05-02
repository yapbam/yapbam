package net.yapbam.gui.dialogs.export;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.HelpManager;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.JTableUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fathzer.soft.ajlib.utilities.FileUtils;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

public class ImportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$

	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JCheckBox ignoreFirstLine = null;
    private JPanel ignoreHeaderPanel = null;
    private JSpinner numberOfHeaderLines = null;
	private JButton first = null;
	private JButton previous = null;
	private JButton next = null;
	private JButton last = null;
	private JCheckBox addToCurrentFile = null;
	private JComboBox accounts = null;
	private JPanel addToAccountPanel = null;
	private JLabel jLabel1 = null;
	private SeparatorPanel columnSeparatorPanel = null;
    private SeparatorPanel decimalSeparatorPanel = null;
	private JPanel jPanel2 = null;
	GlobalData data = null;  //  @jve:decl-index=0:
	private File file;  //  @jve:decl-index=0:
	private int numberOfLines;
	private int currentLine;
	private JComboBox fieldsCombo;
	private String invalidityCause;  //  @jve:decl-index=0:
	private JLabel jLabel = null;
	private File canonicalFile;
	
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
		jLabel.setIcon(IconManager.get(Name.HELP));
		jLabel.setToolTipText(LocalizationData.get("ImportDialog.help.toolTip")); //$NON-NLS-1$
		jLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HelpManager.show(ImportPanel.this, HelpManager.IMPORT);
				super.mouseClicked(e);
			}
		});

		GridBagConstraints gbcColumnSeparatorPanel = new GridBagConstraints();
		gbcColumnSeparatorPanel.gridx = 0;
        gbcColumnSeparatorPanel.gridy = 0;
        gbcColumnSeparatorPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcColumnSeparatorPanel.weightx = 1.0D;
		gbcColumnSeparatorPanel.gridwidth = 0;

        GridBagConstraints gbcScrollPane = new GridBagConstraints();
        gbcScrollPane.gridx = 0;
        gbcScrollPane.gridy = 1;
        gbcScrollPane.fill = GridBagConstraints.BOTH;
        gbcScrollPane.weightx = 1.0;
        gbcScrollPane.weighty = 1.0;
        gbcScrollPane.gridwidth = 0;

        GridBagConstraints gbcPanel2 = new GridBagConstraints();
        gbcPanel2.gridx = 4;
        gbcPanel2.gridy = 2;
        gbcPanel2.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints gbcDecimalSeparatorPanel = (GridBagConstraints) gbcColumnSeparatorPanel.clone();
        gbcDecimalSeparatorPanel.gridy = 3;

        GridBagConstraints gbcAddToCurrentFile = new GridBagConstraints();
        gbcAddToCurrentFile.gridx = 0;
        gbcAddToCurrentFile.gridy = 4;
        gbcAddToCurrentFile.gridwidth = 0;
        gbcAddToCurrentFile.anchor = GridBagConstraints.WEST;

        GridBagConstraints gbcAddToAccountPanel = new GridBagConstraints();
        gbcAddToAccountPanel.gridx = 0;
        gbcAddToAccountPanel.gridy = 5;
        gbcAddToAccountPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcAddToAccountPanel.gridwidth = 0;

		this.setLayout(new GridBagLayout());
		this.add(getColumnSeparatorPanel(), gbcColumnSeparatorPanel);
        this.add(getDecimalSeparatorPanel(), gbcDecimalSeparatorPanel);
		this.add(getJScrollPane(), gbcScrollPane);
		this.add(getAddToCurrentFile(), gbcAddToCurrentFile);
		this.add(getAddToAccountPanel(), gbcAddToAccountPanel);
		this.add(getJPanel2(), gbcPanel2);
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
			jTable = new com.fathzer.soft.ajlib.swing.table.JTable(model) {
				// Implement table header tool tips.
				@Override
				protected JTableHeader createDefaultTableHeader() {
					return new JTableHeader(columnModel) {
						@Override
						public String getToolTipText(MouseEvent e) {
							String tip;
							java.awt.Point p = e.getPoint();
							int index = columnModel.getColumnIndexAtX(p.x);
							int realIndex = columnModel.getColumn(index).getModelIndex();
							if (realIndex == 1) {
								tip = LocalizationData.get("ImportDialog.linkedTo.toolTip"); //$NON-NLS-1$
							} else if (realIndex == 2) {
								tip = LocalizationData.get("ImportDialog.importedFields.toolTip"); //$NON-NLS-1$
							} else {
								// This method should not return null (see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4805978)
								// I don't now what could be a good tip for the first column ... column name will do the job. 
								tip = jTable.getColumnName(0);
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
	 * This method initializes ignoreFirstLine, numberOfHeaderLines and ignoreHeaderPanel
	 * 	
	 * @return javax.swing.JPanel
	 */
	private JPanel getIgnoreFirstLine() {
        if (ignoreHeaderPanel == null) {
			ignoreFirstLine = new JCheckBox();
			ignoreFirstLine.setText(LocalizationData.get("ImportDialog.ignoreFirstLine")); //$NON-NLS-1$
			ignoreFirstLine.setSelected(true);
			ignoreFirstLine.setToolTipText(LocalizationData.get("ImportDialog.ignoreFirstLine.toolTip")); //$NON-NLS-1$

            final SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 999, 1);
            numberOfHeaderLines = new JSpinner(model);
            ((JSpinner.DefaultEditor)numberOfHeaderLines.getEditor()).getTextField().setColumns(3);
            model.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    doSetLine(getNumberOfHeaderLines() - 1);
                }
            });

            ignoreFirstLine.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    numberOfHeaderLines.setEnabled(ignoreFirstLine.isSelected());
                }
            });

            ignoreHeaderPanel = new JPanel(new FlowLayout());
            ignoreHeaderPanel.add(ignoreFirstLine);
            ignoreHeaderPanel.add(numberOfHeaderLines);
        }
		return ignoreHeaderPanel;
	}

	/**
	 * This method initializes first	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getFirst() {
		if (first == null) {
			first = new JButton(IconManager.get(Name.TOP));
			first.setToolTipText(LocalizationData.get("ImportDialog.first.toolTip")); //$NON-NLS-1$
			first.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doSetLine(0);
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
			previous = new JButton(IconManager.get(Name.UP));
			previous.setToolTipText(LocalizationData.get("ImportDialog.previous.toolTip")); //$NON-NLS-1$
			previous.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doSetLine(currentLine-1);
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
			next = new JButton(IconManager.get(Name.DOWN));
			next.setToolTipText(LocalizationData.get("ImportDialog.next.toolTip")); //$NON-NLS-1$
			next.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doSetLine(currentLine+1);
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
			last = new JButton(IconManager.get(Name.BOTTOM));
			last.setToolTipText(LocalizationData.get("ImportDialog.last.toolTip")); //$NON-NLS-1$
			last.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doSetLine(numberOfLines-1);
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
		} else if (accounts.getSelectedIndex()<1) {
			accounts.setSelectedIndex(0);
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
	 * This method initializes columnSeparatorPanel
	 * 	
	 * @return net.yapbam.gui.dialogs.export.SeparatorPanel	
	 */
	private SeparatorPanel getColumnSeparatorPanel() {
		if (columnSeparatorPanel == null) {
			columnSeparatorPanel = SeparatorPanel.createColumnSeparatorPanel();
			columnSeparatorPanel.addPropertyChangeListener(SeparatorPanel.SEPARATOR_PROPERTY, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    setSeparator(columnSeparatorPanel.getSeparator());
                }
            });
		}
		return columnSeparatorPanel;
	}

    /**
     * This method initializes decimalSeparatorPanel
     *
     * @return net.yapbam.gui.dialogs.export.SeparatorPanel
     */
    private SeparatorPanel getDecimalSeparatorPanel() {
        if (decimalSeparatorPanel == null) {
            decimalSeparatorPanel = SeparatorPanel.createDecimalSeparatorPanel();
        }
        return decimalSeparatorPanel;
    }

	private void setSeparator(char separator) {
		if ((separator==CSVParser.DEFAULT_QUOTE_CHARACTER) || (separator==CSVParser.DEFAULT_ESCAPE_CHARACTER)) {
			getColumnSeparatorPanel().setError(LocalizationData.get("ImportDialog.badSeparator")); //$NON-NLS-1$
			getFirst().setEnabled(false);
			getPrevious().setEnabled(false);
			getLast().setEnabled(false);
			getNext().setEnabled(false);
			String[] fields = new String[0];
			setFieldsCombo(fields);
			ImportTableModel model = (ImportTableModel) getJTable().getModel();
			model.setFields(fields);
		} else {
			getColumnSeparatorPanel().setError(null);
			doSetLine(currentLine);
		}
		updateIsValid();
	}
	
	private void doSetLine(int line) {
		try {
			setLine(line);
		} catch (IOException e) {
			ImportDialog.doError(this, e);
		}
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

	public void setFile(File file) throws IOException {
		this.file = file;
		this.canonicalFile = FileUtils.getCanonical(file);
		this.numberOfLines = 0;
		countFileLines();
	}

	private void countFileLines() throws FileNotFoundException, IOException, EmptyImportFileException {
		CSVReader reader = new CSVReader(new FileReader(canonicalFile), columnSeparatorPanel.getSeparator(), '"');
		try {
			for (String[] fields = reader.readNext(); fields!=null; fields=reader.readNext()) {
				numberOfLines++;
			}
			if (this.numberOfLines==0) {
				throw new EmptyImportFileException();
			}
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				getLogger().warn("Error closing file "+canonicalFile, e);
			}
		}
	}
	
	private Logger logger;
	private Logger getLogger() {
		if (this.logger==null) {
			this.logger = LoggerFactory.getLogger(getClass());
		}
		return logger;
	}
	
	public void setLine (int lineNumber) throws IOException {
		if (lineNumber>=this.numberOfLines) {
			throw new IllegalArgumentException("line ("+lineNumber+") must be less than "+this.numberOfLines);
		}
		String[] fields = getFields(lineNumber);
		while (fields==null) {
			// No such line in the file (it was probably edited by the user)
			// Count the line numbers again
			countFileLines();
			// Select the last line of the file
			lineNumber = this.numberOfLines - 1;
			fields = getFields(lineNumber);
		}
		this.currentLine = lineNumber;
		boolean first = this.currentLine==0;
		getFirst().setSelected(first);
		getFirst().setEnabled(!first);
		getPrevious().setEnabled(!first);
		boolean last = this.currentLine==this.numberOfLines-1;
		getLast().setSelected(last);
		getLast().setEnabled(!last);
		getNext().setEnabled(!last);
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
	
	private String[] getFields(int lineNumber) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(canonicalFile), columnSeparatorPanel.getSeparator(), CSVParser.DEFAULT_QUOTE_CHARACTER, lineNumber);
		try {
			return reader.readNext();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				getLogger().warn("Error while closing file "+canonicalFile, e);
			}
		}
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
        return new Importer(file, new ImporterParameters(columnSeparatorPanel.getSeparator(),
                decimalSeparatorPanel.getSeparator(),
                getNumberOfHeaderLines(),
				((ImportTableModel)getJTable().getModel()).getRelations()),
				addToCurrentData?data:null, defaultAccount);
	}

    private int getNumberOfHeaderLines() {
        return ignoreFirstLine.isSelected()?((Number)numberOfHeaderLines.getValue()).intValue():0;
    }

    public boolean getAddToCurrentData() {
		return addToCurrentFile.isSelected();
	}
	
	public String getInvalidityCause() {
		return invalidityCause;
	}
	
	private void updateIsValid() {
		String old = invalidityCause;
		invalidityCause = getColumnSeparatorPanel().getError();
		if (invalidityCause==null) {
			// Date, amount are mandatory
			int[] relations = ((ImportTableModel)getJTable().getModel()).getRelations();
			if (relations[ExportTableModel.AMOUNT_INDEX]<0) {
				invalidityCause = LocalizationData.get("ImportDialog.noAmountSelected"); //$NON-NLS-1$
			} else if (relations[ExportTableModel.DATE_INDEX]<0) {
				invalidityCause = LocalizationData.get("ImportDialog.noDateSelected"); //$NON-NLS-1$
			}
		}
		if (!NullUtils.areEquals(invalidityCause, old)) {
			this.firePropertyChange(INVALIDITY_CAUSE, old, invalidityCause);
		}
	}

	public void setParameters(ImporterParameters parameters) {
		columnSeparatorPanel.setSeparator(parameters.getColumnSeparator());
        decimalSeparatorPanel.setSeparator(parameters.getDecimalSeparator());
		ignoreFirstLine.setSelected(parameters.getIgnoredLeadingLines()!=0);
        if (ignoreFirstLine.isSelected()) {
            numberOfHeaderLines.setValue(parameters.getIgnoredLeadingLines());
        }
		((ImportTableModel)getJTable().getModel()).setRelations(parameters.getImportedFileColumns());
	}
}
