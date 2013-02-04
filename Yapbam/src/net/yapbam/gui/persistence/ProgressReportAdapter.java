package net.yapbam.gui.persistence;

import com.fathzer.soft.jclop.Cancellable;

import net.yapbam.data.ProgressReport;

final class ProgressReportAdapter implements ProgressReport {
		private Cancellable cancellable;
		
		public ProgressReportAdapter (Cancellable cancellable) {
			this.cancellable = cancellable;
		}
		
		@Override
		public void setMax(int length) {
			cancellable.setMax(length);
		}

		@Override
		public void reportProgress(int progress) {
			cancellable.reportProgress(progress);
		}

		@Override
		public boolean isCancelled() {
			return cancellable.isCancelled();
		}
}