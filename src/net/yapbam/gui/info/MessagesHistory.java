package net.yapbam.gui.info;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.yapbam.gui.YapbamState;

public class MessagesHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	private static MessagesHistory INSTANCE;
	
	private Map<String, Long> idToReadTime;
	
	private MessagesHistory() {
		this.idToReadTime = new HashMap<String, Long>();
	}
	
	private static MessagesHistory get() {
		if (INSTANCE==null) {
			INSTANCE = (MessagesHistory) YapbamState.INSTANCE.restore(MessagesHistory.class.getName());
			if (INSTANCE==null) {
				INSTANCE = new MessagesHistory();
			}
		}
		return INSTANCE;

	}
	
	public static long getReadTime(Message message) {
		Long result = get().idToReadTime.get(message.getId());
		return result==null?0:result;
	}
	
	public static void setReadTime(Message message, long time) {
		MessagesHistory history = get();
		if (time<=0) {
			history.idToReadTime.remove(message.getId());
		} else {
			history.idToReadTime.put(message.getId(), time);
		}
		YapbamState.INSTANCE.save(MessagesHistory.class.getName(), history);
	}
}
