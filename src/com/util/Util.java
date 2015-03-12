package com.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Useful methods to be used throughout the project.
 * @author Sam
 *
 */
public class Util implements Constants {
	
	/**
	 * Performs basic validations on a String.
	 * @param str
	 */
	public static void validateString(String str) {
		if (str == null) throw new NullPointerException("Passed value cannot be null");
		if (str.isEmpty()) {
			throw new IllegalArgumentException("The passed String is empty.");
		}
	}
	
	
	/**
	 * Validates an Object for type correctness.
	 * @param object
	 * @param type
	 */
	public static void validateObjectType(Object object, String type) {
		if (object == null) throw new NullPointerException();
		Util.validateString(type);
		
		type = type.toLowerCase();
		
		if (type.equals("int")) {
			type = INT;
		}
		
		// Validate type.
		if ((type.equals(STRING) && !(object instanceof String))
				|| (type.equals(INT) && !(object instanceof Integer))
				|| (type.equals(BOOLEAN) && !(object instanceof Boolean))
				|| (type.equals(DOUBLE) && !(object instanceof Double))
				|| (type.equals(LONG) && !(object instanceof Long))) {
			
			throw new IllegalArgumentException("Illegal type for " + object);
		} 
	}
	
	
	/**
	 * Recursively explores a data type validating every element for not null
	 * and not empty. Supports singleton objects, Collections, and Map.
	 * @param object
	 */
	public static void validateObject(Object object) {
		if (object == null) {
			throw new NullPointerException("object cannot be null");
		}
		
		if (object instanceof String) {
			String str = (String)object;
			if (str.isEmpty()) {
				throw new IllegalArgumentException("String cannot be empty");
			}
			
		} else if (object instanceof Collection<?>) {
			Collection<?> col = (Collection<?>)object;
			if (col.isEmpty()) {
				throw new IllegalArgumentException("Collection cannot be empty");
			}
			for (Object cur : col) {
				validateObject(cur);
			}
			
		} else if (object instanceof Map) {
			Map<?, ?> map = (Map<?, ?>)object;
			if (map.isEmpty()) {
				throw new IllegalArgumentException("Map cannot be empty");
			}
			for (Object cur : map.keySet()) {
				validateObject(cur);
			}
		}
	}
	
	
	/**
	 * Returns the object type in string form.
	 * @param object
	 */
	public static String getType(Object object) {
		if (object == null) throw new NullPointerException();
		if (object instanceof String) return STRING;
		if (object instanceof Integer) return INT;
		if (object instanceof Boolean) return BOOLEAN;
		if (object instanceof Double) return DOUBLE;
		if (object instanceof Long) return LONG;
		throw new IllegalArgumentException("Illegal type for " + object);
	}
	
	
	/**
	 *  Given a Map, appends a status report to it with the passed parameters.
	 * @param success true if operation succeeded, false otherwise.
	 * @param errorMessage 
	 * @param map
	 */
	public static void addStatus(boolean success, String errorMessage, Map<String, Object> map) {
		if (errorMessage == null) {
			throw new IllegalArgumentException("errorMessage cannot be null.");
		}
		
		if (map == null) {
			throw new NullPointerException("Map cannot be null.");
		}
		
		Map<String, Object> statusMap = new HashMap<String, Object>();
		statusMap.put(SUCCESS, success);
		statusMap.put(ERROR_MESSAGE, errorMessage);
		map.put(STATUS, statusMap);
	}
	
	
	/**
	 * Prepares a string that contains spaces to be sent over the wire.
	 */
	public static String accountForSpaces(String str) {
		validateString(str);
		String result = "";
		String delim = "%20";
		
		if (str.contains(" ")) {
			StringTokenizer tok = new StringTokenizer(str);
			
			
			while (tok.hasMoreTokens()) {
				result += tok.nextToken();
				result += delim;
			}
			
			return result.substring(0, result.length() - delim.length());
		}
		
		return str;
	}
	
	
	// To be used for testing purposes only.
	public static void main( String [] args ) {}
	
	
}
