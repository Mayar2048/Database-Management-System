package eg.edu.alexu.csd.oop.db.cs69;

import java.io.File;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.db.Database;

public class ImplemDatabase implements Database {
	private File file;
	private String dpFilePath, rw;
	String[] regex = new String[5];
	private DatabaseClass obj;

	public ImplemDatabase() {
		this.file = null;
		this.dpFilePath = null;
		obj = new DatabaseClass();
		this.rw = "(?!(?i)(((select)|(delete)|((create|drop)\\sdatabase)|(droptable)|(update)|(create)|(insert\\sinto))))";
		regex[0] = "^(?i)(CREATE\\sDATABASE)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*)(;)?(\\s*)$";
		regex[1] = "^(?i)(DROP\\sDATABASE|DELETE\\sDATABASE)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*)(;)?(\\s*)$";
		regex[2] = "^(?i)(CREATE\\sTABLE)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*)[(]([(\\s*)"
				+ rw + "([a-zA-Z]\\w+?)(\\s+)(int|varchar)(\\s*)(?,)(\\s*)]+)?[)](\\s*)(;)?\\s*$";
		regex[3] = "^(?i)(DROP\\sTABLE|DELETE\\sTABLE)(\\s*)" + rw + "([a-zA-Z]\\w+?)(\\s*)(;)?(\\s*)$";
		regex[4] = "ERROR";
	}

	@Override
	public String createDatabase(String databaseName, boolean dropIfExists) {
		file = (new File(databaseName));
		if (dropIfExists) {
			if (file.exists()) {
				this.dealWithFile(1, databaseName);
				this.dealWithFile(2, databaseName);
			} else
				this.dealWithFile(3, databaseName);
		} else {
			if (!file.exists()) {
				this.dealWithFile(2, databaseName);
			} else {
				this.dealWithFile(3, databaseName);
			}
		}
		dpFilePath = file.getAbsolutePath();
		return dpFilePath;
	}

	@Override
	public boolean executeStructureQuery(String query) throws SQLException {
		switch (this.selectQuery(query)) {
		case 0:
			return file.mkdir();	
		case 1:
			String[]entries = file.list();
			for(String entry: entries){
				File currentFile = new File(file.getPath(),entry);
				currentFile.delete();
				}
			return file.delete();		
		case 2:	
			return obj.createFile(query, file);		
		case 3:
			return obj.dropFile(query, file);
		case 4:
			return false;
		}	
		throw new SQLException();
	}

	@Override
	public Object[][] executeQuery(String query) throws SQLException {
		return null;
	}

	@Override
	public int executeUpdateQuery(String query) throws SQLException {
		Parser p = new Parser();
		return p.matchQuery(query, file);
	}

	public int selectQuery(String query) throws SQLException {
		for (int index = 0; index < regex.length; index++) {
			Pattern p = Pattern.compile(regex[index], Pattern.MULTILINE);
			Matcher matcher = p.matcher(query);
			if (matcher.find()) {
				return index;
			}
		}
		throw new SQLException();
	}

	public void dealWithFile(int i, String databaseName) {
		switch (i) {
		case 1: // delete file
			try {
				this.executeStructureQuery("DROP DATABASE " + databaseName);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 2: // create file
			try {
				this.executeStructureQuery("CREATE DATABASE " + databaseName);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				this.executeStructureQuery("ERROR");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}