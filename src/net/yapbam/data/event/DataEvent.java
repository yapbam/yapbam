package net.yapbam.data.event;

public abstract class DataEvent {
	private Object source;
	
	protected DataEvent(Object source) {
		this.source=source;
	}

	public Object getSource() {
		return this.source;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"->"+this.source; //$NON-NLS-1$
	}
}
