package eg.edu.alexu.csd.oop.db.cs69;

import java.io.File;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser extends DatabaseClass{
	private boolean found;
	private String[] regex;
	private String rw;
	
	public Parser() {
		this.regex = new String[7];
		this.found = false;
		this.setRegex();
	}
	
	public void setRegex(){
		this.rw = "(?!(?i)(((select)|(delete)|((create|drop)\\sdatabase)"
				+ "|(droptable)|(update)|(create)|(insert\\sinto))))";
		regex[0] = "^(?i)(INSERT\\sINTO)(\\s*)(\\w+?)(\\s*)(([(](\\s*)[(\\w+?)(?,)(\\s*)]+[)])?)(\\s*)(?i)"
				+ "(VALUES)(\\s*)[(](\\s*)[((\\w+?)|((')(\\w+?)(')))(?,)(\\s*)]+[)](\\s*)(;)?\\s*$";
		regex[1] = "^(?i)(DELETE)(\\s*)(\\*)?(\\s*)(FROM)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*)(;)?\\s*$";
		regex[2] = "^(?i)(DELETE)(\\s*)(FROM)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*)(?i)(WHERE)(\\s*)" 
				+ rw + "([a-zA-Z]\\w+?)(\\s*)(=|>|<)(\\s*)((\\w+?)|((')(\\w+?)(')))(\\s*)(;)?\\s*$";
	}
	
	public int matchQuery(String query, File directory) throws SQLException{
		for(int index = 0; index < regex.length; index++){	
			Pattern p = Pattern.compile(regex[index], Pattern.MULTILINE);
			Matcher matcher = p.matcher(query);
			
			if(matcher.find()){
				switch (index) {					
				case 0:
					return this.insertToFile(query, directory);	
				
				case 1:		
					return this.deleteWholeFile(query, directory);
				}
				
				found = true;
			}	
		}	
	return 0;	
	}
}