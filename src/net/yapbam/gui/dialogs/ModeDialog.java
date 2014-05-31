package net.yapbam.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;
import net.yapbam.gui.widget.AutoSelectFocusListener;

public class ModeDialog extends AbstractDialog<Account, Mode> {
	private static final long serialVersionUID = 1L;
	
	private static final boolean DEBUG = false;
	
	private TextWidget name;
	private ModePanel leftPane;
	private ModePanel rightPane;
	private Mode original;
		
	public ModeDialog(Window owner, Account account) {
		super(owner, LocalizationData.get("ModeDialog.title.new"), account); //$NON-NLS-1$
		original = null;
	}
	
	@Override
	protected JPanel createCenterPane() {
		// Create the content pane.
		JPanel centerPane = new JPanel(new BorderLayout());
		centerPane.add(
						new JLabel(Formatter.format(LocalizationData.get("ModeDialog.account"), data.getName())), BorderLayout.NORTH); //$NON-NLS-1$
		JPanel main = new JPanel(new GridBagLayout());
		if (DEBUG) {
			main.setBorder(BorderFactory.createTitledBorder("main")); //$NON-NLS-1$
		}
		centerPane.add(main, BorderLayout.CENTER);

		JPanel idPanel = new JPanel(new GridBagLayout());
		if (DEBUG) {
			idPanel.setBorder(BorderFactory.createTitledBorder("idPanel")); //$NON-NLS-1$
		}
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		idPanel.add(new JLabel(LocalizationData.get("ModeDialog.name")), c); //$NON-NLS-1$
		c.gridx = 1;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		PropertyChangeListener listener = new AutoUpdateOkButtonPropertyListener(this);
		name = new TextWidget(10);
		name.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, listener);
		name.addFocusListener(AutoSelectFocusListener.INSTANCE);
		idPanel.add(name, c);

		leftPane = new ModePanel(LocalizationData.get("ModeDialog.forDebts"), true); //$NON-NLS-1$
		rightPane = new ModePanel(LocalizationData.get("ModeDialog.forReceipts"), false); //$NON-NLS-1$
		leftPane.addPropertyChangeListener(ModePanel.IS_SELECTED_PROPERTY, listener);
		rightPane.addPropertyChangeListener(ModePanel.IS_SELECTED_PROPERTY, listener);
		leftPane.addPropertyChangeListener(ModePanel.IS_VALID_PROPERTY, listener);
		rightPane.addPropertyChangeListener(ModePanel.IS_VALID_PROPERTY, listener);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		main.add(idPanel, c);
		c = new GridBagConstraints();
		c.gridy = 1;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = new Insets(0, 0, 0, 5);
		main.add(leftPane, c);
		c.gridx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		main.add(rightPane, c);

		return centerPane;
	}

	@Override
	protected Mode buildResult() {
		return new Mode(name.getText(),rightPane.getValueDateComputer(),leftPane.getValueDateComputer(),leftPane.isCheckBookSelected());
	}
	
	/** Opens the dialog, and add the newly created mode to the data
	 * @param data The account where to add the new mode
	 * @param owner The frame upon which the dialog will be displayed
	 * @return Mode the mode which was added, null if the operation was canceled
	 */
	public static Mode open(GlobalData data, Account account, Window owner) {
		ModeDialog dialog = new ModeDialog(owner, account);
		dialog.setVisible(true);
		Mode newMode = dialog.getResult();
		if (newMode!=null) {
			data.add(account, newMode);
		}
		return newMode;
	}

	@Override
	protected String getOkDisabledCause() {
		String name = this.name.getText().trim();
		Account account = (Account) this.data;
		if (name.length()==0) {
			return LocalizationData.get("ModeDialog.bad.emptyName"); //$NON-NLS-1$
		} else if ((account.getMode(name)!=null) && (! ((original!=null) && name.equals(original.getName())))) {
			return Formatter.format(LocalizationData.get("ModeDialog.bad.duplicateMode"),name, account.getName()); //$NON-NLS-1$
		} else if (!(leftPane.isSelected()||rightPane.isSelected())) {
			return LocalizationData.get("ModeDialog.bad.neverAvalaible"); //$NON-NLS-1$
		} else if (leftPane.isSelected() && !leftPane.hasValidContent()) {
			return LocalizationData.get("ModeDialog.bad.debt"); //$NON-NLS-1$
		} else if (rightPane.isSelected() && !rightPane.hasValidContent()) {
			return LocalizationData.get("ModeDialog.bad.receipt"); //$NON-NLS-1$
		}
		return null;
	}

	public void setContent(Mode mode) {
		setTitle(LocalizationData.get("ModeDialog.title.edit")); //$NON-NLS-1$
		original = mode;
		name.setText(mode.getName());
		leftPane.setContent(mode.getExpenseVdc());
		leftPane.setCheckBookSelected(mode.isUseCheckBook());
		rightPane.setContent(mode.getReceiptVdc());
		this.updateOkButtonEnabled();
	}
}