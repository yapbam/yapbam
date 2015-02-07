package net.yapbam.gui.info;

import java.util.List;

public class News extends FilteredList<Info> {
	private boolean onlyUnread;
	
	public News(List<Info> news) {
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
	
	public Info get(int index) {
		if (this.onlyUnread) {
			return super.get(index);
		} else {
			return physical.get(index);
		}
	}

	@Override
	protected boolean isOk(Info element) {
		return !(onlyUnread && element.isRead());
	}

	public boolean isOnlyUnread() {
		return onlyUnread;
	}

	public void setOnlyUnread(boolean onlyUnread) {
		this.onlyUnread = onlyUnread;
	}
}
