package net.yapbam.gui.persistence.writing;

import java.io.File;
import java.net.URI;
import java.text.MessageFormat;

import com.fathzer.soft.jclop.Cancellable;
import com.fathzer.soft.jclop.Service;
import com.fathzer.soft.jclop.SynchronizationState;

import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.data.GlobalData;
import net.yapbam.data.ProgressReport;
import net.yapbam.data.xml.Serializer;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.CancelManager;
import net.yapbam.gui.persistence.writing.WriterResult.State;

class SaveWorker extends Worker<WriterResult, Void> implements ProgressReport, Cancellable {
		private Service service;
		private GlobalData data;
		private URI uri;
		private CancelManager cancelManager;

		SaveWorker(Service service, GlobalData data, URI uri) {
			this.service = service;
			this.data = data;
			this.uri = uri;
			this.cancelManager = new CancelManager(this);
		}

		@Override
		public void setMax(int length) {
			super.setPhase(getPhase(), length);
		}

		@Override
		protected WriterResult doProcessing() throws Exception {
			setPhase(MessageFormat.format(LocalizationData.get("Generic.wait.writingTo"), uri.getPath()), -1); //$NON-NLS-1$
			File previousFile = service.getLocalFile(uri);
			File file = service.getLocalFileForWriting(uri);
			Boolean deleteOnCancelled = !file.exists();
			Serializer.write(data, file, !service.isLocal(), this);
			if (isCancelled()) {
				if (deleteOnCancelled) file.delete();
				return null;
			} else {
				if (!previousFile.equals(file)) { // Previous and currently saved file are not the same
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
	}