package net.yapbam.gui.persistence;

import java.awt.Window;
import java.io.File;
import java.net.URI;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.astesana.ajlib.utilities.FileUtils;
import net.yapbam.data.GlobalData;
import net.yapbam.data.ProgressReport;
import net.yapbam.data.xml.Serializer;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.WriterResult.State;
import net.yapbam.util.Portable;

class DataWriter {
	private Window owner;
	private GlobalData data;
	private URI uri;
	private PersistencePlugin plugin;

	DataWriter (Window owner, GlobalData data, URI uri) {
		this.owner = owner;
		this.data = data;
		this.uri = uri;
		this.plugin = PersistenceManager.MANAGER.getPlugin(uri);
	}

	boolean save() {
		if (uri.getScheme().equals("file") && FileUtils.isIncluded(new File(uri), Portable.getLaunchDirectory())) { //$NON-NLS-1$
			Object[] options = {LocalizationData.get("GenericButton.cancel"),LocalizationData.get("GenericButton.continue")}; //$NON-NLS-1$ //$NON-NLS-2$
			String message = LocalizationData.get("saveDialog.dangerousLocation.message"); //$NON-NLS-1$
			int choice = JOptionPane.showOptionDialog(owner, message,	LocalizationData.get("Generic.warning"), //$NON-NLS-1$
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]); //$NON-NLS-1$
			if (choice==0) return false;
		}
		final Worker<WriterResult, Void> worker = new BackgroundSaver(data, uri);
		WorkInProgressFrame waitFrame = DataReader.buildWaitDialog(owner, worker);
		waitFrame.setVisible(true);
		try {
			WriterResult result = worker.get();
			if (result==null) return false;
			data.setURI(uri);
			data.setChanged(false);
		} catch (ExecutionException e) {
			// An exception occurred while saving to the cache
			//FIXME Test what is the exception ?
			ErrorManager.INSTANCE.display(owner, e.getCause());
			return false;
		} catch (CancellationException e) {
			// The synchronization was cancelled 
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return true;
	}
	
	private static class BackgroundSaver extends Worker<WriterResult, Void> implements ProgressReport, Cancellable {
		private GlobalData data;
		private URI uri;

		BackgroundSaver(GlobalData data, URI uri) {
			this.data = data;
			this.uri = uri;
			setPhase(MessageFormat.format(LocalizationData.get("Generic.wait.writingTo"), uri.getPath()), -1); //$NON-NLS-1$
		}

		@Override
		public void setMax(int length) {
			super.setPhase(getPhase(), length);
		}

		@Override
		protected WriterResult doProcessing() throws Exception {
			PersistencePlugin plugin = PersistenceManager.MANAGER.getPlugin(uri);
			boolean remote = plugin instanceof RemotePersistencePlugin;
			File file = remote ? ((RemotePersistencePlugin)plugin).getLocalFileForWriting(uri) : plugin.getLocalFile(uri);
			Serializer.write(data, file, remote, this);
			if (isCancelled()) return null;
			if (plugin instanceof RemotePersistencePlugin) {
//				// if cache file has a revision, mark it as not synchronized  
//				if (((RemotePersistencePlugin)plugin).getLocalBaseRevision(uri) != null) {
//					((RemotePersistencePlugin)plugin).setIsSynchronized(uri, false);
//				}
				try {
					SynchronizationState state = Synchronizer.backgroundSynchronize(uri, this);
					return new WriterResult(State.FINISHED, state);
//					RemotePersistencePlugin rPlugin = (RemotePersistencePlugin)plugin;
//					rPlugin.upload(file, uri, this);
//					rPlugin.setLocalBaseRevision(uri, rPlugin.getRemoteRevision(uri));
				} catch (Throwable e) {
					return new WriterResult(State.EXCEPTION_WHILE_SYNC, null);
				}
			}
			return null;
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
}
