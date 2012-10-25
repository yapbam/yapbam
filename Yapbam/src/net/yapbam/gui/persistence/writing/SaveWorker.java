package net.yapbam.gui.persistence.writing;

import java.io.File;
import java.net.URI;
import java.text.MessageFormat;

import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.data.GlobalData;
import net.yapbam.data.ProgressReport;
import net.yapbam.data.xml.Serializer;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.Cancellable;
import net.yapbam.gui.persistence.PersistenceManager;
import net.yapbam.gui.persistence.PersistencePlugin;
import net.yapbam.gui.persistence.RemotePersistencePlugin;
import net.yapbam.gui.persistence.SynchronizationState;
import net.yapbam.gui.persistence.Synchronizer;
import net.yapbam.gui.persistence.writing.WriterResult.State;

class SaveWorker extends Worker<WriterResult, Void> implements ProgressReport, Cancellable {
		private GlobalData data;
		private URI uri;

		SaveWorker(GlobalData data, URI uri) {
			this.data = data;
			this.uri = uri;
		}

		@Override
		public void setMax(int length) {
			super.setPhase(getPhase(), length);
		}

		@Override
		protected WriterResult doProcessing() throws Exception {
			setPhase(MessageFormat.format(LocalizationData.get("Generic.wait.writingTo"), uri.getPath()), -1); //$NON-NLS-1$
			PersistencePlugin plugin = PersistenceManager.MANAGER.getPlugin(uri);
			boolean remote = plugin instanceof RemotePersistencePlugin;
			File previousFile = plugin.getLocalFile(uri);
			File file = remote ? ((RemotePersistencePlugin)plugin).getLocalFileForWriting(uri) : plugin.getLocalFile(uri);
			Boolean deleteOnCancelled = !file.exists();
			Serializer.write(data, file, remote, this);
			if (isCancelled()) {
				if (deleteOnCancelled) file.delete();
				return null;
			} else {
				if (!previousFile.equals(file)) { // Previous and currently saved file are not the same
					// Delete previous one
					previousFile.delete();
				}
			}
			if (plugin instanceof RemotePersistencePlugin) {
				try {
					SynchronizationState state = Synchronizer.backgroundSynchronize(uri, this);
					return new WriterResult(State.FINISHED, state, null);
				} catch (Exception e) {
					return new WriterResult(State.EXCEPTION_WHILE_SYNC, null, e);
				}
			} else {
				return new WriterResult(State.FINISHED, SynchronizationState.SYNCHRONIZED, null);
			}
		}
		
		@Override
		public void cancel() {
			super.cancel(false);
		}
		@Override
		public void reportProgress(int progress) {
			super.reportProgress(progress);
		}
		@Override
		public void setPhase(String phase, int length) {
			super.setPhase(phase, length);
		}
	}