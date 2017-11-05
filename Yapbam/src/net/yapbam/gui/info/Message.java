package net.yapbam.gui.info;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;

import com.fathzer.jlocal.Formatter;

class Message {
	enum Kind {
		INFO, WARNING;
	}
	
	private static final String KIND_ATTR = "kind"; //$NON-NLS-1$
	private static final String IGNORE_TIME_ATTR = "ignoreTime"; //$NON-NLS-1$
	private static final String PARAMS_ATTR = "parameters"; //$NON-NLS-1$
	private static final String TEXT_ATTR = "text"; //$NON-NLS-1$
	private static final String ID_ATTR = "id"; //$NON-NLS-1$
	private Kind kind;
	private String id;
	private String content;
	private String[] parameters;
	/** Max ignore time in days. */
	private long maxIgnoreTime;
	
	Message(String id, String kind, String content) {
		super();
		this.id = id;
		if ("news".equals(kind)) { //$NON-NLS-1$
			this.kind = Kind.INFO;
		} else if ("warning".equals(kind)) { //$NON-NLS-1$
			this.kind = Kind.WARNING;
		} else {
			this.kind = null;
		}
		this.content = content;
		this.parameters = new String[0];
		this.maxIgnoreTime = Long.MAX_VALUE;
	}
	
	public Kind getKind() {
		return kind;
	}

	public String getContent() {
		if (parameters==null) {
			return content;
		} else {
			return Formatter.format(content, (Object[])parameters);
		}
	}
	
	private static boolean isMessage(JSONObject obj) {
		Object kind = obj.get(KIND_ATTR);
		return "news".equals(kind) || "warning".equals(kind); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static Message build(JSONObject obj) {
		if ("arguments".equals(obj.get(KIND_ATTR))) { //$NON-NLS-1$
			LoggerFactory.getLogger(Message.class).info("Input: {}", obj); //$NON-NLS-1$
			return null;
		} else if (!isMessage(obj) || !(obj.get(ID_ATTR) instanceof String) || !(obj.get(TEXT_ATTR) instanceof String)) {
			LoggerFactory.getLogger(Message.class).info("Unexpected JSON object: {}", obj); //$NON-NLS-1$
			return null;
		}
		Message result = new Message((String)obj.get(ID_ATTR), (String)obj.get(KIND_ATTR), (String)obj.get(TEXT_ATTR));
		if (obj.get(IGNORE_TIME_ATTR) instanceof Long) {
			result.maxIgnoreTime = (Long) obj.get(IGNORE_TIME_ATTR);
		}
		if (obj.get(PARAMS_ATTR) instanceof JSONArray) {
			JSONArray array = (JSONArray) obj.get(PARAMS_ATTR);
			String[] params = new String[array.size()];
			boolean ok = true;
			for (int i = 0; i < params.length; i++) {
				if (array.get(i) instanceof String) {
					params[i] = (String) array.get(i);
				} else {
					ok = false;
					break;
				}
			}
			if (ok) {
				result.parameters = params;
			}
		}
		return result;
	}

	public void markRead() {
		MessagesHistory.setReadTime(this, System.currentTimeMillis());
	}
	
	public boolean isRead() {
		long readTime = MessagesHistory.getReadTime(this);
		if (readTime<=0) {
			return false;
		} else {
			long ms = System.currentTimeMillis()-readTime;
			long days = ms/(60000*60*24);
			return days<=maxIgnoreTime;
		}
	}
	
	String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "["+getId()+" ("+(isRead()?"read":"unread")+")]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}
 }