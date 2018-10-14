package net.yapbam.gui.tools.currencyconverter;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;

import net.yapbam.currency.AbstractCurrencyConverter;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.tools.Messages;

import javax.swing.SwingConstants;

import com.fathzer.jlocal.Formatter;

public class DialogButtons extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String SOURCE_PROPERTY = "CurrencySource"; //$NON-NLS-1$

	private JLabel lblMessage;
	private JRadioButton rdbtnYahoo;
	private JRadioButton rdbtnECB;
	private JPanel panel;
	private Source source;

	/**
	 * Create the panel.
	 */
	public DialogButtons(Source source) {
		this.source = source;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.anchor = GridBagConstraints.WEST;
		gbcPanel.fill = GridBagConstraints.VERTICAL;
		gbcPanel.insets = new Insets(0, 0, 0, 5);
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 0;
		add(getPanel(), gbcPanel);
		GridBagConstraints gbcLblMessage = new GridBagConstraints();
		gbcLblMessage.insets = new Insets(0, 0, 0, 5);
		gbcLblMessage.anchor = GridBagConstraints.NORTHWEST;
		gbcLblMessage.weightx = 1.0;
		gbcLblMessage.fill = GridBagConstraints.BOTH;
		gbcLblMessage.gridx = 1;
		gbcLblMessage.gridy = 0;
		add(getLblMessage(), gbcLblMessage);

		ButtonGroup group = new ButtonGroup();
		group.add(getRdbtnECB());
		group.add(getRdbtnYahoo());
		ItemListener listener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Source old = DialogButtons.this.source;
				DialogButtons.this.source = getRdbtnECB().isSelected() ? Source.ECB : Source.YAHOO;
				if (!old.equals(DialogButtons.this.source)) {
					firePropertyChange(SOURCE_PROPERTY, old, DialogButtons.this.source);
				}
			}
		};
		getRdbtnECB().addItemListener(listener);
		getRdbtnYahoo().addItemListener(listener);
	}

	private JLabel getLblMessage() {
		if (lblMessage == null) {
			lblMessage = new JLabel();
			lblMessage.setHorizontalAlignment(SwingConstants.RIGHT);
			lblMessage.setVerticalAlignment(SwingConstants.TOP);
		}
		return lblMessage;
	}
	private JRadioButton getRdbtnYahoo() {
		if (rdbtnYahoo == null) {
			rdbtnYahoo = new JRadioButton(Messages.getString("YAHOO.name")); //$NON-NLS-1$
			rdbtnYahoo.setSelected(Source.YAHOO.equals(source));
			rdbtnYahoo.setVisible(false);
		}
		return rdbtnYahoo;
	}
	private JRadioButton getRdbtnECB() {
		if (rdbtnECB == null) {
			rdbtnECB = new JRadioButton(Messages.getString("ECB.name")); //$NON-NLS-1$
			rdbtnECB.setSelected(Source.ECB.equals(source));
		}
		return rdbtnECB;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(BorderFactory.createTitledBorder(Messages.getString("CurrencyConverterPanel.source"))); //$NON-NLS-1$
			panel.setLayout(new BorderLayout(5, 0));
			panel.add(getRdbtnECB(), BorderLayout.WEST);
			panel.add(getRdbtnYahoo(), BorderLayout.EAST);
		}
		return panel;
	}
	
	public Source getSource() {
		return source;
	}

	void setConverter(AbstractCurrencyConverter converter) {
		// Display referenceDate
		String title = Formatter.format(Messages.getString("CurrencyConverterPanel.topMessage"), new Date(converter.getTimeStamp())); //$NON-NLS-1$
		getLblMessage().setText(title);
		if (!converter.isSynchronized()) {
			getLblMessage().setIcon(IconManager.get(Name.ALERT));
		}
	}
}
