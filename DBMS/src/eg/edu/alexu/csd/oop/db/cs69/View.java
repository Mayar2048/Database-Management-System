package eg.edu.alexu.csd.oop.db.cs69;

import java.sql.SQLException;

import eg.edu.alexu.csd.oop.db.Database;

public class View {
	public static void main(String[] args) {
	 	Database obj = new ImplemDatabase();
	 	obj.createDatabase("mayar", false); 	
	}
}
