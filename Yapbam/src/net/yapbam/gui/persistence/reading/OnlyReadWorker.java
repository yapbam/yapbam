package net.yapbam.gui.persistence.reading;

import java.net.URI;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.worker.Worker;

import net.yapbam.data.GlobalData;
import net.yapbam.data.ProgressReport;
import net.yapbam.data.xml.YapbamSerializer;
import net.yapbam.gui.LocalizationData;

/** A worker (see AJLib library) that reads a GlobalData URI in background. 
 */
class OnlyReadWorker extends Worker<GlobalData, Void> implements ProgressReport {
	private URI uri;
	private String password;

	/** Constructor.
	 * @param uri The source URI (null to do nothing)
	 * @param password The password to access to the source (null if no password is needed)
	 */
	public OnlyReadWorker (URI uri, String password) {
		this.uri = uri;
		this.password = password;
		setPhase(Formatter.format(LocalizationData.get("Generic.wait.readingFrom"), uri.getPath()),-1); //$NON-NLS-1$
	}
	
	@Override
	protected GlobalData doProcessing() throws Exception {
		return YapbamSerializer.read(uri, password, this);
	}

	@Override
	public void setMax(int length) {
		super.setPhase(getPhase(), length);
	}
}