package com.util;

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
		if (str == null) throw new NullPointerException();
		if (str.isEmpty()) {
			throw new IllegalArgumentException("The passed String is empty.");
		}
	}
	
	
	/**
	 * Validates an Object for type correctness.
	 * @param object
	 * @param type
	 */
	public static void validateObject(Object object, String type) {
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
}
