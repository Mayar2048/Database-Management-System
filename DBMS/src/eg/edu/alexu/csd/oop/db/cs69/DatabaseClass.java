package eg.edu.alexu.csd.oop.db.cs69;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.db.cs69.files.CreateTableFile;
import eg.edu.alexu.csd.oop.db.cs69.files.ReadTableFile;

public class DatabaseClass extends Query {
	private String name, path;
	private Map<String, String> columns = new LinkedHashMap<String, String>();

	public boolean createFile(String query, File directory) throws SQLException {
		if (directory != null) {
			CreateTableFile writeDoc;
			columns = this.tabCreate(query);
			name = this.tableName;
			if (name.length() > 255) {
				throw new SQLException();
			} else {
				path = directory.getAbsolutePath() + "\\" + tableName + ".xml";
				if(new File(path).exists()){
					return false;
				}else{
					writeDoc = new CreateTableFile(tableName, path, columns);
					if (!writeDoc.create(null)) {
						throw new SQLException();
					} else {
						return writeDoc.create(null);
					}
				}
			}
		} else {
			throw new SQLException();
		}
	}

	public boolean dropFile(String query, File directory) throws SQLException {
		name = this.dropTable(query);
		path = directory.getAbsolutePath() + "\\" + name + ".xml";
		File fileDrop = new File(path);
		if (fileDrop.exists()) {
			fileDrop.delete();
			return true;
		} else
			return false;
	}

	public int insertToFile(String query, File directory) throws SQLException {	
		ArrayList<Map<String, Object>> dataList = new ArrayList<>();
		Map<String,Object> data = new LinkedHashMap<String,Object>();	
	    data = this.insert(query);    
		name = this.tableName;	 
		path = directory.getAbsolutePath() + "\\" + name+ ".xml";
		ReadTableFile in = new ReadTableFile(path);
		dataList=in.getRowList(); 
		dataList.add(data);
		CreateTableFile doc = new CreateTableFile(name, path, in.getAttributes());
		doc.create(dataList);
		return 1;
	}
	
	public int deleteWholeFile(String query, File directory) throws SQLException {
		name = this.delete(query);
		path = directory.getAbsolutePath() + "\\" + name+ ".xml";
		if(new File(path).exists()){
			ReadTableFile readObj = new ReadTableFile(path);
			int num = readObj.getRowList().size();	
			return num;
		}
		return 0;	
	}
}