package eg.edu.alexu.csd.oop.db.cs69;

import java.sql.SQLException;

import eg.edu.alexu.csd.oop.db.Database;

public class MainClass {

	public static void main(String[] args) {
		Database y = new MyDatabase();
		System.out.println(y.createDatabase("Database", true));
		try {
			y.executeStructureQuery("create table xyz(id int,school varchar);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
