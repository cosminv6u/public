package kinder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public enum KinderType {

	SURPRISE_BOYS("Kinder Surprise Boys", "1"), 
	SURPRISE_GIRLS("Kinder Surprise Girls", "2"), 
	JOY_BOYS("Kinder Joy Boys", "3"), 
	JOY_GIRLS("Kinder Joy Girls", "4"), 
	MAXI_BOYS("Kinder Maxi Boys", "5"), // TODO what about Kinder Grand Surprise
	MAXI_GIRLS("Kinder Maxi Girls", "6"), // TODO what about Kinder Maxi Halloween?
	OTHER("Miscellaneous", "7");

	private String title;

	private String id;

	private KinderType(String title, String id) {
		this.title = title;
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getId() {
		return id;
	}

	public static String getAllAsString() {
		JSONObject root = new JSONObject();
		JSONArray rows = new JSONArray();

		JSONObject row1 = new JSONObject();
		row1.put("value", SURPRISE_BOYS.getTitle());
		row1.put("index", SURPRISE_BOYS.getId());
		rows.add(row1);

		JSONObject row2 = new JSONObject();
		row2.put("value", SURPRISE_GIRLS.getTitle());
		row2.put("index", SURPRISE_GIRLS.getId());
		rows.add(row2);

		JSONObject row3 = new JSONObject();
		row3.put("value", JOY_BOYS.getTitle());
		row3.put("index", JOY_BOYS.getId());
		rows.add(row3);

		JSONObject row4 = new JSONObject();
		row4.put("value", JOY_GIRLS.getTitle());
		row4.put("index", JOY_GIRLS.getId());
		rows.add(row4);

		JSONObject row5 = new JSONObject();
		row5.put("value", MAXI_BOYS.getTitle());
		row5.put("index", MAXI_BOYS.getId());
		rows.add(row5);

		JSONObject row6 = new JSONObject();
		row6.put("value", MAXI_GIRLS.getTitle());
		row6.put("index", MAXI_GIRLS.getId());
		rows.add(row6);

		JSONObject row7 = new JSONObject();
		row7.put("value", OTHER.getTitle());
		row7.put("index", OTHER.getId());
		rows.add(row7);

		root.put("rows", rows);
		return root.toJSONString();
	}

	public static KinderType getById(String id) {
		switch (id) {
		case "1":
			return SURPRISE_BOYS;
		case "2":
			return SURPRISE_GIRLS;
		case "3":
			return JOY_BOYS;
		case "4":
			return JOY_GIRLS;
		case "5":
			return MAXI_BOYS;
		case "6":
			return MAXI_GIRLS;
		case "7":
			return OTHER;
		default:
			return null;
		}
	}

}
