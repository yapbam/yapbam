package net.yapbam.gui.widget;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;

import com.fathzer.soft.ajlib.swing.widget.IntegerWidget;

public class PageSelector extends JPanel {
	public static final String PAGE_SELECTED_PROPERTY_NAME = "PageSelected";
	private static final long serialVersionUID = 1L;
	private IntegerWidget pageNumber;
	private JButton nextPage;
	private JButton lastPage;
	private JButton previousPage;
	private JButton firstPage;
	private int pageCount;
	private JLabel sizeLabel;

	/**
	 * Create the panel.
	 */
	public PageSelector() {
		this.pageCount = 0;
		setLayout(new GridBagLayout());
		setOpaque(false);
		
		GridBagConstraints gbcFirstPage = new GridBagConstraints();
		gbcFirstPage.insets = new Insets(0, 0, 0, 5);
		gbcFirstPage.weighty = 1.0;
		gbcFirstPage.fill = GridBagConstraints.VERTICAL;
		gbcFirstPage.gridx = 1;
		gbcFirstPage.gridy = 0;
		add(getFirstPage(), gbcFirstPage);
		
		GridBagConstraints gbcPageNumber = new GridBagConstraints();
		gbcPageNumber.gridx = 3;
		gbcPageNumber.gridy = 0;
		gbcPageNumber.fill = GridBagConstraints.VERTICAL;
		add(getPageNumber(), gbcPageNumber);
		
		sizeLabel = new JLabel("/"+pageCount);
		GridBagConstraints gbcLabel = new GridBagConstraints();
		gbcLabel.insets = new Insets(0, 0, 0, 5);
		gbcLabel.gridx = 4;
		gbcLabel.gridy = 0;
		add(sizeLabel, gbcLabel);
		
		GridBagConstraints gbcNextPage = new GridBagConstraints();
		gbcNextPage.fill = GridBagConstraints.VERTICAL;
		gbcNextPage.insets = new Insets(0, 0, 0, 5);
		gbcNextPage.gridx = 5;
		gbcNextPage.gridy = 0;
		add(getNextPage(), gbcNextPage);
		
		GridBagConstraints gbcLastPage = new GridBagConstraints();
		gbcLastPage.fill = GridBagConstraints.VERTICAL;
		gbcLastPage.gridx = 6;
		gbcLastPage.gridy = 0;
		add(getLastPage(), gbcLastPage);
		
		GridBagConstraints gbcPreviousPage = new GridBagConstraints();
		gbcPreviousPage.fill = GridBagConstraints.VERTICAL;
		gbcPreviousPage.insets = new Insets(0, 0, 0, 5);
		gbcPreviousPage.gridx = 2;
		gbcPreviousPage.gridy = 0;
		add(getPreviousPage(), gbcPreviousPage);
		
		restoreButtonStates();
	}
	
	public IntegerWidget getPageNumber() {
		if (pageNumber==null) {
			pageNumber = new IntegerWidget(pageCount==0?BigInteger.ZERO:BigInteger.ONE, BigInteger.valueOf(pageCount));
			pageNumber.setColumns(2);
			pageNumber.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getNewValue()!=null) {
						setPage(((BigInteger)evt.getNewValue()).intValue()-1);
					}
				}
			});
		}
		return pageNumber;
	}

	public JButton getNextPage() {
		if (nextPage==null) {
			nextPage = new JButton();
			nextPage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPage(getCurrentPage()+1);
				}
			});
			nextPage.setIcon(IconManager.get(Name.NEXT));
			setSelectionButtonSize(nextPage);
		}
		return nextPage;
	}

	public JButton getLastPage() {
		if (lastPage==null) {
			lastPage = new JButton();
			lastPage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPage(pageCount-1);
				}
			});
			lastPage.setIcon(IconManager.get(Name.LAST));
			setSelectionButtonSize(lastPage);
		}
		return lastPage;
	}

	public JButton getPreviousPage() {
		if (previousPage==null) {
			previousPage = new JButton();
			previousPage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPage(getCurrentPage()-1);
				}
			});
			previousPage.setIcon(IconManager.get(Name.PREVIOUS));
			setSelectionButtonSize(previousPage);
		}
		return previousPage;
	}

	public JButton getFirstPage() {
		if (firstPage==null) {
			firstPage = new JButton();
			firstPage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPage(0);
				}
			});
			firstPage.setIcon(IconManager.get(Name.FIRST));
			setSelectionButtonSize(firstPage);
		}
		return firstPage;
	}

	public void setPage(int index) {
		int old = getCurrentPage();
		if (index!=old) {
			pageNumber.setValue(index<0?null:index+1);
			restoreButtonStates();
			firePropertyChange(PAGE_SELECTED_PROPERTY_NAME, old, index);
		}
	}
	
	private void restoreButtonStates() {
		int currentPage = getCurrentPage();
		firstPage.setEnabled(currentPage>0);
		previousPage.setEnabled(currentPage>0);
		nextPage.setEnabled(currentPage<pageCount-1);
		lastPage.setEnabled(currentPage<pageCount-1);
	}
	
	private void setSelectionButtonSize(JButton button) {
		Dimension preferredSize = button.getPreferredSize();
		preferredSize.width = preferredSize.height;
		button.setPreferredSize(preferredSize);
	}
	
	public void setPageCount(int pageCount) {
		if (pageCount<0) {
			throw new IllegalArgumentException();
		}
		this.pageCount = pageCount;
		BigInteger min = pageCount==0?BigInteger.ZERO:BigInteger.ONE;
		BigInteger max = BigInteger.valueOf(pageCount);
		getPageNumber().setRange(min, max);
		sizeLabel.setText("/"+pageCount);
		if (getCurrentPage()>=pageCount) {
			setPage(pageCount-1);
		} else {
			restoreButtonStates();
		}
	}
	
	public int getCurrentPage() {
		BigInteger value = getPageNumber().getValue();
		return value==null?-1:value.intValue()-1;
	}
	
	public int getPageCount() {
		return this.pageCount;
	}
}
