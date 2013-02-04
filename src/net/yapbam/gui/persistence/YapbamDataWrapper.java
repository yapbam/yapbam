package net.yapbam.gui.persistence;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import net.yapbam.data.GlobalData;
import net.yapbam.data.xml.Serializer;

import com.fathzer.soft.jclop.Cancellable;
import com.fathzer.soft.jclop.Service;

public class YapbamDataWrapper extends DataWrapper<GlobalData> {
	public YapbamDataWrapper(GlobalData data) {
		super(data);
	}

	@Override
	public boolean isChanged() {
		return data.somethingHasChanged();
	}

	@Override
	public void setUnchanged() {
		data.setChanged(false);
	}

	@Override
	public URI getURI() {
		return data.getURI();
	}

	@Override
	public void setURI(URI uri) {
		data.setURI(uri);
	}

	@Override
	public void serialize(File file, Service service, Cancellable cancellable) throws IOException {
		Serializer.write(data, file, !service.isLocal(), new ProgressReportAdapter(cancellable));
	}

	@Override
	public GlobalData deserialize(File file, String password, Service service, Cancellable cancellable) throws IOException {
		return Serializer.read(file.toURI(), password, new ProgressReportAdapter(cancellable));
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public boolean needPassword(File file) throws IOException {
		return Serializer.getSerializationData(file.toURI()).isPasswordRequired();
	}

	@Override
	public void commit(URI uri, Object newData) {
		boolean enabled = data.isEventsEnabled();
		data.setEventsEnabled(false);
		data.copy((GlobalData) newData);
		data.setChanged(false);
		data.setURI(uri);
		data.setEventsEnabled(enabled);
	}
}
