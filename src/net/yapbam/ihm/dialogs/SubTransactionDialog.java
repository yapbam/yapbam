package net.yapbam.ihm.dialogs;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.data.SubTransaction;

public class SubTransactionDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	
	private static final boolean DEBUG = false;
	SubTransactionPanel panel;
	
	public SubTransactionDialog(Window owner, GlobalData data, SubTransaction subtransaction) {
		super(owner, "Sous-opération", data); //LOCAL
		if (subtransaction!=null) {
			panel.setDescription(subtransaction.getDescription());
			panel.setAmount(subtransaction.getAmount());
			panel.setCategory(subtransaction.getCategory());
		}
	}
	
	protected JPanel createCenterPane(Object data) {
		this.panel = new SubTransactionPanel();
		this.panel.setData((GlobalData) data);
		this.panel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
        if (DEBUG) this.panel.setBorder(BorderFactory.createTitledBorder("main"));        
        return this.panel;
    }
	
	@Override
	protected Object buildResult() {
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
			return "Ce bouton est désactivé car le libellé de la sous-opération est vide";
		} else if (panel.getAmount()==null) {
			return "Ce bouton est désactivé car le montant est vide";
		} else if (panel.getAmount()==0) {
			return "Ce bouton est désactivé car le montant est nul";
		}
		return null;
	}
}