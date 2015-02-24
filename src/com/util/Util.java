package com.util;

public class Util {
	
	public static void validateString(String str) {
		if (str == null) throw new NullPointerException();
		if (str.isEmpty()) throw new IllegalArgumentException();
	}
	
	public static void validateObject(Object object, String type) {
		if (object == null) throw new NullPointerException();
		Util.validateString(type);
		
		type = type.toLowerCase();
		
		if (type.equals("int")) {
			type = "integer";
		}
		
		// Validate type.
		if ((type.equals("string") && !(object instanceof String))
				|| (type.equals("integer") && !(object instanceof Integer))
				|| (type.equals("boolean") && !(object instanceof Boolean))
				|| (type.equals("double") && !(object instanceof Double))
				|| (type.equals("long") && !(object instanceof Long))) {
			
			throw new IllegalArgumentException("Illegal type for " + object);
		} 
	}
}
