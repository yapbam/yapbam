package net.yapbam.gui.persistence.writing;

import java.io.File;
import java.net.URI;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.worker.Worker;
import com.fathzer.soft.jclop.Cancellable;
import com.fathzer.soft.jclop.Service;
import com.fathzer.soft.jclop.SynchronizationState;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.CancelManager;
import net.yapbam.gui.persistence.DataWrapper;
import net.yapbam.gui.persistence.PersistenceManager;
import net.yapbam.gui.persistence.writing.WriterResult.State;

class SaveWorker extends Worker<WriterResult, Void> implements Cancellable {
		private PersistenceManager manager;
		private DataWrapper<?> data;
		private URI uri;
		private CancelManager cancelManager;

		SaveWorker(PersistenceManager manager, DataWrapper<?> data, URI uri) {
			this.manager = manager;
			this.data = data;
			this.uri = uri;
			this.cancelManager = new CancelManager(this);
		}

		@Override
		protected WriterResult doProcessing() throws Exception {
			setPhase(Formatter.format(LocalizationData.get("Generic.wait.writingTo"), uri.getPath()), -1); //$NON-NLS-1$
			Service service = manager.getAdapter(uri).getService();
			File previousFile = service.getLocalFile(uri);
			File file = service.getLocalFileForWriting(uri);
			Boolean deleteOnCancelled = !file.exists();
			data.serialize(file, service, this);
			if (isCancelled()) {
				if (deleteOnCancelled) {
					file.delete();
				}
				return null;
			} else {
				if (!previousFile.equals(file)) {
					// Previous and currently saved file are not the same
					// Delete previous one
					previousFile.delete();
				}
			}
			if (service.isLocal()) {
				return new WriterResult(State.FINISHED, SynchronizationState.SYNCHRONIZED, null);
			} else {
				try {
					setPhase(LocalizationData.get("synchronization.synchronizing"), -1); //$NON-NLS-1$
					SynchronizationState state = service.synchronize(uri, this, LocalizationData.getLocale());
					return new WriterResult(State.FINISHED, state, null);
				} catch (Exception e) {
					return new WriterResult(State.EXCEPTION_WHILE_SYNC, null, e);
				}
			}
		}
		
		@Override
		public void reportProgress(int progress) {
			super.reportProgress(progress);
		}
		@Override
		public void setPhase(String phase, int length) {
			super.setPhase(phase, length);
		}

		@Override
		public void setCancelAction(Runnable action) {
			this.cancelManager.setAction(action);
		}

		@Override
		public void setMax(int max) {
			super.setPhaseLength(max);
		}
	}