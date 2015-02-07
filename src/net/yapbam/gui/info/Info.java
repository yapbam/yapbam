package net.yapbam.gui.info;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;

import com.fathzer.jlocal.Formatter;

class Info {
	private static final String KIND_ATTR = "kind";
	private static final String IGNORE_TIME_ATTR = "ignoreTime";
	private static final String PARAMS_ATTR = "parameters";
	private static final String TEXT_ATTR = "text";
	private static final String ID_ATTR = "id";
	private String id;
	private String content;
	private String[] parameters;
	/** Max ignore time in days. */
	private long maxIgnoreTime;
	
	private Info(String id, String content) {
		super();
		this.id = id;
		this.content = content;
		this.parameters = new String[0];
		this.maxIgnoreTime = Long.MAX_VALUE;
	}

	public String getContent() {
		if (parameters==null) {
			return content;
		} else {
			return Formatter.format(content, (Object[])parameters);
		}
	}
	
	private static boolean isInfo(JSONObject obj) {
		Object kind = obj.get(KIND_ATTR);
		return "news".equals(kind) || "warning".equals(kind);
	}

	public static Info build(JSONObject obj) {
		if ("arguments".equals(obj.get(KIND_ATTR))) {
			LoggerFactory.getLogger(Info.class).info("Input: {}", obj);
			return null;
		} else if (!isInfo(obj) || !(obj.get(ID_ATTR) instanceof String) || !(obj.get(TEXT_ATTR) instanceof String)) {
			LoggerFactory.getLogger(Info.class).info("Unexpected JSON object: {}", obj);
			return null;
		}
		Info result = new Info((String)obj.get(ID_ATTR), (String)obj.get(TEXT_ATTR));
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
		System.out.println(id+" is mark read (was "+isRead()+")");
		ReadInfo.setReadTime(this, System.currentTimeMillis());
	}
	
	public boolean isRead() {
		long ms = System.currentTimeMillis()-ReadInfo.getReadTime(this);
		long days = ms/(60000*60*24);
		return days<=maxIgnoreTime;
	}
	
	String getId() {
		return this.id;
	}
 }