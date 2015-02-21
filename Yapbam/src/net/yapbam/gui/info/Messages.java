package net.yapbam.gui.info;

import java.util.List;

public class Messages extends FilteredList<Message> {
	private boolean onlyUnread;
	
	public Messages(List<Message> news) {
		super(news);
		this.onlyUnread = true;
	}
	
	public int size() {
		if (this.onlyUnread) {
			return super.size();
		} else {
			return getPhysicalSize();
		}
	}
	
	public Message get(int index) {
		if (this.onlyUnread) {
			return super.get(index);
		} else {
			return physical.get(index);
		}
	}

	@Override
	protected boolean isOk(Message element) {
		return !(onlyUnread && element.isRead());
	}

	public boolean isOnlyUnread() {
		return onlyUnread;
	}

	public void setOnlyUnread(boolean onlyUnread) {
		this.onlyUnread = onlyUnread;
	}
	
	public int getNbRead() {
		int read = 0;
		for (Message message : physical) {
			if (message.isRead()) {
				read++;
			}
		}
		return read;
	}

	public boolean isAllRead() {
		for (Message message : physical) {
			if (!message.isRead()) {
				return false;
			}
		}
		return true;
	}

	public int getNearest(Message message) {
		int index = -1;
		for (Message m : physical) {
			boolean ok = isOk(m);
			if (ok) {
				index++;
			}
			if (m.getId().equals(message.getId())) {
				if (!ok) {
					index++;
				}
				break;
			}
		}
		int size = size();
		if (index>=size) {
			index = size==0?-1:0;
		}
		return index;
	}
}
