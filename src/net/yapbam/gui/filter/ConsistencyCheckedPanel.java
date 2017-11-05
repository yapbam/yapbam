package net.yapbam.gui.filter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.utilities.NullUtils;


public abstract class ConsistencyCheckedPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static String INCONSISTENCY_CAUSE_PROPERTY = "inconsistencyCause"; //$NON-NLS-1$
	private String inconsistencyCause;
	protected PropertyChangeListener consistencyChecker;

	public ConsistencyCheckedPanel() {
		this.inconsistencyCause = null;
		this.consistencyChecker = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				checkConsistency();
			}
		};
	}
	
	protected abstract String computeInconsistencyCause();
	
	public String getInconsistencyCause() {
		return inconsistencyCause;
	}

	protected void checkConsistency() {
		String old = inconsistencyCause;
		inconsistencyCause = computeInconsistencyCause();  //  @jve:decl-index=0:
		if (!NullUtils.areEquals(old, inconsistencyCause)) {
			firePropertyChange(INCONSISTENCY_CAUSE_PROPERTY, old, inconsistencyCause);
		}
	}
}
