package net.yapbam.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.yapbam.data.*;
import net.yapbam.data.event.*;
import net.yapbam.ihm.graphics.balancehistory.BalanceHistoryPane;
import net.yapbam.ihm.transactiontable.TransactionTable;

public class MainFrame extends JFrame implements DataListener {
	//TODO implements undo support (see package undo in JustSomeTests project)
	//TODO implements copy/paste support ?
	//TODO CheckBook support
	//TODO periodic transaction support
	
	private static final long serialVersionUID = 1L;
    
    private GlobalData data;
	private AccountFilteredData accountFilter;
	private FilteredData filteredData;

	private MainMenuBar mainMenu;
	private JTabbedPane mainPane;
	private CheckModePanel checkModePane;
	private JTable transactionTable;
	private BalanceReportField currentBalance;
	private BalanceReportField finalBalance;
	private BalanceReportField checkedBalance;
	private BalanceHistoryPane balanceHistoryPane;
	
	public static void main(String[] args) {
//		Locale.setDefault(new Locale(Locale.ENGLISH.getLanguage(), Locale.US.getCountry()));//TODO Remove
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		Locale locale = Locale.getDefault();
		ResourceBundle res = ResourceBundle.getBundle("Resources",locale); //$NON-NLS-1$
		LocalizationData.setBundle(res);
	    //Schedule a job for the event-dispatching thread:
	    //creating and showing this application's GUI.
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            new MainFrame();
	        }
	    });
	}

	/** Create the GUI and show it.  For thread safety, this method should be invoked from the
	 * event-dispatching thread.
	 */
	private MainFrame() {
	    //Create and set up the window.
		super();
		this.setMinimumSize(new Dimension(800,300));
		
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    this.addWindowListener(new MainFrameListener());
	
	    this.data = new GlobalData();
	    this.accountFilter = new AccountFilteredData(data);
	    this.filteredData = new FilteredData(data);

	    mainMenu = new MainMenuBar(this);
	    setContentPane(this.createContentPane());
		setJMenuBar(mainMenu);
	    
	    this.data.addListener(this);
	    this.data.addListener(mainMenu);
	    
	    // Restore initial state (last opened file and window position)
	    YapbamState.INSTANCE.restoreMainFramePosition(this);
	    YapbamState.INSTANCE.restoreGlobalData(this);
	    YapbamState.INSTANCE.restoreTransactionTableColumns(this);
	
	    //Display the window.
	    setVisible(true);
	}

	private Container createContentPane() {
        mainPane = new JTabbedPane(JTabbedPane.TOP);
		mainPane.addChangeListener(mainMenu);
        JPanel transactionPane = new JPanel(new BorderLayout());
        transactionPane.setOpaque(true);
        
        String noText=""; //$NON-NLS-1$
        JPanel topPanel = new JPanel(new GridBagLayout());
        JButton newTransactionButton = new JButton(this.mainMenu.newTransactionAction);
        newTransactionButton.setText(noText);
        Dimension dimension = newTransactionButton.getPreferredSize();
        dimension.width = dimension.height;
        newTransactionButton.setPreferredSize(dimension);
        final JButton editTransactionButton = new JButton(this.mainMenu.editTransactionAction);
        editTransactionButton.setText(noText);
        editTransactionButton.setPreferredSize(dimension);
        final JButton duplicateTransactionButton = new JButton(this.mainMenu.duplicateTransactionAction);
        duplicateTransactionButton.setText(noText);
        duplicateTransactionButton.setPreferredSize(dimension);
        final JButton deleteTransactionButton = new JButton(this.mainMenu.deleteTransactionAction);
        deleteTransactionButton.setText(noText);
        deleteTransactionButton.setPreferredSize(dimension);
        GridBagConstraints c = new GridBagConstraints();
        topPanel.add(newTransactionButton,c);
        c.gridx = 1;
        topPanel.add(editTransactionButton,c);
        c.gridx = 2;
        topPanel.add(duplicateTransactionButton,c);
        c.gridx = 3;
        topPanel.add(deleteTransactionButton,c);
        
        transactionTable = new TransactionTable(getFilteredData());
        checkModePane = new CheckModePanel(this);       
        c.gridx = 4; c.anchor=GridBagConstraints.EAST; c.weightx=1;
        topPanel.add(checkModePane,c);
        
        transactionPane.add(topPanel, BorderLayout.NORTH);
        
        transactionTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                  Point p = e.getPoint();
                  int row = transactionTable.rowAtPoint(p);
                  if (row >= 0) {
                	  if (checkModePane.isSelected()) {
                		  checkModePane.check();
                	  } else {
                		  mainMenu.editTransactionAction.actionPerformed(new ActionEvent(transactionTable, 0, null));
                	  }
                  }
                }
              }
		});
        JScrollPane scrollPane = new JScrollPane(transactionTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        transactionPane.add(scrollPane, BorderLayout.CENTER);
		ListSelectionModel selModel = transactionTable.getSelectionModel();
		selModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel m = (javax.swing.ListSelectionModel) e.getSource();
				if (!e.getValueIsAdjusting()) {
					boolean ok = m.getMinSelectionIndex()>=0;
					mainMenu.duplicateTransactionAction.setEnabled(ok);
					mainMenu.editTransactionAction.setEnabled(ok);
					mainMenu.deleteTransactionAction.setEnabled(ok);
				}
			}
		});

		JPanel bottomPane = new JPanel(new GridLayout(1,3));
        currentBalance = new BalanceReportField(LocalizationData.get("MainFrame.CurrentBalance")); //$NON-NLS-1$
        finalBalance = new BalanceReportField(LocalizationData.get("MainFrame.FinalBalance")); //$NON-NLS-1$
        checkedBalance = new BalanceReportField(LocalizationData.get("MainFrame.CheckedBalance")); //$NON-NLS-1$
        bottomPane.add(currentBalance);
        bottomPane.add(finalBalance);
        bottomPane.add(checkedBalance);
        transactionPane.add(bottomPane, BorderLayout.SOUTH);

		mainPane.add(LocalizationData.get("MainFrame.Transactions"), transactionPane); //$NON-NLS-1$
		
		balanceHistoryPane = new BalanceHistoryPane(accountFilter.getBalanceHistory());
		accountFilter.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				balanceHistoryPane.setBalanceHistory(accountFilter.getBalanceHistory());
				updateBalances();
			}
		});
		mainPane.add(LocalizationData.get("MainFrame.BalanceHistory"), balanceHistoryPane); //$NON-NLS-1$

        return mainPane;
    }

	public GlobalData getData() {
		return data;
	}
	
	public AccountFilteredData getAccountFilter() {
		return this.accountFilter;
	}
	
	public FilteredData getFilteredData() {
		return this.filteredData;
	}

	public JTable getTransactionTable() {
		return transactionTable;
	}

	public Transaction getSelectedTransaction() {
		ListSelectionModel listSelectionModel = transactionTable.getSelectionModel();
		int index = listSelectionModel.getMinSelectionIndex();
		return getFilteredData().getTransaction(index);
	}

	private void updateBalances() {
		currentBalance.setValue(accountFilter.getCurrentBalance());
		finalBalance.setValue(accountFilter.getFinalBalance());
	    checkedBalance.setValue(accountFilter.getCheckedBalance());
	}

	public void processEvent(DataEvent event) {
		if ((event instanceof FileChangedEvent) || (event instanceof EverythingChangedEvent)) {
			newDataOccured();
		}
	}

	private void newDataOccured() {
		String title = LocalizationData.get("ApplicationName"); //$NON-NLS-1$
		File file = data.getPath();
		if (file!=null) title = title + " - " + file; //$NON-NLS-1$
		this.setTitle(title);
		this.updateBalances();
	}
	
	boolean isTransactionTableVisible() {
		return this.mainPane.getSelectedIndex()==0;
	}
}
