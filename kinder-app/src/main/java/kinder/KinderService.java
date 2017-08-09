package kinder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class KinderService {

	public static String getKinderTypes() {
		return KinderType.getAllAsString();
	}

	public static String getSeriesSurpriseByCodeAndType(String code, KinderType type) {
		if (code == null) {
			return "code is required";
		}
		if (type == null) {
			return "type is required";
		}

		// TODO read from DB
		if ("FF261".equals(code)) {
			JSONObject root = new JSONObject();
			JSONArray fields = new JSONArray();

			JSONObject field = new JSONObject();
			field.put("index", "1");
			field.put("label", "Code");
			field.put("value", "FF261");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "2");
			field.put("label", "Surprise");
			field.put("value", "Alex");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "3");
			field.put("label", "Numéro");
			field.put("value", "Inscrit sur la tasse");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "4");
			field.put("label", "Nombre de pièces");
			field.put("value", "Monobloc");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "5");
			field.put("label", "Autocollants1");
			field.put("value", "base64");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "6");
			field.put("label", "Autocollants2");
			field.put("value", "base64");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "7");
			field.put("label", "Picture");
			field.put("value", "base64");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "8");
			field.put("label", "Paper Picture");
			field.put("value", "base64");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "9");
			field.put("label", "Series Code");
			field.put("value", "FF261 à FF274 et FF299");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "10");
			field.put("label", "Series Name");
			field.put("value", "Série spéciale 40 ans");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "11");
			field.put("label", "Série");
			field.put("value", "FF");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "12");
			field.put("label", "Année");
			field.put("value", "2014 - 2015");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "13");
			field.put("label", "Particularité");
			field.put("value", "Pas de FF262\nFF265 brille dans le noir\nFF274 crache de l'eau"); // new lines
			fields.add(field);

			root.put("fields", fields);

			return root.toJSONString();
		}

		return "no results found";
	}

	public static String getAllSeriesForYearAndType(String year, KinderType type) {
		if (year == null) {
			return "year is required";
		}
		if (type == null) {
			return "type is required";
		}

		// TODO read from DB
		if (year.contains("2014") && year.contains("2015")) {
			JSONObject root = new JSONObject();
			JSONArray rows = new JSONArray();

			// 1
			JSONArray fields = new JSONArray();
			JSONObject field = new JSONObject();
			field.put("index", "1");
			field.put("label", "Code");
			field.put("value", "FF261 à FF274 et FF299");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "2");
			field.put("label", "Name");
			field.put("value", "Anniversaire Spécial 40 ans");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "3");
			field.put("label", "Type");
			field.put("value", "Kinder Surprise Boys");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "4");
			field.put("label", "Series Type");
			field.put("value", "Séries Principales");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "5");
			field.put("label", "Picture");
			field.put("value", "base64");
			fields.add(field);

			JSONObject row = new JSONObject();
			row.put("index", "1");
			row.put("fields", fields);
			rows.add(row);
			// END 1

			// 2
			fields = new JSONArray();
			field = new JSONObject();
			field.put("index", "1");
			field.put("label", "Code");
			field.put("value", "FF001 à FF004");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "2");
			field.put("label", "Name");
			field.put("value", "Animaux");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "3");
			field.put("label", "Type");
			field.put("value", "Kinder Surprise Boys");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "4");
			field.put("label", "Series Type");
			field.put("value", "Séries Secondaires");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "5");
			field.put("label", "Picture");
			field.put("value", "base64");
			fields.add(field);

			row = new JSONObject();
			row.put("index", "2");
			row.put("fields", fields);
			rows.add(row);
			// END 2

			// 3
			fields = new JSONArray();
			field = new JSONObject();
			field.put("index", "1");
			field.put("label", "Code");
			field.put("value", "FF275 à FF283");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "2");
			field.put("label", "Name");
			field.put("value", "Noël");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "3");
			field.put("label", "Type");
			field.put("value", "Kinder Surprise Boys");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "4");
			field.put("label", "Series Type");
			field.put("value", "Duty Free Only");
			fields.add(field);

			field = new JSONObject();
			field.put("index", "5");
			field.put("label", "Picture");
			field.put("value", "base64");
			fields.add(field);

			row = new JSONObject();
			row.put("index", "3");
			row.put("fields", fields);
			rows.add(row);
			// END 3

			root.put("rows", rows);

			return root.toJSONString();
		}

		return "no results found";
	}

}
