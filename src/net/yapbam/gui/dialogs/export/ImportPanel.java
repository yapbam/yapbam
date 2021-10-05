package net.yapbam.gui.dialogs.export;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.JTableUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.table.NimbusPatchBooleanTableCellRenderer;
import com.fathzer.soft.ajlib.utilities.FileUtils;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

public class ImportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$

	private JScrollPane jScrollPane;
	private JTable jTable;
	private JCheckBox ignoreFirstLine;
    private JPanel ignoreHeaderPanel;
    private JSpinner numberOfHeaderLines;
	private JButton first;
	private JButton last;
	private JCheckBox addToCurrentFile;
	private JComboBox<String> accounts;
	private JPanel addToAccountPanel;
	private JLabel jLabel1;
	private SeparatorPanel columnSeparatorPanel;
    private SeparatorPanel decimalSeparatorPanel;
	private JPanel jPanel2;
	GlobalData data;
	private File file;
	private int numberOfLines;
	private int currentLine;
	private JComboBox<String> fieldsCombo;
	private String invalidityCause;
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
        gbcPanel2.gridwidth = 0;
        gbcPanel2.gridx = 0;
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
			// Disallow columns reordering
			jTable.getTableHeader().setReorderingAllowed(false);
			JTableUtils.initColumnSizes(jTable, Integer.MAX_VALUE);
			jTable.setPreferredScrollableViewportSize(getJTable().getPreferredSize());

			fieldsCombo = new JComboBox<String>();
			TableColumn importedColumns = jTable.getColumnModel().getColumn(2);
			importedColumns.setCellEditor(new DefaultCellEditor(fieldsCombo));

			jTable.setDefaultRenderer(Boolean.class, new NimbusPatchBooleanTableCellRenderer());
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
	 * This method initializes ignoreHeaderPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getIgnoreHeaderPanel() {
        if (ignoreHeaderPanel == null) {
            ignoreHeaderPanel = new JPanel(new FlowLayout());
            ignoreHeaderPanel.add(getIgnoreFirstLine());
            ignoreHeaderPanel.add(getNumberOfHeaderLinesSpinner());
        }
		return ignoreHeaderPanel;
	}
	
	private JCheckBox getIgnoreFirstLine() {
		if (ignoreFirstLine==null) {
			ignoreFirstLine = new JCheckBox();
			ignoreFirstLine.setText(LocalizationData.get("ImportDialog.ignoreFirstLine")); //$NON-NLS-1$
			ignoreFirstLine.setSelected(true);
			ignoreFirstLine.setToolTipText(LocalizationData.get("ImportDialog.ignoreFirstLine.toolTip")); //$NON-NLS-1$

            ignoreFirstLine.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
                    getNumberOfHeaderLinesSpinner().setEnabled(ignoreFirstLine.isSelected());
                    doSetLine(getNumberOfHeaderLines());
				}
			});
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
			first = new JButton(IconManager.get(Name.BOTTOM));
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
	 * This method initializes last	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLast() {
		if (last == null) {
			last = new JButton(IconManager.get(Name.TOP));
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
		boolean ok = (addToCurrentFile.isSelected() && !(Boolean)getJTable().getValueAt(0, 1)) && data.getAccountsNumber()!=0;
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
	private JComboBox<String> getAccounts() {
		if (accounts == null) {
			accounts = new JComboBox<String>();
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
			getLast().setEnabled(false);
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
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridwidth = 0;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 0.0D;
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(0, 0, 5, 5);
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.fill = GridBagConstraints.NONE;
			gridBagConstraints2.anchor = GridBagConstraints.EAST;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints5.anchor = GridBagConstraints.EAST;
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.fill = GridBagConstraints.NONE;
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.add(getLast(), gridBagConstraints5);
			jPanel2.add(getFirst(), gridBagConstraints2);
			jPanel2.add(getIgnoreHeaderPanel(), gridBagConstraints1);
			GridBagConstraints gbcLineNumber = new GridBagConstraints();
			gbcLineNumber.anchor = GridBagConstraints.EAST;
			gbcLineNumber.insets = new Insets(0, 0, 0, 5);
			gbcLineNumber.gridx = 1;
			gbcLineNumber.gridy = 0;
			jPanel2.add(getLineNumber(), gbcLineNumber);
		}
		return jPanel2;
	}

	public void setFile(File file) throws IOException {
		this.file = file;
		this.canonicalFile = FileUtils.getCanonical(file);
		this.numberOfLines = 0;
		countFileLines();
		if (numberOfLines<2) {
			getIgnoreFirstLine().setSelected(false);
			getIgnoreFirstLine().setEnabled(false);
		}
		int max = Math.max(numberOfLines-1, 1);
		int current = Math.max(Math.min(getNumberOfHeaderLines(), max), 1);
		SpinnerNumberModel model = new SpinnerNumberModel(current, 1, max, 1);
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				doSetLine(getNumberOfHeaderLines());
			}
		});
		getNumberOfHeaderLinesSpinner().setModel(model);
		max = Math.max(numberOfLines, 1);
		model = new SpinnerNumberModel(1, 1, max, 1);
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int futureLine = ((Number)getLineNumber().getValue()).intValue()-1;
				if (futureLine!=currentLine) {
					doSetLine(futureLine);
				}
			}
		});
		getLineNumber().setModel(model);
	}

	private void countFileLines() throws IOException {
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
				getLogger().warn("Error closing file "+canonicalFile, e); //$NON-NLS-1$
			}
		}
	}
	
	private Logger logger;
	private JSpinner lineNumber;
	private Logger getLogger() {
		if (this.logger==null) {
			this.logger = LoggerFactory.getLogger(getClass());
		}
		return logger;
	}
	
	public void setLine (int lineNumber) throws IOException {
		if (lineNumber>=this.numberOfLines) {
			throw new IllegalArgumentException(Formatter.format("line ({0}) must be less than {1}",lineNumber, this.numberOfLines)); //$NON-NLS-1$
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
		boolean last = this.currentLine==this.numberOfLines-1;
		getLast().setSelected(last);
		getLast().setEnabled(!last);
		getLineNumber().setValue(lineNumber+1);
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
				getLogger().warn("Error while closing file "+canonicalFile, e); //$NON-NLS-1$
			}
		}
	}
		
	public void setData(GlobalData data) {
		this.data = data;
		accounts.removeAllItems();
		for (int i = 0; i < data.getAccountsNumber(); i++) {
			accounts.addItem(data.getAccount(i).getName());
		}
		boolean empty = data.getAccountsNumber()==0;
		addToCurrentFile.setSelected(empty);
		addToCurrentFile.setEnabled(!empty);
		updateAddToAccountPanel();
	}
	
	public Importer getImporter() {
		boolean addToCurrentData = addToCurrentFile.isSelected();
		int index = accounts.getSelectedIndex();
		Account defaultAccount = index>=0?data.getAccount(index):null;
        return new Importer(file, new ImporterParameters(columnSeparatorPanel.getSeparator(),
                decimalSeparatorPanel.getSeparator(),
                DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale()),
                getNumberOfHeaderLines(),
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
		getIgnoreFirstLine().setSelected((numberOfLines>1) && (parameters.getIgnoredLeadingLines()!=0));
        if (getIgnoreFirstLine().isSelected()) {
    		int max = Math.max(numberOfLines-1, 1);
    		int current = Math.max(Math.min(parameters.getIgnoredLeadingLines(), max), 1);
            getNumberOfHeaderLinesSpinner().setValue(current);
        }
		((ImportTableModel)getJTable().getModel()).setRelations(parameters.getImportedFileColumns());
	}
	private JSpinner getLineNumber() {
		if (lineNumber == null) {
			lineNumber = new JSpinner();
			String columnName = LocalizationData.get("ImportDialog.importedFields"); //$NON-NLS-1$
			lineNumber.setToolTipText(Formatter.format(LocalizationData.get("ImportDialog.lineNumber.toolTip"), columnName)); //$NON-NLS-1$
			((JSpinner.DefaultEditor) lineNumber.getEditor()).getTextField().setColumns(4);
		}
		return lineNumber;
	}
	private JSpinner getNumberOfHeaderLinesSpinner() {
		if (numberOfHeaderLines==null) {
	        numberOfHeaderLines = new JSpinner();
			((JSpinner.DefaultEditor) numberOfHeaderLines.getEditor()).getTextField().setColumns(4);
		}
		return numberOfHeaderLines;
	}
    private int getNumberOfHeaderLines() {
        return getIgnoreFirstLine().isSelected()?((Number)getNumberOfHeaderLinesSpinner().getValue()).intValue():0;
    }
 }
