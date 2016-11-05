package eg.edu.alexu.csd.oop.db.cs69.valid;

import java.util.LinkedHashMap;
import java.util.Map;

public class InsertValidity {
	private Map<String, String> attrMap;
	private Map<String, Object> insertedMap;
	private boolean flag = true;

	public InsertValidity(Map<String, String> aMap, Map<String, Object> bMap) {
		attrMap = new LinkedHashMap<String, String>();
		this.attrMap = aMap;
		insertedMap = new LinkedHashMap<String, Object>();
		this.insertedMap = bMap;
	}
	
	@SuppressWarnings("unused")
	public boolean isValid(){
		System.out.println(attrMap.entrySet());
		for (Map.Entry<String, Object> entry : insertedMap.entrySet()) {
			if(attrMap.containsKey(entry.getKey())){
				String str1 = attrMap.get(entry.getKey());
				if(str1.equals("int")){		
					try{
						Integer num = Integer.parseInt((String) entry.getValue());
					}catch(NumberFormatException e){
						//System.out.println("1");
						flag = false;
					}
				}else if(str1.equals("varchar")){
					String str2 = entry.getValue().toString();
					if(!(str2.startsWith("'") && str2.endsWith("'"))){
						//System.out.println("2");
						flag = false;
						break;
					}
				}
			}else{
				//System.out.println("3");
				flag = false;
				break;
			}
		}	
		return flag;
	}
}
