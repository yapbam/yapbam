package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.data.SubTransaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AbstractDialog;

public class SubTransactionDialog extends AbstractDialog<GlobalData, SubTransaction> {
	private static final long serialVersionUID = 1L;
	
	private static final boolean DEBUG = false;
	private SubTransactionPanel panel;
	
	public SubTransactionDialog(Window owner, GlobalData data, SubTransaction subtransaction) {
		super(owner, subtransaction==null?LocalizationData.get("SubTransactionDialog.title.new"):LocalizationData.get("SubTransactionDialog.title.edit"), data); //$NON-NLS-1$
		if (subtransaction!=null) {
			panel.setDescription(subtransaction.getDescription());
			panel.setAmount(subtransaction.getAmount());
			panel.setCategory(subtransaction.getCategory());
		}
	}
	
	@Override
	protected JPanel createCenterPane() {
		this.panel = new SubTransactionPanel();
		this.panel.setData(data);
		this.panel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
        if (DEBUG) this.panel.setBorder(BorderFactory.createTitledBorder("main")); //$NON-NLS-1$
        return this.panel;
    }
	
	@Override
	protected SubTransaction buildResult() {
		Double amount = panel.getAmount();
		String description = panel.getDescription();
		Category category = panel.getCategory();
		return new SubTransaction(amount, description, category);
	}
	
	public SubTransaction getSubTransaction() {
		return (SubTransaction) super.getResult();
	}

	@Override
	protected String getOkDisabledCause() {
		if (panel.getDescription().length()==0) {
			return LocalizationData.get("SubTransactionDialog.empty.desciption"); //$NON-NLS-1$
		} else if (panel.getAmount()==null) {
			return LocalizationData.get("SubTransactionDialog.empty.amount"); //$NON-NLS-1$
		} else if (panel.getAmount()==0) {
			return LocalizationData.get("SubTransactionDialog.null.amount"); //$NON-NLS-1$
		}
		return null;
	}

	public void setPredefined(String[] predefined, int[] groupSizes) {
		this.panel.setPredefined(predefined, groupSizes);
	}
	
	public void setPredefinedUpdater (SubTransactionPanel.PredefinedDescriptionUpdater updater) {
		this.panel.setUpdater(updater);
	}
}