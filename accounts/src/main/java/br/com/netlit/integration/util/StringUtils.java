package br.com.netlit.integration.util;

import java.lang.reflect.Field;
import java.text.Normalizer;

public class StringUtils {

	public static String removeAccent(String str) {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	public static void normalizeObject(Class clazz, Object obj) throws IllegalArgumentException, IllegalAccessException {
		
		        Field[] fields = clazz.getDeclaredFields();
		        for (Field field : fields) {
		            
		            field.setAccessible(true);
		            if (String.class.equals(field.getType())) {
		            	
		            	String object = (String) field.get(obj);
		            	
		            	if (null == object) continue;

		            	object = removeAccent(object);
	            		object = object.toUpperCase();
		            	
		            	field.set(obj, object);
		            	
		        }
		    }
		
	}
}