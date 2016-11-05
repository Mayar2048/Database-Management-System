package eg.edu.alexu.csd.oop.db.cs69.files;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadTableFile extends DefaultHandler {
	private String filePath, tmpElementName, tmpElementValue;
	private ArrayList<Map<String, Object>> rowList;
	private Map<String, String> attrMap;
	private Map<String, Object> rowMap;
	private boolean flag = false;

	public ReadTableFile(String path) {
		this.filePath = path;
		this.read();
	}

	public Map<String, String> getAttributes() {
		return attrMap;
	}
	
	public ArrayList<Map<String, Object>> getRowList(){
		return rowList;
	}

	public void read() {
		rowList = new ArrayList<Map<String, Object>>(0);
		
		// Create SAXParserFactory instance
		SAXParserFactory saxDoc = SAXParserFactory.newInstance();

		try {
			// Create a SAXParser from SAXParserFactory instance
			SAXParser saxParser = saxDoc.newSAXParser();

			// register the handler to the saxParser
			saxParser.parse(filePath, this);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tmpElementName = qName;
		tmpElementValue = "";
		
		if (qName.equalsIgnoreCase("Attributes")) {
			flag = true;
			attrMap = new LinkedHashMap<String, String>();
		}
		
		if(qName.equalsIgnoreCase("Row")){
			rowMap = new LinkedHashMap<String, Object>();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (tmpElementName.equals(qName) && !tmpElementName.equalsIgnoreCase("Attributes") && flag) {
			attrMap.put(tmpElementName.toLowerCase(), tmpElementValue);
		}
		
	    if(qName.equalsIgnoreCase("Attributes") && flag){
			flag = false;
		}
		
		if(tmpElementName.equals(qName) && !tmpElementName.equalsIgnoreCase("Row") && !flag){
			rowMap.put(tmpElementName.toLowerCase(), tmpElementValue);
		}
		
		if(qName.equalsIgnoreCase("Row") && !flag){
			rowList.add(rowMap);
		}
	}

	@Override
	public void characters(char ch[], int start, int length)
			throws SAXException {
		tmpElementValue = new String(ch, start, length).toLowerCase();
	}
}
