package eg.edu.alexu.csd.oop.db.cs69;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {
	protected String tableName;

	public LinkedHashMap<String, String> tabCreate(String query) {
		LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>();
		String[] sp = query.replaceAll("(?i)CREATE\\sTABLE|\\)|;", "").split(
				"[(]");
		tableName = sp[0].trim();
		if (sp.length == 1)
			columns = null;
		else {
			String[] sp2 = sp[1].split(",");
			for (int j = 0; j < sp2.length; j++) {
				sp2[j] = sp2[j].trim();
				String[] sp3 = sp2[j].split("\\s+");
				columns.put(sp3[0], sp3[1]);
			}
		}
		return columns;
	}

	public String dropTable(String query) {
		return query.replaceAll(";|((?i)DROP\\sTABLE)", "").trim();
	}

	public LinkedHashMap<String, Object> insert(String query) {
		String r1 = "^\\s*(?i)(INSERT\\sINTO)(\\s+)(([a-zA-Z]\\w+?))(\\s*)"
				+ "(?i)(VALUES)(\\s*)[(](\\s*)[((\\w+?)|((')(\\.+?)(')))(,?)(\\s*)]+[)](\\s*)(;?)\\s*$";
		String r2 = "^\\s*(?i)(INSERT\\sINTO)(\\s+)(([a-zA-Z]\\w+?))(\\s*)(([(](\\s*)(((\\w+?)(,?)(\\s*))+)[)]))(\\s*)"
				+ "(?i)(VALUES)(\\s*)[(](\\s*)[((\\w+?)|((')(\\.+?)(')))(,?)(\\s*)]+[)](\\s*)(;?)\\s*$";
		Pattern p = Pattern.compile(r1, Pattern.MULTILINE);
		Matcher matcher = p.matcher(query);
		LinkedHashMap<String, Object> in = new LinkedHashMap<String, Object>();
		if (matcher.find()) {
			String[] sp = query.replaceAll(
					"(?i)INSERT\\sINTO|;|\\)+|\\(+|(\\s+)", "").split(
					"(?i)VALUES");
			tableName = sp[0];
			String[] values = sp[1].split(",");
			/*
			 * for(int i=0;i<values.length;i++){ in.put("", values[i]);
			 * 
			 * }
			 */
			in.put("first", values[0]);
			in.put("second", values[1]);
			in.put("age", values[2]);
			System.out.println(in);
			return in;
		}
		String[] sp = query.replaceAll("(?i)INSERT\\sINTO|;|(\\s+)", "").split(
				"(?i)VALUES");

		String[] sp1 = sp[0].replaceAll("\\)+", "").split("\\(");
		tableName = sp1[0];
		String[] col = sp1[1].split(",");

		String[] values = sp[1].replaceAll("\\(+|\\)+", "").split(",");
		for (int i = 0; i < col.length; i++) {
			in.put(col[i], values[i]);
		}
		return in;
	}

	public String delete(String query) {
		String[] sp = query.replaceAll("(?i)DELETE|;|(\\s+)", "").split("(?i)FROM");
		tableName = sp[1];
		return sp[1];
	}

	//
	public String deleteCond(String query) {
		String[] sp = query.replaceAll("(?i)DELETE|;|(\\s+)", "").split("(?i)FROM");
		String[] sp1 = sp[1].split("(?i)WHERE");
		tableName = sp1[0];
		String[] sp2 = sp1[1].split("(<|>|=)");
		String condCol = sp2[0];
		String condVal = sp2[1];
		return sp[0];
	}

}