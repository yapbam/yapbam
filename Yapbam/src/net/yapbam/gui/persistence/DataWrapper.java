package net.yapbam.gui.persistence;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.fathzer.soft.jclop.Cancellable;
import com.fathzer.soft.jclop.Service;

/** Wraps an arbitrary data object to an instance that can be used by PersistenceManager methods.
 * @param <T> The class of the wrapped data object
 * @author Jean-Marc Astesana
 * @see PersistenceManager
 */
public abstract class DataWrapper<T> {
	/** The wrapped data. */
	protected T data;

	/** Constructor.
	 * @param data The data to be wrapped.
	 */
	protected DataWrapper (T data) {
		this.data = data;
	}
	
	/** Tests whether the data has changed since the last time it was saved.
	 * <br>The wrapper is responsible for replying true if the underlying data was changed since the last time setUnchanged was called.
	 * @return true if the data has changed
	 * @see #setUnchanged()
	 */
	public abstract boolean isChanged();
	
	public abstract boolean isEmpty();
	
	/** Sets the data as unchanged.
	 * <br>After a call to this method, the isChanged method returns false ... until the data is changed again.
	 * <br>Typically, this method is called by the PersistenceManager when the data is successfully saved.
	 */
	public abstract void setUnchanged();

	public abstract URI getURI();
	
	public abstract void setURI(URI uri);

	public abstract void serialize(File file, Service service, Cancellable cancellable) throws IOException;
	public abstract T deserialize(File file, String password, Service service, Cancellable cancellable) throws IOException;
	public abstract boolean needPassword(File file) throws IOException;

	public abstract void clear();

	public abstract void commit(URI uri, Object object);
}
