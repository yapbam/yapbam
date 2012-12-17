package net.astesana.cloud.swing;

import java.util.Collection;
import java.util.TreeSet;

import net.astesana.ajlib.swing.worker.Worker;
import net.astesana.cloud.Account;
import net.astesana.cloud.Entry;
import net.yapbam.gui.persistence.Cancellable;

final class RemoteFileListWorker extends Worker<Collection<Entry>, Void> implements Cancellable {
		private final Account account;

		RemoteFileListWorker(Account account) {
			this.account = account;
		}

		@Override
		protected Collection<Entry> doProcessing() throws Exception {
			Collection<Entry> local = account.getLocalFiles();
			TreeSet<Entry> result = new TreeSet<Entry>(local);
			Collection<Entry> remote = account.getRemoteFiles(this);
			result.addAll(remote);
			return local;
		}

		/* (non-Javadoc)
		 * @see net.astesana.ajlib.swing.worker.Worker#setPhase(java.lang.String, int)
		 */
		@Override
		public void setPhase(String phase, int phaseLength) {
			super.setPhase(phase, phaseLength);
		}

		@Override
		public void setCancelAction(Runnable action) {
		}
	}