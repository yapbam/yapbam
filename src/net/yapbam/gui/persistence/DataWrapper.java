package net.yapbam.gui.persistence;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.fathzer.soft.jclop.Cancellable;
import com.fathzer.soft.jclop.Service;

public abstract class DataWrapper<T> {
	//TODO Rename to DataWrapper
	protected T data;

	protected DataWrapper (T data) {
		this.data = data;
	}
	
	/** Tests whether the data has changed since the last time it was saved.
	 * @return true if the data has changed
	 */
	public abstract boolean hasChanged();
	
	public abstract void setChanged(boolean changed);

	public abstract URI getURI();
	
	public abstract void setURI(URI uri);

	public abstract void serialize(File file, Service service, Cancellable cancellable) throws IOException;
	public abstract Object deserialize(File file, String password, Service service, Cancellable cancellable) throws IOException;

	public abstract void clear();

	public abstract boolean needPassword(File file) throws IOException;

	public abstract void commit(URI uri, Object object);
}
