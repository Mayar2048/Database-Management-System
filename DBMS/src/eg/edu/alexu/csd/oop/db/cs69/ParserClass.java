package eg.edu.alexu.csd.oop.db.cs69;

import java.io.File;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.db.cs69.files.CreateTable;

public class ParserClass {
	private boolean found = false;
	private String[] regex = new String[10];

	public boolean valid(String s, File f) throws SQLException{
		String rw = "(?!(?i)(((select)|(delete)|((create|drop)\\sdatabase)|(droptable)|(update)|(create)|(insert\\sinto))))";
		String[] arr = s.split("\\s+");
		String str = arr[arr.length-1];
		String path;
		
		regex[0] = "^(?i)(CREATE\\sDATABASE)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*);(\\s*)$";
		regex[1] = "^(?i)(DROP\\sDATABASE|DELETE\\sDATABASE)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*);(\\s*)$";
		regex[2] = "^(?i)(CREATE\\sTABLE)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*)[(][(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s+)(int|varchar)(\\s*)(?,)(\\s*)]+[)](\\s*);\\s*$";
		regex[3] = "^(?i)(DROP\\sTABLE|DELETE\\sTABLE)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*);(\\s*)$";
		
		regex[4] = "^(?i)(DELETE)(\\s*)(\\*)?(\\s*)(FROM)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*);\\s*$";
		regex[5] = "^(?i)(SELECT)(\\s*)(\\*|[\\w+? \\s* ?,\\s*]+)(\\s*)(?i)(FROM)(\\s*)(\\w+?)(\\s*)(((?i)(WHERE)(\\s*)(\\w+?)(\\s*)(=|>|<)(\\s*)((\\w+?)|((')(\\w+?)(')))(\\s*))?)(\\s*);\\s*$";
		regex[6] = "^(?i)(UPDATE)(\\s*)(\\w+?)(\\s*)(SET)[(\\s*)(\\w+?)(\\s*)(=)(\\s*)((\\w+?)|((')(\\w+?)(')))(\\s*)(?,)(\\s*)]+(?i)(WHERE)(\\s*)(\\w+?)(\\s*)(=)(\\s*)((\\w+?)|((')(\\w+?)(')))(\\s*);\\s*$";
		regex[7] = "^(?i)(INSERT\\sINTO)(\\s*)(\\w+?)(\\s*)(([(](\\s*)[(\\w+?)(?,)(\\s*)]+[)])?)(\\s*)(?i)(VALUES)(\\s*)[(](\\s*)[((\\w+?)|((')(\\w+?)(')))(?,)(\\s*)]+[)](\\s*);\\s*$";
		regex[8] = "^(?i)(DELETE)(\\s*)(FROM)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*)(?i)(WHERE)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*)(=|>|<)(\\s*)((\\w+?)|((')(\\w+?)(')))(\\s*);\\s*$";
		
		for (int i = 0; i < regex.length; i++) {
			found = false;
			Pattern p = Pattern.compile(regex[i], Pattern.MULTILINE);
			Matcher matcher = p.matcher(s);
			if (matcher.find()) {
				switch (i) {
				case 0:
					return f.mkdir();
				case 1:
					return f.delete();
				case 2:		
					String tableName;
					Map<String,String> columns = new LinkedHashMap<String,String>();
					String[] sp = s.replaceAll("(?i)CREATE\\sTABLE|\\)|;", "").split("[(]");
					tableName = sp[0].trim();
					String[] sp2 =sp[1].split(","); 
					path = f.getAbsolutePath() + "\\" +  tableName+ ".xml";
					for(int j=0;j<sp2.length;j++){
						String[] sp3=sp2[j].split("\\s+");
						columns.put(sp3[0].trim(), sp3[1].trim());			
					}
					CreateTable writeDoc = new CreateTable(tableName, path, columns);
					break;
				case 3:
					File check = new File(f.getAbsolutePath() + "\\" +  str.substring(0, str.length()-1)+ ".xml");
					if(check.exists()) {
						return check.delete();
					}
					break;
				default:
					break;
				}
				found = true;
				break;
			}
		}
		
		return found;
	}
}