package net.yapbam.gui.info;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.yapbam.gui.YapbamState;

public class ReadInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private static ReadInfo INSTANCE;
	
	private Map<String, Long> idToReadTime;
	
	private ReadInfo() {
		this.idToReadTime = new HashMap<String, Long>();
	}
	
	private static ReadInfo get() {
		if (INSTANCE==null) {
			INSTANCE = (ReadInfo) YapbamState.INSTANCE.restore(ReadInfo.class.getName());
			if (INSTANCE==null) {
				INSTANCE = new ReadInfo();
			}
		}
		return INSTANCE;

	}
	
	public static long getReadTime(Info info) {
		Long result = get().idToReadTime.get(info.getId());
		return result==null?0:result;
	}
	
	public static void setReadTime(Info info, long time) {
		ReadInfo readInfo = get();
		if (time<=0) {
			readInfo.idToReadTime.remove(info.getId());
		} else {
			readInfo.idToReadTime.put(info.getId(), time);
		}
		YapbamState.INSTANCE.save(ReadInfo.class.getName(), readInfo);
	}
}
