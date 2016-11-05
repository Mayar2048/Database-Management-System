package eg.edu.alexu.csd.oop.db.cs69.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CreateTableFile {
	private String filePath;
	private String tableName;
	private boolean flag = false;
	private Map<String, String> attributes = new LinkedHashMap<String, String>();

	public CreateTableFile(String str, String path, Map<String, String> s) {
		this.filePath = path;
		this.tableName = str;
		this.attributes = s;
	}

	public boolean create(ArrayList<Map<String, Object>> map) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.newDocument();
			org.w3c.dom.Element root = doc.createElement(tableName);
			doc.appendChild(root);
			
			org.w3c.dom.Element child = doc.createElement("Attributes");
			root.appendChild(child);
			
			if(attributes != null){
				for (Map.Entry<String, String> arr : attributes.entrySet()) {			
					org.w3c.dom.Element element = doc.createElement(arr.getKey());
					element.appendChild(doc.createTextNode(arr.getValue()));
					child.appendChild(element);
				}
			}
			
			if(map != null){
				Iterator<Map<String, Object>> row = map.iterator();
				while (row.hasNext()) {
					org.w3c.dom.Element childRow = doc.createElement("Row");
					root.appendChild(childRow);
					
					for (Map.Entry<String, Object> arr : row.next().entrySet()) {			
						org.w3c.dom.Element element = doc.createElement(arr.getKey());
						element.appendChild(doc.createTextNode(arr.getValue().toString()));
						childRow.appendChild(element);
					}
				}	
			}	

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);		
			flag = true;
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
		return flag; 
	}
}
