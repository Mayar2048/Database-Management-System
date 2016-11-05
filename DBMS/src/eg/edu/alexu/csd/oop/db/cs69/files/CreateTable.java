package eg.edu.alexu.csd.oop.db.cs69.files;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CreateTable {
	private String filePath;
	private String tableName;
	private Map<String, String> attributes = new LinkedHashMap<String, String>();

	public CreateTable(String name, String path, Map<String, String> s) {
		this.filePath = path;
		this.tableName = name;
		this.attributes = s;
		this.create();
	}

	public void create() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.newDocument();
			org.w3c.dom.Element root = doc.createElement(tableName);
			doc.appendChild(root);
			
			org.w3c.dom.Element child = doc.createElement("Attributes");
			root.appendChild(child);
			
			for (Map.Entry<String, String> arr : attributes.entrySet()) {			
				org.w3c.dom.Element element = doc.createElement(arr.getKey());
				element.appendChild(doc.createTextNode(arr.getValue()));
				child.appendChild(element);
			}

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		} 
	}
}
