package net.yapbam.gui.info;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;

import com.fathzer.jlocal.Formatter;

class Info {
	private static final String IGNORE_TIME_ATTR = "ignoreTime";
	private static final String PARAMS_ATTR = "parameters";
	private static final String TEXT_ATTR = "text";
	private static final String ID_ATTR = "id";
	private String id;
	private String content;
	private String[] parameters;
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

	public static Info build(JSONObject obj) {
		if (!"news".equals(obj.get("kind")) || !(obj.get(ID_ATTR) instanceof String) || !(obj.get(TEXT_ATTR) instanceof String)) {
			LoggerFactory.getLogger(Info.class).info("Invalid JSON object: {}", obj);
			return null;
		}
		Info result = new Info((String)obj.get(ID_ATTR), (String)obj.get(TEXT_ATTR));
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
		System.out.println (obj.get(IGNORE_TIME_ATTR).getClass());
		return result;
	}
}