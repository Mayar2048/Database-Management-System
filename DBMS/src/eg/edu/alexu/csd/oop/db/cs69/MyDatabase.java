package eg.edu.alexu.csd.oop.db.cs69;

import java.io.File;
import java.sql.SQLException;

import eg.edu.alexu.csd.oop.db.Database;

public class MyDatabase implements Database {
	private File file;
	private String dpFilePath;

	@Override
	public String createDatabase(String databaseName, boolean dropIfExists) {
		file = (new File(databaseName));

		if (dropIfExists) {
			if (file.exists())
				this.dealWithFile(1, databaseName);
			this.dealWithFile(2, databaseName);
		} else {
			if (!file.exists())
				this.dealWithFile(2, databaseName);
		}
		dpFilePath = file.getAbsolutePath();
		return dpFilePath;
	}

	@Override
	public boolean executeStructureQuery(String query) throws SQLException {
		ParserClass p = new ParserClass();
		return p.valid(query, file);
	}

	@Override
	public Object[][] executeQuery(String query) throws SQLException {
		return null;
	}

	@Override
	public int executeUpdateQuery(String query) throws SQLException {
		return 0;
	}

	public void dealWithFile(int i, String databaseName) {
		switch (i) {
		case 1: // delete file
			try {
				this.executeStructureQuery("DROP DATABASE " + databaseName + ";");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 2: // create file
			try {
				this.executeStructureQuery("CREATE DATABASE " + databaseName + ";");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

}
